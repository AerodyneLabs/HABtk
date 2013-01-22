package com.aerodynelabs.habtk.logging;

import java.awt.Color;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Level;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.aerodynelabs.habtk.ui.LogPanel;

public class LogPanelHandler extends Handler {
	
	private int source;
	private LogPanel log;
	
	private static SimpleAttributeSet warning = new SimpleAttributeSet();
	private static SimpleAttributeSet error = new SimpleAttributeSet();
	private static SimpleAttributeSet normal = new SimpleAttributeSet();
	
	public LogPanelHandler(LogPanel log, int source) {
		super();
		this.log = log;
		this.source = source;
		
		StyleConstants.setForeground(error, Color.RED);
		StyleConstants.setForeground(warning, Color.ORANGE);
	}

	@Override
	public void close() throws SecurityException {
		flush();
	}

	@Override
	public void flush() {
		// not needed
	}

	@Override
	public void publish(LogRecord r) {
		String record = getFormatter().format(r);
		SimpleAttributeSet style;
		if(r.getLevel().intValue() == Level.SEVERE.intValue()) {
			style = error;
		} else if(r.getLevel().intValue() == Level.WARNING.intValue()) {
			style = warning;
		} else {
			style = normal;
		}
		log.publishRecord(source, record, style);
	}

}
