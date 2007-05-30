var clientX, clientY;
var gotoWait = false;
var gotoAction = false;
var gotoInterval = false;
var waitMessage = "";
var intervalIcon;
var intervalValue;
var intervalId;

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

  if (gotoInterval)
  {
    gotoInterval = false;
  	toggleToolbox(intervalIcon,intervalValue,intervalId);
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

function toggleToolbox(toolboxIcon, value, id)
{
		intervalIcon = toolboxIcon;
		intervalValue = value;
		intervalId = id;

		var dlg = document.getElementById("updateIntervalToolbox");
		//Input Suffix is defined in BasePanel as Constant SUFFIX_INPUT
		var input = document.getElementById("updateIntervalForm:updateIntervalInput");
		var hidden = document.getElementById("updateIntervalForm:updateIntervalHidden");
		
		if (dlg.style.visibility == "hidden")
		{
				x=-1;
				y=-1;

				//ie doesn't care about the x/y property				
				if (toolboxIcon.x)
				{
				   x = toolboxIcon.x;
				   y = toolboxIcon.y;
				}
				else
				{
					if (clientX && clientY)
					{
					   x = clientX;
					   y = clientY;
					}
					else
					{
						gotoInterval = true;
					}
				}
			
				if (x != -1)
				{
					dlg.style.position = "absolute";
					dlg.style.left = x + "px";
					dlg.style.top = (y + 15) + "px";
					dlg.style.visibility = "visible";
	
					hidden.value = id;
					
					input.value = value;
					input.select();
					input.focus();
				}
		}
		else
		{
			dlg.style.visibility = "hidden";
		}
};

document.onclick = saveXY;
