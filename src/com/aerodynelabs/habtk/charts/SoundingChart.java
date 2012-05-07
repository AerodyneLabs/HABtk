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
	
	public static final int STANDARD = 0;
	public static final int SKEWT = 1;
	
	private TemperaturePlot tPlot;
	private WindPlot wPlot;
	private JFreeChart chart;
	private int type;
	
	public SoundingChart() {
		this(STANDARD);
	}
	
	public SoundingChart(int type) {
		super();
		this.type = type;
		
		if(type == STANDARD) {
			tPlot = new TemperaturePlot();
		} else {
			tPlot = new SkewTLogPPlot();
		}
		wPlot = new WindPlot();
		CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new PressureAxis("Pressure (mbar)"));
		plot.setOrientation(PlotOrientation.HORIZONTAL);
		plot.add(tPlot, 2);
		plot.add(wPlot, 1);
		chart = new JFreeChart("Atmosphere Sounding", new Font(null, Font.BOLD, 18), plot, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		add(chartPanel);
	}
	
	/*public void setType(int type) {
		if(type == STANDARD) {
			tPlot = new TemperaturePlot();
		} else {
			tPlot = new SkewTLogPPlot();
		}
		chart.fireChartChanged();
		tPlot.setProfile(wPlot.getProfile());
	}*/
	
	public int getType() {
		return type;
	}
	
	public void setSounding(AtmosphereProfile sounding) {
		wPlot.setProfile(sounding);
		tPlot.setProfile(sounding);
	}
	
	public AtmosphereProfile getSounding() {
		return wPlot.getProfile();
	}

}
