package com.aerodynelabs.habtk;

import java.util.ListIterator;
import java.util.Vector;

import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapPath;

public class BalloonFlight {
	
	private Predictor predictor;
	private MapPath latestPred;
	private MapPath track;
	private Vector<MapPath> predictions;
	
	public BalloonFlight() {
	}
	
	public BalloonFlight(Predictor predictor) {
		this.predictor = predictor;
	}
	
	public void setPredictor(Predictor pred) {
		predictor = pred;
	}
	
	public Predictor getPredictor() {
		return predictor;
	}
	
	public void setTrack(MapPath t) {
		track = t;
	}
	
	public MapPath getTrack() {
		return track;
	}
	
	public void updatePrediction(MapPath prediction) {
		latestPred = prediction;
	}
	
	public MapPath getLatestPrediction() {
		return latestPred;
	}
	
	public void storePrediction(MapPath prediction) {
		predictions.add(prediction);
	}
	
	public void savePrediction() {
		predictions.add(latestPred);
	}
	
	public int getPredictionCount() {
		return predictions.size();
	}
	
	public ListIterator<MapPath> getPredictions() {
		return predictions.listIterator();
	}

}
