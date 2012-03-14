package com.aerodynelabs.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapPanel extends JPanel implements MouseListener, MouseMotionListener{
	
	private int x, y;
	private double lat, lon;
	private int zoom;
	
	private Point mouseDown;
	
	private TileServer server;
	
	public MapPanel() {
		this(42.01, -93.57, 11, "http://tile.openstreetmap.org/", 18);
	}
	
	public MapPanel(double lat, double lon, int zoom) {
		this(lat, lon, zoom, "http://tile.openstreetmap.org/", 18);
	}
	
	public MapPanel(double lat, double lon, int zoom, String url) {
		this(lat, lon, zoom, url, 17);
	}
	
	public MapPanel(double lat, double lon, int zoom, String url, int maxZoom) {
		super.setPreferredSize(new Dimension(640, 480));
		server = new TileServer(url, maxZoom, this);
		setZoom(zoom);
		setCenter(lat, lon);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	protected void setZoom(int zoom) {
		this.zoom = zoom;
		repaint();
	}
	
	protected int getZoom() {
		return zoom;
	}
	
	protected void zoomIn() {
		System.out.println("Zoom In");
		setZoom(zoom + 1);
	}
	
	protected void zoomOut() {
		System.out.println("Zoom Out");
		setZoom(zoom - 1);
	}
	
	protected void setCenter(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
		repaint();
	}
	
	protected static double tile2lon(int x, int zoom) {
		return x / Math.pow(2.0, zoom) * 360.0 - 180;
	}
	
	protected static double tile2lat(int y, int zoom) {
		double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, zoom);
		return Math.toDegrees(Math.atan(Math.sinh(n)));
	}
	
	protected static int lat2tile(double lat, int zoom) {
		return (int)Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * (1<<zoom));
	}
	
	protected static int lon2tile(double lon, int zoom) {
		return (int)Math.floor((lon + 180) / 360 * (1<<zoom));
	}
	
	protected static int lon2pos(double lon, int zoom) {
		double max = 256 * (1 << zoom);
		return (int)Math.floor((lon + 180) / 360 * max);
	}
	
	protected static int lat2pos(double lat, int zoom) {
		double max = 256 * (1 << zoom);
		double rlat = Math.toRadians(lat);
		return (int)Math.floor((1 - Math.log(Math.tan(rlat) + 1 / Math.cos(rlat)) / Math.PI) / 2 * max);
	}
	
	@Override
	protected void paintComponent(Graphics g0) {
		super.paintComponents(g0);
		Graphics2D g = (Graphics2D)g0.create();
		int width = this.getWidth();
		int height = this.getHeight();
		g.translate(width/2, height/2);
		g.setColor(Color.BLACK);
		
		int sx = lon2tile(lon, zoom);
		int sy = lat2tile(lat, zoom);
		int nx = ((width / 256) + 2) / 2;
		int ny = ((height / 256) + 2) / 2;
		double slon = tile2lon(sx, zoom);
		double slat = tile2lat(sy, zoom);
		int ox = (int)((256/(tile2lon(sx+1, zoom)-slon))*(lon-slon)+0.5);
		int oy = (int)((256/(tile2lat(sy+1, zoom)-slat))*(lat-slat)+0.5);
		
		for(int i = -nx; i <= nx; i++) {
			for(int j = -ny; j <= ny; j++) {
				int dx = i * 256 - ox;
				int dy = j * 256 - oy;
				BufferedImage tile = server.getTile(sx+i, sy+j, zoom);
				g.drawImage(tile, dx, dy, null);
			}
		}
		
		g.drawOval(-4, -4, 9, 9);
	}
	
	@Override
	public void removeNotify() {
		super.removeNotify();
		server.close();
	}
	
	protected void translateMap(int x, int y) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Not needed
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			if(e.getButton() == MouseEvent.BUTTON1) {
				zoomIn();
				e.consume();
			} else if(e.getButton() == MouseEvent.BUTTON3) {
				zoomOut();
				e.consume();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Not needed
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Not needed
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Not needed
	}

	
	public void updateNotify() {
		repaint();
	}

}
