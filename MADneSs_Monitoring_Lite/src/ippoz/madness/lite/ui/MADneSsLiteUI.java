/**
 * 
 */
package ippoz.madness.lite.ui;

import ippoz.madness.lite.experiment.ExperimentSetup;
import ippoz.madness.lite.support.MADneSsLiteSupport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractButton;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Tommy
 *
 */
public class MADneSsLiteUI {

	private ExperimentSetup expSetup;
	
	private JFrame frame;
	private JPanel pPreferences;
	private JPanel pExperiments;
	private JPanel pOutput;
	private JPanel pFooter;
	
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
	
	private void loadPreferencesFile(File selectedFile) {
		// TODO Auto-generated method stub
		
	}
	
	private void setHeader(){
		pPreferences = new JPanel();
		pPreferences.setBorder(new EmptyBorder(20, 20, 20, 20));
		frame.getContentPane().add(pPreferences, BorderLayout.NORTH);
		//pPreferences.setLayout(new BorderLayout(pPreferences, BoxLayout.Y_AXIS));
		
		JLabel lblOutput = new JLabel("RCL Monitoring Tool");
		lblOutput.setFont(lblOutput.getFont().deriveFont(new Float(20)));
		pPreferences.add(lblOutput, BorderLayout.CENTER);
		
	}

	private void setPreferences(){
		pPreferences = new JPanel();
		pPreferences.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder("Preferences")));
		frame.getContentPane().add(pPreferences, BorderLayout.WEST);
		pPreferences.setLayout(new BoxLayout(pPreferences, BoxLayout.Y_AXIS));
		
		JPanel pLoadPreferences = new JPanel();
		
		JLabel lblLoadPreferences = new JLabel("Existing Preferences");
		pLoadPreferences.add(lblLoadPreferences);
		
		JButton bLoadPref = new JButton("Load");
		bLoadPref.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) { 
				JFileChooser fileChooser = new JFileChooser();
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	loadPreferencesFile(fileChooser.getSelectedFile());
		        }
			}
			
		});
		pLoadPreferences.add(bLoadPref);
		
		pPreferences.add(pLoadPreferences);
		
		JLabel lblOutFolder = new JLabel("Output Folder");
		pPreferences.add(lblOutFolder);
		
		final JTextField txtOutputFolder = new JTextField();
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
		
		JPanel pMail = new JPanel();
		pMail.add(lblOutFolder);
		pMail.add(txtOutputFolder);
		pPreferences.add(pMail);
		
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
		
		final JTextField txtShellCommand = new JTextField();
		final JTextField txtExpTime = new JTextField();
		final JTextField txtIterations = new JTextField();
		
		ButtonGroup group = new ButtonGroup();
		JRadioButton rdbtnShell = new JRadioButton("Execute shell command");
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
		//txtShellCommand.setColumns(30);
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
		
		JRadioButton rdbtnTime = new JRadioButton("Iterate for");
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
		JRadioButton rdbtnSingleFile = new JRadioButton("Single File");
		rdbtnSingleFile.setSelected(true);
		pOutput.add(rdbtnSingleFile);
		group.add(rdbtnSingleFile);
		
		JRadioButton rdbtnFileForEach = new JRadioButton("File for Each Experiment");
		pOutput.add(rdbtnFileForEach);
		group.add(rdbtnFileForEach);
		
		JRadioButton rdbtnFileForEach_1 = new JRadioButton("File for each Indicator");
		pOutput.add(rdbtnFileForEach_1);
		group.add(rdbtnFileForEach_1);
		
		JRadioButton rdbtnFileForEach_2 = new JRadioButton("File for each indicator and experiment");
		pOutput.add(rdbtnFileForEach_2);
		group.add(rdbtnFileForEach_2);
		
		JCheckBox chckbxZipResults = new JCheckBox("Zip Results");
		chckbxZipResults.addActionListener(new ActionListener() {
		      
			public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		        expSetup.setOutputZipFlag(abstractButton.getModel().isSelected());
		    }
		
		});
		pOutput.add(chckbxZipResults);
		
		final JCheckBox chckbxSendByMail = new JCheckBox("Send by Mail");
		
		final JTextField txtInsertMailAddress = new JTextField();
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
		        } else {
		        	txtInsertMailAddress.setEnabled(false);
		        	txtInsertMailAddress.setText("Insert Mail Address");
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
		
		JButton bLoadPref = new JButton("Start Experiments");
		bLoadPref.addActionListener(new ActionListener() { 
			
			public void actionPerformed(ActionEvent e) { 
				// TODO
			}
			
		});
		
		expSetup.addObserver(new Obser);
		
		pFooter.add(bLoadPref);
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
