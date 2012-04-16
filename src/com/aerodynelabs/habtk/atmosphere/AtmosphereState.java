package com.aerodynelabs.habtk.atmosphere;

public class AtmosphereState implements Comparable<AtmosphereState> {
	
	double 	h;
	double	p;
	double	t;
	double	dp;
	double	wd;
	double	ws;
	
	public AtmosphereState(double altitude, double pressure, double temperature, double dewPoint, double windDirection, double windSpeed) {
		h = altitude;
		p = pressure;
		t = temperature;
		dp = dewPoint;
		wd = windDirection;
		ws = windSpeed;
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
