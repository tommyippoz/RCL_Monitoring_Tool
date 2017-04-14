/**
 * 
 */
package ippoz.madness.lite.probes;

import ippoz.madness.lite.support.AppLogger;
import ippoz.madness.lite.support.AppUtility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * @author root
 *
 */
public abstract class IteratingCommandProbe extends CycleProbe {
	
	private Thread commandThread; 
	private CommandRunner cRunner;
	protected String header;
	protected volatile String lastReaded;

	public IteratingCommandProbe(String command, String commandArgs, ProbeType probeLayer, String probeName, LinkedList<Indicator> indicators) {
		super(probeLayer, probeName, indicators);
		cRunner = new CommandRunner(command, commandArgs);
		commandThread = new Thread(cRunner);
		commandThread.start();
	}

	protected abstract boolean isHeader(String line);

	@Override
	public void shutdownProbe() {
		cRunner.interruptCommand();
		super.shutdownProbe();
		if(!commandThread.isInterrupted())
			commandThread.interrupt();
	}

	private class CommandRunner implements Runnable {

		private String command;
		private String commandArgs;
		private Process process;
		private boolean halt;
		
		public CommandRunner(String command, String commandArgs) {
			this.command = command;
			this.commandArgs = commandArgs;
			halt = false;
		}

		@Override
		public void run() {
			BufferedReader reader = null;
			String script = command + " " + commandArgs;
			String readed;
			try {
				process = Runtime.getRuntime().exec(script);
				reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		        while (!halt && ((readed = reader.readLine()) != null)) {
		            if(header == null && isHeader(readed)) {
		            	header = readed;
		            } else {
		            	lastReaded = readed;
		            }
		        }
		        reader.close();	
		        process.destroy();
			} catch(Exception ex){
				AppLogger.logException(getClass(), ex, "");
			}
		}
		
		public void interruptCommand(){
			halt = true;
		}
		
	}

}
