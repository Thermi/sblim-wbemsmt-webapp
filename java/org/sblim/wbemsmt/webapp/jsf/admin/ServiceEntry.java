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
  * Description: a service (task) within the admin console
  * 
  */
package org.sblim.wbemsmt.webapp.jsf.admin;


public class ServiceEntry
{
	boolean delete;
	String name;
	boolean isNew = false;
	private String file;
	
	public ServiceEntry(String name,String file)
	{
		this.name = name;
		this.file = file;
		isNew = false;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public String getName() {
		return name;
	}

	public void setName(String hostname) {
		this.name = hostname;
	}

	
	
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public boolean isNew() {
		return isNew;
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
}