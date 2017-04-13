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
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
		frame.setBackground(new Color(240, 240, 240));
		frame.setTitle("RCL Monitoring Tool");
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JLabel lblOutput = new JLabel("RCL Monitoring Tool");
		lblOutput.setFont(lblOutput.getFont().deriveFont(new Float(20)));
		lblOutput.setBorder(new EmptyBorder(20, 20, 20, 20));
		pHeader.add(lblOutput);
		
		pSubHeader = new JPanel();
		pSubHeader.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Preferences")));
		
		JButton bResetPref = new JButton("Reset Preferences");
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
				expSetup.writePreferences(pFile);
			}
			
		});
		
		pSubHeader.add(bSavePref);
		
		pHeader.add(pSubHeader, BorderLayout.CENTER);
		
		
	}

	private void setPreferences(){
		pPreferences = new JPanel();
		pPreferences.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("General")));
		frame.getContentPane().add(pPreferences, BorderLayout.WEST);
		pPreferences.setLayout(new BoxLayout(pPreferences, BoxLayout.Y_AXIS));
		
		JPanel pExpName = new JPanel();
		
		JLabel lblExpName = new JLabel("Experiment Name");
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
		
		JLabel lblObsInterval = new JLabel("Observation Interval");
		pPreferences.add(lblObsInterval);
		
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
		pPreferences.add(pObsInt);
		
		JPanel pIndPreferences = new JPanel();
		
		JLabel lblIndPreferences = new JLabel("Indicator Preferences");
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
		        }
			}
			
		});
		pIndPreferences.add(bIndPref);
		
		pPreferences.add(pIndPreferences);
		
		lblIndPrefFile = new JLabel("Indicator Preferences not set");
		pPreferences.add(lblIndPrefFile);
		
		JPanel panelOkNo = new JPanel();
		ImageIcon imageOK = new ImageIcon("images/picOK.jpg");
		Image imageSupport = imageOK.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageOK = new ImageIcon(imageSupport);
		ImageIcon imageNO = new ImageIcon("images/picNO.jpg");
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
		rdbtnShell = new JRadioButton("Execute shell command");
		rdbtnShell.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnShell.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	txtShellCommand.setEnabled(true);
	        	txtExpTime.setEnabled(false);
	        }
	    });
		rdbtnShell.setSelected(true);
		pExperiments.add(rdbtnShell);
		group.add(rdbtnShell);
		
		txtShellCommand.setText("Shell Command");
		txtShellCommand.setMaximumSize(txtShellCommand.getPreferredSize());
		txtShellCommand.addFocusListener(new FocusListener() {
		      
			public void focusGained(FocusEvent e) { 
				txtShellCommand.setText("");
			}

	        public void focusLost(FocusEvent e) {
	        	if (MADneSsLiteSupport.isValidShellCommand(txtShellCommand.getText())){
	        		expSetup.setShellExperiment(txtShellCommand.getText());
	        		txtIterations.setEnabled(false);
	        	} else {
	        		txtShellCommand.setText("Shell Command");
	        	}
	        }
		});
		
		pExperiments.add(txtShellCommand);
		
		JPanel pExpTime = new JPanel();
		
		rdbtnTime = new JRadioButton("Iterate for (s)");
		rdbtnTime.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	txtShellCommand.setEnabled(false);
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
	        	if (MADneSsLiteSupport.isInteger(txtExpTime.getText())){
	        		expSetup.setTimingExperiment(Integer.parseInt(txtExpTime.getText()));
	        		txtIterations.setEnabled(true);
	        	} else {
	        		txtExpTime.setText("Experiment Time (s)");
	        	}
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
		
		JPanel pIterations = new JPanel();
		
		JLabel lblIterations = new JLabel("Iterations of the Experiment");
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
		ImageIcon imageOK = new ImageIcon("images/picOK.jpg");
		Image imageSupport = imageOK.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageOK = new ImageIcon(imageSupport);
		ImageIcon imageNO = new ImageIcon("images/picNO.jpg");
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
		chckbxZipResults.addActionListener(new ActionListener() {
		      
			public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		        expSetup.setOutputZipFlag(abstractButton.getModel().isSelected());
		    }
		
		});
		pOutput.add(chckbxZipResults);
		
		chckbxSendByMail = new JCheckBox("Send by Mail");
		
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
		ImageIcon imageOK = new ImageIcon("images/picOK.jpg");
		Image imageSupport = imageOK.getImage().getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
		imageOK = new ImageIcon(imageSupport);
		ImageIcon imageNO = new ImageIcon("images/picNO.jpg");
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
		pFooter.setBorder(new EmptyBorder(20, 20, 20, 20));
		frame.getContentPane().add(pFooter, BorderLayout.SOUTH);
		pFooter.setLayout(new BoxLayout(pFooter, BoxLayout.Y_AXIS));
		
		JButton bStartExp = new JButton("Start Experiments");
		bStartExp.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) { 
				expSetup.runExperiment();
			}
			
		});
		
		expSetup.addObserver(new RunExperimentObserver(bStartExp));
		
		pFooter.add(bStartExp);
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
	
}
