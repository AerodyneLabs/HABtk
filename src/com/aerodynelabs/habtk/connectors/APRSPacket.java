//TODO Mic-E
package com.aerodynelabs.habtk.connectors;

import java.util.Scanner;

public class APRSPacket {
	
	String packet;
	String fromCall;
	String toCall;
	String timestamp;
	double latitude;
	double longitude;
	int altitude;
	boolean validPos = true;
	
	public static final int SSID_DOT = 0;
	public static final int SSID_AMBULENCE = 1;
	public static final int SSID_BUS = 2;
	public static final int SSID_FIRETRUCK = 3;
	public static final int SSID_BIKE = 4;
	public static final int SSID_YACHT = 5;
	public static final int SSID_HELO = 6;
	public static final int SSID_AIRCRAFT = 7;
	public static final int SSID_SHIP = 8;
	public static final int SSID_CAR = 9;
	public static final int SSID_MOTORCYCLE = 10;
	public static final int SSID_BALLOON = 11;
	public static final int SSID_JEEP = 12;
	public static final int SSID_RV = 13;
	public static final int SSID_TRUCK = 14;
	public static final int SSID_VAN = 15;
	
	public APRSPacket(String from, String to) {
		//TODO parse fields to packet
		packet = from + ">" + to;
	}
	
	public APRSPacket(String pkt) {
		packet = pkt;
		Scanner scanner = new Scanner(packet);
		fromCall = scanner.findInLine("[A-Za-z][A-Za-z0-9-]*");
		toCall = scanner.findInLine("[A-Za-z][^:>/]*");
		timestamp = scanner.findWithinHorizon("[0-9]{6}[z/h]", 0);
		String tmpLat = scanner.findWithinHorizon("[0-9]{4}.[0-9]{2}[NS]", 0);
		String tmpLon = scanner.findWithinHorizon("[0-9]{5}.[0-9]{2}[EW]", 0);
		String tmpAlt = scanner.findWithinHorizon("A=[0-9]*", 0);
		
		if(tmpLat != null) {
			int latDeg = Integer.parseInt(tmpLat.substring(0, 2));
			double latMin = Double.parseDouble(tmpLat.substring(2,7));
			latitude = latDeg + latMin / 60.0;
			if(tmpLat.endsWith("S")) latitude = -latitude;
		} else {
			validPos = false;
		}
		
		if(tmpLon != null) {
			int lonDeg = Integer.parseInt(tmpLon.substring(0, 3));
			double lonMin = Double.parseDouble(tmpLon.substring(3, 8));
			longitude = lonDeg + lonMin / 60.0;
			if(tmpLon.endsWith("W")) longitude = -longitude;
		} else {
			validPos = false;
		}
		
		if(tmpAlt != null) {
			altitude = Integer.parseInt(tmpAlt.substring(2));
		} else {
			altitude = 0;
		}
	}
	
	public boolean isPosition() {
		if(fromCall == null) return false;
		if(validPos == false) return false;
		//TODO test position packet validity
		return true;
	}
	
	public String getFrom() {
		return fromCall;
	}
	
	public String getTo() {
		return toCall;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public int getAltitude() {
		return altitude;
	}
	
	public String getPacket() {
		return packet;
	}
	
	public String toString() {
		return packet;
	}

}
