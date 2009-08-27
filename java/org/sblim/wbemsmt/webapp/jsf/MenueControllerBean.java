 /** 
  * MenueControllerBean.java
  *
  * © Copyright IBM Corp.  2009,2005
  *
  * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
  *
  * You can obtain a current copy of the Eclipse Public License from
  * http://www.opensource.org/licenses/eclipse-1.0.php
  *
  * @author: Michael Bauschert <Michael.Bauschert@de.ibm.com>
  *
  * Contributors: 
  * 
  * Description: Control the look of the menue, disabling menueitemss
  * 
  */
package org.sblim.wbemsmt.webapp.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.sblim.wbemsmt.bl.cleanup.Cleanup;
import org.sblim.wbemsmt.tools.runtime.RuntimeUtil;

public class MenueControllerBean  implements Cleanup {

	private TreeSelectorBean treeSelector;

	private boolean fileEnabled,
		saveEnabled,
		editEnabled,
		copyEnabled,
		pasteEnabled,
		pasteBelowEnabled,
		pasteSubnodeEnabled,
		deleteEnabled,
		createEnabled,
		reloadEnabled,
		signedOnTextEnabled,
		viewEnabled,
		useSlp;

	private boolean multiHost;
	private boolean embeddedHost;
	private boolean singleHost;
	private boolean testMode;
	
	public MenueControllerBean()
	{
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		String runtimeMode =(String) session.getAttribute(RuntimeUtil.RUNTIME_MODE);
		setFlags(runtimeMode);
	}

	public void setFlags(String runtimeMode) {
        multiHost = RuntimeUtil.MODE_MULTI.equals(runtimeMode);
        singleHost = RuntimeUtil.MODE_SINGLE.equals(runtimeMode);
        embeddedHost = RuntimeUtil.MODE_EMBEDDED.equals(runtimeMode);
        
        fileEnabled = singleHost;
        saveEnabled = false;
		editEnabled = true;
		copyEnabled = false;
		pasteEnabled = false;
		pasteBelowEnabled = false;
		pasteSubnodeEnabled = false;
		deleteEnabled = false;
		createEnabled = false;
		reloadEnabled = false;// moved to admin-area multiHost || singleHost;
		viewEnabled = false;
		signedOnTextEnabled = !multiHost;
	}
	
	public TreeSelectorBean getTreeSelector() {
		return treeSelector;
	}

	public void setTreeSelector(TreeSelectorBean treeSelector) {
		this.treeSelector = treeSelector;
	}

	public boolean isCopyEnabled() {
		return copyEnabled;
	}

	public void setCopyEnabled(boolean copyEnabled) {
		this.copyEnabled = copyEnabled;
	}

	public boolean isCreateEnabled() {
		return createEnabled;
	}

	public void setCreateEnabled(boolean createEnabled) {
		this.createEnabled = createEnabled;
	}

	public boolean isDeleteEnabled() {
		return deleteEnabled;
	}

	public void setDeleteEnabled(boolean deleteEnabled) {
		this.deleteEnabled = deleteEnabled;
	}

	public boolean isEditEnabled() {
		return editEnabled;
	}

	public void setEditEnabled(boolean editEnabled) {
		this.editEnabled = editEnabled;
	}

	public boolean isFileEnabled() {
		return fileEnabled;
	}

	public void setFileEnabled(boolean fileEnabled) {
		this.fileEnabled = fileEnabled;
	}

	public boolean isPasteBelowEnabled() {
		return pasteBelowEnabled;
	}

	public void setPasteBelowEnabled(boolean pasteBelowEnabled) {
		this.pasteBelowEnabled = pasteBelowEnabled;
	}

	public boolean isPasteEnabled() {
		return pasteEnabled;
	}

	public void setPasteEnabled(boolean pasteEnabled) {
		this.pasteEnabled = pasteEnabled;
	}

	public boolean isPasteSubnodeEnabled() {
		return pasteSubnodeEnabled;
	}

	public void setPasteSubnodeEnabled(boolean pasteSubnodeEnabled) {
		this.pasteSubnodeEnabled = pasteSubnodeEnabled;
	}

	public boolean isReloadEnabled() {
		return reloadEnabled;
	}

	public void setReloadEnabled(boolean reloadEnabled) {
		//moved to admin area
		//this.reloadEnabled = reloadEnabled;
		throw new RuntimeException("Cannot set reloadEnabled-Flag");
	}

	public boolean isViewEnabled() {
		return viewEnabled;
	}

	public void setViewEnabled(boolean viewEnabeled) {
		this.viewEnabled = viewEnabeled;
	}

	public boolean isSaveEnabled() {
		return saveEnabled;
	}

	public void setSaveEnabled(boolean saveEnabled) {
		this.saveEnabled = saveEnabled;
	}

	public boolean isSignedOnTextEnabled() {
		return signedOnTextEnabled;
	}

	public void setSignedOnTextEnabled(boolean signedOnTextEnabled) {
		this.signedOnTextEnabled = signedOnTextEnabled;
	}

	public boolean isEmbeddedHost() {
		return embeddedHost;
	}

	public void setEmbeddedHost(boolean embeddedHost) {
		this.embeddedHost = embeddedHost;
	}

	public boolean isUseSlp() {
		return useSlp;
	}

	public void setUseSlp(boolean useSlp) {
		this.useSlp = useSlp;
	}

	public void cleanup() {
		treeSelector = null;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}
	

	
	
	
	
	
}
