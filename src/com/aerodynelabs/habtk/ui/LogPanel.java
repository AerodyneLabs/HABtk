package com.aerodynelabs.habtk.ui;

import java.awt.BorderLayout;
import java.util.logging.Logger;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

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
	private JEditorPane debugConsole;
	private JEditorPane primaryConsole;
	private JEditorPane secondaryConsole;
	private JEditorPane recoveryConsole;
	
	public LogPanel() {
		super();
		super.setLayout(new BorderLayout());
		tabPane = new JTabbedPane();
		add(tabPane, BorderLayout.CENTER);
		
		debugConsole = new JEditorPane();
		debugConsole.setEditable(false);
		
		JScrollPane debugPane = new JScrollPane(debugConsole,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabPane.addTab("Debug Log", debugPane);
	}
	
	public void appendRecord(int type, String record) {
		
	}

}
