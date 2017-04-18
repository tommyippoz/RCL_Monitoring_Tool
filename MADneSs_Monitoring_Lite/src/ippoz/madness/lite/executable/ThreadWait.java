/**
 * 
 */
package ippoz.madness.lite.executable;


/**
 * @author Tommy
 *
 */
public class ThreadWait {
	
	public static void main(String[] args) {
		try {
			System.out.println("Start");
			Thread.sleep(5000);
			System.out.println("End");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
