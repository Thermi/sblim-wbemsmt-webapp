<h:form id="messageForm" rendered="#{messageHandler.havingMessages}">
<h:panelGrid rendered="#{messageHandler.errors}" columns="2" columnClasses="messagePanelColumnIcon,messagePanelColumnText" id="errorMessagePanel" styleClass="messagePanel"> 
<h:graphicImage value="#{style.resourceDir}/images/inline-messages-error.gif" alt="#{messages.error}" title="#{messages.error}"/>
<h:dataTable value="#{messageHandler.errorMessages}" var="msg" border="0" cellspacing="2" width="100%" columnClasses="messagesImage,messagesSummary">
<h:column>
	<h:outputText value="#{msg.messageCode}" styleClass="errorMessageTitle" escape="false"/>
	<f:verbatim><br></f:verbatim>
	<h:outputText value="#{msg.messageText}" styleClass="errorMessageText" escape="false"/>
	<f:verbatim><br><br></f:verbatim>
	<%@ include file="include_messageInputButtons.jsp" %>
</h:column>
</h:dataTable>
<f:facet name="footer">
	<h:commandLink value="#{messages.close}" styleClass="messageCloseLink" onclick="hidePanel(this);return;"/>
</f:facet>
</h:panelGrid>

<h:panelGrid rendered="#{messageHandler.warnings}" columns="2" columnClasses="messagePanelColumnIcon,messagePanelColumnText" id="warningMessagePanel" styleClass="messagePanel"> 
<h:graphicImage value="#{style.resourceDir}/images/inline-messages-warning.gif" alt="#{messages.warning}" title="#{messages.warning}"/>
<h:dataTable value="#{messageHandler.warningMessages}" var="msg" border="0" cellspacing="2" width="100%" columnClasses="messagesImage,messagesSummary">
<h:column>
	<h:outputText value="#{msg.messageCode}" styleClass="warningMessageTitle" escape="false"/>
	<f:verbatim><br></f:verbatim>
	<h:outputText value="#{msg.messageText}" styleClass="warningMessageText" escape="false"/>
	<f:verbatim><br><br></f:verbatim>
	<%@ include file="include_messageInputButtons.jsp" %>
</h:column>
</h:dataTable>
<f:facet name="footer">
	<h:commandLink value="#{messages.close}" styleClass="messageCloseLink" onclick="hidePanel(this);return;"/>
</f:facet>
</h:panelGrid>

<h:panelGrid rendered="#{messageHandler.infos}" columns="2" columnClasses="messagePanelColumnIcon,messagePanelColumnText" id="infoMessagePanel" styleClass="messagePanel"> 
<h:graphicImage value="#{style.resourceDir}/images/inline-messages-info.gif" alt="#{messages.info}" title="#{messages.info}"/>
<h:dataTable value="#{messageHandler.infoMessages}" var="msg" border="0" cellspacing="2" width="100%" columnClasses="messagesImage,messagesSummary">
<h:column>
	<h:outputText value="#{msg.messageCode}" styleClass="infoMessageTitle" escape="false"/>
	<f:verbatim><br></f:verbatim>
	<h:outputText value="#{msg.messageText}" styleClass="infoMessageText" escape="false"/>
	<f:verbatim><br><br></f:verbatim>
	<%@ include file="include_messageInputButtons.jsp" %>
</h:column>
</h:dataTable>
<f:facet name="footer">
	<h:commandLink value="#{messages.close}" styleClass="messageCloseLink" onclick="hidePanel(this);return;"/>
</f:facet>
</h:panelGrid>

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


<h:panelGrid rendered="#{messageHandler.success}" columns="2" columnClasses="messagePanelColumnIcon,messagePanelColumnText" id="successMessagePanel" styleClass="messagePanel"> 
<h:graphicImage value="#{style.resourceDir}/images/inline-messages-success.gif" alt="#{messages.success}" title="#{messages.success}"/>
<h:dataTable value="#{messageHandler.successMessages}" var="msg" border="0" cellspacing="2" width="100%" columnClasses="messagesImage,messagesSummary">
<h:column>
	<h:outputText value="#{msg.messageCode}" styleClass="successMessageTitle" escape="false"/>
	<f:verbatim><br></f:verbatim>
	<h:outputText value="#{msg.messageText}" styleClass="successMessageText" escape="false"/>
	<f:verbatim><br><br></f:verbatim>
	<%@ include file="include_messageInputButtons.jsp" %>
</h:column>
</h:dataTable>
<f:facet name="footer">
	<h:commandLink value="#{messages.close}" styleClass="messageCloseLink" onclick="hidePanel(this);return;"/>
</f:facet>
</h:panelGrid>

<f:verbatim>
<script>
function hidePanel(link)
{
	var parent =  link.parentNode.parentNode.parentNode.parentNode.parentNode;
	parent.removeChild(link.parentNode.parentNode.parentNode.parentNode);
}
</script>
</f:verbatim>
</h:form>