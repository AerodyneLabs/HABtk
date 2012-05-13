package com.aerodynelabs.habtk.prediction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MapPoint;

// http://developer.mapquest.com/web/products/open/elevation-service
public class ElevationService {
	
	private static final String baseUrl = "http://open.mapquestapi.com/elevation/v1/getElevationProfile?";
	
	public static MapPath getElevationProfile(MapPath in) {
		MapPath out = null;
		
		LinkedList<MapPoint> sub = getSubProfile(in.getPath());
		out = new MapPath("Elevation");
		out.addAll(sub);
		
		return out;
	}
	
	public static double getElevation(double lat, double lon) {
		Double elev = Double.NaN;
		String query = "shapeFormat=raw&outShapeFormat=none&latLngCollection=" + lat + "," + lon;
		URL url;
		BufferedReader reader;
		String line;
		String result = "";
		try {
			url = new URL(baseUrl + query);
			InputStream stream = url.openStream();
			reader = new BufferedReader(new InputStreamReader(stream));
			while((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
			Scanner scanner = new Scanner(result);
			String field = scanner.findInLine("\"height\":[0-9.]+");
			scanner = new Scanner(field);
			String alt = scanner.findInLine("[0-9.]+");
			elev = Double.parseDouble(alt);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return elev;
	}
	
	public static String encodePath(List<MapPoint> in) {
		String encoded = "";
		int pLat = 0, pLon = 0;
		Iterator<MapPoint> itr = in.iterator();
		while(itr.hasNext()) {
			MapPoint p = itr.next();
			int lat = floor5(p.getLatitude());
			int lon = floor5(p.getLongitude());
			encoded += encodeSignedNumber(lat - pLat);
			encoded += encodeSignedNumber(lon - pLon);
			pLat = lat;
			pLon = lon;
		}
		return encoded;
	}
	
	private static LinkedList<MapPoint> getSubProfile(List<MapPoint> in) {
		LinkedList<MapPoint> out = null;
		
		String query = "shapeFormat=raw&outShapeFormat=none&latLngCollection=";
//		String query = "shapeFormat=cmp&outShapeFormat=none&latLngCollection=";
		Iterator<MapPoint> itr = in.iterator();
		while(itr.hasNext()) {
			MapPoint p = itr.next();
			query += p.getLatitude() + ",";
			query += p.getLongitude();
			if(itr.hasNext()) query += ",";
		}
//		query += encodePath(in);
		System.out.println(baseUrl + query);
		URL url;
		BufferedReader reader;
		String line;
		String result = "";
		try {
			url = new URL(baseUrl + query);
			InputStream stream = url.openStream();
			reader = new BufferedReader(new InputStreamReader(stream));
			while((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			out = new LinkedList<MapPoint>();
			itr = in.iterator();
			Scanner scanner = new Scanner(result);
			while(itr.hasNext()) {
				MapPoint p = itr.next();
				double elev;
				String field = scanner.findInLine("\"height\":[0-9.]+");
				Scanner s = new Scanner(field);
				String alt = s.findInLine("[0-9.]+");
				elev = Double.parseDouble(alt);
				out.add(new MapPoint(p.getLatitude(), p.getLongitude(), elev, p.getTime()));
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return out;
	}
	
	private static String encodeNumber(int x) {
		StringBuffer encoded = new StringBuffer();
		while(x > 0x20) {
			encoded.append( (char)((0x20 | (x & 0x1f)) + 63) );
			x >>= 5;
		}
		encoded.append( (char)(x + 63) );
		return encoded.toString();
	}
	
	private static String encodeSignedNumber(int x) {
		int y = x << 1;
		if(x < 0) y = ~y;
		return encodeNumber(y);
	}
	
	private static int floor5(double x) {
		return (int) Math.round(x * 1e5);
	}

}
