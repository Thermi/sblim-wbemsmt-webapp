 /** 
  * HostEntry.java
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
  * Description: A Host within the admin console
  * 
  */
package org.sblim.wbemsmt.webapp.jsf.admin;

import java.util.*;

import org.sblim.wbemsmt.tasklauncher.CustomTreeConfig;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig.CimomData;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig.TreeConfigData;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.CimomDocument.Cimom;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigReferenceDocument.TreeconfigReference;
import org.sblim.wbemsmt.util.StringComparator;

public class HostEntry
{
	boolean delete;
	boolean addToFile = false;
	String hostname;
	String namespace;
	String applicationNamespace;
	String user;
	int port;
	List services = new ArrayList();
	boolean isNew = false;
	boolean changePassword;
	private Cimom cimom;
	private String servicesAsString;
	private static Map serviceInstallationStates = new HashMap();
	
	/**
	 * Entry for an existing host
	 * @param config
	 * @param cimom
	 * @param references
	 */
	public HostEntry(AdminBean bean, TaskLauncherConfig config, Cimom cimom, TreeconfigReference[] references)
	{
		this.cimom = cimom;
		isNew = false;
		hostname = cimom.getHostname();
		port = cimom.getPort();
		user = cimom.getUser();
		namespace = cimom.getNamespace();
		applicationNamespace = cimom.getApplicationNamespace();
		
		Map referencesByName = new HashMap();
		Set installedServices = new HashSet();
		Set referencedServices = new HashSet();

		/**
		 * first get all tasks which are installed on a server
		 */
		for (int i = 0; i < references.length; i++) {
			
			TreeconfigReference reference = references[i];
			referencesByName.put(reference.getName(),reference);
			installedServices.add(reference.getName());
		}

		/**
		 * get those which are used on a host
		 */
		TreeconfigReference[] treeconfigReferenceArray = cimom.getTreeconfigReferenceArray();
		for (int i = 0; i < treeconfigReferenceArray.length; i++) {
			TreeconfigReference reference = treeconfigReferenceArray[i];
			referencesByName.put(reference.getName(),reference);
			referencedServices.add(reference.getName());
		}

		String[] keys = (String[]) referencesByName.keySet().toArray(new String[referencesByName.keySet().size()]);
		Arrays.sort(keys, new StringComparator());

		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < keys.length; i++) {
			TreeconfigReference reference = (TreeconfigReference) referencesByName.get(keys[i]);
			ServiceInHost serviceInHost = new ServiceInHost(reference, bean.getTreeconfigByTaskname(reference.getName()));
			serviceInHost.setEnabled(referencedServices.contains(reference.getName()));
			serviceInHost.setConfigured(installedServices.contains(reference.getName()));

			boolean installed = isServiceInstalled(config, reference,new CimomData(cimom));
			serviceInHost.setInstalled(installed);
			
			this.services.add(serviceInHost);
			if (serviceInHost.isEnabled())
			{
				if (sb.length() > 0)
				{
					sb.append(", ");
				}
				sb.append(serviceInHost.getReference().getName());
			}
		}
		servicesAsString = sb.toString();
		
	}
	
	/**
	 * Entry for a new host
	 * @param config
	 * @param services
	 */
	public HostEntry(AdminBean bean, TaskLauncherConfig config, TreeconfigReference[] services)
	{
		isNew = true;
		hostname = AdminBean.NEW_HOST;
		namespace = TaskLauncherConfig.DEFAULT_NAMESPACE;
		applicationNamespace = TaskLauncherConfig.DEFAULT_NAMESPACE;
		port = TaskLauncherConfig.DEFAULT_PORT;
		user = TaskLauncherConfig.DEFAULT_USER;
		
		for (int i = 0; i < services.length; i++) {
			TreeconfigReference configReference = services[i];
			
			ServiceInHost serviceInHost = new ServiceInHost(configReference, bean.getTreeconfigByTaskname(configReference.getName()));
			serviceInHost.setEnabled(false);
			serviceInHost.setConfigured(true);
			
			boolean installed = isServiceInstalled(config, configReference, new CimomData(hostname,port,namespace,applicationNamespace,user));
			serviceInHost.setInstalled(installed);
			
			this.services.add(serviceInHost);
		}		
	}
	
	private boolean isServiceInstalled(TaskLauncherConfig config, TreeconfigReference service, CimomData cimomData) {
		
		Boolean installed = (Boolean) serviceInstallationStates.get(service);
		if (installed == null)
		{
			TreeConfigData treeConfigDataByTaskname = config.getTreeConfigDataByTaskname(service.getName());
			installed = new Boolean(treeConfigDataByTaskname != null && 
							new CustomTreeConfig(treeConfigDataByTaskname,cimomData).isLoaded());
			serviceInstallationStates.put(service,installed);
		}
		return installed.booleanValue();
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
	public boolean isAddToFile() {
		return addToFile;
	}
	public void setAddToFile(boolean addToFile) {
		this.addToFile = addToFile;
	}
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getApplicationNamespace() {
		return applicationNamespace;
	}
	public void setApplicationNamespace(String applicationNamespace) {
		this.applicationNamespace = applicationNamespace;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	/**
	 * return a list of Service in Host objects
	 * @return
	 */
	public List getServices() {
		return services;
	}

	
	/**
	 * Return only the enabled services
	 * @return
	 */
	public List getEnabledServices() {
		List result = new ArrayList();
		
		for (Iterator iter = services.iterator(); iter.hasNext();) {
			ServiceInHost serviceInHost = (ServiceInHost) iter.next();
			if (serviceInHost.isEnabled())
			{
				result.add(serviceInHost);
			}
		}
		
		return result;
	}

	public String getServicesAsString() {
		return servicesAsString;
	}
	public void setServicesAsString(String servicesAsString) {
		this.servicesAsString = servicesAsString;
	}
	public void setServices(List services) {
		this.services = services;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public boolean getChangePassword() {
		return changePassword;
	}
	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}
	public Cimom getCimom() {
		return cimom;
	}
	public void setCimom(Cimom cimom) {
		this.cimom = cimom;
	}
	
	
	

}