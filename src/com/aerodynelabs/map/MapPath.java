package com.aerodynelabs.map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A 3d path
 * 
 * @author Ethan Harstad
 *
 */
public class MapPath {
	
	private String name;
	private double boundNorth = 0.0;
	private double boundSouth = 0.0;
	private double boundEast = 0.0;
	private double boundWest = 0.0;
	private double maxAlt = 0.0;
	private long startTime = 0;
	private long endTime = 0;
	
	private LinkedList<MapPoint> path;
	private ArrayList<MapPoint> markers;
	
	/**
	 * Create a new path.
	 */
	public MapPath() {
		path = new LinkedList<MapPoint>();
		markers = new ArrayList<MapPoint>();
	}
	
	/**
	 * Create a new path with the given name.
	 * @param name
	 */
	public MapPath(String name) {
		this();
		this.name = name;
	}
	
	/**
	 * Create a path from the given sequence of points.
	 * @param path
	 */
	public MapPath(List<MapPoint> in) {
		this();
		Iterator<MapPoint> itr = in.iterator();
		while(itr.hasNext()) {
			add(itr.next());
		}
	}
	
	/**
	 * Add the given marker to the path.
	 * @param mark
	 */
	public void addMarker(MapPoint mark) {
		markers.add(mark);
	}
	
	/**
	 * Add the given named marker to the path.
	 * @param name
	 * @param lat
	 * @param lon
	 */
	public void addMarker(String name, double lat, double lon) {
		markers.add(new MapPoint(lat, lon, 0, 0, name));
	}
	
	/**
	 * Get the first point of this path.
	 * @return
	 */
	public MapPoint getFirst() {
		return path.getFirst();
	}
	
	/**
	 * Get the last ponit of this path.
	 * @return
	 */
	public MapPoint getLast() {
		return path.getLast();
	}
	
	/**
	 * Get the distance between the start and end of this path.
	 * @return Distance in meters
	 */
	public double getDistance() {
		try {
		double dLat = Math.toRadians(path.getLast().getLatitude() - path.getFirst().getLatitude());
		double dLon = Math.toRadians(path.getLast().getLongitude() - path.getFirst().getLongitude());
		double sLat = Math.toRadians(path.getFirst().getLatitude());
		double fLat = Math.toRadians(path.getLast().getLatitude());
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(sLat) * Math.cos(fLat);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		return c * 6371000;
		} catch(Exception e) {
			return 0.0d;
		}
	}
	
	/**
	 * Get the name of this path.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of this path.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the maximum altitude of this path.
	 * @return
	 */
	public double getMaxAlt() {
		return maxAlt;
	}
	
	/**
	 * Get the first time in the path.
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * Get the last time in the path.
	 * @return
	 */
	public long getEndTime() {
		return endTime;
	}
	
	/**
	 * Get the time between the start and end of the path.
	 * @return
	 */
	public long getElapsedTime() {
		return endTime - startTime;
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
	
	/**
	 * Append the point to the end of this path.
	 * @param point
	 */
	public void add(MapPoint point) {
		updateBounds(point.getLatitude(), point.getLongitude());
		if(point.getAltitude() > 0) {
			double alt = point.getAltitude();
			if(alt > maxAlt) maxAlt = alt;
		}
		if(point.getTime() > 0) {
			long time = point.getTime();
			if(time < startTime || startTime == 0) startTime = time;
			if(time > endTime) endTime = time;
		}
		path.add(point);
	}
	
	/**
	 * Append the point to the end of this path.
	 * @param lat
	 * @param lon
	 */
	public void add(double lat, double lon) {
		updateBounds(lat, lon);
		path.add(new MapPoint(lat, lon));
	}
	
	/**
	 * Append the point to the end of this path.
	 * @param lat
	 * @param lon
	 * @param alt
	 */
	public void add(double lat, double lon, double alt) {
		updateBounds(lat, lon);
		if(alt > maxAlt) maxAlt = alt;
		path.add(new MapPoint(lat, lon, alt));
	}
	
	/**
	 * Append the point to the end of this path.
	 * @param lat
	 * @param lon
	 * @param alt
	 * @param time
	 */
	public void add(double lat, double lon, double alt, long time) {
		updateBounds(lat, lon);
		if(alt > maxAlt) maxAlt = alt;
		if(time < startTime || startTime == 0) startTime = time;
		if(time > endTime) endTime = time;
		path.add(new MapPoint(lat, lon, alt, time));
	}
	
	/**
	 * Append the sequence of points to the end of this path.
	 * @param path
	 */
	public void addAll(List<MapPoint> in) {
		Iterator<MapPoint> itr = in.iterator();
		while(itr.hasNext()) {
			add(itr.next());
		}
	}
	
	/**
	 * Get the northernmost point of this path.
	 * @return
	 */
	public double getNorthBound() {
		return boundNorth;
	}
	
	/**
	 * Get the southernmost point of this path.
	 * @return
	 */
	public double getSouthBound() {
		return boundSouth;
	}
	
	/**
	 * Get the easternmost point of this path.
	 * @return
	 */
	public double getEastBound() {
		return boundEast;
	}
	
	/**
	 * Get the westernmost point of this path.
	 * @return
	 */
	public double getWestBound() {
		return boundWest;
	}
	
	/**
	 * Get the path.
	 * @return
	 */
	public LinkedList<MapPoint> getPath() {
		return path;
	}
	
	/**
	 * Get a list of the markers in the path.
	 * @return
	 */
	public List<MapPoint> getMarkers() {
		return markers;
	}
	
	/**
	 * Test if this path extends into the given bounds.
	 * @param north
	 * @param east
	 * @param south
	 * @param west
	 * @return
	 */
	public boolean inBounds(double north, double east, double south, double west) {
		if(south > boundNorth) return false;	// Region is above of bounds
		if(north < boundSouth) return false;	// Region is below of bounds
		if(east < boundWest) return false;		// Region is left of bounds
		if(west > boundEast) return false;		// Region is right of bounds
		
		return true;
	}
	
	/**
	 * Get an iterator over the path.
	 * @return
	 */
	public ListIterator<MapPoint> iterator() {
		return path.listIterator();
	}
	
	/**
	 * Export the path to a CSV file.
	 * @param file
	 * @return
	 */
	public boolean export(File file) {
		try(
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
		) {
			ListIterator<MapPoint> itr = path.listIterator();
			while(itr.hasNext()) {
				out.println(itr.next());
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Export the path to a KML file.
	 * @param file
	 * @return
	 */
	public boolean exportKML(File file) {
		KML kml = new KML();
		kml.addPath(path, name);
		ListIterator<MapPoint> itr = markers.listIterator();
		while(itr.hasNext()) {
			kml.addMark(itr.next());
		}
		return kml.writeFile(file);
	}

}
