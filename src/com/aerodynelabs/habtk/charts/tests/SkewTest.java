package com.aerodynelabs.habtk.charts.tests;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;
import com.aerodynelabs.habtk.atmosphere.AtmosphereSource;
import com.aerodynelabs.habtk.atmosphere.GSDParser;
import com.aerodynelabs.habtk.atmosphere.RUCGFS;
import com.aerodynelabs.habtk.charts.SkewTLogPPlot;

public class SkewTest {
	
	public static void main(String args[]) {
		
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		
		int time = (int)(System.currentTimeMillis() / 1000);
		System.out.println(time);
		
		AtmosphereSource source = new RUCGFS();
		File file = source.getAtmosphere(time, 42.0, -93.5);
		
		GSDParser parser = new GSDParser();
		AtmosphereProfile profile = parser.parseAtmosphere(file);
		
		SkewTLogPPlot plot = new SkewTLogPPlot();
		plot.setProfile(profile);
		JFreeChart chart = new JFreeChart(plot);
		ChartPanel cPanel = new ChartPanel(chart);
		
		panel.add(cPanel);
		frame.pack();
		frame.setVisible(true);
	}

}
