 /** 
  * WbemsmtWebAppTreeState.java
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
  * @author: Michael Bauschert <Michael.Bauschert@de.ibm.com>
  *
  * Contributors: 
  * 
  * Description:
  * 
  */
package org.sblim.wbemsmt.webapp.jsf;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.myfaces.custom.tree2.TreeState;
import org.sblim.wbemsmt.exception.WbemSmtException;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherTreeNode;

public class WbemsmtWebAppTreeState implements TreeState {

	
    private static final Logger logger = Logger.getLogger(WbemsmtWebAppTreeState.class.getName());

    private static final long serialVersionUID = 1L;
	private final TreeState state;
	private final TaskLauncherTreeNode rootNode;

	/**
	 * @param treeModel 
	 * 
	 */
	public WbemsmtWebAppTreeState(TaskLauncherTreeNode rootNode, TreeState state) {
		super();
		this.rootNode = rootNode;
		if (state instanceof WbemsmtWebAppTreeState) {
			WbemsmtWebAppTreeState taskLauncherState = (WbemsmtWebAppTreeState) state;
			this.state = taskLauncherState.getState();
		}
		else
		{
			this.state = state;
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.myfaces.custom.tree2.TreeState#isNodeExpanded(java.lang.String)
	 */
	public boolean isNodeExpanded(String nodeId) {
		try {
			return state.isNodeExpanded(translate(nodeId));
		} catch (Exception e) {
			//JsfUtil.handleException(e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.myfaces.custom.tree2.TreeState#toggleExpanded(java.lang.String)
	 */
	public void toggleExpanded(String nodeId) {
		try {
			state.toggleExpanded(translate(nodeId));
		} catch (Exception e) {
			//JsfUtil.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.myfaces.custom.tree2.TreeState#expandPath(java.lang.String[])
	 */
	public void expandPath(String[] nodePath) {
		try {
			state.expandPath(translate(nodePath));
		} catch (Exception e) {
			//JsfUtil.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.myfaces.custom.tree2.TreeState#collapsePath(java.lang.String[])
	 */
	public void collapsePath(String[] nodePath)
	{
		try {
			state.collapsePath(translate(nodePath));
		} catch (Exception e) {
			//JsfUtil.handleException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.myfaces.custom.tree2.TreeState#isTransient()
	 */
	public boolean isTransient() {
		return state.isTransient();
	}

	/* (non-Javadoc)
	 * @see org.apache.myfaces.custom.tree2.TreeState#setTransient(boolean)
	 */
	public void setTransient(boolean trans) {
		state.setTransient(trans);
	}

	/* (non-Javadoc)
	 * @see org.apache.myfaces.custom.tree2.TreeState#setSelected(java.lang.String)
	 */
	public void setSelected(String nodeId) {
		state.setSelected(nodeId);

	}

	/* (non-Javadoc)
	 * @see org.apache.myfaces.custom.tree2.TreeState#isSelected(java.lang.String)
	 */
	public boolean isSelected(String nodeId) {
		return state.isSelected(nodeId);
	}

	public String translate(String nodePathArray) throws WbemSmtException
	{
		return translate(new String[]{nodePathArray})[0];
	}
	
	public String[] translate(String nodePathArray[]) throws WbemSmtException
	{
		String[] result = new String[nodePathArray.length];
		
		for (int i = 0; i < nodePathArray.length; i++) {
			String nodePath = nodePathArray[i];
			StringTokenizer tokenizer = new StringTokenizer(nodePath,":");
			StringBuffer translatedPath = new StringBuffer();
			Vector childs = new Vector();
			childs.add(rootNode);
			while (tokenizer.hasMoreTokens())
			{
				if (translatedPath.length() > 0)
				{
					translatedPath.append(":");
				}

				String strIndex = tokenizer.nextToken();
				int index = Integer.parseInt(strIndex);
				TaskLauncherTreeNode node = (TaskLauncherTreeNode) childs.get(index);
				childs = node.getSubnodes();
				translatedPath.append(node.getName());
			}
			logger.finest("Translated " + nodePath + " to " + translatedPath.toString() );
			result[i] = translatedPath.toString();
		}
		
		return result;
	}

	public TreeState getState() {
		if (state instanceof WbemsmtWebAppTreeState) {
			WbemsmtWebAppTreeState treeState = (WbemsmtWebAppTreeState) state;
			return treeState.getState();
		}
		else
		{
			return state;
		}
	}
	
	
	
}
