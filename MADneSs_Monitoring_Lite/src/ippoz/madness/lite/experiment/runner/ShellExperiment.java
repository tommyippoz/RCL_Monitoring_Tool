/**
 * 
 */
package ippoz.madness.lite.experiment.runner;

import ippoz.madness.lite.support.AppLogger;
import ippoz.madness.lite.support.AppUtility;

/**
 * @author Tommy
 *
 */
public class ShellExperiment extends ExperimentRunner{

	private String procCommand;
	
	public ShellExperiment(String procCommand){
		super();
		this.procCommand = procCommand;
	}
	
	@Override
	public String toString() {
		return getDetail();
	}
	
	@Override
	public String getDetail() {
		return procCommand;
	}

	@Override
	protected void runExperiment() {
		long startTime = System.currentTimeMillis();
		AppLogger.logInfo(getClass(), AppUtility.runProcess(procCommand, true));
		AppLogger.logInfo(getClass(), "Command executed for " + (System.currentTimeMillis() - startTime) + " ms");
	}

}
