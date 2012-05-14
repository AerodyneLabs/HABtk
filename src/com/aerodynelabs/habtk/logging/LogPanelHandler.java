package com.aerodynelabs.habtk.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.aerodynelabs.habtk.ui.LogPanel;

public class LogPanelHandler extends Handler {
	
	private int source;
	private LogPanel log;
	
	public LogPanelHandler(LogPanel log, int source) {
		super();
		this.log = log;
		this.source = source;
	}

	@Override
	public void close() throws SecurityException {
		log.appendRecord(source, getFormatter().getTail(this));
	}

	@Override
	public void flush() {
		// Not needed		
	}

	@Override
	public void publish(LogRecord r) {
		log.appendRecord(source, getFormatter().format(r));
	}

}
