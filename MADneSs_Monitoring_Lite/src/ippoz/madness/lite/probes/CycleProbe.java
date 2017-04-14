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
		HashMap<Indicator, String> partialData;
		try {
			data = new TreeMap<Date, HashMap<Indicator, String>>();
			startTime = System.currentTimeMillis();
			AppLogger.logInfo(getClass(), "Probe " + getProbeName() + " started");
			while(!halt){
				partialData = readParams();
				if(partialData != null && partialData.size() > 0){
					data.put(new Date(startTime), partialData);
				} else AppLogger.logInfo(getClass(), getProbeName() + ": failed to read data at instant " + new Date(startTime).toString() );
			    startTime = startTime + getObsDelay();
			    Thread.sleep(startTime - System.currentTimeMillis());
			}
			AppLogger.logInfo(getClass(), "Probe " + getProbeName() + " shutdowned");
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
