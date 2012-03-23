package com.aerodynelabs.habtk.prediction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class AtmosphereProfile {
	
	protected int startTime = 0;
	protected int endTime;
	protected double lat;
	protected double lon;
	protected double resolution = 0.0;
	
	private ArrayList<AtmosphereState> data;
	private ArrayList<AtmosphereState> roc;
	
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
	
	public boolean isValid(int time) {
		//TODO isValid
		return true;
	}
	
	public boolean isValid(int time, double lat, double lon) {
		//TODO isvalid
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
		//TODO approximate for values above defined data
		return null;
	}

}
