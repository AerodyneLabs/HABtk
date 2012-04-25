package com.aerodynelabs.habtk.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import org.jfree.ui.DateChooserPanel;

@SuppressWarnings("serial")
public class DateTimePicker extends JDialog {
	
	public static final int DATE = 0;
	public static final int TIME = 1;
	public static final int DATETIME = 2;
	
	private boolean accepted = false;
	private Calendar value;
	
	private JSpinner hour, minute;
//	private JSpinner second;
	private DateChooserPanel calendarWidget;
	
	public DateTimePicker(int type) {
		super();
		if(type == DATE) {
			setTitle("Choose Date");
		} else if(type == TIME) {
			setTitle("Choose Time");
		} else {
			setTitle("Choose Date and Time");
		}
		setModal(true);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		getContentPane().add(panel);
		
		value = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
		
		// Date selection fields
		JPanel datePanel = new JPanel();
		calendarWidget = new DateChooserPanel();
		calendarWidget.setDate(value.getTime());
		calendarWidget.setChosenDateButtonColor(Color.BLUE);
		datePanel.add(calendarWidget);
		if(type != TIME) panel.add(datePanel);
		
		// Time selection fields
		JPanel timePanel = new JPanel();
		setLayout(new FlowLayout(FlowLayout.CENTER));
		timePanel.add(new JLabel("Hour:"));
		SpinnerModel hourModel = new SpinnerNumberModel(value.get(Calendar.HOUR_OF_DAY), 0, 23, 1);
		hour = new JSpinner(hourModel);
		timePanel.add(hour);
		timePanel.add(new JLabel("Minute:"));
		SpinnerModel minuteModel = new SpinnerNumberModel((value.get(Calendar.MINUTE)/15)*15, 0, 60, 15);
		minute = new JSpinner(minuteModel);
		timePanel.add(minute);
//		timePanel.add(new JLabel("Second:"));
//		SpinnerModel secondModel = new SpinnerNumberModel(0, 0, 60, 5);
//		second = new JSpinner(secondModel);
//		timePanel.add(second);
		if(type != DATE) panel.add(timePanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accepted = false;
				dispose();
			}
		});
		buttonPanel.add(cancel);
		JButton ok = new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				accepted = true;
				Date date = calendarWidget.getDate();
				value.setTime(date);
				value.set(Calendar.HOUR_OF_DAY, ((Number)hour.getValue()).intValue());
				value.set(Calendar.MINUTE, ((Number)minute.getValue()).intValue());
				//value.set(Calendar.SECOND, ((Number)second.getValue()).intValue());
				dispose();
			}
		});
		buttonPanel.add(ok);
		panel.add(buttonPanel);
		
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - getWidth()) / 2, (screen.height - getHeight()) / 2);
		setVisible(true);
	}
	
	public boolean wasAccepted() {
		return accepted;
	}
	
	public Date getValue() {
		return value.getTime();
	}

}
