<%-- 
/**
 *  loginMulti.jsp
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

<%
	System.err.println("Request-Locale is " + request.getParameter("locale"));
	if (request.getParameter("locale") != null)
	{
		session.setAttribute("locale",new java.util.Locale(request.getParameter("locale")));
		org.sblim.wbemsmt.webapp.jsf.LocaleManagerBean lm = (org.sblim.wbemsmt.webapp.jsf.LocaleManagerBean)session.getAttribute("localeManager");
		if (lm != null)
		{
			lm.setCurrentLocale(new java.util.Locale(request.getParameter("locale")));
		}
	}
	
%>

<script>
function submitLikeJSF()
{
	document.forms[0].elements['test:useSlp'].value = '<%="true".equalsIgnoreCase(request.getParameter("useSlp"))%>';
	
	<% String forward = request.getParameter("forward") == null ? "true" : request.getParameter("forward");
	   if ("true".equalsIgnoreCase(forward)) {
	%>
	   document.forms[0].elements['test:cmd1'].click();
	<% } else { %>
	   document.forms[0].elements['test:cmd1'].style.visibility = "visible";
	<% } %>
}
</script>
<body onLoad="submitLikeJSF();">
<f:view>
	<h:outputText value="#{messages.redirecting}" />
	<h:form id="test">
		<h:commandButton action="#{loginCheck.multiWithoutLogin}" value="Test" id="cmd1" style="visibility:hidden" />
		<h:inputHidden id="useSlp" value="#{loginCheck.useSlp}" />
	</h:form>
</f:view>
<body>
