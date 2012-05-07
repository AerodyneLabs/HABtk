package com.aerodynelabs.habtk.charts;

// XXX Verify Skew T plot
// XXX Add dry adiabats
// XXX Add moist adiabats

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.List;

import org.jfree.chart.annotations.AbstractXYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;

@SuppressWarnings("serial")
public class SkewTLogPPlot extends TemperaturePlot {
	
	private AtmosphereSeriesCollection dataset;
	
	public SkewTLogPPlot() {
		super();
		dataset = new AtmosphereSeriesCollection(AtmosphereSeriesCollection.DOMAIN_PRESSURE, AtmosphereSeriesCollection.RANGE_TEMPDEWPT);
		
		PressureAxis pAxis = new PressureAxis("Pressure (hPa)");
		NumberAxis tAxis = new NumberAxis("Temperature (\u00b0C)");
		tAxis.setTickUnit(new NumberTickUnit(10, new DecimalFormat("#"), 0));
		tAxis.setRange(-50.0, 50.0);
		
		setDomainGridlineStroke(new BasicStroke(0.5f));
		setRangeGridlineStroke(new BasicStroke(0.5f));
		
		setDataset(dataset);
		setRenderer(new SkewTRenderer());
		setOrientation(PlotOrientation.HORIZONTAL);
		setDomainAxis(pAxis);
		setRangeAxis(tAxis);
		setDomainMinorGridlinesVisible(true);
		
		createMixingLines();
	}
	
