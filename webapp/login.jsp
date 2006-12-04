<%-- 
/**
 *  login.jsp
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
 * Author:     Marius Kreis
 *
 * Contributors:
 *
 */
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>
<f:view>
<html>
<head>
<h:outputText escape="false" value="<title>#{messages.webAppTitle}</title>"/>
<style type="text/css">
BODY, TD {
	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size : smaller;
	margin : 0;
}

INPUT, SELECT, TEXTAREA {
	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size: 1em;
	border : 1px solid #4477bb;
	background-color : #ffffff;
	color : #000000;
	background : #ffffff;
}

TABLE {
	border : 0;
	//padding : 0;
}

.mytable {
	width : 100%;
	height : 100%;
}

.blue {
	background-color : #4477bb;
	background : #4477bb;
	font-size : 2em;
	vertical-align : bottom;
	color : #ffffff;
	height : 40%;
}

.white {
	padding: 3em 3em 3em 3em;
}

.bigbold {
	color : #dddddd;
	font-size : bolder;
}

.error
{
	color : red;
	font-size: small;
}

TD.lang
{
	text-align:left;
	vertical-align:middle;	
	width: 50%;
}

TD.version
{
	text-align:right;	
	vertical-align:middle;	
	width: 50%;
}

TABLE.langAndVersion
{
	width: 100%;
	text-align:right;	
	font-size: 50%;
	vertical-align:middle;	
}

TD.messagesImage
{
	width: 0%;
	vertical-align: middle;
}

TD.messagesSummary
{
	width: 100%;
	vertical-align: middle;
}


</style>
</head>
<body>
<h:panelGrid columns="1" styleClass="mytable" rowClasses="blue, white">

<h:panelGroup>
	<h:outputText styleClass="bigbold" value="#{messages.appTitle}"/><f:verbatim><br></f:verbatim>
	<h:outputText value="#{messages.pleaseLogin}" rendered="#{!loginCheck.loginDisabled}"/>
</h:panelGroup>

<h:panelGroup rendered="#{!loginCheck.loginDisabled}">
<h:outputText value="#{messages.presets}" rendered="false"/>
<h:form  rendered="false">
<h:selectOneMenu binding="#{loginCheck.presetSelect}" valueChangeListener="#{loginCheck.presetChange}" disabled="#{loginCheck.loginDisabled}" onchange="submit();" style="width:300px"/>
</h:form>
		
<h:form id="connectFields">
<h:panelGrid columns="5">

	<h:outputText value="#{messages.hostname}"/>
	<h:outputText value="#{messages.port}"/>
	<h:outputText value="#{messages.namespace}"/>
	<h:outputText value="#{messages.user}"/>
	<h:outputText value="#{messages.password}"/>

	<h:inputText value="#{loginCheck.hostname}" disabled="#{loginCheck.loginDisabled}" style="width:300px" id="host"/>
	<h:inputText value="#{loginCheck.port}" disabled="#{loginCheck.loginDisabled}" size="5" id="port"/>
	<h:inputText value="#{loginCheck.namespace}" disabled="#{loginCheck.loginDisabled}"  id="namespace"/>
	<h:inputText value="#{loginCheck.username}" disabled="#{loginCheck.loginDisabled}"  id="username"/>
	<h:inputSecret value="#{loginCheck.password}" redisplay="true" disabled="#{loginCheck.loginDisabled}"  id="password"/>

</h:panelGrid>
<h:panelGroup  rendered="#{menueController.useSlp}">
<h:selectBooleanCheckbox  value="#{loginCheck.useSlp}"></h:selectBooleanCheckbox><h:outputText value="#{messages.useSlp}"  id="useSlp"/>
</h:panelGroup>
<f:verbatim><br><br></f:verbatim>
<h:commandButton value="#{messages.login}" actionListener="#{loginCheck.login}" action="#{loginCheck.loginAction}" disabled="#{loginCheck.loginDisabled}" id="login"/>
<br>
<%@ include file="include_errorHandling.jsp" %>
</h:form>
</h:panelGroup>

<h:panelGroup rendered="#{loginCheck.loginDisabled}">
<h:outputText styleClass="error" value="#{loginCheck.loginDisabledMsg}" rendered="#{loginCheck.loginDisabled}"/>
</h:panelGroup>

<h:panelGroup rendered="#{loginCheck.loginDisabled}">
<f:verbatim>&nbsp;</f:verbatim>
</h:panelGroup>
<h:panelGrid columns="2" columnClasses="lang,version" styleClass="langAndVersion"> 
	<h:form id="langform">
		<h:panelGroup><f:verbatim>&nbsp;</f:verbatim></h:panelGroup>
		<h:panelGroup>
		<h:commandLink actionListener="#{localeManager.setEnglish}" id="en" ><h:graphicImage url="/images/us.gif" style="border:0px"/></h:commandLink>
		<h:commandLink actionListener="#{localeManager.setGerman}" id="de"><h:graphicImage url="/images/de.gif" style="border:0px"/></h:commandLink>
		</h:panelGroup>
	</h:form>
	<h:outputText value="#{messages.version}: #{objectActionController.currentVersion}"/>
</h:panelGrid>
</h:panelGrid>

</f:view>
</body>
</html>
