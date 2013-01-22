package com.aerodynelabs.habtk.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * A non-modal dialog that displays a message with a timeout.
 * @author Ethan Harstad
 *
 */
@SuppressWarnings("serial")
public class MessageDialog extends JDialog implements ActionListener {
	
	private Timer timer;
	
	public MessageDialog(Frame parent, String title, String message) {
		this(parent, title, message, 0);
	}
	
	public MessageDialog(Frame parent, String title, String message, int timeout) {
		super(parent, false);
		setTitle(title);
		
		if(timeout > 0) {
			timer = new Timer(timeout, this);
			timer.start();
		}
		
		final JOptionPane pane = new JOptionPane(message,
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.OK_OPTION);
		pane.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();
				if(isVisible() && e.getSource() == pane && (prop.equals(JOptionPane.VALUE_PROPERTY))) {
					setVisible(false);
				}
			}
		});
		
		setContentPane(pane);
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.stop();
		dispose();
	}

}
