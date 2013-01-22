package com.aerodynelabs.habtk.connectors;

public interface APRSSource {

	public boolean isValid();
	public boolean isReady();
	public String readLine();
	
}
