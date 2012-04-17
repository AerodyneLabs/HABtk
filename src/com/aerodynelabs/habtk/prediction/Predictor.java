package com.aerodynelabs.habtk.prediction;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MapPoint;

public abstract class Predictor {
	
	private static Object[] options = {"Latex v1.0"};
	
	private static String select() {
		String val = (String)JOptionPane.showInputDialog(
				null, "Chose a prediction algorithm:\n", "New Flight Type",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		return val;
	}
	
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
	
	public abstract boolean setup();
	
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
	
	
	public boolean save(File file) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument();
			
			write(doc);
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource src = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
			transformer.transform(src, result);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public abstract void write(Document doc);
	public abstract boolean read(Document doc);
	public abstract MapPoint predictStep(int s);
	public abstract MapPath runPrediction();
	public abstract void setAscending(boolean ascending);
	public abstract void setGroundLevel(double level);
	public abstract void setStart(MapPoint start);

}
