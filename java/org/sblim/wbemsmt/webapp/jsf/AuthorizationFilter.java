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
 * @author: Marius Kreis <mail@nulldevice.org>
 *
 * Contributors:
 *
 */

package org.sblim.wbemsmt.webapp.jsf;

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

import org.sblim.wbemsmt.tasklauncher.login.LoginCheck;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants;
import org.sblim.wbemsmt.tools.beans.BeanNameConstants.BeanNameConstant;
import org.sblim.wbemsmt.tools.runtime.RuntimeUtil;

/**
 * ServletFilter to check if Http Requests are authorized.
 * The Authorization is done against a SessionBean which can be accessed by JSF aswell.
 * @author Marius Kreis
 */
public class AuthorizationFilter implements Filter
{
    public static final String LOGIN_VIEW_DEFAULT = "/login.jsf";
    
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
            
            String requestURI = httpRequest.getRequestURI();
            String runtimeMode =(String) session.getAttribute(RuntimeUtil.RUNTIME_MODE);
            
            if(requestURI.equals(httpRequest.getContextPath() + "/loginFile.jsf"))
            {
            	cleanupSession(session);
            	runtimeMode = null;
            	switchRuntimeMode(session,runtimeMode, RuntimeUtil.MODE_SINGLE);
            	chain.doFilter(request, response);
            }
            ///AuthorizationFilter.MODE_MULTI.equals(runtimeMode) || 
            else if(requestURI.equals(httpRequest.getContextPath() + "/loginMulti.jsf"))
            {
            	cleanupSession(session);
            	runtimeMode = null;
            	switchRuntimeMode(session,runtimeMode, RuntimeUtil.MODE_MULTI);
            	chain.doFilter(request, response);
            }
            else if(requestURI.equals(httpRequest.getContextPath() + "/welcome.jsf"))
            {
            	chain.doFilter(request, response);
            }
            else if(!requestURI.equals(httpRequest.getContextPath() + this.loginView))
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
            			// access denied, loginBean was not found in session
                    	switchRuntimeMode(session,runtimeMode, RuntimeUtil.MODE_SINGLE);
            			httpResponse.sendRedirect(httpRequest.getContextPath() + this.loginView);
                	}
                }
                else
                {
                		System.out.println("no bean found");
                		// access denied, loginBean was not found in session
                		switchRuntimeMode(session,runtimeMode, RuntimeUtil.MODE_SINGLE);
                		httpResponse.sendRedirect(httpRequest.getContextPath() + this.loginView);
                }
            }
            else //Login for Single Host
            {
                switchRuntimeMode(session,runtimeMode, RuntimeUtil.MODE_SINGLE);
                chain.doFilter(request, response);
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
