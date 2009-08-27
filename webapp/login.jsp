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
<h:outputText escape="false" value="<link href='#{style.resourceDirAbsolute}/styles/login.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDirAbsolute}/styles/main.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<script src='scripts/showWait.js'></script>"/>
</head>
<body class="loginBody">
<h:panelGrid width="100%" cellpadding="0" cellspacing="0" border="0" columns="1" styleClass="loginTable" rowClasses="loginTableRowHeader, loginTableRowMessages, loginTableRowCenter, loginTableRowCenter, loginTableRowFooter">
	<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" columns="2" styleClass="loginTableHeader" columnClasses="loginTableHeaderTitle, loginTableHeaderBanner" rendered="#{!style.embedded}">
		<h:graphicImage value="#{style.resourceDir}/images/title.gif" rendered="#{!style.embedded}" alt="#{messages.webAppTitle}" title="#{messages.webAppTitle}" />
		<h:graphicImage value="#{style.resourceDir}/images/banner.gif" rendered="#{!style.embedded}" alt="#{styleMessages.bannerTitle}" title="#{styleMessages.bannerTitle}"/>
	</h:panelGrid>
	<h:panelGroup>
	<%@ include file="include_errorHandling.jsp" %>
	</h:panelGroup>
	<h:panelGrid styleClass="loginTableCenter" cellpadding="5" columnClasses="loginTableCenterColumn">
		<h:form id="connectFields">
		<h:panelGrid cellpadding="0" cellspacing="0" border="0" columns="1">


			<h:outputText value="#{messages.pleaseLogin}" rendered="#{!loginCheck.loginDisabled}" styleClass="fieldCaption"/>

			<h:outputText value="#{messages.protocol}" styleClass="fieldLabel"/>
			<h:selectOneMenu value="#{loginCheck.protocol}" disabled="#{loginCheck.loginDisabled}" style="width:60px" id="protocol" styleClass="comboBox">
					<f:selectItem itemValue="http" itemLabel="http"/>
					<f:selectItem itemValue="https" itemLabel="https"/>
			</h:selectOneMenu>

			<h:outputText value="#{messages.hostname}" styleClass="fieldLabel"/>
			<h:inputText value="#{loginCheck.hostname}" disabled="#{loginCheck.loginDisabled}" style="width:300px" id="host"  styleClass="textField" size="50"/>

			<h:outputText value="#{messages.port}" styleClass="fieldLabel"/>
			<h:inputText value="#{loginCheck.port}" disabled="#{loginCheck.loginDisabled}" size="12" id="port" styleClass="textField"/>

			<h:outputText value="#{messages.user}" styleClass="fieldLabel"/>
			<h:inputText value="#{loginCheck.username}" disabled="#{loginCheck.loginDisabled}"   size="25" id="username" styleClass="textField"/>

			<h:outputText value="#{messages.password}" styleClass="fieldLabel"/>
			<h:inputSecret value="#{loginCheck.password}" redisplay="true" disabled="#{loginCheck.loginDisabled}"  size="25" id="password" styleClass="textField"/>

			<h:panelGroup>
				<h:selectBooleanCheckbox id="remind" value="#{loginCheck.remindLoginData}" />
				<h:outputText value="#{messages.remindData}"/>
			</h:panelGroup>
			<h:panelGroup  rendered="#{menueController.useSlp}">
			<h:selectBooleanCheckbox  id="useSlpCheckbox" value="#{loginCheck.useSlp}" styleClass="checkbox"/><h:outputText value="#{messages.useSlp}"  id="useSlp"  styleClass="fieldLabel"/>
			</h:panelGroup>
			
			<f:verbatim escape="false"><br><br></f:verbatim>

			<h:commandButton 
			       value="#{messages.login}"
			       actionListener="#{loginCheck.login}"
			       action="#{loginCheck.loginAction}"
			       disabled="#{loginCheck.loginDisabled}"
			       onclick="#{loginCheck.javascriptShowWait}"
			       id="login"
			       styleClass="submitButton"/>

		</h:panelGrid>
		</h:form>
	</h:panelGrid>
	<h:form id="langform" styleClass="languageForm" rendered="#{!style.embedded}">
	<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" columns="2" columnClasses="loginFooterLanguage, loginFooterVersion" styleClass="loginFooter"> 
			<h:panelGroup>
			<h:commandLink actionListener="#{localeManager.setEnglish}" id="en" ><h:graphicImage url="/images/us.gif" styleClass="imageLink" alt="#{messages.language_english}" title="#{messages.language_english}"/></h:commandLink>
			<f:verbatim>&nbsp;</f:verbatim>
			<h:commandLink actionListener="#{localeManager.setGerman}" id="de"><h:graphicImage url="/images/de.gif" styleClass="imageLink" alt="#{messages.language_german}" title="#{messages.language_german}"/></h:commandLink>
			</h:panelGroup>
			<h:outputText value="#{messages.version}: #{objectActionController.currentVersion}" styleClass="fieldLabel"/>
	</h:panelGrid>
	</h:form>
</h:panelGrid>

<%@ include file="include_showWait.jsp" %>


</body>
</html>
	<h:panelGroup>
	<%@ include file="include_errorHandlingBottom.jsp" %>
	</h:panelGroup>
</f:view>