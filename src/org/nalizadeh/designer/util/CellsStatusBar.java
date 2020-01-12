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

import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
public class CellsStatusBar extends JPanel {

	private CellsDesigner designer;

	private MenuPanel menuPanel;
	private MemoryReporter memReporter;
	private CellsAnimatedGif gif;
	private JProgressBar ps;
	private int steps;
	private int counter;

	private static final Color lnColor = new Color(160, 159, 149);
	private static final Color bkcolor = new Color(212, 208, 200);

	private static final ImageIcon img0 = CellsUtil.getImage("Status0");
	private static final ImageIcon img1 = CellsUtil.getImage("Status1");
	private static final ImageIcon img2 = CellsUtil.getImage("Status2");
	private static final ImageIcon img3 = CellsUtil.getImage("Status3");
	private static final ImageIcon img4 = CellsUtil.getImage("Status4");
	private static final ImageIcon img5 = CellsUtil.getImage("Status5");

	public CellsStatusBar(CellsDesigner designer) {
		super(
			new CellsLayout(
				new double[][] {
					{ 22 },
					{ 5, CellsLayout.PREFERRED, 5, CellsLayout.PREFERRED, CellsLayout.FILL }
				}
			)
		);

		this.designer = designer;

		memReporter = new MemoryReporter(this);
		
		gif = new CellsAnimatedGif(CellsUtil.getImage("Indicator1"));

		ps = new JProgressBar();
		ps.setPreferredSize(new Dimension(200, 16));
		ps.setVisible(false);
		ps.setIndeterminate(false);
		ps.setStringPainted(true);
		menuPanel = new MenuPanel();

		setBorder(null);
		setBackground(bkcolor);

		add(gif, "0,1,1,0,0,2,0");
		add(ps, "0,3,1,0,0,2,0");
		add(menuPanel, "0,4,0,0,4,0");

		addItem(new CellsStatusBar.StatusBarItem(100, memReporter.getMenu()));
		addItem(new CellsStatusBar.StatusBarItem("The Cells version " + designer.getVersion()));
		addItem(new CellsStatusBar.StatusBarItem(""));
		addItem(new CellsStatusBar.StatusBarItem("Views", designer.getDockingPopup()));
	}

	public void startMemoryReporting() {
		memReporter.start();
	}

	public void stopMemoryReporting() {
		memReporter.stop();
	}

	public void updateMemoryReporting() {
		memReporter.update();
	}

	public void startBusyLine(int steps) {
		this.steps = steps;
		this.counter = 0;

		ps.setMinimum(0);
		ps.setMaximum(steps);
		ps.setValue(0);
		ps.setVisible(true);

		gif.start();
	}

	public void stopBusyLine() {
		ps.setValue(steps);
		ps.setVisible(false);
		gif.stop();
	}

