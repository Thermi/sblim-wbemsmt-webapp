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
	text-align:left;
}

.row1 {
	background-color : white;
	valign:top;
	text-align:center;
}

.row2 {
	background-color : #B7DDFB;
	valign:top;
	text-align:center;
}

.embeddedRow1 {
	background-color : white;
	valign:top;
	text-align:left;
}

.embeddedRow2 {
	background-color : #B7DDFB;
	valign:top;
	text-align:left;
}

.embeddedTable {
}

.col1 {
	vertical-align:top;
}
.col2 {
	vertical-align:top;
}

</style>
</head>
<body>
<h:form>
<f:verbatim><br><br></f:verbatim>
<h:panelGrid columns="1" width="80%" align="center" headerClass="blue" rowClasses="row1">
<f:facet name="header">
<h:panelGroup>
	<f:verbatim><br></f:verbatim>
	<h:outputText styleClass="bigbold" value="#{messages.taskLauncherAdminconsole}"/>
	<f:verbatim><br><br></f:verbatim>
</h:panelGroup>
</f:facet>
<h:panelGroup rendered="#{admin.adminEnabled}">
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.welcomeAdmin}"/>
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.currentSettings}" style="font-weight:bold"/>
	<f:verbatim><br><br></f:verbatim>
	<h:dataTable cellpadding="10" styleClass="embeddedTable" align="center" width="100%" value="#{admin.hostTableForDisplay}" var="host" columnClasses="col1,col2" rowClasses="embeddedRow1,embeddedRow2" headerClass="header" border="0"> 
		<h:column>
		<f:facet name="header">
		<h:panelGroup>
		<h:outputText value="#{messages.hostname}"/>
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		
		</h:panelGroup>
		</f:facet>
		<h:outputText value="#{host.hostname}"/>
		</h:column>
		<h:column><f:facet name="header"><h:outputText value="#{messages.port}"></h:outputText></f:facet><h:outputText value="#{host.port}"></h:outputText></h:column>
		<h:column><f:facet name="header"><h:outputText value="#{messages.namespace}"></h:outputText></f:facet><h:outputText value="#{host.namespace}"></h:outputText></h:column>
		<h:column><f:facet name="header"><h:outputText value="#{messages.user}"></h:outputText></f:facet><h:outputText value="#{host.user}"></h:outputText></h:column>
		<h:column>
		<f:facet name="header">
		<h:panelGroup>
		<h:outputText value="#{messages.tasks}"/>
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		</h:panelGroup>
		</f:facet>
		<h:outputText value="#{host.servicesAsString}"></h:outputText>		
		</h:column>
	</h:dataTable>
	<f:verbatim><br/></f:verbatim>
	
</h:panelGroup>
<h:outputText styleClass="error" value="#{admin.adminDisabledMsg}" rendered="#{!admin.adminEnabled}"/>
<%@ include file="/include_errorHandling.jsp" %>
</h:panelGrid>
<h:panelGrid columns="1" width="80%" align="center" rendered="#{admin.adminEnabled}">
<h:commandLink styleClass="admin" value="#{messages.edithost}" action="adminHost"/>
<h:commandLink styleClass="admin" value="#{messages.showSLPConfig}" action="#{admin.loadSlpConfiguration}" rendered="#{menueController.useSlp && !admin.slpMode }"/>
</h:panelGrid>
</h:form>
</f:view>
</body>
</html>
