package com.aerodynelabs.habtk.charts;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;

@SuppressWarnings("serial")
public class WindPlot extends JPanel {
	
	private AtmosphereSeriesCollection data;
	private JFreeChart chart;
	
	public WindPlot() {
		super();
		setLayout(new BorderLayout());
		
		data = new AtmosphereSeriesCollection(
				AtmosphereSeriesCollection.DOMAIN_PRESSURE,
				AtmosphereSeriesCollection.RANGE_WIND);
		
		PressureAxis pAxis = new PressureAxis("Pressure (mbar)");
		
		NumberAxis wAxis = new NumberAxis("");
		
		chart = ChartFactory.createXYLineChart("Wind", "Pressure", "Wind", data, PlotOrientation.HORIZONTAL, false, true, false);
		chart.getXYPlot().setDomainAxis(pAxis);
		chart.getXYPlot().setRangeAxis(wAxis);
		chart.getXYPlot().setRenderer(new WindVectorRenderer());
		
		ChartPanel panel = new ChartPanel(chart);
		add(panel, BorderLayout.CENTER);
	}
	
	public void setProfile(AtmosphereProfile profile) {
		data.setProfile(profile);
	}
	
	public AtmosphereProfile getProfile() {
		return data.getProfile();
	}
	
	class WindVectorRenderer extends AbstractXYItemRenderer {
		
		private static final int barbLength = 10;
		private static final int fletchLength = 8;
		
		public WindVectorRenderer() {
			super();
		}

		@Override
		public void drawItem(
						Graphics2D g2,
						XYItemRendererState state,
						Rectangle2D plotArea,
						PlotRenderingInfo info,
						XYPlot plot,
						ValueAxis domainAxis,
						ValueAxis rangeAxis,
						XYDataset dataset,
						int series, int item,
						CrosshairState crosshairState,
						int pass) {
			// TODO Auto-generated method stub
			if(pass > 0) return;
			
			AtmosphereSeriesCollection data = (AtmosphereSeriesCollection) dataset;
			
			Paint seriesPaint = getItemPaint(series, item);
			Stroke seriesStroke = getItemStroke(series, item);
			g2.setPaint(seriesPaint);
			g2.setStroke(seriesStroke);
			
			double alt = data.getXValue(series, item);
			double spd = data.getYValue(0, item);
			double dir = Math.toRadians(data.getYValue(1, item));
			
			RectangleEdge domainLocation = plot.getDomainAxisEdge();
			RectangleEdge rangeLocation = plot.getDomainAxisEdge();
			
			if(series == 0) {
				double ox = rangeAxis.valueToJava2D(0.0, plotArea, rangeLocation);
				double oy = domainAxis.valueToJava2D(alt, plotArea, domainLocation);
				double dx = barbLength * Math.sin(dir);
				double dy = -barbLength * Math.cos(dir);
				
				g2.drawLine((int)(ox + 0.5), (int)(oy + 0.5), (int)(ox + dx + 0.5), (int)(oy + dy + 0.5));
			} else if(series == 1) {
				if(item == 0) return;
				double x1 = rangeAxis.valueToJava2D(spd, plotArea, rangeLocation);
				double y1 = domainAxis.valueToJava2D(alt, plotArea, domainLocation);
				double pAlt = data.getXValue(series, item - 1);
				double pSpd = data.getYValue(0, item - 1);
				double x2 = rangeAxis.valueToJava2D(pSpd, plotArea, rangeLocation);
				double y2 = domainAxis.valueToJava2D(pAlt, plotArea, domainLocation);
				g2.drawLine((int)(x1 + 0.5), (int)(y1 + 0.5), (int)(x2 + 0.5), (int)(y2 + 0.5));
			}
		}
		
	}

}
