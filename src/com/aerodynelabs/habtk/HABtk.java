package com.aerodynelabs.habtk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

/**
 * The main class of HABtk
 * @author eharstad
 * 
 */
public class HABtk {
	
	private static final String VERSION = "0.01 Alpha";
	private static JFrame window;
	
	private static void setup() {
		// Configure window
		window = new JFrame("HABtk - " + VERSION);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Create components
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem fileExitItem = new JMenuItem("Exit");
		fileExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				window.setVisible(false);
				window.dispose();
			}
		});
		fileMenu.add(fileExitItem);
		menuBar.add(fileMenu);
		window.setJMenuBar(menuBar);
		
		MyDoggyToolWindowManager windowManager = new MyDoggyToolWindowManager();
		windowManager.registerToolWindow("Test","Test Tool",null,new JButton("Test Tool"),ToolWindowAnchor.LEFT);
		for(ToolWindow win : windowManager.getToolWindows()) win.setAvailable(true);
		window.getContentPane().add(windowManager);
	}
	
	private static void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		new HABtk();
		try {
			setup();
			start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
