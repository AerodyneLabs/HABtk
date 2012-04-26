package com.aerodynelabs.habtk.ui;

import java.util.LinkedHashMap;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapPath;

@SuppressWarnings("serial")
public class FlightListPanel extends JPanel {
	
	JTable table;
	LinkedHashMap<Predictor, MapPath> flights;
	
	class DataModel extends AbstractTableModel {
		
		private String headers[] = {
				"Launch Time",
				"Balloon",
				"Lift"
		};

		@Override
		public int getColumnCount() {
			return 0;
		}

		@Override
		public int getRowCount() {
			// TODO getRowCount
			return 0;
		}

		@Override
		public Object getValueAt(int arg0, int arg1) {
			// TODO getValueAt
			return null;
		}
		
	}
	
	public FlightListPanel() {
		super();
		
		table = new JTable(new DataModel());
	}

}
