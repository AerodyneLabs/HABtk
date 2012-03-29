package com.aerodynelabs.habtk.connectors;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.util.Enumeration;
import java.util.HashSet;

public class Serial {
	
	public static HashSet<CommPortIdentifier> getAvailiableSerialPorts() {
		HashSet<CommPortIdentifier> ports = new HashSet<CommPortIdentifier>();
		@SuppressWarnings("unchecked")	//XXX unchecked cast, can this be fixed?
		Enumeration<CommPortIdentifier> allPorts = CommPortIdentifier.getPortIdentifiers();
		while(allPorts.hasMoreElements()) {
			CommPortIdentifier com = (CommPortIdentifier)allPorts.nextElement();
			if(com.getPortType() != CommPortIdentifier.PORT_SERIAL) continue;
			try {
				CommPort port = com.open("HABtk", 50);
				port.close();
				ports.add(com);
			} catch(PortInUseException e) {
				continue;
			} catch(Exception e) {
				continue;
			}
		}
		
		return ports;
	}

}
