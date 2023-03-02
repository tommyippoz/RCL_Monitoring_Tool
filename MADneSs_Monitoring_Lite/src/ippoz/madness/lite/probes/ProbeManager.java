/**
 * 
 */
package ippoz.madness.lite.probes;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

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
		if(newProbe.canRun())
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
	
	public TreeMap<Date, HashMap<Indicator, String>> getMonitoredData(){
		TreeMap<Date, HashMap<Indicator, String>> mData, partial;
		if(probes == null || probes.size() == 0 || probes.getFirst() == null)
			return null;
		mData = probes.getFirst().getData();
		for(int i=1;i<probes.size();i++){
			partial = probes.get(i).getData();
			for(Date cDate : mData.keySet()) {
				mData.get(cDate).putAll(partial.get(getClosestDate(cDate, partial.keySet())));
			}
		}
		return mData;
	}
	
	private Date getClosestDate(Date refDate, Set<Date> dateList){
		Date closest = null;
		long minDist = Long.MAX_VALUE;
		for(Date d : dateList){
			if(Math.abs(d.getTime() - refDate.getTime()) < minDist){
				closest = d;
				minDist = Math.abs(d.getTime() - refDate.getTime());
			}
		}
		return closest;
	}
	
}
