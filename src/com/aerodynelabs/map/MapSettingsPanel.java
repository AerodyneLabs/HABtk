package com.aerodynelabs.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class MapSettingsPanel extends JPanel {
	
	private JTable table;
	private MapPanel map;
	
	class ColorEditor extends AbstractCellEditor
						implements TableCellEditor, ActionListener {
		
		JButton button;
		JColorChooser chooser;
		Color curColor;
		JDialog dialog;

		public ColorEditor() {
			button = new JButton();
			button.setActionCommand("edit");
			button.addActionListener(this);
			button.setBorderPainted(false);
			
			chooser = new JColorChooser();
			chooser.setPreviewPanel(new JPanel());
			
			AbstractColorChooserPanel[] panels = chooser.getChooserPanels();
			for(int i = 0; i < panels.length; i++) {
				String name = panels[i].getClass().getName();
				if(name != "javax.swing.colorchooser.DefaultSwatchChooserPanel") chooser.removeChooserPanel(panels[i]);
			}
			
			dialog = JColorChooser.createDialog(button, "Pick a Color", true, chooser, this, null);
		}
		
		@Override
		public Object getCellEditorValue() {
			return curColor;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("edit")) {
				button.setBackground(curColor);
				chooser.setColor(curColor);
				dialog.setVisible(true);
				
				fireEditingStopped();
			} else {
				curColor = chooser.getColor();
			}
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean isSelected, int r, int c) {
			curColor = (Color)value;
			return button;
		}
		
	}
	
	class ColorRenderer extends JLabel implements TableCellRenderer {
		
		Border selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                table.getSelectionBackground());
		Border unselectedBorder;
		
		public ColorRenderer() {
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable arg0,
				Object color, boolean isSelected, boolean hasFocus, int r, int c) {
			Color bg = (Color)color;
			setBackground(bg);
			if(isSelected) {
				setBorder(BorderFactory.createMatteBorder(2, 5, 2, 5, bg));
			} else {
				setBorder(BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground()));
			}
			return this;
		}
		
	}
	
	class DataModel extends AbstractTableModel {

		private String[] headers = {"Enable", "Name", "Color", "History"};
		
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
			if(c == 3) return true;
			return false;
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int c) {
			if(c == 0) return Boolean.class;
			if(c == 2) return Color.class;
			if(c == 3) return Boolean.class;
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
				case 3:
					return overlay.getDrawPaths();
			}
			return null;
		}
		
		@Override
		public void setValueAt(Object o, int r, int c) {
			Object[] keys = map.overlays.keySet().toArray();
			MapOverlay overlay = map.overlays.get(keys[r]);
			if(c == 0) overlay.setEnabled(o.equals(true));
			if(c == 2) overlay.setColor((Color)o);
			if(c == 3) overlay.setDrawPaths(o.equals(true));
			
			map.updateNotify();
		}
		
	}
	
	public MapSettingsPanel(MapPanel map) {
		super();
		super.setPreferredSize(new Dimension(600, 75));
		
		this.map = map;
		
		BorderLayout layout = new BorderLayout();
		super.setLayout(layout);
		table = new JTable(new DataModel());
		table.setDefaultRenderer(Color.class, new ColorRenderer());
		table.setDefaultEditor(Color.class, new ColorEditor());
		
		JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		super.add(scroll, BorderLayout.CENTER);
	}

}
