package com.aerodynelabs.habtk.charts.tests;

import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;
import com.aerodynelabs.habtk.atmosphere.AtmosphereSource;
import com.aerodynelabs.habtk.atmosphere.GSDParser;
import com.aerodynelabs.habtk.atmosphere.RUCGFS;
import com.aerodynelabs.habtk.charts.SoundingChart;

public class AtmosphereChartTest {
	
	public static void main(String args[]) {
		JFrame frame = new JFrame("Atmosphere Chart Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		frame.getContentPane().add(panel);
		
		int time = (int)(System.currentTimeMillis() / 1000);
		System.out.println(time);
		
		AtmosphereSource source = new RUCGFS();
		File file = source.getAtmosphere(time, 42.0, -93.5);
		
		GSDParser parser = new GSDParser();
		AtmosphereProfile profile = parser.parseAtmosphere(file);
		
		SoundingChart chart = new SoundingChart(SoundingChart.STANDARD);
		chart.setSounding(profile);
		panel.add(chart);
		
		frame.pack();
		frame.setVisible(true);
	}

}
