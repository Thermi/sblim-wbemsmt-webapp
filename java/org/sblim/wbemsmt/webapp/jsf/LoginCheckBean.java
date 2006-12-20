/**
 *  LoginCheckBean.java
 *
 * (C) Copyright IBM Corp. 2005
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE COMMON PUBLIC LICENSE
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Common Public License from
 * http://www.opensource.org/licenses/cpl1.0.php
 *
 * @author: Marius Kreis <mail@nulldevice.org>
 *
 * Contributors:
 *
 */

package org.sblim.wbemsmt.webapp.jsf;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.convert.IntegerConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.sblim.wbem.cim.CIMNameSpace;
import org.sblim.wbem.client.CIMClient;
import org.sblim.wbem.client.PasswordCredential;
import org.sblim.wbem.client.UserPrincipal;
import org.sblim.wbemsmt.bl.Cleanup;
import org.sblim.wbemsmt.exception.LoginException;
import org.sblim.wbemsmt.exception.WbemSmtException;
import org.sblim.wbemsmt.tasklauncher.CimomTreeNode;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig.CimomData;
import org.sblim.wbemsmt.tasklauncher.login.LoginCheck;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;
import org.sblim.wbemsmt.tools.runtime.RuntimeUtil;



public class LoginCheckBean extends WbemsmtWebAppBean implements LoginCheck,Cleanup
{
    public static final String SESSION_ATTRIBUTE_USE_SLP = "useSlp";

	/**
     * This logger is used to create internal log entries
     */
	private static final Logger logger = Logger.getLogger("org.sblim.wbemsmt.tasklauncher.jsf");
    
    private static final String HIDDEN_PASSWORD = "*****";
    
    private boolean loggedIn,loginDisabled = false;
    private String loginView = "login",
                   startView = "start",
                   logoutView = "login",
                   username,
                   port,
                   password,
                   hostname,
                   namespace;
    private Vector presets;
    private boolean useSlp;
    
    private TaskLauncherController taskLauncherController;
    private TreeSelectorBean treeSelector;
    
    private HtmlSelectOneMenu presetSelection;

	private String loginFilename;

	private int selectedValue = 0;

	private List presetValues = new ArrayList();


	private CIMClient cimClient;


	private CimomData cimomData;


	private CimomData selection;

    public LoginCheckBean()
    {
    	super();
    	final FacesContext fc = FacesContext.getCurrentInstance();
    	this.presetSelection = new HtmlSelectOneMenu();
    	presetSelection.setConverter(new IntegerConverter());
    	
		Iterator requestLocales = fc.getExternalContext().getRequestLocales();
    	while (requestLocales.hasNext())
    	{
			System.err.println(requestLocales.next().toString());
		}
    	
    	UISelectItems items = (UISelectItems) fc.getApplication().createComponent(UISelectItems.COMPONENT_TYPE);
		items.setValueBinding("value", fc.getApplication().createValueBinding("#{loginCheck.presetValues}"));
		presetSelection.getChildren().add(items);
    }
    
    public List getPresetValues()
    {
    	return presetValues;    	
    }

    public void setPresetValues(List presets) {
    	//do nothing but fullfill the java beans convention for use with lwc
	}

    
	public boolean isUseSlp() {
		return useSlp;
	}

	public void setUseSlp(boolean useSlp) {
		this.useSlp = useSlp;
	}

	private void fillPresets()
    {
		//a config file was found and loaded
    	try {
			if (this.taskLauncherController.getTaskLauncherConfig() != null)
			{
				this.presets = this.taskLauncherController.getTaskLauncherConfig().getCimomData();
				presetValues.clear();
				
				for(int i=0; i<presets.size(); i++)
				{
					TaskLauncherConfig.CimomData cimom = (CimomData) presets.get(i);
					logger.log(Level.INFO, "filling preset " + cimom.getHostname());
					presetValues.add(new SelectItem("" + i,cimom.getHostname()));
				}
				
				if(presets.size() > selectedValue)
				{
					setValuesFromCimomData((CimomData) presets.get(selectedValue),true);
					selection = (CimomData) presets.get(selectedValue);
				}
				else
				{
					selection = null;
				}
				
				loginDisabled = !this.taskLauncherController.getTaskLauncherConfig().getHasConfiguration();
			}
			else
			{
				loginDisabled = true;
			}
		} catch (WbemSmtException e) {
			logger.log(Level.SEVERE,"Cannot load Config ",e);
			loginDisabled = true;
		}
    }
    

    public boolean isLoginDisabled() {
		return loginDisabled;
	}

    public String getLoginDisabledMsg() throws WbemSmtException {
		String msg = bundle.getString("loginDisabled", new Object[]{new File(taskLauncherController.getTaskLauncherConfig().getConfigFile()).getAbsolutePath()});
		return msg;
	}

