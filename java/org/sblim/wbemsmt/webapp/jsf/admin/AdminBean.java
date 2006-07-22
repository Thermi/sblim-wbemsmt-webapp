 /** 
  * AdminBean.java
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
  * @author: Michael Bauschert <Michael.Bauschert@de.ibm.com>
  *
  * Contributors: 
  * 
  * Description: TODO
  * 
  */
package org.sblim.wbemsmt.webapp.jsf.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.xmlbeans.XmlException;
import org.sblim.wbemsmt.exception.WbemSmtException;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TasklauncherconfigDocument;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.CimomDocument.Cimom;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigDocument.Treeconfig;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigReferenceDocument.TreeconfigReference;
import org.sblim.wbemsmt.webapp.jsf.WbemsmtWebAppBean;

public class AdminBean extends WbemsmtWebAppBean {

	private List hostTable;
	private List serviceTable;
	private TasklauncherconfigDocument taskLauncherDoc;
	private File loadedFile;
	
	public final static String NEW_HOST = "<enter new Hostname>";
	public final static String NEW_SERVICE = "<enter file for new service>";
	
	private String newService = NEW_SERVICE;
	
	protected static final Logger logger = Logger.getLogger(AdminBean.class.getName());
	
	/**
	 * @throws WbemSmtException 
	 * 
	 */
	public AdminBean() throws WbemSmtException
	{
		super();
		init();
		
	}

	private void init() throws WbemSmtException {
		loadedFile = new File(TaskLauncherConfig.getConfigFile());
		
		logger.info("Loading config from " + loadedFile.getAbsolutePath());

		if (loadedFile.exists())
		{
			try {
				taskLauncherDoc = TasklauncherconfigDocument.Factory.parse(loadedFile);
			} catch (Exception e) {
				logger.log(Level.SEVERE,"Cannot load config from " + loadedFile.getAbsolutePath(),e);
				taskLauncherDoc = TasklauncherconfigDocument.Factory.newInstance();
			} 
		}
		else
		{
			taskLauncherDoc = TasklauncherconfigDocument.Factory.newInstance();
		}
		hostTable = null;
		serviceTable = null;
	}
	
	public String saveHost()
	{
		try {
			for (int i=hostTable.size()-1; i >= 0 ; i--)
			{
				HostEntry hostEntry = (HostEntry) hostTable.get(i);
				if (hostEntry.isNew() && !NEW_HOST.equals(hostEntry.hostname))
				{
					logger.info("Creating new host " + hostEntry.getHostname());
					Cimom cimom = taskLauncherDoc.getTasklauncherconfig().addNewCimom();
					hostEntry.setCimom(cimom);
					hostEntry.setNew(false);
					
					updateCimom(cimom, hostEntry);

					//add a new Host
					hostTable.add(new HostEntry(getServices()));				
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

	private void updateCimom(Cimom cimom, HostEntry hostEntry) {
		
		cimom.setHostname(hostEntry.getHostname());
		cimom.setName(hostEntry.getHostname());
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

	public String deleteServices()
	{
		try {
			for (int i=serviceTable.size()-1; i >= 0 ; i--)
			{
				ServiceEntry entry = (ServiceEntry) serviceTable.get(i);
				if (entry.isDelete())
				{
					String taskName = entry.getName();
					logger.info("Deleting host " + taskName);
					taskLauncherDoc.getTasklauncherconfig().removeTreeconfig(i);
					serviceTable.remove(i);
					
					
					Cimom[] cimomArray = taskLauncherDoc.getTasklauncherconfig().getCimomArray();
					for (int j = 0; j < cimomArray.length; j++) {
						Cimom cimom = cimomArray[j];
						TreeconfigReference[] treeconfigReferenceArray = cimom.getTreeconfigReferenceArray();
						for (int k = treeconfigReferenceArray.length-1; k >= 0; k--) {
							TreeconfigReference reference = treeconfigReferenceArray[k];
							if (reference.getName().equals(taskName))
							{
								cimom.removeTreeconfigReference(k);
							}
						}
					}
					
				}
			}
			save();
			init();
		} catch (Exception e) {
			handleSaveException(e);
		}
		return "adminService";
	}

	private void save() throws IOException
	{
		logger.info("saving changes to " + loadedFile.getAbsolutePath());
		taskLauncherDoc.save(loadedFile);
	}

	public String addService()
	{
		try {
				String name = newService;
				if (!NEW_SERVICE.equals(name))
				{
					//read in the new File
					try {
						logger.info("Creating new Task with file " + name);
						if (!name.startsWith("/"))
						{
							name = "/" + name;
						}
						InputStream resourceAsStream = getClass().getResourceAsStream(name);
						if (resourceAsStream != null)
						{
							TasklauncherconfigDocument newConfig = TasklauncherconfigDocument.Factory.parse(resourceAsStream);
							Treeconfig[] newTreeconfigArray = newConfig.getTasklauncherconfig().getTreeconfigArray();
							if (newTreeconfigArray.length == 1)
							{
								Treeconfig newTreeConfig = taskLauncherDoc.getTasklauncherconfig().addNewTreeconfig();
								newTreeConfig.set(newTreeconfigArray[0]);
								newService = NEW_SERVICE;
								save();
							}
							else
							{
								FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,bundle.getString("fileNotRead.notOneElementFound"),null));
							}
						}
						else
						{
							logger.log(Level.SEVERE,"Cannot load Task-Config from " + name + " Resource not found.");
							FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,bundle.getString("fileNotRead.notfound"),null));
						}
					} catch (Exception e) {
						handleSaveException(e);
					} 		
			}
			init();
		} catch (Exception e) {
			handleSaveException(e);
		}
		return "adminService";
	}
	
	public List getHostTable()
	{
		if (hostTable == null)
		{
			hostTable = new ArrayList();
			String[] services = getServices();
			
			Cimom[] cimomArray = taskLauncherDoc.getTasklauncherconfig().getCimomArray();
			for (int i = 0; i < cimomArray.length; i++) {
				Cimom cimom = cimomArray[i];
				hostTable.add(new HostEntry(cimom,services));
			}
			hostTable.add(new HostEntry(services));
		}
		return hostTable;
	}

	public List getServiceTable()
	{
		if (serviceTable == null)
		{
			serviceTable = new ArrayList();
			Treeconfig[] treeConfigArray = taskLauncherDoc.getTasklauncherconfig().getTreeconfigArray();
			for (int i = 0; i < treeConfigArray.length; i++) {
				Treeconfig treeConfig = treeConfigArray[i];
				serviceTable.add(new ServiceEntry(treeConfig.getName(),treeConfig.getFilename()));
			}
		}
		return serviceTable;
	}
	
	public String[] getServices()
	{
		 Treeconfig[] treeconfigArray = taskLauncherDoc.getTasklauncherconfig().getTreeconfigArray();
		 String[] result = new String[treeconfigArray.length];
		 for (int i = 0; i < treeconfigArray.length; i++) {
			Treeconfig treeconfig = treeconfigArray[i];
			result[i] = treeconfig.getName();
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
		TaskLauncherController.getLogger().log(Level.SEVERE,bundle.getString("cannotsave"));
	}
	
	public boolean isAdminEnabled()
	{
		File f = new File (TaskLauncherConfig.getConfigFile());
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
	
    public String getAdminDisabledMsg() {
		String msg = bundle.getString("adminDisabled", new Object[]{new File(TaskLauncherConfig.getConfigFile()).getAbsolutePath()});
		return msg;
	}
	
}

