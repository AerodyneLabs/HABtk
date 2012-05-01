package com.aerodynelabs.habtk.ui;

import java.awt.BorderLayout;
import java.sql.Date;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapOverlay;
import com.aerodynelabs.map.MapPanel;
import com.aerodynelabs.map.MapPath;

@SuppressWarnings("serial")
public class FlightListPanel extends JPanel {
	
	private JTable table;
	private Vector<Flight> flights = new Vector<Flight>();
	
	private MapPanel map;
	
	class Flight {
		
		protected Predictor flight;
		protected MapPath path;
		protected MapOverlay overlay;
		
		public Flight(Predictor f, MapPath p, MapOverlay o) {
			flight = f;
			path = p;
			overlay = o;
		}
		
	}
	
	class DataModel extends AbstractTableModel {
		
		private String headers[] = {
				"Show",
				"Launch Time",
				"Balloon",
				"Lift",
				"Time Aloft",
				"Distance"
		};

		@Override
		public int getColumnCount() {
			return headers.length;
		}
		
		@Override
		public String getColumnName(int c) {
			return headers[c];
		}
		
		@Override
		public boolean isCellEditable(int r, int c){
			if(c == 0) return true;
			return false;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int c) {
			switch(c) {
			case 0:
				return Boolean.class;
			case 1:
				return Date.class;
			case 3:
				return Double.class;
			case 4:
				return Date.class;
			case 5:
				return Double.class;
			}
			return String.class;
		}

		@Override
		public int getRowCount() {
			return flights.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			Flight flight = flights.get(row);
			switch(col) {
			case 0:		// Enabled
				return flight.overlay.isEnabled();
			case 1:		// Launch Time
				return flight.path.getStartTime();
			case 2:		// Balloon
				return flight.flight.getTypeName();
			case 3:		// Lift
				return flight.flight.getLift();
			case 4:		// Time aloft
				return flight.path.getElapsedTime();
			case 5:		// Distance
				return flight.path.getDistance();
			}
			return null;
		}
		
		@Override
		public void setValueAt(Object o, int r, int c) {
			Flight flight = flights.get(r);
			if(c == 0) {
				flight.overlay.setEnabled(o.equals(true));
				map.updateNotify();
			}
		}
		
	}
	
	public FlightListPanel(MapPanel map) {
		super();
		setLayout(new BorderLayout());
		
		this.map = map;
		
		table = new JTable(new DataModel());
		JScrollPane scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroller, BorderLayout.CENTER);
	}
	
	public void addFlight(Predictor flight, MapPath path) {
		MapOverlay overlay = new MapOverlay("Prediction " + flights.size()+1);
		flights.add(new Flight(flight, path, overlay));
		repaint();
		map.updateNotify();
	}

}
