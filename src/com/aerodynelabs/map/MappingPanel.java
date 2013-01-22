package com.aerodynelabs.map;

import javax.swing.JSplitPane;

/**
 * A map utilizing the default settings panel.
 * @author Ethan Harstad
 *
 */
@SuppressWarnings("serial")
public class MappingPanel extends JSplitPane {
	
	private MapPanel map;
	private MapSettingsPanel settings;
	
	/**
	 * Create a default map.
	 */
	public MappingPanel() {
		super(JSplitPane.VERTICAL_SPLIT);
		super.setOneTouchExpandable(true);
		map = new MapPanel(42.01, -93.57, 11, "http://otile1.mqcdn.com/tiles/1.0.0/osm/", 18);
		settings = new MapSettingsPanel(map);
		super.setTopComponent(map);
		super.setBottomComponent(settings);
	}
	
	public void setCenter(double lat, double lon) {
		map.setCenter(lat, lon);
	}
	
	public MapOverlay getOverlay(String name) {
		return map.getOverylay(name);
	}
	
	/**
	 * Add the named overlay to the map.
	 * @param name
	 * @param overlay
	 */
	public void addOverlay(String name, MapOverlay overlay) {
		map.addOverlay(name, overlay);
	}
	
	/**
	 * Add the overlay to the map.
	 * @param overlay
	 */
	public void addOverlay(MapOverlay overlay) {
		map.addOverlay(overlay);
	}

}
