package com.aerodynelabs.habtk.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class TerminalPanel extends JPanel {
	
	JTextPane terminal;
	
	public TerminalPanel() {
		super();
		super.setLayout(new BorderLayout());
		terminal = new JTextPane();
		terminal.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(terminal,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
	}

}
