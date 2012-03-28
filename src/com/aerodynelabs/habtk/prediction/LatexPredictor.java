package com.aerodynelabs.habtk.prediction;

import java.util.LinkedList;

import com.aerodynelabs.map.MapPoint;

public class LatexPredictor extends Predictor {
	
	private double startLat, startLon, startAlt;
	private long startTime;
	private int tStep = 10;
	
	public LatexPredictor(double lat, double lon, double alt, long time) {
		startLat = lat;
		startLon = lon;
		startAlt = alt;
		startTime = time;
	}
	
	public LinkedList<MapPoint> runPrediction() {
		LinkedList<MapPoint> path = new LinkedList<MapPoint>();
		path.add(new MapPoint(startLat, startLon, startAlt, startTime));
		
		double lat = startLat;
		double lon = startLon;
		double alt = startAlt;
		long time = startTime;
		
		AtmosphereProfile atmo = AtmosphereFactory.GetGFSProfile(startLat, startLon, time);
		//TODO Latex Prediction
		
		return path;
	}

}
