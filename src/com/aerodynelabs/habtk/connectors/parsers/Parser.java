package com.aerodynelabs.habtk.connectors.parsers;

public interface Parser {
	
	public String findPacket();
	public BalloonPacket parsePacket(String pkt);

}
