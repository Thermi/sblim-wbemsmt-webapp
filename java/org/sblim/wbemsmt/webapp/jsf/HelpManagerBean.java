 /** 
  * HelpManagerBean.java
  *
  * © Copyright IBM Corp.  2009,2005
  *
  * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
  *
  * You can obtain a current copy of the Eclipse Public License from
  * http://www.opensource.org/licenses/eclipse-1.0.php
  *
  * @author: Michael Bauschert <Michael.Bauschert@de.ibm.com>
  *
  * Contributors: 
  * 
  * Description: Managed Bean for managing help
  */
package org.sblim.wbemsmt.webapp.jsf;

import javax.faces.context.FacesContext;

import org.sblim.wbemsmt.bl.help.HelpManager;

/**
 * @author Bauschert
 *
 */
public class HelpManagerBean extends HelpManager {
	
	private ObjectActionControllerBean objActionController;
	
	/**
	 * Returns the Path to the help
	 * @return
	 */
	public String getHelpPath()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		
		String anchor = getMode() == MODE_EDIT ? "#tab_" + objActionController.getSelectedTabIndex() : "";
		
		String language = context.getViewRoot().getLocale().getLanguage();
		//If it's the default language use the root of the help bundle
		language = language.equals(getDefaultLanguage()) ? "/" : "/nl/" + language + "/";
		
		String path = 
			     context.getExternalContext().getRequestContextPath() 
			   + "/help/plugins/"
			   + WBEMSMT_BUNDLE
			   + getApplication()
			   + language 
			   + getCurrentTopic() 
			   + ".html"
			   + anchor;
		
		logger.info("Current help path: " + path);
		
		return path;
	}
	
	public ObjectActionControllerBean getObjActionController() {
		return objActionController;
	}

	public void setObjActionController(
			ObjectActionControllerBean objActionController) {
		this.objActionController = objActionController;
	}
	
	/**
	 * Returns a call to the showHelp-javascript method
	 * @return
	 */
	public String getJavascriptCall()
	{
		return "showHelp('" + getHelpPath() + "');return false;";
	}
	
}
