<%-- 
/**
 *  cimomLogin.jsp
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
 * Author:     Michael.Bauschert@de.ibm.com
 *
 * Contributors:
 *
 */
--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>

<h:panelGrid id="loginPanel1" columns="1" width="100%" align="center">

	<h:dataTable var="cimom" value="#{objectActionController.cimomTreeNodesForLogin}">
		
		<h:column>
			<h:panelGrid columns="1" styleClass="mainTable" headerClass="left">
				<f:facet name="header">
					<h:outputText value="#{messages.loginTo} #{cimom.cimomData.info}"/>
				</f:facet>
				<h:outputLabel for="username" value="#{messages.user}"/>
				<h:inputHidden id="cimominfo" value="#{cimom.cimomData.info}"/>
				<h:inputText id="username" value="#{cimom.cimomData.user}"/>
				<h:outputLabel for="password" value="#{messages.password}"/>
				<h:inputSecret id="password"  value="#{cimom.cimomData.password}" redisplay="true" />
				<h:panelGroup>
					<h:selectBooleanCheckbox id="emptyPassword" value="#{cimom.emptyPassword}" />
					<h:outputLabel value="#{messages.emptyPassword}"/>
				</h:panelGroup>
				<h:panelGroup>
					<h:selectBooleanCheckbox id="remind" value="#{cimom.remindPassword}" />
					<h:outputLabel value="#{messages.remindPassword}"/>
				</h:panelGroup>
			</h:panelGrid>
			<h:panelGrid columns="1" styleClass="mainTable" rendered="#{menueController.useSlp && cimom.slpRendered}">
				<h:panelGroup>
				<h:selectBooleanCheckbox id="useSlp"  value="#{cimom.useSlp}"/><h:outputText value="#{messages.useSlp}"/>
				</h:panelGroup>
			</h:panelGrid>
			
			<f:facet name="footer">
				<h:panelGroup>
					<h:commandButton value="#{messages.login}" action="#{loginCheck.loginActionEmbedded}" onclick="#{loginCheck.javascriptShowWaitMulti}" id="login"/>
					<f:verbatim escape="false"><br><br></f:verbatim>
					<h:panelGroup>
						<h:graphicImage value="#{style.resourceDir}/images/info.gif" alt="#{messages.info}" title="#{messages.info}" />
						<h:outputText value="#{messages.infotext_login}"/>
					</h:panelGroup>
				</h:panelGroup>
				
			</f:facet>
		</h:column>
	</h:dataTable>
	
</h:panelGrid>
