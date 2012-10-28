package com.aerodynelabs.habtk.connectors;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.util.Enumeration;
import java.util.HashSet;

public class Serial {
	
	private SerialPort port;
	private 
	
	void connect(String name, int baud) throws Exception {
		CommPortIdentifier id = CommPortIdentifier.getPortIdentifier(name);
		if(id.isCurrentlyOwned()) throw new Exception(name + " is already in use!");
		
		if(id.getPortType() != CommPortIdentifier.PORT_SERIAL) throw new Exception(name + " is not a serial port!");
		port = (SerialPort) id.open("HABtk", 2000);
		port.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	}
	
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
