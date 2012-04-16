package com.aerodynelabs.habtk.connectors.parsers;

public class CSVParser implements Parser {
	
	String startPattern = "";
	String stopPattern = "\n";
	String delimiter = ",";
	
	public CSVParser() {
		
	}

	@Override
	public BalloonPacket parsePacket(String pkt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String findPacket() {
		// TODO Auto-generated method stub
		return null;
	}

}
