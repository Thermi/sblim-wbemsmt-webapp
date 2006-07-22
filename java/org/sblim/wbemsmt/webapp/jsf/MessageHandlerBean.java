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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class MessageHandlerBean  extends WbemsmtWebAppBean
{
	private Map images = new HashMap();
	private Map colors = new HashMap();
	private List messages = new ArrayList();
	
	

	public MessageHandlerBean() {
		super();
		images.put(FacesMessage.SEVERITY_INFO,"/images/info.gif");
		images.put(FacesMessage.SEVERITY_WARN,"/images/warning.gif");
		images.put(FacesMessage.SEVERITY_ERROR,"/images/error.gif");
		images.put(FacesMessage.SEVERITY_FATAL,"/images/fatal.gif");

		colors.put(FacesMessage.SEVERITY_INFO,"black");
		colors.put(FacesMessage.SEVERITY_WARN,"orange");
		colors.put(FacesMessage.SEVERITY_ERROR,"red");
		colors.put(FacesMessage.SEVERITY_FATAL,"red");
	}

	public List getMessages()
	{
		return messages;
	}
	
	public void setMessages(List messages) {
		this.messages = messages;
	}

	public Map getImages()
	{
		return images;
	}
	
	public Map getColors()
	{
		return colors;
	}

	public void updateMessages() {
		Set messageSet = new HashSet();
		messages.clear();
		Iterator iterator = FacesContext.getCurrentInstance().getMessages();
		while (iterator.hasNext())
		{
			FacesMessage msg = (FacesMessage) iterator.next();
			if (!messageSet.contains(msg.getSummary()))
			{
				messages.add(msg);
				messageSet.add(msg.getSummary());
			}
		}
		
	}	
}
