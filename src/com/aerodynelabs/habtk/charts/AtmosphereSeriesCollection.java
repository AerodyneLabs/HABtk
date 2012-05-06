package com.aerodynelabs.habtk.charts;

import org.jfree.data.xy.AbstractXYDataset;

import com.aerodynelabs.habtk.atmosphere.AtmosphereProfile;
import com.aerodynelabs.habtk.atmosphere.AtmosphereState;

@SuppressWarnings("serial")
public class AtmosphereSeriesCollection extends AbstractXYDataset {
	
	public static final int DOMAIN_ALTITUDE = 0;
	public static final int DOMAIN_PRESSURE = 1;
	
	public static final int RANGE_TEMP = 0;
	public static final int RANGE_DEWPT = 1;
	public static final int RANGE_TEMPDEWPT = 2;
	public static final int RANGE_WINDSPD = 3;
	public static final int RANGE_WINDDIR = 4;
	public static final int RANGE_WIND = 5;
	public static final int RANGE_PRESSURE = 6;
	
	private int domain, range;
	private AtmosphereProfile atmo;
	
	public AtmosphereSeriesCollection(int domain, int range) {
		this.domain = domain;
		this.range = range;
		atmo = null;
	}
	
	public void setProfile(AtmosphereProfile profile) {
		atmo = profile;
		super.fireDatasetChanged();
	}
	
	public AtmosphereProfile getProfile() {
		return atmo;
	}

	@Override
	public int getItemCount(int series) {
		if(atmo == null) {
			return 0;
		} else {
			return atmo.size();
		}
	}

	@Override
	public Number getX(int series, int item) {
		if(atmo == null) return null;
		AtmosphereState x = atmo.get(item);
		if(domain == DOMAIN_ALTITUDE) return x.getAltitude();
		if(domain == DOMAIN_PRESSURE) return x.getPressure() / 100;
		return null;
	}

	@Override
	public Number getY(int series, int item) {
		if(atmo == null) return null;
		AtmosphereState x = atmo.get(item);
		switch(range) {
		case RANGE_TEMP:
			if(series == 0) return x.getTemperature();
		case RANGE_DEWPT:
			if(series == 0) return x.getDewPoint();
		case RANGE_WINDSPD:
			if(series == 0) return x.getWindSpeed();
		case RANGE_WINDDIR:
			if(series == 0) return x.getWindDirection();
		case RANGE_PRESSURE:
			if(series == 0) return x.getPressure() / 100;
		case RANGE_TEMPDEWPT:
			if(series == 0) return x.getTemperature();
			if(series == 1) return x.getDewPoint();
		case RANGE_WIND:
			if(series == 0) return x.getWindSpeed();
			if(series == 1) return x.getWindDirection();
		}
		return null;
	}

	@Override
	public int getSeriesCount() {
		if(atmo == null) return 0;
		switch(range) {
		case RANGE_TEMP:
		case RANGE_DEWPT:
		case RANGE_WINDSPD:
		case RANGE_WINDDIR:
		case RANGE_PRESSURE:
			return 1;
		case RANGE_TEMPDEWPT:
		case RANGE_WIND:
			return 2;
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getSeriesKey(int series) {
		switch(range) {
		case RANGE_TEMP:
			if(series == 0) return "Temperature";
		case RANGE_DEWPT:
			if(series == 0) return "Dew Point";
		case RANGE_WINDSPD:
			if(series == 0) return "Wind Speed";
		case RANGE_WINDDIR:
			if(series == 0) return "Wind Direction";
		case RANGE_PRESSURE:
			if(series == 0) return "Pressure";
		case RANGE_TEMPDEWPT:
			if(series == 0) return "Temperature";
			if(series == 1) return "Dew Point";
		case RANGE_WIND:
			if(series == 0) return "Wind Speed";
			if(series == 1) return "Wind Direction";
		}
		return null;
	}

}
