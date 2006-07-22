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
<html>
<head>
<f:view>
<h:outputText escape="false" value="<title>#{messages.webAppTitleAdmin}</title>"/>
<style type="text/css">
BODY, TD {
	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size : smaller;
	margin : 0;
}

INPUT, SELECT, TEXTAREA {
	font-family : Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size: 1em;
	border : 1px solid #4477bb;
	background-color : #ffffff;
	color : #000000;
	background : #ffffff;
}

TABLE {
	border : 0;
}

.mytable {
	width : 100%;
	height : 100%;
}

.blue {
	background-color : #4477bb;
	background : #4477bb;
	font-size : 2em;
	vertical-align : bottom;
	color : #ffffff;
	height : 40%;
}

.white {
	padding: 3em 3em 3em 3em;
}

.bigbold {
	color : black;
	font-weight : bolder;
}

.error {
	color : red;
	font-weight : bolder;
}

.header {
	background-color : #B7DDFB;
	font-weight : bolder;
}

.row1 {
	background-color : white;
	valign:top;
}

.row2 {
	background-color : #B7DDFB;
	valign:top;
}

.col1 {
	vertical-align:top;
}
.col2 {
	vertical-align:top;
}

</style>
</head>
<body>

<h:form>
<h:panelGrid columns="1" width="60%" align="center">

<f:verbatim><br><br></f:verbatim>
<h:panelGroup>
	<h:outputText styleClass="bigbold" value="#{messages.taskLauncherAdminconsole}"/>
</h:panelGroup>
<f:verbatim><br><br></f:verbatim>
<h:outputText value="#{messages.edithost}"/>
<f:verbatim><br><br></f:verbatim>
<h:dataTable  value="#{admin.hostTable}" var="host" columnClasses="col1,col2" rowClasses="row1,row2" headerClass="header" border="0" cellpadding="3" cellspacing="0"> 
	<h:column><h:selectBooleanCheckbox value="#{host.delete}" rendered="#{!host.new}"></h:selectBooleanCheckbox></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.hostname}"></h:outputText></f:facet><h:inputText value="#{host.hostname}" size="30"></h:inputText></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.port}"></h:outputText></f:facet><h:inputText value="#{host.port}" size="30"></h:inputText></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.namespace}"></h:outputText></f:facet><h:inputText value="#{host.namespace}" size="30"></h:inputText></h:column>
	<h:column><f:facet name="header"><h:outputText value="#{messages.user}"></h:outputText></f:facet><h:inputText value="#{host.user}" size="30"></h:inputText></h:column>
	<h:column>
	<f:facet name="header"><h:outputText value="#{messages.tasks}"></h:outputText></f:facet>
	<h:dataTable  value="#{host.services}" var="service">
		<h:column>
			<h:selectBooleanCheckbox  value="#{service.enabled}"></h:selectBooleanCheckbox>
			<h:outputText value="#{service.service}"></h:outputText>
		</h:column>
	</h:dataTable>
	</h:column>
</h:dataTable>
<h:graphicImage value="/images/arrow.gif"></h:graphicImage>
<h:commandButton value="#{messages.delete}" action="#{admin.deleteHosts}"/>
<f:verbatim><br><br></f:verbatim>
<f:verbatim>&nbsp;</f:verbatim>
<h:commandButton value="#{messages.ok}" action="#{admin.saveHost}"/>

<h:commandLink value="#{messages.editservice}" action="adminService"/>
<f:verbatim><br><br></f:verbatim>
<%@ include file="/include_errorHandling.jsp" %></h:panelGrid>
</h:form>
</f:view>
</body>
</html>
