/**
 *  ObjectActionControllerBean.java
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
 * Author:    Michael.Bauschert@de.ibm.com
 *
 * Contributors:
 *
 */

package org.sblim.wbemsmt.webapp.jsf;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.sblim.wbem.client.CIMClient;
import org.sblim.wbemsmt.exception.ModelLoadException;
import org.sblim.wbemsmt.exception.ObjectDeletionException;
import org.sblim.wbemsmt.exception.ObjectNotFoundException;
import org.sblim.wbemsmt.exception.ObjectUpdateException;
import org.sblim.wbemsmt.exception.ValidationException;
import org.sblim.wbemsmt.tasklauncher.event.EditListener;
import org.sblim.wbemsmt.tools.jsf.EditBean;
import org.sblim.wbemsmt.tools.jsf.IObjectActionController;
import org.sblim.wbemsmt.tools.jsf.TabbedPane;
import org.sblim.wbemsmt.tools.wizard.jsf.IWizardController;
import org.sblim.wbemsmt.tools.wizard.jsf.JSFWizardBase;

public class ObjectActionControllerBean  implements IObjectActionController, IWizardController {

//	private boolean canDelete;
//	private boolean canCreate;
	private TreeSelectorBean treeSelector;
	private FacesContext facesContext;
	private EditListener currentEditListener;
	
	private JSFWizardBase currentWizard;
	private HtmlPanelGrid currentEditor;
	private Map editBeans = new HashMap();
	private String selectedTabId = "undefined";
	private TabbedPane tabbedPane;
	private int selectedTabIndex;
	private String cimomName;
	
	public final String KEY_VERSION = "wbemsmt-version"; 
	
	public ObjectActionControllerBean() {
		this.facesContext = FacesContext.getCurrentInstance();	
	}
//	
//	public boolean isCanCreate() {
//		return canCreate;
//	}
//	public void setCanCreate(boolean canCreate) {
//		this.canCreate = canCreate;
//	}
//	public boolean isCanDelete() {
//		return canDelete;
//	}
//	public void setCanDelete(boolean canDelete) {
//		this.canDelete = canDelete;
//	}
	
	public FacesContext getFacesContext() {
		return facesContext;
	}

	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}

	public String delete() throws ModelLoadException, ObjectNotFoundException, ObjectDeletionException
	{
		return "start";
	}
	
	public String create() throws ObjectUpdateException, ValidationException, ModelLoadException
	{
		return "start";
	}

	public String edit() throws ModelLoadException, ObjectUpdateException, ValidationException
	{
        return "start";
	}

	public TreeSelectorBean getTreeSelector() {
		return treeSelector;
	}
	public void setTreeSelector(TreeSelectorBean treeSelector) {
		this.treeSelector = treeSelector;
	}

	public CIMClient getCIMClient() {
		return treeSelector.getSelectedNode().getTaskLauncherTreeNode().getCimClient();
	}

	public JSFWizardBase getCurrentWizard() {
		return currentWizard;
	}

	public void setCurrentWizard(JSFWizardBase wizard) {
		currentWizard = wizard;
	}

	public HtmlPanelGrid getCurrentEditor() {
		return currentEditor;
	}

	public void setCurrentEditor(HtmlPanelGrid editor) {
		this.currentEditor = editor;
	}
	
	public void addEditBean(String key, EditBean editBean)
	{
		editBeans.put(key,editBean);
	}
	
	public Map getEditBeans()
	{
		return editBeans;
	}

	public void setTab(ActionEvent event)
	{
		HtmlCommandLink link = ((HtmlCommandLink)event.getSource());
		selectedTabId = tabbedPane.getIdByTabname((String) link.getValue());
	}
	
	public String getSelectedTabId()
	{
		return selectedTabId;
	}
	
	public void setSelectedTabId(String id)
	{
		selectedTabId = id;
	}
	
	public void setTabbedPane(TabbedPane tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public TabbedPane getTabbedPane() {
		return tabbedPane;
	}

	public void setSelectedTabIndex(int index) {
		this.selectedTabIndex = index;
	}

	public int getSelectedTabIndex() {
		return selectedTabIndex;
	}

	public String getCimomName() {
		return cimomName;
	}

	public void setCimomName(String cimomName) {
		this.cimomName = cimomName;
	}
	public EditListener getCurrentEditListener() {
		return currentEditListener;
	}
	public void setCurrentEditListener(EditListener currentEditListener) {
		this.currentEditListener = currentEditListener;
	}
	
	public String getCurrentVersion()
	{
		return FacesContext.getCurrentInstance().getExternalContext().getInitParameter(KEY_VERSION);
	}
	
	
	
}
