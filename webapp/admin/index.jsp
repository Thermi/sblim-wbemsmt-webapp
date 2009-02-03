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
 * Called after the redirect by index.html
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
<h:form>
<f:verbatim><br><br></f:verbatim>
<h:panelGrid columns="1" width="80%" headerClass="left">
<f:facet name="header">
<h:panelGroup>
	<f:verbatim><br></f:verbatim>
	<h:outputText styleClass="fieldCaption" value="#{messages.taskLauncherAdminconsole}"/>
	<f:verbatim><br><br></f:verbatim>
</h:panelGroup>
</f:facet>
<h:panelGroup rendered="#{admin.adminEnabled}">
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.welcomeAdmin}" styleClass="fieldLabel"/>
	<f:verbatim><br><br></f:verbatim>
	<%@ include file="/include_errorHandling.jsp" %>
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.currentSettings}" styleClass="tableHeader"/>
	<f:verbatim><br><hr class="tableHeaderHR"><br><br></f:verbatim>
	<!--  var host is an instance of HostEntry -->
	<h:dataTable
		styleClass="multiLineTable" 
		cellpadding="0"
		cellspacing="0"
		width="100%" 
		value="#{admin.hostTableForDisplay}" var="host" 
		columnClasses="multiLineContentFirst left topAlignment topPadding,multiLineContent left topAlignment topPadding,multiLineContentLast left nopadding topAlignment topPadding" 
		rowClasses="multiLineRowWhite,multiLineRowGray"
		headerClass="multiLineHeader left adminTableHeaderHeight"> 
		<h:column>
		<f:facet name="header">
		<h:panelGroup styleClass="multiLineHeaderContent adminTableHeaderHeight">
		<h:outputText value="#{messages.url}"/>
		<f:verbatim>&nbsp;&nbsp;</f:verbatim>
		
		</h:panelGroup>
		</f:facet>
		
		<h:commandLink styleClass="adminLink" value="#{host.protocol}://#{host.hostInfo}:#{host.portAsInt}" action="adminHost" actionListener="#{admin.selectSingleHost}"> 
			<f:param name="hostname" value="#{host}"/>
		</h:commandLink>
		
		
		</h:column>
		<h:column><f:facet name="header"><h:outputText value="#{messages.user}" styleClass="multiLineHeaderContent"></h:outputText></f:facet><h:outputText value="#{host.user}"></h:outputText></h:column>
		<h:column>
			<f:facet name="header">
				<h:panelGroup styleClass="multiLineHeaderContent">
					<h:outputText value="#{messages.tasks}"/>
					<f:verbatim>&nbsp;&nbsp;</f:verbatim>
					<f:verbatim>&nbsp;&nbsp;</f:verbatim>
				</h:panelGroup>
			</f:facet>
			
			<!--  
				var host is an instance of HostEntry 
				service is an instance of ServiceInHost which represents a task of a cim server
			-->
			
			<h:dataTable var="service" value="#{host.enabledServices}" width="100%" columnClasses="width200px topAlignment,width0 topAlignment" cellpadding="0" cellspacing="0" width="100%">
				<h:column>
						<h:outputText value="#{service.reference.name}" style="white-space:no-wrap;padding-right:5px"/>
				</h:column>
				<h:column>
					<h:dataTable var="item" value="#{service.configurationItems}" columnClasses="width200px topAlignment, width200px topAlignment,width0 topAlignment" cellpadding="0" cellspacing="0">
						<h:column>
								<h:outputText value="#{item.value.name}" style="white-space:no-wrap;padding-right:5px"/>
						</h:column>
						<h:column>
								<h:outputText value="#{item.value.value}" style="white-space:no-wrap;padding-right:5px"/>
						</h:column>
						<h:column>
								<h:commandLink onmouseover="showTooltip(event,'#{item.infoTranslated}')" onmouseout="hideTooltip(event)" onclick="return false">
									<h:graphicImage value="../images/info.gif" styleClass="noBorder"/>
								</h:commandLink>
						</h:column>
					</h:dataTable>
				</h:column>
			</h:dataTable>
		</h:column>
	</h:dataTable>
	<f:verbatim><br><br></f:verbatim>
	<h:outputText value="#{messages.installedTasks}" styleClass="tableHeader"/>
	<f:verbatim><br><hr class="tableHeaderHR"><br><br></f:verbatim>
	<h:dataTable
		styleClass="multiLineTable" 
		cellpadding="0"
		cellspacing="0"
		width="100%" 
		value="#{admin.tasks}" var="task" 
		columnClasses="multiLineContentFirst left,multiLineContent left,multiLineContentLast left" 
		rowClasses="multiLineRowWhite,multiLineRowGray"
		headerClass="multiLineHeader left adminTableHeaderHeight"> 
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
	<f:verbatim><br/></f:verbatim>
	
</h:panelGroup>
<h:outputText styleClass="error" value="#{admin.adminDisabledMsg}" rendered="#{!admin.adminEnabled}"/>
</h:panelGrid>
<h:panelGrid columns="1" width="80%" rendered="#{admin.adminEnabled}">
<h:commandLink styleClass="adminLink" value="#{messages.edithost}" action="#{admin.editHosts}" actionListener="#{admin.selectAllHosts}"/>
<h:commandLink styleClass="adminLink" value="#{messages.showSLPConfig}" action="#{admin.loadSlpConfiguration}" rendered="#{menueController.useSlp && !admin.slpMode }"/>
</h:panelGrid>
</h:form>
<t:div id="tooltipPanel" forceId="true" styleClass="tip">
	<h:outputText id="tooltiptext">test</h:outputText>
</t:div>

</body>
</html>
</f:view>
