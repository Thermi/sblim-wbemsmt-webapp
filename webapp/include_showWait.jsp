<h:panelGrid id="waitDlg" style="visibility:hidden" columns="2" styleClass="waitDlg" cellpadding="5" cellspacing="5">
	<h:graphicImage value="#{style.resourceDir}/images/wait.gif" alt="#{messages.wait}" title="#{messages.wait}" id="waitImage"/>
	<h:outputText value="" id="waitMsg" styleClass="waitMsg"/>
</h:panelGrid>