package com.aerodynelabs.habtk.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.event.TableModelEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;

import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapOverlay;
import com.aerodynelabs.map.MapPanel;
import com.aerodynelabs.map.MapPath;

/**
 * A panel to display a series of predictions.
 * @author Ethan Harstad
 *
 */
@SuppressWarnings("serial")
public class FlightListPanel extends JPanel {
	
	private JTable table;
	private TableModel model;
	private Vector<Flight> flights = new Vector<Flight>();
	private JPopupMenu menu;
	
	private MapPanel map;
	private int activeRow;
	
	private int lastColor = 0;
	private static final Color colors[] = {
		new Color(	255,	0,	0),
		new Color(	255,	116,	0),
		new Color(	255,	170,	0),
		new Color(	255,	211,	0),
		new Color(	255,	255,	0),
		new Color(	159,	238,	0),
		new Color(	0,		204,	0),
		new Color(	0,		153,	153),
		new Color(	18,		64,		171),
		new Color(	57,		20,		175),
		new Color(	113,	9,		170),
		new Color(	205,	0,		116)
	};
	
	/**
	 * Custom date and time renderer
	 */
	static class DateTimeRenderer extends SubstanceDefaultTableCellRenderer {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		public DateTimeRenderer() {
			super();
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		@Override
		public void setValue(Object value) {
			setText((value == null) ? "" : sdf.format(value));
		}
		
	}
	
	/**
	 * Custom time renderer
	 */
	static class ElapsedTimeRenderer extends SubstanceDefaultTableCellRenderer {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		
		public ElapsedTimeRenderer() {
			super();
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		@Override
		public void setValue(Object value) {
			setText((value == null) ? "" : sdf.format(value));
		}
		
	}
	
	/**
	 * Relates a predictor to a path and overlay
	 */
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
	
	/**
	 * Custom table data model
	 */
	class DataModel extends AbstractTableModel {
		
		private String headers[] = {
				"Show",
				"Launch Time (UTC)",
				"Balloon Type",
				"Lift (kg)",
				"Time Aloft",
				"Distance (km)",
				"Altitude (km)"
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
				return Double.toString(flight.flight.getLift());
			case 4:		// Time aloft
				return flight.path.getElapsedTime() * 1000;
			case 5:		// Distance
				return Double.toString(Math.floor(flight.path.getDistance()/10 + 0.5) / 100);
			case 6:		// Altitude
				return Double.toString(Math.floor(flight.path.getMaxAlt()) / 1000);
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
	
	/**
	 * Construct a flight list that displays on the given map.
	 * @param map
	 */
	public FlightListPanel(final MapPanel map) {
		super();
		setLayout(new BorderLayout());
		
		this.map = map;
		
		model = new DataModel();
		table = new JTable(model);
		JScrollPane scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroller, BorderLayout.CENTER);
		
		DefaultTableCellRenderer defaultRenderer = new SubstanceDefaultTableCellRenderer();
		defaultRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.setDefaultRenderer(Object.class, defaultRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(new DateTimeRenderer());
		table.getColumnModel().getColumn(4).setCellRenderer(new ElapsedTimeRenderer());
		
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableCellRenderer renderer = table.getTableHeader().getDefaultRenderer();
		for(int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(
	                (int) (renderer.getTableCellRendererComponent(table, table.getModel().getColumnName(i), false, false, 0, i).getPreferredSize().width * 1.1));
		}
		
		menu = new JPopupMenu();
		JMenuItem saveFlight = new JMenuItem("Save Flight");
		saveFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				flights.get(activeRow).flight.save();
			}
		});
		menu.add(saveFlight);
		JMenuItem saveKml = new JMenuItem("Export KML");
		saveKml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("flights/"));
				FileFilter filter = new FileNameExtensionFilter("KML Files", "kml");
				chooser.setFileFilter(filter);
				
				int val = chooser.showSaveDialog(null);
				if(val == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String name = file.getName();
					if(name.lastIndexOf('.') == -1) {
						name += ".kml";
						file = new File(file.getParentFile(), name);
					}
					flights.get(activeRow).path.exportKML(file);
				}
			}
		});
		menu.add(saveKml);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				activeRow = table.rowAtPoint(e.getPoint());
				if(e.getButton() == MouseEvent.BUTTON3) {
					menu.show(e.getComponent(), e.getX(), e.getY());
					return;
				}
				if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
					for(Flight f : flights) {
						f.overlay.setEnabled(false);
					}
					flights.get(activeRow).overlay.setEnabled(true);
					map.updateNotify();
					table.tableChanged(new TableModelEvent(model));
					return;
				}
			}
		});
		
		// Disable tooltips for performance increase
		ToolTipManager.sharedInstance().unregisterComponent(table);
		ToolTipManager.sharedInstance().unregisterComponent(table.getTableHeader());
	}
	
	/**
	 * Add a flight to the list.
	 * @param flight
	 * @param path
	 */
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
