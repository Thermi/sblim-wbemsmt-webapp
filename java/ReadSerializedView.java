import java.io.FileInputStream;
import java.io.ObjectInputStream;
 /** 
 * ReadSerializedView.java
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
 * Description: Read the serialized view object. Can be used to use other object stream with at least to objects 
 * 
 */

public class ReadSerializedView {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		if (args.length != 1)
		{
			System.err.println("Usage: java -cp . ReadSerializedView filenameWithStoredSerialzedView");
			return;
		}
		
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]));

		Object o1 = in.readObject();
		System.out.println("Object #1 " + o1);
		
		Object o2 = in.readObject();
		System.out.println("Object #2 " + o2);

	}

}
