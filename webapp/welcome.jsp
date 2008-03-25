<%-- 
/**
 *  index.jsp
 *
 * © Copyright IBM Corp. 2005
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
<%@ taglib uri="http://sourceforge.net/projects/sblim/wbemsmt" prefix="w" %>

<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>
<f:view>
<html>
<head>
<h:outputText escape="false" value="<title>#{messages.webAppTitleAdmin}</title>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDir}/styles/admin.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDir}/styles/main.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDir}/styles/edit.css' rel='stylesheet' type='text/css'>"/>
</head>
<body>
<h:form>
<f:verbatim><br><br></f:verbatim>
<h:panelGrid columns="1" width="80%" align="center" headerClass="left">
<f:facet name="header">
<h:panelGroup>
	<f:verbatim><br></f:verbatim>
	<h:outputText styleClass="fieldCaption" value="#{messages.taskLauncherWelcome}"/>
	<f:verbatim><br><br></f:verbatim>
</h:panelGroup>
</f:facet>
<h:panelGroup>
	<h:panelGroup rendered="#{admin.welcomeSettingsEnabled}">
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.currentSettings}" styleClass="tableHeader"/>
	<f:verbatim><br><hr class="tableHeaderHR"><br><br></f:verbatim>
	<h:dataTable
		styleClass="multiLineTable" 
		cellpadding="0"
		cellspacing="0"
		align="center" 
		width="100%" 
		value="#{admin.hostTableForDisplay}" var="host" 
		columnClasses="multiLineContentFirst left,multiLineContent left,multiLineContent left,multiLineContent left,multiLineContentLast left" 
		rowClasses="multiLineRowWhite,multiLineRowGray"
		headerClass="multiLineHeader left adminTableHeaderHeight"
		> 
		<h:column>
		<f:facet name="header">
		<h:panelGroup styleClass="multiLineHeaderContent adminTableHeaderHeight">
		<h:outputText value="#{messages.hostname}"/>
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		
		</h:panelGroup>
		</f:facet>
		<h:outputText value="#{host.hostname}"/>
		</h:column>
		<h:column><f:facet name="header"><h:outputText value="#{messages.port}" styleClass="multiLineHeaderContent"></h:outputText></f:facet><h:outputText value="#{host.port}"></h:outputText></h:column>
		<h:column><f:facet name="header"><h:outputText value="#{messages.namespace}" styleClass="multiLineHeaderContent"></h:outputText></f:facet><h:outputText value="#{host.namespace}"></h:outputText></h:column>
		<h:column><f:facet name="header"><h:outputText value="#{messages.user}" styleClass="multiLineHeaderContent"></h:outputText></f:facet><h:outputText value="#{host.user}"></h:outputText></h:column>
		<h:column>
		<f:facet name="header">
		<h:panelGroup styleClass="multiLineHeaderContent">
		<h:outputText value="#{messages.tasks}"/>
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		</h:panelGroup>
		</f:facet>
		<h:outputText value="#{host.servicesAsString}"></h:outputText>		
		</h:column>
	</h:dataTable>
	</h:panelGroup>
    <h:panelGroup rendered="#{admin.welcomeTasksEnabled}">
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.installedTasks}" styleClass="tableHeader"/>
	<f:verbatim><br><hr class="tableHeaderHR"><br><br></f:verbatim>
	<h:dataTable
		styleClass="multiLineTable" 
		cellpadding="0"
		cellspacing="0"
		align="center" 
		width="100%" 
		value="#{admin.tasks}" var="task" 
		columnClasses="multiLineContentFirst left,multiLineContent left,multiLineContentLast left" 
		rowClasses="multiLineRowWhite,multiLineRowGray"
		headerClass="multiLineHeader left adminTableHeaderHeight"
		> 
		<h:column>
		<f:facet name="header">
		<h:panelGroup styleClass="multiLineHeaderContent adminTableHeaderHeight">
		<h:outputText value="#{messages.taskname}"/>
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		</h:panelGroup>
		</f:facet>
		<h:outputText value="#{task.name}"/>
		</h:column>

		<h:column>
		<f:facet name="header">
		<h:panelGroup styleClass="multiLineHeaderContent adminTableHeaderHeight">
		<h:outputText value="#{messages.available}"/>
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		</h:panelGroup> 
		</f:facet>
		<h:outputText value="#{task.bundleAvailable}"/>
		</h:column>

	</h:dataTable>
	</h:panelGroup>
	<f:verbatim><br/></f:verbatim>
	
</h:panelGroup>
<%@ include file="/include_errorHandling.jsp" %>
</h:panelGrid>
<h:panelGrid columns="1" width="80%" align="center">
<h:commandLink value="#{messages.loginSingle}" action="loginSingle" styleClass="adminLink" rendered="false"/>
<h:commandLink value="#{messages.loginMulti}" action="loginMulti" styleClass="adminLink" rendered="false"/>
</h:panelGrid>
</h:form>
</body>
</html>
</f:view>
