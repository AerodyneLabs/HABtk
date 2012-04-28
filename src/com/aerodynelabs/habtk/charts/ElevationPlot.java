package com.aerodynelabs.habtk.charts;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.aerodynelabs.map.MapPath;

@SuppressWarnings("serial")
public class ElevationPlot extends JPanel {
	
	JFreeChart chart;
	ElevationTimeSeriesCollection dataset;
	
	public ElevationPlot() {
		super();
		dataset = new ElevationTimeSeriesCollection();
		chart = ChartFactory.createTimeSeriesChart("Elevation Profile",
				"Time", "Altitude (m)", dataset, false, true, false);
		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(480, 320));
		add(panel);
	}
	
	public void addProfile(MapPath path) {
		dataset.addSeries(path);
	}
	
}
