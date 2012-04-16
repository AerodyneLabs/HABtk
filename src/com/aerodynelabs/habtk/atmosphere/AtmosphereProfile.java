package com.aerodynelabs.habtk.atmosphere;

//XXX test atmosphere profile

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
		AtmosphereState next = null;
		int i = 0;
		while(itr.hasNext()) {
			next = itr.next();
			i++;
			if(alt > next.h) {
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
		
		double hp = getGeopotential(alt);
		base = next;
		base.h = getGeopotential(base.h);
		//TODO approximate for values above defined data
		
		
		return null;
	}
	
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
