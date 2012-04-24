package com.aerodynelabs.habtk.prediction;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;
import com.aerodynelabs.habtk.atmosphere.GSDParser;
import com.aerodynelabs.habtk.atmosphere.RUCGFS;
import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MapPoint;

public class LatexPredictor extends Predictor {
	
	private long startTime;
	private double startLat, startLon, startAlt;
	private double groundLevel;
	private boolean isAscending = true;
	
	private double payloadMass, balloonLift;
	private double parachuteArea, parachuteDrag;
	private double balloonMass, balloonDrag, burstRad;
	
	private static final String balloons[] = {"Kaymont 200", "Kaymont 300", "Kaymont 350", "Kaymont 600", "Kaymont 800", "Kaymont 1000", "Kaymont 1200", "Kaymont 1500", "Kaymont 2000", "Kaymont 3000"};
	private static final double balloonData[][] = {
		{0.2,	1.524,	0.25},	// Kaymont 200
		{0.3,	1.981,	0.25},	// Kaymont 300
		{0.35,	2.134,	0.25},	// Kaymont 350
		{0.6,	3.048,	0.3},	// Kaymont 600
		{0.8,	3.505,	0.3},	// Kaymont 800
		{1.0,	3.962,	0.3},	// Kaymont 1000
		{1.2,	4.267,	0.25},	// Kaymont 1200
		{1.5,	4.724,	0.25},	// Kaymont 1500
		{2.0,	5.334,	0.25},	// Kaymont 2000
		{3.0,	6.553,	0.25},	// Kaymont 3000
	};
	
	private AtmosphereProfile atmo;
	
	@SuppressWarnings("serial")
	class SetupDialog extends JDialog {
		
		boolean accepted = false;
		JTextField fStartLat, fStartLon, fStartAlt, fStartTime;
		JTextField fMass, fLift, fArea, fDrag;
		JComboBox fBalloon;
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
			
			JLabel lBalloon = new JLabel("Balloon:");
			fBalloon = new JComboBox(balloons);
			layout.putConstraint(SpringLayout.NORTH, fBalloon, 6, SpringLayout.SOUTH, fStartAlt);
			layout.putConstraint(SpringLayout.BASELINE, lBalloon, 0, SpringLayout.BASELINE, fBalloon);
			layout.putConstraint(SpringLayout.WEST, lBalloon, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.WEST, fBalloon, 6, SpringLayout.EAST, lBalloon);
			layout.putConstraint(SpringLayout.EAST, fBalloon, -6, SpringLayout.EAST, pane);
			
			JLabel lLift = new JLabel("Neck Lift (kg):");
			fLift = new JTextField();
			layout.putConstraint(SpringLayout.NORTH, fLift, 6, SpringLayout.SOUTH, fBalloon);
			layout.putConstraint(SpringLayout.BASELINE, lLift, 0, SpringLayout.BASELINE, fLift);
			layout.putConstraint(SpringLayout.WEST, lLift, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.WEST, fLift, 6, SpringLayout.EAST, lLift);
			layout.putConstraint(SpringLayout.EAST, fLift, -6, SpringLayout.EAST, pane);
			
			JLabel lMass = new JLabel("Payload Mass (kg):");
			fMass = new JTextField();
			layout.putConstraint(SpringLayout.NORTH, fMass, 6, SpringLayout.SOUTH, fLift);
			layout.putConstraint(SpringLayout.BASELINE, lMass, 0, SpringLayout.BASELINE, fMass);
			layout.putConstraint(SpringLayout.WEST, lMass, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.WEST, fMass, 6, SpringLayout.EAST, lMass);
			layout.putConstraint(SpringLayout.EAST, fMass, -6, SpringLayout.EAST, pane);
			
			JLabel lArea = new JLabel("Chute Area (m^2):");
			fArea = new JTextField();
			layout.putConstraint(SpringLayout.NORTH, fArea, 6, SpringLayout.SOUTH, fMass);
			layout.putConstraint(SpringLayout.BASELINE, lArea, 0, SpringLayout.BASELINE, fArea);
			layout.putConstraint(SpringLayout.WEST, lArea, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.WEST, fArea, 6, SpringLayout.EAST, lArea);
			layout.putConstraint(SpringLayout.EAST, fArea, -6, SpringLayout.EAST, pane);
			
