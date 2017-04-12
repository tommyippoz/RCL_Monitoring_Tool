/**
 * 
 */
package ippoz.madness.lite.experiment;

import ippoz.madness.lite.experiment.runner.ExperimentRunner;
import ippoz.madness.lite.experiment.runner.ShellExperiment;
import ippoz.madness.lite.experiment.runner.TimingExperiment;
import ippoz.madness.lite.probes.CentOSProbe;
import ippoz.madness.lite.probes.Indicator;
import ippoz.madness.lite.probes.JVMProbe;
import ippoz.madness.lite.probes.ProbeType;
import ippoz.madness.lite.support.AppLogger;
import ippoz.madness.lite.support.AppUtility;
import ippoz.madness.lite.support.MADneSsLiteSupport;
import ippoz.madness.lite.support.PreferencesManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

/**
 * @author Tommy
 *
 */
public class ExperimentSetup extends Observable {
	
	private static final String pOutFolder = "OUTPUT_FOLDER";
	private static final String pObsInterval = "OBSERVATION_INTERVAL";
	private static final String pIndPreferences = "INDICATOR_PREFERENCES_FILE";
	private static final String pShellExp = "SHELL_EXPERIMENT";
	private static final String pTimingExp = "TIMING_EXPERIMENT";
	private static final String pExpIter = "EXPERIMENT_ITERATIONS";
	private static final String pOutType = "OUTPUT_TYPE";
	private static final String pZipOut = "OUTPUT_ZIP_FLAG";
	private static final String pMailOut = "OUTPUT_MAIL_ADDRESS";
	
	private String expName;
	private String outputFolder;
	private File indPrefFile;
	private HashMap<ProbeType, LinkedList<Indicator>> indMap;
	private int obsInterval;
	private ExperimentRunner eRunner;
	private int experimentIterations;
	private OutputType outputType;
	private boolean zipOutput;
	private boolean mailOutput;
	private String mailAddress;
	
