<h:panelGrid id="waitDlg" 
         style="visibility:hidden" 
         columns="2" 
         styleClass="waitDlg" rowClasses="waitDlgMsgRow,waitDlgCloseRow"
         cellpadding="0" cellspacing="0">
	<h:graphicImage value="#{style.resourceDir}/images/wait.gif" alt="#{messages.wait}" title="#{messages.wait}" id="waitImage"/>
	<h:outputText value="" id="waitMsg" styleClass="waitMsg"/>
	<h:panelGroup>
		  <h:form style="margin: 0px; padding: 0px;">
          <f:verbatim><br/></f:verbatim>
		  <h:commandLink value="#{messages.close}" styleClass="waitCloseLink" onclick="hideWait();return;"/>          
		  </h:form>
	</h:panelGroup>
	<f:verbatim>&nbsp;</f:verbatim>
</h:panelGrid>