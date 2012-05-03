package com.aerodynelabs.habtk.ui;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 * A panel to display tracking information
 * @author Ethan Harstad
 *
 */
@SuppressWarnings("serial")
public class TrackingPanel extends JPanel {
	
	private JTextField curAlt;
	private JTextField curLon;
	private JTextField curLat;
	private JTextField burAlt;
	private JTextField burLon;
	private JTextField burLat;
	private JTextField lndAlt;
	private JTextField lndLon;
	private JTextField lndLat;
	
	public TrackingPanel() {
		super();
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		// Curent Values
		JLabel lblCurrent = new JLabel("Current");
		springLayout.putConstraint(SpringLayout.NORTH, lblCurrent, 6, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblCurrent, 10, SpringLayout.WEST, this);
		add(lblCurrent);
		JSeparator sepCurrent = new JSeparator();
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, sepCurrent, 0, SpringLayout.VERTICAL_CENTER, lblCurrent);
		springLayout.putConstraint(SpringLayout.WEST, sepCurrent, 6, SpringLayout.EAST, lblCurrent);
		springLayout.putConstraint(SpringLayout.EAST, sepCurrent, -6, SpringLayout.EAST, this);
		add(sepCurrent);
		
		JLabel lblCurLat = new JLabel("Latitude:");
		JLabel lblCurLon = new JLabel("Longitude:");
		JLabel lblCurAlt = new JLabel("Altitude:");
		curAlt = new JTextField();
		curAlt.setEditable(false);
		curLon = new JTextField();
		curLon.setEditable(false);
		curLat = new JTextField();
		curLat.setEditable(false);
		
		springLayout.putConstraint(SpringLayout.WEST, lblCurLon, 6, SpringLayout.WEST, lblCurrent);
		springLayout.putConstraint(SpringLayout.EAST, lblCurLat, 0, SpringLayout.EAST, lblCurLon);
		springLayout.putConstraint(SpringLayout.EAST, lblCurAlt, 0, SpringLayout.EAST, lblCurLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblCurLat, 0, SpringLayout.BASELINE, curLat);
		springLayout.putConstraint(SpringLayout.BASELINE, lblCurLon, 0, SpringLayout.BASELINE, curLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblCurAlt, 0, SpringLayout.BASELINE, curAlt);
		springLayout.putConstraint(SpringLayout.NORTH, curLat, 6, SpringLayout.SOUTH, lblCurrent);
		springLayout.putConstraint(SpringLayout.NORTH, curLon, 6, SpringLayout.SOUTH, curLat);
		springLayout.putConstraint(SpringLayout.NORTH, curAlt, 6, SpringLayout.SOUTH, curLon);
		springLayout.putConstraint(SpringLayout.WEST, curLat, 6, SpringLayout.EAST, lblCurLat);
		springLayout.putConstraint(SpringLayout.WEST, curLon, 6, SpringLayout.EAST, lblCurLon);
		springLayout.putConstraint(SpringLayout.WEST, curAlt, 6, SpringLayout.EAST, lblCurAlt);
		springLayout.putConstraint(SpringLayout.EAST, curLat, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, curLon, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, curAlt, -6, SpringLayout.EAST, this);
		
		add(lblCurLat);
		add(lblCurLon);
		add(lblCurAlt);
		add(curLat);
		add(curLon);
		add(curAlt);
		
