package com.aerodynelabs.habtk;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.ToolWindowType;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import com.aerodynelabs.habtk.ui.AboutDialog;
import com.aerodynelabs.habtk.ui.TerminalPanel;
import com.aerodynelabs.habtk.ui.TrackingPanel;
import com.aerodynelabs.map.MappingPanel;

/**
 * The main class of HABtk
 * @author eharstad
 * 
 */
public class HABtk {
	
	private static final String VERSION = "0.01 Alpha";
	private static JFrame window;
	private static MyDoggyToolWindowManager windowManager;
	private static MappingPanel map;
	
	private static void setup() {
		// Configure window
		window = new JFrame("HABtk - " + VERSION);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Create components
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		window.setJMenuBar(menuBar);
		
		JMenuItem fileSaveItem = new JMenuItem("Save Map");
		fileSaveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO save map action
			}
		});
		fileMenu.add(fileSaveItem);
		
		JMenuItem filePrintItem = new JMenuItem("Print Map");
		filePrintItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO print map action
			}
		});
		fileMenu.add(filePrintItem);
		
		fileMenu.add(new JSeparator());
		JMenuItem fileExitItem = new JMenuItem("Exit");
		fileExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.setVisible(false);
				exit();
			}
		});
		fileMenu.add(fileExitItem);
		
		JMenuItem helpHelpItem = new JMenuItem("Help Contents");
		helpHelpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO help contents action
			}
		});
		helpMenu.add(helpHelpItem);
		
		JMenuItem helpUpdateItem = new JMenuItem("Check for Updates");
		helpUpdateItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO help update action
				new Updater();
			}
		});
		helpMenu.add(helpUpdateItem);
		
		JMenuItem helpAboutItem = new JMenuItem("About");
		helpAboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AboutDialog(window);
			}
		});
		helpMenu.add(helpAboutItem);
		
		map = new MappingPanel();
		
		windowManager = new MyDoggyToolWindowManager();
		windowManager.registerToolWindow("Log", "Log", null, new TerminalPanel(), ToolWindowAnchor.BOTTOM);
		windowManager.registerToolWindow("Tracking", "Tracking", null, new TrackingPanel(), ToolWindowAnchor.LEFT);
		windowManager.registerToolWindow("Map", "Map", null, map, ToolWindowAnchor.RIGHT);
		for(ToolWindow win : windowManager.getToolWindows()) win.setAvailable(true);
		windowManager.getToolWindow("Log").setType(ToolWindowType.SLIDING);
		windowManager.getToolWindow("Map").setActive(true);
		
		window.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				try {
					File workspace = new File("workspace.xml");
					if(workspace.exists()) {
						FileInputStream in = new FileInputStream(workspace);
						windowManager.getPersistenceDelegate().apply(in);
						in.close();
					}
				} catch(Exception e1) {
					e1.printStackTrace();
				}
			}
			
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		window.getContentPane().add(windowManager);
	}
	
	private static void start() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.setVisible(true);
			}
		});
	}
	
	private static void exit() {
		try {
			FileOutputStream out = new FileOutputStream("workspace.xml");
			windowManager.getPersistenceDelegate().save(out);
			out.close();
		} catch(Exception e1) {
			e1.printStackTrace();
		}
		window.dispose();
		System.exit(0);
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
