/**
 * 
 */
package ippoz.madness.lite.experiment.runner;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import ippoz.madness.lite.probes.Indicator;
import ippoz.madness.lite.probes.JVMProbe;
import ippoz.madness.lite.probes.Probe;
import ippoz.madness.lite.probes.ProbeManager;
import ippoz.madness.lite.probes.ProbeType;

/**
 * @author Tommy
 *
 */
public abstract class ExperimentRunner implements Runnable {
	
	private Date startTime;
	private ProbeManager pManager;
	private int obsInterval;
	
	public ExperimentRunner(){
		obsInterval = 1000;
	}
	
	public void setProbes(HashMap<ProbeType, LinkedList<Indicator>> indMap) {
		pManager = new ProbeManager();
		for(ProbeType pt : indMap.keySet()){
			switch(pt){
				case JVM:
					addProbe(new JVMProbe(indMap.get(pt)));
					break;
				default:
					break;
			}
		}
		
	}
	
	public Date getStartTime(){
		return startTime;
	}
	
	public void setObsInterval(int obsInt){
		obsInterval = obsInt;
	}
	
	public int getObsInterval(){
		return obsInterval;
	}
	
	private void addProbe(Probe probe){
		probe.setObsDelay(obsInterval);
		pManager.addProbe(probe);
	}

	public LinkedList<HashMap<Indicator, String>> getMonitoredData(){
		return pManager.getMonitoredData();
	}
	
	@Override
	public void run() {
		startTime = new Date();
		pManager.startProbes();
		runExperiment();
		pManager.shutdownProbes();
	}
	
	public void newExperiment(){
		
	}

	protected abstract void runExperiment();

	public abstract String getDetail();

}
