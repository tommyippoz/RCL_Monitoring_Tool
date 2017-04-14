/**
 * 
 */
package ippoz.madness.lite.probes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * @author root
 *
 */
public class UnixNetworkProbe extends IteratingCommandProbe {

	private HashMap<String, String> paramNames;
	
	public UnixNetworkProbe(LinkedList<Indicator> indicators) {
		super("dstat", "-n --tcp", ProbeType.UNIX_NETWORK, "UnixNetwork", indicators);
	}
	
	@Override
	public void setupParameters() {
		setupParamNames();	
	}

	private void setupParamNames() {
		paramNames = new HashMap<String, String>();
		paramNames.put("recv", "Net_Received");
		paramNames.put("send", "Net_Sent");
		paramNames.put("lis", "Tcp_Listen");
		paramNames.put("act", "Tcp_Established");
		paramNames.put("syn", "Tcp_Syn");
		paramNames.put("tim", "Tcp_TimeWait");
		paramNames.put("clo", "Tcp_Close");
	}

	@Override
	protected boolean isHeader(String line) {
		return line.trim().endsWith("--");
	}

	@Override
	protected HashMap<Indicator, String> readParams() {
		String tokenName;
		HashMap<Indicator, String> params = new HashMap<Indicator, String>();
		Iterator<String> keyIt = paramNames.keySet().iterator();
		synchronized(lastReaded){
			StringTokenizer lt = new StringTokenizer(lastReaded.replace("|", ""));
			while(lt.hasMoreTokens()){
				tokenName = paramNames.get(keyIt.next());
				if(hasIndicator(tokenName)){
					params.put(getIndicator(tokenName), parseQuantity(lt.nextToken()));
				} else lt.nextToken();
			}
		}
		return params;
	}

	private String parseQuantity(String splitted) {
		String cleared = splitted.trim().toUpperCase();
		if(cleared.endsWith("B"))
			return splitted.substring(0, splitted.length()-1);
		else if(cleared.endsWith("K"))
			return String.valueOf(1000*Integer.parseInt(splitted.substring(0, splitted.length()-1)));
		else if(cleared.endsWith("M"))
			return String.valueOf(1000000*Integer.parseInt(splitted.substring(0, splitted.length()-1)));
		else return splitted;
	}

	@Override
	public LinkedList<Indicator> setDefaultIndicators() {
		LinkedList<Indicator> defaultList = new LinkedList<Indicator>();
		for(String indName : listDefaultIndicatorNames()){
			defaultList.add(Indicator.buildIndicator(indName, ProbeType.UNIX_NETWORK));
		}
		return defaultList;
	}

	public static String[] listDefaultIndicatorNames() {
		return new String[]{"Net_Received", "Net_Sent", "Tcp_Listen", "Tcp_Established",
				"Tcp_Syn", "Tcp_TimeWait", "Tcp_Close"};
	}	

}
