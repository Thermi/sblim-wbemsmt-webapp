 /** 
  * HostEntry.java
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

import java.util.ArrayList;
import java.util.List;

import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.CimomDocument.Cimom;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigReferenceDocument.TreeconfigReference;

public class HostEntry
{
	boolean delete;
	String hostname;
	String namespace;
	String user;
	int port;
	List services = new ArrayList();
	boolean isNew = false;
	boolean changePassword;
	private Cimom cimom;
	
	public HostEntry(Cimom cimom, String[] services)
	{
		this.cimom = cimom;
		isNew = false;
		hostname = cimom.getHostname();
		port = cimom.getPort();
		user = cimom.getUser();
		namespace = cimom.getNamespace();
		
		for (int i = 0; i < services.length; i++) {
			String service = services[i];
			
			ServiceInHost serviceInHost = new ServiceInHost(service);
			
			TreeconfigReference[] treeconfigReferenceArray = cimom.getTreeconfigReferenceArray();
			
			boolean found = false;
			for (int j = 0; !found && j < treeconfigReferenceArray.length; j++) {
				TreeconfigReference reference = treeconfigReferenceArray[j];
				if (reference.getName().equals(service))
				{
					found = true;
				}
			}
			
			serviceInHost.setEnabled(found);
			this.services.add(serviceInHost);
		}
	}
	public HostEntry(String[] services)
	{
		isNew = true;
		hostname = AdminBean.NEW_HOST;
		namespace = "/root/cimv2";
		port = 5988;
		user = "pegasus";
		
		for (int i = 0; i < services.length; i++) {
			String service = services[i];
			
			ServiceInHost serviceInHost = new ServiceInHost(service);
			serviceInHost.setEnabled(false);
			this.services.add(serviceInHost);
		}		
	}
	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
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