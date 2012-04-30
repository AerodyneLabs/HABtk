package com.aerodynelabs.habtk.charts;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

@SuppressWarnings("serial")
public class TemperaturePlot extends JPanel {
	
	private JFreeChart chart;
	private AtmosphereSeriesCollection dataset;
	
	public TemperaturePlot() {
		super();
		dataset = new AtmosphereSeriesCollection();
		chart = ChartFactory.createXYLineChart("Temperature", "Temperature (C)", "Altitude (m)", dataset, PlotOrientation.VERTICAL, false, true, false);
		ChartPanel panel = new ChartPanel(chart);
		add(panel);
	}
	

}
