<%-- 
/**
 *  header.jsp
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
 * Author:     Marius Kreis
 *
 * Contributors: Michael.Bauschert@de.ibm.com
 *
 */
--%>

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>
<f:loadBundle basename="#{style.resourceBundle}" var="styleMessages"/>

<h:panelGroup>
<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" columns="2" styleClass="editTableHeader" columnClasses="editTableHeaderTitle, editTableHeaderBanner" rendered="#{!style.embedded}">
	<h:graphicImage value="#{style.resourceDir}/images/title.gif" rendered="#{!style.embedded}" alt="#{messages.webAppTitle}" title="#{messages.webAppTitle}" />
	<h:graphicImage value="#{style.resourceDir}/images/banner.gif" rendered="#{!style.embedded}" alt="#{styleMessages.bannerTitle}" title="#{styleMessages.bannerTitle}"/>
</h:panelGrid>


<%@ include file="include_errorHandling.jsp" %>
<h:form id="breadcrumb">
<h:panelGrid columns="2" width="100%" styleClass="editTableTopLink" cellpadding="5" columnClasses="editTableTopLinkColumnSelected, editTableTopLinkColumnInfo">
<h:panelGroup>
<h:outputText value="#{messages.selectedNode}" styleClass="fieldLabel"/><f:verbatim>&nbsp;</f:verbatim><h:outputText styleClass="fieldLabel" value="#{messages.noSelection}" rendered="#{treeSelector.selectedNode == null}"/><h:outputText value="#{treeSelector.selectedNode.description}" rendered="#{treeSelector.selectedNode != null}" styleClass="fieldLabel" escape="false"/>
<f:verbatim>&nbsp;</f:verbatim>

    <h:commandLink styleClass="actionPanel" onclick="showActionMenue();return false;" id="actionLink">
		<h:graphicImage 
			value="#{style.resourceDir}/images/empty.png" 
			alt="empty"
			title="empty" 
			id="actionIcon" width="0" height="0" styleClass="actionImage"
			/>
		<h:outputText value="#{messages.actions}" styleClass="actionLink"/>
		<h:graphicImage 
			value="#{style.resourceDir}/images/actions.gif" 
			alt="#{messages.actions}"
			title="#{messages.actions}" 
			styleClass="actionImage"
			/>
    </h:commandLink>
      <h:panelGroup style="visibility:hidden; position:absolute; top:0px; left:0px;" id="popup">
          <h:panelGrid binding="#{treeSelector.contextMenuTable}"/>
      </h:panelGroup>
</h:panelGroup>
<h:panelGroup rendered="#{!style.embedded}">
			<h:outputText id="loggedInText" styleClass="fieldLabel" value="#{messages.loggedInAs} #{loginCheck.username}@#{loginCheck.hostname}#{loginCheck.namespace}" rendered="#{menueController.signedOnTextEnabled}"></h:outputText>
			<f:verbatim>&nbsp;&nbsp;</f:verbatim>
			<h:commandLink id="link_1_logout" value="#{messages.logout}" action="#{loginCheck.logoutAction}"  styleClass="treeTopLink" />
			<f:verbatim><br></f:verbatim>
			<h:commandLink id="link_1_help" value="#{messages.menu_Help}" onclick="#{helpManager.javascriptCall}" styleClass="treeTopLink"/>
</h:panelGroup>
<h:panelGroup rendered="#{style.embedded}">
	<f:verbatim>&nbsp;</f:verbatim>
</h:panelGroup>

</h:panelGrid>           
</h:form>	
</h:panelGroup>		  