	private void createMixingLines() {
		// XXX Verify mixing lines
		final float dash[] = {10.0f};
		Stroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
								10.0f, dash, 0.0f);
		Paint paint = Color.GREEN;
		XYItemRenderer r = getRenderer();
		// 0.1
		r.addAnnotation(new SkewTLineAnnotation(1050, -40.9, 100, -60.8378, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("0.1", 950, -40));
		// 0.2
		r.addAnnotation(new SkewTLineAnnotation(1050, -34.137, 100, -55.3946, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("0.2", 950, -34));
		// 0.4
		r.addAnnotation(new SkewTLineAnnotation(1050, -26.8943, 100, -49.6071, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("0.4", 950, -27));
		// 0.6
		r.addAnnotation(new SkewTLineAnnotation(1050, -22.4151, 100, -46.0493, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("0.6", 950, -22));
		// 1g/kg
		r.addAnnotation(new SkewTLineAnnotation(1050, -14.4964, 100, -41.3729, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("1", 950, -14.5));
		// 2g/kg
		r.addAnnotation(new SkewTLineAnnotation(1050, -7.93414, 100, -34.6573, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("2", 950, -8));
		// 3
		r.addAnnotation(new SkewTLineAnnotation(1050, -2.62, 100, -30.5186, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("3", 950, -2.5));
		// 4g/kg
		r.addAnnotation(new SkewTLineAnnotation(1050, 1.2955, 100, -27.4833, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("4", 950, 1.3));
		// 5
		r.addAnnotation(new SkewTLineAnnotation(1050, 4.41855, 100, -25.0709, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("5", 950, 4.5));
		// 6
		r.addAnnotation(new SkewTLineAnnotation(1050, 7.02728, 100, -23.0616, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("6", 950, 7));
		// 8g/kg
		r.addAnnotation(new SkewTLineAnnotation(1050, 11.2498, 100, -19.8204, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("8", 950, 11));
		// 10
		r.addAnnotation(new SkewTLineAnnotation(1050, 14.6159, 100, -17.2465, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("10", 950, 14.6));
		// 15
		r.addAnnotation(new SkewTLineAnnotation(1050, 20.9365, 100, -12.4366, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("15", 950, 21));
		// 20
		r.addAnnotation(new SkewTLineAnnotation(1050, 25.5789, 100, -8.92309, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("20", 950, 25.6));
		// 30
		r.addAnnotation(new SkewTLineAnnotation(1050, 32.3335, 100, -3.83991, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("30", 950, 32.3));
		// 40
		r.addAnnotation(new SkewTLineAnnotation(1050, 37.2617, 100, -0.152649, stroke, paint), Layer.BACKGROUND);
		r.addAnnotation(new XYTextAnnotation("40", 950, 37.2));
	}
	
	public void setProfile(AtmosphereProfile profile) {
		dataset.setProfile(profile);
	}
	
	public AtmosphereProfile getProfile() {
		return dataset.getProfile();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void drawRangeGridlines(Graphics2D g2, Rectangle2D area, List ticks) {
		Stroke gridStroke = getRangeGridlineStroke();
		Paint gridPaint = getRangeGridlinePaint();
		NumberAxis axis = (NumberAxis) getRangeAxis();
		if(axis == null) return;
		for(int value = -100; value <= 50; value += 10)
			getRenderer().drawRangeLine(g2, this, axis, area, value, gridPaint, gridStroke);
	}
	
	class SkewTLineAnnotation extends AbstractXYAnnotation {
		
		double x1, y1, x2, y2;
		Stroke stroke;
		Paint paint;

		public SkewTLineAnnotation(double x1, double y1, double x2, double y2, Stroke stroke,
				Paint paint) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.stroke = stroke;
			this.paint = paint;
		}

		@Override
		public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea,
				ValueAxis domainAxis, ValueAxis rangeAxis, int rendererIndex,
				PlotRenderingInfo info) {			
			PlotOrientation orientation = plot.getOrientation();
	        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(
	                plot.getDomainAxisLocation(), orientation);
	        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(
	                plot.getRangeAxisLocation(), orientation);
	        
	        double y1 = domainAxis.valueToJava2D(this.x1, dataArea, domainEdge);
	        double y2 = domainAxis.valueToJava2D(this.x2, dataArea, domainEdge);
	        double x1 = rangeAxis.valueToJava2D(this.y1, dataArea, rangeEdge);
	        x1 += dataArea.getMaxY() - y1;
	        double x2 = rangeAxis.valueToJava2D(this.y2, dataArea, rangeEdge);
	        x2 += dataArea.getMaxY() - y2;
	        
			g2.setStroke(stroke);
			g2.setPaint(paint);
			Line2D.Double line = new Line2D.Double(x1, y1, x2, y2);
			g2.draw(line);
		}
		
	}
	
	class SkewTRenderer extends AbstractXYItemRenderer {

		public SkewTRenderer() {
			super();
		}
		
		@Override
		public void drawRangeLine(Graphics2D g2, XYPlot plot, ValueAxis axis, Rectangle2D dataArea, double value, Paint paint, Stroke stroke) {
			g2.setPaint(paint);
			g2.setStroke(stroke);
			
			double x0 = axis.valueToJava2D(value, dataArea, plot.getRangeAxisEdge());
			double y0 = dataArea.getMaxY();
			double y1 = dataArea.getMinY();
			double x1 = x0 + (y0 - y1);
			g2.drawLine((int)(x0 + 0.5), (int)(y0 + 0.5), (int)(x1 + 0.5), (int)(y1 + 0.5));
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
			double temp = data.getYValue(series, item);
			
			RectangleEdge domainLocation = plot.getDomainAxisEdge();
			RectangleEdge rangeLocation = plot.getRangeAxisEdge();
			
			if(item > 0) {
				double y1 = domainAxis.valueToJava2D(alt, plotArea, domainLocation);
				double x1 = rangeAxis.valueToJava2D(temp, plotArea, rangeLocation);
				x1 += plotArea.getMaxY() - y1;
				double pAlt = data.getXValue(series, item - 1);
				double pTemp = data.getYValue(series, item - 1);
				double y2 = domainAxis.valueToJava2D(pAlt, plotArea, domainLocation);
				double x2 = rangeAxis.valueToJava2D(pTemp, plotArea, rangeLocation);
				x2 += plotArea.getMaxY() - y2;
				g2.drawLine((int)(x1 + 0.5), (int)(y1 + 0.5), (int)(x2 + 0.5), (int)(y2 + 0.5));
			}
		}
		
	}

}
