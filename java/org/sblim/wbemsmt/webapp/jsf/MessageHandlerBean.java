/** 
 * MessageHandlerBean.java
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
 * Description: Own Implementation for showing the faces messages via a DataTable
 * 
 */
package org.sblim.wbemsmt.webapp.jsf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.sblim.wbemsmt.bl.Cleanup;
import org.sblim.wbemsmt.bl.adapter.Message;
import org.sblim.wbemsmt.tools.jsf.FacesMessageWrapper;
import org.sblim.wbemsmt.tools.jsf.WbemsmtFacesMessage;
import org.sblim.wbemsmt.tools.jsf.WbesmtFacesSeverity;
import org.sblim.wbemsmt.webapp.jsf.style.StyleBean;

public class MessageHandlerBean extends WbemsmtWebAppBean implements Cleanup {
	
	static Logger logger = Logger.getLogger(MessageHandlerBean.class.getName());

	private List successMessages = new ArrayList();
	private List infoMessages = new ArrayList();
	private List warningMessages = new ArrayList();
	private List errorMessages = new ArrayList();

	private boolean infos, errors,warnings,success = false;
	
	private StyleBean style;

	public MessageHandlerBean() {
		super();
	}
	
	public List getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List errorMessages) {
		this.errorMessages = errorMessages;
	}
	public List getInfoMessages() {
		return infoMessages;
	}
	public void setInfoMessages(List infoMessages) {
		this.infoMessages = infoMessages;
	}
	public List getSuccessMessages() {
		return successMessages;
	}
	public void setSuccessMessages(List successMessages) {
		this.successMessages = successMessages;
	}
	public List getWarningMessages() {
		return warningMessages;
	}
	public void setWarningMessages(List warningMessages) {
		this.warningMessages = warningMessages;
	}
	
	public void updateMessages() {
		Set messageSet = new HashSet();
		successMessages.clear();
		infoMessages.clear();
		warningMessages.clear();
		errorMessages.clear();
		Iterator iterator = FacesContext.getCurrentInstance().getMessages();
		while (iterator.hasNext()) {
			FacesMessage msg = (FacesMessage) iterator.next();
			
			WbemsmtFacesMessage wbemsmtFacesMessage = null;
			if (msg instanceof WbemsmtFacesMessage) {
				wbemsmtFacesMessage = (WbemsmtFacesMessage) msg;
			}
			else
			{
				wbemsmtFacesMessage = new FacesMessageWrapper(msg);
			}
			
			
			if (!messageSet.contains(msg.getSummary())) {
				
				WbesmtFacesSeverity severity = new WbesmtFacesSeverity(msg);
				if (Message.isSuccess(severity.getSeverity()))
				{
					successMessages.add(wbemsmtFacesMessage);	
					messageSet.add(msg.getSummary());
				}
				else if (Message.isInfo(severity.getSeverity()))
				{
					infoMessages.add(wbemsmtFacesMessage);	
					messageSet.add(msg.getSummary());
				}
				else if (Message.isWarning(severity.getSeverity()))
				{
					warningMessages.add(wbemsmtFacesMessage);	
					messageSet.add(msg.getSummary());
				}
				else if (Message.isError(severity.getSeverity()))
				{
					errorMessages.add(wbemsmtFacesMessage);	
					messageSet.add(msg.getSummary());
				} else
				{
					logger.severe(msg.getSummary() + " is from a not recognized type: " + severity.getSeverity());
				}
			}
		}

		Collections.sort(successMessages, new FacesMessagesComparator());
		Collections.sort(infoMessages, new FacesMessagesComparator());
		Collections.sort(warningMessages, new FacesMessagesComparator());
		Collections.sort(errorMessages, new FacesMessagesComparator());
		
		success = successMessages.size() > 0;
		infos = infoMessages.size() > 0;
		warnings = warningMessages.size() > 0;
		errors = errorMessages.size() > 0;
	}

	public void cleanup() {
		successMessages.clear();
		infoMessages.clear();
		warningMessages.clear();
		errorMessages.clear();
	}

	public StyleBean getStyle() {
		return style;
	}

	public void setStyle(StyleBean style) {
		this.style = style;
	}

	public boolean isErrors() {
		return errors;
	}

	public boolean isInfos() {
		return infos;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isWarnings() {
		return warnings;
	}
	
	public boolean isHavingMessages()
	{
		return warnings || success || infos || errors;
	}

	
	
}
