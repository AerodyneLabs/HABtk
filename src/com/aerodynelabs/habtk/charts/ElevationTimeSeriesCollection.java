package com.aerodynelabs.habtk.charts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.data.Range;
import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYDomainInfo;

import com.aerodynelabs.map.MapPath;

@SuppressWarnings("serial")
public class ElevationTimeSeriesCollection extends AbstractXYDataset implements XYDataset, XYDomainInfo {
	// XXX implement XYRangeInfo
	
	private ArrayList<MapPath> data = new ArrayList<MapPath>();

	@Override
	public int getItemCount(int series) {
		return data.get(series).getPath().size();
	}

	@Override
	public Number getX(int series, int item) {
		Long time = new Long(data.get(series).getPath().get(item).getTime());
		time *= 1000l;
		return time.doubleValue();
	}

	@Override
	public Number getY(int series, int item) {
		return data.get(series).getPath().get(item).getAltitude();
	}

	@Override
	public int getSeriesCount() {
		return data.size();
	}

	@Override
	public Comparable<String> getSeriesKey(int series) {
		// XXX Improve series keys for unnamed paths
		String name = data.get(series).getName();
		if(name == null) name = Integer.toString(data.get(series).hashCode());
		return name;
	}
	
	public void addSeries(MapPath path) {
		data.add(path);
		super.fireDatasetChanged();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Range getDomainBounds(List visibleSeriesKeys, boolean includeInterval) {
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < visibleSeriesKeys.size(); j++) {
				Object o = visibleSeriesKeys.get(j);
				String key = o.toString();
				if(getSeriesKey(i).equals(key)) {
					MapPath path = data.get(i);
					min = Math.min(min, path.getStartTime()*1000);
					max = Math.max(max, path.getEndTime()*1000);
					break;
				}
			}
		}
		if(min == Long.MAX_VALUE) return new Range(new Date().getTime(), new Date().getTime());
		System.out.println(min + " - " + max);
		return new Range(min, max);
	}
	
}
