	<h:panelGroup rendered="#{msg.hasOKCancel}">
	<h:commandButton 
       value="#{messages.ok}"
       actionListener="#{objectActionController.messageInputOKAction}"
       action="#{objectActionController.getLastMessageInputResult}"
       styleClass="submitButton">
       <f:param name="listener" value="#{msg.messageInputHandler}"/>
    </h:commandButton>
	<f:verbatim>&nbsp;</f:verbatim>
	<h:commandButton 
       value="#{messages.cancel}"
       actionListener="#{objectActionController.messageInputCancelAction}"
       action="#{objectActionController.getLastMessageInputResult}"
       styleClass="submitButton">
       <f:param name="listener" value="#{msg.messageInputHandler}"/>
    </h:commandButton>
	</h:panelGroup>
	<h:panelGroup rendered="#{msg.hasYesNo}">
	<h:commandButton 
       value="#{messages.yes}"
       actionListener="#{objectActionController.messageInputYesAction}"
       action="#{objectActionController.getLastMessageInputResult}"
       styleClass="submitButton">
       <f:param name="listener" value="#{msg.messageInputHandler}"/>
    </h:commandButton>
	<f:verbatim>&nbsp;</f:verbatim>
	<h:commandButton 
       value="#{messages.no}"
       actionListener="#{objectActionController.messageInputNoAction}"
       action="#{objectActionController.getLastMessageInputResult}"
       styleClass="submitButton">
       <f:param name="listener" value="#{msg.messageInputHandler}"/>
    </h:commandButton>
	</h:panelGroup>
