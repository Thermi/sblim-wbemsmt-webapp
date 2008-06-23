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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.sblim.wbemsmt.bl.cleanup.Cleanup;
import org.sblim.wbemsmt.bl.messages.ErrCodes;
import org.sblim.wbemsmt.bl.messages.Message;
import org.sblim.wbemsmt.bl.tree.CustomTreeConfig;
import org.sblim.wbemsmt.exception.WbemsmtException;
import org.sblim.wbemsmt.session.jsf.WebSessionManger;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig.TreeConfigData;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TasklauncherconfigDocument;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.CimomDocument.Cimom;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.ConfigurationDefinitionDocument.ConfigurationDefinition;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.ConfigurationValueDocument.ConfigurationValue;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigDocument.Treeconfig;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigReferenceDocument.TreeconfigReference;
import org.sblim.wbemsmt.tools.jsf.JsfBase;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;
import org.sblim.wbemsmt.tools.jsf.WbemsmtCookieUtil;
import org.sblim.wbemsmt.tools.slp.SLPLoader;
import org.sblim.wbemsmt.tools.slp.SLPUtil;
import org.sblim.wbemsmt.webapp.jsf.WbemsmtWebAppBean;

public class AdminBean extends WbemsmtWebAppBean implements Cleanup {

	private static final String COOKIE_KEY_BULKCHANGES = "WBEMSMT-ADMIN-BULKCHANGES";
	public static final String CLICK_BLIND_BUTTON_HANDLER = "if (document.getElementById('adminForm:bulkChanges').checked == false) {document.getElementById('adminForm:blindSubmit').click();};";
	private List hostTable;
	private List serviceXmls;
	private TasklauncherconfigDocument taskLauncherDoc;
	private File loadedFile;
    private TaskLauncherController taskLauncherController;
    private boolean welcomeSettingsEnabled;
    private boolean welcomeTasksEnabled;
    
    private static String activeSessionId = null;
    
