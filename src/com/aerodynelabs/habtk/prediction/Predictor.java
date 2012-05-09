package com.aerodynelabs.habtk.prediction;

import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MapPoint;

public abstract class Predictor implements Cloneable {
	
	/**
	 * Valid predictor types
	 */
	private static Object[] options = {"Latex v1.0"};
	
	/**
	 * Select which type of predictor.
	 * @return
	 */
	private static String select() {
		String val = (String)JOptionPane.showInputDialog(
				null, "Chose a prediction algorithm:\n", "New Flight Type",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		return val;
	}
	
	/**
	 * Create a new predictor.
	 * @return
	 */
	public static Predictor create() {
		Predictor flight = null;
		
		String type = select();
		if(type == null) return null;
		if(type.equals(options[0])) {
			flight = new LatexPredictor();
		}
		
		if(flight.setup()) {
			return flight;
		} else {
			return null;
		}
	}
	
	/**
	 * Display a dialog to setup the predictor.
	 * @return
	 */
	public abstract boolean setup();
	
	/**
	 * Load predictor from XML file chose by user.
	 * @return
	 */
	public static Predictor load() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("flights/"));
		FileFilter filter = new FileNameExtensionFilter("Flight Definitions", "xml");
		chooser.setFileFilter(filter);
		
		File file;
		int val = chooser.showOpenDialog(null);
		if(val == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
		} else {
			return null;
		}
		
		return load(file);
	}
	
	/**
	 * Load predictor from specified XML file.
	 * @param file
	 * @return
	 */
	public static Predictor load(File file) {
		Predictor flight = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(file);
			doc.getDocumentElement().normalize();
			
			Element root = doc.getDocumentElement();
			if(root.getNodeName().equals("balloonFlight") == false) return null;
			if(root.getElementsByTagName("predictor").item(0).getTextContent().equals("Latex Predictor v1.0")) {
				flight = new LatexPredictor();
			}
			
			
			if(flight.read(doc) == false) return null;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return flight;
	}
	
	/**
	 * Save predictor to XML file chose by user.
	 * @return
	 */
	public boolean save() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("flights/"));
		FileFilter filter = new FileNameExtensionFilter("Flight Definitions", "xml");
		chooser.setFileFilter(filter);
		
		File file;
		int val = chooser.showSaveDialog(null);
		if(val == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			String name = file.getName();
			if(name.lastIndexOf('.') == -1) {
				name += ".xml";
				file = new File(file.getParentFile(), name);
			}
		} else {
			return false;
		}
		
		return save(file);
	}
	
	/**
	 * Save predictor to given file.
	 * @param file
	 * @return
	 */
	public boolean save(File file) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument();
			
			write(doc);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource src = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(src, result);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * Close the predictor.
	 */
	@Override
	public abstract Predictor clone();
	
	/**
	 * Write predictor into given XML document.
	 * @param doc
	 */
	public abstract void write(Document doc);
	
	/**
	 * Read predictor from given XML document.
	 * @param doc
	 * @return
	 */
	public abstract boolean read(Document doc);
	
	/**
	 * Run prediction from current start point to ground level.
	 * @return
	 */
	public abstract MapPath runPrediction();
	
	/**
	 * Set if prediction should start with ascent.
	 * @param ascending
	 */
	public abstract void setAscending(boolean ascending);
	
	/**
	 * Set termination altitude.
	 * @param level Altitude in meters above sea level.
	 */
	public abstract void setGroundLevel(double level);
	
	/**
	 * Set start point.
	 * @param start
	 */
	public abstract void setStart(MapPoint start);
	
	/**
	 * Set start time (seconds).
	 * @param time
	 */
	public abstract void setStartTime(long time);
	
	/**
	 * Get the current start point.
	 * @return
	 */
	public abstract MapPoint getStart();
	
	/**
	 * Get a name representing the type of this prediction.
	 * @return
	 */
	public abstract String getTypeName();
	
	/**
	 * Get the lift or relevant field.
	 * @return
	 */
	public abstract double getLift();
	
	/**
	 * Test if this prediction equals the given object.
	 */
	public abstract boolean equals(Object o);
	
	/**
	 * Get the hash code of this prediction.
	 */
	public abstract int hashCode();
	
	/**
	 * Get final lat/lon from start lat/lon, bearing and distance.
	 * @param start
	 * @param bearing	degrees from north
	 * @param range		meters
	 * @return
	 */
	protected Point2D.Double directGeodesic(Point2D.Double start, double bearing, double range) {
		double radDist = range / 6367500;
		double lat1 = Math.toRadians(start.y);
		double lon1 = Math.toRadians(start.x);
		double lat2 = Math.asin( Math.sin(lat1)*Math.cos(radDist) + Math.cos(lat1)*Math.sin(radDist)*Math.cos(bearing) );
		double lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(radDist)*Math.cos(lat1), Math.cos(radDist)-Math.sin(lat1)*Math.sin(lat2));
		return new Point2D.Double(Math.toDegrees(lon2), Math.toDegrees(lat2));
	}

	public abstract MapPoint getBurst();
	
	public abstract MapPoint getLanding();
	
}
