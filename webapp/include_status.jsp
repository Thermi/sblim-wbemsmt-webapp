<h:form id="langform" styleClass="languageForm" rendered="#{!style.embedded}">
<h:panelGrid width="100%" cellpadding="5" cellspacing="0" border="0" columns="2" columnClasses="editFooterLanguage, editFooterVersion" styleClass="editFooter"> 
      <h:panelGroup id="langGroup">
         <h:commandLink actionListener="#{localeManager.setEnglish}" id="en" ><h:graphicImage url="/images/us.gif" styleClass="imageLink" alt="#{messages.language_english}" title="#{messages.language_english}"/></h:commandLink>
         <f:verbatim>&nbsp;</f:verbatim>
         <h:commandLink actionListener="#{localeManager.setGerman}" id="de"><h:graphicImage url="/images/de.gif" styleClass="imageLink" alt="#{messages.language_german}" title="#{messages.language_german}"/></h:commandLink>
         <f:verbatim>&nbsp;&nbsp;</f:verbatim>
         <h:commandLink id="link_1_logout" value="logout" action="#{loginCheck.logoutAction}"  rendered="#{menueController.fileEnabled && menueController.testMode}"/>
      </h:panelGroup>
      <h:panelGroup id="versionGroup">
	      <h:outputText value="#{messages.version}: #{objectActionController.currentVersion}" styleClass="fieldLabel"/>
      </h:panelGroup>
</h:panelGrid>
</h:form>
