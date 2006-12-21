/**
 *  TreeNodeActionListener.java
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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.apache.myfaces.custom.tree2.HtmlTree;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherTreeNode;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants;

public class TreeNodeActionListener implements ActionListener
{
    private static final Logger logger = Logger.getLogger(TreeNodeActionListener.class.getName());
    private UIComponent component;
    private FacesContext facesContext;
    private UIViewRoot viewRoot;

    public TreeNodeActionListener()
    {
    }

    public void processAction(ActionEvent actionEvent)
    {
        logger.log(Level.INFO, "Processing TreeNode clicked...");
        this.component = actionEvent.getComponent();
        this.facesContext = FacesContext.getCurrentInstance();
        this.viewRoot = facesContext.getViewRoot();
        
        // find the parent of the component which is a HtmlTree
        while(component != null && !(component instanceof HtmlTree))
        {
            component = component.getParent();
        }
        if(component == null) throw new AbortProcessingException("Component is not child of a HtmlTree");
        
        
        HtmlTree tree = (HtmlTree) component;
        JsfTreeNode node = (JsfTreeNode) tree.getNode();
        
        TreeSelectorBean treeSelectorBean = (TreeSelectorBean)BeanNameConstants.TREE_SELECTOR.getBoundValue(facesContext);
        treeSelectorBean.setSelectedNode(node);
        
        ObjectActionControllerBean objectActionController = (ObjectActionControllerBean)BeanNameConstants.OBJECT_ACTION_CONTROLLER.getBoundValue(facesContext);
        objectActionController.setSelectedNode(node.getTaskLauncherTreeNode());
        
        TaskLauncherTreeNode treeNode = node.getTaskLauncherTreeNode();

        String result = treeNode.click();
        if (result != null)
        {
        	treeSelectorBean.setCurrentOutcome(result);        
        }
    }

    public UIComponent getComponent()
    {
        return component;
    }

    public FacesContext getFacesContext()
    {
        return facesContext;
    }

    public UIViewRoot getViewRoot()
    {
        return viewRoot;
    }
}
