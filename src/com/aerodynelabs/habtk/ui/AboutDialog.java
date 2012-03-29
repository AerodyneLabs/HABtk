package com.aerodynelabs.habtk.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog implements ActionListener {
	
	public AboutDialog(JFrame parent) {
		super(parent, "About", true);
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		
		JLabel title = new JLabel("HABtk");
		JLabel subtitle = new JLabel("High Altitude Ballooning toolkit");
		
		panel.add(title);
		panel.add(subtitle);
		
		JButton but = new JButton("Close");
		but.addActionListener(this);
		panel.add(but);
		
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}
	
}
