package com.aerodynelabs.habtk.connectors.parsers;

import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

public class APRSPacket {
	
	String packet = "";
	String fromCall = "";
	String toCall = "";
	String payload = "";
	char symbolTable;
	char symbolSymbol;
	long timestamp;
	double latitude;
	double longitude;
	int altitude;
	boolean validPos = false;
	
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
	
	public APRSPacket(String from, String to, String payload) {
		packet = from + ">" + to + ":" + payload;
	}
	
	public APRSPacket(String pkt) {
		int s = 0;
		// Get source
		for(int i = s; i < pkt.length(); i++) {
			char c = pkt.charAt(i);
			if(c != '>') {
				fromCall += c;
			} else {
				s = i + 1;
				break;
			}
		}
		// Get destination
		for(int i = s; i < pkt.length(); i++) {
			char c = pkt.charAt(i);
			if(c != ':') {
				toCall += c;
			} else {
				s = i + 1;
				break;
			}
		}
		// Get payload
		payload = pkt.substring(s);
		s = 1;
		// Parse payload
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		switch(payload.charAt(0)) {
			case ':':	// Message
				// TODO message
				break;
			case '>':	// Status
				// TODO status
				break;
			case 'T':	// Telemetry data
				// TODO telemetry
				break;
			case 0x1c:	// Current mic-e
			case '`':	// Current mic-e
				// TODO current mic-e
				break;
			case '\'':	// Old mic-e or new TM-D700
			case 0x1d:	// Old mic-e
				// TODO old mic-e
				break;
			case '/':	// Position, w/ timestamp, w/o messaging
			case '@':	// Position, w/ timestamp, w/ messaging
				String time = "";
				int stop = s;
				for(int i = s; i < stop + 8; i++) {
					s++;
					time += payload.charAt(i);
					if(!Character.isDigit(payload.charAt(i))) break;
				}
				char timeType = time.charAt(time.length() - 1);
				switch(timeType) {
					case '/':	// DDHHmm local
						cal.setTimeZone(TimeZone.getDefault());
						// resume parsing time as below
					case 'z':	// DDHHmm zulu
						cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time.substring(0, 2)));
						cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(2, 4)));
						cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(4, 6)));
//						cal.set(Calendar.SECOND, 0);
//						cal.set(Calendar.MILLISECOND, 0);
						break;
					case 'h':	// HHmmss zulu
						cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(0, 2)));
						cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(2, 4)));
						cal.set(Calendar.SECOND, Integer.parseInt(time.substring(4, 6)));
//						cal.set(Calendar.MILLISECOND, 0);
						break;
					default:	// MMDDHHmm zulu
						cal.set(Calendar.MONTH, Integer.parseInt(time.substring(0, 2)));
						cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time.substring(2, 4)));
						cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(4, 6)));
						cal.set(Calendar.MINUTE, Integer.parseInt(time.substring(6)));
//						cal.set(Calendar.SECOND, 0);
//						cal.set(Calendar.MILLISECOND, 0);
						break;
				}
				// Resume parsing position below
			case '!':	// Position, w/o timestamp, w/o messaging
			case '=':	// Position, w/o timestamp, w/ messaging
				// Timestamp
				timestamp = cal.getTimeInMillis();

				// Read position info
				Scanner scanner = new Scanner(payload.substring(s));
				String rLat = scanner.findInLine("[0-9.]+[NS]");
				symbolTable = scanner.findInLine(".").charAt(0);
				String rLon = scanner.findInLine("[0-9.]+[EW]");
				symbolSymbol = scanner.findInLine(".").charAt(0);
				// Parse lat
				latitude = Integer.parseInt(rLat.substring(0, 2));
				latitude += Double.parseDouble(rLat.substring(2, rLat.length() - 2)) / 60.0;
				if(rLat.charAt(rLat.length() - 1) == 'S') latitude = -latitude;
				// Parse lon
				longitude = Integer.parseInt(rLon.substring(0, 3));
				longitude += Double.parseDouble(rLon.substring(3, rLon.length() - 2)) / 60.0;
				if(rLon.charAt(rLon.length() - 1) == 'W') longitude = -longitude;
				
				// Save remaining payload to reinit scanners
				// Remaining fields can occur anywhere
				String work = scanner.nextLine();
				
				// Parse altitude if possible
				scanner = new Scanner(work);
				String rAlt = scanner.findInLine("/A=[0-9]{6}");
				if(scanner != null) {
					altitude = Integer.parseInt(rAlt.substring(3));
				}
				
				// Parse extensions
				// TODO parse aprs extensions
				
				break;
		}
	}
	
	private String toBase91() {
		// TODO toBase91
		return null;
	}
	
	private double convertBase91() {
		// TODO convertBase91
		return 0.0;
	}
	
	public String getPayload() {
		return payload;
	}
	
	public boolean isPosition() {
		return validPos;
	}
	
	public String getFrom() {
		return fromCall;
	}
	
	public String getTo() {
		return toCall;
	}
	
	public long getTimestamp() {
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
		String out = 
				"Source: " + fromCall + "\n" +
				"Dest: " + toCall + "\n" +
				"Payload: " + payload;
		return out;
	}

}
