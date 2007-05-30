//var debugWindow = window.open("debug.html", "Zweitfenster", "width=300,height=400,left=100,top=200,resizable=yes");
function addText(text)
{
	//alert(text)
	//if (debugWindow.document.forms.debugform)
	{
		//debugWindow.document.forms.debugform.debugOut.value = debugWindow.document.forms.debugform.debugOut.value + "\n" + text;
	}
}


dojo.provide("org.sblim.wbemsmt.ajax");

dojo.require("dojo.dom.*");

org.sblim.wbemsmt.ajax.AjaxPanelController = function(formId)
{
    this.form = dojo.byId(formId);
    this.windowTimeout = new Array();
    
    if(typeof this.form == "undefined" || this.form.tagName.toLowerCase() != "form")
    {
        alert("AjaxPanelController: Form with id:" + formId + " not found!");
        return;
    }
}

// starting/stopping point of automatically partial page refresh

org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.startPeriodicalUpdate = function(refreshTimeout, zoneId)
{
	addText("startPeriodicalUpdate");
	if (refreshTimeout > 0)
	{
	    var content = new Array;
	    content["org.sblim.wbemsmt.ajax.AjaxPanelController.triggeredComponents"] = zoneId;
	    this.doAjaxSubmit(content, refreshTimeout, zoneId);
	}
};

org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.stopPeriodicalUpdate = function(zoneId)
{
	addText("stopPeriodicalUpdate");
	
	if (this.windowTimeout[zoneId])
	{

    	addText("Remove Timeout: " + zoneId);
	    window.clearTimeout(this.windowTimeout[zoneId]);
	    delete this.windowTimeout[zoneId];
	}
};

org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.togglePeriodicalUpdate = function(refreshTimeout, zoneId)
{
	if (this.windowTimeout[zoneId])
	{
		this.stopPeriodicalUpdate(zoneId);
	}
	else
	{
		this.startPeriodicalUpdate(refreshTimeout, zoneId);
	}
};

//Callback Method which handles the AJAX Response

org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.handleCallback = function(type, data, evt)
{
    if(type == "load")
    {
    
    	addText("handleCallBack");
    
	    var componentUpdates = data.getElementsByTagName("component");
	    var componentUpdate = null;
	    var domElement = null;
		for (var i = 0; i < componentUpdates.length; i++)
		{
			componentUpdate = componentUpdates[i];
			addText("update component " + componentUpdate.getAttribute("id") + " HMTL " + componentUpdate.firstChild.data);
			domElement = dojo.byId(componentUpdate.getAttribute("id"));
			domElement.innerHTML = componentUpdate.firstChild.data;
		}
		var stateElem = data.getElementsByTagName("state")[0];
	    
		if(stateElem)
		{
			var stateUpdate = dojo.dom.firstElement(stateElem,'INPUT');
		
			if(stateUpdate)
			{
				 var stateUpdateId = stateUpdate.getAttribute('id');

				 if(stateUpdateId =='javax.faces.ViewState')
				 {
					var formArray = document.forms;
			
					for(var i=0; i<formArray.length; i++)
					{
					  var form = formArray[i];
					  var domElement = form.elements['javax.faces.ViewState'];
					  if(domElement)
						domElement.value = stateUpdate.getAttribute('value');
					}
				}
				else if (stateUpdateId !='jsf_tree')
					alert("server didn't return appropriate element for state-update. returned element-id: "+
						  stateUpdate.getAttribute('id')+", value : "+stateUpdate.getAttribute('value'));
			}
		}
    }
    else
    {
        //alert("an Error occured during the ajax-request " + data.message);
    }
}

org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.doAjaxSubmit = function(content, refreshTimeout, zoneId)
{   
	addText("doAjaxSubmit");

	var ajaxCtrl = this;  
    var requestUri = "";
    var formAction = this.form.attributes["action"];
    if(formAction == null)
    {
        requestUri = location.href;
    }
    else
    {
        requestUri = formAction.nodeValue;
    }

    content["org.sblim.wbemsmt.ajax.AjaxPanelController.ajaxRequest"]="true";

    dojo.io.bind({
        url		: requestUri,
        method	: "post",
        useCache: false,
        content	: content,
        handle	: this.handleCallback,
        mimetype: "text/xml",
        transport: "XMLHTTPTransport",
        formNode: this.form
    });

    if(refreshTimeout)
    {
    	addText("New Timeout: " + zoneId + " timeout " + refreshTimeout);
        this.windowTimeout[zoneId] = window.setTimeout(function() {
            ajaxCtrl.startPeriodicalUpdate(refreshTimeout, zoneId);
        }, refreshTimeout)
    }

    return false;
};