    public void setLoginDisabledMsg(String msg) {
    	//do nothing but fullfill the java beans convention for use with lwc
	}

    public void setLoginDisabled(boolean presetDisabled) {
		this.loginDisabled = presetDisabled;
	}

    public HtmlSelectOneMenu getPresetSelect()
    {
        logger.log(Level.INFO, presetSelection.getId());
    	return this.presetSelection;
    }
    
    public void setPresetSelect(HtmlSelectOneMenu menu)
    {
        this.presetSelection = menu;
    }

    public void presetChange(ValueChangeEvent e)
    {
    	selectedValue = ((Integer) e.getNewValue()).intValue();
    	presetSelection.setValue(new Integer(selectedValue));
		selection = (CimomData) presets.get(selectedValue);
    	setValuesFromCimomData(selection,true);
    	//FacesContext.getCurrentInstance().getResponseComplete();
    }
    
    private void setValuesFromCimomData(TaskLauncherConfig.CimomData data, boolean overwriteUserId)
    {
    	if (overwriteUserId)
    	{
    		this.username = data.getUser();
    	}
    	
    	this.hostname = data.getHostname();
    	this.namespace = data.getNamespace();
    	this.port = ""+data.getPort();
    }
    
    private CIMClient createCIMClient(boolean setCimClient) throws LoginException
    {
        CIMClient cimClient;
        try
        {
        	username = username == null ? "" : username; 
        	password = StringUtils.isEmpty(password) ? " " : password; 
        	hostname = hostname == null ? "" : hostname; 
        	port = port == null ? "" : port; 
        	namespace = namespace == null ? "" : namespace; 
        	
        	String url = "HTTP://" + hostname + ":" + port.trim() + namespace.trim();
        	
        	logger.info("Coonecting to " + url + " with user " + username);
        	
            cimClient = new CIMClient(new CIMNameSpace(url), new UserPrincipal(username.trim()), new PasswordCredential(password.toCharArray()));
            Enumeration enumeration = cimClient.enumerateClasses();
            if(enumeration == null)
            {
            	throw new LoginException(bundle.getString("cannot.connect.noElementsFound"));
            }
        }
        catch(Exception e)
        {
            if (e instanceof LoginException) {
            	LoginException exception = (LoginException) e;
            	throw exception;
			}
            else
            {
            	throw new LoginException(bundle.getString("internal.error"),e);
            }
        }
        if (setCimClient)
        {
        	try {
				this.taskLauncherController.init(hostname, cimClient,useSlp);
				treeSelector.setTaskLauncherController(hostname,taskLauncherController);
			}catch (WbemSmtException e) {
				throw new LoginException(bundle.getString("internal.error"),e);
			}
        }
        return cimClient;
    }
    
