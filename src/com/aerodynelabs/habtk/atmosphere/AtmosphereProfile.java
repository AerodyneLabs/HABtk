package com.aerodynelabs.habtk.atmosphere;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class AtmosphereProfile {
	
	protected long startTime = 0;
	protected long endTime;
	protected double lat;
	protected double lon;
	protected double resolution = 0.0;
	
	protected ArrayList<AtmosphereState> data;
	protected ArrayList<AtmosphereState> roc;
	
	private static final double R = 8.31432;
	private static final double MW = 0.0289644;
	private static final double G = 9.080665;
	
	public AtmosphereProfile() {
		this(0, 0);
	}
	
	public AtmosphereProfile(int startTime) {
		this(startTime, startTime + 10800);
	}
	
	public AtmosphereProfile(int startTime, int endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		
		data = new ArrayList<AtmosphereState>(10);
		roc = new ArrayList<AtmosphereState>(10);
	}
	
	public void setCenter(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public boolean isValid(long time) {
		if(time < startTime) return false;
		if(time > endTime) return false;
		return true;
	}
	
	public boolean isValid(long time, double lat, double lon) {
		if(time < startTime) return false;
		if(time > endTime) return false;
		if(resolution == 0.0) return true;
		if(Math.abs(this.lat - lat) > resolution) return false;
		if(Math.abs(this.lon - lon) > resolution) return false;
		return true;
	}
	
	public Iterator<AtmosphereState> iterator() {
		return data.iterator();
	}
	
	public void addData(double altitude, double pressure, double temperature, double dewPoint, double windDirection, double windSpeed) {
		data.add(new AtmosphereState(altitude, pressure, temperature, dewPoint, windDirection, windSpeed));
		Collections.sort(data);
		
		roc.clear();
		Iterator<AtmosphereState> itr = data.iterator();
		AtmosphereState cur = itr.next();
		while(itr.hasNext()) {
			AtmosphereState next = itr.next();
			double dh = next.h - cur.h;
			double dp = next.p - cur.p;
			double dt = next.t - cur.t;
			double dd = next.dp - cur.dp;
			double ds = next.ws - cur.ws;
			double ddir = next.wd - cur.wd;
			if(Math.abs(ddir) > 180) {
				if(ddir > 0) {
					ddir = ddir - 360;
				} else {
					ddir = ddir + 360;
				}
			}
			roc.add(new AtmosphereState(dh, dp/dh, dt/dh, dd/dh, ddir/dh, ds/dh));
			cur = next;
		}
	}
	
	public AtmosphereState getAtAltitude(double alt) {
		Iterator<AtmosphereState> itr = data.iterator();
		AtmosphereState base = itr.next();
		if(alt < base.h) return null;
		AtmosphereState next = null;
		int i = -1;
		while(itr.hasNext()) {
			next = itr.next();
			i++;
			if(alt >= next.h) {
				base = next;
				continue;
			}
			
			AtmosphereState dd = roc.get(i);
			double dh = alt - base.h;
			return new AtmosphereState(
					alt,
					dd.p * dh + base.p,
					dd.t * dh + base.t,
					dd.dp * dh + base.dp,
					dd.wd * dh + base.wd,
					dd.ws * dh + base.ws);
		}

		//XXX Add wind speed extrapolation
		//XXX Add dew point extrapolation
		double Hb = base.h;
		double Tb = base.t + 273.15;
		double Pb = base.p;
		
		if(Hb < 11000) {
			if(alt < 11000) {
				double T = Tb - (0.0065 * (alt - Hb));
				double P = Pb * Math.pow((Tb - (0.0065*(alt - Hb)))/Tb, -(G * MW)/(R * -0.0065));
				return new AtmosphereState(alt, P, T - 273.15, base.dp, base.wd, base.ws);
			}
			double T = Tb - (0.0065 * (11000 - Hb));
			double P = Pb * Math.pow((Tb - (0.0065*(11000 - Hb)))/Tb, -(G * MW)/(R * -0.0065));
			Hb = 11000;
			Tb = T;
			Pb = P;
		}
		if(Hb < 20000) {
			if(alt < 20000) {
				double T = Tb;
				double P = Pb * Math.exp(-(G*MW*(alt - Hb))/(R*Tb));
				return new AtmosphereState(alt, P, T - 273.15, base.dp, base.wd, base.ws);
			}
			double T = Tb;
			double P = Pb * Math.exp(-(G*MW*(20000 - Hb))/(R*Tb));
			Hb = 20000;
			Tb = T;
			Pb = P;
		}
		if(Hb < 32000) {
			if(alt < 32000) {
				double T = Tb + (0.001 * (alt - Hb));
				double P = Pb * Math.pow((Tb + (0.001*(alt - Hb)))/Tb, -(G * MW)/(R * 0.001));
				return new AtmosphereState(alt, P, T - 273.15, base.dp, base.wd, base.ws);
			}
			double T = Tb + (0.001 * (32000 - Hb));
			double P = Pb * Math.pow((Tb + (0.001*(32000 - Hb)))/Tb, -(G * MW)/(R * 0.001));
			Hb = 32000;
			Tb = T;
			Pb = P;
		}
		if(Hb < 47000) {
			if(alt < 47000) {
				double T = Tb + (0.0028 * (alt - Hb));
				double P = Pb * Math.pow((Tb + (0.0028*(alt - Hb)))/Tb, -(G * MW)/(R * 0.0028));
				return new AtmosphereState(alt, P, T - 273.15, base.dp, base.wd, base.ws);
			}
			double T = Tb + (0.0028 * (47000 - Hb));
			double P = Pb * Math.pow((Tb + (0.0028*(47000 - Hb)))/Tb, -(G * MW)/(R * 0.0028));
			Hb = 47000;
			Tb = T;
			Pb = P;
		}
		
		
		return null;
	}
	
	@SuppressWarnings("unused")
	private double getGeopotential(double hg) {
		double RE = 63567660;
		return (hg * RE) / (RE + hg);
	}
	
	@SuppressWarnings("unused")
	private double getGeometric(double hp) {
		double RE = 63567660;
		return (hp * RE) / (RE - hp);
	}
	
	public String toString() {
		String ret = "Profile for " + lat + ", " + lon + " @ " + startTime + "\n";
		Iterator<AtmosphereState> itr = data.iterator();
		while(itr.hasNext()) {
			ret += itr.next().toString() + "\n";
		}
		return ret;
	}

}
