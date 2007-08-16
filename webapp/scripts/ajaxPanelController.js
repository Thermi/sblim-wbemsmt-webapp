if (typeof console == 'undefined') {
// Try to be compatible with other browsers
// Only use firebug logging when available
console = new Object;
console.trace = function() {};
console.log = function() {};
console.debug = function() {};
console.info = function() {};
console.warn = function() {};
console.error = function() {};
console.time = function() {};
console.timeEnd = function() {};
console.count = function() {};
console.profile = function() {};
console.profileEnd = function() {};
console.group = function() {};
console.groupEnd = function() {};
console.dir = function() {};
console.dirxml = function() {};
} 

dojo.provide("org.sblim.wbemsmt.ajax");

dojo.require("dojo.dom.*");

//flag to synchronize the requests
var ajaxRequestIsRunning = false;

var stopAllAjaxRequests = false;
	


org.sblim.wbemsmt.ajax.AjaxPanelController = function(formId)
{
    this.form = dojo.byId(formId);
    this.windowTimeout = new Array();
    this.threads = new Array();
    stopAllAjaxRequests = false;
    
    if(typeof this.form == "undefined" || this.form.tagName.toLowerCase() != "form")
    {
        alert("AjaxPanelController: Form with id:" + formId + " not found!");
        return;
    }
    
    this.execute();
}

// starting/stopping point of automatically partial page refresh

org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.startPeriodicalUpdate = function(refreshTimeout, zoneId)
{
	console.log("startPeriodicalUpdate");
	if (refreshTimeout > 0 && stopAllAjaxRequests == false)
	{
		var idx = this.threads.length;
		this.threads[idx] = new Object();
		this.threads[idx]["refreshTimeout"] = refreshTimeout;
		this.threads[idx]["zoneId"] = zoneId;
	}
};

org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.stopPeriodicalUpdate = function(zoneId)
{
	console.log("stopPeriodicalUpdate");
	
	if (this.windowTimeout[zoneId])
	{

    	console.log("Remove Timeout: " + zoneId);
	    window.clearTimeout(this.windowTimeout[zoneId]);
	    delete this.windowTimeout[zoneId];
	}
};

// Execute the first ajax submit of the current threads list
// waits a few milliseconds and execute itself again
org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.execute = function()
{
	if (this.threads.length > 0 && stopAllAjaxRequests == false)
	{
		var zoneId = this.threads[0]["zoneId"];
		var refreshTimeout = this.threads[0]["refreshTimeout"];
		var content = new Array;
	    content["org.sblim.wbemsmt.ajax.AjaxPanelController.triggeredComponents"] = zoneId;
	    
	    //delete the first element
	    this.threads.shift();
	    //submit the request
	    this.doAjaxSubmit(content, refreshTimeout, zoneId);
	}
	
	//call the same method again
	var ajaxCtrl = this;  
	if (stopAllAjaxRequests == false)
	{
		window.setTimeout(function() {ajaxCtrl.execute();}, 200)	
	}
}

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
    
    	console.log("handleCallBack");
    
	    var componentUpdates = data.getElementsByTagName("component");
	    var componentUpdate = null;
	    var domElement = null;
		for (var i = 0; i < componentUpdates.length; i++)
		{
			componentUpdate = componentUpdates[i];
			console.log("update component " + componentUpdate.getAttribute("id") + " HMTL " + componentUpdate.firstChild.data);
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
    ajaxRequestIsRunning = false;
}

org.sblim.wbemsmt.ajax.AjaxPanelController.prototype.doAjaxSubmit = function(content, refreshTimeout, zoneId)
{   
	console.log("doAjaxSubmit");

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

	if (ajaxRequestIsRunning == false)
	{
		
		ajaxRequestIsRunning = true;
	    
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
	}

    if(refreshTimeout)
    {
    	console.log("New Timeout: " + zoneId + " timeout " + refreshTimeout);
        this.windowTimeout[zoneId] = window.setTimeout(function() {
            ajaxCtrl.startPeriodicalUpdate(refreshTimeout, zoneId);
        }, refreshTimeout)
    }

    return false;
};
