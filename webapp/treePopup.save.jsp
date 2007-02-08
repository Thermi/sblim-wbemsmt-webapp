<%-- 
/**
 *  tree.jsp
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
 * Author:     Michael Bauschert
 *
 * Contributors:
 *
 */
--%>


			<t:popup    
						closePopupOnExitingElement="false"
			            closePopupOnExitingPopup="true"
			            displayAtDistanceX="-5"
			            displayAtDistanceY="-5"
			            styleClass="popup">

			    put TreeNode-Output here

			    <f:facet name="popup">

<h:dataTable value="#{node.contextMenu.menuItems}" var="menuItem" rendered="#{node.hasContextMenu}">   
  <h:column>
      <h:commandLink value="#{menuItem.description}" actionListener="#{menuItem.processEvent}" action="#{menuItem.navigate}" />
  </h:column>
</h:dataTable> 

			    </f:facet>
			</t:popup>


<!-- header.jsp -->

        <t:navigationMenuItem id="nav_2_6" itemLabel="Create Instance" itemDisabled="#{!objectActionController.canCreate}" action="#{objectActionController.create}"  rendered="#{menueController.createEnabled}" />
        <t:navigationMenuItem id="nav_2_7" itemLabel="Delete Instance" itemDisabled="#{!objectActionController.canDelete}" action="#{objectActionController.delete}"  rendered="#{menueController.deleteEnabled}"/>


<h:form rendered="false">
<h:commandButton value="#{messages.create}" action="#{objectActionController.create}" disabled="#{!objectActionController.canCreate}" rendered="false"/>		
<h:commandButton value="#{messages.delete}" action="#{objectActionController.delete}" disabled="#{!objectActionController.canDelete}" rendered="false"/>
</h:form>
