package com.aerodynelabs.habtk.connectors.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.aerodynelabs.habtk.connectors.parsers.APRSPacket;

public class PacketTest {
	
	public static void main(String args[]) {
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
			System.out.println("At: " + packet.getTimestamp());
			System.out.println("From: " + packet.getFrom());
			System.out.println("To: " + packet.getTo());
			System.out.println("Lat: " + packet.getLatitude());
			System.out.println("Lon: " + packet.getLongitude());
			System.out.println("Alt: " + packet.getAltitude());
		}
	}

}
