package ippoz.madness.lite.executable;

import ippoz.madness.lite.experiment.ExperimentSetup;
import ippoz.madness.lite.ui.MADneSsLiteUI;

import java.awt.EventQueue;

public class MonitoringLiteUI {

	private MADneSsLiteUI mUI;
	private ExperimentSetup expSetup;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MonitoringLiteUI window = new MonitoringLiteUI();
					window.setFrameVisible();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void setFrameVisible() {
		mUI.setFrameVisible();
	}

	/**
	 * Create the application.
	 */
	public MonitoringLiteUI() {
		expSetup = new ExperimentSetup();
		mUI = new MADneSsLiteUI(expSetup);
	}

}
