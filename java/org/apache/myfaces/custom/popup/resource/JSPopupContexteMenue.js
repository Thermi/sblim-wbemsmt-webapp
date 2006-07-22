var orgApacheMyfacesPopupCurrentlyOpenedPopup;
var orgApacheMyfacesPopupFrameUnder;

/**
* fix for the div over control bug in ie
*/
function orgApacheMyfacesPopupfixIE() {
    if(document.all) {
    	if(orgApacheMyfacesPopupCurrentlyOpenedPopup == null) return false;
    	var iframe = document.getElementById(orgApacheMyfacesPopupCurrentlyOpenedPopup.id+"_IFRAME");


		if(iframe == null) {
			orgApacheMyfacesPopupFrameUnder = document.createElement("<iframe id='"+orgApacheMyfacesPopupCurrentlyOpenedPopup.id+"_IFRAME' style='visibility:hidden; position: absolute; top:0px;left:0px;'/>");
	   		document.body.insertBefore(orgApacheMyfacesPopupFrameUnder);
   		} else {
   			orgApacheMyfacesPopupFrameUnder = iframe;
   		}

		var popup  = orgApacheMyfacesPopupCurrentlyOpenedPopup;
   		iframe = orgApacheMyfacesPopupFrameUnder;

   		if(popup != null &&
   			(popup.style.display == "block")) {

			popup.style.zIndex	= 99;
   			iframe.style.zIndex = popup.style.zIndex - 1;
   			iframe.style.width 	= popup.offsetWidth;
	    	iframe.style.height = popup.offsetHeight;
	    	iframe.style.top 	= popup.style.top;
    		iframe.style.left 	= popup.style.left;
    		
    		iframe.style.marginTop 		= popup.style.marginTop;
    		iframe.style.marginLeft 	= popup.style.marginLeft;
    		iframe.style.marginRight 	= popup.style.marginRight;
    		iframe.style.marginBottem 	= popup.style.marginBottom;
    		
			iframe.style.display = "block";
			iframe.style.visibility = "visible"; /*we have to set an explicit visible otherwise it wont work*/

   		} else {
   			iframe.style.display = "none";
   		}
    }
    return false;
}


function orgApacheMyfacesPopup(popupId,displayAtDistanceX,displayAtDistanceY)
{
    this.popupId = popupId;
    this.displayAtDistanceX=displayAtDistanceX;
    this.displayAtDistanceY=displayAtDistanceY;
    this.display = orgApacheMyfacesPopupDisplay;
    this.hide = orgApacheMyfacesPopupHide;
    this.redisplay=orgApacheMyfacesPopupRedisplay;
}
function orgApacheMyfacesPopupHide()
{

}

function orgApacheMyfacesPopupRedisplay()
{

}

function orgApacheMyfacesPopupDisplay()
{
    var popupElem = document.getElementById(this.popupId);

    popupElem.style.visibility="visible";
    popupElem.style.left=x;
    popupElem.style.top=y;
    orgApacheMyfacesPopupCurrentlyOpenedPopup = popupElem;
    document.onclick = hidePopup2;
    //orgApacheMyfacesPopupfixIE();
}

function orgApacheMyfacesPopupGetScrollingX() {
    if (self.pageXOffset) {
        return self.pageXOffset;
    } else if (document.documentElement && document.documentElement.scrollLeft) {
        return document.documentElement.scrollLeft;
    } else if (document.body) {
        return document.body.scrollLeft;
    } else {
        return 0;
    }
}

function orgApacheMyfacesPopupGetScrollingY() {
    if (self.pageYOffset) {
        return self.pageYOffset;
    } else if (document.documentElement && document.documentElement.scrollTop) {
        return document.documentElement.scrollTop;
    } else if (document.body) {
        return document.body.scrollTop;
    } else {
        return 0;
    }
}

var x = 0;
var y = 0;
var poppedUp = false;

document.oncontextmenu = new Function("addText('poppedUp' + poppedUp); return (!poppedUp)");

function saveMouseData(myEvent)
{
	addText("saveMouseData");
	
	poppedUp = true;
	
	if (!myEvent)
	{
		myEvent = window.event;
	}
	
	if (myEvent.x)
	{
		//IE
		x = myEvent.x;
		y = myEvent.y;
	}
	else
	{
		//Netscape
		x = myEvent.pageX;
		y = myEvent.pageY;
	}

	addText("saveMouseData before X:" + x + " Y: " + y );

    x-=5;
    y-=5;

	addText("saveMouseData after X:" + x + " Y: " + y );
}

function hidePopup(clickEvent)
{
	hidePopup2();
}

function hidePopup2()
{
	addText("hidePopup " + orgApacheMyfacesPopupCurrentlyOpenedPopup);
	if (orgApacheMyfacesPopupCurrentlyOpenedPopup)
	{
		with (orgApacheMyfacesPopupCurrentlyOpenedPopup)
		{
			style.visibility="hidden";
		}
		poppedUp = false;
		document.onclick = new Function("return true;");
	}
}
