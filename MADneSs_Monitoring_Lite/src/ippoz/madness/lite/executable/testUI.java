package ippoz.madness.lite.executable;

import ippoz.madness.lite.ui.ProgressBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				executeBar();
			}
		});
		frmRclMonitoringTool.getContentPane().add(btnNewButton, BorderLayout.CENTER);

	}
	
	ProgressBar pBar;
	
	private void executeBar() {
		pBar = new ProgressBar(frmRclMonitoringTool, "Test", 0, 10);
		new Thread(new Runnable() {
			public void run() {
				pBar.showBar();
			}
		}).start();
		new Thread(new Runnable() {
			public void run() {
				for(int i=0;i<10;i++){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pBar.moveNext();
				}
				pBar.deleteFrame();
			}
		}).start();
	}

}
