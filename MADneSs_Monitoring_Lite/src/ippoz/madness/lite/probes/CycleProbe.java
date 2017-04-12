/**
 * 
 */
package ippoz.madness.lite.probes;

import ippoz.madness.lite.support.AppLogger;

import java.util.HashMap;
import java.util.LinkedList;

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
			data = new LinkedList<HashMap<Indicator,String>>();
			startTime = System.currentTimeMillis();
			while(!halt){
			    data.add(readParams());
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
