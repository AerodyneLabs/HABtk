package com.aerodynelabs.map.tests;

import javax.swing.JFrame;

import com.aerodynelabs.map.MapOverlay;
import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MappingPanel;

public class MapPanel_Test {

	public static void main(String args[]) {
		JFrame app = new JFrame("MapPanel Test");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//MapPanel map = new MapPanel(42.01, -93.57, 11, "http://otile1.mqcdn.com/tiles/1.0.0/osm/", 18);
		MappingPanel map = new MappingPanel();
		app.getContentPane().add(map);
		app.pack();
		
		MapOverlay overlay = new MapOverlay("Test Overlay");
		map.addOverlay(overlay);
		
		MapPath path = new MapPath("Test");
		path.add(42.01, -93.57);
		path.add(42.02, -93.61);
		path.add(42.03, -93.62);
		path.add(42.08, -93.58);
		overlay.addPath("Test Path", path);
		
		app.setVisible(true);
		//System.out.println("North: " + map.getNorthBound());
		//System.out.println("South: " + map.getSouthBound());
		//System.out.println("East: " + map.getEastBound());
		//System.out.println("West: " + map.getWestBound());
		//System.out.println(MapPanel.lon2pos(-93.57, map.getZoom()));
		//System.out.println(map.getLatPos(42.01));
	}
	
}
