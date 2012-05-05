package com.aerodynelabs.habtk.atmosphere;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A class to retrieve GFS soundings from http://rucsoundings.noaa.gov
 * 
 * @author Ethan Harstad
 *
 */
public class RUCGFS implements AtmosphereSource {

	@Override
	public File getAtmosphere(int time, double lat, double lon) {
		// Adjust fields to match model
		int startTime = (time / 10800) * 10800;
		double rlat = (int)(lat / 0.5) * 0.5;
		double rlon = (int)(lon / 0.5) * 0.5;
		
		// Get the time the model was last run
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		int modelRun = (cal.get(Calendar.HOUR_OF_DAY) / 12) * 12;
		cal.set(Calendar.HOUR_OF_DAY, modelRun);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		int modelTime = (int)(cal.getTimeInMillis() / 1000);
		
		// Check for local copy
		File local = null;
		File root = new File("wind/");
		if(root.exists()) {
			File files[] = root.listFiles(new Filter(startTime, rlat, rlon));
			if(files.length > 0) local = files[files.length - 1];
		} else {
			root.mkdir();
		}
		
		// Get a new model if needed
		boolean old = true;
		if(local != null) {
			String name = local.getName();
			int s = name.lastIndexOf('_');
			int e = name.lastIndexOf('.');
			int t = Integer.parseInt(name.substring(s + 1, e));
			old = modelTime > t;
		}
		File net = null;
		if(old) net = download(startTime, modelTime, rlat, rlon);
		
		// Return the best model
		if(net == null) {
			return local;
		} else {
			return net;
		}
	}
	
	private File download(int time, int modelTime, double lat, double lon) {
		String address = "http://rucsoundings.noaa.gov/get_soundings.cgi?data_source=GFS;airport=" +
				lat + "," + lon + ";hydrometeors=false&startSecs=" + time +
				"&endSecs=" + (time+1);
		
		URL url;
		InputStream is = null;
		InputStreamReader isr = null;
		try {
			url = new URL(address);
			is = url.openStream();
		} catch(MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		File file = new File("wind/gfs_" + (int)(lat*10) + "_" + (int)(lon*10) + "_" + time + "_" + modelTime + ".gsd");
		
		FileWriter fw;
		try {
			fw = new FileWriter(file);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		PrintWriter writer = new PrintWriter(new BufferedWriter(fw));
		
		String line = null;
		try {
			while((line = br.readLine()) != null) {
				writer.println(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		writer.flush();
		return file;
	}
	
	class Filter implements FilenameFilter {
		
		private String filter;
		
		public Filter(int time, double lat, double lon) {
			filter = "gfs_" + (int)(lat*10) + "_" + (int)(lon*10) + "_" + time + "_";
		}
		
		@Override
		public boolean accept(File dir, String name) {
			if(!name.startsWith(filter)) return false;
			if(!name.endsWith(".gsd")) return false;
			return true;
		}
		
	}

}
