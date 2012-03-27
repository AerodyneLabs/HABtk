package com.aerodynelabs.map;

import java.util.LinkedList;
import java.util.ListIterator;

public class MapPath {
	
	private double boundNorth = 0.0;
	private double boundSouth = 0.0;
	private double boundEast = 0.0;
	private double boundWest = 0.0;
	
	private LinkedList<MapPoint> path;
	
	public MapPath() {
		path = new LinkedList<MapPoint>();
	}
	
	public void add(double lat, double lon) {
		// Update path bounds
		if(lat > boundNorth) {
			boundNorth = lat;
		} else if(lat < boundSouth) {
			boundSouth = lat;
		}
		if(lon > boundEast) {
			boundEast = lon;
		} else if(lon < boundWest) {
			boundWest = lon;
		}
		
		// Add to end of path
		path.add(new MapPoint(lat, lon));
	}
	
	public double getNorthBound() {
		return boundNorth;
	}
	
	public double getSouthBound() {
		return boundSouth;
	}
	
	public double getEastBound() {
		return boundEast;
	}
	
	public double getWestBound() {
		return boundWest;
	}
	
	public LinkedList<MapPoint> getPath() {
		return path;
	}
	
	public boolean inBounds(double north, double east, double south, double west) {
		if(south > boundNorth) return false;	// Region is above of bounds
		if(north < boundSouth) return false;	// Region is below of bounds
		if(east < boundWest) return false;		// Region is left of bounds
		if(west > boundEast) return false;		// Region is right of bounds
		
		return true;
	}
	
	public ListIterator<MapPoint> iterator() {
		return path.listIterator();
	}

}
