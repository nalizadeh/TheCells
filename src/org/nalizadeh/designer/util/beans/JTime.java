/*
 * Copyright 2007 N.A.J. nalizadeh.org - All rights reserved.
 * nalizadeh.org PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package org.nalizadeh.designer.util.beans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.MemoryImageSource;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2007 N.A.J</p>
 *
 * <p>Organisation:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class JTime extends JPanel {

	private static int m0 = 0xffd4d0c8; // gray-dialog
	private static int m1 = 0xffffffff; // white
	private static int m2 = 0xff000000; // black
	private static int m3 = 0xff28446b; // blue-x
	private static int m4 = 0xff7a7a7d; // shadow-1
	private static int m5 = 0xff686f74; // shadow-2
	private static int m6 = 0xffbaafa1; // shadow-3

	private static int m7 = 0xfff1efe9; // shadow-4
	private static int m8 = 0xffe8e4e0; // shadow-5
	private static int m9 = 0xffd6dad7; // shadow-6
	private static int ma = 0xffd6dace; // shadow-7
	private static int mb = 0xffcdcfce; // shadow-8

	private static int[] fpix =
		{
			m0, m0, m0, m0, m2, m2, m2, m2, m2, m0, m0, m0, m0, //
			m0, m0, m2, m2, m4, m1, m1, m1, m4, m2, m2, m0, m0, //
			m0, m2, m4, m1, m1, m1, m3, m1, m1, m1, m4, m2, m0, //
			m0, m2, m1, m1, m1, m1, m3, m1, m1, m7, m8, m2, m0, //
			m2, m4, m1, m1, m1, m1, m3, m1, m7, m9, m9, m5, m2, //
			m2, m1, m1, m1, m1, m1, m3, m8, m9, mb, mb, m8, m2, //
			m2, m1, m1, m1, m1, m1, m3, m9, mb, mb, mb, m9, m2, //
			m2, m1, m1, m1, m1, m3, m9, mb, mb, mb, mb, m9, m2, //
			m2, m4, m1, m1, m3, m9, mb, mb, mb, mb, m9, m5, m2, //
			m6, m2, m1, m7, m9, mb, mb, mb, mb, mb, m9, m2, m6, //
			m0, m2, m4, m8, ma, mb, mb, mb, m9, m9, m4, m2, m0, //
			m0, m6, m2, m2, m4, m9, m9, m9, m4, m2, m2, m6, m0, //
			m0, m0, m6, m6, m2, m2, m2, m2, m2, m6, m6, m0, m0, //
			m0, m0, m0, m0, m6, m6, m6, m6, m6, m0, m0, m0, m0, //
		};

	private static Image fimg =
		java.awt.Toolkit.getDefaultToolkit().createImage(
			new MemoryImageSource(13, 14, fpix, 0, 13)
		);

	private TimeBox ob;
	private JTextField tx;
	private JButton bt;

	private String value = "12:00";

	private Vector actionListeners = new Vector();

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public JTime() {
		this("12:00");
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public JTime(String value) {
		super(new BorderLayout(1, 0));

		ob = new TimeBox(this);

		tx = new JTextField();
		tx.setEditable(false);

		bt = new JButton(new ImageIcon(fimg));
		bt.setRequestFocusEnabled(false);
		bt.setPreferredSize(new Dimension(20, 20));
		bt.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					open();
				}
			}
		);

		add(tx, BorderLayout.CENTER);
		add(bt, BorderLayout.EAST);
		setPreferredSize(new Dimension(80, 21));

		setValue(value);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setValue(String value) {
		String v = ob.setValue(value);
		if (v != null) {
			this.value = v;
			tx.setText(v);
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setEditable(boolean b) {
		tx.setEditable(b);
		bt.setEnabled(b);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean isEditable() {
		return tx.isEditable();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setEnabled(boolean b) {
		tx.setEnabled(b);
		bt.setEnabled(b);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean isEnabled() {
		return tx.isEnabled();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setVisible(boolean b) {
		tx.setVisible(b);
		bt.setVisible(b);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean isVisible() {
		return tx.isVisible();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	private void open() {
		ob.setValue(value);
		ob.open(0, getHeight() + 2);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void close() {
		value = ob.getValue();
		tx.setText(value);

		// alle Listeners benachrichtigen
		for (int i = 0; i < actionListeners.size(); i++) {
			ActionListener al = (ActionListener) actionListeners.elementAt(i);
			al.actionPerformed(new ActionEvent(this, 0, null));
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void addActionListener(ActionListener al) {
		actionListeners.add(al);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void removeActionListener(ActionListener al) {
		actionListeners.remove(al);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		JFrame frame = new JFrame("JTime Example");
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);
		frame.getContentPane().setLayout(null);
		JTime ohr = new JTime();
		ohr.setValue("12:00"); //12:31");
		ohr.setBounds(40, 40, 75, 20);
		frame.getContentPane().add(ohr);
		frame.setSize(300, 200);
		frame.setVisible(true);
	}
}


/**
 * Class TimeBox
 *
 * @author:   Nader Alizadeh
 * @version:  1.0 14.03.01
 * @see
 */

