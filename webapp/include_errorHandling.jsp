<h:dataTable value="#{messageHandler.messages}" var="msg" border="0" cellspacing="2" width="100%" columnClasses="messagesImage,messagesSummary">
<h:column>
	<h:graphicImage value="#{messageHandler.images[msg.severity]}"/>
</h:column>
<h:column>
	<h:outputText value="#{msg.summary}" style="color: #{messageHandler.colors[msg.severity]};"/>
</h:column>
</h:dataTable>
