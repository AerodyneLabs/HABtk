package com.aerodynelabs.map;

public class MapPoint {
	
	protected double lat;
	protected double lon;
	protected double alt;
	protected long time;
	protected String name = null;
	
	public MapPoint(double latitude, double longitude) {
		lat = latitude;
		lon = longitude;
	}
	
	public MapPoint(double latitude, double longitude, double altitude) {
		lat = latitude;
		lon = longitude;
		alt = altitude;
	}
	
	public MapPoint(double latitude, double longitude, double altitude, long time) {
		lat = latitude;
		lon = longitude;
		alt = altitude;
		this.time = time;
	}
	
	public MapPoint(double latitude, double longitude, double altitude, long time, String name) {
		lat = latitude;
		lon = longitude;
		alt = altitude;
		this.time = time;
		this.name = name;
	}
	
	public double getLatitude() {
		return lat;
	}
	
	public double getLongitude() {
		return lon;
	}
	
	public double getAltitude() {
		return alt;
	}
	
	public long getTime() {
		return time;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return lat + ", " + lon + ", " + alt + ", " + time;
	}

}
