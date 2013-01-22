package com.aerodynelabs.habtk.charts;

import java.awt.BorderLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;

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
		setLayout(new BorderLayout());
		
		if(type == STANDARD) {
			tPlot = new TemperaturePlot();
		} else {
			tPlot = new SkewTLogPPlot();
		}
		wPlot = new WindPlot();
		PressureAxis axis = new PressureAxis("Pressure (hPa)");
		CombinedDomainXYPlot plot = new CombinedDomainXYPlot(axis);
		plot.setOrientation(PlotOrientation.HORIZONTAL);
		plot.add(tPlot, 2);
		plot.add(wPlot, 1);
//		wPlot.setDomainAxis(axis);
		plot.setGap(10);
		chart = new JFreeChart("Atmosphere Sounding", new Font(null, Font.BOLD, 18), plot, false);
		ChartPanel chartPanel = new ChartPanel(chart, 600, 400, 300, 200, 1920, 1200,
				true, false, true, true, false, false);
		add(chartPanel, BorderLayout.CENTER);
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
		chart.clearSubtitles();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		chart.addSubtitle(new TextTitle(sdf.format(sounding.getTime())));
	}
	
	public AtmosphereProfile getSounding() {
		return wPlot.getProfile();
	}

}
