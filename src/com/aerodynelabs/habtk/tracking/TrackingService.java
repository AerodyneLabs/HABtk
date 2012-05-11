package com.aerodynelabs.habtk.tracking;

import java.util.Vector;

import com.aerodynelabs.map.MapPoint;

public class TrackingService implements Runnable {
	
	// Status variables
	private boolean stop = false;
	private boolean enabled = false;
	private boolean nearby = false;
	
	private Tracker primary = null;
	private Tracker secondary = null;
	private Tracker recovery = null;
	
	private Vector<PositionListener> listeners;
	
	public TrackingService() {
		new Thread(this).start();
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
	
	public void addListener(PositionListener listener) {
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
		System.out.println(pkt);
		for(PositionListener listener : listeners) {
			listener.positionUpdateEvent(new PositionEvent(source, pkt));
		}
	}

	@Override
	public void run() {
		while(stop != true) {
			while(primary != null && primary.isReady()) {
				MapPoint pkt = primary.getPacket();
				if(pkt != null && enabled == true) notifyListeners(PositionEvent.PRIMARY, pkt);
			}
			while(secondary != null && secondary.isReady()) {
				MapPoint pkt = secondary.getPacket();
				if(pkt != null && enabled == true) notifyListeners(PositionEvent.SECONDARY, pkt);
			}
			while(recovery != null && recovery.isReady()) {
				MapPoint pkt = recovery.getPacket();
				if(pkt != null && enabled == true) notifyListeners(PositionEvent.RECOVERY, pkt);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				stop = true;
				e.printStackTrace();
			}
		}
	}

}
