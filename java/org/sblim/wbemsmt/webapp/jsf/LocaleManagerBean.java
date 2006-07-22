 /** 
  * LocaleManagerBean.java
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
package org.sblim.wbemsmt.webapp.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MultiHashMap;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tools.resources.ILocaleManager;
import org.sblim.wbemsmt.tools.resources.LocaleChangeListener;
import org.sblim.wbemsmt.tools.resources.ResourceBundleManager;
import org.sblim.wbemsmt.tools.resources.WbemSmtResourceBundle;

public class LocaleManagerBean implements ILocaleManager
{
	private Locale currentLocale;
	private static final Logger logger = TaskLauncherController.getLogger();
	private List resourceBundles = new ArrayList();
	private Map resourceBundleByAppName = new HashMap();
	private MultiHashMap bundleNamesByAppName = new MultiHashMap();
	
	Set listeners = new HashSet();
	
	public LocaleManagerBean()
	{
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession session = request.getSession(true);
		currentLocale = (Locale) session.getAttribute("locale");
		if (currentLocale == null)
		{
			currentLocale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
		}
		logger.info("Locale is " + currentLocale);
	}
	
	public void addLocaleChangeListener(LocaleChangeListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeLocaleChangeListener(LocaleChangeListener listener)
	{
		listeners.remove(listener);
	}

	public Locale getCurrentLocale()
	{
		return currentLocale;
	}
	
	public String getCurrentLocaleString()
	{
		return currentLocale.toString();
	}

	public void setCurrentLocale(Locale newLocale)
	{
		if (!newLocale.toString().equals(currentLocale.toString()))
		{
			logger.info("Locale is " + newLocale);
			currentLocale = newLocale;
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//			request.getSession().invalidate();
			HttpSession session = request.getSession(true);
			session.setAttribute("locale",newLocale);
			sendEvent(newLocale);
			
			//do a reload
			setResourceBundle(resourceBundles);
			
			logger.info("Locale is " + currentLocale);
		}
	}

	private void sendEvent(Locale newLocale) {
		for (Iterator iter = listeners.iterator(); iter.hasNext();) {
			LocaleChangeListener listener = (LocaleChangeListener) iter.next();
			listener.localeChanged(newLocale);
		}
	}
	
	public void setEnglish(ActionEvent event)
	{
		setCurrentLocale(Locale.ENGLISH);
	}

	public void setGerman(ActionEvent event)
	{
		setCurrentLocale(Locale.GERMAN);
	}

	public List getResourceBundle() {
		return resourceBundles;
	}

	public Map getBundle()
	{
		return resourceBundleByAppName;
	}

	
	/**
	 * Expects a List with String with format <application>:<bundle>
	 * @param resourceBundles
	 */
	
	public void setResourceBundle(List resourceBundles) {
		this.resourceBundles = resourceBundles;
		bundleNamesByAppName.clear();
		for (Iterator iter = resourceBundles.iterator(); iter.hasNext();) {
			String bundleInfo = (String) iter.next();
			int index = bundleInfo.indexOf(":");
			if (index > -1)
			{
				String app = bundleInfo.substring(0,index);
				String bundle = bundleInfo.substring(index+1);
				bundleNamesByAppName.put(app,bundle);
			}
		}
		
		resourceBundleByAppName.clear();
		
		for (Iterator iter = bundleNamesByAppName.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Entry) iter.next();
			String app = (String) entry.getKey();
			List list = (List) entry.getValue();
			String[] bundleNames = (String[]) list.toArray(new String[list.size()]);
			WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(bundleNames,currentLocale);
			resourceBundleByAppName.put(app,bundle);
		}
	}	
	
	/**
	 * Expects a String with format <application>:<bundle>
	 * @param appAndBundle
	 */
	public void addResourceBundleEntry(String appAndBundle)
	{
		int index = appAndBundle.indexOf(":");
		if (index > -1)
		{
			String app = appAndBundle.substring(0,index);
			String bundle = appAndBundle.substring(index+1);
			bundleNamesByAppName.put(app,bundle);
		}
		
		resourceBundleByAppName.clear();
		
		for (Iterator iter = bundleNamesByAppName.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Entry) iter.next();
			String app = (String) entry.getKey();
			List list = (List) entry.getValue();
			String[] bundleNames = (String[]) list.toArray(new String[list.size()]);
			WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(bundleNames,currentLocale);
			resourceBundleByAppName.put(app,bundle);
		}
	}
	
}
