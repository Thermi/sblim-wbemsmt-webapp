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

	<h:panelGrid columns="2" styleClass="wizardOuterPanel" rowClasses="wizardOuterPanelRow" columnClasses="wizardOuterPanelColumnProgress,wizardOuterPanelColumnContent" cellpadding="0" cellspacing="0" border="0" width="100%">
		<h:panelGrid columns="2" cellpadding="5" cellspacing="0" border="0">
			<h:dataTable var="step" value="#{objectActionController.currentWizard.stepList}">
				<h:column>
					<f:verbatim><nobr></f:verbatim>
					<h:graphicImage value="#{style.resourceDir}/images/empty.png" rendered="#{step.level == 2}" styleClass="wizardProgressEmptyGraphic"/>
					<h:graphicImage value="#{style.resourceDir}/images/empty.png" rendered="#{step.level == 3}" styleClass="wizardProgressEmptyGraphic"/>
					<h:graphicImage value="#{style.resourceDir}/images/empty.png" rendered="#{step.level == 4}" styleClass="wizardProgressEmptyGraphic"/>
					<h:commandLink rendered="#{step.visited && !step.userdefined}" 
								   value="#{step.text}"
								   styleClass="wizardLink"
								   actionListener="#{objectActionController.currentWizard.select}"
								   action="#{objectActionController.currentWizard.selectAction}">
						<f:param name="stepName" value="#{step.pageName}"/>
					</h:commandLink>
					<h:outputText rendered="#{!step.visited || step.userdefined}" value="#{step.text}" styleClass="wizardProgressText"/>
					<f:verbatim></nobr></f:verbatim>
				</h:column>
				<h:column>
					<h:graphicImage value="#{style.resourceDir}/images/visited.png" rendered="#{step.visited && !step.current}" styleClass="wizardProgressGraphic"/>
					<h:graphicImage value="#{style.resourceDir}/images/current.png" rendered="#{step.visited &&  step.current}" styleClass="wizardProgressGraphic"/>
					<h:graphicImage value="#{style.resourceDir}/images/emptyStepState.png	" rendered="#{!step.visited}" styleClass="wizardProgressGraphic"/>
				</h:column>
			</h:dataTable>		
		</h:panelGrid>
		<h:panelGrid width="100%" styleClass="wizardContent" columns="1" cellpadding="0" cellspacing="0" border="0" rowClasses="wizardRowTitle,wizardRowPanel,wizardRowButtons" columnClasses="wizardContentColumn">
			<h:panelGrid width="100%" styleClass="wizardTitle" id="wizardTitle" columns="1" cellpadding="0" cellspacing="0" border="0">			
				<h:outputText styleClass="wizardTitleText" value="#{objectActionController.currentWizard.titleText}"/>
				<%-- escape = false because subtitles are generated with <br> for every linebreak --%>
				<h:outputText styleClass="wizardSubTitleText" escape="false" value="#{objectActionController.currentWizard.currentPanel.subTitleText}"/>
			</h:panelGrid>
			<h:panelGrid id="wizardPanel" binding="#{objectActionController.currentWizard.currentPanel.inputFieldContainer}"/>
			<h:panelGrid id="wizardButtons" cellpadding="0" cellspacing="2" columns="4" width="0%" width="100%" styleClass="wizardFooter" columnClasses="wizardFooterColumnButtons,wizardFooterColumnRemainder">
				<h:panelGroup>
					<f:verbatim><nobr></f:verbatim>
					<h:commandButton id="prev" styleClass="wizardButton" value="#{messages.button_previous}" action="#{objectActionController.currentWizard.back}" disabled="#{objectActionController.currentWizard.backButtonDisabled}" />
					<h:commandButton id="next" styleClass="wizardButton" value="#{messages.button_next}" action="#{objectActionController.currentWizard.next}" disabled="#{objectActionController.currentWizard.nextButtonDisabled}" />
					<h:commandButton id="finish" styleClass="wizardButton" value="#{objectActionController.currentWizard.finishText}" action="#{objectActionController.currentWizard.finish}" disabled="#{objectActionController.currentWizard.finishButtonDisabled}"/>
					<h:commandButton id="cancel" styleClass="wizardButton" value="#{messages.button_cancel}" action="#{objectActionController.currentWizard.cancel}" disabled="#{objectActionController.currentWizard.cancelButtonDisabled}"/>
					<f:verbatim></nobr></f:verbatim>
				</h:panelGroup>
				<h:outputText style="width:100%"></h:outputText>
			</h:panelGrid>
		</h:panelGrid>
	</h:panelGrid>