class TimeBox extends JPopupMenu {

	private JSlider slider = null;
	private JLabel label = null;
	private JClock clock = null;
	private String value = "12:00";

	private JTime parent;

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public TimeBox(JTime parent) {
		super();
		this.parent = parent;

		label = new JLabel(value, JLabel.CENTER);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);

		slider = new JSlider(JSlider.VERTICAL, 0, 48, 24);
		slider.addChangeListener(new SliderListener());
		slider.setMinorTickSpacing(1);
		slider.setMajorTickSpacing(12);
		slider.setInverted(true);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		slider.addMouseListener(new SliderMouseListener());

		Font font = new Font("Dialog", 0, 10);
		JLabel l1 = new JLabel("0");
		JLabel l2 = new JLabel("6");
		JLabel l3 = new JLabel("12");
		JLabel l4 = new JLabel("18");
		JLabel l5 = new JLabel("24");
		l1.setFont(font);
		l2.setFont(font);
		l3.setFont(font);
		l4.setFont(font);
		l5.setFont(font);
		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(0), l1);
		labelTable.put(new Integer(12), l2);
		labelTable.put(new Integer(24), l3);
		labelTable.put(new Integer(36), l4);
		labelTable.put(new Integer(48), l5);
		slider.setLabelTable(labelTable);

		clock = new JClock();
		clock.setPreferredSize(new Dimension(50, 50));

		JPanel p1 = new JPanel(new BorderLayout());
		JPanel p2 = new JPanel(new BorderLayout());
		p1.add(clock, BorderLayout.NORTH);
		p1.add(label, BorderLayout.CENTER);
		p2.add(p1, BorderLayout.NORTH);
		p2.add(slider, BorderLayout.CENTER);
		p2.setPreferredSize(new Dimension(50, 230));
		add(p2);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public String setValue(String v) {
		try {
			if (v == null) {
				return null;
			}
			if (v.length() != 5) {
				return null;
			}
			if (v.indexOf(":") != 2) {
				return null;
			}
			int h = Integer.parseInt(v.substring(0, 2));
			int m = Integer.parseInt(v.substring(3, 5));
			label.setText(v);
			clock.setValue(0, m == 0 ? 0 : 30, h);
			slider.setValue(h * 2 + (m >= 30 ? 1 : 0));
			repaint();
			return value;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void open(int x, int y) {
		show(parent, x, y);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void close() {
		setVisible(false);
		parent.close();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	/**
	 * Listens to the slider.
	 */
	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			int i = (int) source.getValue();
			int h = i / 2;
			int m = i % 2;
			value = (h < 10 ? "0" + h : "" + h) + ":" + (m == 0 ? "00" : "30");
			label.setText(value);
			clock.setValue(0, m == 0 ? 0 : 30, h);
			clock.repaint();
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	class SliderMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				close();
			}
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
}


class JClock extends JPanel implements Runnable {

	// dieses Applet erzeugt einen Thread,
	// der die ganze Arbeit macht
	Thread timer = null;

	// Konstante
	final double pi = 3.14159;

	long speed = 70;

	public JClock() {
	}

	// Diese Methode wird beim repaint aufgerufen
	public void paint(Graphics g) {
		update2(g);
	}

	private void ticks(Graphics g) {
		int i, x0, x1, y0, y1;
		double angle, factor = 0.95;
		double radius;

		if (getSize().width > getSize().height) {
			radius = ((double) getSize().height - (double) getSize().height * 0.1) / 2.0;
		} else {
			radius = ((double) getSize().width - (double) getSize().width * 0.1) / 2.0;
		}

		g.setColor(Color.darkGray);

		// Minutenstriche malen
		for (i = 0; i < 60; i++) {
			angle = pi / 2.0 - i * pi / 30.0;

			if (i % 5 == 0) {
				x0 = (int) (Math.cos(angle) * radius * factor) + getSize().width / 2;
				y0 = (int) (Math.sin(angle) * radius * factor) + getSize().height / 2;
				x1 = (int) (Math.cos(angle) * radius * (2.0 - factor)) + getSize().width / 2;
				y1 = (int) (Math.sin(angle) * radius * (2.0 - factor)) + getSize().height / 2;
				g.drawLine(x0, y0, x1, y1);
			} else {
				x0 = (int) (Math.cos(angle) * radius) + getSize().width / 2;
				y0 = (int) (Math.sin(angle) * radius) + getSize().height / 2;
				g.drawLine(x0, y0, x0, y0);
			}
		}
	}

	// malt die Zeiger der Uhr
	private void arrows(Graphics g, int sec, int min, int hour) {
		double radius, hour_angle, min_angle, sec_angle;
		int hour_x, hour_y, min_x, min_y, sec_x, sec_y;

		if (getSize().width > getSize().height) {
			radius = ((double) getSize().height - (double) getSize().height * 0.1) / 2.0;
		} else {
			radius = ((double) getSize().width - (double) getSize().width * 0.1) / 2.0;
		}

		hour_angle = pi / 2.0 - (hour % 12) * pi / 6.0 - min * pi / 360.0;
		min_angle = pi / 2.0 - min * pi / 30.0;
		sec_angle = pi / 2.0 - sec * pi / 30.0;

		hour_x = (int) (Math.cos(hour_angle) * radius * 0.65) + getSize().width / 2;
		hour_y = -(int) (Math.sin(hour_angle) * radius * 0.65) + getSize().height / 2;

		min_x = (int) (Math.cos(min_angle) * radius * 0.8) + getSize().width / 2;
		min_y = -(int) (Math.sin(min_angle) * radius * 0.8) + getSize().height / 2;

		sec_x = (int) (Math.cos(sec_angle) * radius * 0.85) + getSize().width / 2;
		sec_y = -(int) (Math.sin(sec_angle) * radius * 0.85) + getSize().height / 2;

		g.drawLine(getSize().width / 2, getSize().height / 2, hour_x, hour_y);

		//g.drawLine(getSize().width/2-1, getSize().height/2, hour_x, hour_y);
		//g.drawLine(getSize().width/2, getSize().height/2+1, hour_x, hour_y+1);
		//g.drawLine(getSize().width/2-1, getSize().height/2, min_x, min_y);
		//g.drawLine(getSize().width/2, getSize().height/2+1, min_x, min_y+1);
		//g.drawLine(getSize().width/2, getSize().height/2, sec_x, sec_y);
	}

	// Wird von 'paint' aufgerufen. Hier findet das Zeichnen
	// der Uhr statt
	public void update(Graphics g) {
		Dimension d = getSize();
		double radius;
		double factor = 0.9;
		int sec, min, hour;

		// Uhrzeit neu holen. (deprecated seit JDK 1.1.5,
		// wird heute anders gemacht)
		Calendar dat = new java.util.GregorianCalendar();

		sec = (int) dat.get(Calendar.SECOND);
		min = (int) dat.get(Calendar.MINUTE);
		hour = (int) dat.get(Calendar.HOUR);

		g.setColor(getBackground());
		g.fillRect(0, 0, d.width, d.height);

		// Umrandung und Zeiger neu zeichen
		ticks(g);
		arrows(g, sec, min, hour % 12);
	}

	// Wird von 'paint' aufgerufen. Hier findet das Zeichnen
	// der Uhr statt
	int sec, min, hour;

	public void setValue(int s, int m, int h) {
		sec = s;
		min = m;
		hour = h;
		;
	}

	private void update2(Graphics g) {
		Dimension d = getSize();
		double radius;
		double factor = 0.9;

		g.setColor(getBackground());
		g.fillRect(0, 0, d.width, d.height);

		// Umrandung und Zeiger neu zeichen
		ticks(g);
		arrows(g, sec, min, hour % 12);
	}

	// die Startmethode.
	public void start() {
		if (timer == null) {

			// erzeugt neuen Thread dieses Prozesses mit
			// dem Ziel 'this', d.h. der Thread lauft in der
			// Laufzeitumgebung dieses Prozesses.
			timer = new Thread(this);

			// Startmethode aufrufen und damit Applet starten
			timer.start();
		}
	}

	// Applet anhalten
	public void stop() {
		timer = null;
	}

	// Diese Methode muss fuer Schnittstelle
	// 'runnable' implemntiert sein.
	public void run() {
		long startTime = System.currentTimeMillis();

		while (timer != null) {
			try {
				startTime += speed;
				repaint();

				// Eine kurze Zeit warten
				Thread.sleep(Math.max(0, startTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
			}
		}
		timer = null;
	}
}
