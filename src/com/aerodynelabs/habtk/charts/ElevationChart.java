package com.aerodynelabs.habtk.charts;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.aerodynelabs.map.MapPath;

@SuppressWarnings("serial")
public class ElevationChart extends JPanel {
	
	private JFreeChart chart;
	private ElevationTimeSeriesCollection dataset;
	
	public ElevationChart() {
		super();
		setLayout(new BorderLayout());
		dataset = new ElevationTimeSeriesCollection();
		chart = ChartFactory.createTimeSeriesChart("Elevation Profile",
				"Time", "Altitude (m)", dataset, false, true, false);
		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(480, 320));
		add(panel, BorderLayout.CENTER);
	}
	
	public void addProfile(MapPath path) {
		dataset.addSeries(path);
	}
	
}
