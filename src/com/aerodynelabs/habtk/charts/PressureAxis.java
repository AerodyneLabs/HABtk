package com.aerodynelabs.habtk.charts;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.TickType;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;

@SuppressWarnings("serial")
public class PressureAxis extends LogAxis {
	
	private static final ArrayList<NumberTick> ticks = new ArrayList<NumberTick>();
	
	public PressureAxis(String name) {
		super(name);
		setInverted(true);
		setAutoRange(false);
		setRange(100.0, 1050.0);
		setMinorTickMarksVisible(true);
		setUpperMargin(0);
		setLowerMargin(0);
		
		for(int i = 100; i <= 1000; i += 100) {
			ticks.add(new NumberTick(TickType.MAJOR, i, createTickLabel(i), TextAnchor.CENTER_RIGHT, TextAnchor.CENTER, 0.0));
			ticks.add(new NumberTick(TickType.MINOR, i + 50, "", TextAnchor.CENTER_RIGHT, TextAnchor.CENTER, 0.0));
		}
	}
	
	@Override
	protected List<NumberTick> refreshTicksVertical(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {	
		return ticks;
	}
	
}
