package com.aerodynelabs.habtk.connectors.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.aerodynelabs.habtk.connectors.parsers.APRSPacket;

public class PacketTest {
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String args[]) {
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(reader);
		String input = null;
		while(true) {
			try {
				input = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(input == "exit") return;
			APRSPacket packet = new APRSPacket(input);
			if(packet.isPosition()) {
				System.out.println("Payload: " + packet.getPayload());
				System.out.println("At: " + sdf.format(packet.getTimestamp()));
				System.out.println("From: " + packet.getFrom());
				System.out.println("To: " + packet.getTo());
				System.out.println("Lat: " + packet.getLatitude());
				System.out.println("Lon: " + packet.getLongitude());
				System.out.println("Alt: " + packet.getAltitude());
				System.out.println("Comment: " + packet.getComment());
			}
		}
	}

}
