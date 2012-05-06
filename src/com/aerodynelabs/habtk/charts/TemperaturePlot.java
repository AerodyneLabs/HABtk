package com.aerodynelabs.habtk.charts;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;

@SuppressWarnings("serial")
public class TemperaturePlot extends JPanel {
	
	private JFreeChart chart;
	private AtmosphereSeriesCollection dataset;
	
	public TemperaturePlot() {
		super();
		
		dataset = new AtmosphereSeriesCollection();
		
		PressureAxis pAxis = new PressureAxis("Pressure (mbar)");
		
		NumberAxis tAxis = new NumberAxis("Temperature (\u00b0C)");
		tAxis.setAutoRange(false);
		tAxis.setRange(-60.0, 40.0);
		
		chart = ChartFactory.createXYLineChart("Temperature", "Temperature (C)", "Altitude (m)", dataset, PlotOrientation.HORIZONTAL, false, true, false);
		chart.getXYPlot().setDomainAxis(pAxis);
		chart.getXYPlot().setRangeAxis(tAxis);
		chart.getXYPlot().setDomainMinorGridlinesVisible(true);
		
		ChartPanel panel = new ChartPanel(chart);
		add(panel);
	}
	

}
