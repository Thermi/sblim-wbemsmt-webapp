 /** 
  * AdminBean.java
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
  * @author: Michael Bauschert <Michael.Bauschert@de.ibm.com>
  *
  * Contributors: 
  * 
  * Description: Managed Bean for the JSF tasklauncher admin console
  * 
  */
package org.sblim.wbemsmt.webapp.jsf.admin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;

import org.sblim.wbemsmt.bl.ErrCodes;
import org.sblim.wbemsmt.bl.adapter.Message;
import org.sblim.wbemsmt.exception.WbemSmtException;
import org.sblim.wbemsmt.tasklauncher.CustomTreeConfig;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig.TreeConfigData;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TasklauncherconfigDocument;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.CimomDocument.Cimom;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigDocument.Treeconfig;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigReferenceDocument.TreeconfigReference;
import org.sblim.wbemsmt.tools.jsf.JsfBase;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;
import org.sblim.wbemsmt.tools.slp.SLPLoader;
import org.sblim.wbemsmt.tools.slp.SLPUtil;
import org.sblim.wbemsmt.webapp.jsf.WbemsmtWebAppBean;

public class AdminBean extends WbemsmtWebAppBean {

	private List hostTable;
	private List serviceXmls;
	private TasklauncherconfigDocument taskLauncherDoc;
	private File loadedFile;
    private TaskLauncherController taskLauncherController;
    private boolean welcomeSettingsEnabled;
    private boolean welcomeTasksEnabled;
    
	/**
	 * True if the slp config is shown
	 */
	private boolean slpMode = false;
	
	public final static String NEW_HOST = "<enter new Hostname>";
	public final static String NEW_SERVICE = "<enter file for new service>";
	
	private String newService = null;
	
	protected static final Logger logger = Logger.getLogger(AdminBean.class.getName());
	
	private SLPLoader slpLoader;
	private Treeconfig[] treeconfigArray;
	/**
	 * @throws WbemSmtException 
	 * 
	 */
	public AdminBean() throws WbemSmtException
	{
		super();
		init();
		
	}
	
	public SLPLoader getSlpLoader() {
		return slpLoader;
	}

	public void setSlpLoader(SLPLoader slpLoader) {
		this.slpLoader = slpLoader;
	}
	
	

	public boolean isSlpMode() {
		return slpMode;
	}

	public void setSlpMode(boolean slpMode) {
		this.slpMode = slpMode;
	}

	public void init() throws WbemSmtException {
		
		if (taskLauncherController != null)
		{
			loadedFile = new File(taskLauncherController.getTaskLauncherConfig().getConfigFile());
			
			logger.info("Loading config from " + loadedFile.getAbsolutePath());
			
			if (loadedFile.exists())
			{
				try {
					taskLauncherDoc = TasklauncherconfigDocument.Factory.parse(loadedFile);
					treeconfigArray = taskLauncherController.getTaskLauncherConfig().getTasklauncherConfigDoc().getTasklauncherconfig().getTreeconfigArray();
				} catch (Exception e) {
					throw new WbemSmtException("Cannot load config from " + loadedFile.getAbsolutePath(),e);
				} 
			}
			else
			{
				throw new WbemSmtException("Cannot load config from " + loadedFile.getAbsolutePath() + " File doesn't exist");
			}
			hostTable = null;
			slpMode = false;
		}
	}
	
	public String reloadFromFile() throws WbemSmtException
	{
		init();
		return "";
	}
	
	public String saveHost()
	{
		try {
			for (int i=hostTable.size()-1; i >= 0 ; i--)
			{
				HostEntry hostEntry = (HostEntry) hostTable.get(i);
				if ((hostEntry.isNew() || hostEntry.isAddToFile()) && !NEW_HOST.equals(hostEntry.hostname))
				{
					logger.info("Creating new host " + hostEntry.getHostname());
					Cimom cimom = taskLauncherDoc.getTasklauncherconfig().addNewCimom();
					hostEntry.setCimom(cimom);
					hostEntry.setNew(false);
					
					updateCimom(cimom, hostEntry);

					//add a new Host
					hostTable.add(new HostEntry(taskLauncherController.getTaskLauncherConfig(), getServices()));				
				}
				else if (hostEntry.getCimom() != null)
				{
					Cimom cimom = hostEntry.getCimom();
					hostEntry.setCimom(cimom);

					updateCimom(cimom, hostEntry);
				}
			}
			
			save();
			init();
		} catch (Exception e) {
			handleSaveException(e);
		}
		return "adminHost";
	}

