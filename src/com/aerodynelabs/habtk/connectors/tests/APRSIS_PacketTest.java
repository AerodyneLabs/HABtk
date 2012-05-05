package com.aerodynelabs.habtk.connectors.tests;

import com.aerodynelabs.habtk.connectors.APRSIS;

public class APRSIS_PacketTest {
	
	public static void main(String args[]) {
		APRSIS server = new APRSIS("midwest.aprs2.net", 14580, "NV1K", "3327");
		server.setFilter("r/42.0/-93.6/100");
		while(true) {
			if(server.ready()) {
				String line = server.readLine();
				if(line.startsWith("#")) continue;
				
				System.out.println(line);
				System.out.println("Length: " + line.length());
				
				
				
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
