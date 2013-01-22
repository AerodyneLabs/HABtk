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

/**
 * A default overlay to draw MapPaths with paths and points.
 * @author Ethan Harstad
 *
 */
public class MapOverlay {
	
	private String name;
	private Hashtable<String, MapPath> paths;
	private boolean enabled = true;
	private boolean drawPaths = true;
	private boolean highlighted = false;
	private Color color = Color.BLACK;
	
	/**
	 * Create a name overlay.
	 * @param name
	 */
	public MapOverlay(String name) {
		this.name = name;
		paths = new Hashtable<String, MapPath>();
	}
	
	/**
	 * Get the name of this overlay.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Add the named path to this overlay.
	 * @param name
	 * @param path
	 */
	public void addPath(String name, MapPath path) {
		paths.put(name, path);
	}
	
	/**
	 * Test if the given path name is in this overlay.
	 * @param name
	 * @return
	 */
	public boolean hasPath(String name) {
		return paths.containsKey(name);
	}
	
	/**
	 * Get the named path.
	 * @param name
	 * @return
	 */
	public MapPath getPath(String name) {
		return paths.get(name);
	}
	
	/**
	 * Remove the named path.
	 * @param name
	 */
	public void removePath(String name) {
		paths.remove(name);
	}
	
	/**
	 * Append the given point to the named path.
	 * @param name
	 * @param point
	 */
	public void appendPath(String name, MapPoint point) {
		paths.get(name).add(point.lat, point.lon);
	}
	
	/**
	 * Enable the overlay.
	 * @param enable
	 */
	public void setEnabled(boolean enable) {
		enabled = enable;
	}
	
	/**
	 * Test if enabled.
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Enable drawing of map paths;
	 * @param draw
	 */
	public void setDrawPaths(boolean draw) {
		drawPaths = draw;
	}
	
	/**
	 * Test if paths are drawn.
	 * @return
	 */
	public boolean getDrawPaths() {
		return drawPaths;
	}
	
	/**
	 * Set the color to use to draw this overlay.
	 * @param c
	 */
	public void setColor(Color c) {
		color = c;
	}
	
	/**
	 * Get the color of this overlay.
	 * @return
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Set the highlighted state of the overlay.
	 * @param state
	 */
	public void setHighlighted(boolean state) {
		highlighted = state;
	}
	
	/**
	 * Get the highlighted state of the overlay.
	 * @return
	 */
	public boolean getHighlighted() {
		return highlighted;
	}
	
	/**
	 * Draw this overlay on the given map with the given graphics context.
	 * @param map
	 * @param g0
	 */
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
			if(highlighted) {
				g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			} else {
				g.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			}
			
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
			ListIterator<MapPoint> iter = p.getMarkers().listIterator();
			while(iter.hasNext()) {
				MapPoint mark = iter.next();
				int x = map.getLonPos(mark.getLongitude());
				int y = map.getLatPos(mark.getLatitude());
				g.setColor(Color.GREEN);
				g.fillOval(x-3, y-3, 7, 7);
				g.setColor(Color.BLACK);
				g.setFont(new Font("SansSerif", Font.PLAIN, 9));
				FontMetrics metrics = g.getFontMetrics();
				String name = mark.getName();
				if(name != null) g.drawString(name, x-(metrics.stringWidth(name)/2), y-(metrics.getHeight()/2));
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
