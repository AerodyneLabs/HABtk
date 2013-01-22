package com.aerodynelabs.habtk.connectors.tests;

import com.aerodynelabs.habtk.connectors.AGWPE;

public class AGWPETest {
	
	public static void main(String args[]) {
		AGWPE agw = new AGWPE("localhost", 8000);
		System.out.println(agw.isConnected());
		while(true) {
			
		}
	}

}
