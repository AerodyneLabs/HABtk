package com.aerodynelabs.habtk.charts;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeriesCollection;

@SuppressWarnings("serial")
public class ElevationPlot extends JPanel {
	
	JFreeChart chart;
	TimeSeriesCollection dataset;
	
	public ElevationPlot() {
		dataset = new TimeSeriesCollection();
		chart = ChartFactory.createTimeSeriesChart("Elevation Profile",
				"Time", "Altitude (m)", dataset, false, true, false);
	}
	
}
