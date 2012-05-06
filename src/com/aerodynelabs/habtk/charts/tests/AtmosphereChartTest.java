package com.aerodynelabs.habtk.charts.tests;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;
import com.aerodynelabs.habtk.atmosphere.AtmosphereSource;
import com.aerodynelabs.habtk.atmosphere.GSDParser;
import com.aerodynelabs.habtk.atmosphere.RUCGFS;
import com.aerodynelabs.habtk.charts.PressureAxis;
import com.aerodynelabs.habtk.charts.TemperaturePlot;
import com.aerodynelabs.habtk.charts.WindPlot;

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
		
		TemperaturePlot tPlot = new TemperaturePlot();
		tPlot.setProfile(profile);
		WindPlot wPlot = new WindPlot();
		wPlot.setProfile(profile);
		
//		ChartPanel tChart = new ChartPanel(new JFreeChart(tPlot));
//		ChartPanel wChart = new ChartPanel(new JFreeChart(wPlot));
//		panel.add(tChart);
//		panel.add(wChart);
		
		PressureAxis domain = new PressureAxis("Pressure (mbar)");
		CombinedDomainXYPlot plot = new CombinedDomainXYPlot(domain);
		plot.setOrientation(PlotOrientation.HORIZONTAL);
		plot.add(tPlot, 2);
		plot.add(wPlot, 1);
		JFreeChart chart = new JFreeChart("Atmosphere Sounding", new Font(null, Font.BOLD, 18), plot, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		panel.add(chartPanel);
		
		frame.pack();
		frame.setVisible(true);
	}

}
