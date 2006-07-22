 /** 
  * WbemsmtWebAppViewHandlerImpl.java
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
  * Description: View handler for the wbemsmt task launcher
  * 
  */

package org.sblim.wbemsmt.webapp.jsf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.myfaces.tomahawk.application.jsp.JspTilesViewHandlerImpl;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants;
import org.sblim.wbemsmt.tools.resources.ILocaleManager;

public class WbemsmtWebAppViewHandlerImpl extends JspTilesViewHandlerImpl {
	
	public WbemsmtWebAppViewHandlerImpl(ViewHandler viewHandler)
	{
		super(viewHandler);
	}

	public void renderView(FacesContext fc, UIViewRoot viewToRender) throws IOException, FacesException {
		ILocaleManager localeManager = (ILocaleManager) BeanNameConstants.LOCALE_MANAGER.asValueBinding(fc).getValue(fc);
		Locale currentLocale = localeManager.getCurrentLocale();
		// TODO Auto-generated method stub
		viewToRender.setLocale(currentLocale);

		MessageHandlerBean bean = (MessageHandlerBean) BeanNameConstants.MESSAGE_HANDLER.getBoundValue(fc);
		bean.updateMessages();
		
		
		System.out.println("Locale is " + currentLocale.toString());
//		
//		PrintWriter pw = new PrintWriter(new FileWriter("D:/out.txt"));
//		JsfUtil.traceComponentTree(viewToRender, pw);
//		pw.flush();
//		pw.close();
//		
		
		super.renderView(fc, viewToRender);
	}
}
