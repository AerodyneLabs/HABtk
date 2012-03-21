package com.aerodynelabs.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Hashtable;
import java.util.ListIterator;

public class MapOverlay {
	
	private Hashtable<String, MapPath> paths;
	
	public MapOverlay() {
		paths = new Hashtable<String, MapPath>();
	}
	
	public void addPath(String name, MapPath path) {
		paths.put(name, path);
	}
	
	public void drawOverlay(MapPanel map, Graphics g0) {
		Collection<MapPath> c = paths.values();
		for(MapPath p : c) {
			if(!p.inBounds(map.getNorthBound(), map.getEastBound(),
					map.getSouthBound(), map.getWestBound())) continue;
			
			Graphics2D g = (Graphics2D)g0.create();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(2));
			
			ListIterator<Point2D.Double> iter = p.iterator();
			if(!iter.hasNext()) continue;
			Point2D.Double pp = iter.next();
			Point2D.Double cp = null;
			while(iter.hasNext()) {
				cp = iter.next();
				g.drawLine(map.getLonPos(pp.x), map.getLatPos(pp.y),
						map.getLonPos(cp.x), map.getLatPos(cp.y));
				pp = cp;
			}
			if(cp != null) {
				g.setColor(Color.GREEN);
				g.fillOval(map.getLonPos(cp.x)-3, map.getLatPos(cp.y)-3, 7, 7);
			}
		}
	}

}