	public String saveSelectedSlpHosts()
	{
		try {
			
			List newHosts = new ArrayList();
			
			for (int i=hostTable.size()-1; i >= 0 ; i--)
			{
				HostEntry hostEntry = (HostEntry) hostTable.get(i);
				if (hostEntry.isAddToFile())
				{
					logger.info("Added host from slp: " + hostEntry.getHostname());
					JsfBase.addMessage(Message.create(ErrCodes.MSG_ADDED_HOST, Message.INFO, bundle, "addedHostFromSlp",new Object[]{hostEntry.getHostname()}));
					newHosts.add(hostEntry);				
				}
			}
			//reload from file
			init();
			//add the hosts
			//the last element is the "enter a new host" - host. Add the newHosts before that one
			List hosts = getHostTable();
			Object addNewHostItem = hosts.remove(hosts.size()-1);
			hosts.addAll(newHosts);
			hosts.add(addNewHostItem);
			
			//save the hosts
			saveHost();
		} catch (Exception e) {
			handleSaveException(e);
		}
		return "adminHost";
	}

	private void updateCimom(Cimom cimom, HostEntry hostEntry) {
		
		cimom.setHostname(hostEntry.getHostname());
		cimom.setNamespace(hostEntry.getNamespace());
		cimom.setPort(hostEntry.port);
		cimom.setUser(hostEntry.user);

		while (cimom.getTreeconfigReferenceArray().length > 0)
		{
			cimom.removeTreeconfigReference(0);
		}
		List services = hostEntry.getServices();
		for (Iterator iter = services.iterator(); iter.hasNext();) {
			ServiceInHost serviceInHost = (ServiceInHost) iter.next();
			if (serviceInHost.isEnabled())
			{
				TreeconfigReference reference = cimom.addNewTreeconfigReference();
				reference.setName(serviceInHost.getService());
			}
		}
	}

	public String deleteHosts()
	{
		try {
			for (int i=hostTable.size()-1; i >= 0 ; i--)
			{
				HostEntry hostEntry = (HostEntry) hostTable.get(i);
				if (hostEntry.isDelete())
				{
					logger.info("Deleting host " + hostEntry.getHostname());
					JsfBase.addMessage(Message.create(ErrCodes.MSG_REMOVED_HOST, Message.INFO, bundle, "removedHost",new Object[]{hostEntry.getHostname()}));
					taskLauncherDoc.getTasklauncherconfig().removeCimom(i);
					hostTable.remove(i);
				}
			}
			
			save();
			init();
		} catch (Exception e) {
			handleSaveException(e);
		}

		return "adminHost";
	}

	private void save() throws IOException
	{
		logger.info("saving changes to " + loadedFile.getAbsolutePath());
		taskLauncherDoc.save(loadedFile);
	}

	public List getHostTable()
	{
		if (hostTable == null)
		{
			hostTable = new ArrayList();
			try {
				String[] services = getServices();
				
				Cimom[] cimomArray = taskLauncherDoc.getTasklauncherconfig().getCimomArray();
				for (int i = 0; i < cimomArray.length; i++) {
					Cimom cimom = cimomArray[i];
					hostTable.add(new HostEntry(taskLauncherController.getTaskLauncherConfig(), cimom,services));
				}
				if (!slpMode)
				{
					hostTable.add(new HostEntry(taskLauncherController.getTaskLauncherConfig(),services));
				}
			} catch (WbemSmtException e) {
				JsfUtil.handleException(e);
			}
		}
		return hostTable;
	}

	public List getHostTableForDisplay()
	{
		List result = getHostTable();
		if (result.size() > 0)
		{
			HostEntry entry = (HostEntry) result.get(result.size()-1);
			if (entry.getHostname().equals(NEW_HOST))
			{
				result.remove(result.size()-1);
			}
		}
		hostTable  = null;
		return result;
	}
	
