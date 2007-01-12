var clientX, clientY;
var gotoWait = false;
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

function saveXY (clickEvent) {

  if (!clickEvent)
    clickEvent = window.event;


  clientX  = clickEvent.clientX;
  clientY  = clickEvent.clientY;
  
  if (gotoWait)
  	showWait();
}

document.onclick = saveXY;
