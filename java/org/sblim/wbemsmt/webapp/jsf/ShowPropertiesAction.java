/**
 *  ShowPropertiesAction.java
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

import org.sblim.wbem.cim.CIMObject;
import org.sblim.wbem.cim.CIMProperty;
import org.sblim.wbemsmt.bl.tree.TaskLauncherTreeNodeEvent;
import org.sblim.wbemsmt.tasklauncher.CIMInstanceNode;

public class ShowPropertiesAction implements ActionListener, java.io.Serializable
{
    private static final long serialVersionUID = 54644547768L;
    private static final Logger logger = Logger.getLogger(ShowPropertiesAction.class.getName());
    
    private CIMInstanceNode instanceNode;
    private String panelId;
    
    
    public ShowPropertiesAction(CIMInstanceNode instanceNode, String panelId)
    {
        super();
        this.instanceNode = instanceNode;
        this.panelId = panelId;
    }

    
    public CIMInstanceNode getInstance()
    {
        return this.instanceNode;
    }

    public void setInstanceNode(CIMInstanceNode instanceNode)
    {
        this.instanceNode = instanceNode;
    }

    public void processAction(ActionEvent actionEvent)
            throws AbortProcessingException
    {
        FacesContext  facesContext = FacesContext.getCurrentInstance();
        UIViewRoot    root = facesContext.getViewRoot();
        HtmlPanelGrid panel = (HtmlPanelGrid) root.findComponent(panelId);
        CIMObject     cimObject = this.instanceNode.getCimInstance();
                
        logger.log(Level.FINEST, "Showing Properties for CIM Object " + cimObject.getName());
        

        if(panel != null)
        {
            panel.getChildren().clear();
            
            for (Iterator iter = cimObject.getAllProperties().iterator(); iter.hasNext();) {
				Object object = (Object) iter.next();
                CIMProperty property = (CIMProperty) object;
                
                HtmlOutputText name = new HtmlOutputText();
                name.setValue(property.getName());
                panel.getChildren().add(name);
                
                HtmlOutputText value = new HtmlOutputText();
                value.setValue(property.getValue());
                panel.getChildren().add(value);
            }
        }
    }

    public boolean processesEvent(int eventType)
    {
    	return (eventType & TaskLauncherTreeNodeEvent.TYPE_CLICKED) > 0;
    }    
}
