package com.aerodynelabs.map;

//TODO overlay color display
//TODO allow overlay color select

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class MapSettingsPanel extends JPanel {
	
	private JTable table;
	private MapPanel map;
	
	class DataModel extends AbstractTableModel {

		private String[] headers = {"Enable", "Name", "Color"};
		
		@Override
		public int getColumnCount() {
			return headers.length;
		}
		
		@Override
		public String getColumnName(int c) {
			return headers[c];
		}
		
		@Override
		public boolean isCellEditable(int r, int c) {
			if(c == 0) return true;
			if(c == 2) return true;
			return false;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int c) {
			if(c == 0) return Boolean.class;
			return String.class;
		}

		@Override
		public int getRowCount() {
			return map.overlays.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			Object[] keys = map.overlays.keySet().toArray();
			MapOverlay overlay = map.overlays.get(keys[row]);
			switch(col) {
				case 0:
					return overlay.isEnabled();
				case 1:
					return overlay.getName();
				case 2:
					return overlay.getColor();
			}
			return null;
		}
		
		@Override
		public void setValueAt(Object o, int r, int c) {
			Object[] keys = map.overlays.keySet().toArray();
			MapOverlay overlay = map.overlays.get(keys[r]);
			if(c == 0) {
				overlay.setEnabled(o.equals(true));
				return;
			}
			if(c == 2) {
				
				return;
			}
			
			//FIXME force repaint on state change
			//map.repaint();
			//map.paintImmediately(0, 0, map.getWidth(), map.getHeight());
		}
		
	}
	
	public MapSettingsPanel(MapPanel map) {
		super();
		super.setPreferredSize(new Dimension(600, 100));
		
		this.map = map;
		
		BorderLayout layout = new BorderLayout();
		super.setLayout(layout);
		table = new JTable(new DataModel());
		
		JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		super.add(scroll, BorderLayout.CENTER);
	}

}
