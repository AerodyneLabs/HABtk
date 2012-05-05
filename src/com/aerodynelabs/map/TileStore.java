package com.aerodynelabs.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A class to store tiles locally on disk.
 * @author Ethan Harstad
 *
 */
public class TileStore {
	
	protected static final long LIFETIME = 1210000000l;
	
	private File store;
	
	public TileStore(String name) {
		store = new File("tiles/" + name);
		store.mkdirs();
	}
	
	protected synchronized void put(Tile tile, BufferedImage image) {
		String name = TileServer.getTileName(tile);
		File file = new File(store, name);
		file.mkdirs();
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected boolean isOld(Tile tile) {
		String name = TileServer.getTileName(tile);
		File file = new File(store, name);
		if(file.exists()) {
			if(file.lastModified() > System.currentTimeMillis() - LIFETIME) return false;
		}
		return true;
	}
	
	protected synchronized BufferedImage get(Tile tile) {
		String name = TileServer.getTileName(tile);
		File file = new File(store, name);
		if(!file.exists()) return null;
		BufferedImage image;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			image = null;
			e.printStackTrace();
		}
		return image;
	}

}
