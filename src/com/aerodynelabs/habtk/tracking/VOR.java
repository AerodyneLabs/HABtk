package com.aerodynelabs.habtk.tracking;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class VOR {
	
	ArrayList<Station> database = new ArrayList<Station>();
	int prevIndex;
	
	class Station {
		String name;
		double lat;
		double lon;
		
		public Station(String name, double lat, double lon) {
			this.name = name;
			this.lat = lat;
			this.lon = lon;
		}
	}
	
	public void initDatabase(String filename) {
		File file = new File(filename);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null) {
				Scanner scanner = new Scanner(line);
				String name = scanner.next();
				double lat = scanner.nextDouble();
				double lon = scanner.nextDouble();
				database.add(new Station(name, lat, lon));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public Point2D.Double getNearest(double lat, double lon) {
		Point2D.Double near = new Point2D.Double(-1000, -1000);
		
		//XXX Improve nearest VOR calculation
		double best = Double.MAX_VALUE;
		int bestindex = -1;
		int index = -1;
		Iterator<Station> itr = database.iterator();
		while(itr.hasNext()) {
			Station x = itr.next();
			index++;
			double dist = Math.sqrt(Math.pow(lat - x.lat, 2) + Math.pow(lon - x.lon, 2));
			if(dist < best) {
				bestindex = index;
			}
		}
		prevIndex = bestindex;
		
		return near;
	}
	
	public String getName() {
		return database.get(prevIndex).name;
	}

}
