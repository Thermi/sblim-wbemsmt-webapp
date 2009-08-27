<h:outputText value="#{messageHandler.updateMessages}"/>
<h:form id="messageForm1" rendered="#{messageHandler.havingMessages}">
<%@ include file="include_errorTables.jsp" %>
<f:verbatim>
	<script>
	var msgForm =  document.messageForm;
	var msgForm2 =  document.messageForm1;
	if (msgForm && msgForm2 )
	{ 
	    while (msgForm2.childNodes.length > 0)
	   	{
	   	   msgForm.appendChild(msgForm2.removeChild(msgForm2.childNodes[0]))
	   	}
		
	}
    </script>
 </f:verbatim>
</h:form>