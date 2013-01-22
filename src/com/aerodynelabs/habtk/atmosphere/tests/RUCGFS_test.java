package com.aerodynelabs.habtk.atmosphere.tests;

import java.io.File;
import java.util.Scanner;

import com.aerodynelabs.habtk.atmosphere.*;

public class RUCGFS_test {
	
	public static void main(String args[]) {
		int time = (int)(System.currentTimeMillis() / 1000);
		System.out.println(time);
		
		AtmosphereSource source = new RUCGFS();
		File file = source.getAtmosphere(time, 42.0, -93.5);
		
		GSDParser parser = new GSDParser();
		AtmosphereProfile profile = parser.parseAtmosphere(file);
		System.out.println(profile);
		
		while(true) {
			Scanner input = new Scanner(System.in);
			double alt = Double.parseDouble(input.nextLine());
			System.out.println(profile.getAtAltitude(alt));
		}
	}

}
