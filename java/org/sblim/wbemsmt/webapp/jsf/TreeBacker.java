/**
 *  TreeBacker.java
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
 * Author:     Marius Kreis
 *
 * Contributors: Michael.Bauschert@de.ibm.com
 *
 */

package org.sblim.wbemsmt.webapp.jsf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.myfaces.custom.tree2.HtmlTree;
import org.apache.myfaces.custom.tree2.TreeModel;
import org.apache.myfaces.custom.tree2.TreeModelBase;
import org.sblim.wbemsmt.bl.help.HelpManager;
import org.sblim.wbemsmt.bl.tree.ITaskLauncherTreeNode;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEvent;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEventListener;
import org.sblim.wbemsmt.exception.WbemSmtException;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherContextMenu;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherDelegaterTreeNode;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherTreeFactory;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherTreeNode;
import org.sblim.wbemsmt.tasklauncher.jsf.ExpandAllListener;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;
import org.sblim.wbemsmt.tools.resources.ILocaleManager;
import org.sblim.wbemsmt.tools.resources.ResourceBundleManager;
import org.sblim.wbemsmt.tools.resources.WbemSmtResourceBundle;

/**
 * 
 * @author Bauschert
 *
 * Set the vm property TASKLAUNCHER_ADD_EXPAND_ALL to true to expand all Leafs of the Tree
 */
public class TreeBacker implements TaskLauncherTreeNodeEventListener
{
    private static final String SYSTEM_PROPERTY_TASKLAUNCHER_ADD_EXPAND_ALL = "tasklauncher.add.expand.all";


	public static final String CONTEXT_MENUES = "ContextMenues";


	private static final Logger logger = Logger.getLogger("org.sblim.wbemsmt.webapp.jsf");
   
    
    private boolean treeModified = false;
    
    
    private TaskLauncherTreeFactory treeFactory;
    
    private TaskLauncherTreeNode taskLauncherRootNode;
    private JsfTreeNode jsfRootNode;
    private TreeModel treeModel;
    private HtmlTree tree;
    
    private boolean treeUpdated = false;
    
    /**
     * Set this value to true if the tree should be displayed will all nodes expanded
     */
    private boolean expandAll = false;



	private ArrayList menuList = new ArrayList();


	protected WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(FacesContext.getCurrentInstance(),getClass().getClassLoader());



	private Set menuIds = new HashSet();

	
    public TreeBacker()
    {
        this.jsfRootNode = new JsfTreeNode("root", bundle.getString("loading"), false);
        this.tree = new HtmlTree();
        addListener();
    }

	private void addListener() {
	}
    
    public TreeBacker(TaskLauncherTreeFactory treeFactory)
    {
    	this();
        setTreeFactory(treeFactory);
    }
    
    public void setTreeFactory(TaskLauncherTreeFactory treeFactory)
    {
        this.treeFactory = treeFactory;
    }
    
    public HtmlTree getTree()
    {
        return tree;
    }
    
    public void setTree(HtmlTree tree)
    {
        this.tree = tree;
        addListener();
    }

    public TreeModel getTreeModel() throws WbemSmtException
    {
    	// update only at the first time
    	if(this.treeUpdated == false)
		{ 
			this.updateTree();
			this.treeUpdated = true;
		}
        logger.log(Level.FINE, "returning JSF TreeModel...");
        return this.treeModel;
    }
    
    public TreeModel getTreeModelDirect()
    {
        return this.treeModel;
    }

    public void updateTree(ActionEvent e)
    {
        try {
			updateTree();
		} catch (Exception e1) {
			JsfUtil.handleException(e1);
		}
    }
    
	public String updateTree() throws WbemSmtException
    {        
		
		//Reset the Help
		HelpManager hm = (HelpManager)BeanNameConstants.HELP_MANAGER.getBoundValue(FacesContext.getCurrentInstance());
		hm.resetTopic();
		
    	logger.log(Level.INFO, "Updating Tree...");
        if(this.treeFactory == null)
        {
            this.jsfRootNode.getChildren().add(new JsfTreeNode("custom", bundle.getString("loading"), false));
        }
        else
        {
            logger.log(Level.FINE, "Building JSF Tree...");
            // update reference to root node of the cim tree
        	this.taskLauncherRootNode = new TaskLauncherDelegaterTreeNode(this.treeFactory.getRootNodes(),"root");
        	
        	this.jsfRootNode = new JsfTreeNode(JsfTreeNode.FACET_TREENODE, taskLauncherRootNode.getName(), false, taskLauncherRootNode);
            
            jsfRootNode.getChildren().clear();
        }

        this.treeModel = new TreeModelBase(this.jsfRootNode);
        
        initTreeState();
        
        if(this.treeFactory != null)
        {
            this.taskLauncherRootNode.readSubnodes(true);
        	if (expandAll || 
        		"true".equalsIgnoreCase(System.getProperty(TreeBacker.SYSTEM_PROPERTY_TASKLAUNCHER_ADD_EXPAND_ALL,"false")))
        	{
        		List childs = taskLauncherRootNode.getSubnodes(false);
        		for (Iterator iter = childs.iterator(); iter.hasNext();) {
					TaskLauncherTreeNode child = (TaskLauncherTreeNode) iter.next();
					ExpandAllListener listener = new ExpandAllListener();
					child.addEventListener(listener);
				}
        		expandAll();
        	}
        }
        
        
        String result = restoreOldSelection();
        
        
    	initMenuList();
    	
        treeUpdated = true;
        
        return result;
    }

