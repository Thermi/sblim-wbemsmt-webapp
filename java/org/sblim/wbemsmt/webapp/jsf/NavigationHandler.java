/**
 *  NavigationHandler.java
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
            TreeSelectorBean selector = (TreeSelectorBean) context.getApplication().getExpressionFactory().createValueExpression(context.getELContext(), "#{treeSelector}", Object.class).getValue(context.getELContext());
            selector.setCurrentTreeBacker(treeId);
            outcome = TreeSelectorBean.TREE_SELECTION_ACTION;
        }
        
        super.handleNavigation(context, fromAction, outcome);
    }
}
