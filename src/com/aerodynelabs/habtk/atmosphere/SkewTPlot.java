package com.aerodynelabs.habtk.atmosphere;

import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


@SuppressWarnings("serial")
public class SkewTPlot extends JPanel {
	
	JFreeChart chart;
	XYSeriesCollection set;
	
	public SkewTPlot() {
		super();
		set = new XYSeriesCollection();
		chart = ChartFactory.createXYLineChart("Atmosphere Profile", "Altitude (m)", "Temperature (C)", set, PlotOrientation.HORIZONTAL, false, true, false);
		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new Dimension(600, 400));
		super.add(panel);
	}
	
	public void setProfile(AtmosphereProfile profile) {
		XYSeries t = new XYSeries("Temp");
		XYSeries dp = new XYSeries("Dex Point");
		Iterator<AtmosphereState> itr = profile.data.iterator();
		while(itr.hasNext()) {
			AtmosphereState c = itr.next();
			t.add(c.h, c.t);
			dp.add(c.h, c.dp);
		}
		set.removeAllSeries();
		set.addSeries(dp);
		set.addSeries(t);
	}

}
