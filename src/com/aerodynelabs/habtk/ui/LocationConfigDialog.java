package com.aerodynelabs.habtk.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.aerodynelabs.habtk.tracking.LocationService;

@SuppressWarnings("serial")
public class LocationConfigDialog extends JDialog {

	private boolean accepted = false;
	
	private JTextField fLatitude;
	private JTextField fLongitude;
	private JTextField fAltitude;
	private LocationService locationService;
	
	public LocationConfigDialog(LocationService service) {
		locationService = service;
		
		setTitle("Setup Location");
		setModal(true);
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		Container pane = getContentPane();
		
		JLabel lLatitude = new JLabel("Latitude:");
		fLatitude = new JTextField(10);
		layout.putConstraint(SpringLayout.NORTH, fLatitude, 6, SpringLayout.NORTH, pane);
		layout.putConstraint(SpringLayout.EAST, fLatitude, -6, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.BASELINE, lLatitude, 0, SpringLayout.BASELINE, fLatitude);
		layout.putConstraint(SpringLayout.EAST, lLatitude, -6, SpringLayout.WEST, fLatitude);
		add(lLatitude);
		add(fLatitude);
		
		JLabel lLongitude = new JLabel("Longitude:");
		fLongitude = new JTextField(10);
		layout.putConstraint(SpringLayout.NORTH, fLongitude, 6, SpringLayout.SOUTH, fLatitude);
		layout.putConstraint(SpringLayout.WEST, lLongitude, 6, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.WEST, fLongitude, 6, SpringLayout.EAST, lLongitude);
		layout.putConstraint(SpringLayout.EAST, pane, 6, SpringLayout.EAST, fLongitude);
		layout.putConstraint(SpringLayout.BASELINE, lLongitude, 0, SpringLayout.BASELINE, fLongitude);
		add(lLongitude);
		add(fLongitude);
		
		JLabel lAltitude = new JLabel("Altitude:");
		fAltitude = new JTextField(10);
		layout.putConstraint(SpringLayout.NORTH, fAltitude, 6, SpringLayout.SOUTH, fLongitude);
		layout.putConstraint(SpringLayout.EAST, fAltitude, -6, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.BASELINE, lAltitude, 0, SpringLayout.BASELINE, fAltitude);
		layout.putConstraint(SpringLayout.EAST, lAltitude, -6, SpringLayout.WEST, fAltitude);
		add(lAltitude);
		add(fAltitude);
		
		JLabel lGps = new JLabel("GPS");
		JSeparator sGps = new JSeparator();
		layout.putConstraint(SpringLayout.WEST, lGps, 6, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.WEST, sGps, 6, SpringLayout.EAST, lGps);
		layout.putConstraint(SpringLayout.EAST, sGps, -6, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.NORTH, lGps, 6, SpringLayout.SOUTH, fAltitude);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, sGps, 0, SpringLayout.VERTICAL_CENTER, lGps);
		add(lGps);
		add(sGps);
		
		JLabel lPort = new JLabel("Port:");
		JComboBox<String> fPort = new JComboBox<String>();
		//TODO Populate fPort with detected port names
		layout.putConstraint(SpringLayout.EAST, fPort, -6, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.EAST, lPort, -6, SpringLayout.WEST, fPort);
		layout.putConstraint(SpringLayout.NORTH, fPort, 6, SpringLayout.SOUTH, lGps);
		layout.putConstraint(SpringLayout.BASELINE, lPort, 0, SpringLayout.BASELINE, fPort);
		add(lPort);
		add(fPort);
		
		String[] baudRates = {"1200", "4800", "9600", "19200"};
		JLabel lBaud = new JLabel("Baud Rate:");
		JComboBox<String> fBaud = new JComboBox<String>(baudRates);
		layout.putConstraint(SpringLayout.EAST, fBaud, -6, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.EAST, lBaud, -6, SpringLayout.WEST, fBaud);
		layout.putConstraint(SpringLayout.NORTH, fBaud, 6, SpringLayout.SOUTH, fPort);
		layout.putConstraint(SpringLayout.BASELINE, lBaud, 0, SpringLayout.BASELINE, fBaud);
		add(lBaud);
		add(fBaud);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accepted = false;
				dispose();
			}
		});
		JButton ok = new JButton("Accept");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					double lat = Double.parseDouble(fLatitude.getText());
					double lon = Double.parseDouble(fLongitude.getText());
					double alt = Double.parseDouble(fAltitude.getText());
					locationService.setStaticLocation(lat, lon, alt);
					accepted = true;
				} catch(NumberFormatException nfe) {
					nfe.printStackTrace();
					accepted = false;
				}
				dispose();
			}
		});
		layout.putConstraint(SpringLayout.NORTH, ok, 6, SpringLayout.SOUTH, fBaud);
		layout.putConstraint(SpringLayout.SOUTH, pane, 6, SpringLayout.SOUTH, ok);
		layout.putConstraint(SpringLayout.NORTH, cancel, 0, SpringLayout.NORTH, ok);
		layout.putConstraint(SpringLayout.EAST, ok, -6, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.EAST, cancel, -6, SpringLayout.WEST, ok);
		add(cancel);
		add(ok);
		
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
		setVisible(true);
	}
	
	public boolean wasAccepted() {
		return accepted;
	}

}
