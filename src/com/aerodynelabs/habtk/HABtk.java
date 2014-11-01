package com.aerodynelabs.habtk;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.noos.xing.mydoggy.Content;
import org.noos.xing.mydoggy.ContentManager;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.ToolWindowType;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.noos.xing.mydoggy.plaf.ui.content.MyDoggyMultiSplitContentManagerUI;
//import org.pushingpixels.substance.api.DecorationAreaType;
//import org.pushingpixels.substance.api.SubstanceColorScheme;
//import org.pushingpixels.substance.api.SubstanceLookAndFeel;
//import org.pushingpixels.substance.api.skin.SubstanceTwilightLookAndFeel;


import com.aerodynelabs.habtk.help.HelpWindow;
import com.aerodynelabs.habtk.logging.DebugLog;
import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.habtk.tracking.LocationService;
import com.aerodynelabs.habtk.tracking.PositionEvent;
import com.aerodynelabs.habtk.tracking.PositionListener;
import com.aerodynelabs.habtk.tracking.TrackingService;
import com.aerodynelabs.habtk.ui.AboutDialog;
import com.aerodynelabs.habtk.ui.LocationConfigDialog;
import com.aerodynelabs.habtk.ui.MessageDialog;
import com.aerodynelabs.habtk.ui.PredictionPanel;
import com.aerodynelabs.habtk.ui.LogPanel;
import com.aerodynelabs.habtk.ui.TrackingConfigDialog;
import com.aerodynelabs.habtk.ui.TrackingPanel;
import com.aerodynelabs.map.MapOverlay;
import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MapPoint;
import com.aerodynelabs.map.MappingPanel;

/**
 * The main class of HABtk
 * @author Ethan Harstad
 * 
 */
public class HABtk implements PositionListener {
	
	private static final String VERSION = "0.01 Alpha";
	
	private static final Logger debugLog = Logger.getLogger("Debug");
	
	private static HABtk habtk;
	private static JFrame window;
	private static MyDoggyToolWindowManager windowManager;
	private static ContentManager contentManager;
	private static JCheckBoxMenuItem flightTrackItem;
	private static JCheckBoxMenuItem locationTrackItem;
	private static TrackingPanel trackingPanel;
	private static MappingPanel trackingMap;
	
	private static boolean tracking = false;
	private static BalloonFlight flight;
	private static TrackingService trackingService;
	private static LocationService locationService;
	private static MapPoint prevPoint = null;
	private static int descendingPkts = 0;
	
