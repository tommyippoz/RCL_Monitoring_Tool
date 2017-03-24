/**
 * 
 */
package ippoz.monitor.probes.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ippoz.madness.lite.probes.mbean.BeanManager;
import ippoz.madness.lite.support.ProbeLogger;

/**
 * @author Tommy
 *
 */
public class ClientBeanProbe {
	
	private static String PREF_FILENAME = "beanFetcher.preferences";
	private static String BEAN_PREF_FILENAME;
	private static String DEST_IP_ADDRESS;
	private static int DEST_PORT_NUMBER;
	private static int OBSERVATION_MS_DELAY;
	private static int CONN_ATTEMPTS_NUMBER;
	
	public static void main(String[] args) {
		long startTime;
		int attCount = 1;
		Socket socket = null;
		ObjectOutputStream outStream = null;
		BeanManager bManager = null;	 
		try {
			loadPreferences(args);
			bManager = new BeanManager();
			bManager.writeBeansToFile("AvailableBeans.csv");
			bManager.loadBeanPreferences(BEAN_PREF_FILENAME);
			while(socket == null && attCount <= CONN_ATTEMPTS_NUMBER) {
				ProbeLogger.logInfo(ClientBeanProbe.class, "Attempt " + attCount + ": Waiting for the Socket at " + DEST_IP_ADDRESS + ":" + DEST_PORT_NUMBER);
				try {
					socket = new Socket(DEST_IP_ADDRESS, DEST_PORT_NUMBER);
					ProbeLogger.logInfo(ClientBeanProbe.class, "Server found at attempt " + attCount);
				} catch (IOException e) {
					e.printStackTrace();
					Thread.sleep(1000);
				}
				attCount++;
			}
			if(socket != null) {
				outStream = new ObjectOutputStream(socket.getOutputStream());
				startTime = System.currentTimeMillis();
				while(socket.isConnected()){
					startTime = System.currentTimeMillis();
				    outStream.writeObject(bManager.getObservation().toString());
				    ProbeLogger.logInfo(ClientBeanProbe.class, "Observation fetched in " + (System.currentTimeMillis() - startTime));
				    startTime = startTime + OBSERVATION_MS_DELAY;
				    Thread.sleep(startTime - System.currentTimeMillis());
				}
			} else ProbeLogger.logError(ClientBeanProbe.class, "SocketError", "Server not found");
		} catch (Exception ex) {
			ProbeLogger.logException(ClientBeanProbe.class, ex, "Error");
		} finally {
			try {
				if(socket != null && !socket.isClosed())
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void loadPreferences(String[] args) throws IOException{
		String readed, tag, value;
		BufferedReader reader;
		File prefFile;
		String workingDir = ClientBeanProbe.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		if(workingDir.endsWith(".jar"))
			workingDir = workingDir.substring(0, workingDir.lastIndexOf("/") + 1);
		else workingDir = "";
		if(args.length == 1 && new File(args[0]).exists())
			PREF_FILENAME = args[0];
		prefFile = new File(workingDir + PREF_FILENAME);
		if(prefFile.exists()){
			reader = new BufferedReader(new FileReader(prefFile));
			while(reader.ready()){
				readed = reader.readLine();
				if(readed.length() > 0) {
					if(readed.contains("=")){
						tag = readed.split("=")[0];
						value = readed.split("=")[1];
						if(tag.equals("bean_pref_filename"))
								BEAN_PREF_FILENAME = value;
						else if(tag.equals("dest_ip_address"))
							DEST_IP_ADDRESS = value;
						else if(tag.equals("dest_port_number"))
							DEST_PORT_NUMBER = Integer.parseInt(value);
						else if(tag.equals("observation_ms_delay"))
							OBSERVATION_MS_DELAY = Integer.parseInt(value);
						else if(tag.equals("conn_attempts_number"))
							CONN_ATTEMPTS_NUMBER = Integer.parseInt(value);
						else {
							ProbeLogger.logInfo(ClientBeanProbe.class, "Parsing process not concluded: " + readed);
						}
					}
				}
			}
			reader.close();
		} else {
			ProbeLogger.logInfo(ClientBeanProbe.class, "Unexisting preference file: " + workingDir + PREF_FILENAME);
		}
	}


}