	public String[] getServices() throws WbemSmtException
	{
		 String[] result = new String[treeconfigArray.length];
		 for (int i = 0; i < treeconfigArray.length; i++) {
			Treeconfig treeconfig = treeconfigArray[i];
			result[i] = treeconfig.getName();
		 }
		 return result;
	}

	public List getTasks() throws WbemSmtException
	{
		 List result = new ArrayList();
		 for (int i = 0; i < treeconfigArray.length; i++) {
			Treeconfig treeconfig = treeconfigArray[i];

			TreeConfigData treeConfigDataByTaskname = taskLauncherController.getTaskLauncherConfig().getTreeConfigDataByTaskname(treeconfig.getName());
			Boolean installed = new Boolean(treeConfigDataByTaskname != null && 
							new CustomTreeConfig(treeConfigDataByTaskname).isLoaded());
			result.add(new Task(treeconfig.getName(),true,installed.booleanValue()));
		 }
		 return result;
	}

	public String getNewService() {
		return newService;
	}

	public void setNewService(String newService) {
		this.newService = newService;
	}

	private void handleSaveException(Exception e) {
		logger.log(Level.SEVERE,"Cannot save to file " + loadedFile.getAbsolutePath(),e);
		JsfBase.addMessage(new Message(ErrCodes.MSG_CANNOT_SAVE, Message.WARNING, bundle.getString(ErrCodes.MSG_CANNOT_SAVE,"cannot.save", new Object[]{loadedFile.getAbsolutePath()})));
	}
	
	
	public List getServiceXmls() {
		if (serviceXmls == null || serviceXmls.size() == 0)
		{
			serviceXmls = new ArrayList();
			
			try {
				File[] taskXMLs = taskLauncherController.getTaskLauncherConfig().getTaskXMLs();
				for (int i = 0; i < taskXMLs.length; i++) {
					serviceXmls.add(new SelectItem(taskXMLs[i].getAbsolutePath()));
				}
			
			} catch (Exception e1) {
				logger.log(Level.SEVERE,"Cannot task-config.xmls ",e1);
			}
		}
		return serviceXmls;
	}

	public void setServiceXmls(List serviceXmls) {
		this.serviceXmls = serviceXmls;
	}

	public boolean isAdminEnabled() throws WbemSmtException
	{
		File f = new File (taskLauncherController.getTaskLauncherConfig().getConfigFile());
		if (f.exists())
		{
			try {
				TasklauncherconfigDocument document = TasklauncherconfigDocument.Factory.parse(f);
				return document.getTasklauncherconfig() != null;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
    public String getAdminDisabledMsg() throws WbemSmtException {
		String msg = bundle.getString("adminDisabled", new Object[]{new File(taskLauncherController.getTaskLauncherConfig().getConfigFile()).getAbsolutePath()});
		return msg;
	}
	
    public String loadSlpConfiguration()
    {
		if (slpLoader.getCanFindHosts())
		{
	    	TasklauncherconfigDocument result = SLPUtil.readFromSlp(slpLoader,taskLauncherDoc.getTasklauncherconfig().getTreeconfigArray());
			
			//reload the displayed hosts
			hostTable = null;
			taskLauncherDoc = result;
			slpMode = true;
		}
		else
		{
			JsfBase.addMessage(new Message(ErrCodes.MSG_CANNOT_LOAD_SLP, Message.WARNING, bundle.getString(ErrCodes.MSG_CANNOT_LOAD_SLP,"cannot.load.slp.conf")));
		}
    	return "";
    }

	public TaskLauncherController getTaskLauncherController() {
		return taskLauncherController;
	}

	public void setTaskLauncherController (
			TaskLauncherController taskLauncherController) throws WbemSmtException {
		this.taskLauncherController = taskLauncherController;
		init();
	}

	public boolean isWelcomeSettingsEnabled() {
		return welcomeSettingsEnabled;
	}

	public void setWelcomeSettingsEnabled(boolean welcomeSettingsEnabled) {
		this.welcomeSettingsEnabled = welcomeSettingsEnabled;
	}

	public boolean isWelcomeTasksEnabled() {
		return welcomeTasksEnabled;
	}

	public void setWelcomeTasksEnabled(boolean welcomeTasksEnabled) {
		this.welcomeTasksEnabled = welcomeTasksEnabled;
	}
	
	
	
}

