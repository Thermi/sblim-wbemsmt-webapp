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
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>
<f:view>
<html>
<head>
<h:outputText escape="false" value="<title>#{messages.webAppTitleAdmin}</title>"/>
<h:outputText escape="false" value="<script type='text/javascript' src='../scripts/tooltip.js'></script>"/>
<h:outputText escape="false" value="<link href='../#{style.resourceDir}/styles/admin.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='../#{style.resourceDir}/styles/main.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='../#{style.resourceDir}/styles/edit.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='../#{style.resourceDir}/styles/tooltip.css' rel='stylesheet' type='text/css'>"/>
</head>
<body>

<h:form id="adminForm">
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
	<f:verbatim><br><br></f:verbatim>
	<%@ include file="../include_errorHandling.jsp" %>
</h:panelGroup>

	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.hosts}" styleClass="tableHeader"/>
	<f:verbatim><br><hr class="tableHeaderHR"><br><br></f:verbatim>
<h:dataTable 
	width="100%" 
	value="#{admin.hostTable}" var="host" 
	columnClasses="multiLineContentFirst left  topAlignment topPadding,multiLineContent left topAlignment topPadding,multiLineContent left topAlignment topPadding,multiLineContent left topAlignment topPadding,multiLineContent left topAlignment topPadding,multiLineContentLast left nopadding topAlignment topPadding" 
	rowClasses="multiLineRowWhite,multiLineRowGray"
	headerClass="multiLineHeader left adminTableHeaderHeight"
	cellpadding="0" cellspacing="0" > 
	<h:column ><h:panelGroup>
			  <h:selectBooleanCheckbox value="#{host.delete}" rendered="#{!host.new && !admin.slpMode}"></h:selectBooleanCheckbox>
	          <h:selectBooleanCheckbox value="#{host.addToFile}" rendered="#{admin.slpMode}"></h:selectBooleanCheckbox>
	          <f:verbatim>&nbsp;</f:verbatim>
			  </h:panelGroup>
	</h:column>
	          
	<h:column><f:facet name="header"><h:outputText value="#{messages.protocol}"></h:outputText></f:facet>
		<h:selectOneMenu value="#{host.protocol}" disabled="#{admin.slpMode}" id="protocol" styleClass="comboBox" style="width:60px">
			<f:selectItem itemValue="http" itemLabel="http"/>
			<f:selectItem itemValue="https" itemLabel="https"/>
		</h:selectOneMenu>
	</h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.hostname}"></h:outputText></f:facet><h:inputText value="#{host.hostInfo}" size="30" disabled="#{admin.slpMode}" onblur="#{host.onBlur}"></h:inputText></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.port}"></h:outputText></f:facet><h:inputText value="#{host.port}" size="12" disabled="#{admin.slpMode}"></h:inputText></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.user}"></h:outputText></f:facet><h:inputText value="#{host.user}" size="15" disabled="#{admin.slpMode}"></h:inputText></h:column>

	<h:column>
	<f:facet name="header">
	<h:panelGroup>
		<h:outputText value="#{messages.tasks}"/>
	</h:panelGroup>
	</f:facet>
	<h:dataTable  value="#{host.services}" var="service" columnClasses="width0 topAlignment,width0 topAlignment toppaddingServiceTexts,width0 topAlignment,width0 topAlignment,width100 topAlignment" cellpadding="0" cellspacing="0" width="100%">
		<h:column>
			<h:selectBooleanCheckbox  value="#{service.enabled}" onchange="#{admin.clickBlindButton}" disabled="#{admin.slpMode || host.new}"></h:selectBooleanCheckbox>
		</h:column>
		<h:column>
			<h:outputText value="#{service.reference.name}"></h:outputText>
		</h:column>
		<h:column rendered="#{!admin.slpMode && (!service.installed || !service.configured) }">
			<h:panelGroup>
				<h:outputText value="<nobr>#{service.configured ? '' : messages.not_configured}</nobr>" escape="false"></h:outputText>
				<h:outputText value="<br>" escape="false" rendered="#{!service.installed && !service.configured }"></h:outputText>
				<h:outputText value="<nobr>#{service.installed ? '' : messages.not_installed}<nobr>" escape="false"></h:outputText>
			</h:panelGroup>
		</h:column>
		<h:column rendered="#{!admin.slpMode && service.installed && service.configured }">
			<h:panelGroup>
			<h:dataTable var="item" value="#{service.configurationItems}" columnClasses="width100 topAlignment toppaddingServiceTexts,width0 topAlignment,width0 topAlignment" cellpadding="0" cellspacing="0">
				<h:column>
						<h:outputText value="#{item.value.name}" style="white-space:no-wrap;padding-right:5px"/>
				</h:column>
				<h:column>
						<h:inputText value="#{item.value.value}" disabled="#{!service.enabled}"/>
				</h:column>
				<h:column>
						<h:commandLink onmouseover="showTooltip(event,'#{item.infoTranslated}')" onmouseout="hideTooltip(event)" onclick="return false">
							<h:graphicImage value="../images/info.gif" styleClass="noBorder"/>
						</h:commandLink>
				</h:column>
			</h:dataTable>
			</h:panelGroup>
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
<h:panelGroup>
	<h:selectBooleanCheckbox id="bulkChanges" value="#{admin.bulkChanges}" disabled="#{admin.slpMode}"></h:selectBooleanCheckbox>
	<h:outputText value="#{messages.bulkChanges}"/>
</h:panelGroup>
<h:commandButton value="#{messages.save}" action="#{admin.saveHost}" rendered="#{!admin.slpMode}"/>
<h:commandButton value="#{messages.save}" id="blindSubmit" action="#{admin.saveHostSilent}" rendered="#{!admin.slpMode}" style="visibility:hidden"/>
<f:verbatim><br><br></f:verbatim>

<h:commandLink styleClass="adminLink" value="#{messages.showSLPConfig}" action="#{admin.loadSlpConfiguration}" rendered="#{menueController.useSlp && !admin.slpMode}"/>
<h:commandLink styleClass="adminLink" value="#{messages.reloadSLPConfig}" action="#{admin.loadSlpConfiguration}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="adminLink" value="#{messages.useAllSLPConfig}" action="#{admin.saveHost}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="adminLink" value="#{messages.useOldConfig}" action="#{admin.reloadFromFile}" rendered="#{menueController.useSlp && admin.slpMode}"/>
<h:commandLink styleClass="adminLink" value="#{messages.summary}" action="#{admin.showSummary}" actionListener="#{admin.selectAllHosts}"/>
<f:verbatim><br><br></f:verbatim>
<h:outputText value="#{messages.not_configured_hint}"></h:outputText>
<h:outputText value="#{messages.not_installed_hint}"></h:outputText>
</h:panelGrid>
</h:form>

<t:div id="tooltipPanel" forceId="true" styleClass="tip">
	<h:outputText id="tooltiptext">test</h:outputText>
</t:div>

</body>
</html>
</f:view>
