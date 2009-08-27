 /** 
  * LoginFileGenerator.java
  *
  * © Copyright IBM Corp.  2009,2005
  *
  * THIS FILE IS PROVIDED UNDER THE TERMS OF THE ECLIPSE PUBLIC LICENSE
  * ("AGREEMENT"). ANY USE, REPRODUCTION OR DISTRIBUTION OF THIS FILE
  * CONSTITUTES RECIPIENTS ACCEPTANCE OF THE AGREEMENT.
  *
  * You can obtain a current copy of the Eclipse Public License from
  * http://www.opensource.org/licenses/eclipse-1.0.php
  *
  * @author: Michael Bauschert <Michael.Bauschert@de.ibm.com>
  *
  * Contributors: 
  * 
  * Description: Generate File for Login directly without the login-screen for development
  * 
  */
package org.sblim.wbemsmt.webapp.jsf;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import org.sblim.wbemsmt.tasklauncher.TaskLauncherConfig;

public class LoginFileGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	    if (args.length != 6)
	    {
	        System.err.println("Usage LoginFileGenerator host port protocol user password file");
	    }
	    
	    try {
			   TaskLauncherConfig.CimomData data = new TaskLauncherConfig.CimomData(args[0],Integer.parseInt(args[1]),args[2], args[3]);
			
		       String filename = args[5];
		       
		       System.out.println("Writing to " + filename);
		       System.out.println(data.getInfo());
		       
            XMLEncoder e = new XMLEncoder(
                       new BufferedOutputStream(
                           new FileOutputStream(filename)));
		       e.writeObject(data);
		       e.writeObject(args[4]);
		       e.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
