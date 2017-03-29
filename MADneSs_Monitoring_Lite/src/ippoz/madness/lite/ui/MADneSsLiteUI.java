/**
 * 
 */
package ippoz.madness.lite.ui;

import ippoz.madness.lite.experiment.ExperimentSetup;
import ippoz.madness.lite.support.MADneSsLiteSupport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
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
	
	private void setHeader(){
		
	}
	
	private void setPreferences(){
		
	}
	
	private void setExperiments(){
		
	}
	
	private void setOutput(){
		pOutput = new JPanel();
		frame.getContentPane().add(pOutput, BorderLayout.EAST);
		pOutput.setLayout(new BoxLayout(pOutput, BoxLayout.Y_AXIS));
		
		JLabel lblOutput = new JLabel("Output");
		pOutput.add(lblOutput);
		
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
		txtInsertMailAddress.setColumns(10);
		txtInsertMailAddress.addFocusListener(new FocusListener() {
		      
			public void focusGained(FocusEvent e) { }

	        public void focusLost(FocusEvent e) {
	        	if (MADneSsLiteSupport.isValidEmailAddress(txtInsertMailAddress.getText())){
	        			expSetup.setMailAddress(txtInsertMailAddress.getText());
	        	} else {
	        		JOptionPane.showMessageDialog(null, "Error: Please enter a valid E-Mail address", "Mail Error", JOptionPane.ERROR_MESSAGE);
	        		txtInsertMailAddress.setEnabled(false);
		        	txtInsertMailAddress.setText("Insert Mail Address");
		        	chckbxSendByMail.setSelected(false);
	        	}
	        }
		});
		
		chckbxSendByMail.addActionListener(new ActionListener() {
		      
			public void actionPerformed(ActionEvent actionEvent) {
		        AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
		        if(abstractButton.getModel().isSelected()){
		        	txtInsertMailAddress.setEnabled(true);
		        } else {
		        	txtInsertMailAddress.setEnabled(false);
		        	txtInsertMailAddress.setText("Insert Mail Address");
		        }
		    }
		
		});
		pOutput.add(chckbxSendByMail);
				
		pOutput.add(txtInsertMailAddress);
	}
	
	private void setFooter(){
		
	}

	public void setFrameVisible() {
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
}
