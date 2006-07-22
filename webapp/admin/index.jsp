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
<h:panelGroup rendered="#{admin.adminEnabled}">
	<f:verbatim><br><br></f:verbatim>
	<h:commandLink value="#{messages.edithost}" action="adminHost"/>
	<f:verbatim><br></f:verbatim>
	<h:commandLink value="#{messages.editservice}" action="adminService"/>
	<f:verbatim><br><br></f:verbatim>
</h:panelGroup>
<h:outputText styleClass="error" value="#{admin.adminDisabledMsg}" rendered="#{!admin.adminEnabled}"/>
<%@ include file="/include_errorHandling.jsp" %>
</h:panelGrid>
</h:form>
</f:view>
</body>
</html>
