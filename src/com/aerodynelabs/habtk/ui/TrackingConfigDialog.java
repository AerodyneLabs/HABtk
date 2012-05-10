package com.aerodynelabs.habtk.ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.aerodynelabs.habtk.tracking.Tracker;
import com.aerodynelabs.habtk.tracking.TrackingService;

@SuppressWarnings("serial")
public class TrackingConfigDialog extends JDialog {
	
	private boolean accepted = false;
	
	private JTextField fPrimary;
	private JTextField fSecondary;
	private JTextField fRecovery;
	
	private Tracker primary;
	private Tracker secondary;
	private Tracker recovery;
	
	public TrackingConfigDialog(TrackingService tracker) {
		setTitle("Setup Flight");
		setModal(true);
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		Container pane = getContentPane();
		
		primary = tracker.getPrimary();
		secondary = tracker.getSecondary();
		recovery = tracker.getRecovery();
		
		JLabel lPrimary = new JLabel("Primary Tracking");
		layout.putConstraint(SpringLayout.NORTH, lPrimary, 6, SpringLayout.NORTH, pane);
		layout.putConstraint(SpringLayout.WEST, lPrimary, 6, SpringLayout.WEST, pane);
		add(lPrimary);
		JSeparator sPrimary = new JSeparator();
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, sPrimary, 0, SpringLayout.VERTICAL_CENTER, lPrimary);
		layout.putConstraint(SpringLayout.WEST, sPrimary, 6, SpringLayout.EAST, lPrimary);
		layout.putConstraint(SpringLayout.EAST, sPrimary, -6, SpringLayout.EAST, pane);
		add(sPrimary);
		fPrimary = new JTextField(30);
		fPrimary.setEditable(false);
		if(primary != null) fPrimary.setText(primary.toString());
		JButton bPrimary = new JButton("Configure");
		bPrimary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tracker t = Tracker.create();
				if(t != null) {
					primary = t;
					fPrimary.setText(t.toString());
				}
			}
		});
		layout.putConstraint(SpringLayout.NORTH, fPrimary, 6, SpringLayout.SOUTH, lPrimary);
		layout.putConstraint(SpringLayout.NORTH, bPrimary, 0, SpringLayout.NORTH, fPrimary);
		layout.putConstraint(SpringLayout.SOUTH, bPrimary, 0, SpringLayout.SOUTH, fPrimary);
		layout.putConstraint(SpringLayout.WEST, fPrimary, 6, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.WEST, bPrimary, 6, SpringLayout.EAST, fPrimary);
		layout.putConstraint(SpringLayout.EAST, pane, 6, SpringLayout.EAST, bPrimary);
		add(fPrimary);
		add(bPrimary);
		
		JLabel lSecondary = new JLabel("Secondary Tracking");
		layout.putConstraint(SpringLayout.NORTH, lSecondary, 6, SpringLayout.SOUTH, fPrimary);
		layout.putConstraint(SpringLayout.WEST, lSecondary, 6, SpringLayout.WEST, pane);
		add(lSecondary);
		JSeparator sSecondary = new JSeparator();
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, sSecondary, 0, SpringLayout.VERTICAL_CENTER, lSecondary);
		layout.putConstraint(SpringLayout.WEST, sSecondary, 6, SpringLayout.EAST, lSecondary);
		layout.putConstraint(SpringLayout.EAST, sSecondary, -6, SpringLayout.EAST, pane);
		add(sSecondary);
		fSecondary = new JTextField(30);
		fSecondary.setEditable(false);
		if(secondary != null) fSecondary.setText(secondary.toString());
		JButton bSecondary = new JButton("Configure");
		bSecondary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tracker t = Tracker.create();
				if(t != null) {
					secondary = t;
					fSecondary.setText(t.toString());
				}
			}
		});
		layout.putConstraint(SpringLayout.NORTH, fSecondary, 6, SpringLayout.SOUTH, lSecondary);
		layout.putConstraint(SpringLayout.NORTH, bSecondary, 0, SpringLayout.NORTH, fSecondary);
		layout.putConstraint(SpringLayout.SOUTH, bSecondary, 0, SpringLayout.SOUTH, fSecondary);
		layout.putConstraint(SpringLayout.EAST, fSecondary, 0, SpringLayout.EAST, fPrimary);
		layout.putConstraint(SpringLayout.WEST, fSecondary, 6, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.WEST, bSecondary, 6, SpringLayout.EAST, fSecondary);
		layout.putConstraint(SpringLayout.EAST, bSecondary, -6, SpringLayout.EAST, pane);
		add(fSecondary);
		add(bSecondary);
		
		JLabel lRecovery = new JLabel("Recovery Tracking");
		layout.putConstraint(SpringLayout.NORTH, lRecovery, 6, SpringLayout.SOUTH, fSecondary);
		layout.putConstraint(SpringLayout.WEST, lRecovery, 6, SpringLayout.WEST, pane);
		add(lRecovery);
		JSeparator sRecovery = new JSeparator();
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, sRecovery, 0, SpringLayout.VERTICAL_CENTER, lRecovery);
		layout.putConstraint(SpringLayout.WEST, sRecovery, 6, SpringLayout.EAST, lRecovery);
		layout.putConstraint(SpringLayout.EAST, sRecovery, -6, SpringLayout.EAST, pane);
		add(sRecovery);
		fRecovery = new JTextField(30);
		fRecovery.setEditable(false);
		if(recovery != null) fRecovery.setText(recovery.toString());
		JButton bRecovery = new JButton("Configure");
		bRecovery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Tracker t = Tracker.create();
				if(t != null) {
					recovery = t;
					fRecovery.setText(t.toString());
				}
			}
		});
		layout.putConstraint(SpringLayout.NORTH, fRecovery, 6, SpringLayout.SOUTH, lRecovery);
		layout.putConstraint(SpringLayout.NORTH, bRecovery, 0, SpringLayout.NORTH, fRecovery);
		layout.putConstraint(SpringLayout.SOUTH, bRecovery, 0, SpringLayout.SOUTH, fRecovery);
		layout.putConstraint(SpringLayout.EAST, fRecovery, 0, SpringLayout.EAST, fPrimary);
		layout.putConstraint(SpringLayout.WEST, fRecovery, 6, SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.WEST, bRecovery, 6, SpringLayout.EAST, fRecovery);
		layout.putConstraint(SpringLayout.EAST, bRecovery, -6, SpringLayout.EAST, pane);
		add(fRecovery);
		add(bRecovery);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accepted = false;
				dispose();
			}
		});
		JButton ok = new JButton("Accept");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// TODO
					accepted = true;
				} catch(Exception e1) {
					e1.printStackTrace();
					accepted = false;
				}
				dispose();
			}
		});
		layout.putConstraint(SpringLayout.NORTH, ok, 6, SpringLayout.SOUTH, fRecovery);
		layout.putConstraint(SpringLayout.SOUTH, pane, 6, SpringLayout.SOUTH, ok);
		layout.putConstraint(SpringLayout.NORTH, cancel, 0, SpringLayout.NORTH, ok);
		layout.putConstraint(SpringLayout.EAST, ok, -6, SpringLayout.EAST, pane);
		layout.putConstraint(SpringLayout.EAST, cancel, -6, SpringLayout.WEST, ok);
		add(cancel);
		add(ok);
		
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
		setVisible(true);
	}
	
	public boolean wasAccepted() {
		return accepted;
	}
	
	public Tracker getPrimary() {
		return primary;
	}
	
	public Tracker getSecondary() {
		return secondary;
	}
	
	public Tracker getRecovery() {
		return recovery;
	}

}
