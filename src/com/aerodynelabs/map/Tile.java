package com.aerodynelabs.map;

/**
 * A class to represent a map tile.
 * @author Ethan Harstad
 *
 */
public class Tile {
	
	private final int x, y, zoom;
	
	public Tile(int x, int y, int zoom) {
		this.x = x;
		this.y = y;
		this.zoom = zoom;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + zoom;
		return result;
	}
	
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null) return false;
		if(getClass() != o.getClass()) return false;
		Tile obj = (Tile)o;
		if(x != obj.x) return false;
		if(y != obj.y) return false;
		if(zoom != obj.zoom) return false;
		return true;
	}
	
	public String toString() {
		return zoom + "/" + x + "/" + y;
	}

}
