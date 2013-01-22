package com.aerodynelabs.habtk.prediction.tests;

import java.awt.geom.Point2D;
import java.util.Scanner;

public class GeodesicTest {
	
	private static Point2D.Double directGeodesic(Point2D.Double start, double bearing, double range) {
		double radDist = range / 6367500;
		double lat1 = Math.toRadians(start.y);
		double lon1 = Math.toRadians(start.x);
		double lat2 = Math.asin( Math.sin(lat1)*Math.cos(radDist) + Math.cos(lat1)*Math.sin(radDist)*Math.cos(bearing) );
		double lon2 = lon1 + Math.atan2(Math.sin(bearing)*Math.sin(radDist)*Math.cos(lat1), Math.cos(radDist)-Math.sin(lat1)*Math.sin(lat2));
		return new Point2D.Double(Math.toDegrees(lon2), Math.toDegrees(lat2));
	}
	
	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		while(true) {
			String line = in.nextLine();
			Scanner scanner = new Scanner(line);
			double dX = scanner.nextDouble();
			double dY = scanner.nextDouble();
			
			double range = Math.pow(Math.pow(dX, 2.0) + Math.pow(dY, 2.0), 0.5);
//			double bearing = Math.atan(dX / dY);
			double bearing = Math.atan2(dX, dY);
			bearing = bearing < 0 ? bearing + 2.0 * Math.PI : bearing;
			
			System.out.println(Math.toDegrees(bearing) + " for " + range / 1000 + " km");
			
			Point2D.Double cPos = directGeodesic(new Point2D.Double(-93.635, 42.0), bearing, range);
			
			System.out.println(cPos.y + ", " + cPos.x);
		}
	}

}
