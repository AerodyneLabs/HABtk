package com.aerodynelabs.habtk.charts;

import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;

@SuppressWarnings("serial")
public class SoundingChart extends JPanel {
	
	private TemperaturePlot tPlot;
	private WindPlot wPlot;
	private JFreeChart chart;
	
	public SoundingChart() {
		super();
		
		tPlot = new TemperaturePlot();
		wPlot = new WindPlot();
		CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new PressureAxis("Pressure (mbar)"));
		plot.setOrientation(PlotOrientation.HORIZONTAL);
		plot.add(tPlot, 2);
		plot.add(wPlot, 1);
		chart = new JFreeChart("Atmosphere Sounding", new Font(null, Font.BOLD, 18), plot, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		add(chartPanel);
	}
	
	public void setSounding(AtmosphereProfile sounding) {
		wPlot.setProfile(sounding);
		tPlot.setProfile(sounding);
	}
	
	public AtmosphereProfile getSounding() {
		return wPlot.getProfile();
	}

}
