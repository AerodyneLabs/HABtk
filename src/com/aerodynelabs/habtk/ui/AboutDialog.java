package com.aerodynelabs.habtk.ui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * A window to provide attribution and other information.
 * @author Ethan Harstad
 *
 */
@SuppressWarnings("serial")
public class AboutDialog extends JDialog implements ActionListener, HyperlinkListener {
	
	public AboutDialog(JFrame parent) {
		super(parent, "About", true);
		
		String aboutInfo =
			"<html><body>" +
			"<big>HAB-tk</big><br /><b>High Altitude Ballooning toolkit</b><br />" +
			"<a href = \"http://www.aerodynelabs.com\">AeroDyne Laboratories</a><hr />" +
			"" +
			"Attribution:<br />" +
			"Map data &copy; <a href=\"http://www.openstreetmap.org/\">OpenStreetMap</a> contributors, <a href=\"http://creativecommons.org/licenses/by-sa/2.0/\">CC-BY-SA</a><br />" +
			"Directions, Elevation, Guidance, and Tiles Courtesy of <a href=\"http://www.mapquest.com/\">MapQuest</a> <img src=\"http://developer.mapquest.com/content/osm/mq_logo.png\"><br />" +
			"Docking framework by <a href=\"http://mydoggy.sourceforge.net/\">MyDoggy</a><br />" +
			"Look and feel by <a href=\"https://github.com/kirillcool/substance\">Substance</a>/<a href=\"http://insubstantial.posterous.com/\">Insubstantial</a><br />" +
			"Charts by <a href=\"http://www.jfree.org/jfreechart/\">JFreechart</a><br />" +
			"<hr />Please e-mail bug reports and feature requests to <a href=\"mailto:support@aerodynelabs.com\">support@aerodynelabs.com</a>." +
			"</body></html>";
		
		JEditorPane aboutPane = new JEditorPane("text/html", aboutInfo);
		aboutPane.setEditable(false);
		aboutPane.addHyperlinkListener(this);
		aboutPane.setBorder(new EmptyBorder(0, 10, 0, 10));
		getContentPane().add(aboutPane);
		
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			System.out.println("EVENT: " + e.getURL());
			try {
				Desktop.getDesktop().browse(e.getURL().toURI());
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
}
