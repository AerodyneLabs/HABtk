package com.aerodynelabs.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.Hashtable;
import java.util.ListIterator;

public class MapOverlay {
	
	private String name;
	private Hashtable<String, MapPath> paths;
	private boolean enabled = true;
	private boolean drawPaths = true;
	private Color color = Color.BLACK;
	
	public MapOverlay(String name) {
		this.name = name;
		paths = new Hashtable<String, MapPath>();
	}
	
	public String getName() {
		return name;
	}
	
	public void addPath(String name, MapPath path) {
		paths.put(name, path);
	}
	
	public boolean hasPath(String name) {
		return paths.containsKey(name);
	}
	
	public MapPath getPath(String name) {
		return paths.get(name);
	}
	
	public void removePath(String name) {
		paths.remove(name);
	}
	
	public void appendPath(String name, MapPoint point) {
		paths.get(name).add(point.lat, point.lon);
	}
	
	public void setEnabled(boolean enable) {
		enabled = enable;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setDrawPaths(boolean draw) {
		drawPaths = draw;
	}
	
	public boolean getDrawPaths() {
		return drawPaths;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void drawOverlay(MapPanel map, Graphics g0) {
		if(!enabled) return;
		Collection<MapPath> c = paths.values();
		for(MapPath p : c) {
			if(!p.inBounds(map.getNorthBound(), map.getEastBound(),
					map.getSouthBound(), map.getWestBound())) continue;
			
			Graphics2D g = (Graphics2D)g0.create();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(color);
			g.setStroke(new BasicStroke(2));

			if(drawPaths) {
				ListIterator<MapPoint> iter = p.iterator();
				if(!iter.hasNext()) continue;
				MapPoint pp = iter.next();
				MapPoint cp = null;
				while(iter.hasNext()) {
					cp = iter.next();
					g.drawLine(map.getLonPos(pp.lon), map.getLatPos(pp.lat),
							map.getLonPos(cp.lon), map.getLatPos(cp.lat));
					pp = cp;
				}
			}
			if(p.getPath().getLast() != null) {
				MapPoint cp = p.getPath().getLast();
				g.setColor(Color.GREEN);
				int x = map.getLonPos(cp.lon);
				int y = map.getLatPos(cp.lat);
				g.fillOval(x-3, y-3, 7, 7);
				g.setColor(Color.BLACK);
				g.setFont(new Font("SansSerif", Font.PLAIN, 9));
				FontMetrics metrics = g.getFontMetrics();
				String name = p.getName();
				if(name != null) g.drawString(name, x-(metrics.stringWidth(name)/2), y-(metrics.getHeight()/2));
			}
		}
	}

}
