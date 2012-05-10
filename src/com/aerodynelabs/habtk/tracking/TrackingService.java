package com.aerodynelabs.habtk.tracking;

public class TrackingService implements Runnable {
	
	// Status variables
	private boolean stop = false;
	private boolean enabled = false;
	
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

	@Override
	public void run() {
		while(stop != true) {
			// TODO
			
			if(enabled) {
				
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
