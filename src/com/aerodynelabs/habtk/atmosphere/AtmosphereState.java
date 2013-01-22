package com.aerodynelabs.habtk.atmosphere;

/**
 * A representation of the conditions of the atmosphere.
 * Units are managed by the user.
 * 
 * @author Ethan Harstad
 */
public class AtmosphereState implements Comparable<AtmosphereState> {
	
	protected double 	h;
	protected double	p;
	protected double	t;
	protected double	dp;
	protected double	wd;
	protected double	ws;
	
	/**
	 * Create a new state.
	 * @param altitude
	 * @param pressure
	 * @param temperature
	 * @param dewPoint
	 * @param windDirection
	 * @param windSpeed
	 */
	public AtmosphereState(double altitude, double pressure, double temperature, double dewPoint, double windDirection, double windSpeed) {
		h = altitude;
		p = pressure;
		t = temperature;
		dp = dewPoint;
		wd = windDirection;
		ws = windSpeed;
	}
	
	/**
	 * Get the altitude of this state.
	 * @return
	 */
	public double getAltitude() {
		return h;
	}
	
	/**
	 * Get the pressure of this state.
	 * @return
	 */
	public double getPressure() {
		return p;
	}
	
	/**
	 * Get the temperature of this state.
	 * @return
	 */
	public double getTemperature() {
		return t;
	}
	
	/**
	 * Get the dew point of this state.
	 * @return
	 */
	public double getDewPoint() {
		return dp;
	}
	
	/**
	 * Get the wind speed of this state.
	 * @return
	 */
	public double getWindSpeed() {
		return ws;
	}
	
	/**
	 * Get the wind direction of this state.
	 * @return
	 */
	public double getWindDirection() {
		return wd;
	}
	
	/**
	 * Compare the altitude of the given object to this object.
	 */
	@Override
	public int compareTo(AtmosphereState x) {
		if(h < x.h) return -1;
		if(h > x.h) return 1;
		return 0;
	}
	
	/**
	 * Get a human readable representation of this state.
	 */
	@Override
	public String toString() {
		return "@" + h +"m: " + p + "Pa, " + t + "C, " + dp + "C, " + ws + "m/s from " + wd + "deg";
	}

}
