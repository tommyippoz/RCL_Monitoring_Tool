/**
 * 
 */
package ippoz.madness.lite.probes;

/**
 * @author Tommy
 *
 */
public class Indicator {
	
	private String indName;
	private ProbeType pType;
	
	public static Indicator buildIndicator(String ind, ProbeType probe) {
		return new Indicator(ind, probe);
	}
	
	private Indicator(String indName, ProbeType pType) {
		this.indName = indName;
		this.pType = pType;
	}
	
	public String getIndName() {
		return indName;
	}
	
	public ProbeType getProbeType() {
		return pType;
	}
	
	public boolean matches(String ind){
		return ind.toUpperCase().compareTo(indName.toUpperCase()) == 0;
	}
	
}
