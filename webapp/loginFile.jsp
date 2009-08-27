<%-- 
/**
 *  loginFile.jsp
 *
 * © Copyright IBM Corp.  2009,2005
 *
 * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
 * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
 * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
 *
 * You can obtain a current copy of the Eclipse Public License from
 * http://www.opensource.org/licenses/eclipse-1.0.php
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
	document.forms[0].elements['test:cmd1'].click();
}
</script>
<body onLoad="submitLikeJSF();">

<f:view>
	<h:graphicImage url="/images/sleep.gif" style="border:0px"/>
	<f:verbatim>
	<br>
	</f:verbatim>
	<h:outputText value="#{messages.redirecting}" />
	<h:form id="test">
		<h:commandButton action="#{loginCheck.forward}" value="Test" id="cmd1" style="visibility:hidden" />
	</h:form>
</f:view>
<body>
