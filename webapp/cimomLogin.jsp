<%-- 
/**
 *  cimomLogin.jsp
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
	<f:verbatim>
	<br><br>
	</f:verbatim>
	<h:panelGrid id="loginPanel" columns="2" styleClass="mainTable">
		<f:facet name="header">
			<h:outputText value="#{messages.loginTo} #{loginCheck.cimomName}"/>
		</f:facet>
		<h:outputLabel for="username" value="#{messages.user}"/>
		<h:inputText id="username" value="#{loginCheck.username}"/>
		<h:outputLabel for="password" value="#{messages.password}"/>
		<h:inputSecret id="password" value="#{loginCheck.password}"/>
	</h:panelGrid>
	<h:commandButton value="#{messages.login}" action="#{loginCheck.loginActionEmbedded}"/>
</h:panelGrid>
