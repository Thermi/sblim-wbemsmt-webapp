<%-- 
  
  /** 
  * wizardPage.jsp.java
  *
  * 
  * © Copyright IBM Corp. 2006,2007
  *
  * THIS FILE IS PROVIDED UNDER THE TER	MS OF THE COMMON PUBLIC LICENSE
  * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
  *
  * You can obtain a current copy of the Common Public License from
  * http://www.opensource.org/licenses/cpl1.0.php
  *
  * @author: org.sblim.wbemsmt.webapp.templates.generator.JspGenerator
  * @template: org/sblim/wbemsmt/webapp/templates/wizardPage.vm
  *
  * Contributors: 
  *   michael.bauschert@de.ibm.com
  * 
  * Description: 
  * 
  * generated Class
  */
--%>  

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://myfaces.apache.org/tomahawk" prefix="t"%>
<f:loadBundle basename="org.sblim.wbemsmt.webapp.jsf.webapp_messages" var="messages"/>
<f:loadBundle basename="#{style.resourceBundle}" var="styleMessages"/>

<f:view>
<html>
<head>
<h:outputText id="title" escape="false" value="<title>#{messages.webAppTitle}</title>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDirAbsolute}/styles/defaultStyles.css' rel='stylesheet' type='text/css'/>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDirAbsolute}/styles/edit.css' rel='stylesheet' type='text/css'/>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDirAbsolute}/styles/main.css' rel='stylesheet' type='text/css'/>"/>
<h:outputText escape="false" value="<link href='#{style.resourceDirAbsolute}/styles/wizard.css' rel='stylesheet' type='text/css'/>"/>
<h:outputText escape="false" value="<script src='scripts/showWait.js'></script>"/>
<h:outputText escape="false" value="<script src='scripts/tooltip.js'></script>"/>
</head>

