import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Locale;
 /** 
 * SerializationTest.java
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
 * Description: Reproduces a GCJ-Bug which occurs while deserializing objects
 * 
 */

public class SerializationTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1)
		{
			System.err.println("Usage: java -cp . SerializationTest filename");
			return;
		}
		
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(args[0]));

		
		Object[] fiveElementArray = new Object[5];
		fiveElementArray[0] = new Object[7];
		fiveElementArray[1] = Locale.UK;
		fiveElementArray[4] = new Long(0);
		
		Object[] in1 = new Object[3];
		in1[0] = fiveElementArray;
		out.writeObject(in1);

		HashSet hashSet = new HashSet();
		hashSet.add("Test1");
		hashSet.add(hashSet);
		out.writeObject(hashSet);
		 
		out.flush();
		out.close();
		
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(args[0]));

		Object o1 = in.readObject();
		System.out.println("Object #1 " + o1);
		
		Object o2 = in.readObject();
		System.out.println("Object #2 " + o2);

	}
}
