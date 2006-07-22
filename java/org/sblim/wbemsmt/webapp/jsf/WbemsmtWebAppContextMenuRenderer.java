 /** 
  * WbemsmtWebAppContextMenuRenderer.java
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
  * Description: Renderer for the ContextMenue of the wbemsmt task launcher's tree
  * 
  */


package org.sblim.wbemsmt.webapp.jsf;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.custom.popup.HtmlPopup;
import org.apache.myfaces.custom.popup.HtmlPopupRenderer;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;
import org.apache.myfaces.shared_impl.renderkit.RendererUtils;
import org.apache.myfaces.shared_impl.renderkit.html.HTML;
import org.apache.myfaces.shared_impl.renderkit.html.HtmlRenderer;
import org.apache.myfaces.shared_impl.renderkit.html.util.JavascriptUtils;


public class WbemsmtWebAppContextMenuRenderer extends HtmlRenderer {

    public static final String RENDERER_TYPE = "org.apache.myfaces.Popup";
    //private static final Log log = LogFactory.getLog(HtmlListRenderer.class);

    public boolean getRendersChildren()
{
    return true;
}

public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException
{
}

public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException
{

}


public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException
{
    RendererUtils.checkParamValidity(facesContext, uiComponent, HtmlPopup.class);

    HtmlPopup popup = (HtmlPopup) uiComponent;

    UIComponent popupFacet = popup.getPopup();

    String popupId = writePopupScript(
            facesContext, popup.getClientId(facesContext),
            popup.getDisplayAtDistanceX(),popup.getDisplayAtDistanceY(), uiComponent);

    writeMouseOverAttribs(popupId, uiComponent.getChildren());

    RendererUtils.renderChildren(facesContext, uiComponent);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement(HTML.DIV_ELEM, popup);
//    writer.writeAttribute(HTML.STYLE_ATTR,(popup.getStyle()!=null?(popup.getStyle()+
//            (popup.getStyle().trim().endsWith(";")?"":";")):"")+
//            "position:absolute;display:none;",null);
    if(popup.getStyleClass()!=null)
    {
        writer.writeAttribute(HTML.CLASS_ATTR,popup.getStyleClass(),null);
    }
    writer.writeAttribute(HTML.ID_ATTR, popup.getClientId(facesContext),null);
    //writer.writeAttribute(HTML.ONMOUSEOVER_ATTR, new String(popupId+".redisplay();"),null);

//    Boolean closeExitPopup = popup.getClosePopupOnExitingPopup();

//    if(closeExitPopup==null || closeExitPopup.booleanValue())
//        writer.writeAttribute(HTML.ONMOUSEOUT_ATTR, "addText('div-MouseOut');" + popupId + ".hide();",null);

    RendererUtils.renderChild(facesContext, popupFacet);
    writer.endElement(HTML.DIV_ELEM);
    
}

private void writeMouseOverAttribs(String popupId, List children)
{
	String template =   "this.oncontextmenu = new Function(\"{0}.display();return false;\");" +
						"this.onmousedown = saveMouseData;";
//						"" +
//						"function popupId_saveMouseEvent(mouseEvent) '{'" +
//						"   if (!mouseEvent) '{'" +
//						"     mouseEvent = window.event;" +
//						"   }" +
//						"   {0}_mouseEvent = mouseEvent;" +
//						"   addText(\"Save Event as {0}_mouseEvent\");" +
//						"" +
//						"}";

	
    for (int i = 0; i < children.size(); i++)
    {
        UIComponent uiComponent = (UIComponent) children.get(i);

        String msg = MessageFormat.format(template,new Object[]{popupId,popupId,popupId,popupId});
        
        callMethod(uiComponent,"onmouseover", msg);

        writeMouseOverAttribs(popupId, uiComponent.getChildren());
    }
}

private String writePopupScript(FacesContext context, String clientId,
                                Integer displayAtDistanceX, Integer displayAtDistanceY, UIComponent uiComponent)
    throws IOException
{
    AddResourceFactory.getInstance(context).addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, HtmlPopupRenderer.class, "JSPopupContexteMenue.js");

    String popupId = JavascriptUtils.getValidJavascriptName(clientId+"Popup",false);

    ResponseWriter writer = context.getResponseWriter();
    writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
    writer.writeAttribute(HTML.SCRIPT_TYPE_ATTR,HTML.SCRIPT_TYPE_TEXT_JAVASCRIPT,null);
    writer.writeText("var "+popupId+"=new orgApacheMyfacesPopup('"+clientId+"',"+
            (displayAtDistanceX==null?-5:displayAtDistanceX.intValue())+","+
            (displayAtDistanceY==null?-5:displayAtDistanceY.intValue())+");",null);
    writer.endElement(HTML.SCRIPT_ELEM);

    return popupId;
}


private void callMethod(UIComponent uiComponent, String propName, String value)
{
    Object oldValue = uiComponent.getAttributes().get(propName);

    String oldValueStr = "";

    String genCommentary = "/* generated code */";

    if(oldValue != null)
    {
        oldValueStr = oldValue.toString().trim();

        int genCommentaryIndex;

        //check if generated code has already been added...
        if((genCommentaryIndex=oldValueStr.indexOf(genCommentary))!=-1)
        {
            oldValueStr = oldValueStr.substring(0,genCommentaryIndex);
        }

        if(oldValueStr.length()>0 && !oldValueStr.endsWith(";"))
            oldValueStr +=";";
    }

    value = oldValueStr+genCommentary+value;

    uiComponent.getAttributes().put(propName, value);
}

}
