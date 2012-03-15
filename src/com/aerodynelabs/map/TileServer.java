package com.aerodynelabs.map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

public class TileServer extends Thread {
	
	private static final int TILE_SIZE = 256;
	
	private String url;
	private boolean OFFLINE = false;
	private int maxZoom;
	
	private final MapPanel client;
	
	private TileCache cache;
	private TileStore store;
	private ConcurrentLinkedQueue<Tile> queue;
	
	private boolean alive;
	
	private static BufferedImage dTile;
	
	public TileServer(MapPanel client) {
		this("http://tile.openstreetmap.org/", 18, client);
	}
	
	public TileServer(String url, MapPanel client) {
		this(url, 17, client);
	}
	
	public TileServer(String url, int maxZoom, MapPanel client) {
		this.client = client;
		this.url = url;
		this.maxZoom = maxZoom;
		cache = new TileCache();
		String name = url.replaceAll("http://", "");
		name = name.replaceAll("[./\\\\]", "_");
		name = name.replaceAll("[^a-zA-Z0-9_]", "");
		store = new TileStore(name);
		queue = new ConcurrentLinkedQueue<Tile>();
		alive = true;
		this.start();
		loadResources();
	}
	
	private void loadResources() {
		try {
			dTile = ImageIO.read(new File("resources/loadingTile.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		alive = false;
		queue.clear();
	}
	
	protected static String getTileName(Tile tile) {
		return tile.getZoom() + "/" + tile.getX() + "/" + tile.getY() + ".png";
	}
	
	private String getTileAddress(Tile tile) {
		return url + getTileName(tile);
	}
	
	protected synchronized BufferedImage getTile(int x, int y, int zoom) {
		if(zoom > maxZoom) {
			BufferedImage tile = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_RGB);
			//TODO
			return tile;
		}
		
		BufferedImage image = null;
		Tile tile = new Tile(x, y, zoom);
		//System.out.println(getTileName(tile));
		// Check cache
		image = cache.get(tile);
		if(image != null) {
			//System.out.println("Found in Cache");
			return image;
		}
		// Check store
		image = store.get(tile);
		if(image != null) {
			//System.out.println("Found in Store");
			cache.put(tile, image);
			return image;
		}
		// Download
		// TODO Downloading image tile
		if(!OFFLINE) {
			if(!queue.contains(tile)) {
				//System.out.println("Added " + tile);
				queue.add(tile);
			}
			this.notify();
		}
		
		return dTile;
	}
	
	protected int getMaxZoom() {
		return maxZoom;
	}
	
	public void run() {
		while(alive) {
			while(!queue.isEmpty()) {
				Tile tile = queue.poll();
				//System.out.println("Getting " + tile);
				BufferedImage image = null;
				try {
					image = ImageIO.read(new URL(getTileAddress(tile)));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(image != null) {
					store.put(tile, image);
					client.updateNotify();
				}
			}
			try {
				synchronized(this) {
					// System.out.println("Waiting");
					wait(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
