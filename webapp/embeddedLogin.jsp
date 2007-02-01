<%-- 
/**
 *  loginFile.jsp
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
 * Author:    Michael.Bauschert@de.ibm.com
 *
 * Contributors:
 *
 */
--%>


<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>

<script>
function submitLikeJSF()
{
	document.forms[0].elements['test:task'].value = '<%=request.getParameter("task")%>';
	document.forms[0].elements['test:target'].value = '<%=request.getParameter("target")%>';
	document.forms[0].elements['test:cmd1'].click();
}
</script>
<style>
body { 
    font-family: Verdana, Arial, Helvetica, sans-serif;
    font-size: x-small;
    background-color: #FFFFFF;
    margin: 0px;
    padding: 0px;
}
</style>

<body onLoad="submitLikeJSF();">

<f:view>
	<f:verbatim>
	<br>
	</f:verbatim>
	<h:outputText value="#{messages.redirecting}" />
	<h:form id="test">
		<h:inputHidden id="task" value="#{loginCheck.task}" />
		<h:inputHidden id="target" value="#{loginCheck.target}" />
		<h:commandButton action="#{loginCheck.loginEmbedded}" value="Test" id="cmd1" style="visibility:hidden" />
	</h:form>
</f:view>
<body>
