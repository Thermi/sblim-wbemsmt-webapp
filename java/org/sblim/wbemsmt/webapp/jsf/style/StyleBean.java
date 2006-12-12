 /** 
  * Style.java
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
  * Description: Infoclass for a Style
  * 
  */

package org.sblim.wbemsmt.webapp.jsf.style;

public class StyleBean {

	/**
	 * the resourcedirectory
	 * @return
	 */
	private String resourceDir;
	private String resourceBundle;
	
	private boolean header;
	
	public String getResourceDir() {
		return resourceDir;
	}

	public void setResourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
	}

	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public String getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(String resourceBundle) {
		this.resourceBundle = resourceBundle;
	}


	
	
	
	
	

}
