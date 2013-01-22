package com.aerodynelabs.habtk.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
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
	
	private static final Color isobar = Color.black;
	private static final Color isotherm = Color.magenta;
	private static final Color mixing = Color.green;
	private static final Color dry = Color.orange;
	private static final Color wet = Color.orange;
	
	private AtmosphereSeriesCollection dataset;
	
	public SkewTLogPPlot() {
		super();
		dataset = new AtmosphereSeriesCollection(AtmosphereSeriesCollection.DOMAIN_PRESSURE, AtmosphereSeriesCollection.RANGE_TEMPDEWPT);
		
		PressureAxis pAxis = new PressureAxis("Pressure (hPa)");
		NumberAxis tAxis = new NumberAxis("Temperature (\u00b0C)");
		tAxis.setTickUnit(new NumberTickUnit(10, new DecimalFormat("#"), 0));
		tAxis.setRange(-50.0, 50.0);
		
		setDomainGridlinePaint(isobar);
		setDomainGridlineStroke(new BasicStroke(1.0f));
		setRangeGridlinePaint(isotherm);
		setRangeGridlineStroke(new BasicStroke(1.0f));
		
		setDataset(dataset);
		setRenderer(new SkewTRenderer());
		setOrientation(PlotOrientation.HORIZONTAL);
		setDomainAxis(pAxis);
		setRangeAxis(tAxis);
		
		createDryAdiabats();
		createWetAdiabats();
		createMixingLines();
	}
	
	private void createWetAdiabats() {
		final float dash[] = {10.0f, 10.0f};
		Stroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
								10.0f, dash, 0.0f);
		XYItemRenderer r = getRenderer();
		double step = 500;
		double x = -40;
		while(x < 50) {
			ArrayList<Point2D.Double> path = new ArrayList<Point2D.Double>();
			double T = x + 273.15;
			for(double h = 0; h <= 15000; h += step) {
				double p = 105000.0 * Math.pow((1 - 2.5577 * Math.pow(10, -5) * h), 5.35588);	// pressure (Pa)
				path.add(new Point2D.Double(p / 100.0, T - 273.15));
				double e = 6.11 * Math.pow(10.0, (7.5 * (T - 273.15)) / (237.7 + (T - 273.15)) );						// saturated vapor pressure
				double w = 621.97 * e / ((p / 100) - e);	// saturated mixing ratio (g/kg)
				w = w / 1000;
				double t = 9.8076 * (1 + ( (2230000 * w) / (287 * T) )) / (1007 + ( (Math.pow(2230000, 2)*w*0.6220) / (287*Math.pow(T, 2)) ));
				T = T - t*step;
			}
			r.addAnnotation(new SkewTPathAnnotation(path, stroke, wet), Layer.BACKGROUND);
			if(x > 0) {
				x += 5;
			} else {
				x += 10;
			}
		}
	}
	
	private void createDryAdiabats() {
		Stroke stroke = new BasicStroke(0.5f);
		XYItemRenderer r = getRenderer();
		for(double x = -40; x <= 90; x += 10) {
			ArrayList<Point2D.Double> path = new ArrayList<Point2D.Double>();
			for(double h = 0; h <= 20000; h += 500) {
				double p = 105000.0 * Math.pow((1 - 2.5577 * Math.pow(10, -5) * h), 5.35588);
				double t = x - (9.8 * (h / 1000.0));
				path.add(new Point2D.Double(p / 100.0, t));
			}
			r.addAnnotation(new SkewTPathAnnotation(path, stroke, dry), Layer.BACKGROUND);
		}
	}
	
	private void createMixingLines() {
		final float dash[] = {10.0f, 10.0f};
		Stroke stroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
								10.0f, dash, 0.0f);
		Paint paint = mixing;
		XYItemRenderer r = getRenderer();
		double y = 950;
		final double rot = -Math.PI / 3;
		// 0.1
		r.addAnnotation(new SkewTLineAnnotation(1050, -40.9, 100, -60.8378, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t01 = new XYTextAnnotation("0.1", y, -40);
		t01.setRotationAngle(rot);
		r.addAnnotation(t01);
		// 0.2
		r.addAnnotation(new SkewTLineAnnotation(1050, -34.137, 100, -55.3946, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t02 = new XYTextAnnotation("0.2", y, -34);
		t02.setRotationAngle(rot);
		r.addAnnotation(t02);
		// 0.4
		r.addAnnotation(new SkewTLineAnnotation(1050, -26.8943, 100, -49.6071, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t04 = new XYTextAnnotation("0.4", y, -26);
		t04.setRotationAngle(rot);
		r.addAnnotation(t04);
		// 0.6
		r.addAnnotation(new SkewTLineAnnotation(1050, -22.4151, 100, -46.0493, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t06 = new XYTextAnnotation("0.6", y, -21);
		t06.setRotationAngle(rot);
		r.addAnnotation(t06);
		// 1g/kg
		r.addAnnotation(new SkewTLineAnnotation(1050, -14.4964, 100, -41.3729, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t1 = new XYTextAnnotation("1.0", y, -14.0);
		t1.setRotationAngle(rot);
		r.addAnnotation(t1);
		// 2g/kg
		r.addAnnotation(new SkewTLineAnnotation(1050, -7.93414, 100, -34.6573, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t2 = new XYTextAnnotation("2.0", y, -7);
		t2.setRotationAngle(rot);
		r.addAnnotation(t2);
		// 3
		r.addAnnotation(new SkewTLineAnnotation(1050, -2.62, 100, -30.5186, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t3 = new XYTextAnnotation("3.0", y, -2.0);
		t3.setRotationAngle(rot);
		r.addAnnotation(t3);
		// 4g/kg
		r.addAnnotation(new SkewTLineAnnotation(1050, 1.2955, 100, -27.4833, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t4 = new XYTextAnnotation("4.0", y, 2.0);
		t4.setRotationAngle(rot);
		r.addAnnotation(t4);
		// 5
		r.addAnnotation(new SkewTLineAnnotation(1050, 4.41855, 100, -25.0709, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t5 = new XYTextAnnotation("5.0", y, 5.0);
		t5.setRotationAngle(rot);
		r.addAnnotation(t5);
		// 6
		r.addAnnotation(new SkewTLineAnnotation(1050, 7.02728, 100, -23.0616, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t6 = new XYTextAnnotation("6.0", y, 7.5);
		t6.setRotationAngle(rot);
		r.addAnnotation(t6);
		// 8g/kg
		r.addAnnotation(new SkewTLineAnnotation(1050, 11.2498, 100, -19.8204, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t8 = new XYTextAnnotation("8.0", y, 11.75);
		t8.setRotationAngle(rot);
		r.addAnnotation(t8);
		// 10
		r.addAnnotation(new SkewTLineAnnotation(1050, 14.6159, 100, -17.2465, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t10 = new XYTextAnnotation("10", y, 15.0);
		t10.setRotationAngle(rot);
		r.addAnnotation(t10);
		// 15
		r.addAnnotation(new SkewTLineAnnotation(1050, 20.9365, 100, -12.4366, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t15 = new XYTextAnnotation("15", y, 21.4);
		t15.setRotationAngle(rot);
		r.addAnnotation(t15);
		// 20
		r.addAnnotation(new SkewTLineAnnotation(1050, 25.5789, 100, -8.92309, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t20 = new XYTextAnnotation("20", y, 26.1);
		t20.setRotationAngle(rot);
		r.addAnnotation(t20);
		// 30
		r.addAnnotation(new SkewTLineAnnotation(1050, 32.3335, 100, -3.83991, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t30 = new XYTextAnnotation("30", y, 32.8);
		t30.setRotationAngle(rot);
		r.addAnnotation(t30);
		// 40
		r.addAnnotation(new SkewTLineAnnotation(1050, 37.2617, 100, -0.152649, stroke, paint), Layer.BACKGROUND);
		XYTextAnnotation t40 = new XYTextAnnotation("40", y, 37.8);
		t40.setRotationAngle(rot);
		r.addAnnotation(t40);
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
	
	protected Line2D.Double transformLine(double ix1, double iy1, double ix2, double iy2,
											XYPlot plot, Rectangle2D plotArea,
											ValueAxis domainAxis, ValueAxis rangeAxis) {
		PlotOrientation orientation = plot.getOrientation();
        RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(
                plot.getDomainAxisLocation(), orientation);
        RectangleEdge rangeEdge = Plot.resolveRangeAxisLocation(
                plot.getRangeAxisLocation(), orientation);
        
        double y1 = domainAxis.valueToJava2D(ix1, plotArea, domainEdge);
        double y2 = domainAxis.valueToJava2D(ix2, plotArea, domainEdge);
        double x1 = rangeAxis.valueToJava2D(iy1, plotArea, rangeEdge);
        x1 += plotArea.getMaxY() - y1;
        double x2 = rangeAxis.valueToJava2D(iy2, plotArea, rangeEdge);
        x2 += plotArea.getMaxY() - y2;
        
        return new Line2D.Double(x1, y1, x2, y2);
	}
	
	class SkewTPathAnnotation extends AbstractXYAnnotation {
		
		ArrayList<Point2D.Double> path;
		Stroke stroke;
		Paint paint;
		
		public SkewTPathAnnotation(ArrayList<Point2D.Double> path, Stroke stroke, Paint paint) {
			this.path = path;
			this.stroke = stroke;
			this.paint = paint;
		}
		
		@Override
		public void draw(Graphics2D g2, XYPlot plot, Rectangle2D dataArea,
				ValueAxis domainAxis, ValueAxis rangeAxis, int rendererIndex, PlotRenderingInfo info) {
			g2.setStroke(stroke);
			g2.setPaint(paint);
			Iterator<Point2D.Double> itr = path.iterator();
			Point2D.Double c = null, p = null;
			if(itr.hasNext()) c = itr.next();
			while(itr.hasNext()) {
				p = c;
				c = itr.next();
				g2.draw(transformLine(p.x, p.y, c.x, c.y, plot, dataArea, domainAxis, rangeAxis));
			}
		}
		
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
			g2.setStroke(stroke);
			g2.setPaint(paint);
			g2.draw(transformLine(x1, y1, x2, y2, plot, dataArea, domainAxis, rangeAxis));
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
