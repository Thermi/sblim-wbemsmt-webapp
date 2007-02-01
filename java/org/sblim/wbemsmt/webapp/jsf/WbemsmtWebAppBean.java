 /** 
  * WbemsmtWebAppBean.java
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
  * Description: base class for wbemsmt backing beans
  * 
  */
package org.sblim.wbemsmt.webapp.jsf;

import java.util.Locale;

import javax.faces.context.FacesContext;

import org.sblim.wbemsmt.tools.beans.BeanNameConstants;
import org.sblim.wbemsmt.tools.resources.ILocaleManager;
import org.sblim.wbemsmt.tools.resources.LocaleChangeListener;
import org.sblim.wbemsmt.tools.resources.LocaleManager;
import org.sblim.wbemsmt.tools.resources.ResourceBundleManager;
import org.sblim.wbemsmt.tools.resources.WbemSmtResourceBundle;

public abstract class WbemsmtWebAppBean
{
    protected WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(FacesContext.getCurrentInstance(),getClass().getClassLoader());
    
    public WbemsmtWebAppBean()
    {
    	WbemSmtResourceBundle bundleMessages = ResourceBundleManager.getResourceBundle(new String[]{"messages"},LocaleManager.getCurrent(FacesContext.getCurrentInstance()).getCurrentLocale());
    	bundle.add(bundleMessages);
    	
    	FacesContext fc = FacesContext.getCurrentInstance();
		ILocaleManager localeManager = (ILocaleManager) BeanNameConstants.LOCALE_MANAGER.asValueBinding(fc).getValue(fc);
		localeManager.addLocaleChangeListener(new LocaleChangeListener()
		{
			public void localeChanged(Locale newLocale)
			{
				bundle = ResourceBundleManager.getResourceBundle(FacesContext.getCurrentInstance());

				WbemSmtResourceBundle bundleMessages = ResourceBundleManager.getResourceBundle(new String[]{"messages"},newLocale);
		    	bundle.add(bundleMessages);
			}
		});
    	
	}

}
