/**
 * 
 */
package ippoz.madness.lite.ui;

import ippoz.madness.lite.experiment.ExperimentSetup;
import ippoz.madness.lite.experiment.OutputType;
import ippoz.madness.lite.experiment.runner.ShellExperiment;
import ippoz.madness.lite.experiment.runner.TimingExperiment;
import ippoz.madness.lite.support.MADneSsLiteSupport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Tommy
 *
 */
public class MADneSsLiteUI {

	private ExperimentSetup expSetup;
	
	private JFrame frame;
	private JPanel pHeader;
	private JPanel pSubHeader;
	private JPanel pPreferences;
	private JPanel pExperiments;
	private JPanel pOutput;
	private JPanel pFooter;
	private File pFile;
	
	private JTextField txtExpName;
	private JTextField txtOutputFolder;
	private JTextField txtObsInterval;
	private JLabel lblIndPrefFile;
	
	private JRadioButton rdbtnShell;
	private JRadioButton rdbtnTime;
	private JTextField txtShellCommand;
	private JTextField txtExpTime;
	private JTextField txtIterations;
	
	private JRadioButton rdbtnSingleFile;
	private JRadioButton rdbtnFileForEachInd;
	private JRadioButton rdbtnFileForEachExp;
	private JRadioButton rdbtnFileForEachIndExp;
	private JCheckBox chckbxZipResults;
	private JCheckBox chckbxSendByMail;
	private JTextField txtInsertMailAddress;
	
	public MADneSsLiteUI(ExperimentSetup expSetup){
		this.expSetup = expSetup;
		setFrame();
		buildUI();
	}
	
	private void setFrame(){
		frame = new JFrame();
		frame.setTitle("RCL Monitoring Tool");
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 1200, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(new ImageIcon(MADneSsLiteUI.class.getResource("/RCL_Logo_Small.png")).getImage());
	}
	
	private void buildUI(){
		setHeader();
		setPreferences();
		setExperiments();
		setOutput();
		setFooter();
	}
	
	public void loadPreferencesFile(File selectedFile) {
		ExperimentSetup.generateDefaultIndicatorPreferences();
		String errLog = expSetup.loadPreferencesFile(selectedFile, true);
		if(errLog != null && errLog.length() > 0){
			JOptionPane.showMessageDialog(frame, errLog, "Preferences Alert", JOptionPane.WARNING_MESSAGE);
		}
		formReload();
	}
	
	private void formReload(){
		
		// Preferences
		if(expSetup.getExperimentName() != null){
			txtExpName.setText(expSetup.getExperimentName());
			txtExpName.setEnabled(true);
		}
		if(expSetup.getOutputFolder() != null){
			txtOutputFolder.setText(expSetup.getOutputFolder());
			txtOutputFolder.setEnabled(true);
		}
		if(expSetup.getObservationInterval() > 0){
			txtObsInterval.setText(String.valueOf(expSetup.getObservationInterval()));
			txtObsInterval.setEnabled(true);
		}
		if(expSetup.areIndicatorPreferencesSet()){
			lblIndPrefFile.setText("Observed Indicators: " + expSetup.getObservedIndicators());
		} else lblIndPrefFile.setText("Indicator Preferences Not Set");
		
		// Experiment
		if(expSetup.getExperimentRunner() != null){
			if(expSetup.getExperimentRunner() instanceof ShellExperiment){
				rdbtnShell.setSelected(true);
				txtShellCommand.setText(expSetup.getExperimentRunner().getDetail());
				txtShellCommand.setEnabled(true);
				txtExpTime.setEnabled(false);
				
			} else if(expSetup.getExperimentRunner() instanceof TimingExperiment){
				rdbtnTime.setSelected(true);
				txtExpTime.setText(expSetup.getExperimentRunner().getDetail());
				txtExpTime.setEnabled(true);
				txtShellCommand.setEnabled(false);
			}
		}
		if(expSetup.getExperimentIterations() > 0){
			txtIterations.setText(String.valueOf(expSetup.getExperimentIterations()));
			txtIterations.setEnabled(true);
		}
		
		// Output
		switch(expSetup.getOutputType()){
			case SINGLE_FILE:
				rdbtnSingleFile.setSelected(true);
				break;
			case EACH_INDICATOR:
				rdbtnFileForEachInd.setSelected(true);
				break;
			case EACH_EXPERIMENT:
				rdbtnFileForEachExp.setSelected(true);
				break;
			case EACH_EXPERIMENT_EACH_INDICATOR:
				rdbtnFileForEachIndExp.setSelected(true);
				break;
			default: 
				break;
		}
		if(expSetup.getZipOutputFlag())
			chckbxZipResults.setSelected(true);
		if(expSetup.getMailOutputFlag()){
			chckbxSendByMail.setSelected(true);
			txtInsertMailAddress.setText(expSetup.getMailAddress());
			txtInsertMailAddress.setEnabled(true);
		}

	}
	
