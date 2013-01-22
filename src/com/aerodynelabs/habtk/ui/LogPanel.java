package com.aerodynelabs.habtk.ui;

import java.awt.BorderLayout;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

import com.aerodynelabs.habtk.logging.LogPanelHandler;
import com.aerodynelabs.habtk.logging.TrackingFormatter;

/**
 * A panel to display a log window.
 * @author Ethan Harstad
 *
 */
@SuppressWarnings("serial")
public class LogPanel extends JPanel {
	
	public static final int TYPE_DEBUG = 0;
	public static final int TYPE_PRIMARY = 1;
	public static final int TYPE_SECONDARY = 2;
	public static final int TYPE_RECOVERY = 3;
	
	private JTabbedPane tabPane;
	private JTabbedPane trackingTabs;
	private JEditorPane debugConsole;
	private JEditorPane primaryConsole;
	private JEditorPane secondaryConsole;
	private JEditorPane recoveryConsole;
	
	public LogPanel() {
		super();
		super.setLayout(new BorderLayout());
		tabPane = new JTabbedPane(JTabbedPane.LEFT);
		add(tabPane, BorderLayout.CENTER);
		
		trackingTabs = new JTabbedPane(JTabbedPane.TOP);
		tabPane.addTab("Tracking", trackingTabs);
		
		primaryConsole = new JEditorPane();
		primaryConsole.setEditable(false);
		JScrollPane primaryPane = new JScrollPane(primaryConsole,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		trackingTabs.addTab("Primary", primaryPane);
		
		secondaryConsole = new JEditorPane();
		secondaryConsole.setEditable(false);
		JScrollPane secondaryPane = new JScrollPane(secondaryConsole,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		trackingTabs.addTab("Secondary", secondaryPane);
		
		recoveryConsole = new JEditorPane();
		recoveryConsole.setEditable(false);
		JScrollPane recoveryPane = new JScrollPane(recoveryConsole,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		trackingTabs.addTab("Recovery", recoveryPane);
		
		debugConsole = new JEditorPane();
		debugConsole.setEditable(false);
		
		JScrollPane debugPane = new JScrollPane(debugConsole,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabPane.addTab("Debug Log", debugPane);
		
		setupLoggers();
	}
	
	private void setupLoggers() {
		LogPanelHandler debugHandler = new LogPanelHandler(this, TYPE_DEBUG);
		debugHandler.setFormatter(new SimpleFormatter());
		Logger.getLogger("Debug").addHandler(debugHandler);
		
		LogPanelHandler primaryHandler = new LogPanelHandler(this, TYPE_PRIMARY);
		primaryHandler.setFormatter(new TrackingFormatter());
		Logger.getLogger("Primary").addHandler(primaryHandler);
		
		LogPanelHandler secondaryHandler = new LogPanelHandler(this, TYPE_SECONDARY);
		secondaryHandler.setFormatter(new TrackingFormatter());
		Logger.getLogger("Secondary").addHandler(secondaryHandler);
		
		LogPanelHandler recoveryHandler = new LogPanelHandler(this, TYPE_RECOVERY);
		recoveryHandler.setFormatter(new TrackingFormatter());
		Logger.getLogger("Recovery").addHandler(recoveryHandler);
	}
	
	public void publishRecord(int type, String record, SimpleAttributeSet style) {
		Document doc;
		switch(type) {
			case TYPE_DEBUG:
				doc = debugConsole.getDocument();
				break;
			case TYPE_PRIMARY:
				doc = primaryConsole.getDocument();
				break;
			case TYPE_SECONDARY:
				doc = secondaryConsole.getDocument();
				break;
			case TYPE_RECOVERY:
				doc = recoveryConsole.getDocument();
				break;
			default:
				return;
		}
		try {
			doc.insertString(doc.getLength(), record, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

}
