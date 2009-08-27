 /** 
  * WbemsmtWebAppViewHandlerImpl.java
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
  * Description: View handler for the wbemsmt task launcher
  * 
  */

package org.sblim.wbemsmt.webapp.jsf;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.apache.myfaces.application.jsp.JspViewHandlerImpl;
import org.sblim.wbemsmt.bl.messages.Message;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;
import org.sblim.wbemsmt.tools.resources.ILocaleManager;

public class WbemsmtWebAppViewHandlerImpl extends JspViewHandlerImpl {
	
	public void renderView(FacesContext fc, UIViewRoot viewToRender) throws IOException, FacesException {
		ILocaleManager localeManager = (ILocaleManager) BeanNameConstants.LOCALE_MANAGER.asValueExpression(fc).getValue(fc.getELContext());
		Locale currentLocale = localeManager.getCurrentLocale();
		
		viewToRender.setLocale(currentLocale);
		
		addMessagesFromSession(fc);
		
		MessageHandlerBean bean = (MessageHandlerBean) BeanNameConstants.MESSAGE_HANDLER.getBoundValue(fc);
		bean.updateMessages();
		
		
//		System.out.println("Locale is " + currentLocale.toString());
//		
//		PrintWriter pw = new PrintWriter(new FileWriter("D:/out.txt"));
//		JsfUtil.traceComponentTree(viewToRender, pw);
//		pw.flush();
//		pw.close();
//		
		
		super.renderView(fc, viewToRender);
	}

	private void addMessagesFromSession(FacesContext fc) {
		Iterator<Entry<String,Object>> it = fc.getExternalContext().getSessionMap().entrySet().iterator();
		List<Object> remove = new ArrayList<Object>();
		while (it.hasNext())
		{
			Map.Entry<String,Object> entry = (Entry<String,Object>) it.next();
			if (entry.getValue() instanceof Message)
			{
				Message msg = (Message) entry.getValue();
				JsfUtil.addMessage(msg);
				remove.add(entry.getKey());
			}
		}
		
		for (Iterator<Object> iter = remove.iterator(); iter.hasNext();) {
			Object key = (Object) iter.next();
			fc.getExternalContext().getSessionMap().remove(key);
		}
	}


}

