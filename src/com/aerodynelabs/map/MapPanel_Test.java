package com.aerodynelabs.map;

import javax.swing.JFrame;

public class MapPanel_Test {

	public static void main(String args[]) {
		JFrame app = new JFrame("MapPanel Test");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MapPanel map = new MapPanel();
		app.getContentPane().add(map);
		app.pack();
		app.setVisible(true);
	}
	
}
