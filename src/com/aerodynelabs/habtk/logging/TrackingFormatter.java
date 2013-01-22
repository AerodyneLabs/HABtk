package com.aerodynelabs.habtk.logging;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class TrackingFormatter extends Formatter {

	@Override
	public String format(LogRecord r) {
		if(r.getLevel().intValue() == Level.INFO.intValue()) {
			return r.getMessage();
		} else {
			return r.getLevel().toString() + ": " + r.getMessage();
		}
	}

}