	/**
	 * Restores the old selection within the tree and the previous selected tab in the corresponding editPanel
	 * @param result
	 * @return
	 * @throws WbemSmtException 
	 */
	private String restoreOldSelection() throws WbemSmtException {
		
		String result = "start";
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ObjectActionControllerBean objectActionController = (ObjectActionControllerBean)BeanNameConstants.OBJECT_ACTION_CONTROLLER.getBoundValue(facesContext);
		TreeSelectorBean treeSelectorBean =  (TreeSelectorBean) BeanNameConstants.TREE_SELECTOR.getBoundValue(FacesContext.getCurrentInstance());

		TaskLauncherTreeNode selectedNode = objectActionController.getSelectedNode();
		int selectTabIndex = objectActionController.getSelectedTabIndex();
		String selectTabId = objectActionController.getSelectedTabId();

		if (selectedNode != null)
        {
    		selectedNode = (TaskLauncherTreeNode) getRootNode().getTaskLauncherTreeNode().findNode(selectedNode);
    		if (selectedNode != null)
    		{
        		treeSelectorBean.setSelectedTaskLauncherTreeNode(selectedNode);

        		result = selectedNode.click(true);

        		objectActionController.setSelectedNode(selectedNode);
        		objectActionController.setSelectedTabIndex(selectTabIndex);
            	objectActionController.setSelectedTabId(selectTabId);
    		}
    		
        
        }
		return result;
	}

	private void initTreeState() {
		WbemsmtWebAppTreeState treeState = new WbemsmtWebAppTreeState(taskLauncherRootNode,treeModel.getTreeState());
		treeModel.setTreeState(treeState);
		treeState.setSelected("0");
		TreeSelectorBean treeSelector =  (TreeSelectorBean) BeanNameConstants.TREE_SELECTOR.getBoundValue(FacesContext.getCurrentInstance());
		treeSelector.setSelectedNode(null);
        //if state is not transient the tree is collapsed after each page change
        this.treeModel.getTreeState().setTransient(true);
        
	}

	private void initMenuList() {
		
		menuList.clear();
		menuIds .clear();
    	JsfTreeNode rootNode = getRootNode();
    	addMenue(menuList,rootNode);
    	
    	ILocaleManager manager = (ILocaleManager) BeanNameConstants.LOCALE_MANAGER.getBoundValue(FacesContext.getCurrentInstance());
    	
    	for (Iterator iter = menuList.iterator(); iter.hasNext();) {
			TaskLauncherContextMenu menu = (TaskLauncherContextMenu) iter.next();
			menu.initI18n(manager);
		}
    	
	}

	/**
	 * get the root node of the Tree
	 * @return
	 */
	public JsfTreeNode getRootNode() {
		return (JsfTreeNode) treeModel.getNodeById("0");
	}

	private void addMenue(List menues, JsfTreeNode nodeById) {
		
		nodeById.getTaskLauncherTreeNode().addEventListener(this);
		
		if (nodeById.getHasContextMenu())
		{
			TaskLauncherContextMenu menu = nodeById.getContextMenu();
			menu.setNode(nodeById.getTaskLauncherTreeNode());
			if (menuIds.contains(menu.getId()))
			{
				logger.severe("Duplicate ID " + menu.getId() + " for node " + menu.getNode().getName());
			}
			else
			{
				menuIds.add(menu.getId());
				menues.add(menu);
			}
		}
		List children = nodeById.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			JsfTreeNode childNode = (JsfTreeNode) iter.next();
			addMenue(menues,childNode);
		}
	}
	

	public String processEvent(TaskLauncherTreeNodeEvent event) {
		if (event.getType() == TaskLauncherTreeNodeEvent.TYPE_REFRESHED)
		{
			treeModified = true;
			logger.finest("Tree is modified " + treeModified);
			initMenuList();
			initTreeState();
			//refreshContextMenu();
		}
		return null;
	}

	public void setParameters(Properties parameters) {
	}

	public Properties getParameters() {
		return null;
	}

	public boolean isCustomListener() {
		return false;
	}

	public String expandAll() {
		TreeModel treeModel = getTreeModelDirect();
		JsfTreeNode node = getRootNode();
		expandNode(treeModel,node);
		
		try {
			String result = restoreOldSelection();
			return result;
		} catch (WbemSmtException e) {
			JsfUtil.handleException(e);
		}
		return "";
	}


	private void expandNode(TreeModel treeModel, JsfTreeNode node) {
		String[] pathToExpand = node.getPath(getTree());
		treeModel.getTreeState().expandPath(pathToExpand);
		
		List children = node.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			JsfTreeNode childNode = (JsfTreeNode) iter.next();
			expandNode(treeModel,childNode);
		}
	}

	public void collapseAll(ActionEvent event) {
		collapseAll();
	}

	public void collapseAll() {
		TreeModel treeModel = getTreeModelDirect();
		JsfTreeNode node = getRootNode();
		collapseNode(treeModel,node);
	}


	private void collapseNode(TreeModel treeModel, JsfTreeNode node) {
		String[] pathToExpand = node.getPath(getTree());
		treeModel.getTreeState().collapsePath(pathToExpand);
		
		List children = node.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			JsfTreeNode childNode = (JsfTreeNode) iter.next();
			collapseNode(treeModel,childNode);
		}
	}

	public boolean isExpandAll() {
		return expandAll;
	}

	public void setExpandAll(boolean expandAll) {
		this.expandAll = expandAll;
	}

	public JsfTreeNode findJsfTreeNode(ITaskLauncherTreeNode taskLauncherTreeNode)
	{
		return jsfRootNode.find(taskLauncherTreeNode);
	}

	public TaskLauncherTreeFactory getTreeFactory() {
		return treeFactory;
	}
	
	
	
	
}
