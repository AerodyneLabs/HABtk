package com.aerodynelabs.habtk.connectors.tests;

import com.aerodynelabs.habtk.connectors.APRSIS;
import com.aerodynelabs.habtk.connectors.parsers.APRSPacket;

public class APRSIS_PacketTest {
	
	public static void main(String args[]) {
		APRSIS server = new APRSIS("midwest.aprs2.net", 14580, "NV1K-1", "3327");
		//server.setFilter("r/42.0/-93.6/100");
		server.setFilter("b/W0ISU-11");
		while(true) {
			if(server.isReady()) {
				String line = server.readLine();
				if(line.startsWith("#")) continue;
				
				System.out.println("Rcvd: " + line);
				APRSPacket pkt = new APRSPacket(line);
				if(pkt.isPosition()) {
					System.out.println("From: " + pkt.getFrom());
					System.out.println("Position: " + pkt.getLatitude() + ", " + pkt.getLongitude() + ", " + pkt.getAltitude() + " m");
				}
				
				System.out.println();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
