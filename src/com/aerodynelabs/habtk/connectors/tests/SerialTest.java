package com.aerodynelabs.habtk.connectors.tests;

import gnu.io.CommPortIdentifier;

import java.util.HashSet;
import java.util.Iterator;

import com.aerodynelabs.habtk.connectors.Serial;

public class SerialTest {
	
	public static void main(String args[]) {
		HashSet<CommPortIdentifier> ports = Serial.getAvailiableSerialPorts();
		Iterator<CommPortIdentifier> itr = ports.iterator();
		while(itr.hasNext()) {
			CommPortIdentifier port = itr.next();
			System.out.println(port.getName());
		}
	}

}
