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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sblim.wbemsmt.tasklauncher.CustomTreeConfig;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig;
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
	String user;
	int port;
	List services = new ArrayList();
	boolean isNew = false;
	boolean changePassword;
	private Cimom cimom;
	private String servicesAsString;
	private static Map serviceInstallationStates = new HashMap();
	
	public HostEntry(TaskLauncherConfig config, Cimom cimom, String[] services)
	{
		this.cimom = cimom;
		isNew = false;
		hostname = cimom.getHostname();
		port = cimom.getPort();
		user = cimom.getUser();
		namespace = cimom.getNamespace();
		
		Set installedServices = new HashSet();
		for (int i = 0; i < services.length; i++) {
			installedServices.add(services[i]);
		}

		TreeconfigReference[] treeconfigReferenceArray = cimom.getTreeconfigReferenceArray();
		Set referencedServices = new HashSet();
		for (int i = 0; i < treeconfigReferenceArray.length; i++) {
			referencedServices.add(treeconfigReferenceArray[i].getName());
		}
		
		Set allServicesSet = new HashSet();
		allServicesSet.addAll(installedServices);
		allServicesSet.addAll(referencedServices);
		
		List allServices = new ArrayList();
		allServices.addAll(allServicesSet);
		Collections.sort(allServices, new StringComparator());

		StringBuffer sb = new StringBuffer();
		
		for (Iterator iter = allServices.iterator(); iter.hasNext();) {
			String service = (String) iter.next();
			ServiceInHost serviceInHost = new ServiceInHost(service);
			serviceInHost.setEnabled(referencedServices.contains(service));
			
			boolean configured = installedServices.contains(service);
			serviceInHost.setConfigured(configured);

			boolean installed = isServiceInstalled(config, service);
			serviceInHost.setInstalled(installed);
			
			this.services.add(serviceInHost);
			if (serviceInHost.isEnabled())
			{
				if (sb.length() > 0)
				{
					sb.append(", ");
				}
				sb.append(serviceInHost.getService());
			}
		}
		servicesAsString = sb.toString();
		
	}
	private boolean isServiceInstalled(TaskLauncherConfig config, String service) {
		
		Boolean installed = (Boolean) serviceInstallationStates.get(service);
		if (installed == null)
		{
			TreeConfigData treeConfigDataByTaskname = config.getTreeConfigDataByTaskname(service);
			installed = new Boolean(treeConfigDataByTaskname != null && 
							new CustomTreeConfig(treeConfigDataByTaskname).isLoaded());
			serviceInstallationStates.put(service,installed);
		}
		return installed.booleanValue();
	}
	public HostEntry(TaskLauncherConfig config, String[] services)
	{
		isNew = true;
		hostname = AdminBean.NEW_HOST;
		namespace = TaskLauncherConfig.DEFAULT_NAMESPACE;
		port = TaskLauncherConfig.DEFAULT_PORT;
		user = TaskLauncherConfig.DEFAULT_USER;
		
		for (int i = 0; i < services.length; i++) {
			String service = services[i];
			
			ServiceInHost serviceInHost = new ServiceInHost(service);
			serviceInHost.setEnabled(false);
			serviceInHost.setConfigured(true);
			
			boolean installed = isServiceInstalled(config, service);
			serviceInHost.setInstalled(installed);
			
			this.services.add(serviceInHost);
		}		
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
	public List getServices() {
		return services;
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