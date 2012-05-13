package com.aerodynelabs.habtk.prediction.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aerodynelabs.habtk.charts.ElevationChart;
import com.aerodynelabs.habtk.prediction.ElevationService;
import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapOverlay;
import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MappingPanel;

public class Elevation_Test {

	public static void main(String args[]) {
		double elev = ElevationService.getElevation(42, -93.635);
		System.out.println("Elevation: " + elev);
		
		Predictor flight = Predictor.create();
		System.out.println(flight);
		if(flight == null) System.exit(0);
		MapPath path = flight.runPrediction();
		
		MapPath out = ElevationService.getElevationProfile(path);
		
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
		profile.addProfile(out);
		panel.add(profile);
		
		frame.pack();
		frame.setVisible(true);
	}
	
}
