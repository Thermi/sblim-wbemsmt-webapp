<%-- 
/**
 *  include_errorHandling.jsp
 *
 * © Copyright IBM Corp. 2008
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

<h:form id="messageForm">
<%@ include file="include_errorTables.jsp" %>

      <w:ajaxPanel id="showAsyncMessages" periodicalUpdate="#{messageHandler.asynchronousMessageUpdateInterval}">
		<h:panelGrid rendered="#{messageHandler.asynchronousMessages}" columns="2" columnClasses="messagePanelColumnIcon,messagePanelColumnText" id="asyncMessagePanel" styleClass="messagePanel"> 
			<h:graphicImage value="#{style.resourceDir}/images/inline-messages-info.gif" alt="#{messages.info}" title="#{messages.info}"/>
			<h:panelGrid border="0" cellspacing="0" width="100%" columns="1" style="height:0%">
				<h:commandLink actionListener="#{messageHandler.showAsynchronousMessages}">
					<h:outputText value="#{messageHandler.newMessagesCode}" styleClass="treeLink bold" escape="false"/>
			    </h:commandLink>
				<h:commandLink actionListener="#{messageHandler.showAsynchronousMessages}" styleClass="treeLink">
					<h:outputText value="#{messageHandler.newMessagesText}" escape="false"/>
			    </h:commandLink>
				<f:facet name="footer">
					<h:commandLink value="#{messages.close}" styleClass="messageCloseLink" onclick="hidePanel(this);return;"/>
				</f:facet>
			</h:panelGrid>
		</h:panelGrid>
      </w:ajaxPanel>


</h:form>