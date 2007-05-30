<h:panelGrid id="waitDlg" 
         style="visibility:hidden" 
         columns="1" 
         styleClass="waitDlg" rowClasses="waitDlgMsgRow,waitDlgCloseRow"
         cellpadding="0" cellspacing="0">
	<h:panelGroup>
		<h:graphicImage value="#{style.resourceDir}/images/wait.gif" alt="#{messages.wait}" title="#{messages.wait}" id="waitImage" style="vertical-align:middle"/>
		<f:verbatim>&nbsp;</f:verbatim>
		<h:outputText value="" id="waitMsg" styleClass="waitMsg" style="vertical-align:middle"/>
	</h:panelGroup>
	<h:panelGroup>
		  <h:form style="margin: 0px; padding: 0px;">
          <f:verbatim><br/></f:verbatim>
		  <h:commandLink value="#{messages.close}" styleClass="waitCloseLink" onclick="hideWait();return;"/>          
		  </h:form>
	</h:panelGroup>
</h:panelGrid>

<h:panelGrid id="updateIntervalToolbox" 
         style="visibility:hidden" 
         columns="1" 
         styleClass="waitDlg" rowClasses="waitDlgMsgRow,waitDlgCloseRow"
         cellpadding="0" cellspacing="0">
	<h:panelGroup>
		<h:form id="updateIntervalForm">
			<f:verbatim>&nbsp;</f:verbatim>
			<h:outputText value="#{messages.updateInterval}<br><br>" styleClass="waitMsg" escape="false"/>
			<f:verbatim>&nbsp;</f:verbatim>
			<h:inputText value="#{objectActionController.updateIntervalValue}" id="updateIntervalInput"/>
			<h:inputHidden value="#{objectActionController.updateIntervalKey}" id="updateIntervalHidden"/>
			<h:commandButton action="#{objectActionController.setInterval}" value="#{messages.ok}"/>
		</h:form>
	</h:panelGroup>
	<h:panelGroup>
		  <h:form style="margin: 0px; padding: 0px;">
          <f:verbatim><br/></f:verbatim>
		  <h:commandLink value="#{messages.close}" styleClass="waitCloseLink" onclick="toggleToolbox();return;"/>          
		  </h:form>
	</h:panelGroup>
</h:panelGrid>

