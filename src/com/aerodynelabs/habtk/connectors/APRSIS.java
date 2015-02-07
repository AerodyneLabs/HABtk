package com.aerodynelabs.habtk.connectors;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class APRSIS implements APRSSource {
	
	private Socket socket;
	public BufferedReader in;
	private BufferedWriter out;
	private boolean valid = false;
	
	private String address;
	private int port;
	private String callsign;
	private String code;
	
	private String filter = "";
	
	public APRSIS(String callsign, String code) {
		this("http://rotate.aprs2.net", 14580, callsign, code);
	}
	
	public APRSIS(String addr, int port, String callsign, String code) {
		this.address = addr;
		this.port = port;
		
		try {
			socket = new Socket(this.address, this.port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		login(callsign, code);
	}
	
	protected void login(String callsign, String code) {
		this.callsign = callsign;
		this.code = code;
		
		try {
			// Wait for notice from server
			while(!in.ready());		// Nothing yet
			in.readLine();			// Throw it away
			
			// Send login string
			String msg = "user " + callsign + " pass " + code + " vers HAB-tk v0.1\r\n";
			out.write(msg);
			out.flush();
			
			// Wait for a response
			for(int i = 0; i < 5; i++) {
				while(!in.ready());	// Wait for response
				Scanner test = new Scanner(in.readLine());
				String response = test.findInLine("logresp");
				if(response != null) {
					if(test.findInLine("unverified") != null) {
						valid = false;
					} else {
						valid = true;
					}
					test.close();
					return;
				}
				test.close();
			}	// Timeout without response
		} catch (IOException e) {
			e.printStackTrace();
		}
		valid = false;
	}
	
	@Override
	public boolean isValid() {
		return valid;
	}
	
	@Override
	public boolean isReady() {
		try {
			return in.ready();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String readLine() {
		String msg = "";
		if(valid) {
			try {
				msg = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				
			}
		}
		return msg;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
		write("#filter " + this.filter);
	}
	
	public void addFilter(String filter) {
		this.filter += " " + filter;
		write("#filter " + this.filter);
	}
	
	public String getFilter() {
		return filter;
	}
	
	public boolean write(String msg) {
		try {
			out.write(msg + "\r\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String toString() {
		return callsign + "@" + address + ":" + port + " Auth: " + code; 
	}

}
