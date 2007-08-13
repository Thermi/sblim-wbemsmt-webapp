 /** 
  * TaskConfigurationItem.java
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
  * Description: configuration Item for a task
  * 
  */
package org.sblim.wbemsmt.webapp.jsf.admin;

import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.ConfigurationDefinitionDocument.ConfigurationDefinition;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.ConfigurationValueDocument.ConfigurationValue;
import org.sblim.wbemsmt.tools.resources.WbemSmtResourceBundle;

public class TaskConfigurationItem {
	
	
	private ConfigurationDefinition def;
	private ConfigurationValue value;
	private String infoTranslated;

	public TaskConfigurationItem(WbemSmtResourceBundle bundle, ConfigurationDefinition def, ConfigurationValue value)
	{
		this.def = def;
		this.value = value;
		this.infoTranslated = bundle.getString(def.getInfo());
	}

	public ConfigurationDefinition getDef() {
		return def;
	}

	public void setDef(ConfigurationDefinition def) {
		this.def = def;
	}

	public ConfigurationValue getValue() {
		return value;
	}

	public void setValue(ConfigurationValue value) {
		this.value = value;
	}
	
	public String getInfoTranslated()
	{
		return infoTranslated;
	}
	
	
}
