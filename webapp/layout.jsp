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
<f:loadBundle basename="#{style.resourceBundle}" var="styleMessages"/>

<f:view>
<html>
<head>
<h:outputText id="title" escape="false" value="<title>#{messages.webAppTitle}</title>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDir}/styles/edit.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDir}/styles/main.css' rel='stylesheet' type='text/css'>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDir}/styles/wizard.css' rel='stylesheet' type='text/css'>"/>
</head>
<body>
<h:panelGrid columns="1" cellpadding="0" cellspacing="0" border="0" styleClass="editOuterTable" rowClasses="editOuterTableRowHeader, editOuterTableRowMain, editOuterTableRowLanguage" columnClasses="editOuterTableColumn">
<tiles:insert attribute="header" flush="false"/>
<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" id="mainPanel" columns="2" styleClass="editTableCenter" columnClasses="editTableCenterColumnTree, editTableCenterColumnContent" rowClasses="editTableCenterRows">
	<h:panelGroup>
		<h:form id="menuForm">
		<tiles:insert attribute="menu" flush="false"/>
		</h:form>
	</h:panelGroup>
	<h:panelGroup>
		<h:form id="mainForm">
		<tiles:insert attribute="content" flush="false"/>
		</h:form>
	</h:panelGroup>
</h:panelGrid>
<h:form id="langform" styleClass="languageForm">
<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" columns="2" columnClasses="editFooterLanguage, editFooterVersion" styleClass="editFooter"> 
		<h:panelGroup id="langGroup">
			<h:commandLink actionListener="#{localeManager.setEnglish}" id="en" ><h:graphicImage url="/images/us.gif" styleClass="imageLink" alt="#{messages.language_english}" title="#{messages.language_english}"/></h:commandLink>
			<f:verbatim>&nbsp;</f:verbatim>
			<h:commandLink actionListener="#{localeManager.setGerman}" id="de"><h:graphicImage url="/images/de.gif" styleClass="imageLink" alt="#{messages.language_german}" title="#{messages.language_german}"/></h:commandLink>
			<f:verbatim>&nbsp;&nbsp;</f:verbatim>
			<h:commandLink id="link_1_logout" value="logout" action="#{loginCheck.logoutAction}"  rendered="#{menueController.fileEnabled && menueController.testMode}"/>
		</h:panelGroup>
		<h:outputText value="#{messages.version}: #{objectActionController.currentVersion}" styleClass="fieldLabel"/>
</h:panelGrid>
</h:form>
</h:panelGrid>
<h:panelGrid id="waitDlg" style="visibility:hidden" columns="2" styleClass="waitDlg" cellpadding="5" cellspacing="5">
	<h:graphicImage value="#{style.resourceDir}/images/wait.gif" alt="#{messages.wait}" title="#{messages.wait}" id="waitImage"/>
	<h:outputText value="" id="waitMsg" styleClass="waitMsg"/>
</h:panelGrid>

</body>
</html>
</f:view>
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

var clientX, clientY;
var gotoWait = false;
var waitMessage = "";
function showWait()
{

	if (clientX && clientY)
	{
		var waitMsg = document.getElementById("waitMsg");
		
		if (waitMessage.indexOf("...") == -1)
		{
			waitMessage = waitMessage + "...";
		}
		
		waitMsg.innerHTML = waitMessage;

		var waitDlg = document.getElementById("waitDlg");
		waitDlg.style.position = "absolute";
		waitDlg.style.top = clientY + 20 + "px";
		waitDlg.style.left = clientX + 30 + "px";
		waitDlg.style.visibility = "visible";
		
		
	}
	else
	{
		gotoWait = true;
	}	
	
}

function saveXY (clickEvent) {

  if (!clickEvent)
    clickEvent = window.event;


  clientX  = clickEvent.clientX;
  clientY  = clickEvent.clientY;
  
  if (gotoWait)
  	showWait();
}

document.onclick = saveXY;

</script>

<script>
function showConfirm(msg)
{
	check = confirm(msg);
	return check;
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

