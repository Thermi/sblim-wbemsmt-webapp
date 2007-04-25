/**
 *  ContextMenuActionListener.java
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
 * @author: Marius Kreis <mail@nulldevice.org>
 *
 * Contributors:
 *
 */

package org.sblim.wbemsmt.webapp.jsf;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.sblim.wbemsmt.bl.tree.ITaskLauncherTreeNode;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEvent;
import org.sblim.wbemsmt.tasklauncher.event.TaskLauncherContextMenuEventListener;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;

public class ContextMenuActionListener implements ActionListener
{
	private TaskLauncherContextMenuEventListener listener;
	
	public ContextMenuActionListener(TaskLauncherContextMenuEventListener listener)
	{
		this.listener = listener;
	}
	
	public void processAction(ActionEvent e)
	{
		try {
			listener.processEvent(new TaskLauncherTreeNodeEvent(this,(ITaskLauncherTreeNode)e.getSource(), FacesContext.getCurrentInstance()));
		} catch (Exception ex) {
			JsfUtil.handleException(ex);
		}
	}
}
