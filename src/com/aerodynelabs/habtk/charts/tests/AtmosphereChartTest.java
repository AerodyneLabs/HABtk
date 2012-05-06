package com.aerodynelabs.habtk.charts.tests;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.aerodynelabs.habtk.charts.TemperaturePlot;

public class AtmosphereChartTest {
	
	public static void main(String args[]) {
		JFrame frame = new JFrame("Atmosphere Chart Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		frame.getContentPane().add(panel);
		
		TemperaturePlot tPlot = new TemperaturePlot();
		panel.add(tPlot);
		
		frame.pack();
		frame.setVisible(true);
	}

}
