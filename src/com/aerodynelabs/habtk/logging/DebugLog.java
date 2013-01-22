package com.aerodynelabs.habtk.logging;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DebugLog {

	private static final Logger debugLog = Logger.getLogger("Debug");
	private static FileHandler logFile;
	private static SimpleFormatter logFormatter;
	
	/**
	 * Create a global logger
	 * @param level Severity threshold
	 */
	public static void setupLogger(int level) {
		switch(level) {
			case 0:
				debugLog.setLevel(Level.ALL);
				break;
			case 1:
				debugLog.setLevel(Level.SEVERE);
				break;
			case 2:
				debugLog.setLevel(Level.WARNING);
				break;
			case 3:
				debugLog.setLevel(Level.INFO);
				break;
			default:
				debugLog.setLevel(Level.OFF);
		}
		
		try {
			logFile = new FileHandler("log.txt");
		} catch(Exception e) {
			debugLog.log(Level.SEVERE, "Exception", e);
		}
		logFormatter = new SimpleFormatter();
		logFile.setFormatter(logFormatter);
		debugLog.addHandler(logFile);
	}
	
}
