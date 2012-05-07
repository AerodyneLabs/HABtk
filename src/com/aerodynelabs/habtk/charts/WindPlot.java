package com.aerodynelabs.habtk.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

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
public class WindPlot extends XYPlot {
	
	private AtmosphereSeriesCollection data;
	
	public WindPlot() {
		super();
		
		data = new AtmosphereSeriesCollection(
				AtmosphereSeriesCollection.DOMAIN_PRESSURE,
				AtmosphereSeriesCollection.RANGE_WIND);
		
		PressureAxis pAxis = new PressureAxis("Pressure (mbar)");
		
		NumberAxis wAxis = new NumberAxis("Speed (m/s)");
		
		setDomainGridlinePaint(Color.BLACK);
		setDomainGridlineStroke(new BasicStroke(1.0f));
		
		setDataset(data);
		setOrientation(PlotOrientation.HORIZONTAL);
		setDomainAxis(pAxis);
		setRangeAxis(wAxis);
		setRenderer(new WindVectorRenderer());
	}
	
	public void setProfile(AtmosphereProfile profile) {
		data.setProfile(profile);
		super.fireChangeEvent();
	}
	
	public AtmosphereProfile getProfile() {
		return data.getProfile();
	}
	
	class WindVectorRenderer extends AbstractXYItemRenderer {
		
		private static final int barbLength = 15;
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
			if(pass > 0) return;
			
			AtmosphereSeriesCollection data = (AtmosphereSeriesCollection) dataset;
			
			Paint seriesPaint = null;
			if(series == 0) {
				seriesPaint = Color.RED;
			} else {
				seriesPaint = Color.BLUE;
			}
			Stroke seriesStroke = getItemStroke(series, item);
			g2.setPaint(seriesPaint);
			g2.setStroke(seriesStroke);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			double alt = data.getXValue(series, item);
			double spd = data.getYValue(0, item);
			double dir = Math.toRadians(data.getYValue(1, item));
			
			RectangleEdge domainLocation = plot.getDomainAxisEdge();
			RectangleEdge rangeLocation = plot.getRangeAxisEdge();
			
			if(series == 0) {	// Draw vector
				// Draw barb
				double ox = rangeAxis.valueToJava2D(0.0, plotArea, rangeLocation) + barbLength * 1.5;
				double oy = domainAxis.valueToJava2D(alt, plotArea, domainLocation);
				double dx = barbLength * Math.sin(dir);
				double dy = -barbLength * Math.cos(dir);
				g2.drawLine((int)(ox + 0.5), (int)(oy + 0.5), (int)(ox + dx + 0.5), (int)(oy + dy + 0.5));
				// Draw fletching
				ox = ox + dx;
				oy = oy + dy;
				double s = spd * 1.944;
				while(s > 0) {
					dx = 0;
					dy = 0;
					if(s > 50) {
						dx = fletchLength * Math.sin(dir + (Math.PI/3));
						dy = -fletchLength * Math.cos(dir + (Math.PI/3));
						double tx = 3 * Math.sin(dir);
						double ty = -3 * Math.cos(dir); 
						int x[] = {(int)(ox + 0.5), (int)(ox + dx + 0.5), (int)(ox + tx + 0.5)};
						int y[] = {(int)(oy + 0.5), (int)(oy + dy + 0.5), (int)(oy + ty + 0.5)};
						g2.fillPolygon(x, y, 3);
						dx = 0;
						dy = 0;
						s = s - 50;
					} else if(s > 10) {
						dx = fletchLength * Math.sin(dir + (Math.PI/3));
						dy = -fletchLength * Math.cos(dir + (Math.PI/3));
						s = s - 10;
					} else if(s > 5) {
						dx = 0.5 * fletchLength * Math.sin(dir + (Math.PI/3));
						dy = -0.5 * fletchLength * Math.cos(dir + (Math.PI/3));
						s = s - 5;
					} else {
						s = 0;
					}
					g2.drawLine((int)(ox + 0.5), (int)(oy + 0.5), (int)(ox + dx + 0.5), (int)(oy + dy + 0.5));
					dx = 3 * Math.sin(dir + Math.PI);
					dy = -3 * Math.cos(dir + Math.PI);
					ox = ox + dx;
					oy = oy + dy;
				}
			} else if(series == 1) {	// Draw line series
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
