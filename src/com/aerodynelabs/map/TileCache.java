package com.aerodynelabs.map;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

/**
 * A class to store map tiles/images in local memory.
 * @author Ethan Harstad
 *
 */
public class TileCache {
	
	private int maxSize;
	
	@SuppressWarnings("serial")
	private LinkedHashMap<Tile, BufferedImage> cache = new LinkedHashMap<Tile, BufferedImage>(maxSize, 0.75f, true) {
		protected boolean removeEldestEntry(java.util.Map.Entry<Tile, BufferedImage> eldest) {
			return size() > maxSize;
		}
	};
	
	protected void put(Tile tile, BufferedImage image) {
		cache.put(tile, image);
	}
	
	protected BufferedImage get(Tile tile) {
		return cache.get(tile);
	}

	public TileCache() {
		this(256);
	}
	
	public TileCache(int size) {
		maxSize = size;
	}
	
}
