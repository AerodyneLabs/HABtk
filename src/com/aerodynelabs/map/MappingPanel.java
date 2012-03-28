package com.aerodynelabs.map;

import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class MappingPanel extends JSplitPane {
	
	private MapPanel map;
	private MapSettingsPanel settings;
	
	public MappingPanel() {
		super(JSplitPane.VERTICAL_SPLIT);
		super.setOneTouchExpandable(true);
		map = new MapPanel(42.01, -93.57, 11, "http://otile1.mqcdn.com/tiles/1.0.0/osm/", 18);
		settings = new MapSettingsPanel(map);
		super.setTopComponent(map);
		super.setBottomComponent(settings);
	}
	
	public void addOverlay(String name, MapOverlay overlay) {
		map.addOverlay(name, overlay);
	}
	
	public void addOverlay(MapOverlay overlay) {
		map.addOverlay(overlay);
	}

}
