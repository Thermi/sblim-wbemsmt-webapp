<%-- 
/**
 *  header.jsp
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
<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" columns="2" styleClass="editTableHeader" columnClasses="editTableHeaderTitle, editTableHeaderBanner" rendered="#{style.header}">
	<h:graphicImage value="#{style.resourceDir}/images/title.gif" rendered="#{style.header}" alt="#{messages.webAppTitle}" title="#{messages.webAppTitle}" />
	<h:graphicImage value="#{style.resourceDir}/images/banner.gif" rendered="#{style.header}" alt="#{styleMessages.bannerTitle}" title="#{styleMessages.bannerTitle}"/>
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
          <h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="actionsPopup" columnClasses="actionsPopupItem">
		<h:commandLink       value="#{messages.updateTree}"
					        action="#{treeSelector.currentTreeBacker.updateTree}"
					    styleClass="treeTopLink"/>
		<h:commandLink       value="#{messages.menu_expandAll}"
      					    action="#{treeSelector.currentTreeBacker.expandAll}"
               			styleClass="treeTopLink"/>

		<h:commandLink       value="#{messages.menu_collapseAll}"
					actionListener="#{treeSelector.currentTreeBacker.collapseAll}"
              			styleClass="treeTopLink"/>

		<h:panelGroup rendered="#{treeSelector.selectedNode.contextMenu.itemCount >= 1}">
			<f:verbatim><hr class="actionsPopupSeparatorHr"/></f:verbatim>
		</h:panelGroup>

		<h:commandLink       value="#{treeSelector.selectedNode.contextMenu.menuItems[0].description}"
					actionListener="#{treeSelector.selectedNode.contextMenu.menuItems[0].processEvent}"
	           				action="#{treeSelector.getCurrentOutcome}"
	           			  rendered="#{treeSelector.selectedNode.contextMenu.itemCount >= 1}" styleClass="treeTopLink"
	           			  onclick="hideActionMenue();#{treeSelector.selectedNode.contextMenu.menuItems[0].javaScriptConfirmStatement}
	           			           #{treeSelector.selectedNode.contextMenu.menuItems[0].javaScriptWaitStatement}"/>

		<h:commandLink           value="#{treeSelector.selectedNode.contextMenu.menuItems[1].description}"
						actionListener="#{treeSelector.selectedNode.contextMenu.menuItems[1].processEvent}"
		           				action="#{treeSelector.getCurrentOutcome}"
	    	       			  rendered="#{treeSelector.selectedNode.contextMenu.itemCount >= 2}" styleClass="treeTopLink" 
	        	   			  onclick="hideActionMenue();#{treeSelector.selectedNode.contextMenu.menuItems[1].javaScriptConfirmStatement}
	           				           #{treeSelector.selectedNode.contextMenu.menuItems[1].javaScriptWaitStatement}"/>

		<h:commandLink           value="#{treeSelector.selectedNode.contextMenu.menuItems[2].description}"
						actionListener="#{treeSelector.selectedNode.contextMenu.menuItems[2].processEvent}"
		           				action="#{treeSelector.getCurrentOutcome}"
	    	       			  rendered="#{treeSelector.selectedNode.contextMenu.itemCount >= 3}" styleClass="treeTopLink" 
	        	   			  onclick="hideActionMenue();#{treeSelector.selectedNode.contextMenu.menuItems[2].javaScriptConfirmStatement}
	           				           #{treeSelector.selectedNode.contextMenu.menuItems[2].javaScriptWaitStatement}"/>
          <f:verbatim><br/></f:verbatim>
		  <h:commandLink value="#{messages.close}" styleClass="treeTopLink" onclick="hideActionMenue();return;"/>          
          </h:panelGrid>
      </h:panelGroup>
</h:panelGroup>
<h:panelGroup>
			<h:outputText id="loggedInText" styleClass="fieldLabel" value="#{messages.loggedInAs} #{loginCheck.username}@#{loginCheck.hostname}#{loginCheck.namespace}" rendered="#{menueController.signedOnTextEnabled}"></h:outputText>
			<f:verbatim>&nbsp;&nbsp;</f:verbatim>
			<h:commandLink id="link_1_logout" value="#{messages.logout}" action="#{loginCheck.logoutAction}"  styleClass="treeTopLink" rendered="#{style.logoutLink}" />
			<f:verbatim><br></f:verbatim>
			<h:commandLink id="link_1_help" value="#{messages.menu_Help}" action="help" styleClass="treeTopLink"  rendered="#{style.helpLink}"/>
</h:panelGroup>
</h:panelGrid>           
</h:form>	
</h:panelGroup>		  