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

<t:jscookMenu id="navMenu" layout="hbr" theme="ThemeOffice" >
<%/* Availaible jscookMenu themes: ThemeIE, ThemeMiniBlack, ThemeOffice, ThemePanel
     Availaible jscookMenu layout: hbr, hbl, hur, hul, vbr, vbl, vur, vul
     respect to Heng Yuan http://www.cs.ucla.edu/~heng/JSCookMenu
*/%>
    <t:navigationMenuItem id="nav_1" itemLabel="#{messages.menu_File}"  rendered="#{menueController.fileEnabled}">
        <t:navigationMenuItem id="nav_1_1" itemLabel="#{messages.menu_Save} (#{treeSelector.currentTreeBackerName})" action="#{treeSelector.saveCurrentTreeConfigAction}" rendered="#{menueController.saveEnabled}"/>    
        <t:navigationMenuItem id="nav_1_2" itemLabel="#{messages.menu_Logout}" action="#{loginCheck.logoutAction}" />
    </t:navigationMenuItem>
    <t:navigationMenuItem id="nav_2" itemLabel="#{messages.menu_Edit}"  rendered="#{menueController.editEnabled}">
        <t:navigationMenuItem id="nav_2_1" itemLabel="#{messages.menu_Copy}" action="#{treeSelector.copy}" rendered="#{menueController.copyEnabled}" />
        <t:navigationMenuItem id="nav_2_2" itemLabel="#{messages.menu_PasteAbove}" action="#{treeSelector.pasteAbove}"  rendered="#{menueController.pasteEnabled}"/>
        <t:navigationMenuItem id="nav_2_3" itemLabel="#{messages.menu_PasteBelow}" action="#{treeSelector.pasteBelow}"  rendered="#{menueController.pasteBelowEnabled}" />
        <t:navigationMenuItem id="nav_2_4" itemLabel="#{messages.menu_PasteSubnode}" action="#{treeSelector.pasteSubnode}"  rendered="#{menueController.pasteSubnodeEnabled}"/>
        <t:navigationMenuItem id="nav_2_5" itemLabel="#{messages.menu_Delete}" action="#{treeSelector.deleteNode}"  rendered="#{menueController.deleteEnabled}" />
        <t:navigationMenuItem id="nav_2_8" itemLabel="#{messages.menu_rereadSLP}" action="#{treeSelector.reloadSLP}"  rendered="#{menueController.reloadEnabled && taskLauncherController.slpLoader.canFindServiceAgents}"/>
	    <t:navigationMenuItem id="nav_2_9" itemLabel="#{messages.menu_expandAll}" actionListener="#{treeSelector.currentTreeBacker.expandAll}"  rendered="true"/>
	    <t:navigationMenuItem id="nav_2_10" itemLabel="#{messages.menu_collapseAll}" actionListener="#{treeSelector.currentTreeBacker.collapseAll}"  rendered="true"/>
    </t:navigationMenuItem>
    <t:navigationMenuItem id="nav_3" itemLabel="#{messages.menu_View}"  rendered="#{menueController.viewEnabled}">
    	<t:navigationMenuItems value="#{treeSelector.menuItems}" />
    </t:navigationMenuItem>
    <t:navigationMenuItem id="nav_4" itemLabel="#{messages.menu_Help}" >
        <t:navigationMenuItem id="nav_4_1" itemLabel="#{messages.menu_About}" action="help"/>
    </t:navigationMenuItem>
</t:jscookMenu>
<%@ include file="include_errorHandling.jsp" %>
<h:outputText value="#{messages.selectedNode}"/><f:verbatim>&nbsp;</f:verbatim><h:outputText value="#{messages.noSelection}" rendered="#{treeSelector.selectedNode == null}"/><h:outputText value="#{treeSelector.selectedNode.description}" rendered="#{treeSelector.selectedNode != null}"/>
<f:verbatim>&nbsp;</f:verbatim>
	<h:commandLink           value="#{treeSelector.selectedNode.contextMenu.menuItems[0].description}"
				actionListener="#{treeSelector.selectedNode.contextMenu.menuItems[0].processEvent}"
           				action="#{treeSelector.getCurrentOutcome}"
           			  rendered="#{treeSelector.selectedNode.contextMenu.itemCount >= 1}" styleClass="contextInHeader" />
<f:verbatim>&nbsp;</f:verbatim>
<h:commandLink           value="#{treeSelector.selectedNode.contextMenu.menuItems[1].description}"
				actionListener="#{treeSelector.selectedNode.contextMenu.menuItems[1].processEvent}"
           				action="#{treeSelector.getCurrentOutcome}"
           			  rendered="#{treeSelector.selectedNode.contextMenu.itemCount >= 2}" styleClass="contextInHeader" />
<f:verbatim>&nbsp;</f:verbatim>
<h:commandLink           value="#{treeSelector.selectedNode.contextMenu.menuItems[2].description}"
				actionListener="#{treeSelector.selectedNode.contextMenu.menuItems[2].processEvent}"
           				action="#{treeSelector.getCurrentOutcome}"
           			  rendered="#{treeSelector.selectedNode.contextMenu.itemCount >= 3}" styleClass="contextInHeader" />