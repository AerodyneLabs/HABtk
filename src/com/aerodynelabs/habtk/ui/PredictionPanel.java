package com.aerodynelabs.habtk.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class PredictionPanel extends JPanel {
	
	
	
	public PredictionPanel() {
		super();
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		JButton run = new JButton("Run");
		run.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO run predictions
			}
		});
		layout.putConstraint(SpringLayout.SOUTH, this, 6, SpringLayout.SOUTH, run);
		layout.putConstraint(SpringLayout.EAST, run, -6, SpringLayout.EAST, this);
		add(run);
	}

}
