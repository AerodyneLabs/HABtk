package com.aerodynelabs.habtk.prediction;

import java.io.File;

public interface AtmosphereParser {
	
	public AtmosphereProfile parseAtmosphere(File  file);

}
