/**
 *  NavigationHandler.java
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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import org.apache.myfaces.application.NavigationHandlerImpl;

public class NavigationHandler extends NavigationHandlerImpl
{
    private static final Logger logger = Logger.getLogger(NavigationHandler.class.getName());
    
    public NavigationHandler()
    {
        super();
    }
    
    
    public void handleNavigation(FacesContext context,
            String fromAction,
            String outcome)
    {
        if(outcome != null && outcome.startsWith(TreeSelectorBean.TREE_SELECTION_ACTION))
        {
            String treeId = outcome.substring(TreeSelectorBean.TREE_SELECTION_ACTION.length());
            logger.log(Level.FINE, "Changing Current Tree to: " + treeId);
            TreeSelectorBean selector = (TreeSelectorBean) context.getApplication().createValueBinding("#{treeSelector}").getValue(context);
            selector.setCurrentTreeBacker(treeId);
            outcome = TreeSelectorBean.TREE_SELECTION_ACTION;
        }
        
        super.handleNavigation(context, fromAction, outcome);
    }
}
