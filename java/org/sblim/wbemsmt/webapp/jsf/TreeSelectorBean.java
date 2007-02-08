/**
 *  TreeSelectorBean.java
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
 * Contributors: Michael Bauschert
 *
 */

package org.sblim.wbemsmt.webapp.jsf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.myfaces.custom.navmenu.NavigationMenuItem;
import org.apache.myfaces.custom.tree2.HtmlTree;
import org.sblim.wbemsmt.bl.Cleanup;
import org.sblim.wbemsmt.bl.tree.ITaskLauncherTreeNode;
import org.sblim.wbemsmt.bl.tree.ITreeSelector;
import org.sblim.wbemsmt.exception.WbemSmtException;
import org.sblim.wbemsmt.tasklauncher.ITaskLauncherUiTreeNode;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherTreeFactory;
import org.sblim.wbemsmt.tasklauncher.TreeSelector;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;
import org.sblim.wbemsmt.tools.runtime.RuntimeUtil;



public class TreeSelectorBean extends TreeSelector implements ITreeSelector, Cleanup
{
    public static final String TREE_SELECTION_ACTION = "treeselect_";
    
    public static final Logger logger = Logger.getLogger(TreeSelectorBean.class.getName());
    
    private HashMap treeBackerMap;
    private NavigationMenuItem[] menuItems;
    
    private JsfTreeNode clipboard;
    private JsfTreeNode selectedNode;
    
    private String currentOutcome;
    /**
     * Set by the ManagedBeanFacilty to define if the Tree will be expanded after loading
     * 
     */
    private boolean expandAll = false;
    
    public TreeSelectorBean()
    {
        this.menuItems = new NavigationMenuItem[0];
        this.treeBackerMap = new HashMap();
    }


    public NavigationMenuItem[] getMenuItems()
    {
        return this.menuItems;
    }

    public TreeBacker getCurrentTreeBacker()
    {
    	return getCurrentTreeBacker(false);
    }
    
    public TreeBacker getCurrentTreeBacker(boolean silent)
    {
        if(!this.treeBackerMap.isEmpty())
        {
            TreeBacker treeBacker = (TreeBacker) treeBackerMap.get(this.getCurrentTreeBackerName());
            if (treeBacker != null)
            {
            	return treeBacker;
            }
            else
            {
            	if (!silent)throw new RuntimeException("No TreeBacker found with name " + getCurrentTreeBackerName());
            }
        }
        if (!silent)throw new RuntimeException("Cannot get Treebacker - No TreeBackers set." + getCurrentTreeBackerName());
        return null;
    }
    
    public HtmlTree getCurrentTree()
    {
        if(!this.treeBackerMap.isEmpty())
        {
             HtmlTree tree = getCurrentTreeBacker().getTree();
             tree.setId("_bla");
             return tree;
        }
        else return null;
    }
    
    public void setCurrentTree(HtmlTree tree)
    {
    	//do nothing but fullfil the lwc wanted definition of a setter method
    	//even if its not used...
    }
    
    
    public JsfTreeNode getClipboard()
    {
        return (JsfTreeNode) this.clipboard.clone();
    }

    public String copy()
    {
        if(this.selectedNode != null)
        {
            logger.log(Level.INFO, "Copying node " + selectedNode.getDescription() + " to Clipboard");
            this.clipboard = (JsfTreeNode) selectedNode.clone();
        }
        return "";
    }
    
    public String saveCurrentTreeConfigAction()
    {
        super.saveCurrentTreeConfig();
        return "";
    }

    public String pasteAbove()
    {
        if(this.clipboard != null && this.selectedNode != null)
        {
            logger.log(Level.INFO, "Paste current node " + clipboard.getDescription() + " above " + selectedNode.getDescription());
            selectedNode.pasteAbove(this.getClipboard());
        }
        return "";
    }

    public String pasteBelow()
    {
        if(this.clipboard != null && this.selectedNode != null)
        {
            logger.log(Level.INFO, "Paste current node " + clipboard.getDescription() + " below " + selectedNode.getDescription());
            selectedNode.pasteBelow(this.getClipboard());
        }
        return "";
    }
    
    public String pasteSubnode()
    {
        if(this.clipboard != null && this.selectedNode != null)
        {
            logger.log(Level.INFO, "Paste current node " + clipboard.getDescription() + " as subnode of " + selectedNode.getDescription());
            selectedNode.pasteSubnode(this.getClipboard());
        }
        return "";
    }
    
