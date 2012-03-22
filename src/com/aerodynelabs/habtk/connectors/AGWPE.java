package com.aerodynelabs.habtk.connectors;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class AGWPE {
	
	class AGWFrame {
		byte port;
		byte kind;
		byte pid = 0x00;
		byte from[] = new byte[10];
		byte to[] = new byte[10];
		int len = 0;
		byte data[];
	}
	
	private Socket socket;
	private BufferedInputStream in;
	private BufferedOutputStream out;
	
	public AGWPE(String addr, int port) {
		try {
			socket = new Socket(addr, port);
			in = new BufferedInputStream(socket.getInputStream());
			out = new BufferedOutputStream(socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		if(socket == null) return false;
		return true;
	}
	
	public void disconnect() {
		if(socket == null) return;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void login(String user, String pass) {
		AGWFrame frame = new AGWFrame();
		frame.port = 0;
		frame.kind = 'P';
		frame.len = 510;
		//TODO send login frame
	}
	
	public void registerCallsign(String call) {
		//TODO registerCallsign
	}

}
