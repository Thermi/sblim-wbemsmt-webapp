<%-- 
/**
 *  login.jsp
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
 * Author:     Marius Kreis
 *
 * Contributors:
 *
 */
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>
<f:loadBundle basename="#{style.resourceBundle}" var="styleMessages"/>
<f:view>
<html>
<head>
<h:outputText escape="false" value="<title>#{messages.webAppTitle}</title>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDir}/styles/login.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDir}/styles/main.css' rel='stylesheet' type='text/css'>"/>
</head>
<body class="loginBody">
<h:panelGrid width="100%" cellpadding="0" cellspacing="0" border="0" columns="1" styleClass="loginTable" rowClasses="loginTableRowHeader, loginTableRowMessages, loginTableRowCenter, loginTableRowCenter, loginTableRowFooter">
	<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" columns="2" styleClass="loginTableHeader" columnClasses="loginTableHeaderTitle, loginTableHeaderBanner" rendered="#{!style.embedded}">
		<h:graphicImage value="#{style.resourceDir}/images/title.gif" rendered="#{!style.embedded}" alt="#{messages.webAppTitle}" title="#{messages.webAppTitle}" />
		<h:graphicImage value="#{style.resourceDir}/images/banner.gif" rendered="#{!style.embedded}" alt="#{styleMessages.bannerTitle}" title="#{styleMessages.bannerTitle}"/>
	</h:panelGrid>
	<h:panelGroup>
	<%@ include file="/include_errorHandling.jsp" %>
	</h:panelGroup>
	<h:panelGroup>
		<f:verbatim>&nbsp;</f:verbatim>
	</h:panelGroup>
</h:panelGrid>
</body>
</html>
</f:view>