    public String deleteNode()
    {
        if(this.selectedNode != null)
        {
            logger.log(Level.INFO, "Deleting node " + selectedNode.getDescription());
            selectedNode.delete();
        }
        return "";
    }
    
    /* (non-Javadoc)
	 * @see org.sblim.wbemsmt.webapp.jsf
	 */
    public void setSelectedNode(ITaskLauncherUiTreeNode uiTreeNode)
    {
    	if (uiTreeNode instanceof JsfTreeNode)
    	{
    		JsfTreeNode jsfNode = (JsfTreeNode)uiTreeNode;
    		this.selectedNode = jsfNode;
			String[] path = selectedNode.getPath(getCurrentTreeBacker().getTree());
			getCurrentTreeBacker().getTree().expandPath(path);
			getCurrentTreeBacker().getTreeModelDirect().getTreeState().setSelected(path[path.length-1]);
			selectedTasklauncherTreeNode = selectedNode.getTaskLauncherTreeNode();
    	}
    	else if (uiTreeNode != null)
    	{
			logger.warning("TreeSelectorBean cannot handle nodes from type " + uiTreeNode.getClass().getName());
    		this.selectedNode = null;    		
    		selectedTasklauncherTreeNode = null;
    	}
    	else
    	{
    		this.selectedNode = null;    		
    		selectedTasklauncherTreeNode = null;
    	}
    }
    
	public void setSelectedTaskLauncherTreeNode(ITaskLauncherTreeNode node) {
		if (node != null)
		{
			JsfTreeNode jsfNode = getCurrentTreeBacker().findJsfTreeNode(node);
			if (jsfNode != null)
			{
				setSelectedNode(jsfNode);
				
			}
			else
			{
				logger.warning("The node " + node.getInfo() + " was not found in Tree. Tree is not synchronized");
			}
		}
		else
		{
			setSelectedNode(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.sblim.wbemsmt.webapp.jsf.ITreeSelector#getSelectedNode()
	 */
    public ITaskLauncherUiTreeNode getSelectedNode() {
		return selectedNode;
	}


	public String getCurrentTreeBackerName()
    {
        if(!this.treeBackerMap.isEmpty())
        {
            if(this.currentTreeName == null)
            {
                // just get the first element
                this.currentTreeName = (String) treeBackerMap.keySet().iterator().next();
            }
            return this.currentTreeName;
        }
        else return "";
    }

    public void setTaskLauncherController(String cimClientName, TaskLauncherController controller) throws WbemSmtException
    {
        super.setTaskLauncherController(controller);
        this.createMenuAndTreebacker();
    	setCurrentTreeBacker(cimClientName);
    	getCurrentTreeBacker().updateTree();
        
    }
    
    private void createMenuAndTreebacker()
    {
        this.menuItems = new NavigationMenuItem[factories.size()];
        int i=0;
        for (Iterator iter = factories.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
            menuItems[i] = new NavigationMenuItem(name, TreeSelectorBean.TREE_SELECTION_ACTION + name);
            i++;
            logger.log(Level.INFO, "Adding TreeBacker \"" + name + "\"");
            TreeBacker treeBacker = new TreeBacker((TaskLauncherTreeFactory) factories.get(name));
            treeBacker.setExpandAll(expandAll);
			this.treeBackerMap.put(name, treeBacker);
        }
    }
    
    public String reloadSLP()
    {
    	try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			String runtimeMode =(String) session.getAttribute(RuntimeUtil.RUNTIME_MODE);
			super.reloadConfig(runtimeMode);
			getCurrentTreeBacker().updateTree();
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Configuration was reloaded using SLP",""));
		} catch (WbemSmtException e) {
			JsfUtil.handleException(e);
		}
    	return "start";
    }


	/* (non-Javadoc)
	 * @see org.sblim.wbemsmt.webapp.jsf.ITreeSelector#getCurrentOutcome()
	 */
	public String getCurrentOutcome() {
		return currentOutcome == null ? "start" : currentOutcome;
	}


	/* (non-Javadoc)
	 * @see org.sblim.wbemsmt.webapp.jsf.ITreeSelector#setCurrentOutcome(java.lang.String)
	 */
	public void setCurrentOutcome(String currentOutcome) {
		this.currentOutcome = currentOutcome;
	}

	public void expandAll() {
		getCurrentTreeBacker().expandAll();
	}

	public void cleanup()
	{
		treeBackerMap.clear();
	}


	public boolean isExpandAll() {
		return expandAll;
	}


	public void setExpandAll(boolean expandAll) {
		this.expandAll = expandAll;
	}


	
    
    
    
}