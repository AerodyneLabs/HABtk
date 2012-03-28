package com.aerodynelabs.habtk.prediction;

public class AtmosphereFactory {
	
	public static AtmosphereProfile GetGFSProfile(double lat, double lon, long time) {
		AtmosphereSource src = new RUCGFS();
		AtmosphereParser parser = new GSDParser();
		return parser.parseAtmosphere(src.getAtmosphere((int)time, lat, lon));
	}

}
