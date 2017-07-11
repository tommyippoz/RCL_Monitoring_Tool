/**
 * 
 */
package ippoz.madness.lite.experiment.runner;

import ippoz.madness.lite.probes.CentOSProbe;
import ippoz.madness.lite.probes.Indicator;
import ippoz.madness.lite.probes.JVMProbe;
import ippoz.madness.lite.probes.Probe;
import ippoz.madness.lite.probes.ProbeManager;
import ippoz.madness.lite.probes.ProbeType;
import ippoz.madness.lite.probes.UnixAdditionalProbe;
import ippoz.madness.lite.probes.UnixNetworkProbe;
import ippoz.madness.lite.support.AppLogger;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

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
			if(indMap.get(pt) != null && indMap.get(pt).size() > 0){
				switch(pt){
					case JVM:
						addProbe(new JVMProbe(indMap.get(pt)));
						break;
					case CENTOS:
						addProbe(new CentOSProbe(indMap.get(pt)));
						break;
					case UNIX_NETWORK:
						addProbe(new UnixNetworkProbe(indMap.get(pt)));
						break;
					case UNIX:
						addProbe(new UnixAdditionalProbe(indMap.get(pt)));
						break;
					default:
						break;
				}
			} else AppLogger.logInfo(getClass(), "No indicators were selected for probe " + pt.toString());
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

	public TreeMap<Date, HashMap<Indicator, String>> getMonitoredData(){
		return pManager.getMonitoredData();
	}
	
	@Override
	public void run() {
		startTime = new Date();
		pManager.startProbes();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		runExperiment();
		pManager.shutdownProbes();
	}
	
	protected abstract void runExperiment();

	public abstract String getDetail();

}
