package com.aerodynelabs.habtk.tracking;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.aerodynelabs.map.MapPoint;

public class LocationService implements Runnable {
	
	private static final Logger debugLog = Logger.getLogger("Debug");
	
	private boolean run = true;
	private MapPoint staticLocation;
	
	private Vector<PositionListener> listeners;
	
	public LocationService() {
		listeners = new Vector<PositionListener>();
		new Thread(this, "Location Service").start();
	}
	
	public void addListener(PositionListener listener) {
		listeners.add(listener);
	}
	
	public boolean removeListener(PositionListener listener) {
		return listeners.remove(listener);
	}
	
	public void stop() {
		run = false;
	}
	
	public MapPoint getStaticLocation() {
		return staticLocation;
	}
	
	public void setStaticLocation(double lat, double lon, double alt) {
		MapPoint loc = new MapPoint(lat, lon, alt);
		staticLocation = loc;
		notifyListeners(loc);
	}
	
	private void notifyListeners(MapPoint location) {
		for(PositionListener listener : listeners) {
			listener.positionUpdateEvent(new PositionEvent(PositionEvent.LOCATION, location));
		}
	}

	@Override
	public void run() {
		while(run == true) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(500);
			} catch(InterruptedException e) {
				run = false;
				e.printStackTrace();
				debugLog.log(Level.SEVERE, "Exception", e);
			}
		}
	}

}
