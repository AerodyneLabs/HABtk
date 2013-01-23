package com.aerodynelabs.habtk.tracking;

import java.io.File;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.aerodynelabs.habtk.logging.TrackingFormatter;
import com.aerodynelabs.map.MapPoint;

public class TrackingService implements Runnable {
	
	private static final Logger debugLog = Logger.getLogger("Debug");
	private static final Logger primaryLog = Logger.getLogger("Primary");
	private static final Logger secondaryLog = Logger.getLogger("Secondary");
	private static final Logger recoveryLog = Logger.getLogger("Recovery");
	
	// Status variables
	private boolean stop = false;
	private boolean enabled = false;
	private boolean nearby = false;
	
	private Tracker primary = null;
	private Tracker secondary = null;
	private Tracker recovery = null;
	
	private Vector<PositionListener> listeners;
	
	public TrackingService() {
		listeners = new Vector<PositionListener>();
		File logStore = new File("flights/current");
		logStore.mkdirs();
		try {
			FileHandler primaryFile = new FileHandler("flights/current/primary.log");
			primaryFile.setFormatter(new TrackingFormatter());
			FileHandler secondaryFile = new FileHandler("flights/current/secondary.log");
			secondaryFile.setFormatter(new TrackingFormatter());
			FileHandler recoveryFile = new FileHandler("flights/current/recovery.log");
			recoveryFile.setFormatter(new TrackingFormatter());
			primaryLog.addHandler(primaryFile);
			secondaryLog.addHandler(secondaryFile);
			recoveryLog.addHandler(recoveryFile);
		} catch(Exception e) {
			System.err.println("Problem creating tracking logs:");
			e.printStackTrace();
			debugLog.log(Level.SEVERE, "Exception", e);
			
		}
		
		new Thread(this, "Tracking Service").start();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean e) {
		enabled = e;
	}
	
	public void stop() {
		stop = true;
	}
	
	public void addListener(final PositionListener listener) {
		listeners.add(listener);
	}
	
	public boolean removeListener(PositionListener listener) {
		return listeners.remove(listener);
	}
	
	public void setMapNearby(boolean x) {
		nearby = x;
	}
	
	public void setPrimary(Tracker t) {
		primary = t;
	}
	
	public void setSecondary(Tracker t) {
		secondary = t;
	}
	
	public void setRecovery(Tracker t) {
		recovery = t;
	}
	
	public Tracker getPrimary() {
		return primary;
	}
	
	public Tracker getSecondary() {
		return secondary;
	}
	
	public Tracker getRecovery() {
		return recovery;
	}
	
	public boolean getMapNearby() {
		return nearby;
	}
	
	private void notifyListeners(int source, MapPoint pkt) {
		for(PositionListener listener : listeners) {
			listener.positionUpdateEvent(new PositionEvent(source, pkt));
		}
	}

	@Override
	public void run() {
		while(stop != true) {
			while(primary != null && primary.isReady()) {
				MapPoint pkt = primary.getPacket();
				if(pkt != null && enabled == true) {
					notifyListeners(PositionEvent.PRIMARY, pkt);
					primaryLog.log(Level.INFO, pkt.toString());
				}
			}
			while(secondary != null && secondary.isReady()) {
				MapPoint pkt = secondary.getPacket();
				if(pkt != null && enabled == true) {
					notifyListeners(PositionEvent.SECONDARY, pkt);
					secondaryLog.log(Level.INFO, pkt.toString());
				}
			}
			while(recovery != null && recovery.isReady()) {
				MapPoint pkt = recovery.getPacket();
				if(pkt != null && enabled == true) {
					notifyListeners(PositionEvent.RECOVERY, pkt);
					recoveryLog.log(Level.INFO, pkt.toString());
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				stop = true;
				e.printStackTrace();
				debugLog.log(Level.SEVERE, "Exception", e);
			}
		}
	}

}
