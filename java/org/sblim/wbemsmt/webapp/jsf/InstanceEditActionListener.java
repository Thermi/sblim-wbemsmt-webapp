/**
 *  InstanceEditActionListener.java
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

import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEvent;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEventListener;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEventListenerImpl;

public class InstanceEditActionListener extends TaskLauncherTreeNodeEventListenerImpl
{
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
	
	public Priority getPriority() {
		return TaskLauncherTreeNodeEventListener.PRIO_LOWEST;
	}
	
	
}
