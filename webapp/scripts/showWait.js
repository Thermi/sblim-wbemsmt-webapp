var clientX, clientY;
var gotoWait = false;
var gotoAction = false;
var waitMessage = "";
function showWait()
{

	if (clientX && clientY)
	{
		var waitMsg = document.getElementById("waitMsg");
		
		if (waitMessage.indexOf("...") == -1)
		{
			waitMessage = waitMessage + "...";
		}
		
		waitMsg.innerHTML = waitMessage;

		var waitDlg = document.getElementById("waitDlg");
		waitDlg.style.position = "absolute";
		waitDlg.style.top = clientY + 20 + "px";
		waitDlg.style.left = clientX + 30 + "px";
		waitDlg.style.visibility = "visible";
		
		
	}
	else
	{
		gotoWait = true;
	}	
	
}

function hideWait()
{
	var waitDlg = document.getElementById("waitDlg");
	waitDlg.style.visibility = "hidden";
	stop();
}

function saveXY (clickEvent) {

  

  if (!clickEvent)
    clickEvent = window.event;


  clientX  = clickEvent.clientX;
  clientY  = clickEvent.clientY;
  
  if (gotoWait)
  {
  	  gotoWait = false;
	  showWait();
  }
  	

  if (gotoAction)
  {
    gotoAction = false;
  	showActionMenue();
  }

}

var actionMenueVisible = false;

function showActionMenue()
{
	if (actionMenueVisible)
	{
		hideActionMenue();
		return;
	}

	var actionButton = document.getElementById("breadcrumb:actionIcon");
	var actionMenue = document.getElementById("breadcrumb:popup");

	x=-1;
	y=-1;
	
	if (actionButton.x)
	{
	   x = actionButton.x;
	   y = actionButton.y + actionButton.height + 10;
	}
	else
	{
		if (clientX && clientY)
		{
		   x = clientX;
		   y = clientY + 10;
		}
		else
		{
			gotoAction = true;
		}
	}

	if (x != -1)
	{
		actionMenue.style.position = "absolute";
		actionMenue.style.top = y;
		actionMenue.style.left = x;
		actionMenue.style.visibility = "visible";
		actionMenueVisible = true;
	}
}

function hideActionMenue()
{
	var actionMenue = document.getElementById("breadcrumb:popup");
	actionMenue.style.visibility = "hidden";
	actionMenueVisible = false;
}

document.onclick = saveXY;
