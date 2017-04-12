/**
 * 
 */
package ippoz.madness.lite.experiment.runner;

import ippoz.madness.lite.support.AppLogger;

/**
 * @author Tommy
 *
 */
public class TimingExperiment extends ExperimentRunner {

	private int expDuration;
	
	public TimingExperiment(int expDuration){
		super();
		this.expDuration = expDuration;
	}
	
	
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}



	@Override
	public String getDetail() {
		return String.valueOf(expDuration);
	}
	
	@Override
	public String toString() {
		return getDetail();
	}

	@Override
	protected void runExperiment() {
		try {
			Thread.sleep(expDuration*1000);
		} catch (InterruptedException ex) {
			AppLogger.logException(getClass(), ex, "Error while executing TimingExperiment");
		}
	}

	public void setDuration(int expDuration) {
		this.expDuration = expDuration;
	}

}
