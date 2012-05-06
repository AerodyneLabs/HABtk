package com.aerodynelabs.habtk.charts;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;

@SuppressWarnings("serial")
public class TemperaturePlot extends JPanel {
	
	private JFreeChart chart;
	private AtmosphereSeriesCollection dataset;
	
	public TemperaturePlot() {
		super();
		setLayout(new BorderLayout());
		
		dataset = new AtmosphereSeriesCollection(AtmosphereSeriesCollection.DOMAIN_PRESSURE, AtmosphereSeriesCollection.RANGE_TEMPDEWPT);
		
		PressureAxis pAxis = new PressureAxis("Pressure (mbar)");
		
		NumberAxis tAxis = new NumberAxis("Temperature (\u00b0C)");
		tAxis.setRange(-60.0, 40.0);
		tAxis.setAutoRange(true);
		
		chart = ChartFactory.createXYLineChart("Temperature", "Temperature (C)", "Altitude (m)", dataset, PlotOrientation.HORIZONTAL, true, true, false);
		chart.getXYPlot().setDomainAxis(pAxis);
		chart.getXYPlot().setRangeAxis(tAxis);
		chart.getXYPlot().setDomainMinorGridlinesVisible(true);
		
		ChartPanel panel = new ChartPanel(chart);
		add(panel, BorderLayout.CENTER);
	}
	
	public void setProfile(AtmosphereProfile profile) {
		dataset.setProfile(profile);
		chart.getXYPlot().getRangeAxis().configure();
		chart.fireChartChanged();
	}
	
	public AtmosphereProfile getProfile() {
		return dataset.getProfile();
	}

}
