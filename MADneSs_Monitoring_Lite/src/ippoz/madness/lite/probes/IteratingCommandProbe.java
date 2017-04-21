/**
 * 
 */
package ippoz.madness.lite.probes;

import ippoz.madness.lite.support.AppLogger;

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
		if(cRunner.canRun()){
			commandThread = new Thread(cRunner);
			commandThread.start();
		} else AppLogger.logError(getClass(), "ShellCommandError", command + " - cannot be executed. Unable to instantiate probe " + probeLayer);
	}
	
	@Override
	public boolean canRun(){
		return commandThread != null;
	}

	protected abstract boolean isHeader(String line);

	@Override
	public void shutdownProbe() {
		cRunner.interruptCommand();
		super.shutdownProbe();
		if(commandThread != null && !commandThread.isInterrupted())
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

		public boolean canRun() {
			String script = command + " " + commandArgs;
			try {
				process = Runtime.getRuntime().exec(script);
		        process.destroy();
		        return true;
			} catch(Exception ex){
				return false;
			}
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
