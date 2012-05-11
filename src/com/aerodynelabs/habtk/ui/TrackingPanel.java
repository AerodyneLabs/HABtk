package com.aerodynelabs.habtk.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.Timer;

import com.aerodynelabs.habtk.tracking.PositionEvent;
import com.aerodynelabs.habtk.tracking.PositionListener;

/**
 * A panel to display tracking information
 * @author Ethan Harstad
 *
 */
@SuppressWarnings("serial")
public class TrackingPanel extends JPanel implements PositionListener, ActionListener {
	
	private JTextField curAlt;
	private JTextField curLon;
	private JTextField curLat;
	private JTextField curTime;
	private JTextField burAlt;
	private JTextField burLon;
	private JTextField burLat;
	private JTextField burTime;
	private JTextField lndAlt;
	private JTextField lndLon;
	private JTextField lndLat;
	private JTextField lndTime;
	
	private Date time, cTime, bTime, lTime;
	private Timer timer;
	
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
		JLabel lblCurTime = new JLabel("Time:");
		curAlt = new JTextField();
		curAlt.setEditable(false);
		curLon = new JTextField();
		curLon.setEditable(false);
		curLat = new JTextField();
		curLat.setEditable(false);
		curTime = new JTextField();
		curTime.setEditable(false);
		
		springLayout.putConstraint(SpringLayout.WEST, lblCurLon, 6, SpringLayout.WEST, lblCurrent);
		springLayout.putConstraint(SpringLayout.EAST, lblCurLat, 0, SpringLayout.EAST, lblCurLon);
		springLayout.putConstraint(SpringLayout.EAST, lblCurAlt, 0, SpringLayout.EAST, lblCurLon);
		springLayout.putConstraint(SpringLayout.EAST, lblCurTime, 0, SpringLayout.EAST, lblCurLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblCurLat, 0, SpringLayout.BASELINE, curLat);
		springLayout.putConstraint(SpringLayout.BASELINE, lblCurLon, 0, SpringLayout.BASELINE, curLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblCurAlt, 0, SpringLayout.BASELINE, curAlt);
		springLayout.putConstraint(SpringLayout.BASELINE, lblCurTime, 0, SpringLayout.BASELINE, curTime);
		springLayout.putConstraint(SpringLayout.NORTH, curLat, 6, SpringLayout.SOUTH, curTime);
		springLayout.putConstraint(SpringLayout.NORTH, curLon, 6, SpringLayout.SOUTH, curLat);
		springLayout.putConstraint(SpringLayout.NORTH, curAlt, 6, SpringLayout.SOUTH, curLon);
		springLayout.putConstraint(SpringLayout.NORTH, curTime, 6, SpringLayout.SOUTH, lblCurrent);
		springLayout.putConstraint(SpringLayout.WEST, curLat, 6, SpringLayout.EAST, lblCurLat);
		springLayout.putConstraint(SpringLayout.WEST, curLon, 6, SpringLayout.EAST, lblCurLon);
		springLayout.putConstraint(SpringLayout.WEST, curAlt, 6, SpringLayout.EAST, lblCurAlt);
		springLayout.putConstraint(SpringLayout.WEST, curTime, 6, SpringLayout.EAST, lblCurTime);
		springLayout.putConstraint(SpringLayout.EAST, curLat, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, curLon, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, curAlt, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, curTime, -6, SpringLayout.EAST, this);
		
		add(lblCurLat);
		add(lblCurLon);
		add(lblCurAlt);
		add(lblCurTime);
		add(curLat);
		add(curLon);
		add(curAlt);
		add(curTime);
		
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
		
		JLabel lblBurTime = new JLabel("Time:");
		JLabel lblBurLat = new JLabel("Latitude:");
		JLabel lblBurLon = new JLabel("Longitude:");
		JLabel lblBurAlt = new JLabel("Altitude:");
		burTime = new JTextField();
		burTime.setEditable(false);
		burLat = new JTextField();
		burLat.setEditable(false);
		burLon = new JTextField();
		burLon.setEditable(false);
		burAlt = new JTextField();
		burAlt.setEditable(false);
		
		springLayout.putConstraint(SpringLayout.WEST, lblBurLon, 6, SpringLayout.WEST, lblBurst);
		springLayout.putConstraint(SpringLayout.EAST, lblBurTime, 0, SpringLayout.EAST, lblBurLon);
		springLayout.putConstraint(SpringLayout.EAST, lblBurLat, 0, SpringLayout.EAST, lblBurLon);
		springLayout.putConstraint(SpringLayout.EAST, lblBurAlt, 0, SpringLayout.EAST, lblBurLon);
		springLayout.putConstraint(SpringLayout.WEST, burTime, 6, SpringLayout.EAST, lblBurTime);
		springLayout.putConstraint(SpringLayout.WEST, burLat, 6, SpringLayout.EAST, lblBurLat);
		springLayout.putConstraint(SpringLayout.WEST, burLon, 6, SpringLayout.EAST, lblBurLon);
		springLayout.putConstraint(SpringLayout.WEST, burAlt, 6, SpringLayout.EAST, lblBurAlt);
		springLayout.putConstraint(SpringLayout.EAST, burTime, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, burLat, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, burLon, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, burAlt, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.NORTH, burTime, 6, SpringLayout.SOUTH, lblBurst);
		springLayout.putConstraint(SpringLayout.NORTH, burLat, 6, SpringLayout.SOUTH, burTime);
		springLayout.putConstraint(SpringLayout.NORTH, burLon, 6, SpringLayout.SOUTH, burLat);
		springLayout.putConstraint(SpringLayout.NORTH, burAlt, 6, SpringLayout.SOUTH, burLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblBurTime, 0, SpringLayout.BASELINE, burTime);
		springLayout.putConstraint(SpringLayout.BASELINE, lblBurLat, 0, SpringLayout.BASELINE, burLat);
		springLayout.putConstraint(SpringLayout.BASELINE, lblBurLon, 0, SpringLayout.BASELINE, burLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblBurAlt, 0, SpringLayout.BASELINE, burAlt);
		
