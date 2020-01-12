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

package org.nalizadeh.designer.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

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
public class CellsOutlookBar extends JPanel implements MouseListener, MouseMotionListener,
	Animation.AnimationListener
{

	private static final ImageIcon icon1 = CellsUtil.getImage("designerview.gif");

	private static final ImageIcon iconExpanded = CellsUtil.getImage("olexp.gif");
	private static final ImageIcon iconCollapsed = CellsUtil.getImage("olcol.gif");
	private static final ImageIcon iconExpandedRo = CellsUtil.getImage("olexpro.gif");
	private static final ImageIcon iconCollapsedRo = CellsUtil.getImage("olcolro.gif");

	private static final Font font = new Font("Dialog", 0, 11);

	private static final Color backColor = new Color(193, 214, 230);
	private static final Color buttForeColor = Color.BLACK;
	private static final Color buttDisabledForeColor = backColor;
	private static final Color buttRolloverForeColor = Color.BLUE;
	private static final Color buttRolloverBackColor1 = UIManager.getColor("window");
	private static final Color buttRolloverBackColor2 = new Color(193, 214, 230);

	private ArrayList<OutlookBarItem> items = new ArrayList<OutlookBarItem>();
	private boolean multipleExpandable = true;

	private Animation animation = null;
	private Component animationComp = null;
	private boolean animationExpanding;

	public CellsOutlookBar() {
		super();
		setLayout(null);
		setDoubleBuffered(true);
		setBorder(BorderFactory.createEtchedBorder());
		setBackground(backColor);

		animation = new Animation(Animation.ANIMATE_VERTIKAL);
		animation.addAnimationListener(this);
	}

	public void addItem(String name, Icon icon, Component comp) {
		if (getItem(name) == null) {
			OutlookBarButton b = new OutlookBarButton(name, icon);
			b.addMouseListener(this);
			b.addMouseMotionListener(this);
			items.add(new OutlookBarItem(b, comp));
		}
	}

	private OutlookBarItem getItem(String name) {
		for (OutlookBarItem item : items) {
			if (item.button.text.equals(name)) {
				return item;
			}
		}
		return null;
	}

	public void setEnabled(boolean b) {
		super.setEnabled(b);
		for (OutlookBarItem item : items) {
			item.button.enabled = b;
			item.component.setEnabled(b);
		}
	}

	public void setItemEnabled(String name, boolean b) {
		OutlookBarItem item = getItem(name);
		if (item != null) {
			item.button.enabled = b;
		}
	}

	public void setMultipleExpandable(boolean b) {
		multipleExpandable = b;
	}

	public void expand(String name) {
		OutlookBarItem item = getItem(name);
		if (item != null && !item.button.expanded) {
			item.button.expanded = true;
			revalidate();
		}
	}

	public void collapse(String name) {
		OutlookBarItem item = getItem(name);
		if (item != null && item.button.expanded) {
			item.button.expanded = false;
			revalidate();
		}
	}

	public void expandAll() {
		for (OutlookBarItem item : items) {
			item.button.expanded = true;
		}
		revalidate();
	}

	public void collapseAll() {
		for (OutlookBarItem item : items) {
			item.button.expanded = false;
		}
		revalidate();
	}

	public void doLayout() {

		removeAll();

		int hg = 20;

		Insets is = getInsets();

		int w = getWidth() - (is.left + is.right);
		int h = getHeight() - (is.top + is.bottom);
		int y = is.top;
		int x = is.left;
		int n = 0;

		for (OutlookBarItem item : items) {
			if (item.button.expanded) {
				n++;
			}
		}

		if (animationComp == null) {
			int hx = n > 0 ? (h - (items.size() * (hg + 1))) / n : 0;

			for (OutlookBarItem item : items) {
				item.button.setBounds(x, y, w, hg);
				add(item.button);
				y += (hg + 1);
				if (item.button.expanded) {
					item.component.setBounds(x, y, w, hx);
					add(item.component);
					((JComponent) item.component).revalidate();
					y += hx + 1;
				}
			}
		} else {

			if (!animationExpanding) {
				n++;
			}

			if (!animation.isStarted()) {
				int hx = n > 0 ? (h - (items.size() * (hg + 1))) / n : 0;
				animation.start(animationExpanding, w, hx);

			} else if (animation.isCompleted()) {
				animationComp = null;
				doLayout();

			} else {
				n--;
				int rest = h - (items.size() * (hg + 1));
				rest -= animationComp.getHeight();
				int hx = n > 0 ? rest / n : 0;

				for (OutlookBarItem item : items) {
					item.button.setBounds(x, y, w, hg);
					add(item.button);
					y += (hg + 1);
					if (item.button.expanded) {
						if (animationComp.equals(item.component)) {
							animationComp.setBounds(x, y, w, animation.getValueY());
							add(animationComp);
							((JComponent) animationComp).revalidate();
							y += animationComp.getHeight() + 1;
						} else {
							item.component.setBounds(x, y, w, hx);
							add(item.component);
							((JComponent) item.component).revalidate();
							y += hx + 1;
						}

					} else if (animationComp.equals(item.component)) {
						animationComp.setBounds(x, y, w, animation.getValueY());
						add(animationComp);
						((JComponent) animationComp).revalidate();
						y += animationComp.getHeight() + 1;
					}
				}
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		OutlookBarButton ob = (OutlookBarButton)e.getComponent();
		ob.rollover = ob.enabled;
		repaint();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		for (OutlookBarItem item : items) {
			if (item.button == e.getComponent()) {
				if (item.button.enabled) {
					item.button.expanded = !item.button.expanded;
					animationComp = item.component;
					animationExpanding = item.button.expanded;
				}
				if (multipleExpandable) {
					break;
				}
			} else if (!multipleExpandable) {
				item.button.expanded = false;
			}
		}

		revalidate();
		repaint();
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		OutlookBarButton ob = (OutlookBarButton)e.getComponent();
		ob.rollover = false;
		repaint();
	}

	public void animated(Object src) {
		invalidate();
		doLayout();
		repaint();
	}

	class OutlookBarItem {
		private OutlookBarButton button;
		private Component component;

		public OutlookBarItem(OutlookBarButton b, Component c) {
			button = b;
			component = c;
		}
	}

	class OutlookBarButton extends JPanel {

		private String text;
		private Icon icon;
		private boolean enabled;
		private boolean expanded;
		private boolean rollover;

		public OutlookBarButton(String text, Icon icon) {
			this.text = text;
			this.icon = icon;
			this.enabled = true;
			this.expanded = false;
			this.rollover = false;
		}

		public void paint(Graphics g) {
			super.paint(g);

			Rectangle r = getBounds();

			Graphics2D g2D = (Graphics2D) g;
			g2D.setPaint(
				new GradientPaint(
					0,
					0,
					rollover ? buttRolloverBackColor2 : buttRolloverBackColor1,
					r.width + 20,
					r.height + 20,
					rollover ? buttRolloverBackColor1 : buttRolloverBackColor2,
					true
				)
			);

			g2D.fillRect(0, 0, r.width, r.height);

			g.setColor(rollover ? Color.DARK_GRAY : Color.WHITE);
			g.drawLine(0, 0, r.width - 1, 0);
			g.drawLine(0, 0, 0, r.height - 1);

			g.setColor(rollover ? Color.WHITE : Color.DARK_GRAY);
			g.drawLine(0, 0 + r.height - 1, r.width, r.height - 1);
			g.drawLine(0 + r.width - 1, 0, r.width - 1, r.height - 1);

			g.setFont(font);

			g.setColor(
				enabled ? (rollover ? buttRolloverForeColor : buttForeColor)
				: buttDisabledForeColor
			);
			g.drawString(text, 35, getHeight() / 2 + 4);

			if (icon != null) {
				icon.paintIcon(this, g, 10, getHeight() / 2 - icon.getIconHeight() / 2);
			}

			Icon ic =
				expanded ? (rollover ? iconCollapsedRo : iconCollapsed)
				: (rollover ? iconExpandedRo : iconExpanded);

			ic.paintIcon(this, g, getWidth() - 20, getHeight() / 2 - 3);
		}
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("CellsDesigner");

		CellsOutlookBar ob = new CellsOutlookBar();
		ob.setPreferredSize(new Dimension(180, 100));
		JPanel p1 = new JPanel();
		p1.setBackground(Color.GREEN);
		JPanel p2 = new JPanel();
		p2.setBackground(Color.CYAN);
		JPanel p3 = new JPanel();
		p3.setBackground(Color.ORANGE);
		JPanel p4 = new JPanel();
		p4.setBackground(Color.BLUE);

		ob.addItem("Folders", icon1, p1);
		ob.addItem("System Settings", icon1, p2);
		ob.addItem("Application Rolls", icon1, p3);
		ob.addItem("Look & Feel", icon1, p4);

		ob.setMultipleExpandable(true);
		ob.setItemEnabled("Application Rolls", false);

		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(ob, BorderLayout.WEST);
		frame.getContentPane().add(new JPanel(), BorderLayout.CENTER);
		frame.setSize(200, 400);
		frame.setVisible(true);
	}

}

class Animation implements ActionListener {

	public static final int ANIMATE_VERTIKAL = 1;
	public static final int ANIMATE_HORIZONTAL = 2;

	private final int step = 10;

	private Timer animatorTimer;

	private boolean started = false;
	private boolean completed = false;

	private int mode = ANIMATE_VERTIKAL;

	private int maxWidth, mw;
	private int maxHeight, mh;
	private boolean expanding = false;

	private ArrayList<AnimationListener> listeners = new ArrayList<AnimationListener>();

	public Animation(int mode) {
		this.mode = mode;
	}

	public void actionPerformed(ActionEvent e) {

		if (expanding) {
			mw += step;
			mh += step;
			if (mw > maxWidth) {
				mw = maxWidth;
			}
			if (mh > maxHeight) {
				mh = maxHeight;
			}
			if (mw == maxWidth && mh == maxHeight) {
				completed = true;
			}
		} else {
			mw -= step;
			mh -= step;
			if (mw < 0) {
				mw = 0;
			}
			if (mh < 0) {
				mh = 0;
			}
			if (mw == 0 && mh == 0) {
				completed = true;
			}
		}

		for (AnimationListener listener : listeners) {
			listener.animated(this);
		}

		if (completed) {
			stop();
		}
	}

	public void start(boolean expanding, int maxWidth, int maxHeight) {

		if (!isStarted()) {

			this.expanding = expanding;
			this.maxWidth = maxWidth;
			this.maxHeight = maxHeight;

			started = true;
			completed = false;

			mw = mode == ANIMATE_VERTIKAL ? maxWidth : expanding ? 0 : maxWidth;
			mh = mode == ANIMATE_VERTIKAL ? expanding ? 0 : maxHeight : maxHeight;

			animatorTimer = new Timer(10, this);
			animatorTimer.start();
		}
	}

	public void stop() {
		animatorTimer.stop();
		started = false;
	}

	public boolean isExpanding() {
		return expanding;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isCompleted() {
		return completed;
	}

	public int getValueX() {
		return mw;
	}

	public int getValueY() {
		return mh;
	}

	public void addAnimationListener(AnimationListener listener) {
		listeners.add(listener);
	}

	public void removeAnimationListener(AnimationListener listener) {
		listeners.remove(listener);
	}

	public static interface AnimationListener {
		public void animated(Object src);
	}
}
