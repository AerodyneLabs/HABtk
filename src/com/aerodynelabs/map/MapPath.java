package com.aerodynelabs.map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class MapPath {
	
	private String name;
	private double boundNorth = 0.0;
	private double boundSouth = 0.0;
	private double boundEast = 0.0;
	private double boundWest = 0.0;
	
	private LinkedList<MapPoint> path;
	
	public MapPath() {
		path = new LinkedList<MapPoint>();
	}
	
	public MapPath(String name) {
		this();
		this.name = name;
	}
	
	public MapPath(List<MapPoint> path) {
		this();
		this.path.addAll(path);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	private void updateBounds(double lat, double lon) {
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
	}
	
	public void add(double lat, double lon) {
		updateBounds(lat, lon);
		
		// Add to end of path
		path.add(new MapPoint(lat, lon));
	}
	
	public void add(double lat, double lon, double alt) {
		updateBounds(lat, lon);
		path.add(new MapPoint(lat, lon, alt));
	}
	
	public void add(double lat, double lon, double alt, long time) {
		updateBounds(lat, lon);
		path.add(new MapPoint(lat, lon, alt, time));
	}
	
	public void addAll(List<MapPoint> path) {
		this.path.addAll(path);
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
	
	public boolean export(File file) {
		PrintWriter out;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		ListIterator<MapPoint> itr = path.listIterator();
		while(itr.hasNext()) {
			out.println(itr.next());
		}
		
		return true;
	}

}
