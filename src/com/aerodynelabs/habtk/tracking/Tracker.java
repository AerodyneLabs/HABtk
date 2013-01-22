package com.aerodynelabs.habtk.tracking;

import javax.swing.JOptionPane;

import com.aerodynelabs.map.MapPoint;

public abstract class Tracker {
	
	private static Object[] options = {
		"APRS"
	};
	
	private static String select() {
		String val = (String)JOptionPane.showInputDialog(
				null, "Choose a tracking source:\n", "New Tracker Type",
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		return val;
	}
	
	public static Tracker create() {
		Tracker tracker = null;
		
		String type = select();
		if(type == null) return null;
		if(type.equals("APRS")) {
			tracker = new APRSTracker();
		}
		
		if(tracker.setup()) {
			return tracker;
		}
		return null;
	}
	
	public abstract boolean setup();
	public abstract String toString();
	public abstract boolean isReady();
	public abstract MapPoint getPacket();

}
