package com.aerodynelabs.map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class MapSettingsPanel extends JPanel {
	
	private JTable table;
	
	public MapSettingsPanel() {
		super();
		table = new JTable();
		
		JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		super.add(scroll);
	}

}
