/**
 * 
 */
package ippoz.madness.lite.experiment;

import ippoz.madness.lite.experiment.runner.ExperimentRunner;
import ippoz.madness.lite.support.MADneSsLiteSupport;
import ippoz.madness.lite.support.PreferencesManager;

import java.io.File;
import java.util.Observable;

/**
 * @author Tommy
 *
 */
public class ExperimentSetup extends Observable {
	
	private static final String pOutFolder = "OUTPUT_FOLDER";
	private static final String pShellExp = "SHELL_EXPERIMENT";
	private static final String pTimingExp = "TIMING_EXPERIMENT";
	private static final String pExpIter = "EXPERIMENT_ITERATIONS";
	private static final String pOutType = "OUTPUT_TYPE";
	private static final String pZipOut = "OUTPUT_ZIP_FLAG";
	private static final String pMailOut = "OUTPUT_MAIL_ADDRESS";
	
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
	
	public String loadPreferencesFile(File selectedFile, boolean overwriteFlag) {
		String errLog = "";
		String pValue;
		PreferencesManager pManager = new PreferencesManager(selectedFile);
		for(String pTag : pManager.listTags()){
			pValue = pManager.getPreference(pTag); 
			if(pValue != null && pValue.length() > 0){
				switch(pTag){
					case pOutFolder:
						if(MADneSsLiteSupport.isValidPath(pValue)){
							if(overwriteFlag || (!overwriteFlag && outputFolder == null))
								setOutputFolder(pValue);
						} else errLog = errLog + pOutFolder + ": '" + pValue + "' is not a valid path\n";
						break;
					case pShellExp:
						break;
					case pTimingExp:
						break;
					case pExpIter:
						if(MADneSsLiteSupport.isInteger(pValue)){
							if(overwriteFlag || (!overwriteFlag && experimentIterations <= 0))
								setExperimentIterations(Integer.parseInt(pValue));
						} else errLog = errLog + pExpIter + ": '" + pValue + "' is not a valid number\n";
						break;
					case pOutType:
						if(OutputType.valueOf(pValue.toUpperCase()) != null){
							if(overwriteFlag || (!overwriteFlag && outputType == null))
								outputType = OutputType.valueOf(pValue.toUpperCase());
						}
						break;
					case pZipOut:
						if(MADneSsLiteSupport.isBoolean(pValue)){
							if(overwriteFlag)
								setOutputZipFlag(Boolean.parseBoolean(pValue));
						} else errLog = errLog + pZipOut + ": '" + pValue + "' is not a valid flag\n";
						break;
					case pMailOut:
						if(MADneSsLiteSupport.isBoolean(pValue)){
							if(overwriteFlag)
								setMailAddressFlag(Boolean.parseBoolean(pValue));
						} else errLog = errLog + pMailOut + ": '" + pValue + "' is not a valid flag\n";
						break;
					default:
						errLog = errLog + "Unable to parse preference '" + pTag + "'\n";	
				}
			}
		}
		return errLog;		
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

	public String getOutputFolder() {
		return outputFolder;
	}

	public ExperimentRunner getExperimentRunner() {
		return eRunner;
	}

	public int getExperimentIterations() {
		return experimentIterations;
	}

	public OutputType getOutputType() {
		return outputType;
	}

	public boolean getZipOutputFlag() {
		return zipOutput;
	}

	public boolean getMailOutputFlag() {
		return mailOutput;
	}

	public String getMailAddress() {
		return mailAddress;
	}
	
	

}
