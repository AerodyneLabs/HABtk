package com.aerodynelabs.habtk.tracking;

import com.aerodynelabs.map.MapPoint;

public class TrackingService implements Runnable {
	
	// Status variables
	private boolean stop = false;
	private boolean enabled = false;
	private boolean nearby = false;
	
	private Tracker primary = null;
	private Tracker secondary = null;
	private Tracker recovery = null;
	
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
	
	public void addListener() {
		// TODO addListener
	}
	
	public boolean removeListener() {
		// TODO removeListener
		return false;
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
	
	private void notifyListeners(MapPoint pkt) {
		System.out.println(pkt);
	}

	@Override
	public void run() {
		while(stop != true) {
			// TODO
			while(primary != null && primary.isReady()) {
				MapPoint pkt = primary.getPacket();
				if(pkt != null && enabled == true) notifyListeners(pkt);
			}
			while(secondary != null && secondary.isReady()) {
				MapPoint pkt = secondary.getPacket();
				if(pkt != null && enabled == true) notifyListeners(pkt);
			}
			while(recovery != null && recovery.isReady()) {
				MapPoint pkt = recovery.getPacket();
				if(pkt != null && enabled == true) notifyListeners(pkt);
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
