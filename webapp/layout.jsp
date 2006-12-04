<%-- 
/**
 *  layout.jsp
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

<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-tiles" prefix="tiles" %>


<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>
<f:view>
<html>
<head>
<link href="styles/main.css" rel="stylesheet" type="text/css">
<h:outputText id="title" escape="false" value="<title>#{messages.webAppTitle}</title>"/>
</head>
<body>
<h:form id="mainForm">
<tiles:insert attribute="header" flush="false"/>
<h:panelGrid width="100%" id="mainPanel" columns="2" columnClasses="menuColumn, contentColumn" styleClass="layout">
	<h:panelGroup>
		<f:verbatim><div class="menu"></f:verbatim>
			<tiles:insert attribute="menu" flush="false"/>
		<f:verbatim></div></f:verbatim>
	</h:panelGroup>
	<h:panelGroup>
		<f:verbatim><div class="menu"></f:verbatim>
			<tiles:insert attribute="content" flush="false"/>
	</h:panelGroup>
</h:panelGrid>
</h:form>
<h:panelGrid columns="2" columnClasses="lang,version" styleClass="langAndVersion" id="langgrid"> 
	<h:form id="langform">
		<h:panelGroup id="langGroup">
			<h:commandLink id="en" actionListener="#{localeManager.setEnglish}"><h:graphicImage url="/images/us.gif" style="border:0px"/></h:commandLink>
			<h:commandLink id="de" actionListener="#{localeManager.setGerman}"><h:graphicImage url="/images/de.gif" style="border:0px"/></h:commandLink>
			<f:verbatim>&nbsp;&nbsp;</f:verbatim>
			<h:outputText id="loggedInText" style="vertical-align:top" value="#{messages.loggedInAs} #{loginCheck.username}@#{loginCheck.hostname}#{loginCheck.namespace}" rendered="#{menueController.signedOnTextEnabled}"></h:outputText>
			<f:verbatim>&nbsp;&nbsp;</f:verbatim>
			<h:commandLink id="link_1_logout" value="logout" action="#{loginCheck.logoutAction}"  rendered="#{menueController.fileEnabled && menueController.testMode}" styleClass="footerLink"/>
		</h:panelGroup>
	</h:form>
	<h:outputText id="version" value="#{messages.version}: #{objectActionController.currentVersion}"/>
</h:panelGrid>
</f:view>
</body>
<script>

var currentLink;
var currentPopup;
var poppedUp=false;

document.oncontextmenu = new Function("addText('poppedUp' + poppedUp); return (!poppedUp)");

function enablePopup(link, popupName)
{
	addText("enablePopup for " + link.innerHTML);
	link.oncontextmenu = new Function("return popup('" + popupName + "');");
	link.onmousedown = saveMouseData;
	currentLink = link
	poppedUp = true;
}

function popup(popupName)
{
	addText("Popup " + popupName);
	hide(currentPopup);
	currentPopup = document.getElementById(popupName);
	with (currentPopup)
	{
		style.visibility="visible";
		style.left=x-2;
		style.top=y-2;
	}
	poppedUp = true;
	return false;
}
</script>
<script>
//var debugWindow = window.open("debug.html", "Zweitfenster", "width=300,height=400,left=100,top=200,resizable=yes");
function addText(text)
{
	//debugWindow.document.forms.debugform.debugOut.value= debugWindow.document.forms.debugform.debugOut.value + "\n" + text;
}
</script>

</html>

