package com.aerodynelabs.habtk.prediction;

//XXX test atmosphere model

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
		if(time < startTime) return false;
		if(time > endTime) return false;
		return true;
	}
	
	public boolean isValid(int time, double lat, double lon) {
		if(time < startTime) return false;
		if(time > endTime) return false;
		if(resolution == 0.0) return true;
		if(Math.abs(this.lat - lat) > resolution) return false;
		if(Math.abs(this.lon - lon) > resolution) return false;
		return true;
	}
	
	public AtmosphereState getAtAltitude(int time, double alt) {
		AtmosphereState s = start.getAtAltitude(alt);
		AtmosphereState e = end.getAtAltitude(alt);
		
		double tStep = endTime - startTime;
		double deltaT = time - startTime;
		double dt = deltaT / tStep;
		
		double p = (e.p - s.p) * dt + s.p;
		double t = (e.t - s.t) * dt + s.t;
		double d = (e.dp - s.dp) * dt + s.dp;
		double spd = (e.ws - s.ws) * dt + s.ws;
		double ddir = e.wd - s.wd;
		if(Math.abs(ddir) > 180) {
			if(ddir > 0) {
				ddir = ddir - 360;
			} else {
				ddir = ddir + 360;
			}
		}
		double dir = ddir * dt + s.wd;

		return new AtmosphereState(alt, p, t, d, dir, spd);
	}

}
