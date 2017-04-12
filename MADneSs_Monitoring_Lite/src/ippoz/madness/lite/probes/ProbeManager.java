/**
 * 
 */
package ippoz.madness.lite.probes;

import ippoz.madness.lite.support.AppLogger;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Tommy
 *
 */
public class ProbeManager {

	private LinkedList<Probe> probes;
	
	public ProbeManager(){
		probes = new LinkedList<Probe>();
	}
	
	public void addProbe(Probe newProbe){
		newProbe.setupParameters();
		probes.add(newProbe);
	}
	
	public void startProbes(){
		for(Probe probe : probes){
			new Thread(probe).start();
		}
	}
	
	public void shutdownProbes(){
		for(Probe probe : probes){
			probe.shutdownProbe();
		}
	}
	
	public LinkedList<HashMap<Indicator, String>> getMonitoredData(){
		LinkedList<HashMap<Indicator, String>> mData, partial;
		if(probes == null || probes.size() == 0)
			return null;
		mData = probes.getFirst().getData();
		for(int i=1;i<probes.size();i++){
			partial = probes.get(i).getData();
			for(int j=0;j<mData.size();j++){
				mData.get(j).putAll(partial.get(j));
			}
		}
		return mData;
	}
	
}
