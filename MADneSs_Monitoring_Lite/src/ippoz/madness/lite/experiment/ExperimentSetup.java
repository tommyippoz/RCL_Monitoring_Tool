/**
 * 
 */
package ippoz.madness.lite.experiment;

import ippoz.madness.lite.experiment.runner.ExperimentRunner;
import ippoz.madness.lite.support.MADneSsLiteSupport;

import java.util.Observable;

/**
 * @author Tommy
 *
 */
public class ExperimentSetup extends Observable {
	
	private String outputFolder;
	private ExperimentRunner eRunner;
	private int experimentIterations;
	private OutputType outputType;
	private boolean zipOutput;
	private boolean mailOutput;
	private String mailAddress;
	
	public ExperimentSetup(){
		outputFolder = null;
		eRunner = null;
		experimentIterations = 0;
		outputType = OutputType.SINGLE_FILE;
		zipOutput = false;
		mailOutput = false;
		mailAddress = null;
	}
	
	public void init(){
		setupChanged();
	}
	
	private void setupChanged(){
		setChanged();
		notifyObservers();
	}
	
	public void setOutputFolder(String text) {
		if(text != null && text.trim().length() > 0)
			outputFolder = text.trim();
		else outputFolder = null;
		setupChanged();
	}

	public void setOutputZipFlag(boolean selectedZip) {
		zipOutput = selectedZip;
		setupChanged();
	}
	
	public void setMailAddressFlag(boolean selectedMail) {
		mailOutput = selectedMail;
		setupChanged();
	}

	public void setMailAddress(String text) {
		if(text != null && MADneSsLiteSupport.isValidEmailAddress(text))
			mailAddress = text;
		else mailAddress = null;
		setupChanged();
	}

	public void setShellExperiment(String text) {
		// TODO Auto-generated method stub
		
	}

	public void setTimingExperiment(int parseInt) {
		// TODO Auto-generated method stub
		
	}

	public void setExperimentIterations(int expIterations) {
		if(expIterations > 0){
			experimentIterations = expIterations;
			setupChanged();
		}
	}

	public boolean isPreferenceSetupCompleted() {
		return outputFolder != null;
	}

	public boolean isExperimentSetupCompleted() {
		return eRunner != null && experimentIterations > 0;
	}

	public boolean isOutputSetupCompleted() {
		return outputType != null && (!mailOutput || mailOutput && MADneSsLiteSupport.isValidEmailAddress(mailAddress));
	}

	public boolean experimentReady() {
		return isPreferenceSetupCompleted() && isExperimentSetupCompleted() && isOutputSetupCompleted();
	}

}
