 /** 
  * WbemsmtImageLoader.java
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
  * @author: Michael Bauschert <Michael.Bauschert@de.ibm.com>
  *
  * Contributors: 
  * 
  * Description: Servlet for loading images
  * 
  */
package org.sblim.wbemsmt.webapp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Bauschert
 *
 */
public class WbemsmtImageLoader extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8100334849482534323L;

	protected Logger logger = Logger.getLogger("org.sblim.wbemsmt.webapp.servlet");
	
	private static Map contextTypes = new HashMap();
	
	private static final String PARAM_PATH = "path";
	
	static
	{
		contextTypes.put(".jpg","image/jpeg");
		contextTypes.put(".gif","image/gif");
		contextTypes.put(".png","image/png");
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String path = request.getParameter(PARAM_PATH);
		
		if (path == null)
		{
			throw new ServletException("Parameter " + PARAM_PATH + " not set");
		}
		
		logger.info("Loading image "  + path);
		
		setMimeType(path,response);
		
		InputStream in = null;
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();

			in = getClass().getResourceAsStream(path);
			
			if (in == null)
			{
				throw new ServletException("Resource " + path + " was not found");
			}
			
			
			byte[] buffer = new byte[100000];
			while (true) {
			    synchronized (buffer) {
			         int amountRead = in.read(buffer);
			         if (amountRead == -1) {
			            break;
			         }
			         out.write(buffer, 0, amountRead); 
			    }
			 }
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Loading image "  + path + " failed",e);
		}
		finally
		{
			if (in != null) {
		       in.close();
		    }
		    if (out != null) {
		       out.flush();
		       out.close();
		    }			
		}
	}

	private void setMimeType(String path, HttpServletResponse response) throws ServletException {

		int lastIndex = path.lastIndexOf(".");
		if (lastIndex == -1)
		{
			throw new ServletException("Path " + path + " contains no '.'");
		}
		else
		{
			String suffix = path.substring(lastIndex).toLowerCase();
			String contentType = (String) contextTypes.get(suffix);
			if (contentType == null)
			{
				throw new ServletException("Suffix " + suffix + " was not registered");
			}
			else
			{
				response.setContentType(contentType);
			}
		}
		
		
	}
	
	
	
}
