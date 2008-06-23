/**
 *  LoginCheckBean.java
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
 * @author: Michael.Bauschert@de.ibm.com
 *
 * Contributors:
 *
 * Backing Bean for embeddedLogin.jsp - can be used for USMi
 * 
 */

package org.sblim.wbemsmt.webapp.jsf.embedded;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wbem.client.WBEMClient;

import org.apache.commons.lang.StringUtils;
import org.sblim.wbemsmt.bl.cleanup.Cleanup;
import org.sblim.wbemsmt.bl.messages.ErrCodes;
import org.sblim.wbemsmt.bl.messages.Message;
import org.sblim.wbemsmt.bl.tree.CimomTreeNode;
import org.sblim.wbemsmt.bl.tree.TaskLauncherDelegaterTreeNode;
import org.sblim.wbemsmt.exception.WbemsmtException;
import org.sblim.wbemsmt.exception.impl.LoginException;
import org.sblim.wbemsmt.exception.impl.userobject.LoginUserObject;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherController;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig.CimomData;
import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig.TreeConfigData;
import org.sblim.wbemsmt.tasklauncher.login.LoginCheck;
import org.sblim.wbemsmt.tools.jsf.JsfUtil;
import org.sblim.wbemsmt.util.StringTokenizer;
import org.sblim.wbemsmt.webapp.jsf.TreeSelectorBean;
import org.sblim.wbemsmt.webapp.jsf.WbemsmtWebAppBean;


/**
 * Backing Bean for embeddedLogin.jsp - can be used for USMi
 */
public class LoginCheckBean extends WbemsmtWebAppBean implements LoginCheck,Cleanup
{
	/**
     * This logger is used to create internal log entries
     */
//	private static final Logger logger = Logger.getLogger("org.sblim.wbemsmt.tasklauncher.jsf");
    
    private String target = "",
    			   namespace = "",
                   startView = "start",
                   timeoutView = "timeout",
                   task = "";

    private TaskLauncherController taskLauncherController;
    private TreeSelectorBean treeSelector;
    
    private Map targetsForTasks = new HashMap();
    private Map namespacesForTasks = new HashMap();
    
	private WBEMClient cimClient;

	private boolean loggedIn;

	private boolean useSlp;


    public LoginCheckBean()
    {
    	super();
    }
    
    private WBEMClient createCIMClient(boolean initModel,String username, String password, String hostname, String port, String protocol, List treeconfigs,boolean useSlp) throws WbemsmtException
    {
        if (initModel)
        {
        	try {
				this.taskLauncherController.init(hostname, port,protocol,username,password,useSlp,treeconfigs	);
				treeSelector.setTaskLauncherController(hostname,taskLauncherController);
			}catch (WbemsmtException e) {
	            WbemsmtException e1 = new LoginException(e, new LoginUserObject(username + "@" + protocol + "://" + hostname + ":" + port));
				throw e1;
			}
        }
        return null;
    }
    


    /**
     * Used by the Login Action within a portlet container
     * @return
     */
    public String loginEmbedded()
    {
    	
    	//first do a cleanup so that there is no old tree if the login failes
    	treeSelector.cleanup();
    	
		try {
			
			if (task == null)
			{
				Message message = Message.create(ErrCodes.MSG_TASK_NOT_SUPPORTED, Message.ERROR, bundle, "task.not.specified");
				JsfUtil.addMessage(message);
				return startView;
			}
			
			String[] targets = null;

			if (StringUtils.isEmpty(target))
			{
				Object targetsFromConfig = targetsForTasks.get(task);
				if (targetsFromConfig != null)
				{
					targets = new StringTokenizer((String) targetsFromConfig,",").asArray(true, false);
				}
				else
				{
					Message message = Message.create(ErrCodes.MSG_TASK_NOT_SUPPORTED, Message.ERROR, bundle, "target.not.specified");
					JsfUtil.addMessage(message);
					return startView;
				}
			}
			else
			{
				targets = new StringTokenizer(target,",").asArray(true, false);
			}
			
			String[] namespaces = null;
			if (StringUtils.isEmpty(namespace))
			{
				Object namespacesFromConfig = namespacesForTasks.get(task);
				if (namespacesFromConfig != null)
				{
					namespaces = new StringTokenizer((String) namespacesFromConfig,",").asArray(true, false);
				}
				else
				{
					Message message = Message.create(ErrCodes.MSG_TASK_NOT_SUPPORTED, Message.ERROR, bundle, "namespace.not.specified");
					JsfUtil.addMessage(message);
					return startView;
				}
				
			}
			else
			{
				namespaces = new StringTokenizer(namespace,",").asArray(true, false);
			}

			//first check if we have the same amount of target hosts and namespaces
			if (namespaces.length != targets.length)
			{
				Message message = Message.create(ErrCodes.MSG_TASK_NOT_SUPPORTED, Message.ERROR, bundle, "nr.of.namespaces.and.targets.not.matching");
				JsfUtil.addMessage(message);
				return startView;
			}
			
			CimomData datas[] = new CimomData[targets.length];
			for (int i = 0; i < targets.length; i++) {
				datas[i] = new CimomData();
				datas[i].setHostname(targets[i]);
				datas[i].setPort(5988);
				datas[i].setUser("pegasus");
				
				TreeConfigData treeConfig = taskLauncherController.getTaskLauncherConfig().getTreeConfigDataByTaskname(task);
				if (treeConfig == null)
				{
					Message message = Message.create(ErrCodes.MSG_TASK_NOT_SUPPORTED, Message.ERROR, bundle, "task.not.supported", new Object[]{task,targets[i]});
					JsfUtil.addMessage(message);
				}
				else
				{
					datas[i].addTreeConfig(treeConfig,null);
				}
			}
			this.cimClient = null;
			
			if (datas.length == 1)
			{
				buildSingleTarget(datas[0]);
			}
			else
			{
				buildMultiTarget(datas);
			}
			loggedIn = true;
		} catch (Exception e) {
			JsfUtil.handleException(e);
			return timeoutView;
		}
		return startView;
    }

