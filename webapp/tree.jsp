<%-- 
/**
 *  tree.jsp
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
 * Contributors:
 *
 */
--%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>

<h:commandLink value="update" actionListener="#{treeSelector.currentTreeBacker.updateTree}" styleClass="update"/>
<t:tree2 binding="#{treeSelector.currentTree}" value="#{treeSelector.currentTreeBacker.treeModel}" var="node" preserveToggle="true" varNodeToggler="t" clientSideToggle="false" showRootNode="false">
	<f:facet name="root">
	    <h:panelGroup>
	        <f:facet name="expand">
	            <t:graphicImage value="images/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
	        </f:facet>
	        <f:facet name="collapse">
	            <t:graphicImage value="images/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
	        </f:facet>
	        <h:outputText value="#{node.description}" styleClass="nodeFolder"/>
	    </h:panelGroup>
	</f:facet>
	<f:facet name="treenode">
	    <h:panelGroup>
	        <f:facet name="expand">
	            <t:graphicImage value="images/yellow-folder-open.png" rendered="#{t.nodeExpanded}" border="0"/>
	        </f:facet>
	        <f:facet name="collapse">
	            <t:graphicImage value="images/yellow-folder-closed.png" rendered="#{!t.nodeExpanded}" border="0"/>
	        </f:facet>
			<h:commandLink value="#{node.description}"
						   action="#{treeSelector.getCurrentOutcome}"
						   actionListener="#{t.setNodeSelected}"
						   styleClass="#{node.hasEventListener ? 'event' : 'noevent'} #{t.nodeSelected ? 'selected' : 'notselected'}"
						   >
				<f:actionListener type="org.sblim.wbemsmt.webapp.jsf.TreeNodeActionListener"/>
				<f:param name="node_id" value="#{node.identifier}" />
			</h:commandLink>
	    </h:panelGroup>
	</f:facet>
</t:tree2>
