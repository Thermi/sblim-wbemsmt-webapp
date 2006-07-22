// 
//  popupMenue.js
// 
// (C) Copyright IBM Corp. 2005
// 
// THIS FILE IS PROVIDED UNDER THE TERMS OF THE COMMON PUBLIC LICENSE
// ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
// CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
// 
// You can obtain a current copy of the Common Public License from
// http://www.opensource.org/licenses/cpl1.0.php
// 
// Author:     Michael Bauschert
// 
// Contributors:
// 
// Javascript-File for the popupMenue
// 


if (document.all)
{
	//ie
	document.oncontextmenu = new Function("return false;");
}
else
{
	document.oncontextmenu = new Function("return false;");
}

document.onmouseup = saveMouseData;

var x = 0;
var y = 0;
var rightPressed = false;
var objectForPopup = "";


function saveMouseData(myEvent)
{
	if (!myEvent)
	{
		myEvent = window.event;
	}
	
	//alert(myEvent);
	//alert(myEvent.x);
	//alert(myEvent.y);
	
	rightPressed = false;
	
	if (myEvent.which && myEvent.which == 3)
	{
		rightPressed = true;
	}
	
	if (myEvent.button && myEvent.button == 2)
	{
		rightPressed = true;
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
	
	
	if (rightPressed)
	{
		with (document.getElementById(objectForPopup))
		{
			style.visibility="visible";
			style.left=x-2;
			style.top=y-2;
		}
		return false;
	}
	else
	{
		return true;
	}
}

function popup(objectName)
{
	hide(objectForPopup);
	objectForPopup = objectName;
}

function hidePopup(clickEvent)
{
	hide(objectForPopup);
}

function hide(objectName)
{
	if (document.getElementById(objectName))
	{
		with (document.getElementById(objectName))
		{
			style.visibility="hidden";
		}
	}
	return false;
}