    public void login(ActionEvent event)
    {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			session.setAttribute(RuntimeUtil.RUNTIME_MODE,RuntimeUtil.MODE_SINGLE);
			logger.log(Level.INFO, "Runtime mode: " + RuntimeUtil.MODE_SINGLE);
			login();
		} catch (WbemSmtException e) {
			JsfUtil.handleException(e);
		}
    }
    
    public void login() throws LoginException
    {
        // authenticating user
        //HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        
    	this.cimClient = this.createCIMClient(true); 
    	
        logger.log(Level.INFO, "connection ok, good authentication, cimclient created");
        this.loggedIn = true;
        logger.log(Level.INFO, "loggin in user: " + this.username);
    }
    
    
    public boolean isLoggedIn()
    {
        return this.loggedIn;
    }
    
    /**
     * Used by the Startscreen
     * @return
     */
    public String loginAction()
    {
        if(this.isLoggedIn())
        {
        	return startView;
        }
        else
        {
        	return loginView;
        }
    }
    
    public String logoutAction()
    {
        this.logout();
        return logoutView;
    }
    
    public void logout()
    {
        logger.log(Level.INFO, "Logging out...");
        this.loggedIn = false;
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		String runtimeMode =(String) session.getAttribute(RuntimeUtil.RUNTIME_MODE);
        session.invalidate();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(RuntimeUtil.RUNTIME_MODE,runtimeMode);
        javax.faces.context.FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * Used by the Login Action withion the Application.
     * @return
     */
    public String loginActionEmbedded()
    {
    	try {
			CimomTreeNode treeNode = (CimomTreeNode)treeSelector.getSelectedTaskLauncherTreeNode();
			treeNode.setSlpLoader(useSlp ? taskLauncherController.getSlpLoader() : null);
			setValuesFromCimomData(treeNode.getCimomData(),false);
			CIMClient client = createCIMClient(false);
			treeNode.setCimClient(client);
			treeNode.getCimomData().setUser(username);
			treeNode.updateName();
			treeNode.buildTree();
			treeNode.readSubnodes(true);
			return startView;
		} catch (WbemSmtException e) {
			JsfUtil.handleException(e);
			return startView;
		}
    }
    public String logoutActionEmbedded()
    {
    	try {
			CimomTreeNode treeNode = (CimomTreeNode)treeSelector.getSelectedTaskLauncherTreeNode();
			treeNode.setCimClient(null);
			treeNode.getCimomData().setUser(null);
			treeNode.updateName();
			treeNode.buildTree();
			treeNode.readSubnodes(true);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(bundle.getString("loggedOutFrom",new Object[]{treeNode.getCimomData().getInfo()}),""));
			return "cimomLogin";
		} catch (WbemSmtException e) {
			JsfUtil.handleException(e);
			return "";
		}
    }
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getNamespace()
    {
        return namespace;
    }

    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    public void setPassword(String password)
    {
        if(!password.equals(HIDDEN_PASSWORD))
        this.password = password;
    }
    
    public String getPassword()
    {
        if(password != null && password.length() > 0)
            return HIDDEN_PASSWORD;
        else
            return "";
    }

    public void setLoginView(String loginView)
    {
        this.loginView = loginView;
    }

    public void setStartView(String startView)
    {
        this.startView = startView;
    }
    
    public void setLogoutView(String logoutView)
    {
        this.logoutView = logoutView;
    }

    public void setTaskLauncherController(TaskLauncherController controller) throws WbemSmtException
    {
        this.taskLauncherController = controller;
        fillPresets();
    }
    
    public void setTreeSelector(TreeSelectorBean treeSelector) {
		this.treeSelector = treeSelector;
	}

	public void setLoginFilename(String filename)
    {
		this.loginFilename = filename;
    }
    
    public String forward()
    {
		try {
			
//			JsfBase.addMessage(new Message(ErrCodes.MSG_ADDED_HOST,Message.INFO,"testMessage1"));
//			JsfBase.addMessage(new Message(ErrCodes.MSG_ABOVE_MAX,Message.INFO,"testMessage2"));
//			JsfBase.addMessage(new Message(ErrCodes.MSG_BELOW_MIN,Message.SUCCESS,"testMessage3"));
//			JsfBase.addMessage(new Message(ErrCodes.MSG_CANNOT_CONVERT,Message.SUCCESS,"testMessage4"));
//			JsfBase.addMessage(new Message(ErrCodes.MSG_CANNOT_LOAD_SLP,Message.WARNING,"testMessage5"));
//			JsfBase.addMessage(new Message(ErrCodes.MSG_CANNOT_SAVE,Message.WARNING,"testMessage6"));
			
			File f = new File(loginFilename);
			logger.info("Reading LoginData from " + f.getAbsolutePath());
	        XMLDecoder d = new XMLDecoder(
                   new BufferedInputStream(
                       new FileInputStream(f)));
 	        TaskLauncherConfig.CimomData data = (TaskLauncherConfig.CimomData)d.readObject();
			
			Vector configs = taskLauncherController.getTaskLauncherConfig().getTreeConfigDataByHostname(data.getHostname());
			data.setTreeConfigs(configs);
			setValuesFromCimomData(data,true);
			this.password = (String) d.readObject();
			d.close();
			login();
		} catch (Exception e) {
			JsfUtil.handleException(e);
		}
    	if (loggedIn)
    	{
    		return startView;
    	}
    	else
    	{
    		return loginView;
    	}
    }

    public String multiWithoutLogin()
    {
		try {
			this.cimClient = null;
			taskLauncherController.createTreeFactoriesMultiHost();
			treeSelector.setTaskLauncherController(TaskLauncherController.NAME_FOR_MULTI_CIMOM_TREE,taskLauncherController);
			//neded to get a standard tree with all the CIMOMs
			treeSelector.setCurrentTreeBacker(TaskLauncherController.NAME_FOR_MULTI_CIMOM_TREE);
			treeSelector.getCurrentTreeBacker().updateTree();
			loggedIn = true;
		} catch (Exception e) {
			JsfUtil.handleException(e);
		}
    	if (loggedIn)
    	{
    		return startView;
    	}
    	else
    	{
    		return loginView;
    	}
    }

	public void reloadLoginSettings() throws WbemSmtException {
		fillPresets();
	}

	/**
	 * Only set in SingleMode
	 * @return
	 * @see RuntimeUtil#MODE_SINGLE
	 */
	public CIMClient getCimClient() {
		return cimClient;
	}
	
	public void setCimClient(CIMClient cimClient) {
		this.cimClient = cimClient;
	}

	public void setCimomData(CimomData cimomData) {
		this.cimomData = cimomData;
	}

	public CimomData getCimomData() {
		return cimomData;
	}
	
	public String getCimomName() {
		return cimomData.getInfo();
	}
	
	public void setCimomName()
	{
    	//do nothing but fullfill the java beans convention for use with lwc
	}

	public void cleanup() {
		treeSelector = null;		
		taskLauncherController = null;
	}
	
	
	
	

}