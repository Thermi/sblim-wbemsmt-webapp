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
<f:view>
<html>
<head>
<h:outputText escape="false" value="<title>#{messages.webAppTitleAdmin}</title>"/>
<h:outputText escape="false" value="<link href='../#{style.resourceDir}/styles/admin.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='../#{style.resourceDir}/styles/main.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='../#{style.resourceDir}/styles/edit.css' rel='stylesheet' type='text/css'>"/>
</head>
<body>

<h:form>
<f:verbatim><br><br></f:verbatim>
<h:panelGrid columns="1" width="80%" align="center" headerClass="left" >
<f:facet name="header">
<h:panelGroup>
	<f:verbatim><br></f:verbatim>
	<h:outputText styleClass="fieldCaption" value="#{messages.taskLauncherAdminconsole}"/>
	<f:verbatim><br><br></f:verbatim>
</h:panelGroup>
</f:facet>

<h:panelGroup>
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.editConfiguredHosts}" rendered="#{!admin.slpMode}" styleClass="fieldLabel"/>
	<h:outputText value="#{messages.editSlpHosts}" rendered="#{admin.slpMode}" styleClass="fieldLabel"/>
</h:panelGroup>

	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.hosts}" styleClass="tableHeader"/>
	<f:verbatim><br><hr class="tableHeaderHR"><br><br></f:verbatim>
<h:dataTable 
	width="100%" 
	value="#{admin.hostTable}" var="host" 
	columnClasses="multiLineContentFirst left,multiLineContent left,multiLineContent left,multiLineContent left,multiLineContent left,multiLineContentLast left" 
	rowClasses="multiLineRowWhite,multiLineRowGray"
	headerClass="multiLineHeader left adminTableHeaderHeight"
	cellpadding="0" cellspacing="0" > 
	<h:column><h:panelGroup>
			  <h:selectBooleanCheckbox value="#{host.delete}" rendered="#{!host.new && !admin.slpMode}"></h:selectBooleanCheckbox>
	          <h:selectBooleanCheckbox value="#{host.addToFile}" rendered="#{admin.slpMode}"></h:selectBooleanCheckbox>
	          <f:verbatim>&nbsp;</f:verbatim>
			  </h:panelGroup>
	</h:column>
	          
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
<h:commandButton value="#{messages.save}" action="#{admin.saveHost}" rendered="#{!admin.slpMode}"/>
<f:verbatim><br><br></f:verbatim>

<h:commandLink styleClass="adminLink" value="#{messages.showSLPConfig}" action="#{admin.loadSlpConfiguration}" rendered="#{menueController.useSlp && !admin.slpMode}"/>
<h:commandLink styleClass="adminLink" value="#{messages.reloadSLPConfig}" action="#{admin.loadSlpConfiguration}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="adminLink" value="#{messages.useAllSLPConfig}" action="#{admin.saveHost}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="adminLink" value="#{messages.useOldConfig}" action="#{admin.reloadFromFile}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="adminLink" value="#{messages.summary}" action="adminIndex"/>
<f:verbatim><br><br></f:verbatim>
<h:outputText value="#{messages.not_configured_hint}"></h:outputText>
<h:outputText value="#{messages.not_installed_hint}"></h:outputText>
<f:verbatim><br><br></f:verbatim>
<%@ include file="/include_errorHandling.jsp" %>
</h:panelGrid>
</h:form>
</body>
</html>
</f:view>
