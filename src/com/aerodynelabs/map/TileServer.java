package com.aerodynelabs.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

/**
 * A class to download map images from a webserver.
 * @author Ethan Harstad
 *
 */
public class TileServer extends Thread {
	
	//private static final int TILE_SIZE = 256;
	
	private String url;
	private boolean OFFLINE = false;
	private int maxZoom;
	
	private final MapPanel client;
	
	private TileCache cache;
	private TileStore store;
	private ConcurrentLinkedQueue<Tile> queue;
	
	private boolean alive;
	
	private static BufferedImage dTile;
	private static BufferedImage zTile;
	private static BufferedImage nTile;
	
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
	
	/**
	 * Load local resources
	 */
	private void loadResources() {
		try {
			dTile = ImageIO.read(new File("resources/loadingTile.png"));
			zTile = ImageIO.read(new File("resources/zoomTile.png"));
			nTile = ImageIO.read(new File("resources/noTile.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the server connection
	 */
	public void close() {
		alive = false;
		queue.clear();
	}
	
	/**
	 * Get the name of a time
	 * @param tile
	 * @return
	 */
	protected static String getTileName(Tile tile) {
		return tile.getZoom() + "/" + tile.getX() + "/" + tile.getY() + ".png";
	}
	
	/**
	 * Get the address of a time
	 * @param tile
	 * @return
	 */
	private String getTileAddress(Tile tile) {
		return url + getTileName(tile);
	}
	
	/**
	 * Get tile image from cache or local store or server (in that order).
	 * @param x
	 * @param y
	 * @param zoom
	 * @return
	 */
	protected synchronized BufferedImage getTile(int x, int y, int zoom) {
		if(zoom > maxZoom) return zTile;
		if(
			x < 0 ||
			y < 0 ||
			x > (1 << zoom) ||
			y > (1 << zoom)
			) return nTile;
		
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
			// Check if image is out of date
			if(!OFFLINE) {
				if(store.isOld(tile)) {
					if(!queue.contains(tile)) {
						queue.add(tile);
					}
				}
			}
			return image;
		}
		// Download
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
	
	/**
	 * Start the download manager thread
	 */
	public void run() {
		while(alive) {
//			try {
//				InetAddress addr = InetAddress.getByName("www.google.com");
//				OFFLINE = !addr.isReachable(250);
//			} catch (Exception e1) {
//				OFFLINE = true;
//				e1.printStackTrace();
//			}
//			System.out.println(OFFLINE);
			while(!queue.isEmpty() && !OFFLINE) {
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
					wait(500);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
