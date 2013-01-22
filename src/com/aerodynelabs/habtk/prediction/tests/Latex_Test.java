package com.aerodynelabs.habtk.prediction.tests;

import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aerodynelabs.habtk.charts.ElevationChart;
import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapOverlay;
import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MappingPanel;

public class Latex_Test {
	
	static Predictor flight;
	
	public static void main(String args[]) {
		flight = Predictor.create();
		System.out.println(flight);
		if(flight == null) System.exit(0);
		
		MapPath path = flight.runPrediction();
//		path.export(new File("flights/test.flt"));
		path.exportKML(new File("flights/test.kml"));
		System.out.println(path.getElapsedTime() + " seconds");
		System.out.println(path.getDistance()/1000 + " kilometers");
		System.out.println(path.getMaxAlt() + " meters burst alt");
		
		JFrame frame = new JFrame("Latex Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		frame.getContentPane().add(panel);
		
		MappingPanel map = new MappingPanel();
		MapOverlay overlay = new MapOverlay("Prediction");
		overlay.addPath("Prediction", path);
		map.addOverlay(overlay);
		panel.add(map);
		
		ElevationChart profile = new ElevationChart();
		profile.addProfile(path);
		panel.add(profile);
		
		frame.pack();
		frame.setVisible(true);
	}

}
