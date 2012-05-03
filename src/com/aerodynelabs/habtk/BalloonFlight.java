package com.aerodynelabs.habtk;

import java.util.ListIterator;
import java.util.Vector;

import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapPath;

/**
 * A class to relate predictors to predictions and tracks
 * @author eharstad
 *
 */
public class BalloonFlight {
	
	private Predictor predictor;
	private MapPath latestPred;
	private MapPath track;
	private Vector<MapPath> predictions;
	
	public BalloonFlight() {
	}
	
	/**
	 * Create with base predictor
	 * @param predictor
	 */
	public BalloonFlight(Predictor predictor) {
		this.predictor = predictor;
	}
	
	/**
	 * Set the predictor associated with this flight
	 * @param pred
	 */
	public void setPredictor(Predictor pred) {
		predictor = pred;
	}
	
	/**
	 * Get the predictor associated with this flight
	 * @return
	 */
	public Predictor getPredictor() {
		return predictor;
	}
	
	/**
	 * Set the track associated with this flight
	 * @param t
	 */
	public void setTrack(MapPath t) {
		track = t;
	}
	
	/**
	 * Get the track associated with this flight
	 * @return
	 */
	public MapPath getTrack() {
		return track;
	}
	
	/**
	 * Replace the latest prediction with the given one
	 * @param prediction
	 */
	public void updatePrediction(MapPath prediction) {
		latestPred = prediction;
	}
	
	/**
	 * Get the latest prediction for this flight
	 * @return
	 */
	public MapPath getLatestPrediction() {
		return latestPred;
	}
	
	/**
	 * Store the given prediction in the flight
	 * @param prediction
	 */
	public void storePrediction(MapPath prediction) {
		predictions.add(prediction);
	}
	
	/**
	 * Save the latest prediction in the flight
	 */
	public void savePrediction() {
		predictions.add(latestPred);
	}
	
	/**
	 * Get the number of predictions stored in this flight
	 * @return
	 */
	public int getPredictionCount() {
		return predictions.size();
	}
	
	/**
	 * Get an iterator over the stored predictions
	 * @return
	 */
	public ListIterator<MapPath> getPredictions() {
		return predictions.listIterator();
	}

}
