package com.aerodynelabs.habtk.atmosphere;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * A class to parse NOAA GSD formatted soundings.
 * 
 * @author Ethan Harstad
 *
 */
public class GSDParser implements AtmosphereParser {

	/**
	 * Parse the given file and return a profile.
	 */
	@Override
	public AtmosphereProfile parseAtmosphere(File file) {
		String line;
		Double conversion = 0.514444444;
		int hour, day, year;
		String month;
		double lat, lon;
		AtmosphereProfile profile = new AtmosphereProfile();
		
		try(
			FileReader fr = new FileReader(file);
			BufferedReader reader = new BufferedReader(fr);
		) {
			while((line = reader.readLine()) != null) {
				System.out.println(line);
				if(line.equals("")) break;
				Scanner s = new Scanner(line);
				String token = s.next();
				int type;
				try {
					type = Integer.parseInt(token);
				} catch(NumberFormatException e) {
					// Date and time
					int one, two;
					try {
						one = s.nextInt();
						two = s.nextInt();
						hour = one;
						day = two;
						month = s.next();
						year = s.nextInt();
					} catch(Exception e1) {
						continue;
					}
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MMM HH z");
					try {
						Date date = sdf.parse(year + "-" + day + "-" + month + " " + hour + " GMT");
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
						cal.setTime(date);						
						profile.startTime = (int)(cal.getTimeInMillis() / 1000);
					} catch (ParseException e1) {
						e1.printStackTrace();
						break;
					}
					continue;
				}
				switch(type) {
				case 1:
					// Lat and Lon
					try {
						s.next();
						s.next();
						lat = s.nextDouble();
						lon = s.nextDouble();
					} catch(Exception e) {
						break;
					}
					profile.lat = lat;
					profile.lon = lon;
					break;
				case 3:
					// Wind speed units
					s.next();
					s.next();
					String unit = s.next();
					if(unit.equals("kt")) {
						conversion = 0.514444444;
					} else if (unit.equals("m/s")) {
						conversion = 1.0;
					} else {
						System.err.println("Unsupported wind speed unit!");
						break;
					}
					break;
				case 4:
				case 5:
				case 9:
					// Atmosphere data line
					try {
						int p = s.nextInt();
						int h = s.nextInt();
						int t = s.nextInt();
						int dp = s.nextInt();
						int dir = s.nextInt();
						int spd = s.nextInt();
						if(
							p == 99999 ||
							h == 99999 ||
							t == 99999 ||
							dp == 99999 ||
							dir == 99999 ||
							spd == 99999
							) break;
						profile.addData(h, p * 10.0, t / 10.0, dp / 10.0, dir, spd * conversion);
					} catch(Exception e) {
						break;
					}
					break;
				default:
					break;
				}
				s.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return profile;
	}
	
	// Not needed except for RUC which is unimplemented (so far)
	/*
	private static double correctBearing(double dir) {
		return dir;
	}
	*/

}
