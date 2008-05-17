/**
 *  LoginCheckBean.java
 *
 * © Copyright IBM Corp. 2005
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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.convert.IntegerConverter;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import javax.wbem.client.WBEMClient;

import org.apache.commons.lang.StringUtils;
import org.sblim.wbemsmt.bl.Cleanup;
import org.sblim.wbemsmt.bl.ErrCodes;
import org.sblim.wbemsmt.bl.adapter.Message;
import org.sblim.wbemsmt.bl.adapter.MessageUtil;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEvent;
import org.sblim.wbemsmt.exception.WbemsmtException;
import org.sblim.wbemsmt.exception.impl.LoginException;
import org.sblim.wbemsmt.exception.impl.userobject.LoginUserObject;
import org.sblim.wbemsmt.tasklauncher.CimomTreeNode;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig.CimomData;
import org.sblim.wbemsmt.tasklauncher.login.CimomLoginLogoutListener;
import org.sblim.wbemsmt.tasklauncher.login.LoginCheck;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigDocument.Treeconfig;
import org.sblim.wbemsmt.tools.jsf.JavascriptUtil;
import org.sblim.wbemsmt.tools.jsf.JsfBase;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;
import org.sblim.wbemsmt.tools.jsf.WbemsmtCookieUtil;
import org.sblim.wbemsmt.tools.jsf.WbemsmtCookieUtil.LoginData;
import org.sblim.wbemsmt.tools.resources.ResourceBundleManager;
import org.sblim.wbemsmt.tools.resources.WbemSmtResourceBundle;
import org.sblim.wbemsmt.tools.runtime.RuntimeUtil;
import org.sblim.wbemsmt.tools.slp.SLPLoader;
import org.sblim.wbemsmt.tools.slp.SLPUtil;
import org.sblim.wbemsmt.webapp.jsf.admin.HostEntry;



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
                   startView = "startPage",
                   logoutView = "login",
                   username,
                   port,
                   password,
                   hostname,
                   protocol;
    private Vector presets;
    public boolean useSlp;
    private boolean remindLoginData;
    
    private TaskLauncherController taskLauncherController;
    private TreeSelectorBean treeSelector;
    private ObjectActionControllerBean objectActionController;  
    
    private HtmlSelectOneMenu presetSelection;

	private String loginFilename;

	private int selectedValue = 0;

	private List presetValues = new ArrayList();


	private WBEMClient cimClient;


	private CimomData cimomData;


	private CimomData selection;

	/**
	 * The LoginData from the Cookie
	 */
	private LoginData loginDataFromCookie;

    public LoginCheckBean()
    {
    	super();
    	final FacesContext fc = FacesContext.getCurrentInstance();
    	this.presetSelection = new HtmlSelectOneMenu();
    	presetSelection.setConverter(new IntegerConverter());
    	
		loginDataFromCookie = WbemsmtCookieUtil.getLoginDataFromCookie();
		if (loginDataFromCookie != null)
		{
			setValuesFromCookie(loginDataFromCookie);
		}
		remindLoginData = loginDataFromCookie != null;
    	
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
					if (loginDataFromCookie == null)
					{
						setValuesFromCimomData((CimomData) presets.get(selectedValue),true);
					}
					
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
		} catch (WbemsmtException e) {
			logger.log(Level.SEVERE,"Cannot load Config ",e);
			loginDisabled = true;
		}
    }
    

    public boolean isLoginDisabled() {
		return loginDisabled;
	}

    public String getLoginDisabledMsg() throws WbemsmtException {
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
    
    
    private void setValuesFromCookie(LoginData loginData) {
    	this.username = loginData.getUser();
    	this.password = loginData.getPassword();
    	this.hostname = loginData.getHost();
    	this.port = loginData.getPort();
    	this.protocol = loginData.getProtocol();
    	this.useSlp = loginData.isUseSlp();
	}
    
    private void setValuesFromCimomData(TaskLauncherConfig.CimomData data, boolean overwriteUserId)
    {
    	if (overwriteUserId)
    	{
    		this.username = data.getUser();
    	}
    	
    	this.hostname = data.getHostname();
    	this.port = ""+data.getPort();
    }
    
    private WBEMClient createCIMClient(boolean setCimClient) throws WbemsmtException
    {
    	return createCIMClient(setCimClient,hostname,""+getPortAsInt(), protocol,  username,password,useSlp);
    }
    
    private WBEMClient createCIMClient(boolean setCimClient, String hostname, String port, String protocol, String username, String password, boolean useSlp) throws WbemsmtException
    {
//        CIMClient cimClient = null;
//        try
//        {
//        	username = username == null ? "" : username; 
//        	password = StringUtils.isEmpty(password) ? " " : password; 
//        	hostname = hostname == null ? "" : hostname; 
//        	port = port == null ? "" : port; 
//        	namespace = namespace == null ? "" : namespace.trim();
//        	protocol = StringUtils.isNotEmpty(protocol) ? protocol : TaskLauncherConfig.DEFAULT_PROTOCOL;
//        	
//        	String url = protocol.toLowerCase() + "://" + hostname + ":" + port.trim();
//        	
//        	logger.info("Coonecting to " + url + " with user " + username);
//        	
//        	WbemsmtSession.getSession().createCIMClientPool(hostname,port,username,password);
//        	
//        	
//            cimClient = WbemsmtSession.getSession().getCIMClientPool(hostname).getCIMClient(namespace);
//            Enumeration enumeration = cimClient.enumerateClasses();
//            if(enumeration == null)
//            {
//            	throw new LoginException(bundle.getString("cannot.connect.noElementsFound"),cimClient);
//            }
//        }
//        catch(Exception e)
//        {
//            if (e instanceof LoginException) {
//            	LoginException exception = (LoginException) e;
//            	throw exception;
//			}
//            else
//            {
//            	throw new LoginException(bundle.getString("internal.error"),e,cimClient);
//            }
//        }
        if (setCimClient)
        {
        	try {
				this.taskLauncherController.init(hostname, port, protocol, username,password ,useSlp);
				treeSelector.setTaskLauncherController(hostname,taskLauncherController);
			}catch (WbemsmtException e) {
	            WbemsmtException e1 = new LoginException(e, new LoginUserObject(username + "@" + protocol + "://" + hostname + ":" + port));
	            throw e1;
			}
        }
        return null;
    }
    
    /**
     * called by the startscreen
     * @param event
     */
    public void login(ActionEvent event)
    {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			session.setAttribute(RuntimeUtil.RUNTIME_MODE,RuntimeUtil.MODE_SINGLE);
			RuntimeUtil.getInstance().setMode(RuntimeUtil.MODE_SINGLE);
			logger.log(Level.INFO, "Runtime mode: " + RuntimeUtil.MODE_SINGLE);
			login();
		} catch (WbemsmtException e) {
			JsfUtil.handleException(e);
		}
    }
    
    public void login() throws WbemsmtException
    {
    	
    	//first do a cleanup so that there is no old tree if the login failes
    	treeSelector.cleanup();
    	
    	this.cimClient = this.createCIMClient(true); 
    	
        logger.log(Level.INFO, "connection ok, good authentication, cimclient created");
        this.loggedIn = true;
        logger.log(Level.INFO, "loggin in user: " + this.username);
        
        if (remindLoginData)
        {
        	loginDataFromCookie =  new WbemsmtCookieUtil.LoginData(username,password,hostname,port,protocol,useSlp);
        	WbemsmtCookieUtil.addLoginDataCookie(loginDataFromCookie);
        }
        else
        {
        	WbemsmtCookieUtil.removeLoginDataCookie();	
        }
        
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

        if (runtimeMode.equals(RuntimeUtil.MODE_SINGLE))
        {
    		loginDataFromCookie = WbemsmtCookieUtil.getLoginDataFromCookie();
    		if (loginDataFromCookie != null)
    		{
    			setValuesFromCookie(loginDataFromCookie);
    		}
    		remindLoginData = loginDataFromCookie != null;
        }
        

		
        javax.faces.context.FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * Used by the Login Action withion the Application.
     * This kind of Login is used for MultiHost-Login
     * @return
     */
    public String loginActionEmbedded()
    {
    		
    		List nodes = objectActionController.getCimomTreeNodesForLogin();
    		
    		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
    			try {
					CimomTreeNode treeNode = (CimomTreeNode) iter.next();
					String host = treeNode.getCimomData().getHostname();
					String user = treeNode.getCimomData().getUser();
					
					//check the password and set it to the node and the cimomdata
					String passwd = treeNode.getCimomData().getPassword() == null ? "" : treeNode.getCimomData().getPassword();
					treeNode.getCimomData().setPassword(passwd);
					
					boolean emptyPassword = treeNode.isEmptyPassword();
					boolean nodeUsesSlp = treeNode.isUseSlp();
					

					//handle the Node only if the user wants to use a empty password or he entered a password
					
					if (emptyPassword || StringUtils.isNotEmpty(passwd))
					{
						if (emptyPassword && StringUtils.isNotEmpty(passwd))
						{
							MessageUtil.addMessage(ErrCodes.MSGDEF_EMPTY_PASSWORD_AND_PASSWORD_SET, ResourceBundleManager.getCommonResourceBundle(),new Object[]{treeNode.getCimomData().getInfo()});
						}
						else
						{
							if (treeNode.isRemindPassword())
							{
								String passwordForCookie = emptyPassword ? "" : passwd;
								WbemsmtCookieUtil.addMultiLogonCookie(host, user, new WbemsmtCookieUtil.MultiHostLoginData(emptyPassword,nodeUsesSlp,passwordForCookie));
							}
							else
							{
								WbemsmtCookieUtil.removeMutliLogonCookie(host, user);
							}

							SLPLoader slpLoader = taskLauncherController.getSlpLoader();
							treeNode.setSlpLoader(nodeUsesSlp ? slpLoader : null);
							setValuesFromCimomData(treeNode.getCimomData(),false);
							
							
							try {
								if (nodeUsesSlp)
								{
									Treeconfig[] supportedTasksForHost = SLPUtil.getSupportedTasksForHost(slpLoader, host, taskLauncherController.getTaskLauncherConfig().getTreeconfig());
									treeNode.buildTree(supportedTasksForHost);
								}
								else
								{
									treeNode.buildTree();
								}
								treeNode.updateName();
								treeNode.readSubnodes(true);
							} catch (Exception e) {
								JsfUtil.handleException(e);
							}
						}
					}
					
    			} catch (Exception e) {
    				JsfUtil.handleException(e);
    			}
			}	
			return startView;
    }
    public String logoutActionEmbedded()
    {
    	try {
			CimomTreeNode treeNode = (CimomTreeNode)treeSelector.getSelectedTaskLauncherTreeNode();
			treeNode.logout();
			JsfBase.addMessage(Message.create(ErrCodes.MSG_LOGGED_OUT, Message.INFO, bundle, "loggedOutFrom",new Object[]{treeNode.getCimomData().getInfo()}));
			//do the same as if the user has clicked on the treeNode...
			return new CimomLoginLogoutListener().processEvent(new TaskLauncherTreeNodeEvent(this,treeNode,this,TaskLauncherTreeNodeEvent.TYPE_CLICKED));
		} catch (WbemsmtException e) {
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
		
		if ((TaskLauncherConfig.HTTP.equalsIgnoreCase(protocol) || protocol == null) 
			&& (""+TaskLauncherConfig.HTTP_PORT_DEFAULT).equals(port))
		{
			port = HostEntry.DEFAULT_TOKEN;
		}
		if (TaskLauncherConfig.HTTPS.equalsIgnoreCase(protocol) 
			&& (""+TaskLauncherConfig.HTTPS_PORT_DEFAULT).equals(port))
		{
			port = HostEntry.DEFAULT_TOKEN;
		}
		
		return  port;
	}

	public int getPortAsInt() {
		
		boolean useDefault = false;
		try {
			useDefault = HostEntry.DEFAULT_TOKEN.equalsIgnoreCase(port) || Integer.parseInt(port) == -1;
		} catch (NumberFormatException e) {
			logger.warning("port is no number (int) " + port);
			useDefault = true;
			port = HostEntry.DEFAULT_TOKEN;
		} 
		
		if (useDefault)
		{
			if (TaskLauncherConfig.HTTP.equalsIgnoreCase(protocol))
			{
				return TaskLauncherConfig.HTTP_PORT_DEFAULT;
			}
			else if (TaskLauncherConfig.HTTPS.equalsIgnoreCase(protocol))
			{
				return TaskLauncherConfig.HTTPS_PORT_DEFAULT;
			}
			else
			{
				logger.warning("Cannot handle protocol " + protocol);
				return TaskLauncherConfig.HTTP_PORT_DEFAULT;
			}
		}
		else
		{
			return Integer.parseInt(port);
		}
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
    
    public boolean isRemindLoginData() {
		return remindLoginData;
	}


	public void setRemindLoginData(boolean remindLoginData) {
		this.remindLoginData = remindLoginData;
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

    public void setTaskLauncherController(TaskLauncherController controller) throws WbemsmtException
    {
        this.taskLauncherController = controller;
        fillPresets();
    }
    
    public void setTreeSelector(TreeSelectorBean treeSelector) {
		this.treeSelector = treeSelector;
	}

    
    
	public void setObjectActionController(
			ObjectActionControllerBean objectActionController) {
		this.objectActionController = objectActionController;
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
			
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			session.setAttribute(RuntimeUtil.RUNTIME_MODE,RuntimeUtil.MODE_SINGLE);
			RuntimeUtil.getInstance().setMode(RuntimeUtil.MODE_SINGLE);
			logger.log(Level.INFO, "Runtime mode: " + RuntimeUtil.MODE_SINGLE);

			
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
			taskLauncherController.createTreeFactoriesMultiHost(useSlp);
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

	public void reloadLoginSettings() throws WbemsmtException {
		fillPresets();
	}

	/**
	 * Only set in SingleMode
	 * @return
	 * @see RuntimeUtil#MODE_SINGLE
	 */
	public WBEMClient getCimClient() {
		return cimClient;
	}
	
	public void setCimClient(WBEMClient cimClient) {
		this.cimClient = cimClient;
	}

	public void setCimomData(CimomData cimomData) {
		this.cimomData = cimomData;
	}

	public CimomData getCimomData() {
		return cimomData;
	}
	
	public String getCimomName() {
		return cimomData != null ? cimomData.getInfo() : "";
	}
	
	public void setCimomName()
	{
    	//do nothing but fullfill the java beans convention for use with lwc
	}

	public void cleanup() {
		treeSelector = null;		
		taskLauncherController = null;
	}
	
	public String getJavascriptShowWait()
	{
		WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(FacesContext.getCurrentInstance());
		if (cimomData == null)
		{
			return JavascriptUtil.getShowWaitCall(
					"'" + bundle.getString("login.to",new Object[]{""}) + " ' + " +
					"document.getElementById('connectFields:host').value",false);
		}
		else
		{
			return JavascriptUtil.getShowWaitCall(bundle.getString("login.to", new Object[]{getCimomName()}));
		}
	}
	
	public String getJavascriptShowWaitMulti()
	{
		return JavascriptUtil.getShowWaitCall(bundle.getString("login.to.multiple"));
	}
	
}