    //true if the user wants no onBlur and onClick events
    private boolean bulkChanges = false;
    
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
	private boolean allHosts = true;
	private HostEntry selectedHost;
	/**
	 * @throws WbemsmtException 
	 * 
	 */
	public AdminBean() throws WbemsmtException
	{
		super();
		init();

		Cookie cookie = WbemsmtCookieUtil.getCookie(COOKIE_KEY_BULKCHANGES);
		if (cookie != null)
		{
			bulkChanges = "true".equalsIgnoreCase(cookie.getValue());			
		}
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

	public void init() throws WbemsmtException {
		
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
					throw new WbemsmtException(WbemsmtException.ERR_FAILED, "Cannot load config from " + loadedFile.getAbsolutePath(),e);
				} 
			}
			else
			{
				throw new WbemsmtException(WbemsmtException.ERR_FAILED,"Cannot load config from " + loadedFile.getAbsolutePath() + " File doesn't exist");
			}
			hostTable = null;
			slpMode = false;
		}
	}
	
	public String reloadFromFile() throws WbemsmtException
	{
		init();
		return "";
	}
	
	public String saveHostSilent()
	{
		return saveHost(true);
	}

	public String saveHost()
	{
		return saveHost(false);
	}

	public String saveHost(boolean silent)
	{
		
		try {
			if (allHosts)
			{
				for (int i=hostTable.size()-1; i >= 0 ; i--)
				{
					HostEntry hostEntry = (HostEntry) hostTable.get(i);
					if ((hostEntry.isNew() || hostEntry.isAddToFile()) && !NEW_HOST.equals(hostEntry.hostname))
					{
						logger.info("Creating new host " + hostEntry.getHostname());
						Cimom cimom = taskLauncherDoc.getTasklauncherconfig().addNewCimom();
						hostEntry.setCimom(cimom);
						hostEntry.setNew(false);
						if (!silent)
							JsfBase.addMessage(Message.create(ErrCodes.MSG_ADDED_HOST, Message.INFO, bundle, "addedHost",new Object[]{hostEntry.getHostname()}));					
						
						updateCimom(cimom, hostEntry);
						
						//add a new Host
						hostTable.add(new HostEntry(this,taskLauncherController.getTaskLauncherConfig(), getTreeconfigReferences()));				
					}
					else if (hostEntry.getCimom() != null)
					{
						Cimom cimom = hostEntry.getCimom();
						hostEntry.setCimom(cimom);
						
						updateCimom(cimom, hostEntry);
						if (!silent)
							JsfBase.addMessage(Message.create(ErrCodes.MSG_SAVE_SUCCESS, Message.INFO, bundle, "save.success"));					
					}
				}
			}
			else
			{
				Cimom cimom = selectedHost.getCimom();
				updateCimom(cimom, selectedHost);
				if (!silent)
					JsfBase.addMessage(Message.create(ErrCodes.MSG_SAVE_SUCCESS, Message.INFO, bundle, "save.success"));					
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
			saveHost(false);
		} catch (Exception e) {
			handleSaveException(e);
		}
		return "adminHost";
	}

	private void updateCimom(Cimom cimom, HostEntry hostEntry) {
		
		cimom.setHostname(hostEntry.getHostname());
		cimom.setPort(hostEntry.getPortAsInt());
		cimom.setUser(hostEntry.user);
		cimom.setProtocol(StringUtils.isNotEmpty(hostEntry.protocol) ? hostEntry.protocol : TaskLauncherConfig.DEFAULT_PROTOCOL );

		TreeconfigReference[] treeconfigReferenceArray = cimom.getTreeconfigReferenceArray();
		Map referencesByName = new HashMap();
		
		for (int i = 0; i < treeconfigReferenceArray.length; i++) {
			TreeconfigReference reference = treeconfigReferenceArray[i];
			referencesByName.put(reference.getName(), reference);
		}
		List services = hostEntry.getServices();
		for (Iterator iter = services.iterator(); iter.hasNext();) {
			ServiceInHost serviceInHost = (ServiceInHost) iter.next();
			TreeconfigReference refInCimom = (TreeconfigReference) referencesByName.get(serviceInHost.getReference().getName());
			
			//If checkbox was selected but reference not exists -> add the ref from the 
			if (serviceInHost.isEnabled())
			{
				if ( refInCimom == null )
				{
					refInCimom = cimom.addNewTreeconfigReference();
					refInCimom.set(serviceInHost.getReference().copy());
				}
			
				refInCimom.setNamespace(serviceInHost.getNamespace());
			}
			//If checkbox was not selected but reference exists -> delete the ref from the cimom
			else if (!serviceInHost.isEnabled() && refInCimom != null)
			{
				deleteRef(cimom,refInCimom);
			}
			
		}
	}

	/**
	 * Delete the ref if contained in the cimom
	 * @param cimom
	 * @param refInCimom
	 */
	private void deleteRef(Cimom cimom, TreeconfigReference refInCimom) {
		TreeconfigReference[] treeconfigReferenceArray = cimom.getTreeconfigReferenceArray();
		for (int i = 0; i < treeconfigReferenceArray.length; i++) {
			TreeconfigReference reference = treeconfigReferenceArray[i];
			if (reference == refInCimom)
			{
				cimom.removeTreeconfigReference(i);
				return;
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
		
		if (allHosts)
		{
			return "adminHost";
		}
		else
		{
			//if there is only one item we can go back to the overview
			allHosts = true;
			return "adminIndex";
		}
		

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
				Cimom[] cimomArray = taskLauncherDoc.getTasklauncherconfig().getCimomArray();
				for (int i = 0; i < cimomArray.length; i++) {
					Cimom cimom = cimomArray[i];
					hostTable.add(new HostEntry(this,taskLauncherController.getTaskLauncherConfig(), cimom,getTreeconfigReferences()));
				}
				if (!slpMode)
				{
					hostTable.add(new HostEntry(this,taskLauncherController.getTaskLauncherConfig(),getTreeconfigReferences()));
				}
			} catch (WbemsmtException e) {
				JsfUtil.handleException(e);
			}
		}
		
		if (allHosts)
		{
			return hostTable;
		}
		else
		{
			for (Iterator iterator = hostTable.iterator(); iterator.hasNext();) {
				HostEntry entry = (HostEntry) iterator.next();
				if (entry.getHostname().equals(selectedHost.getHostname()))
				{
				    selectedHost = entry;
					List result = new ArrayList();
					result.add(entry);
					return result;
				}
			}
			return null;
		}

		
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
	
	public String[] getServices() throws WbemsmtException
	{
		 String[] result = new String[treeconfigArray.length];
		 for (int i = 0; i < treeconfigArray.length; i++) {
			Treeconfig treeconfig = treeconfigArray[i];
			result[i] = treeconfig.getName();
		 }
		 return result;
	}

	/**
	 * Get new Treeconfig references with all the the configuation values
	 * @return
	 * @throws WbemsmtException
	 */
	public TreeconfigReference[] getTreeconfigReferences() throws WbemsmtException
	{
		TreeconfigReference[] result = new TreeconfigReference[treeconfigArray.length];
		 for (int i = 0; i < treeconfigArray.length; i++) {
			Treeconfig treeconfig = treeconfigArray[i];
			result[i] = TreeconfigReference.Factory.newInstance();
			result[i].setName(treeconfig.getName());
			for (int j = 0; j < treeconfig.getConfigurationDefinitionArray().length; j++) {
				ConfigurationDefinition definition = treeconfig.getConfigurationDefinitionArray()[j];
				ConfigurationValue value = result[i].addNewConfigurationValue();
				value.setName(definition.getName());
				value.setValue(definition.getDefaultValue());
			}
			
		 }
		 return result;
	}	
	
	/**
	 * Get the Configuration Definitions for a specific task
	 * @return
	 * @throws WbemsmtException
	 */
	public ConfigurationDefinition[] getConfigDefinitionsByTaskname(String taskname)
	{
		 for (int i = 0; i < treeconfigArray.length; i++) {
			if (treeconfigArray[i].getName().equals(taskname))
			{
				return treeconfigArray[i].getConfigurationDefinitionArray();
			}
		 }
		 return null;
	}	

	/**
	 * Get the Configuration Definitions for a specific task
	 * @return
	 * @throws WbemsmtException
	 */
	public Treeconfig getTreeconfigByTaskname(String taskname)
	{
		 for (int i = 0; i < treeconfigArray.length; i++) {
			if (treeconfigArray[i].getName().equals(taskname))
			{
				return treeconfigArray[i];
			}
		 }
		 return null;
	}	
	
	public List getTasks() throws WbemsmtException
	{
		 List result = new ArrayList();
		 for (int i = 0; i < treeconfigArray.length; i++) {
			Treeconfig treeconfig = treeconfigArray[i];

			TreeConfigData treeConfigDataByTaskname = taskLauncherController.getTaskLauncherConfig().getTreeConfigDataByTaskname(treeconfig.getName());
			Boolean installed = new Boolean(treeConfigDataByTaskname != null && 
								CustomTreeConfig.isLoaded(treeConfigDataByTaskname));
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

	public boolean isAdminEnabled() throws WbemsmtException
	{
		File f = new File (taskLauncherController.getTaskLauncherConfig().getConfigFile());
		if (f.exists())
		{
			try {
				TasklauncherconfigDocument document = TasklauncherconfigDocument.Factory.parse(f);
				boolean result = document.getTasklauncherconfig() != null;
				return result;
			} catch (Exception e) {
				return false;
			}
		}
		return false;
	}
	
    public String getAdminDisabledMsg() throws WbemsmtException {
		String msg = bundle.getString("adminDisabled", new Object[]{new File(taskLauncherController.getTaskLauncherConfig().getConfigFile()).getAbsolutePath()});
		return msg;
	}
	
    public String loadSlpConfiguration()
    {
		if (slpLoader.getCanFindHosts())
		{
	    	TasklauncherconfigDocument result = SLPUtil.readFromSlp(slpLoader,treeconfigArray);
			
	    	while (result.getTasklauncherconfig().getTreeconfigArray().length > 0)
	    	{
	    		result.getTasklauncherconfig().removeTreeconfig(0);
	    	}
	    	
			//reload the displayed hosts
			hostTable = null;
			taskLauncherDoc = result;
			slpMode = true;
		}
		else
		{
			JsfBase.addMessage(new Message(ErrCodes.MSG_CANNOT_LOAD_SLP, Message.WARNING, bundle.getString(ErrCodes.MSG_CANNOT_LOAD_SLP,"cannot.load.slp.conf")));
		}
		
    	if (canEdit(true))
    	{
    		return "adminHost";
    	}
    	else
    	{
    		return "";
    	}
		
    	
    }

    /**
     * checks id the current session is the only one
     * @return true if the current session is the only one and the user can edit data
     * 
     * @param setActiveSessionId if this flag is true the methods marks the current session
     * as the session which is able to edit the config. Other sessions will be blicked
     */
	private boolean canEdit(boolean setActiveSessionId) {

		String sessionId = WebSessionManger.getCurrentWebSession().getId();
		
		if (activeSessionId == null)
		{
			if (setActiveSessionId)
			{
				activeSessionId = sessionId;
			}
			return true;
		}
		else if (activeSessionId.equals(sessionId))
		{
			return true;
		}
		else
		{
			JsfBase.addMessage(Message.create(ErrCodes.MSGDEF_EDITING_NOT_POSSIBLE,bundle));
			return false;
		}
	}

	public TaskLauncherController getTaskLauncherController() {
		return taskLauncherController;
	}

	public void setTaskLauncherController (
		TaskLauncherController taskLauncherController) throws WbemsmtException {
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
	
	public void selectSingleHost(ActionEvent event)
	{
		List children = event.getComponent().getChildren();
		UIParameter parameter = (UIParameter) children.get(0);
		allHosts = false;
		selectedHost = (HostEntry)parameter.getValue();
	}
	
	public void selectAllHosts(ActionEvent event)
	{
		allHosts = true;
	}

	public boolean isBulkChanges() {
		return bulkChanges;
	}

	public void setBulkChanges(boolean bulkChanges) {
		this.bulkChanges = bulkChanges;
		WbemsmtCookieUtil.addCookie(COOKIE_KEY_BULKCHANGES,""+bulkChanges);
	}
	
	public String getClickBlindButton()
	{
		return CLICK_BLIND_BUTTON_HANDLER;
	}

	
	public void cleanup() {
		
		String sessionId = WebSessionManger.getCurrentWebSession().getId();
		if (sessionId.equals(activeSessionId))
		{
			activeSessionId = null;
			logger.info("User's session with session id " + sessionId + " was timed out. Admin console can be used to edit the configfuration by other users");
		}
	}
	
	public String editHosts()
	{
		if (canEdit(true))
		{
			return "adminHost";
		}
		else
		{
			return "adminIndex";
		}
	}
	
	public String showSummary()
	{
		activeSessionId = null;
		return "adminIndex";
	}

}

