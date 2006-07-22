<%-- 
/**
 *  wizardPage.jsp
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
 * Author:     Michael.Bauschert@de.ibm.com
 *
 * Contributors:
 *
 */
--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>

	<h:panelGrid id="wizardPanel" binding="#{objectActionController.currentWizard.currentPanel.inputFieldContainer}"/>
	<h:commandButton value="#{messages.button_previous}" action="#{objectActionController.currentWizard.back}" disabled="#{objectActionController.currentWizard.backButtonDisabled}" />
	<h:commandButton value="#{messages.button_next}" action="#{objectActionController.currentWizard.next}" disabled="#{objectActionController.currentWizard.nextButtonDisabled}" />
	<h:commandButton value="#{messages.button_finish}" action="#{objectActionController.currentWizard.finish}" disabled="#{objectActionController.currentWizard.finishButtonDisabled}"/>
	<h:commandButton value="#{messages.button_cancel}" action="#{objectActionController.currentWizard.cancel}" disabled="#{objectActionController.currentWizard.cancelButtonDisabled}"/>