		// Burst Values
		JLabel lblBurst = new JLabel("Burst");
		springLayout.putConstraint(SpringLayout.NORTH, lblBurst, 6, SpringLayout.SOUTH, curAlt);
		springLayout.putConstraint(SpringLayout.WEST, lblBurst, 10, SpringLayout.WEST, this);
		add(lblBurst);
		JSeparator sepBurst = new JSeparator();
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, sepBurst, 0, SpringLayout.VERTICAL_CENTER, lblBurst);
		springLayout.putConstraint(SpringLayout.WEST, sepBurst, 6, SpringLayout.EAST, lblBurst);
		springLayout.putConstraint(SpringLayout.EAST, sepBurst, -6, SpringLayout.EAST, this);
		add(sepBurst);
		
		JLabel lblBurLat = new JLabel("Latitude:");
		JLabel lblBurLon = new JLabel("Longitude:");
		JLabel lblBurAlt = new JLabel("Altitude:");
		burLat = new JTextField();
		burLat.setEditable(false);
		burLon = new JTextField();
		burLon.setEditable(false);
		burAlt = new JTextField();
		burAlt.setEditable(false);
		
		springLayout.putConstraint(SpringLayout.WEST, lblBurLon, 6, SpringLayout.WEST, lblBurst);
		springLayout.putConstraint(SpringLayout.EAST, lblBurLat, 0, SpringLayout.EAST, lblBurLon);
		springLayout.putConstraint(SpringLayout.EAST, lblBurAlt, 0, SpringLayout.EAST, lblBurLon);
		springLayout.putConstraint(SpringLayout.WEST, burLat, 6, SpringLayout.EAST, lblBurLat);
		springLayout.putConstraint(SpringLayout.WEST, burLon, 6, SpringLayout.EAST, lblBurLon);
		springLayout.putConstraint(SpringLayout.WEST, burAlt, 6, SpringLayout.EAST, lblBurAlt);
		springLayout.putConstraint(SpringLayout.EAST, burLat, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, burLon, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, burAlt, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.NORTH, burLat, 6, SpringLayout.SOUTH, lblBurst);
		springLayout.putConstraint(SpringLayout.NORTH, burLon, 6, SpringLayout.SOUTH, burLat);
		springLayout.putConstraint(SpringLayout.NORTH, burAlt, 6, SpringLayout.SOUTH, burLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblBurLat, 0, SpringLayout.BASELINE, burLat);
		springLayout.putConstraint(SpringLayout.BASELINE, lblBurLon, 0, SpringLayout.BASELINE, burLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblBurAlt, 0, SpringLayout.BASELINE, burAlt);
		
		add(lblBurLat);
		add(lblBurLon);
		add(lblBurAlt);
		add(burLat);
		add(burLon);
		add(burAlt);
		
		// Landing values
		JLabel lblLanding = new JLabel("Landing");
		springLayout.putConstraint(SpringLayout.NORTH, lblLanding, 6, SpringLayout.SOUTH, burAlt);
		springLayout.putConstraint(SpringLayout.WEST, lblLanding, 10, SpringLayout.WEST, this);
		add(lblLanding);
		JSeparator sepLanding = new JSeparator();
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, sepLanding, 0, SpringLayout.VERTICAL_CENTER, lblLanding);
		springLayout.putConstraint(SpringLayout.WEST, sepLanding, 6, SpringLayout.EAST, lblLanding);
		springLayout.putConstraint(SpringLayout.EAST, sepLanding, -6, SpringLayout.EAST, this);
		add(sepLanding);
		
		JLabel lblLndLat = new JLabel("Latitude:");
		JLabel lblLndLon = new JLabel("Longitude:");
		JLabel lblLndAlt = new JLabel("Altitude:");
		lndLat = new JTextField();
		lndLat.setEditable(false);
		lndLon = new JTextField();
		lndLon.setEditable(false);
		lndAlt = new JTextField();
		lndAlt.setEditable(false);
		
		springLayout.putConstraint(SpringLayout.WEST, lblLndLon, 6, SpringLayout.WEST, lblLanding);
		springLayout.putConstraint(SpringLayout.EAST, lblLndLat, 0, SpringLayout.EAST, lblLndLon);
		springLayout.putConstraint(SpringLayout.EAST, lblLndAlt, 0, SpringLayout.EAST, lblLndLon);
		springLayout.putConstraint(SpringLayout.WEST, lndLat, 6, SpringLayout.EAST, lblLndLat);
		springLayout.putConstraint(SpringLayout.WEST, lndLon, 6, SpringLayout.EAST, lblLndLon);
		springLayout.putConstraint(SpringLayout.WEST, lndAlt, 6, SpringLayout.EAST, lblLndAlt);
		springLayout.putConstraint(SpringLayout.NORTH, lndLat, 6, SpringLayout.SOUTH, lblLanding);
		springLayout.putConstraint(SpringLayout.NORTH, lndLon, 6, SpringLayout.SOUTH, lndLat);
		springLayout.putConstraint(SpringLayout.NORTH, lndAlt, 6, SpringLayout.SOUTH, lndLon);
		springLayout.putConstraint(SpringLayout.EAST, lndLat, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, lndLon, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, lndAlt, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.BASELINE, lblLndLat, 0, SpringLayout.BASELINE, lndLat);
		springLayout.putConstraint(SpringLayout.BASELINE, lblLndLon, 0, SpringLayout.BASELINE, lndLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblLndAlt, 0, SpringLayout.BASELINE, lndAlt);
		
		add(lblLndLat);
		add(lblLndLon);
		add(lblLndAlt);
		add(lndLat);
		add(lndLon);
		add(lndAlt);
		
	}
}