<body>
<h:panelGrid columns="1" cellpadding="0" cellspacing="0" border="0" styleClass="editOuterTable" rowClasses="editOuterTableRowHeader, editOuterTableRowMain, editOuterTableRowLanguage" columnClasses="editOuterTableColumn">
<%@ include file="include_header.jsp" %>
<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" id="mainPanel" columns="2" styleClass="editTableCenter" columnClasses="editTableCenterColumnTree, editTableCenterColumnContent" rowClasses="editTableCenterRows">
   <h:panelGroup>
      <h:form id="menuForm">
      <%@ include file="include_tree.jsp" %>
      </h:form>
   </h:panelGroup>
   <h:panelGroup>

   <h:form id="mainForm">
      
      

      <h:panelGrid columns="2" styleClass="wizardOuterPanel" rowClasses="wizardOuterPanelRow" columnClasses="wizardOuterPanelColumnProgress,wizardOuterPanelColumnContent" cellpadding="0" cellspacing="0" border="0" width="100%">
         <h:panelGrid columns="2" cellpadding="5" cellspacing="0" border="0">
            <h:dataTable var="step" value="#{objectActionController.currentWizard.stepList}">
               <h:column>
                  <f:verbatim><nobr></f:verbatim>
                  <h:graphicImage value="#{style.resourceDir}/images/empty.png" rendered="#{step.level == 2}" styleClass="wizardProgressEmptyGraphic"/>
                  <h:graphicImage value="#{style.resourceDir}/images/empty.png" rendered="#{step.level == 3}" styleClass="wizardProgressEmptyGraphic"/>
                  <h:graphicImage value="#{style.resourceDir}/images/empty.png" rendered="#{step.level == 4}" styleClass="wizardProgressEmptyGraphic"/>
                  <h:commandLink rendered="#{step.visited && !step.userdefined}" 
                              value="#{step.text}"
                              styleClass="wizardLink"
                              actionListener="#{objectActionController.currentWizard.select}"
                              action="#{objectActionController.currentWizard.selectAction}">
                     <f:param name="stepName" value="#{step.pageName}"/>
                  </h:commandLink>
                  <h:outputText rendered="#{!step.visited || step.userdefined}" value="#{step.text}" styleClass="wizardProgressText"/>
                  <f:verbatim></nobr></f:verbatim>
               </h:column>
               <h:column>
                  <h:graphicImage value="#{style.resourceDir}/images/visited.png" rendered="#{step.visited && !step.current}" styleClass="wizardProgressGraphic"/>
                  <h:graphicImage value="#{style.resourceDir}/images/current.png" rendered="#{step.visited &&  step.current}" styleClass="wizardProgressGraphic"/>
                  <h:graphicImage value="#{style.resourceDir}/images/emptyStepState.png   " rendered="#{!step.visited}" styleClass="wizardProgressGraphic"/>
               </h:column>
            </h:dataTable>      
         </h:panelGrid>
         <h:panelGrid width="100%" styleClass="wizardContent" columns="1" cellpadding="0" cellspacing="0" border="0" rowClasses="wizardRowTitle,wizardRowPanel,wizardRowButtons" columnClasses="wizardContentColumn">
            <h:panelGrid width="100%" styleClass="wizardTitle" id="wizardTitle" columns="1" cellpadding="0" cellspacing="0" border="0">         
               <h:outputText styleClass="wizardTitleText" value="#{objectActionController.currentWizard.titleText}"/>
               <%-- escape = false because subtitles are generated with <br> for every linebreak --%>
               <h:outputText styleClass="wizardSubTitleText" escape="false" value="#{objectActionController.currentWizard.currentPanel.subTitleText}"/>
            </h:panelGrid>
            <h:panelGrid id="wizardPanel" binding="#{objectActionController.currentWizard.currentPanel.inputFieldContainer}"/>
            <h:panelGrid id="wizardButtons" cellpadding="0" cellspacing="2" columns="4" width="0%" width="100%" styleClass="wizardFooter" columnClasses="wizardFooterColumnButtons,wizardFooterColumnRemainder">
               <h:panelGroup>
                  <f:verbatim><nobr></f:verbatim>
                  <h:commandButton id="prev" styleClass="wizardButton" value="#{messages.button_previous}" action="#{objectActionController.currentWizard.back}" disabled="#{objectActionController.currentWizard.backButtonDisabled}" />
                  <h:commandButton id="next" styleClass="wizardButton" value="#{messages.button_next}" action="#{objectActionController.currentWizard.next}" disabled="#{objectActionController.currentWizard.nextButtonDisabled}" />
                  <h:commandButton id="finish" styleClass="wizardButton" value="#{objectActionController.currentWizard.finishText}" action="#{objectActionController.currentWizard.finish}" onclick="#{objectActionController.currentWizard.javaScriptWaitStatement}" disabled="#{objectActionController.currentWizard.finishButtonDisabled}"/>
                  <h:commandButton id="cancel" styleClass="wizardButton" value="#{messages.button_cancel}" action="#{objectActionController.currentWizard.cancel}" disabled="#{objectActionController.currentWizard.cancelButtonDisabled}" onclick="#{objectActionController.currentWizard.cancelJavaScriptConfirmStatement}"/>
                  <f:verbatim></nobr></f:verbatim>
               </h:panelGroup>
               <h:outputText style="width:100%"></h:outputText>
            </h:panelGrid>
         </h:panelGrid>
      </h:panelGrid>


   </h:form>

   </h:panelGroup>
</h:panelGrid>

<h:form id="langform" styleClass="languageForm" rendered="#{!style.embedded}">
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
<%@ include file="include_showWait.jsp" %>

</body>
</html>
</f:view>


<script>
function showConfirm(msg)
{
   check = confirm(msg);
   return check;
}
</script>

<script>

var changeMessage = "";

function inputFieldChanged(message)
{
   changeMessage = message;
   //addText("Field changed");
}

function checkModifications()
{
   executeRequest = true;

   if (changeMessage.length > 0)
   {
      executeRequest = confirm(changeMessage);
   }
   return executeRequest;
}
</script>

<script>
//var debugWindow = window.open("debug.html", "Zweitfenster", "width=300,height=400,left=100,top=200,resizable=yes");
function addText(text)
{
   //debugWindow.document.forms.debugform.debugOut.value= debugWindow.document.forms.debugform.debugOut.value + "\n" + text;
}
</script>

<script>
function showHelp(url)
{
   var helpWindow = window.open(url, "Zweitfenster", "width=300,height=500,left=100,top=200,resizable=yes,scrollbars=yes");
   helpWindow.focus();
   
}
</script>