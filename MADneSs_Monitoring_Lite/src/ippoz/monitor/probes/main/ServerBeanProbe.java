/**
 * 
 */
package ippoz.monitor.probes.main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import net.sf.json.JSONObject;

/**
 * @author Tommy
 *
 */
public class ServerBeanProbe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		ObjectInputStream inStream = null;
		JSONObject jObj = null;
		try {
            serverSocket = new ServerSocket(9888);
	        clientSocket = serverSocket.accept();                       
	        inStream = new ObjectInputStream(clientSocket.getInputStream());
            while ((jObj = (JSONObject) inStream.readObject()) != null) {
                System.out.println(jObj.toString(2));
            } 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
        	try {
				clientSocket.close();
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

	}

}
