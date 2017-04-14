/**
 * 
 */
package ippoz.madness.lite.probes;

import ippoz.madness.lite.probes.mbean.BeanManager;
import ippoz.madness.lite.support.AppLogger;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Tommy
 *
 */
public class JVMProbe extends CycleProbe {

	private BeanManager bManager;
	
	public JVMProbe(LinkedList<Indicator> indicators) {
		super(ProbeType.JVM, "JVM", indicators);
		try {
			bManager = new BeanManager();
		} catch (Exception ex){
			AppLogger.logException(getClass(), ex, "Error while Initializing MBeans");
		}	
	}
	
	@Override
	public LinkedList<Indicator> setDefaultIndicators() {
		return bManager.getStandardAttributes();
	}

	@Override
	protected HashMap<Indicator, String> readParams() {
		return bManager.getObservation(getIndicators());
	}

	@Override
	public void setupParameters() {
		bManager.setBeanPreferences(getIndicators());
	}

	public static LinkedList<Indicator> listDefaultIndicatorNames() {
		BeanManager bM;
		try {
			bM = new BeanManager();
			return bM.getStandardAttributes();
		} catch (Exception ex) {
			AppLogger.logException(JVMProbe.class, ex, "Error while fetching JVM standard attributes");
		} 
		return null;
	}

	

}
