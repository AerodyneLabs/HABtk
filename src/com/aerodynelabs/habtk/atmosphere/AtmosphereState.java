package com.aerodynelabs.habtk.atmosphere;

public class AtmosphereState implements Comparable<AtmosphereState> {
	
	protected double 	h;
	protected double	p;
	protected double	t;
	protected double	dp;
	protected double	wd;
	protected double	ws;
	
	public AtmosphereState(double altitude, double pressure, double temperature, double dewPoint, double windDirection, double windSpeed) {
		h = altitude;
		p = pressure;
		t = temperature;
		dp = dewPoint;
		wd = windDirection;
		ws = windSpeed;
	}
	
	public double getAltitude() {
		return h;
	}
	
	public double getPressure() {
		return p;
	}
	
	public double getTemperature() {
		return t;
	}
	
	public double getDewPoint() {
		return dp;
	}
	
	public double getWindSpeed() {
		return ws;
	}
	
	public double getWindDirection() {
		return wd;
	}
	
	@Override
	public int compareTo(AtmosphereState x) {
		if(h < x.h) return -1;
		if(h > x.h) return 1;
		return 0;
	}
	
	@Override
	public String toString() {
		return "@" + h +"m: " + p + "Pa, " + t + "C, " + dp + "C, " + ws + "m/s from " + wd + "deg";
	}

}
