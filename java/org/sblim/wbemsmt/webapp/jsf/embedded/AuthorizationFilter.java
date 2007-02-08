/**
 *  AuthorizationFilter.java
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
 * @author: Michael Bauschert
 *
 * Contributors:
 *
 */

package org.sblim.wbemsmt.webapp.jsf.embedded;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.sblim.wbemsmt.bl.ErrCodes;
import org.sblim.wbemsmt.bl.adapter.Message;
import org.sblim.wbemsmt.tasklauncher.login.LoginCheck;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants.BeanNameConstant;
import org.sblim.wbemsmt.tools.resources.ResourceBundleManager;
import org.sblim.wbemsmt.tools.resources.WbemSmtResourceBundle;
import org.sblim.wbemsmt.tools.runtime.RuntimeUtil;

/**
 * ServletFilter to check if Http Requests are authorized.
 * The Authorization is done against a SessionBean which can be accessed by JSF aswell.
 * @author Michael Bauschert
 */
public class AuthorizationFilter implements Filter
{
    public static final String LOGIN_VIEW_DEFAULT = "/login.jsf";
    public static final String TIMEOUT_VIEW_DEFAULT = "/timeout.jsf";
    
    private String loginView,
                   loginCheckBean,
                   loginCheckClass;

    public AuthorizationFilter()
    {
        super();
    }
    
    public void init(FilterConfig filterConfig)
    {        
        // setting parameters
        loginView = filterConfig.getInitParameter("LoginView");
        if(loginView == null) loginView = LOGIN_VIEW_DEFAULT;

        loginCheckBean = filterConfig.getInitParameter("LoginCheckBean");
        if(loginCheckBean == null) throw new IllegalArgumentException("Filter parameter LoginCheckBean not set");
        
        loginCheckClass = filterConfig.getInitParameter("LoginCheckClass");
        if(loginCheckClass == null) throw new IllegalArgumentException("Filter parameter LoginCheckClass not set");
    
        RuntimeUtil.getInstance().setPlType(RuntimeUtil.PL_TYPE_JSF);
        
    }
    
    /**
     * Filter Requests by checking Authorization.
     * A request is authorized if its session contains a bean with the LoginCheck interface and this beans isLoggedIn method returns true.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws ServletException, IOException
    {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession session = httpRequest.getSession();
     
            WbemSmtResourceBundle bundle = ResourceBundleManager.getResourceBundle(new String[]{"messages"},request.getLocale());
            Message message = Message.create(ErrCodes.MSG_TIME_OUT, Message.ERROR, bundle, "timeout");
            
            String requestURI = httpRequest.getRequestURI();
            String runtimeMode =(String) session.getAttribute(RuntimeUtil.RUNTIME_MODE);
            
            if(requestURI.equals(httpRequest.getContextPath() + "/embeddedLogin.jsf"))
            {
            	cleanupSession(session);
            	runtimeMode = null;
            	switchRuntimeMode(session,runtimeMode, RuntimeUtil.MODE_EMBEDDED);
            	chain.doFilter(request, response);
            }
            else if(requestURI.equals(httpRequest.getContextPath() + "/welcome.jsf"))
            {
            	chain.doFilter(request, response);
            }
            else if(requestURI.equals(httpRequest.getContextPath() + this.loginView))
            {
            	chain.doFilter(request, response);
            }
            else
            {
                if(session.getAttribute(this.loginCheckBean) != null)
                {
                	LoginCheck lc = (LoginCheck) session.getAttribute(this.loginCheckBean);
                    if(lc.isLoggedIn())
                    {
                        // access granted
                        chain.doFilter(request, response);
                    }
                    else
                    {
            			// access denied, user not logged in
                    	System.out.println("access denied, user not logged in");
                    	session.setAttribute("msg",message);
            			httpResponse.sendRedirect(httpRequest.getContextPath() + this.loginView);
                	}
                }
                else
                {
                	// access denied, loginBean was not found in session
                	System.out.println("no loginCheckBean found in session");
                	session.setAttribute("msg",message);
                	httpResponse.sendRedirect(httpRequest.getContextPath() + this.loginView);
                }
            }
    }

	private void cleanupSession(HttpSession session) {
		session.removeAttribute(BeanNameConstants.TASKLAUNCHER_CONTROLLER.getName());
		session.removeAttribute(BeanNameConstants.LOGIN_CHECK.getName());
		session.removeAttribute(BeanNameConstants.MENUE_CONTROLLER.getName());
	}

	private void switchRuntimeMode(HttpSession session, String currentMode, String newMode) {

		//makes it sense to make the switch ?
		if (!newMode.equals(currentMode))
		{
			session.getServletContext().log("Switch Runtime mode from  " + currentMode + " to " + newMode);
			session.setAttribute(RuntimeUtil.RUNTIME_MODE,newMode);
			RuntimeUtil.getInstance().setMode(newMode);
			
			//Removing the Beans from Session ensures that the ManagedBeanFacility creates new objects of the managed beans if needed
			removeIfExisting(session, BeanNameConstants.TASKLAUNCHER_CONTROLLER);
			removeIfExisting(session, BeanNameConstants.LOGIN_CHECK);
			removeIfExisting(session, BeanNameConstants.MENUE_CONTROLLER);
		}

		
	}

	/**
	 * Remove the bean from session
	 * @param session
	 * @param bean
	 */
	private void removeIfExisting(HttpSession session, BeanNameConstant bean) {
		
		if (session.getAttribute(bean.getName()) != null)
		{
			session.removeAttribute(bean.getName());
		}
		
	}

	public void destroy()
    {
    }
    
}
