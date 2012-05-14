package com.aerodynelabs.habtk.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class HTMLFormatter extends Formatter {
	
	SimpleDateFormat sdf;
	
	public HTMLFormatter() {
		super();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	@Override
	public String format(LogRecord r) {
		StringBuffer buf = new StringBuffer(1000);
		if(r.getLevel().intValue() >= Level.WARNING.intValue()) {
			buf.append("<b>");
			buf.append(r.getLevel());
			buf.append("</b>");
		} else {
			buf.append(r.getLevel());
		}
		buf.append(" ");
		buf.append(sdf.format(new Date(r.getMillis())));
		buf.append(": ");
		buf.append(formatMessage(r));
		buf.append("\n");
		return buf.toString();
	}
	
	@Override
	public String getHead(Handler h) {
		return "<HTML><HEAD>\n" +
				sdf.format(new Date()) +
				"</HEAD><BODY><PRE>";
	}
	
	@Override
	public String getTail(Handler h) {
		return "</PRE></BODY></HTML>";
	}

}
