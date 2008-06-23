 /** 
  * SessionInvalidationListener.java
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
  * Description: Listener invoked if a session is invalidated
  * 
  */
package org.sblim.wbemsmt.webapp.jsf;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.sblim.wbemsmt.bl.cleanup.Cleanup;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants;

public class SessionInvalidationListener implements HttpSessionListener {

	private static Logger logger = Logger.getLogger(SessionInvalidationListener.class.getName());	
	
	public void sessionCreated(HttpSessionEvent arg0) {
//		HttpSession session = arg0.getSession();
//		int seconds = 60;
//		session.setMaxInactiveInterval(seconds);
//		logger.info("Session with id " + session.getId() + " will expire after " + seconds + " seconds of inactivity.");
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		
		HttpSession session = arg0.getSession();

		logger.info("Session with id " + session.getId() + " was destroyed. Doing Cleanup");
		
		List deleteItems = new ArrayList();

		Enumeration attributeNames = session.getAttributeNames();
		while (attributeNames.hasMoreElements())
		{
			String name = (String) attributeNames.nextElement();
			Object value = session.getAttribute(name);
			if (value instanceof Cleanup) {
				Cleanup cleanupObject = (Cleanup) value;
				try {
					cleanupObject.cleanup();
				} catch (Throwable e) {
					e.printStackTrace();
				}
				deleteItems.add(name);
			}
		}
		
		for (Iterator iter = deleteItems.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			session.removeAttribute(key);
		}
		
		session.removeAttribute(BeanNameConstants.LOCALE_MANAGER.getName());
		session.removeAttribute(BeanNameConstants.LOGIN_CHECK.getName());
		session.removeAttribute(BeanNameConstants.MENUE_CONTROLLER.getName());
		session.removeAttribute(BeanNameConstants.MESSAGE_HANDLER.getName());
		session.removeAttribute(BeanNameConstants.OBJECT_ACTION_CONTROLLER.getName());
		session.removeAttribute(BeanNameConstants.TASKLAUNCHER_CONTROLLER.getName());
		session.removeAttribute(BeanNameConstants.TREE_SELECTOR.getName());
	}


}
