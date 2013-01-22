package com.aerodynelabs.habtk.tracking;

import com.aerodynelabs.map.MapPoint;

public class PositionEvent {
	
	public final static int PRIMARY = 0;
	public final static int SECONDARY = 1;
	public final static int RECOVERY = 2;
	public final static int BURST = 3;
	public final static int LANDING = 4;
	
	private int source;
	private MapPoint position;
	
	public PositionEvent(int source, MapPoint position) {
		this.source = source;
		this.position = position;
	}
	
	public int getSource() {
		return source;
	}
	
	public MapPoint getPosition() {
		return position;
	}

}
