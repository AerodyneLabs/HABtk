package com.aerodynelabs.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
			if(cp != null) {
				g.setColor(Color.GREEN);
				g.fillOval(map.getLonPos(cp.lon)-3, map.getLatPos(cp.lat)-3, 7, 7);
			}
		}
	}

}
