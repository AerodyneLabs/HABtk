package com.aerodynelabs.habtk.prediction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class GSDParser implements AtmosphereParser {

	@Override
	public AtmosphereProfile parseAtmosphere(File file) {
		// TODO Parse GSD file
		//ArrayList<String> data = new ArrayList<String>(50);
		AtmosphereProfile profile = new AtmosphereProfile();
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		String line;
		try {
			while((line = reader.readLine()) != null) {
				//data.add(line);
				Scanner scanner = new Scanner(line);
				String token = scanner.next();
				int type;
				try {
					type = Integer.parseInt(token);
				} catch(NumberFormatException e) {
					//e.printStackTrace();
					continue;
				}
				String sPressure, sAlt, sTemp, sDewPt, sDir, sSpeed;
				switch(type) {
					case 1:
						//TODO parse station information
						continue;
					case 2:
						continue;
					case 3:
						//TODO parse wind speed units
						continue;
					case 4:
					case 5:
						//TODO parse line
						try {
							sPressure	= scanner.next();
							sAlt		= scanner.next();
							sTemp		= scanner.next();
							sDewPt		= scanner.next();
							sDir		= scanner.next();
							sSpeed		= scanner.next();
						} catch(Exception e) {
							//e.printStackTrace();
							continue;
						}
						break;
					case 9:
						//TODO parse ground conditions
						continue;
					default:
						continue;
				}
				double p, h, t, dp, dir, spd;
				try {
					p = Double.parseDouble(sPressure);
					h = Double.parseDouble(sAlt);
					t = Double.parseDouble(sTemp);
					dp = Double.parseDouble(sDewPt);
					dir = Double.parseDouble(sDir);
					spd = Double.parseDouble(sSpeed);
				} catch(NumberFormatException e) {
					continue;
				}
				double cDir = correctBearing(dir);
				profile.addData(h, p, t, dp, cDir, spd);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return profile;
	}
	
	private double correctBearing(double dir) {
		//TODO correctBearing
		return dir;
	}

}
