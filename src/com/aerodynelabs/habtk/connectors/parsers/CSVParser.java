package com.aerodynelabs.habtk.connectors.parsers;

public class CSVParser implements Parser {
	
	String startPattern = "";
	String stopPattern = "\n";
	String delimiter = ",";
	
	public CSVParser() {
		
	}

	@Override
	public BalloonPacket parsePacket(String pkt) {
		// TODO parse csv packet
		return null;
	}

	@Override
	public String findPacket() {
		// TODO find csv packet
		return null;
	}

}
