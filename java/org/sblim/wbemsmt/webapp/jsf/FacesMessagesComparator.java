 /** 
  * FacesMessagesComparator.java
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
  * Description: Compares the Severity of FacesMessages
  * 
  */
package org.sblim.wbemsmt.webapp.jsf;

import java.util.Comparator;

import javax.faces.application.FacesMessage;

import org.sblim.wbemsmt.tools.jsf.WbesmtFacesSeverity;

public class FacesMessagesComparator implements Comparator {

	/**
	 * 
	 */
	public FacesMessagesComparator() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		
		FacesMessage msg1 = (FacesMessage) o1;
		FacesMessage msg2 = (FacesMessage) o2;

		WbesmtFacesSeverity sev1 = new WbesmtFacesSeverity(msg1);
		WbesmtFacesSeverity sev2 = new WbesmtFacesSeverity(msg2);
		
		
		
		return sev1.compareTo(sev2);
	}

}
