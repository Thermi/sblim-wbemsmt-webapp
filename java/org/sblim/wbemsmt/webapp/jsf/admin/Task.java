 /** 
  * Task.java
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
  * Description: DataObject for a installed Task
  * 
  */
package org.sblim.wbemsmt.webapp.jsf.admin;

import javax.faces.context.FacesContext;

import org.sblim.wbemsmt.tools.resources.ResourceBundleManager;
import org.sblim.wbemsmt.tools.resources.WbemSmtResourceBundle;

public class Task {

	String name;
	boolean configFound;
	boolean bundleInstalled;
	public Task(String name, boolean configFound, boolean bundleInstalled) {
		super();
		this.name = name;
		this.configFound = configFound;
		this.bundleInstalled = bundleInstalled;
	}
	public String getBundleAvailable() {
		
		WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(FacesContext.getCurrentInstance());
		return configFound && bundleInstalled ? bundle.getString("yes") : bundle.getString("no");
	}

	public String getName() {
		return name;
	}
	
	
	
}
