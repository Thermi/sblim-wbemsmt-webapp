 /** 
  * ServiceInHost.java
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
  * Description: Service within a host in the admin console
  * 
  */
package org.sblim.wbemsmt.webapp.jsf.admin;

import org.apache.commons.lang.StringUtils;
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

		if (referencedConfig != null)
		{
			ConfigurationDefinition[] definitions = referencedConfig.getConfigurationDefinitionArray();

			WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(referencedConfig.getResourceBundleArray());

			// add the namespace as the first configuration value
			ConfigurationDefinition namespaceDefinition = ConfigurationDefinition.Factory.newInstance();
			namespaceDefinition.setDefaultValue(referencedConfig.getNamespace());
			namespaceDefinition.setName("namespace");
			namespaceDefinition.setInfo("info.namespace");

			ConfigurationValue namespaceValue = ConfigurationValue.Factory.newInstance();
			namespaceValue.setName("namespace");
			namespaceValue.setValue(StringUtils.isNotEmpty(reference.getNamespace()) ? reference.getNamespace() : referencedConfig.getNamespace() );
			
			
			items = new TaskConfigurationItem[definitions.length+1];
			items[0] = new TaskConfigurationItem(bundle,namespaceDefinition,namespaceValue);
			
			for (int i = 1; i < items.length; i++) {
				items[i] = new TaskConfigurationItem(bundle,definitions[i-1],getValue(definitions [i-1]));
			}
		}
		else
		{
			items = new TaskConfigurationItem[0];
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
	
	public String getNamespace()
	{
		return items[0].getValue().getValue();
	}
	
}