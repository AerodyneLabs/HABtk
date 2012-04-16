package com.aerodynelabs.habtk.ui;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class TerminalPanel extends JPanel {
	
	JEditorPane terminal;
	
	public TerminalPanel() {
		super();
		super.setLayout(new BorderLayout());
		terminal = new JEditorPane();
		terminal.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane(terminal,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, BorderLayout.CENTER);
	}

}