	public void updateBusyLine() {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					int s = (steps - counter) / 2;
					ps.setString(s + " secounds remaining");
					ps.setValue(counter++);
				}
			}
		);
	}

	public void addItem(StatusBarItem item) {
		menuPanel.addItem(item);
	}

	public void updateItem(int index, String text, ImageIcon icon) {
		menuPanel.updateItem(index, text, icon);
	}

	public void updateItemMenu(int index, JPopupMenu menu) {
		menuPanel.updateItemMenu(index, menu);
	}
	
	public static class StatusBarItem {
		private String title;
		private ImageIcon icon;
		private Component comp;
		private JPopupMenu menu;
		private Rectangle btRect;
		private int width;

		public StatusBarItem(String title) {
			this(title, -1, null, null);
		}

		public StatusBarItem(Component comp) {
			this("", comp);
		}

		public StatusBarItem(String title, Component comp) {
			this(title, -1, comp, null);
		}

		public StatusBarItem(int width, Component comp) {
			this("", width, comp, null);
		}

		public StatusBarItem(String title, JPopupMenu menu) {
			this(title, -1, null, menu);
		}

		public StatusBarItem(int width, JPopupMenu menu) {
			this("", width, null, menu);
		}

		public StatusBarItem(String title, int width, Component comp, JPopupMenu menu) {
			this(title, null, width, null, menu);
		}

		public StatusBarItem(int width) {
			this("", null, width, null, null);
		}

		public StatusBarItem(String title, ImageIcon icon, int width, Component comp, JPopupMenu menu) {
			this.title = title;
			this.icon = icon;
			this.width = width;
			this.comp = comp;
			this.menu = menu;
			this.btRect = new Rectangle();
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public ImageIcon getIcon() {
			return icon;
		}

		public void setIcon(ImageIcon icon) {
			this.icon = icon;
		}

		public JPopupMenu getMenu() {
			return menu;
		}

		public void setMenu(JPopupMenu menu) {
			this.menu = menu;
		}

		public int getWidth() {
			return width;
		}

		public Component getComponent() {
			return comp;
		}

		public void setComponent(Component comp) {
			this.comp = comp;
		}

		public void setButtonRect(Rectangle rect) {
			btRect.setBounds(rect);
		}

		public Rectangle getButtonRect() {
			return btRect;

		}
	}

	private class MenuPanel extends JPanel {

		private List<StatusBarItem> items = new ArrayList<StatusBarItem>();
		private boolean closed = false;
		private int wx = 0;

		public MenuPanel() {
			setBorder(null);
			setBackground(bkcolor);
			addMouseListener(
				new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						if (!closed) {
							for (StatusBarItem it : items) {
								Rectangle r = it.getButtonRect();
								if (r.contains(e.getX(), e.getY())) {
									openMenu(it.getMenu(), r.x, r.y);
									return;
								}
							}
						}
						int x = e.getX();
						int y = e.getY();
						int w = getWidth();
						int iw = img3.getIconWidth();
						int ih = img3.getIconHeight();

						if (x > w - wx - 16 && x < w - wx - 16 + iw && y > 4 && y < ih + 4) {
							closed = !closed;
							repaint();
						}
					}
				}
			);
		}

		public void addItem(StatusBarItem item) {
			items.add(item);
		}

		public void updateItem(int index, String text, ImageIcon icon) {
			StatusBarItem item = items.get(index);
			item.setTitle(text);
			item.setIcon(icon);
			repaint();
		}

		public void updateItemMenu(int index, JPopupMenu menu) {
			StatusBarItem item = items.get(index);
			item.setMenu(menu);
		}

		private void openMenu(JPopupMenu menu, int x, int y) {
			int h = menu.getHeight();
			if (h == 0) {
				menu.pack();
				menu.setSize(menu.getPreferredSize());
				h = menu.getHeight();
			}
			menu.show(this, x, y - h);
		}

		public void paint(Graphics g) {

			super.paint(g);

			int w = getWidth();
			int iw = img2.getIconWidth();

			g.drawImage(img2.getImage(), w - iw - 1, 0, null);

			wx = iw;

			if (closed) {
				g.drawImage(img4.getImage(), w - wx - 16, 4, null);
				return;
			}

			FontMetrics fm = g.getFontMetrics(g.getFont());

			for (StatusBarItem it : items) {
				
				int mw = it.getMenu() != null ? 20 : 0;
				int cw = it.getIcon() != null ? it.getIcon().getIconWidth() + 8 : 0;
				int tw = it.getWidth();
				
				if (tw == -1) {
					tw = fm.stringWidth(it.getTitle()) + 12;
				}

				wx += tw + mw + cw;
				g.drawImage(img0.getImage(), w - wx - 1, 0, tw + mw + cw, 20, null);
				g.setColor(Color.black);
				g.drawString(it.getTitle(), w - wx + cw + 5, 15);

				if (cw != 0) {
					g.drawImage(it.getIcon().getImage(), w - wx + 4 , 5, null);
				}

				if (it.getMenu() != null) {
					g.drawImage(img5.getImage(), w - wx + tw + 1, 4, null);
					it.setButtonRect(
						new Rectangle(w - wx + tw + 1, 4, img5.getIconWidth(), img5.getIconHeight())
					);
				}
				g.setColor(lnColor);
				g.drawLine(w - wx - 1, 0, w - wx - 1, 20);
			}
			g.drawImage(img1.getImage(), w - wx - 4, 0, null);
			g.drawImage(img3.getImage(), w - wx - 16, 5, null);
		}
	}

	private class MemoryReporter implements ActionListener {

		private JPopupMenu popup;
		private CellsStatusBar statusbar;

		// reporting each 10 secounds
		private int pause = 10000;
		private Timer timer = null;

		public MemoryReporter(CellsStatusBar statusbar) {

			this.statusbar = statusbar;

			Action gcAction =
				new AbstractAction("Invoke garbage collector", CellsUtil.getImage("StartGC")) {
					public void actionPerformed(ActionEvent e) {
						Runtime rt = Runtime.getRuntime();
						rt.gc();
						update();
					}
				};

			Action umAction =
				new AbstractAction("Refresh memory report", CellsUtil.getImage("MemUpdate")) {
					public void actionPerformed(ActionEvent e) {
						Runtime rt = Runtime.getRuntime();
						rt.gc();
						update();
					}
				};

			popup = new JPopupMenu();
			popup.add(new JMenuItem(gcAction));
			popup.add(new JMenuItem(umAction));

			timer = new Timer(pause, this);
			timer.setInitialDelay(10);
		}

		public void update() {
			Runtime rt = Runtime.getRuntime();
			double l = 1000000;

			long m = rt.maxMemory();
			long t = rt.totalMemory();
			long f = rt.freeMemory();

			long p = ((t - f) * 100) / m;

			String ms = Double.toString(m / l);
			ms = ms.substring(0, ms.indexOf(".") + 3);

			statusbar.updateItem(0, p + "% " + "of " + ms + " MB", null);
		}

		public void actionPerformed(ActionEvent e) {
			update();
		}

		public void start() {
			timer.start();
		}

		public void stop() {
			timer.stop();
		}

		public JPopupMenu getMenu() {
			return popup;
		}
	}
}
