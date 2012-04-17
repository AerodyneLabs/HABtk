package com.aerodynelabs.habtk.prediction;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;
import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MapPoint;

public class LatexPredictor extends Predictor {
	
	private long startTime;
	private double startLat, startLon, startAlt;
	private double groundLevel;
	private boolean isAscending = true;
	
	private AtmosphereProfile atmo;
	
	@SuppressWarnings("serial")
	class SetupDialog extends JDialog {
		
		boolean accepted = false;
		JTextField fStartLat, fStartLon, fStartAlt, fStartTime;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		public SetupDialog() {
			setTitle("Setup Flight");
			setModal(true);
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			Container pane = getContentPane();
			
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			JLabel lStartTime = new JLabel("Launch Time UTC:");
			fStartTime = new JTextField(sdf.format(new Date()), 12);
			JButton bStartTime = new JButton("Calendar");
			bStartTime.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// TODO calendar JCommon DateChooserPanel
					JOptionPane.showMessageDialog(null, "Date picker not implemented yet!");
				}
			});
			layout.putConstraint(SpringLayout.WEST, lStartTime, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.NORTH, lStartTime, 6, SpringLayout.NORTH, pane);
			layout.putConstraint(SpringLayout.WEST, fStartTime, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.NORTH, fStartTime, 6, SpringLayout.SOUTH, lStartTime);
			layout.putConstraint(SpringLayout.WEST, bStartTime, 6, SpringLayout.EAST, fStartTime);
			layout.putConstraint(SpringLayout.NORTH, bStartTime, 0, SpringLayout.NORTH, fStartTime);
			layout.putConstraint(SpringLayout.EAST, bStartTime, -6, SpringLayout.EAST, getContentPane());
			layout.putConstraint(SpringLayout.SOUTH, bStartTime, 0, SpringLayout.SOUTH, fStartTime);
			
			JLabel lStartLat = new JLabel("Start Latitude:");
			fStartLat = new JTextField("42.0000", 10);
			JLabel lStartLon = new JLabel("Start Longitude:");
			fStartLon = new JTextField("-93.6350", 10);
			layout.putConstraint(SpringLayout.NORTH, lStartLat, 6, SpringLayout.SOUTH, fStartTime);
			layout.putConstraint(SpringLayout.NORTH, lStartLon, 6, SpringLayout.SOUTH, fStartTime);
			layout.putConstraint(SpringLayout.WEST, lStartLat, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.WEST, lStartLon, 6, SpringLayout.WEST, fStartLon);
			layout.putConstraint(SpringLayout.EAST, lStartLon, -6, SpringLayout.EAST, pane);
			layout.putConstraint(SpringLayout.NORTH, fStartLat, 6, SpringLayout.SOUTH, lStartLat);
			layout.putConstraint(SpringLayout.NORTH, fStartLon, 6, SpringLayout.SOUTH, lStartLon);
			layout.putConstraint(SpringLayout.WEST, fStartLat, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.WEST, fStartLon, 6, SpringLayout.EAST, fStartLat);
			layout.putConstraint(SpringLayout.EAST, pane, 6, SpringLayout.EAST, fStartLon);
			
			JLabel lStartAlt = new JLabel("Start Altitude (m):");
			fStartAlt = new JTextField("297.5", 10);
			layout.putConstraint(SpringLayout.NORTH, fStartAlt, 6, SpringLayout.SOUTH, fStartLat);
			layout.putConstraint(SpringLayout.BASELINE, lStartAlt, 0, SpringLayout.BASELINE, fStartAlt);
			layout.putConstraint(SpringLayout.WEST, lStartAlt, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.WEST, fStartAlt, 6, SpringLayout.EAST, lStartAlt);
			layout.putConstraint(SpringLayout.EAST, fStartAlt, -6, SpringLayout.EAST, pane);
			
			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					accepted = false;
					dispose();
				}
			});
			JButton ok = new JButton("Ok");
			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					accepted = true;
					startLat = Double.parseDouble(fStartLat.getText());
					startLon = Double.parseDouble(fStartLon.getText());
					startAlt = Double.parseDouble(fStartAlt.getText());
					groundLevel = startAlt;
					try {
						startTime = sdf.parse(fStartTime.getText()).getTime() / 1000;
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					dispose();
				}
			});
			layout.putConstraint(SpringLayout.EAST, cancel, -6, SpringLayout.EAST, pane);
			layout.putConstraint(SpringLayout.EAST, ok, -6, SpringLayout.WEST, cancel);
			layout.putConstraint(SpringLayout.NORTH, cancel, 6, SpringLayout.SOUTH, fStartAlt);
			layout.putConstraint(SpringLayout.NORTH, ok, 6, SpringLayout.SOUTH, fStartAlt);
			layout.putConstraint(SpringLayout.SOUTH, cancel, -6, SpringLayout.SOUTH, pane);
			layout.putConstraint(SpringLayout.SOUTH, pane, 6, SpringLayout.SOUTH, ok);
			
			add(lStartTime);
			add(fStartTime);
			add(bStartTime);
			add(lStartLat);
			add(fStartLat);
			add(lStartLon);
			add(fStartLon);
			add(lStartAlt);
			add(fStartAlt);
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
	
	public LatexPredictor() {
		
	}
	
	public boolean setup() {
		SetupDialog dialog = new SetupDialog();
		return dialog.wasAccepted();
	}
	
	public String toString() {
		String ret =
				"Latex Predictor v1.0\n" +
				startLat + ", " + startLon + " from " + startAlt +
				" @ " + startTime;
		
		return ret;
	}
	
	public MapPoint predictStep(int s) {
		
		return null;
	}
	
	public MapPath runPrediction() {
		
		return null;
	}

	@Override
	public void setAscending(boolean ascending) {
		isAscending = ascending;
	}

	@Override
	public void setGroundLevel(double level) {
		groundLevel = level;
	}

	@Override
	public void setStart(MapPoint start) {
		startLat = start.getLatitude();
		startLon = start.getLongitude();
		startTime = start.getTime();
		startAlt = start.getAltitude();
	}

}