	public ExperimentSetup(){
		expName = null;
		outputFolder = null;
		indPrefFile = null;
		indMap = null;
		obsInterval = 0;
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
					case pObsInterval:
						if(MADneSsLiteSupport.isInteger(pValue)){
							if(overwriteFlag || (!overwriteFlag && obsInterval <= 0))
								setObservationInterval(Integer.parseInt(pValue));
						} else errLog = errLog + pObsInterval + ": '" + pValue + "' is not a valid number\n";
						break;
					case pIndPreferences:
						if(new File(pValue).exists()) {
							if(overwriteFlag || (!overwriteFlag && indMap == null))
								loadIndicatorPreferences(new File(pValue));
						} else errLog = errLog + pIndPreferences + ": '" + pValue + "' is not a valid file\n";
						break;
					case pShellExp:
						break;
					case pTimingExp:
						if(MADneSsLiteSupport.isInteger(pValue) && Integer.parseInt(pValue) > 0){
							if(overwriteFlag || (!overwriteFlag && eRunner != null))
								eRunner = new TimingExperiment(Integer.parseInt(pValue));
						} else errLog = errLog + pTimingExp + ": '" + pValue + "' is not a valid experiment timing\n";
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
						if(MADneSsLiteSupport.isValidEmailAddress(pValue)){
							if(overwriteFlag){
								setMailAddressFlag(true);
								setMailAddress(pValue);
							}
						} else errLog = errLog + pMailOut + ": '" + pValue + "' is not a valid mail address\n";
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
	
	public void setObservationInterval(int obsInt) {
		if(obsInt > 0){
			obsInterval = obsInt;
			setupChanged();
		}
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

	public void setShellExperiment(String shellCommand) {
		// TODO Auto-generated method stub
		
	}

	public void setTimingExperiment(int expDuration) {
		if(expDuration > 0){
			eRunner = new TimingExperiment(expDuration);
			setupChanged();
		}
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
	
	public int getObservationInterval() {
		return obsInterval;
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

	public void writePreferences(File pFile) {
		BufferedWriter writer;
		try{
			writer = new BufferedWriter(new FileWriter(pFile));
			writer.write("* Preferences File for RCL Monitoring Tool\n");
			writer.write("\n* Output Folder\n");
			if(outputFolder != null){
				writer.write(pOutFolder + " = " + outputFolder + "\n");
			}
			writer.write("\n* Observation Interval\n");
			if(obsInterval > 0){
				writer.write(pObsInterval + " = " + obsInterval + "\n");
			}
			writer.write("\n* Indicator Preferences File\n");
			if(indPrefFile != null){
				writer.write(pIndPreferences + " = " + indPrefFile.getPath() + "\n");
			}
			writer.write("\n* Experiment Type Shell - Timing\n");
			if(eRunner != null && eRunner instanceof ShellExperiment){
				writer.write(pShellExp + " = " + eRunner.toString() + "\n");
			} else if(eRunner != null && eRunner instanceof TimingExperiment){
				writer.write(pTimingExp + " = " + eRunner.toString() + "\n");
			}
			writer.write("\n* ExperimentIter\n");
			if(experimentIterations > 0){
				writer.write(pExpIter + " = " + experimentIterations + "\n");
			}
			writer.write("\n* Output Type\n");
			if(outputType != null){
				writer.write(pOutType + " = " + outputType.toString() + "\n");
			}
			writer.write("\n* Zip Output\n");
			writer.write(pZipOut + " = " + String.valueOf(zipOutput) + "\n");
			writer.write("\n* Mail Output\n");
			if(mailOutput){
				writer.write(pMailOut + " = " + mailAddress + "\n");
			}
			writer.close();
		} catch(FileNotFoundException ex){
			AppLogger.logException(getClass(), ex, "Preferences file not found");
		} catch (IOException ex) {
			AppLogger.logException(getClass(), ex, "Error while writing preferences");
		}
	}

	public void runExperiment() {
		Thread t;
		try {
			if(eRunner != null){
				if(obsInterval > 0){
					eRunner.setObsInterval(obsInterval);
				}
				AppLogger.logInfo(getClass(), "Executing " + experimentIterations + " experiments");
				for(int i=0;i<experimentIterations;i++){
					eRunner.setProbes(indMap);
					t = new Thread(eRunner);
					AppLogger.logInfo(getClass(), "Experiment " + (i+1) + ": STARTED");
					t.start();
					t.join();
					AppLogger.logInfo(getClass(), "Experiment " + (i+1) + ": FINISHED");
					AppLogger.logInfo(getClass(), "Experiment " + (i+1) + ": Stored " + storeData(i+1) + " Observations");
					t = null;
				}
			} else AppLogger.logError(getClass(), "ExpRunnerError", "Experiment Runner is not set");
		} catch (InterruptedException ex) {
			AppLogger.logException(getClass(), ex, "Error while running experiment");
		}
		
	}

	private int storeData(int i) {
		String completeExpName = expName + "_" + i;
		File mainDir = new File(outputFolder + "/" + completeExpName);
		mainDir.mkdirs();
		if(eRunner.getMonitoredData() != null){
			switch(outputType){
				case SINGLE_FILE:
				case EACH_INDICATOR:
				case EACH_EXPERIMENT:
				case EACH_EXPERIMENT_EACH_INDICATOR:
				default:
					printSingleFile(mainDir, completeExpName);
					break;
			}
		}
		return eRunner.getMonitoredData() != null ? eRunner.getMonitoredData().size() : 0;
	}

	private void printSingleFile(File mainDir, String completeExpName) {
		File singleFile;
		BufferedWriter writer;
		try {
			if(eRunner.getMonitoredData() != null && eRunner.getMonitoredData().size() > 0){
				singleFile = new File(mainDir.getPath() + "/" + completeExpName + ".csv");
				writer = new BufferedWriter(new FileWriter(singleFile));
				writer.write("#,datetime");
				for(Indicator ind : eRunner.getMonitoredData().getFirst().keySet()){
					writer.write("," + ind.getIndName());
				}
				writer.newLine();
				for(int i=0;i<eRunner.getMonitoredData().size();i++){
					writer.write((i+1) + "," + new Date(eRunner.getStartTime().getTime() + i*eRunner.getObsInterval()).toString());
					for(String value : eRunner.getMonitoredData().get(i).values()){
						writer.write("," + value);
					}
					writer.newLine();
				}
				writer.close();
			}
		} catch (IOException ex) {
			AppLogger.logException(getClass(), ex, "Unable to write output of experiment " + completeExpName);
		}
	}

	public void loadIndicatorPreferences(File indFile) {
		String readed;
		ProbeType currentType;
		BufferedReader reader;
		try {
			if(indFile.exists()){
				indPrefFile = indFile;
				currentType = null;
				indMap = new HashMap<ProbeType, LinkedList<Indicator>>();
				reader = new BufferedReader(new FileReader(indPrefFile));
				while(reader.ready()){
					readed = reader.readLine().trim();
					if(readed.length() > 0 && !readed.startsWith("*")){
						try {
							ProbeType.valueOf(readed);
							currentType = ProbeType.valueOf(readed);
							indMap.put(currentType, new LinkedList<Indicator>());
						} catch(Exception ex){
							if(currentType != null){
								indMap.get(currentType).add(Indicator.buildIndicator(readed, currentType));
							} else AppLogger.logError(getClass(), "IndicatorDefinitionError", "Unable to fetch layer of indicator '" + readed + "'");
						}
					}
				}
				reader.close();
				setChanged();
			}
		} catch (IOException ex) {
			AppLogger.logException(getClass(), ex, "Unable to load Indicator preferences");
		}
	}

	public int getObservedIndicators() {
		int count = 0;
		if(indMap != null){
			for(ProbeType pt : indMap.keySet()){
				count = count + indMap.get(pt).size();
			}
			return count;
		} else return 0;
	}

	public boolean areIndicatorPreferencesSet() {
		return indMap != null;
	}

	public static void generateDefaultIndicatorPreferences() {
		HashMap<ProbeType, LinkedList<String>> defaultInd = buildDefaultMap();
		File indDefFile = new File(System.getProperty("user.dir") + "/defaultIndicators.preferences");
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(indDefFile));
			writer.write("* Default Indicators for RCL Monitoring Tool\n* Use '*' to comment rows\n"
					+ "* All indicators are commented, please uncomment the chosen ones\n");
			for(ProbeType pt : defaultInd.keySet()){
				writer.write("\n" + pt.toString() + "\n");
				for(String indName : defaultInd.get(pt)){
					writer.write("* " + indName + "\n");
				}
			}
			writer.close();
		} catch (IOException ex) {
			AppLogger.logException(ExperimentSetup.class, ex, "Unable to write default indicators");
		}
	}
	
	private static HashMap<ProbeType, LinkedList<String>> buildDefaultMap(){
		HashMap<ProbeType, LinkedList<String>> defaultInd = new HashMap<ProbeType, LinkedList<String>>();
		defaultInd.put(ProbeType.JVM, new LinkedList<String>());
		for(Indicator ind : JVMProbe.listDefaultIndicatorNames()){
			defaultInd.get(ProbeType.JVM).add(ind.getIndName());
		}
		if(AppUtility.isUNIX()){
			defaultInd.put(ProbeType.CENTOS, new LinkedList<String>());
			for(String indName : CentOSProbe.listDefaultIndicatorNames()){
				defaultInd.get(ProbeType.CENTOS).add(indName);
			}
		}
		return defaultInd;
	}

}