	/**
	 * Create GUI Components
	 */
	private static void setup() {
		// Configure window
		window = new JFrame("HABtk - " + VERSION);
		window.setMinimumSize(new Dimension(800, 600));
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		windowManager = new MyDoggyToolWindowManager();
		contentManager = windowManager.getContentManager();
		contentManager.setContentManagerUI(new MyDoggyMultiSplitContentManagerUI());
		
		// Create components
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu flightMenu = new JMenu("Tracking");
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(fileMenu);
		menuBar.add(flightMenu);
		menuBar.add(helpMenu);
		window.setJMenuBar(menuBar);
		
		JMenuItem fileNewFlightItem = new JMenuItem("New Flight");
		fileNewFlightItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Predictor flightPredictor = Predictor.create();
				if(flightPredictor != null) flight.setPredictor(flightPredictor);
			}
		});
		fileMenu.add(fileNewFlightItem);
		
		JMenuItem fileLoadFlightItem = new JMenuItem("Load Flight");
		fileLoadFlightItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Predictor flightPredictor = Predictor.load();
				flight.setPredictor(flightPredictor);
			}
		});
		fileMenu.add(fileLoadFlightItem);
		
		JMenuItem fileSaveFlightItem = new JMenuItem("Save Flight");
		fileSaveFlightItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(flight.getPredictor() != null) flight.getPredictor().save();
			}
		});
		fileMenu.add(fileSaveFlightItem);
		
		fileMenu.add(new JSeparator());
		JMenuItem fileExitItem = new JMenuItem("Exit");
		fileExitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.setVisible(false);
				exit();
			}
		});
		fileMenu.add(fileExitItem);
		
		JMenuItem flightConfigItem = new JMenuItem("Configure Tracking");
		flightConfigItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TrackingConfigDialog config = new TrackingConfigDialog(trackingService);
				if(config.wasAccepted()) {
					trackingService.setPrimary(config.getPrimary());
					trackingService.setSecondary(config.getSecondary());
					trackingService.setRecovery(config.getRecovery());
				}
			}
		});
		flightMenu.add(flightConfigItem);
		
		flightTrackItem = new JCheckBoxMenuItem("Enable Tracking");
		flightTrackItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(flightTrackItem.getState() == true) {
					if(trackingService.getPrimary() == null) {
						JOptionPane.showMessageDialog(window, "Primary tracker is not configured.\nDisabling tracking service.", "Configuration Error", JOptionPane.ERROR_MESSAGE, null);
						flightTrackItem.setState(false);
						return;
					}
					if(flight.getPredictor() == null) {
						JOptionPane.showMessageDialog(window, "Flight is not defined, cannot run predictions.", "Undefined Flight", JOptionPane.WARNING_MESSAGE, null);
						flightTrackItem.setState(false);
						return;
					}
					Content mapContent = contentManager.getContent("Tracking Map");
					if(mapContent == null) {
						contentManager.removeAllContents();
						trackingMap = new MappingPanel();
						trackingMap.setCenter(
								flight.getPredictor().getStart().getLatitude(),
								flight.getPredictor().getStart().getLongitude());
						MapOverlay trackOverlay = new MapOverlay("Track");
						trackOverlay.addPath("Track", flight.getTrack());
						trackingMap.addOverlay(trackOverlay);
						MapOverlay predOverlay = new MapOverlay("Prediction");
						predOverlay.addPath("Prediction", flight.getLatestPrediction());
						trackingMap.addOverlay(predOverlay);
						contentManager.addContent("Tracking Map", "Tracking Map",
								null, trackingMap);
					}
				}
				tracking = flightTrackItem.getState();
				trackingService.setEnabled(tracking);
				if(tracking == true) {
					
				}
			}
		});
		flightMenu.add(flightTrackItem);
		
		flightMenu.add(new JSeparator());
		JMenuItem locationConfigItem = new JMenuItem("Configure Location");
		locationConfigItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocationConfigDialog config = new LocationConfigDialog(locationService);
				if(config.wasAccepted()) {
					// TODO Configure location
				}
			}
		});
		flightMenu.add(locationConfigItem);
		
		locationTrackItem = new JCheckBoxMenuItem("Enable GPS Location");
		locationTrackItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Enable location tracking handler
			}
		});
		flightMenu.add(locationTrackItem);
		
		JMenuItem helpHelpItem = new JMenuItem("Help Contents");
		helpHelpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HelpWindow.showHelp();
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
		
		trackingPanel = new TrackingPanel(locationService);
		trackingService.addListener(trackingPanel);
		trackingService.addListener(habtk);
		
		windowManager.registerToolWindow("Log", "Log", null, new LogPanel(), ToolWindowAnchor.BOTTOM);
		windowManager.registerToolWindow("Tracking", "Tracking", null, trackingPanel, ToolWindowAnchor.LEFT);
		windowManager.registerToolWindow("Prediction", null, null, new PredictionPanel(windowManager), ToolWindowAnchor.LEFT);
		for(ToolWindow win : windowManager.getToolWindows()) win.setAvailable(true);
		windowManager.getToolWindow("Log").setType(ToolWindowType.SLIDING);
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
			}
			
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					File workspace = new File("workspace.xml");
					if(workspace.exists()) {
						FileInputStream in = new FileInputStream(workspace);
						windowManager.getPersistenceDelegate().apply(in);
						in.close();
					}
				} catch(Exception e1) {
					debugLog.log(Level.SEVERE, "Exception", e);
				}
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		window.getContentPane().add(windowManager);
		window.pack();
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	
	/**
	 * Save state and exit
	 */
	private static void exit() {
		try {
			FileOutputStream out = new FileOutputStream("workspace.xml");
			windowManager.getPersistenceDelegate().save(out);
			out.close();
		} catch(Exception e1) {
			debugLog.log(Level.SEVERE, "Exception", e1);
		}
		window.dispose();
		System.exit(0);
	}

	/**
	 * Main method
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		if(args.length == 0) {
			DebugLog.setupLogger(9);
		} else {
			DebugLog.setupLogger(Integer.parseInt(args[0]));
		}
	
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		
		habtk = new HABtk();
		flight = new BalloonFlight();
		trackingService = new TrackingService();
		locationService = new LocationService();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					//try {
						//UIManager.setLookAndFeel(new SubstanceTwilightLookAndFeel());
						//UIManager.put(SubstanceLookAndFeel.WINDOW_ROUNDED_CORNERS, false);
					//} catch (UnsupportedLookAndFeelException e) {
						//e.printStackTrace();
					//}
					setup();
					
					window.setVisible(true);
					debugLog.log(Level.INFO, "Application Started");
				}
			});
		} catch(Exception e) {
			debugLog.log(Level.SEVERE, "Exception", e);
		}
	}

	@Override
	public void positionUpdateEvent(PositionEvent e) {
		switch(e.getSource()) {
			case PositionEvent.PRIMARY:
				MapPoint point = e.getPosition();
				flight.getTrack().add(point);
				
				boolean ascending = true;
				if(prevPoint != null) {
					if(point.getAltitude() < prevPoint.getAltitude()) {
						ascending = false;
						descendingPkts++;
						if(descendingPkts == 5) {
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									new MessageDialog(window, "Burst", "Balloon is descending.", 15000);
								}
							});
						}
					} else {
						descendingPkts = 0;
					}
				}
				prevPoint = point;
				
				Predictor pred = flight.getPredictor().clone();
				pred.setStart(e.getPosition());
				pred.setAscending(ascending);
				MapPath prediction = pred.runPrediction();
				
				flight.updatePrediction(prediction);
				MapOverlay predOverlay = new MapOverlay("Prediction");
				predOverlay.addPath("Prediction", prediction);
				trackingMap.addOverlay("Prediction", predOverlay);
				
				trackingPanel.positionUpdateEvent(
						new PositionEvent(PositionEvent.BURST, pred.getBurst()));
				trackingPanel.positionUpdateEvent(
						new PositionEvent(PositionEvent.LANDING, pred.getLanding()));
				break;
		}
	}

}
