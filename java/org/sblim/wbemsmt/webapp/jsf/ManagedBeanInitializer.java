 /** 
  * ManagedBeanInitializationManager.java
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
  * Description: a PhaseListener responsible for 
  * 
  */
package org.sblim.wbemsmt.webapp.jsf;

import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants;

public class ManagedBeanInitializer implements PhaseListener {

    private static Logger logger = Logger.getLogger(ManagedBeanInitializer.class.getName());
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
	 */
	public void afterPhase(PhaseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
	 */
	public void beforePhase(PhaseEvent arg0) {
		
		logger.info("Initializing " + BeanNameConstants.TASKLAUNCHER_CONTROLLER.getName());
		TaskLauncherController controller = (TaskLauncherController) BeanNameConstants.TASKLAUNCHER_CONTROLLER.getBoundValue(FacesContext.getCurrentInstance());
		System.out.println(controller);
	}

	/* (non-Javadoc)
	 * @see javax.faces.event.PhaseListener#getPhaseId()
	 */
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
