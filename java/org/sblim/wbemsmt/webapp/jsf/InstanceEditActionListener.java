/**
 *  InstanceEditActionListener.java
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
 * @author: Marius Kreis <mail@nulldevice.org>
 *
 * Contributors:
 *
 */

package org.sblim.wbemsmt.webapp.jsf;

import java.util.Properties;
import java.util.logging.Logger;

import javax.faces.component.html.HtmlPanelGrid;

import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEvent;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEventListenerImpl;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;

public class InstanceEditActionListener extends TaskLauncherTreeNodeEventListenerImpl
{
    private static final Logger logger = TaskLauncherController.getLogger();
    
    private HtmlPanelGrid instanceList,
                          propertyList;
    
    public InstanceEditActionListener()
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
    }

    public String processEvent(TaskLauncherTreeNodeEvent event)
    {
    	return "";
    }

	public boolean isCustomListener() {
		return true;
	}
}
