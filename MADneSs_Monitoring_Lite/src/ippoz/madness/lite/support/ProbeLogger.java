/**
 * 
 */
package ippoz.madness.lite.support;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author Tommy
 *
 */
public class ProbeLogger {
	
	private static Logger logger;	
	private static boolean console;
	
	private ProbeLogger(String logName, String logFolder, boolean console) { 
	    FileHandler fh;  
	    try {  
	    	ProbeLogger.console = console;
	    	logger = Logger.getLogger("FantaLogger"); 
	    	if(logFolder != null) {
		        fh = new FileHandler(logFolder + "//" + logName + ".log");  
		        fh.setFormatter(new SimpleFormatter());  
		        logger.addHandler(fh); 
	    	}
	    	logger.setUseParentHandlers(false);
	    } catch (SecurityException e) {  
	    	System.err.println("[Logger] Unable to create logger: permission denied");
	        e.printStackTrace();  
	    } catch (IOException e) {
	    	System.err.println("[Logger] Unable to create logger: unavailable file location");
			e.printStackTrace();
		}  
	}
	
	public static void logException(Class<?> source, Exception ex, String message){
		log("Exception", ex.getClass().getName() + "@" + source.getName(), ex.getMessage(), Level.SEVERE);
		//if(console)
			//ex.printStackTrace();
	}
	
	public static void logError(Class<?> source, String error, String message){
		log("Error", error + "@" + source.getName(), message, Level.SEVERE);
	}
	
	public static void logInfo(Class<?> source, String message){
		log("Info", source.getName(), message, Level.INFO);
	}
	
	private static void log(String tag, String location, String message, Level level){
		String aggMessage = "[" + tag + "][" + location + "] " + message;
		if(logger == null)
			new ProbeLogger("ProbeLogger", null, true);
		logger.log(level, aggMessage);
		if(console)
			System.out.println(aggMessage);
	}

}
