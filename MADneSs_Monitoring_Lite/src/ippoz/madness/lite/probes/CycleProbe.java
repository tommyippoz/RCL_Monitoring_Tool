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
 * @author root
 *
 */
public abstract class CycleProbe extends Probe {
	
	private boolean halt;

	public CycleProbe(ProbeType probeLayer, String probeName, LinkedList<Indicator> indicators) {
		super(probeLayer, probeName, indicators);
		halt = false;
	}

	@Override
	public void startProbe() {
		long startTime;	 
		try {
			data = new TreeMap<Date, HashMap<Indicator, String>>();
			startTime = System.currentTimeMillis();
			while(!halt){
			    data.put(new Date(startTime), readParams());
			    startTime = startTime + getObsDelay();
			    Thread.sleep(startTime - System.currentTimeMillis());
			}
		} catch (Exception ex) {
			AppLogger.logException(getClass(), ex, "Error");
		} 
		
	}

	protected abstract HashMap<Indicator, String> readParams();

	@Override
	public void shutdownProbe() {
		halt = true;
	}

}
