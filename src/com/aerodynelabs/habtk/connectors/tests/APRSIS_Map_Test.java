package com.aerodynelabs.habtk.connectors.tests;

import javax.swing.JFrame;

import com.aerodynelabs.habtk.connectors.APRSIS;
import com.aerodynelabs.habtk.connectors.parsers.APRSPacket;
import com.aerodynelabs.map.MapOverlay;
import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MapPoint;
import com.aerodynelabs.map.MappingPanel;

public class APRSIS_Map_Test {

	public static void main(String args[]) {
		APRSIS server = new APRSIS("midwest.aprs2.net", 14580, "NV1K", "3327");
		JFrame frame = new JFrame("APRSIS Map Test");
		MappingPanel map = new MappingPanel();
		frame.getContentPane().add(map);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		server.setFilter("r/42.0/-93.6/100");
		
		MapOverlay overlay = new MapOverlay("APRS");
		map.addOverlay(overlay);
		
		while(true) {
			if(server.ready()) {
				String line = server.readLine();
				
				if(line.startsWith("#")) continue;
				System.out.println(">> " + line);
				
				APRSPacket packet = new APRSPacket(line);
				if(!packet.isPosition()) continue;
				if(overlay.hasPath(packet.getFrom())) {
					System.out.println("Updating " + packet.getFrom() + " @ " + packet.getLatitude() + "," + packet.getLongitude());
					overlay.appendPath(packet.getFrom(), new MapPoint(packet.getLatitude(), packet.getLongitude()));
				} else {
					System.out.println("Adding " + packet.getFrom() + " @ " + packet.getLatitude() + "," + packet.getLongitude());
					MapPath path = new MapPath(packet.getFrom());
					path.add(packet.getLatitude(), packet.getLongitude());
					overlay.addPath(packet.getFrom(), path);
				}
				
				map.repaint();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
