<%-- 
/**
 *  index.jsp
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
 * Author:        Michael Bauschert
 *
 * Contributors:
 *
 */
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>

<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>
<html>
<head>
<link href="../styles/main.css" rel="stylesheet" type="text/css">
<f:view>
<h:outputText escape="false" value="<title>#{messages.webAppTitleAdmin}</title>"/>
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
}

.mytable {
	width : 100%;
	height : 100%;
}

.blue {
	background-color : #4477bb;
	background : #4477bb;
	height : 40%
}

.bigbold {
	color : black;
	font-weight : bolder;
	font-size : 2em;
	text-align:center;
}

.white {
	padding: 3em 3em 3em 3em;
}


.error {
	color : red;
	font-weight : bolder;
}

.header {
	background-color : #B7DDFB;
	font-weight : bolder;
}

.row1 {
	background-color : white;
	vertical-align:middle;
}

.row2 {
	background-color : #B7DDFB;
	vertical-align:middle;
}

.outerRow1 {
	background-color : white;
	text-align:center;
	valign:top;
}

.outerRow2 {
	background-color : white;
	text-align:left;
	valign:top;
}

.centered

.col1 {
}
.col2 {
}

</style>
</head>
<body>

<h:form>
<f:verbatim><br><br></f:verbatim>
<h:panelGrid columns="1" width="80%" align="center" headerClass="blue" rowClasses="outerRow1,outerRow2,outerRow2,outerRow2,outerRow2,outerRow2,outerRow2,outerRow2,outerRow2,outerRow2,outerRow2">
<f:facet name="header">
<h:panelGroup>
	<f:verbatim><br></f:verbatim>
	<h:outputText styleClass="bigbold" value="#{messages.taskLauncherAdminconsole}"/>
	<f:verbatim><br><br></f:verbatim>
</h:panelGroup>
</f:facet>

<h:panelGroup>
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.editConfiguredHosts}" rendered="#{!admin.slpMode}"/>
	<h:outputText value="#{messages.editSlpHosts}" rendered="#{admin.slpMode}"/>
</h:panelGroup>

<f:verbatim><br><br></f:verbatim>
<h:dataTable width="100%" value="#{admin.hostTable}" var="host" columnClasses="col1" rowClasses="row1,row2" headerClass="header" border="0" cellpadding="10" cellspacing="0"> 
	<h:column><h:selectBooleanCheckbox value="#{host.delete}" rendered="#{!host.new && !admin.slpMode}"></h:selectBooleanCheckbox></h:column>
	<h:column><h:selectBooleanCheckbox value="#{host.addToFile}" rendered="#{admin.slpMode}"></h:selectBooleanCheckbox></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.hostname}"></h:outputText></f:facet><h:inputText value="#{host.hostname}" size="30" disabled="#{admin.slpMode}"></h:inputText></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.port}"></h:outputText></f:facet><h:inputText value="#{host.port}" size="30" disabled="#{admin.slpMode}"></h:inputText></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.namespace}"></h:outputText></f:facet><h:inputText value="#{host.namespace}" size="30" disabled="#{admin.slpMode}"></h:inputText></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.user}"></h:outputText></f:facet><h:inputText value="#{host.user}" size="30" disabled="#{admin.slpMode}"></h:inputText></h:column>
	<h:column>
	<f:facet name="header">
	<h:panelGroup>
		<h:outputText value="#{messages.tasks}"/>
	</h:panelGroup>
	</f:facet>
	<h:dataTable  value="#{host.services}" var="service">
		<h:column>
			<h:selectBooleanCheckbox  value="#{service.enabled}" disabled="#{admin.slpMode}"></h:selectBooleanCheckbox>
		</h:column>
		<h:column>
			<h:outputText value="#{service.service}"></h:outputText>
		</h:column>
		<h:column>
			<h:outputText value="<nobr>#{service.configured ? '' : messages.not_configured}</nobr>" escape="false"></h:outputText>
		</h:column>
		<h:column>
			<h:outputText value="<nobr>#{service.installed ? '' : messages.not_installed}<nobr>" escape="false"></h:outputText>
		</h:column>
	</h:dataTable>
	</h:column>
</h:dataTable>
<h:panelGroup>
<h:graphicImage value="/images/arrow.gif"></h:graphicImage>
<h:commandButton value="#{messages.delete}" action="#{admin.deleteHosts}" rendered="#{!admin.slpMode}"/>
<h:commandButton value="#{messages.useSelectedSLPConfig}" action="#{admin.saveSelectedSlpHosts}" rendered="#{menueController.useSlp && admin.slpMode}"/>
</h:panelGroup>
<f:verbatim><br><br></f:verbatim>
<f:verbatim>&nbsp;</f:verbatim>
<h:commandButton value="#{messages.save}" action="#{admin.saveHost}" rendered="#{!admin.slpMode}"/>

<h:commandLink styleClass="admin" value="#{messages.showSLPConfig}" action="#{admin.loadSlpConfiguration}" rendered="#{menueController.useSlp && !admin.slpMode}"/>
<h:commandLink styleClass="admin" value="#{messages.reloadSLPConfig}" action="#{admin.loadSlpConfiguration}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="admin" value="#{messages.useAllSLPConfig}" action="#{admin.saveHost}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="admin" value="#{messages.useOldConfig}" action="#{admin.reloadFromFile}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="admin" value="#{messages.summary}" action="adminIndex"/>
<f:verbatim><br><br></f:verbatim>
<h:outputText value="#{messages.not_configured_hint}"></h:outputText>
<h:outputText value="#{messages.not_installed_hint}"></h:outputText>
<f:verbatim><br><br></f:verbatim>
<%@ include file="/include_errorHandling.jsp" %>
</h:panelGrid>
</h:form>
</f:view>
</body>
</html>
