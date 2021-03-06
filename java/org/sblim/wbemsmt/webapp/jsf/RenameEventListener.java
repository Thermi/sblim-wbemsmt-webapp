/**
 *  RenameEventListener.java
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
 * @author: Marius Kreis <mail@nulldevice.org>
 *
 * Contributors:
 *
 */

package org.sblim.wbemsmt.webapp.jsf;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.myfaces.custom.tree2.HtmlTree;
import org.sblim.wbemsmt.bl.tree.ITaskLauncherTreeNode;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEvent;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEventListener;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEventListenerImpl;


public class RenameEventListener extends TaskLauncherTreeNodeEventListenerImpl
{
    private static final Logger logger = Logger.getLogger(RenameEventListener.class.getName());
    private static final String PROPERTIES_PANEL_ID = "submenu";
    private String panelID = PROPERTIES_PANEL_ID;
    
    public RenameEventListener()
    {
        super();

    }
    
    public void setParameters(Properties parameters)
    {
    	super.setParameters(parameters);
    	processParameters();
    }
    
    private void processParameters()
    {
    	this.panelID = (parameters.containsKey("panel_id")) ? parameters.getProperty("panel_id") : PROPERTIES_PANEL_ID;
    	logger.finest("processing with panelID " + panelID );
    }

    public String processEvent(TaskLauncherTreeNodeEvent event)
    {
        logger.log(Level.INFO, "RenameEventListener is processing event....");
        
        TreeNodeActionListener action = (TreeNodeActionListener) event.getSource();
    
        HtmlTree tree = (HtmlTree) action.getComponent();
        JsfTreeNode node = (JsfTreeNode) tree.getNode();
        
        ITaskLauncherTreeNode launcherNode = node.getTaskLauncherTreeNode();    
        launcherNode.setName("haseee");
        
        return "";
    }
    
    public boolean processesEvent(int eventType)
    {
    	return (eventType & TaskLauncherTreeNodeEvent.TYPE_CLICKED) > 0;
    }

    public boolean isCustomListener() {
		return true;
	}

	public Priority getPriority() {
		return TaskLauncherTreeNodeEventListener.PRIO_LOWEST;
	}
    
}
