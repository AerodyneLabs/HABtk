package com.aerodynelabs.habtk.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;

import org.noos.xing.mydoggy.ToolWindowManager;

import com.aerodynelabs.habtk.prediction.Predictor;
import com.aerodynelabs.map.MapPanel;
import com.aerodynelabs.map.MapPath;
import com.aerodynelabs.map.MapPoint;

@SuppressWarnings("serial")
public class PredictionPanel extends JPanel {
	
	private JTextField fFlight;
	private JTextField fStart;
	private JTextField fStop;
	private JSpinner fStep;
	private JSpinner fDays;
	private JButton cancel, run;
	private JProgressBar progress;
	
	private final ToolWindowManager twm;
	private Predictor baseFlight;
	
	private MapPanel map;
	private FlightListPanel list;
	
	private int nTasks = 0;
	private int cTasks = 0;
	
	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	public PredictionPanel(ToolWindowManager windowManager) {
		super();
		twm = windowManager;
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date now = new Date();
		
		JLabel lFlight = new JLabel("Flight:");
		fFlight = new JTextField();
		fFlight.setEditable(false);
		JButton bNew = new JButton("New");
		bNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Predictor pred = Predictor.create();
				if(pred != null) {
					baseFlight = pred; 
					fFlight.setText(baseFlight.toString());
				}
			}
		});
		JButton bLoad = new JButton("Open");
		bLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Predictor pred = Predictor.load();
				if(pred != null) {
					baseFlight = pred;
					fFlight.setText(baseFlight.toString());
				}
			}
		});
		JButton bEdit = new JButton("Edit");
		bEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(baseFlight == null) return;
				if(baseFlight.setup()) fFlight.setText(baseFlight.toString());
			}
		});
		layout.putConstraint(SpringLayout.NORTH, lFlight, 6, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, lFlight, 6, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, fFlight, 6, SpringLayout.SOUTH, lFlight);
		layout.putConstraint(SpringLayout.WEST, fFlight, 6, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, fFlight, -6, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, bNew, 6, SpringLayout.SOUTH, fFlight);
		layout.putConstraint(SpringLayout.NORTH, bLoad, 6, SpringLayout.SOUTH, fFlight);
		layout.putConstraint(SpringLayout.NORTH, bEdit, 6, SpringLayout.SOUTH, fFlight);
		layout.putConstraint(SpringLayout.WEST, bNew, 6, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, bLoad, 6, SpringLayout.EAST, bNew);
		layout.putConstraint(SpringLayout.WEST, bEdit, 6, SpringLayout.EAST, bLoad);
		layout.putConstraint(SpringLayout.EAST, bEdit, -6, SpringLayout.EAST, this);
		add(lFlight);
		add(fFlight);
		add(bNew);
		add(bLoad);
		add(bEdit);
		
		JLabel lStart = new JLabel("Start Time:");
		fStart = new JTextField(dateTimeFormat.format(now));
		JButton bStart = new JButton("Pick");
		layout.putConstraint(SpringLayout.NORTH, fStart, 6, SpringLayout.SOUTH, bNew);
		layout.putConstraint(SpringLayout.BASELINE, lStart, 0, SpringLayout.BASELINE, fStart);
		layout.putConstraint(SpringLayout.NORTH, bStart, 0, SpringLayout.NORTH, fStart);
		layout.putConstraint(SpringLayout.SOUTH, bStart, 0, SpringLayout.SOUTH, fStart);
		layout.putConstraint(SpringLayout.WEST, lStart, 6, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, fStart, 6, SpringLayout.EAST, lStart);
		layout.putConstraint(SpringLayout.WEST, bStart, 6, SpringLayout.EAST, fStart);
		layout.putConstraint(SpringLayout.EAST, this, 6, SpringLayout.EAST, bStart);
		add(lStart);
		add(fStart);
		add(bStart);
		
		JLabel lStop = new JLabel("Stop Time:");
		fStop = new JTextField(timeFormat.format(new Date(now.getTime() + 6*60*60*1000)));
		JButton bStop = new JButton("Pick");
		layout.putConstraint(SpringLayout.NORTH, fStop, 6, SpringLayout.SOUTH, fStart);
		layout.putConstraint(SpringLayout.BASELINE, lStop, 0, SpringLayout.BASELINE, fStop);
		layout.putConstraint(SpringLayout.NORTH, bStop, 0, SpringLayout.NORTH, fStop);
		layout.putConstraint(SpringLayout.SOUTH, bStop, 0, SpringLayout.SOUTH, fStop);
		layout.putConstraint(SpringLayout.WEST, lStop, 6, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, fStop, 6, SpringLayout.EAST, lStop);
		layout.putConstraint(SpringLayout.EAST, fStop, 0, SpringLayout.EAST, fStart);
		layout.putConstraint(SpringLayout.WEST, bStop, 6, SpringLayout.EAST, fStop);
		layout.putConstraint(SpringLayout.EAST, bStop, -6, SpringLayout.EAST, this);
		add(lStop);
		add(fStop);
		add(bStop);
		
		JLabel lStep = new JLabel("Interval (hr):");
		fStep = new JSpinner(new SpinnerNumberModel(3, 1, 24, 1));
		layout.putConstraint(SpringLayout.NORTH, fStep, 6, SpringLayout.SOUTH, fStop);
		layout.putConstraint(SpringLayout.BASELINE, lStep, 0, SpringLayout.BASELINE, fStep);
		layout.putConstraint(SpringLayout.WEST, lStep, 6, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, fStep, 6, SpringLayout.EAST, lStep);
		add(lStep);
		add(fStep);
		
		JLabel lDays = new JLabel("Days out:");
		fDays = new JSpinner(new SpinnerNumberModel(3, 1, 5, 1));
		layout.putConstraint(SpringLayout.NORTH, fDays, 6, SpringLayout.SOUTH, fStop);
		layout.putConstraint(SpringLayout.BASELINE, lDays, 0, SpringLayout.BASELINE, fDays);
		layout.putConstraint(SpringLayout.WEST, lDays, 6, SpringLayout.EAST, fStep);
		layout.putConstraint(SpringLayout.WEST, fDays, 6, SpringLayout.EAST, lDays);
		layout.putConstraint(SpringLayout.EAST, fDays, -6, SpringLayout.EAST, this);
		add(lDays);
		add(fDays);
		
		progress = new JProgressBar();
		progress.setStringPainted(true);
		progress.setMaximum(nTasks + 1);
		progress.setValue(cTasks + 1);
		layout.putConstraint(SpringLayout.NORTH, progress, 6, SpringLayout.SOUTH, fDays);
		layout.putConstraint(SpringLayout.WEST, progress, 6, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, progress, -6, SpringLayout.EAST, this);
		add(progress);
		
		cancel = new JButton("Cancel");
		cancel.setEnabled(false);
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO cancel predictions
				run.setEnabled(true);
				cancel.setEnabled(false);
			}
		});
		
		run = new JButton("Run");
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO run predictions
				cancel.setEnabled(true);
				run.setEnabled(false);
				if(map == null) {
					MapPoint startPoint = baseFlight.getStart();
					map = new MapPanel(startPoint.getLatitude(), startPoint.getLongitude(), 9);
					twm.getContentManager().addContent("Prediction Map", "Prediction Map", null, map, "Map Panel");
				}
				if(list == null) {
					list = new FlightListPanel(map);
					twm.getContentManager().addContent("Prediction List", "Prediction List", null, list, "Prediction List");
				}
				MapPath path = baseFlight.runPrediction();
				list.addFlight(baseFlight.clone(), path);
			}
		});
		layout.putConstraint(SpringLayout.NORTH, run, 6, SpringLayout.SOUTH, progress);
		layout.putConstraint(SpringLayout.NORTH, cancel, 0, SpringLayout.NORTH, run);
		layout.putConstraint(SpringLayout.EAST, run, -6, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.EAST, cancel, -6, SpringLayout.WEST, run);
		add(cancel);
		add(run);
	}

}
