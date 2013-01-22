package com.aerodynelabs.habtk.atmosphere;

import java.io.File;

public interface AtmosphereSource {
	
	public File getAtmosphere(int time, double lat, double lon);

}
