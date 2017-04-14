/**
 * 
 */
package ippoz.madness.lite.probes;

import ippoz.madness.lite.support.AppLogger;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * @author Tommy
 *
 */
public abstract class Probe implements Runnable {
	
	private ProbeType probeLayer;
	private String probeName;
	private LinkedList<Indicator> indicators;
	protected TreeMap<Date, HashMap<Indicator, String>> data;
	private int obsDelay;

	public Probe(ProbeType probeLayer, String probeName, LinkedList<Indicator> indicators) {
		this.probeLayer = probeLayer;
		this.probeName = probeName;
		this.indicators = indicators;
		data = null;
		obsDelay = 1000;
	}
	
	public TreeMap<Date, HashMap<Indicator, String>> getData(){
		return data;
	}
	
	public boolean hasIndicator(String indName){
		for(Indicator ind : indicators){
			if(ind.matches(indName))
				return true;
		}
		return false;
	}
	
	public Indicator getIndicator(String indName) {
		for(Indicator ind : indicators){
			if(ind.matches(indName))
				return ind;
		}
		return null;
	}

	public LinkedList<Indicator> getIndicators() {
		return indicators;
	}

	public int getObsDelay() {
		return obsDelay;
	}

	public void setObsDelay(int obsDelay) {
		this.obsDelay = obsDelay;
	}

	public ProbeType getProbeLayer() {
		return probeLayer;
	}

	public String getProbeName() {
		return probeName;
	}
	
	public abstract LinkedList<Indicator> setDefaultIndicators();
	
	public abstract void setupParameters();
	
	public abstract void startProbe();
	
	public abstract void shutdownProbe();

	@Override
	public void run() {
		if(indicators == null){
			indicators = setDefaultIndicators();
		}
		data = null;
		startProbe();
	}
	
}
