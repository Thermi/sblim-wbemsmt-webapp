/**
 *  ShowInstancesActionListener.java
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

import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.event.AbortProcessingException;

import org.apache.myfaces.custom.tree2.HtmlTree;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEvent;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEventListener;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEventListenerImpl;
import org.sblim.wbemsmt.tasklauncher.CIMClassNode;
import org.sblim.wbemsmt.tasklauncher.CIMInstanceNode;

public class ShowInstancesActionListener extends TaskLauncherTreeNodeEventListenerImpl
{
    private static final Logger logger = Logger.getLogger(ShowInstancesActionListener.class.getName());
    private static final String ROOT_PANEL_ID = "content";
    private static final String PROPERTIES_PANEL_ID = "properties";
    private String panelID = ROOT_PANEL_ID;
    
    private HtmlPanelGrid instanceList,
                          propertyList;
    
    public ShowInstancesActionListener()
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
    	this.panelID = (parameters.containsKey("panel_id")) ? parameters.getProperty("panel_id") : ROOT_PANEL_ID;
    }

    public String processEvent(TaskLauncherTreeNodeEvent event)
    {
    	if((event.getType() & TaskLauncherTreeNodeEvent.TYPE_CLICKED) == 0) return "";
    	
        TreeNodeActionListener action = (TreeNodeActionListener) event.getSource();
        UIViewRoot root = action.getViewRoot();
        
        // looking for our PanelGrid to add elements to
        HtmlPanelGrid panel = (HtmlPanelGrid) root.findComponent(panelID);
        if(panel == null)
    	{
        	logger.log(Level.SEVERE, "HtmlPanelGrid with id " + panelID + " was not found.");
        	throw new AbortProcessingException("HtmlPanelGrid with id " + panelID + " was not found.");
    	}

        // get source component, check if the clicked node can be processed
        HtmlTree tree = (HtmlTree) action.getComponent();
        JsfTreeNode node = (JsfTreeNode) tree.getNode();
        if(!(node.getTaskLauncherTreeNode() instanceof CIMClassNode)) return ""; //throw new AbortProcessingException("selected Node is not a CIMClassNode");
        
        // gather infos
        logger.log(Level.INFO, "Showing Instances of CIM Class " + node.getDescription());
        CIMClassNode cimclassNode = (CIMClassNode) node.getTaskLauncherTreeNode();        
        Vector instanceNodes = cimclassNode.getInstanceNodes();
        
        // generate elements
        buildLayout(panel);
        fillInstances(instanceNodes, instanceList);
        
        return "";
    }
    
    
    private void buildLayout(HtmlPanelGrid panel)
    {
        panel.getChildren().clear();
        panel.setColumns(2);
        
        instanceList = new HtmlPanelGrid();
        instanceList.setRowClasses("oddRow, evenRow");
        
        propertyList = new HtmlPanelGrid();
        propertyList.setId(PROPERTIES_PANEL_ID);
        propertyList.setRowClasses("oddRow, evenRow");
        propertyList.setColumns(2);
        
        panel.getChildren().add(instanceList);
        panel.getChildren().add(propertyList);
    }
    
    
    private void fillInstances(Vector instanceNodes, HtmlPanelGrid panel)
    {
        panel.getChildren().clear();
        int i = 0;
        for (Iterator iter = instanceNodes.iterator(); iter.hasNext();) {
			CIMInstanceNode instanceNode = (CIMInstanceNode) iter.next();
            HtmlCommandLink instanceLink = new HtmlCommandLink();
            instanceLink.setId("_m" + i++);
            //+ javax.faces.context.FacesContext.getCurrentInstance().getViewRoot().createUniqueId()
            instanceLink.setValue(instanceNode.getName());

            ShowPropertiesAction ppcontroller = new ShowPropertiesAction(instanceNode, PROPERTIES_PANEL_ID);
            instanceLink.addActionListener(ppcontroller);
            panel.getChildren().add(instanceLink);
        }
    }
    
	public boolean isCustomListener() {
		return true;
	}

	public Priority getPriority() {
		return TaskLauncherTreeNodeEventListener.PRIO_LOWEST;
	}
	
}
