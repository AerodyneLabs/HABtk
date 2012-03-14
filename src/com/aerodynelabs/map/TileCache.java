package com.aerodynelabs.map;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

public class TileCache {
	
	private int maxSize;
	
	@SuppressWarnings("serial")
	private LinkedHashMap<Tile, BufferedImage> map = new LinkedHashMap<Tile, BufferedImage>(maxSize, 0.75f, true) {
		protected boolean removeEldestEntry(java.util.Map.Entry<Tile, BufferedImage> eldest) {
			return size() > maxSize;
		}
	};
	
	protected void put(Tile tile, BufferedImage image) {
		map.put(tile, image);
	}
	
	protected BufferedImage get(Tile tile) {
		return map.get(tile);
	}

	public TileCache() {
		this(256);
	}
	
	public TileCache(int size) {
		maxSize = size;
	}
	
}
