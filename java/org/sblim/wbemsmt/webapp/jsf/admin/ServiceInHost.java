 /** 
  * ServiceInHost.java
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
  * Description: Service within a host in the admin console
  * 
  */
package org.sblim.wbemsmt.webapp.jsf.admin;

import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.ConfigurationDefinitionDocument.ConfigurationDefinition;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.ConfigurationValueDocument.ConfigurationValue;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigDocument.Treeconfig;
import org.sblim.wbemsmt.tasklauncher.tasklauncherconfig.TreeconfigReferenceDocument.TreeconfigReference;
import org.sblim.wbemsmt.tools.resources.ResourceBundleManager;
import org.sblim.wbemsmt.tools.resources.WbemSmtResourceBundle;

public class ServiceInHost
{
	boolean enabled;
	boolean installed;
	boolean configured;
	private TreeconfigReference reference;
	private TaskConfigurationItem[] items;

	ServiceInHost(TreeconfigReference reference,Treeconfig referencedConfig)
	{
		this.reference = reference;
		
		
		ConfigurationDefinition[] definitions = referencedConfig.getConfigurationDefinitionArray();

		WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(referencedConfig.getResourceBundleArray());
		
		items = new TaskConfigurationItem[definitions.length];
		for (int i = 0; i < items.length; i++) {
			items[i] = new TaskConfigurationItem(bundle,definitions[i],getValue(definitions [i]));
		}
	}

	private ConfigurationValue getValue(ConfigurationDefinition definition) {
		
		for (int i = 0; i < reference.getConfigurationValueArray().length; i++) {
			ConfigurationValue value = reference.getConfigurationValueArray()[i];
			if (value.getName().equals(definition.getName()))
			{
				return value;
			}
		}
		
		ConfigurationValue value = reference.addNewConfigurationValue();
		value.setName(definition.getName());
		value.setValue(definition.getDefaultValue());
		return value;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isConfigurable() {
		return items.length > 0;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public TreeconfigReference getReference() {
		return reference;
	}

	public void setReference(TreeconfigReference reference) {
		this.reference = reference;
	}

	public boolean isInstalled() {
		return installed;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}
	
	public TaskConfigurationItem[] getConfigurationItems()
	{
		return items;
	}
	
}