		add(lblBurTime);
		add(lblBurLat);
		add(lblBurLon);
		add(lblBurAlt);
		add(burTime);
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
		
		JLabel lblLndTime = new JLabel("Time:");
		JLabel lblLndLat = new JLabel("Latitude:");
		JLabel lblLndLon = new JLabel("Longitude:");
		JLabel lblLndAlt = new JLabel("Altitude:");
		lndTime = new JTextField();
		lndTime.setEditable(false);
		lndLat = new JTextField();
		lndLat.setEditable(false);
		lndLon = new JTextField();
		lndLon.setEditable(false);
		lndAlt = new JTextField();
		lndAlt.setEditable(false);
		
		springLayout.putConstraint(SpringLayout.WEST, lblLndLon, 6, SpringLayout.WEST, lblLanding);
		springLayout.putConstraint(SpringLayout.EAST, lblLndTime, 0, SpringLayout.EAST, lblLndLon);
		springLayout.putConstraint(SpringLayout.EAST, lblLndLat, 0, SpringLayout.EAST, lblLndLon);
		springLayout.putConstraint(SpringLayout.EAST, lblLndAlt, 0, SpringLayout.EAST, lblLndLon);
		springLayout.putConstraint(SpringLayout.WEST, lndTime, 6, SpringLayout.EAST, lblLndLat);
		springLayout.putConstraint(SpringLayout.WEST, lndLat, 6, SpringLayout.EAST, lblLndLat);
		springLayout.putConstraint(SpringLayout.WEST, lndLon, 6, SpringLayout.EAST, lblLndLon);
		springLayout.putConstraint(SpringLayout.WEST, lndAlt, 6, SpringLayout.EAST, lblLndAlt);
		springLayout.putConstraint(SpringLayout.NORTH, lndTime, 6, SpringLayout.SOUTH, lblLanding);
		springLayout.putConstraint(SpringLayout.NORTH, lndLat, 6, SpringLayout.SOUTH, lndTime);
		springLayout.putConstraint(SpringLayout.NORTH, lndLon, 6, SpringLayout.SOUTH, lndLat);
		springLayout.putConstraint(SpringLayout.NORTH, lndAlt, 6, SpringLayout.SOUTH, lndLon);
		springLayout.putConstraint(SpringLayout.EAST, lndTime, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, lndLat, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, lndLon, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.EAST, lndAlt, -6, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.BASELINE, lblLndTime, 0, SpringLayout.BASELINE, lndTime);
		springLayout.putConstraint(SpringLayout.BASELINE, lblLndLat, 0, SpringLayout.BASELINE, lndLat);
		springLayout.putConstraint(SpringLayout.BASELINE, lblLndLon, 0, SpringLayout.BASELINE, lndLon);
		springLayout.putConstraint(SpringLayout.BASELINE, lblLndAlt, 0, SpringLayout.BASELINE, lndAlt);
		
		add(lblLndTime);
		add(lblLndLat);
		add(lblLndLon);
		add(lblLndAlt);
		add(lndTime);
		add(lndLat);
		add(lndLon);
		add(lndAlt);
		
		timer = new Timer(1000, this);
		timer.start();
	}

	@Override
	public void positionUpdateEvent(PositionEvent e) {
		switch(e.getSource()) {
			case PositionEvent.PRIMARY:
				curLat.setText(Double.toString(e.getPosition().getLatitude()));
				curLon.setText(Double.toString(e.getPosition().getLongitude()));
				curAlt.setText(Double.toString(e.getPosition().getAltitude()));
				cTime = new Date(e.getPosition().getTime() * 1000);
				updateTimes();
				break;
			case PositionEvent.SECONDARY:
				break;
			case PositionEvent.RECOVERY:
				break;
			case PositionEvent.BURST:
				burLat.setText(Double.toString(e.getPosition().getLatitude()));
				burLon.setText(Double.toString(e.getPosition().getLongitude()));
				burAlt.setText(Double.toString(e.getPosition().getAltitude()));
				bTime = new Date(e.getPosition().getTime() * 1000);
				updateTimes();
				break;
			case PositionEvent.LANDING:
				lndLat.setText(Double.toString(e.getPosition().getLatitude()));
				lndLon.setText(Double.toString(e.getPosition().getLongitude()));
				lndAlt.setText(Double.toString(e.getPosition().getAltitude()));
				lTime = new Date(e.getPosition().getTime() * 1000);
				updateTimes();
				break;
		}
		
	}
	
	private void updateTimes() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		String prefix;
		Date tMinus;
		time = new Date();
		
		if(cTime != null) {
			tMinus = new Date(time.getTime() - cTime.getTime());
			prefix = tMinus.getTime() < 0 ? "T-" : "T+";
			curTime.setText(prefix + sdf.format(tMinus) + " (" + sdf.format(cTime) + ")");
		}
		
		if(bTime != null) {
			tMinus = new Date(bTime.getTime() - time.getTime());
			prefix = tMinus.getTime() < 0 ? "T+" : "T-";
			burTime.setText(prefix + sdf.format(tMinus) + " (" + sdf.format(bTime) + ")");
		}
		
		if(lTime != null) {
			tMinus = new Date(lTime.getTime() - time.getTime());
			prefix = tMinus.getTime() < 0 ? "T+" : "T-";
			lndTime.setText(prefix + sdf.format(tMinus) + " (" + sdf.format(lTime) + ")");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		updateTimes();
	}
	
}
