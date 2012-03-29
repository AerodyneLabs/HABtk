package com.aerodynelabs.habtk.prediction;

public class AtmosphereModel {
	
	protected int startTime;
	protected int endTime;
	protected double lat = 0.0;
	protected double lon = 0.0;
	protected double resolution = 0.0;
	
	protected AtmosphereProfile start;
	protected AtmosphereProfile end;
	
	public AtmosphereModel(int startTime, int endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public AtmosphereModel(int startTime, int endTime, double lat, double lon) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.lat = lat;
		this.lon = lon;
	}
	
	public AtmosphereModel(int startTime, int endTime, double lat, double lon, double resolution) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.lat = lat;
		this.lon = lon;
		this.resolution = resolution;
	}
	
	public boolean isValid(int time) {
		//TODO isValid
		return true;
	}
	
	public boolean isValid(int time, double lat, double lon) {
		//TODO isValid
		return true;
	}
	
	public AtmosphereState getAtAltitude(int time, double alt) {
		AtmosphereState s = start.getAtAltitude(alt);
		AtmosphereState e = end.getAtAltitude(alt);
		double x = (time - startTime) / (endTime - startTime);
		//TODO Atmosphere model
		return null;
	}

}
