<%-- 
  
  /** 
  * cimomLogin.jsp.java
  *
  * 
  * © Copyright IBM Corp.  2009,2006,2007
  *
  * THIS FILE IS PROVIDED UNDER THE TER	MS OF THE ECLIPSE PUBLIC LICENSE
  * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
  *
  * You can obtain a current copy of the Eclipse Public License from
  * http://www.opensource.org/licenses/eclipse-1.0.php
  *
  * @author: org.sblim.wbemsmt.webapp.templates.generator.JspGenerator
  * @template: org/sblim/wbemsmt/webapp/templates/cimomLogin.vm
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
      
      

<h:panelGrid id="loginPanel1" columns="1" width="100%" >

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