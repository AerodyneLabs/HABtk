package com.aerodynelabs.habtk.prediction;

import java.io.File;

public interface AtmosphereSource {
	
	public File getAtmosphere(int time, double lat, double lon);

}