	private void buildSingleTarget(CimomData data) throws WbemsmtException
	{
	    //TODO for USMi retrieve the CIMClient from a USMi context
		createCIMClient(true,
				data.getUser(), 
				"wbem01smt", 
				data.getHostname(), 
				"" + data.getPort(),
				"http",
				data.getTreeConfigs(),
				false
				);
	}

	private void buildMultiTarget(CimomData[] datas) throws WbemsmtException {

		taskLauncherController.createTreeFactoriesMultiHost(datas);
		treeSelector.setTaskLauncherController(TaskLauncherController.NAME_FOR_MULTI_CIMOM_TREE,taskLauncherController);
		//neded to get a standard tree with all the CIMOMs
		treeSelector.setCurrentTreeBacker(TaskLauncherController.NAME_FOR_MULTI_CIMOM_TREE);
		
		List rootNodes = treeSelector.getCurrentTreeFactory().getRootNodes();
		for (Iterator iter = rootNodes.iterator(); iter.hasNext();) {
			TaskLauncherDelegaterTreeNode delegaterNode = (TaskLauncherDelegaterTreeNode) iter.next();
			CimomTreeNode cimomNode = (CimomTreeNode) delegaterNode.getSubnodes().get(0);
			try {
				cimomNode.setCimClient(createCIMClient(false,
						cimomNode.getCimomData().getUser(), 
						"wbem01smt", 
						cimomNode.getCimomData().getHostname(), 
						"" + cimomNode.getCimomData().getPort(), 
						"http",
						cimomNode.getCimomData().getTreeConfigs(),false));
				
				cimomNode.getCimomData().setUser(cimomNode.getCimomData().getUser());
				cimomNode.setName(cimomNode.getCimomData().getHostname());
				cimomNode.buildTree();
				cimomNode.readSubnodes(false);
			} catch (Exception e) {
				cimomNode.setEnabled(false);
				JsfUtil.handleException(e);
			}				
		}

		treeSelector.getCurrentTreeBacker().updateTree();
		
	}

	public WBEMClient getCimClient() {
		return cimClient;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void reloadLoginSettings() throws WbemsmtException {
	}

	public void setCimomData(CimomData data) {
	}

	public void cleanup() {
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public TaskLauncherController getTaskLauncherController() {
		return taskLauncherController;
	}

	public void setTaskLauncherController(
			TaskLauncherController taskLauncherController) {
		this.taskLauncherController = taskLauncherController;
	}

	public TreeSelectorBean getTreeSelector() {
		return treeSelector;
	}

	public void setTreeSelector(TreeSelectorBean treeSelector) {
		this.treeSelector = treeSelector;
	}

	public String getStartView() {
		return startView;
	}

	public void setStartView(String startView) {
		this.startView = startView;
	}

	public Map getTargetsForTasks() {
		return targetsForTasks;
	}

	public void setTargetsForTasks(Map targetsForTasks) {
		this.targetsForTasks = targetsForTasks;
	}
	
	public Map getNamespacesForTasks() {
		return namespacesForTasks;
	}

	public void setNamespacesForTasks(Map namespacesForTasks) {
		this.namespacesForTasks = namespacesForTasks;
	}

	public boolean isUseSlp() {
		return useSlp;
	}

	public void setUseSlp(boolean useSlp) {
		this.useSlp = useSlp;
	}	

	
	
	

}