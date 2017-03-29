package ippoz.madness.lite.executable;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.Color;

public class testUI {

	private JFrame frmRclMonitoringTool;
	private JTextField txtInsertMailAddress;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testUI window = new testUI();
					window.frmRclMonitoringTool.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRclMonitoringTool = new JFrame();
		frmRclMonitoringTool.setBackground(new Color(240, 240, 240));
		frmRclMonitoringTool.setTitle("RCL Monitoring Tool");
		frmRclMonitoringTool.setResizable(false);
		frmRclMonitoringTool.setBounds(100, 100, 450, 300);
		frmRclMonitoringTool.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmRclMonitoringTool.getContentPane().add(panel, BorderLayout.WEST);
		
		JLabel lblPreferences = new JLabel("Preferences");
		panel.add(lblPreferences);
		
		JLayeredPane layeredPane = new JLayeredPane();
		panel.add(layeredPane);
		
		JRadioButton rdbtnJ = new JRadioButton("j");
		panel.add(rdbtnJ);
		
		JPanel pHeader = new JPanel();
		frmRclMonitoringTool.getContentPane().add(pHeader, BorderLayout.NORTH);
		
		JLabel lblRclMonitoringTool = new JLabel("RCL Monitoring Tool");
		pHeader.add(lblRclMonitoringTool);
		
		JPanel pFooter = new JPanel();
		frmRclMonitoringTool.getContentPane().add(pFooter, BorderLayout.SOUTH);
		
		JTextPane txtpnLogbox = new JTextPane();
		txtpnLogbox.setText("LogBox");
		pFooter.add(txtpnLogbox);
		
		JButton btnStart = new JButton("Start");
		pFooter.add(btnStart);
		
		JPanel panel_3 = new JPanel();
		frmRclMonitoringTool.getContentPane().add(panel_3, BorderLayout.EAST);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblOutput = new JLabel("Output");
		panel_3.add(lblOutput);
		
		JRadioButton rdbtnSingleFile = new JRadioButton("Single File");
		panel_3.add(rdbtnSingleFile);
		
		JRadioButton rdbtnFileForEach = new JRadioButton("File for Each Experiment");
		panel_3.add(rdbtnFileForEach);
		
		JRadioButton rdbtnFileForEach_1 = new JRadioButton("File for each Indicator");
		panel_3.add(rdbtnFileForEach_1);
		
		JRadioButton rdbtnFileForEach_2 = new JRadioButton("File for each indicator and experiment");
		panel_3.add(rdbtnFileForEach_2);
		
		JCheckBox chckbxZipResults = new JCheckBox("Zip Results");
		panel_3.add(chckbxZipResults);
		
		JCheckBox chckbxSendByMail = new JCheckBox("Send by Mail");
		panel_3.add(chckbxSendByMail);
		
		txtInsertMailAddress = new JTextField();
		txtInsertMailAddress.setText("Insert Mail address Here");
		panel_3.add(txtInsertMailAddress);
		txtInsertMailAddress.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		frmRclMonitoringTool.getContentPane().add(panel_4, BorderLayout.CENTER);
		
		JLabel lblExperimentalCampaign = new JLabel("Experimental Campaign");
		panel_4.add(lblExperimentalCampaign);
		frmRclMonitoringTool.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{frmRclMonitoringTool.getContentPane(), panel, lblPreferences, layeredPane, rdbtnJ, pHeader, lblRclMonitoringTool, pFooter, txtpnLogbox, btnStart, panel_3, lblOutput, rdbtnSingleFile, rdbtnFileForEach, rdbtnFileForEach_1, rdbtnFileForEach_2, chckbxZipResults, chckbxSendByMail, txtInsertMailAddress, panel_4, lblExperimentalCampaign}));
	}

}