	private void setHeader(){
		pHeader = new JPanel();
		frame.getContentPane().add(pHeader, BorderLayout.NORTH);
		pHeader.setLayout(new BoxLayout(pHeader, BoxLayout.Y_AXIS));
		
		JPanel pLblOutput = new JPanel();
		
		JLabel lblOutput = new JLabel("RCL Monitoring Tool");
		lblOutput.setFont(lblOutput.getFont().deriveFont(new Float(25)));
		lblOutput.setBorder(new EmptyBorder(20, 20, 20, 20));
		pLblOutput.add(lblOutput);
		pHeader.add(pLblOutput);
		
		pSubHeader = new JPanel();
		pSubHeader.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Preferences")));
		
		JButton bResetPref = new JButton("Reset Preferences");
		bResetPref.addMouseListener( new TextBoxListener("Generates default preferences.\n Overwrites the existing ones."));
		bResetPref.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) { 
				try {
					pFile = new File("prefFile.preferences");
					if(pFile.exists())
						pFile.delete();
					pFile.createNewFile();
					loadPreferencesFile(pFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		pSubHeader.add(bResetPref);
		
		pSubHeader.add(Box.createRigidArea(new Dimension(100,0)));
		
		JButton bLoadPref = new JButton("Load Existing Preferences");
		bLoadPref.addMouseListener( new TextBoxListener("Loads existing preferences from a file"));
		bLoadPref.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) { 
				int returnValue = 0;
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Preferences Files", "preferences"));
		        returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	pFile = fileChooser.getSelectedFile();
		        	loadPreferencesFile(fileChooser.getSelectedFile());
		        }
			}
			
		});
		pSubHeader.add(bLoadPref);
		
		pSubHeader.add(Box.createRigidArea(new Dimension(100,0)));
		
		JButton bSavePref = new JButton("Save Preferences");
		bSavePref.addMouseListener( new TextBoxListener("Saves preferences in a file"));
		bSavePref.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) { 
				if(pFile == null){
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
					fileChooser.setFileFilter(new FileNameExtensionFilter("Preferences Files", "preferences"));
			        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			        	pFile = fileChooser.getSelectedFile();
			        }
				}
				if(pFile != null) {
					expSetup.writePreferences(pFile);
				}
			}
			
		});
		
		pSubHeader.add(bSavePref);
		
		pHeader.add(pSubHeader);
		
		
	}

	private void setPreferences(){
		pPreferences = new JPanel();
		pPreferences.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("General")));
		frame.getContentPane().add(pPreferences, BorderLayout.WEST);
		pPreferences.setLayout(new BoxLayout(pPreferences, BoxLayout.Y_AXIS));
		
		JPanel pExpName = new JPanel();
		
		JLabel lblExpName = new JLabel("Experiment Name");
		lblExpName.addMouseListener( new TextBoxListener("Defines the name of the experiment"));
		pExpName.add(lblExpName);
		
		txtExpName = new JTextField();
		txtExpName.setText("");
		txtExpName.setEnabled(true);
		txtExpName.setColumns(15);
		txtExpName.setMaximumSize(txtExpName.getPreferredSize());
		txtExpName.getDocument().addDocumentListener(new DocumentListener() {
			  
			public void changedUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void removeUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void insertUpdate(DocumentEvent e) {
				workOnUpdate();
			}
 
			public void workOnUpdate() {
			    expSetup.setExperimentName(txtExpName.getText());
			}
		});
		
		pExpName.add(lblExpName);
		pExpName.add(txtExpName);
		pPreferences.add(pExpName);
		
		JLabel lblOutFolder = new JLabel("Output Folder");
		lblOutFolder.addMouseListener( new TextBoxListener("Defines the destination folder of generated files"));
		pPreferences.add(lblOutFolder);
		
		txtOutputFolder = new JTextField();
		txtOutputFolder.setText("");
		txtOutputFolder.setEnabled(true);
		txtOutputFolder.setColumns(15);
		txtOutputFolder.setMaximumSize(txtOutputFolder.getPreferredSize());
		txtOutputFolder.getDocument().addDocumentListener(new DocumentListener() {
			  
			public void changedUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void removeUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void insertUpdate(DocumentEvent e) {
				workOnUpdate();
			}
 
			public void workOnUpdate() {
			    expSetup.setOutputFolder(txtOutputFolder.getText());
			}
		});
		
		JPanel pOutFolder = new JPanel();
		pOutFolder.add(lblOutFolder);
		pOutFolder.add(txtOutputFolder);
		pPreferences.add(pOutFolder);
		
		JPanel pIndPreferences = new JPanel();
		
		JLabel lblIndPreferences = new JLabel("Indicator Preferences");
		lblIndPreferences.addMouseListener(new TextBoxListener("Loads the indicator to monitor. Default indicators will be used if no data is provided"));
		pIndPreferences.add(lblIndPreferences);
		
		JButton bIndPref = new JButton("Load");
		bIndPref.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) { 
				int returnValue = 0;
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Preferences Files", "preferences"));
		        returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	expSetup.loadIndicatorPreferences(fileChooser.getSelectedFile());
		        	formReload();
		        }
			}
			
		});
		pIndPreferences.add(bIndPref);
		
		pPreferences.add(pIndPreferences);
		
		lblIndPrefFile = new JLabel("Indicator Preferences not set");
		pPreferences.add(lblIndPrefFile);
		
		JPanel panelOkNo = new JPanel();
		ImageIcon imageOK = new ImageIcon(MADneSsLiteUI.class.getResource("/picOK.jpg"));
		Image imageSupport = imageOK.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageOK = new ImageIcon(imageSupport);
		ImageIcon imageNO = new ImageIcon(MADneSsLiteUI.class.getResource("/picNO.jpg"));
		imageSupport = imageNO.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageNO = new ImageIcon(imageSupport);
		JLabel labelOK = new JLabel("", imageOK, JLabel.CENTER);
		JLabel labelNO = new JLabel("", imageNO, JLabel.CENTER);
		labelOK.setEnabled(false);
		panelOkNo.add(labelNO);
		panelOkNo.add(labelOK);
		pPreferences.add(panelOkNo);
		expSetup.addObserver(new SetupObserver(0, labelNO, labelOK));
	}
	
	private void setExperiments(){
		pExperiments = new JPanel();
		pExperiments.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Experiments")));
		frame.getContentPane().add(pExperiments, BorderLayout.CENTER);
		pExperiments.setLayout(new BoxLayout(pExperiments, BoxLayout.Y_AXIS));
		
		txtShellCommand = new JTextField();
		txtExpTime = new JTextField();
		txtIterations = new JTextField();
		
		ButtonGroup group = new ButtonGroup();
		
		JPanel pShComd = new JPanel();
		
		rdbtnShell = new JRadioButton("Execute shell command");
		rdbtnShell.addMouseListener( new TextBoxListener("Monitors the system as long as a given task is running"));
		rdbtnShell.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	txtShellCommand.setEnabled(true);
	        	txtExpTime.setEnabled(false);
	        	txtExpTime.setText("-- not set --");
	        }
	    });
		rdbtnShell.setSelected(true);
		group.add(rdbtnShell);
		
		pShComd.add(rdbtnShell);
		
		txtShellCommand.setText("Shell Command");
		txtShellCommand.setColumns(20);
		txtShellCommand.addFocusListener(new FocusListener() {
		      
			public void focusGained(FocusEvent e) { 
				txtShellCommand.setText("");
			}

	        public void focusLost(FocusEvent e) {
	        	
	        }
		});
		txtShellCommand.getDocument().addDocumentListener(new DocumentListener() {
			  
			public void changedUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void removeUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void insertUpdate(DocumentEvent e) {
				workOnUpdate();
			}

			public void workOnUpdate() {
				if (MADneSsLiteSupport.isValidShellCommand(txtShellCommand.getText())){
	        		expSetup.setShellExperiment(txtShellCommand.getText());
	        		txtIterations.setEnabled(true);
	        	}
			}
		});
		
		pShComd.add(txtShellCommand);
		pExperiments.add(pShComd);
		
		JPanel pExpTime = new JPanel();
		
		rdbtnTime = new JRadioButton("Iterate for (s)");
		rdbtnTime.addMouseListener( new TextBoxListener("Monitors the system for a given amount of time (seconds)"));
		rdbtnTime.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	txtShellCommand.setEnabled(false);
	        	txtShellCommand.setText("-- not set --");
	        	txtExpTime.setEnabled(true);
	        }
	    });
		pExpTime.add(rdbtnTime);
		group.add(rdbtnTime);
		
		txtExpTime.setText("Experiment Time (s)");
		txtExpTime.setEnabled(false);
		txtExpTime.setColumns(12);
		txtExpTime.addFocusListener(new FocusListener() {
		      
			public void focusGained(FocusEvent e) { 
				txtExpTime.setText("");
			}

	        public void focusLost(FocusEvent e) {
	        	
	        }
		});
		txtExpTime.getDocument().addDocumentListener(new DocumentListener() {
			  
			public void changedUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void removeUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void insertUpdate(DocumentEvent e) {
				workOnUpdate();
			}
 
			public void workOnUpdate() {
			    if (MADneSsLiteSupport.isInteger(txtExpTime.getText())){ 
			    	expSetup.setTimingExperiment(Integer.parseInt(txtExpTime.getText()));
					txtIterations.setEnabled(true);
				} else {
					expSetup.setTimingExperiment(0);
				}
			}
		});
		
		pExpTime.add(txtExpTime);
		pExperiments.add(pExpTime);
		
		JLabel lblObsInterval = new JLabel("Observation Interval");
		lblObsInterval.addMouseListener( new TextBoxListener("Defines the interval (milliseconds) between to observations"));
		
		txtObsInterval = new JTextField();
		txtObsInterval.setText("");
		txtObsInterval.setEnabled(true);
		txtObsInterval.setColumns(5);
		txtObsInterval.setMaximumSize(txtObsInterval.getPreferredSize());
		txtObsInterval.getDocument().addDocumentListener(new DocumentListener() {
			  
			public void changedUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void removeUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void insertUpdate(DocumentEvent e) {
				workOnUpdate();
			}
 
			public void workOnUpdate() {
				if(MADneSsLiteSupport.isInteger(txtObsInterval.getText()))
					expSetup.setObservationInterval(Integer.parseInt(txtObsInterval.getText()));
			}
		});
		
		JPanel pObsInt = new JPanel();
		pObsInt.add(lblObsInterval);
		pObsInt.add(txtObsInterval);
		pExperiments.add(pObsInt);
		
		JPanel pIterations = new JPanel();
		
		JLabel lblIterations = new JLabel("Iterations of the Experiment");
		lblIterations.addMouseListener( new TextBoxListener("Sets the number of iterations of the defined experiment"));
		pIterations.add(lblIterations);
		
		txtIterations.setText("Iterations");
		txtIterations.setEnabled(false);
		txtIterations.setColumns(6);
		txtIterations.addFocusListener(new FocusListener() {
		      
			public void focusGained(FocusEvent e) { 
				txtIterations.setText("");
			}

	        public void focusLost(FocusEvent e) {
	        	if (MADneSsLiteSupport.isInteger(txtIterations.getText())){
	        		expSetup.setExperimentIterations(Integer.parseInt(txtIterations.getText()));
	        	} else {
	        		txtIterations.setText("Iterations");
	        	}
	        }
		});
		txtIterations.getDocument().addDocumentListener(new DocumentListener() {
			  
			public void changedUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void removeUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void insertUpdate(DocumentEvent e) {
				workOnUpdate();
			}
 
			public void workOnUpdate() {
			    if (MADneSsLiteSupport.isInteger(txtIterations.getText())){ 
			    	expSetup.setExperimentIterations(Integer.parseInt(txtIterations.getText()));
				} else {
					expSetup.setExperimentIterations(0);
				}
			}
		});
		
		pIterations.add(txtIterations);
		
		pExperiments.add(pIterations);
		
		JPanel panelOkNo = new JPanel();
		ImageIcon imageOK = new ImageIcon(MADneSsLiteUI.class.getResource("/picOK.jpg"));
		Image imageSupport = imageOK.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageOK = new ImageIcon(imageSupport);
		ImageIcon imageNO = new ImageIcon(MADneSsLiteUI.class.getResource("/picNO.jpg"));
		imageSupport = imageNO.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageNO = new ImageIcon(imageSupport);
		JLabel labelOK = new JLabel("", imageOK, JLabel.CENTER);
		JLabel labelNO = new JLabel("", imageNO, JLabel.CENTER);
		labelOK.setEnabled(false);
		panelOkNo.add(labelNO);
		panelOkNo.add(labelOK);
		pExperiments.add(panelOkNo);
		expSetup.addObserver(new SetupObserver(1, labelNO, labelOK));
	}
	
	private void setOutput(){
		pOutput = new JPanel();
		pOutput.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Output Setup")));
		frame.getContentPane().add(pOutput, BorderLayout.EAST);
		pOutput.setLayout(new BoxLayout(pOutput, BoxLayout.Y_AXIS));
		
		ButtonGroup group = new ButtonGroup();
		rdbtnSingleFile = new JRadioButton("Single File");
		rdbtnSingleFile.addMouseListener( new TextBoxListener("Output is put in an unique file"));
		rdbtnSingleFile.setSelected(true);
		rdbtnSingleFile.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changEvent) {
				AbstractButton aButton = (AbstractButton)changEvent.getSource();
				if(aButton.getModel().isSelected()){
					expSetup.setOutputType(OutputType.SINGLE_FILE);
				}
			}});
		pOutput.add(rdbtnSingleFile);
		group.add(rdbtnSingleFile);
		
		rdbtnFileForEachExp = new JRadioButton("File for Each Experiment");
		rdbtnFileForEachExp.addMouseListener( new TextBoxListener("Output is put in a file for each experiment"));
		rdbtnFileForEachExp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changEvent) {
				AbstractButton aButton = (AbstractButton)changEvent.getSource();
				if(aButton.getModel().isSelected()){
					expSetup.setOutputType(OutputType.EACH_EXPERIMENT);
				}
			}});
		pOutput.add(rdbtnFileForEachExp);
		group.add(rdbtnFileForEachExp);
		
		rdbtnFileForEachInd = new JRadioButton("File for each Indicator");
		rdbtnFileForEachInd.addMouseListener( new TextBoxListener("Output is put in a file for each involved indicator"));
		rdbtnFileForEachInd.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changEvent) {
				AbstractButton aButton = (AbstractButton)changEvent.getSource();
				if(aButton.getModel().isSelected()){
					expSetup.setOutputType(OutputType.EACH_INDICATOR);
				}
			}});
		pOutput.add(rdbtnFileForEachInd);
		group.add(rdbtnFileForEachInd);
		
		rdbtnFileForEachIndExp = new JRadioButton("File for each indicator and experiment");
		rdbtnFileForEachIndExp.addMouseListener( new TextBoxListener("Output is put a file for each indicator in each experiment"));
		rdbtnFileForEachIndExp.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent changEvent) {
				AbstractButton aButton = (AbstractButton)changEvent.getSource();
				if(aButton.getModel().isSelected()){
					expSetup.setOutputType(OutputType.EACH_EXPERIMENT_EACH_INDICATOR);
				}
			}});
		pOutput.add(rdbtnFileForEachIndExp);
		group.add(rdbtnFileForEachIndExp);
		
		chckbxZipResults = new JCheckBox("Zip Results");
		chckbxZipResults.addMouseListener( new TextBoxListener("Sets if output is compressed in an unique .zip file"));
		chckbxZipResults.addActionListener(new ActionListener() {
		      
			public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		        expSetup.setOutputZipFlag(abstractButton.getModel().isSelected());
		    }
		
		});
		pOutput.add(chckbxZipResults);
		
		chckbxSendByMail = new JCheckBox("Send by Mail");
		chckbxSendByMail.addMouseListener(new TextBoxListener("Defines if the zipped output must be sent remotely to a given mail address"));
		
		txtInsertMailAddress = new JTextField();
		txtInsertMailAddress.setText("Insert Mail address Here");
		txtInsertMailAddress.setEnabled(false);
		txtInsertMailAddress.setColumns(20);
		txtInsertMailAddress.setMaximumSize(txtInsertMailAddress.getPreferredSize());
		txtInsertMailAddress.addFocusListener(new FocusListener() {
		      
			public void focusGained(FocusEvent e) { 
				txtInsertMailAddress.setText("");
			}

	        public void focusLost(FocusEvent e) { 
	        	if (!MADneSsLiteSupport.isValidEmailAddress(txtInsertMailAddress.getText())){
	        		txtInsertMailAddress.setText("Insert Mail address Here");
	        	}
	        }
	        
		});
		txtInsertMailAddress.getDocument().addDocumentListener(new DocumentListener() {
			  
			public void changedUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void removeUpdate(DocumentEvent e) {
				workOnUpdate();
			}
			  
			public void insertUpdate(DocumentEvent e) {
				workOnUpdate();
			}
 
			public void workOnUpdate() {
	        	expSetup.setMailAddress(txtInsertMailAddress.getText().trim());
			}
		});
		
		chckbxSendByMail.addActionListener(new ActionListener() {
		      
			public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		        expSetup.setMailAddressFlag(abstractButton.getModel().isSelected());
		        if(abstractButton.getModel().isSelected()){
		        	txtInsertMailAddress.setEnabled(true);
		        	chckbxZipResults.setSelected(true);
		        	chckbxZipResults.setEnabled(false);
		        } else {
		        	txtInsertMailAddress.setEnabled(false);
		        	txtInsertMailAddress.setText("Insert Mail Address");
		        	chckbxZipResults.setEnabled(true);
		        }
		    }
		
		});
		pOutput.add(chckbxSendByMail);
		
		JPanel pMail = new JPanel();
		pMail.add(txtInsertMailAddress);
		pMail.setBorder(new EmptyBorder (0, 10, 0, 0));
		pOutput.add(pMail);
		
		JPanel panelOkNo = new JPanel();
		ImageIcon imageOK = new ImageIcon(MADneSsLiteUI.class.getResource("/picOK.jpg"));
		Image imageSupport = imageOK.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageOK = new ImageIcon(imageSupport);
		ImageIcon imageNO = new ImageIcon(MADneSsLiteUI.class.getResource("/picNO.jpg"));
		imageSupport = imageNO.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageNO = new ImageIcon(imageSupport);
		JLabel labelOK = new JLabel("", imageOK, JLabel.CENTER);
		JLabel labelNO = new JLabel("", imageNO, JLabel.CENTER);
		labelOK.setEnabled(false);
		panelOkNo.add(labelNO);
		panelOkNo.add(labelOK);
		pOutput.add(panelOkNo);
		expSetup.addObserver(new SetupObserver(2, labelNO, labelOK));

	}
	
	private void setFooter(){
		pFooter = new JPanel();
		pFooter.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Experimental Campaign")));
		
		frame.getContentPane().add(pFooter, BorderLayout.SOUTH);
		pFooter.setLayout(new BoxLayout(pFooter, BoxLayout.Y_AXIS));
		
		JButton bStartExp = new JButton("Start Experiments");
		bStartExp.setFont(bStartExp.getFont().deriveFont((float) 18.0));
		bStartExp.addMouseListener( new TextBoxListener("Starts Experimental Campaign. A progress bar will be showed."));
		bStartExp.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) { 
				ProgressBar pBar = new ProgressBar(frame, "Experiments Progress", 0, expSetup.getExperimentIterations());
				expSetup.runExperiment(pBar);
			}
			
		});
		
		expSetup.addObserver(new RunExperimentObserver(bStartExp));
		
		JPanel pStartExp = new JPanel();
		
		pStartExp.add(bStartExp);
		
		pFooter.add(pStartExp);
	}

	public void setFrameVisible() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private class RunExperimentObserver implements Observer {
		
		private JButton button;
		
		public RunExperimentObserver(JButton button){
			this.button = button;
		}

		@Override
		public void update(Observable arg0, Object arg1) {
			button.setEnabled(expSetup.experimentReady());
		}
		
	}
	
	private class SetupObserver implements Observer {

		private int panelFlag;
		private JLabel lblNO;
		private JLabel lblOK;
		
		public SetupObserver(int panelFlag, JLabel lblNO, JLabel lblOK) {
			super();
			this.panelFlag = panelFlag;
			this.lblNO = lblNO;
			this.lblOK = lblOK;
		}
		
		@Override
		public void update(Observable arg0, Object arg1) {
			boolean flag = false;
			switch(panelFlag){
				case 0:
					flag = expSetup.isPreferenceSetupCompleted();
					break;
				case 1:
					flag = expSetup.isExperimentSetupCompleted();
					break;
				case 2:
					flag = expSetup.isOutputSetupCompleted();
					break;
			}
			if(flag){
				lblOK.setEnabled(true);
				lblNO.setEnabled(false);
			} else {
				lblOK.setEnabled(false);
				lblNO.setEnabled(true);
			}
				
		}
		
	}
	
	private class TextBoxListener extends MouseAdapter {

		private JDialog dialog;

        public TextBoxListener(String text) {
        	dialog = new JDialog(frame, true);
            dialog.setUndecorated(true);
            dialog.setResizable(false);
            dialog.setModal(false);
            dialog.add(new JLabel(text));
            dialog.pack();
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            Component c = (Component)me.getSource();
            int x = c.getLocationOnScreen().x + (c.getWidth()/2);
            int y = c.getLocationOnScreen().y + c.getHeight();
            dialog.setLocation(x,y);
            dialog.setVisible(true);
        }

        @Override
        public void mouseExited(MouseEvent me) {
        	dialog.setVisible(false);
        	dialog.dispose();
        	dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
        }

    }

}
