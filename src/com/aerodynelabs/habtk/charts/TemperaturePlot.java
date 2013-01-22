package com.aerodynelabs.habtk.charts;

import java.awt.Color;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;

@SuppressWarnings("serial")
public class TemperaturePlot extends XYPlot {
	
	private AtmosphereSeriesCollection dataset;
	
	public TemperaturePlot() {
		super();
		
		dataset = new AtmosphereSeriesCollection(AtmosphereSeriesCollection.DOMAIN_PRESSURE, AtmosphereSeriesCollection.RANGE_TEMPDEWPT);
		
		PressureAxis pAxis = new PressureAxis("Pressure (mbar)");
		
		NumberAxis tAxis = new NumberAxis("Temperature (\u00b0C)");
		tAxis.setRange(-60.0, 40.0);
		tAxis.setAutoRange(true);
		
		setDataset(dataset);
		XYLineAndShapeRenderer r = new XYLineAndShapeRenderer(true, false);
		r.setSeriesPaint(0, Color.RED);
		r.setSeriesPaint(0, Color.BLUE);
		setRenderer(r);
		setOrientation(PlotOrientation.HORIZONTAL);
		setDomainAxis(pAxis);
		setRangeAxis(tAxis);
		setDomainMinorGridlinesVisible(true);
	}
	
	public void setProfile(AtmosphereProfile profile) {
		dataset.setProfile(profile);
		super.fireChangeEvent();
	}
	
	public AtmosphereProfile getProfile() {
		return dataset.getProfile();
	}

}