			JLabel lDrag = new JLabel("Chute Cd:");
			fDrag = new JTextField();
			layout.putConstraint(SpringLayout.NORTH, fDrag, 6, SpringLayout.SOUTH, fArea);
			layout.putConstraint(SpringLayout.BASELINE, lDrag, 0, SpringLayout.BASELINE, fDrag);
			layout.putConstraint(SpringLayout.WEST, lDrag, 6, SpringLayout.WEST, pane);
			layout.putConstraint(SpringLayout.WEST, fDrag, 6, SpringLayout.EAST, lDrag);
			layout.putConstraint(SpringLayout.EAST, fDrag, -6, SpringLayout.EAST, pane);
			
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
					try {
						startLat = Double.parseDouble(fStartLat.getText());
						startLon = Double.parseDouble(fStartLon.getText());
						startAlt = Double.parseDouble(fStartAlt.getText());
						groundLevel = startAlt;
						startTime = sdf.parse(fStartTime.getText()).getTime() / 1000;
						payloadMass = Double.parseDouble(fMass.getText());
						balloonLift = Double.parseDouble(fLift.getText());
						parachuteArea = Double.parseDouble(fArea.getText());
						parachuteDrag = Double.parseDouble(fDrag.getText());
						double bDat[] = balloonData[fBalloon.getSelectedIndex()];
						balloonMass = bDat[0];
						burstRad = bDat[1];
						balloonDrag = bDat[2];
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					dispose();
				}
			});
			layout.putConstraint(SpringLayout.EAST, cancel, -6, SpringLayout.EAST, pane);
			layout.putConstraint(SpringLayout.EAST, ok, -6, SpringLayout.WEST, cancel);
			layout.putConstraint(SpringLayout.NORTH, cancel, 6, SpringLayout.SOUTH, fDrag);
			layout.putConstraint(SpringLayout.NORTH, ok, 6, SpringLayout.SOUTH, fDrag);
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
			add(lBalloon);
			add(fBalloon);
			add(lMass);
			add(fMass);
			add(lLift);
			add(fLift);
			add(lDrag);
			add(fDrag);
			add(lArea);
			add(fArea);
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
	
	public void write(Document doc) {
		Element root = doc.createElement("balloonFlight");
		doc.appendChild(root);
		
		Element predictor = doc.createElement("predictor");
		predictor.appendChild(doc.createTextNode("Latex Predictor v1.0"));
		root.appendChild(predictor);
		
		Element startLat = doc.createElement("startLat");
		startLat.appendChild(doc.createTextNode(Double.toString(this.startLat)));
		root.appendChild(startLat);
		
		Element startLon = doc.createElement("startLon");
		startLon.appendChild(doc.createTextNode(Double.toString(this.startLon)));
		root.appendChild(startLon);
		
		Element startAlt = doc.createElement("startAlt");
		startAlt.appendChild(doc.createTextNode(Double.toString(this.startAlt)));
		root.appendChild(startAlt);
		
		Element startTime = doc.createElement("startTime");
		startTime.appendChild(doc.createTextNode(String.valueOf(this.startTime)));
		root.appendChild(startTime);
		
		Element lift = doc.createElement("balloonLift");
		lift.appendChild(doc.createTextNode(Double.toString(this.balloonLift)));
		root.appendChild(lift);
		
		Element radius = doc.createElement("burstRadius");
		radius.appendChild(doc.createTextNode(Double.toString(this.burstRad)));
		root.appendChild(radius);
		
		Element bMass = doc.createElement("balloonMass");
		bMass.appendChild(doc.createTextNode(Double.toString(this.balloonMass)));
		root.appendChild(bMass);
		
		Element bDrag = doc.createElement("balloonDrag");
		bDrag.appendChild(doc.createTextNode(Double.toString(this.balloonDrag)));
		root.appendChild(bDrag);
		
		Element mass = doc.createElement("payloadMass");
		mass.appendChild(doc.createTextNode(Double.toString(this.payloadMass)));
		root.appendChild(mass);
		
		Element drag = doc.createElement("parachuteDrag");
		drag.appendChild(doc.createTextNode(Double.toString(this.parachuteDrag)));
		root.appendChild(drag);
		
		Element area = doc.createElement("parachuteArea");
		area.appendChild(doc.createTextNode(Double.toString(this.parachuteArea)));
		root.appendChild(area);
	}
	
	public boolean read(Document doc) {
		try {
			Element root = doc.getDocumentElement();
			startLat = Double.parseDouble(root.getElementsByTagName("startLat").item(0).getTextContent());
			startLon = Double.parseDouble(root.getElementsByTagName("startLon").item(0).getTextContent());
			startAlt = Double.parseDouble(root.getElementsByTagName("startAlt").item(0).getTextContent());
			startTime = Long.parseLong(root.getElementsByTagName("startTime").item(0).getTextContent());
			balloonLift = Double.parseDouble(root.getElementsByTagName("balloonLift").item(0).getTextContent());
			payloadMass = Double.parseDouble(root.getElementsByTagName("payloadMass").item(0).getTextContent());
			parachuteDrag = Double.parseDouble(root.getElementsByTagName("parachuteDrag").item(0).getTextContent());
			parachuteArea = Double.parseDouble(root.getElementsByTagName("parachuteArea").item(0).getTextContent());
			burstRad = Double.parseDouble(root.getElementsByTagName("burstRadius").item(0).getTextContent());
			balloonMass = Double.parseDouble(root.getElementsByTagName("balloonMass").item(0).getTextContent());
			balloonDrag = Double.parseDouble(root.getElementsByTagName("balloonDrag").item(0).getTextContent());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
				" @ " + startTime + "\n" +
				balloonMass + "kg balloon, " + balloonDrag + " drag, " + burstRad + "m burst radius\n" +
				balloonLift + "kg lift, " + payloadMass + "kg mass\n" +
				parachuteArea + "m2 parachute, " + parachuteDrag + " drag";
		
		return ret;
	}
	
	public MapPath runPrediction() {
		// TODO
		// Create output variable
		MapPath path = new MapPath();
		
		// Get atmosphere profile
		File wind = new RUCGFS().getAtmosphere((int)startTime, startLat, startLon);
		atmo = new GSDParser().parseAtmosphere(wind);
		if(atmo == null) return null;
		
		// Initial conditions
		double cLat = startLat;
		double cLon = startLon;
		double cAlt = startAlt;
		
		
		// Calculate ascent
		while(isAscending) {
			
		}
		// Calculate descent
		while(cAlt > groundLevel){
			
		}
		
		return path;
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
