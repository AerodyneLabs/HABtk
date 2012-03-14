package com.aerodynelabs.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TileStore {
	
	//TODO TileStore lifetime
	//private static final long LIFETIME = 1210000000l;
	
	private File store;
	
	public TileStore(String name) {
		store = new File("tiles/" + name);
		store.mkdirs();
	}
	
	protected void put(Tile tile, BufferedImage image) {
		String name = TileServer.getTileName(tile);
		File file = new File(store, name);
		file.mkdirs();
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected BufferedImage get(Tile tile) {
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
