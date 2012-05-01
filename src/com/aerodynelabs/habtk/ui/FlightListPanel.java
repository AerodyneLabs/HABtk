package com.aerodynelabs.habtk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapOverlay;
import com.aerodynelabs.map.MapPanel;
import com.aerodynelabs.map.MapPath;

@SuppressWarnings("serial")
public class FlightListPanel extends JPanel {
	
	private JTable table;
	private TableModel model;
	private Vector<Flight> flights = new Vector<Flight>();
	
	private MapPanel map;
	
	private int lastColor = 0;
	private static final Color colors[] = {
//		new Color(0,	0,	63),
		new Color(0,	0,	127),
//		new Color(0,	0,	191),
		new Color(0,	0,	255),
//		new Color(0,	63,	0),
		new Color(0,	127,	0),
//		new Color(0,	191,	0),
		new Color(0,	255,	0),
//		new Color(63,	0,	0),
		new Color(127,	0,	0),
//		new Color(191,	0,	0),
		new Color(255,	0,	0)
	};
	
	static class DateTimeRenderer extends DefaultTableCellRenderer {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		public DateTimeRenderer() {
			super();
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		
		@Override
		public void setValue(Object value) {
			setText((value == null) ? "" : sdf.format(value));
		}
		
	}
	
	static class ElapsedTimeRenderer extends DefaultTableCellRenderer {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		public ElapsedTimeRenderer() {
			super();
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		
		@Override
		public void setValue(Object value) {
			setText((value == null) ? "" : sdf.format(value));
		}
		
	}
	
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
				"Distance",
				"Altitude"
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
				return flight.path.getStartTime() * 1000;
			case 2:		// Balloon
				return flight.flight.getTypeName();
			case 3:		// Lift
				return flight.flight.getLift();
			case 4:		// Time aloft
				return flight.path.getElapsedTime() * 1000;
			case 5:		// Distance
				return Double.toString(Math.floor(flight.path.getDistance()/10 + 0.5) / 100) + " km";
			case 6:		// Altitude
				return Double.toString(Math.floor(flight.path.getMaxAlt()) / 1000) + " km";
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
		
		model = new DataModel();
		table = new JTable(model);
		table.getColumnModel().getColumn(1).setCellRenderer(new DateTimeRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new ElapsedTimeRenderer());
		
		
		
		JScrollPane scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroller, BorderLayout.CENTER);
	}
	
	public void addFlight(Predictor flight, MapPath path) {
		MapOverlay overlay = new MapOverlay("Prediction " + flights.size()+1);
		overlay.addPath("Prediction", path);
		overlay.setColor(colors[lastColor++]);
		if(lastColor >= colors.length) lastColor = 0;
		map.addOverlay(overlay);
		flights.add(0, new Flight(flight, path, overlay));
		table.tableChanged(new TableModelEvent(model));
		map.updateNotify();
	}

}
