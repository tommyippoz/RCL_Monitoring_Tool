/**
 * 
 */
package ippoz.madness.lite.probes;

import ippoz.madness.lite.support.AppLogger;
import ippoz.madness.lite.support.AppUtility;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author root
 *
 */
public class CentOSProbe extends CycleProbe {
	
	public CentOSProbe(HashMap<String, String> involvedAttributes, LinkedList<Indicator> indicators) {
		super(ProbeType.CENTOS, "CentOS", indicators);
	}

	@Override
	protected HashMap<Indicator, String> readParams() {
		HashMap<Indicator, String> outMap = new HashMap<Indicator, String>();
		outMap.putAll(getMemInfo());
		outMap.putAll(getVirtMemInfo());
		outMap.putAll(getCpuInfo());
		return outMap;
	}

	private HashMap<Indicator, String> getMemInfo() {
		String[] splitted;
		String attValue;
		HashMap<Indicator, String> outMap = new HashMap<Indicator, String>();
		try {
			for(String readedRow : AppUtility.runScriptInto("cat /proc/meminfo", "", false)){
				splitted = readedRow.trim().split(":");
				if(hasIndicator(splitted[0].trim().toUpperCase())){
					if(splitted[1].trim().toUpperCase().endsWith("GB")){
						attValue = String.valueOf((int)(Double.parseDouble(splitted[1].trim().substring(0, splitted[1].trim().indexOf(" ")))*1000));
					} else if(splitted[1].trim().toUpperCase().endsWith("KB")){
						attValue = splitted[1].trim().substring(0, splitted[1].trim().indexOf(" "));
					}
					else attValue = readedRow.split(":")[1].trim();
					outMap.put(getIndicator(splitted[0].trim().toUpperCase()), attValue);
				}
			}
		} catch (IOException ex) {
			AppLogger.logException(getClass(), ex, "Unable to read /proc/meminfo");
		}
		return outMap;
	}

	private HashMap<Indicator, String> getVirtMemInfo() {
		String[] splitted;
		HashMap<Indicator, String> outMap = new HashMap<Indicator, String>();
		try {
			for(String readedRow : AppUtility.runScriptInto("cat /proc/vmstat", "", false)){
				splitted = readedRow.trim().split(" ");
				if(hasIndicator(splitted[0].trim().toUpperCase())){
					outMap.put(getIndicator(splitted[0].trim().toUpperCase()), splitted[1].trim());
				}
			}
		} catch (IOException ex) {
			AppLogger.logException(getClass(), ex, "Unable to read /proc/meminfo");
		}
		return outMap;
	}
	
	private HashMap<Indicator, String> getCpuInfo() {
		String[] splitted;
		HashMap<Indicator, String> outMap = new HashMap<Indicator, String>();
		LinkedList<String> outList;
		try {
			outList = AppUtility.runScriptInto("cat /proc/stat", "", false);
			if(outList.size() > 0){
				splitted = outList.getFirst().trim().split(" ");
				if(hasIndicator("CPU User Processes"))
					outMap.put(getIndicator("CPU User Processes"), splitted[2].trim());
				if(hasIndicator("CPU Niced Processes"))
					outMap.put(getIndicator("CPU Niced Processes"), splitted[3].trim());
				if(hasIndicator("CPU Kernel Processes"))
					outMap.put(getIndicator("CPU Kernel Processes"), splitted[4].trim());
				if(hasIndicator("CPU Idle Processes"))
					outMap.put(getIndicator("CPU Idle Processes"), splitted[5].trim());
				if(hasIndicator("CPU I/O Wait Processes"))
					outMap.put(getIndicator("CPU I/O Wait Processes"), splitted[6].trim());
				if(hasIndicator("CPU Interrupts"))
					outMap.put(getIndicator("CPU Interrupts"), splitted[7].trim());
				if(hasIndicator("CPU Soft Interrupts"))
					outMap.put(getIndicator("CPU Soft Interrupts"), splitted[8].trim());
			}
		} catch (IOException ex) {
			AppLogger.logException(getClass(), ex, "Unable to read /proc/stat");
		}
		return outMap;
	}

	@Override
	public void setupParameters() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LinkedList<Indicator> setDefaultIndicators() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String[] listDefaultIndicatorNames() {
		return new String[]{"CPU User Processes", "CPU Niced Processes", "CPU Kernel Processes", 
				"CPU Idle Processes", "CPU I/O Wait Processes", "CPU Interrupts", "CPU Soft Interrupts"};
	}
	
}
