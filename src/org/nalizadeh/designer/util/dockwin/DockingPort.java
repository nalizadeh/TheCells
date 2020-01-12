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

package org.nalizadeh.designer.util.dockwin;

import org.nalizadeh.designer.util.CellsGraphics;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

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
public class DockingPort extends JPanel implements MouseListener, MouseMotionListener {

	private static final Cursor CURSOR_MOVE = new Cursor(Cursor.HAND_CURSOR);
	private static final Cursor CURSOR_DOCK_CENTER = createCursor("cdockcenter");
	private static final Cursor CURSOR_DOCK_WEST = createCursor("cdockwest");
	private static final Cursor CURSOR_DOCK_EAST = createCursor("cdockeast");
	private static final Cursor CURSOR_DOCK_NORTH = createCursor("cdocknorth");
	private static final Cursor CURSOR_DOCK_SOUTH = createCursor("cdocksouth");

	private static final Color HINT_COLOR = Color.BLACK;
	private static final Border SP_BORDER = null; //new JSplitPane(JSplitPane.VERTICAL_SPLIT).getBorder();

	private String constraints;
	private Component component;
	private DockingPort root;
	private DockingPort parent;
	private Map<String, DockingPort> ports;

	private DockingPort selected;

	private DockingPort hinted;
	private Rectangle hintrec;
	private DockingHintimage hintimg;
	private Point mpoint;
	private boolean canDraging;
	private boolean draging;

	private int tabPosition;

	private DockingMenu menu;
	private DockingPopup popup;
	private boolean popupEnabled;

	public DockingPort() {
		this(null, null, BorderLayout.CENTER);
		this.menu = new DockingMenu();
		this.popup = new DockingPopup();

		setDoubleBuffered(false);
		initPorts();
	}

	private DockingPort(DockingPort parent, Component component, String constraints) {
		super(new BorderLayout(0, 0));

		setBorder(null);

		this.parent = parent;
		this.root = findRoot();
		this.component = component;
		this.constraints = constraints;
		this.ports = new HashMap<String, DockingPort>();
		this.selected = null;
		this.hinted = null;
		this.hintrec = null;
		this.hintimg = null;
		this.draging = false;
		this.menu = null;
		this.popup = null;
		this.popupEnabled = true;

		ports.put(BorderLayout.NORTH, null);
		ports.put(BorderLayout.SOUTH, null);
		ports.put(BorderLayout.WEST, null);
		ports.put(BorderLayout.EAST, null);
		ports.put(BorderLayout.CENTER, null);
	}

	public DockingPort addDockingWindow(DockingWindow win, String constraints) {

		DockingPort dp = ports.get(constraints);
		
		if (dp == null) {
			dp = new DockingPort(this, win, constraints);
			ports.put(constraints, dp);

		} else if (dp.component instanceof DockingWindow) {
			JTabbedPane tp = new JTabbedPane();
			DockingWindow w = (DockingWindow) dp.component;
			tp.add(w.getTitle(), dp.component);
			tp.add(win.getTitle(), win);
			tp.setIconAt(0, w.getIcon());
			tp.setIconAt(1, win.getIcon());
			tp.setTabPlacement(JTabbedPane.BOTTOM);
			tp.setBorder(null);
			dp.component = tp;

		} else if (dp.component instanceof JTabbedPane) {
			JTabbedPane tp = (JTabbedPane) dp.component;
			tp.add(win.getTitle(), win);
			tp.setIconAt(tp.getComponentCount() - 2, win.getIcon());
		}

		win.setPort(dp, constraints);
		
		root.menu.add(win);
		root.popup.add(win);
		root.layoutPorts();

		return dp;
	}

	public void addDockingPort(DockingWindow win, String cons) {

		if (cons.equals(BorderLayout.CENTER)) {
			if (parent != null && component != null) {
				parent.addDockingWindow(win, constraints);
				return;
			} else {
				addDockingWindow(win, cons);
				return;
			}
		}

		String newCons =
			cons.equals(BorderLayout.WEST) ? BorderLayout.EAST
			: cons.equals(BorderLayout.EAST) ? BorderLayout.WEST
			: cons.equals(BorderLayout.NORTH) ? BorderLayout.SOUTH
			: cons.equals(BorderLayout.SOUTH) ? BorderLayout.NORTH : BorderLayout.CENTER;

		if (getPortCount() == 1) {
			for (DockingPort dp : ports.values()) {
				if (dp != null) {
					dp.constraints = newCons;
					ports.clear();
					ports.put(cons, new DockingPort(this, win, cons));
					ports.put(newCons, dp);

					win.setPort(this, cons);

					break;
				}
			}
		} else {

			Component com = parent == null ? null : component;

			DockingPort dp1 = new DockingPort(this, win, cons);
			DockingPort dp2 = new DockingPort(this, com, newCons);
			dp2.ports.putAll(ports);
			dp2.initPortsParent(true);

			ports.clear();
			ports.put(cons, dp1);
			ports.put(newCons, dp2);

			win.setPort(dp1, cons);
		}

		component = null;

		root.menu.add(win);
		root.popup.add(win);
		root.layoutPorts();
	}

	private void removeDockingWindow(DockingWindow win) {
		if (component == win) {
			component = null;
			parent.ports.put(constraints, null);

		} else if (component instanceof JTabbedPane) {
			JTabbedPane tp = (JTabbedPane) component;
			for (Component co : tp.getComponents()) {
				if (co == win) {
					tp.remove(win);
					if (tp.getComponentCount() == 0) {
						component = null;
						parent.ports.put(constraints, null);
					} else if (tp.getComponentCount() == 1) {
						component = tp.getComponent(0);
					}
				}
			}
		}

		if (component == null) {
			DockingPort dp = parent;
			while (dp != null && dp.getPortCount() == 0) {
				if (dp.parent != null) {
					dp.parent.ports.put(dp.constraints, null);
				}
				dp = dp.parent;
			}
		}
	}

	public boolean selectDockingWindow(DockingWindow win) {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				if (dp.component == win) {
					dp.select(true);
					return true;

				} else if (dp.component instanceof JTabbedPane) {
					JTabbedPane tp = (JTabbedPane) dp.component;
					for (Component co : tp.getComponents()) {
						if (co == win) {
							tp.setSelectedComponent(co);
							dp.select(true);
							return true;
						}
					}
				}
				if (dp.selectDockingWindow(win)) {
					return true;
				}
			}
		}
		return false;
	}

	public JMenu getMenu() {
		return menu;
	}

	public JPopupMenu getPopup() {
		return popup;
	}

	public void setPopupEnabled(boolean b) {
		popupEnabled = b;
	}

	public void paint(final Graphics g) {

		if (hintimg != null) {
			if (this == root) {

				// paint the background
				g.drawImage(hintimg.screen, 0, 0, null);

				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));

				// paint the image
				g2d.translate(hintimg.x, hintimg.y);
				g2d.drawImage(hintimg.image, 0, 0, null);
				g2d.translate(-hintimg.x, -hintimg.y);

				if (hintrec != null) {
					CellsGraphics.drawCompositeRect(g, hintrec, HINT_COLOR);
				}
			}
			return;
		}

		super.paint(g);
	}

	public void select(boolean b) {

		if (component instanceof JTabbedPane) {
			JTabbedPane tp = (JTabbedPane) component;
			if (!b) {
				for (Component co : tp.getComponents()) {
					if (co instanceof DockingWindow) {
						((DockingWindow) co).select(b);
					}
				}
			} else {
				((DockingWindow) tp.getSelectedComponent()).select(b);
			}
		} else if (component != null) {
			((DockingWindow) component).select(b);
		}
	}

	public void deselectAll() {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				dp.deselectAll();
			}
		}
		select(false);
	}

	private void initPortsParent(boolean wins) {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				dp.parent = this;
				if (wins) {
					if (dp.component instanceof DockingWindow) {
						DockingWindow dw = (DockingWindow) dp.component;
						dw.setPort(this, dw.getConstraints());
					} else if (dp.component instanceof JTabbedPane) {
						JTabbedPane tp = (JTabbedPane) dp.component;
						for (Component co : tp.getComponents()) {
							if (co instanceof DockingWindow) {
								DockingWindow dw = (DockingWindow) co;
								dw.setPort(this, dw.getConstraints());
							}
						}
					}
				}
			}
		}
		if (wins) {
			if (component instanceof DockingWindow) {
				DockingWindow dw = (DockingWindow) component;
				dw.setPort(this, dw.getConstraints());

			} else if (component instanceof JTabbedPane) {
				JTabbedPane tp = (JTabbedPane) component;
				for (Component co : tp.getComponents()) {
					if (co instanceof DockingWindow) {
						DockingWindow dw = (DockingWindow) co;
						dw.setPort(this, dw.getConstraints());
					}
				}
			}
		}
	}

	private DockingWindow getDockingWin() {
		if (component instanceof JTabbedPane) {
			return (DockingWindow) ((JTabbedPane) component).getSelectedComponent();
		}
		return (DockingWindow) component;
	}

	private boolean containsDockingWindow(DockingWindow win) {
		return findDockingPort(win) != null;
	}

	private boolean containsDockingPort(DockingPort port) {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				if (dp == port || dp.containsDockingPort(port)) {
					return true;
				}
			}
		}
		return false;
	}

	private DockingPort findDockingPort(DockingWindow win) {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				if (dp.component instanceof DockingWindow) {
					if (dp.component == win) {
						return dp;
					}
				} else if (dp.component instanceof JTabbedPane) {
					JTabbedPane tp = (JTabbedPane) dp.component;
					for (Component co : tp.getComponents()) {
						if (co == win) {
							return dp;
						}
					}
				}
				dp = dp.findDockingPort(win);
				if (dp != null) {
					return dp;
				}
			}
		}
		return null;
	}

	private DockingPort findDockingPort(int x, int y) {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {

				// only by mouse draging
				if (root.draging && dp == root.selected) {
					continue;
				}

				DockingPort d = dp.findDockingPort(x, y);
				if (d != null) {
					return d;
				}
			}
		}

		return convertRectangle(this).contains(x, y) ? this : null;
	}

	private Rectangle findHintRect(int mx, int my) {

		Rectangle r = convertRectangle(this);

		int w = r.width;
		int h = r.height;
		int x = r.x;
		int y = r.y;
		int ws = w / 3;
		int hs = h / 3;

		Rectangle north = new Rectangle(x, y, w, hs);
		if (north.contains(mx, my)) {
			return north;
		}
		Rectangle south = new Rectangle(x, y + h - hs, w, hs);
		if (south.contains(mx, my)) {
			return south;
		}
		Rectangle west = new Rectangle(x, y + hs, ws, h - 2 * hs);
		if (west.contains(mx, my)) {
			west.y = y;
			west.height = h;
			return west;
		}
		Rectangle east = new Rectangle(x + w - ws, y + hs, ws, h - 2 * hs);
		if (east.contains(mx, my)) {
			east.y = y;
			east.height = h;
			return east;
		}
		Rectangle center = new Rectangle(x + ws, y + hs, ws, hs);
		if (center.contains(mx, my)) {
			center.setBounds(r);
			return center;
		}

		return null;
	}

	private String findConstraints(int mx, int my) {

		Rectangle r = convertRectangle(this);

		int w = r.width;
		int h = r.height;
		int x = r.x;
		int y = r.y;
		int ws = w / 3;
		int hs = h / 3;

		Rectangle north = new Rectangle(x, y, w, hs);
		if (north.contains(mx, my)) {
			return BorderLayout.NORTH;
		}
		Rectangle south = new Rectangle(x, y + h - hs, w, hs);
		if (south.contains(mx, my)) {
			return BorderLayout.SOUTH;
		}
		Rectangle west = new Rectangle(x, y + hs, ws, h - 2 * hs);
		if (west.contains(mx, my)) {
			return BorderLayout.WEST;
		}
		Rectangle east = new Rectangle(x + w - ws, y + hs, ws, h - 2 * hs);
		if (east.contains(mx, my)) {
			return BorderLayout.EAST;
		}
		Rectangle center = new Rectangle(x + ws, y + hs, ws, hs);
		if (center.contains(mx, my)) {
			return BorderLayout.CENTER;
		}

		return null;
	}

	private Cursor findCursor(int mx, int my) {

		Rectangle r = convertRectangle(this);

		int w = r.width;
		int h = r.height;
		int x = r.x;
		int y = r.y;
		int ws = w / 3;
		int hs = h / 3;

		Rectangle north = new Rectangle(x, y, w, hs);
		if (north.contains(mx, my)) {
			return CURSOR_DOCK_NORTH;
		}
		Rectangle south = new Rectangle(x, y + h - hs, w, hs);
		if (south.contains(mx, my)) {
			return CURSOR_DOCK_SOUTH;
		}
		Rectangle west = new Rectangle(x, y + hs, ws, h - 2 * hs);
		if (west.contains(mx, my)) {
			return CURSOR_DOCK_WEST;
		}
		Rectangle east = new Rectangle(x + w - ws, y + hs, ws, h - 2 * hs);
		if (east.contains(mx, my)) {
			return CURSOR_DOCK_EAST;
		}
		Rectangle center = new Rectangle(x + ws, y + hs, ws, hs);
		if (center.contains(mx, my)) {
			return CURSOR_DOCK_CENTER;
		}

		return CURSOR_MOVE;
	}

	private int getPortCount() {
		int n = 0;
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				n++;
			}
		}
		return n;
	}

	private boolean canDock(String cons) {

		if (
			cons != null
			&& (
				hinted != selected.parent //
				|| !cons.equals(selected.constraints) //
				|| selected.component instanceof JTabbedPane
			)
		) {

			int n = getPortCount();
			if (n == 1) {
				for (DockingPort dp : ports.values()) {
					if (dp != null) {
						if (dp.component instanceof JTabbedPane) {
							return !cons.equals(BorderLayout.CENTER);
						}
						return dp.getPortCount() > 0;
					}
				}
			}
			return n > 1;
		}
		return false;
	}

	private void clear() {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				dp.clear();
			}
		}
		removeAll();
	}

	private void clearPorts() {
		removeAll();
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				dp.clearPorts();
			}
		}
		component = null;
		if (parent != null) {
			parent.ports.put(constraints, null);
		}

	}

	private void clearTree() {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				dp.clearTree();
			}
		}
		if (component == null && getPortCount() == 1) {
			for (DockingPort dp : ports.values()) {
				if (dp != null && dp.getPortCount() != 0) {
					ports.clear();
					ports.putAll(dp.ports);
					dp.ports.clear();
					initPortsParent(false);
					break;
				}
			}
		}
	}

	private void open(DockingWindow win) {
		deselectAll();
		if (!containsDockingWindow(win)) {
			win.minimize();
			DockingPort dp = win.getPort();
			while (true) {
				if (root == dp || root.containsDockingPort(dp)) {
					break;
				}
				dp = dp.parent;
			}

			dp.addDockingWindow(win, win.getConstraints());
			dp.select(true);
		}
	}

	public void maximize(DockingWindow dw) {
		dw.maximize();
		tabPosition = -1;
		if (component instanceof JTabbedPane) {
			tabPosition = ((JTabbedPane) component).getComponentZOrder(dw);
		}
		root.layoutPorts();
	}

	public void minimize(DockingWindow win) {
		win.minimize();
		if (component instanceof JTabbedPane) {
			JTabbedPane tp = (JTabbedPane) component;
			tp.add(win, tabPosition);
			tp.setTitleAt(tabPosition, win.getTitle());
			tp.setIconAt(tabPosition, win.getIcon());
			tp.setSelectedIndex(tabPosition);
		}

		root.layoutPorts();
	}

	public void iconify(DockingWindow win) {
		root.deselectAll();
		win.iconify();
		removeDockingWindow(win);
		root.layoutPorts();
	}

	public void close(DockingWindow win) {
		iconify(win);
	}

	public void setToDefault() {
		clearPorts();
		initPorts();
		deselectAll();
		menu.selectAll(true);
		popup.selectAll(true);
	}

	public DockingPort getRoot() {
		return root;
	}

	protected void initPorts() {
	}

	private DockingWindow findMaximizedWin() {
		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				DockingWindow w = dp.findMaximizedWin();
				if (w != null) {
					return w;
				}
				if (dp.component instanceof DockingWindow) {
					if (((DockingWindow) dp.component).isMaximized()) {
						return (DockingWindow) dp.component;
					}
				} else if (dp.component instanceof JTabbedPane) {
					JTabbedPane tp = (JTabbedPane) dp.component;
					for (Component co : tp.getComponents()) {
						if (co instanceof DockingWindow) {
							if (((DockingWindow) co).isMaximized()) {
								return (DockingWindow) co;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private DockingPort findMaximizedPort() {
		DockingWindow dw = findMaximizedWin();
		return dw != null ? dw.getPort() : null;
	}

	private DockingPort findRoot() {
		DockingPort da = this;
		while (da.parent != null) {
			da = da.parent;
		}
		return da;
	}

	private Point convertPoint(Component src, int x, int y) {
		return SwingUtilities.convertPoint(src, x, y, root);
	}

	private Rectangle convertRectangle(Component src) {
		Point p = convertPoint(src, 0, 0);
		return new Rectangle(p.x, p.y, src.getWidth(), src.getHeight());
	}

	private Rectangle convertRectToScreen(Component src) {
		Rectangle b = src.getBounds();
		Point p = new Point(b.x, b.y);
		SwingUtilities.convertPointToScreen(p, src.getParent());
		b.x = p.x;
		b.y = p.y;
		return b;
	}

	private Point convertPointToScreen(Component src, Point p) {
		SwingUtilities.convertPointToScreen(p, src);
		return p;
	}

	public void mouseClicked(MouseEvent e) {
		if (draging) {
			e.consume();
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (draging) {
			e.consume();
		}
	}

	public void mouseExited(MouseEvent e) {
		if (draging) {
			e.consume();
		}
	}

	public void mouseMoved(MouseEvent e) {		if (draging) {
			e.consume();
		}
	}

	public void mousePressed(MouseEvent e) {

		deselectAll();

		selected = findMaximizedPort();

		if (selected == null) {
			mpoint = convertPoint(e.getComponent(), e.getX(), e.getY());
			selected = findDockingPort(mpoint.x, mpoint.y);
		}

		if (selected != null) {
			selected.select(true);
			DockingWindow dw = selected.getDockingWin();
			if (dw != null) {
				canDraging = dw.canDrag(e);
			}
		}
		repaint();
	}

	public void mouseDragged(MouseEvent e) {

		synchronized (this) {
			if (canDraging && !draging && selected != null) {
			
				DockingWindow dw = selected.getDockingWin();
				if (dw != null) {
					draging = dw.canDrag(e);

					if (draging) {
						hintimg = new DockingHintimage(dw);
					}
				}
			}
		
			if (draging) {
				Point p = new Point(mpoint.x, mpoint.y);
				mpoint = convertPoint(e.getComponent(), e.getX(), e.getY());
				p.move(mpoint.x - p.x, mpoint.y - p.y);

				hinted = findDockingPort(mpoint.x, mpoint.y);
				hintrec = hinted == null ? null : hinted.findHintRect(mpoint.x, mpoint.y);
				hintimg.x += p.x;
				hintimg.y += p.y;

				e.consume();
				setCursor(hinted == null ? CURSOR_MOVE : hinted.findCursor(mpoint.x, mpoint.y));
				repaint();
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		mpoint = convertPoint(e.getComponent(), e.getX(), e.getY());

		if (e.isPopupTrigger()) {
			if (popupEnabled) {
				popup.show(this, mpoint.x, mpoint.y);
			}

		} else if (draging) {

			if (hinted != null) {

				String cons = hinted.findConstraints(mpoint.x, mpoint.y);

				if (canDock(cons)) {
					DockingWindow win = selected.getDockingWin();
					if (win != null) {
						selected.removeDockingWindow(win);
						if (
							hinted.parent != null
							&& hinted.parent.ports.get(hinted.constraints) == null
						) {
							hinted = hinted.parent;
						}
						hinted.addDockingPort(win, cons);

						clearTree();
						layoutPorts();

						selectDockingWindow(win);
					}
				}
			}
		}
		setCursor(Cursor.getDefaultCursor());
		draging = false;
		hinted = null;
		hintrec = null;
		hintimg = null;

		repaint();
	}

	public static ImageIcon getImage(String name) {
		URL url = DockingPort.class.getResource("images/" + name);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}

	public static void fillGradient(Graphics g, Rectangle b, Color c1, Color c2) {
		Graphics2D g2 = (Graphics2D) g;
		GradientPaint gp = new GradientPaint(0, b.height, c1, b.width, 0, c2, false);
		g2.setPaint(gp);
		g2.fill(new Rectangle(0, 0, b.width, b.height));
	}

	private static Cursor createCursor(String name) {
		BufferedImage sizedTransBim = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = sizedTransBim.createGraphics();
		g.drawImage(getImage(name + ".gif").getImage(), 0, 0, null);
		g.dispose();
		Point hotspot = new Point(0, 0);
		return Toolkit.getDefaultToolkit().createCustomCursor(sizedTransBim, hotspot, name);
	}

	private void debug() {
		System.out.println("---");
		System.out.println("---");
		root.dodebug(1);
	}

	private void dodebug(int p) {
		StringBuffer sb1 = new StringBuffer();
		for (int i = 0; i < p; i++) {
			sb1.append(" ");
		}

		for (DockingPort dp : ports.values()) {
			if (dp != null) {
				StringBuffer sb2 = new StringBuffer(sb1.toString() + dp.constraints);
				if (dp.component != null) {
					if (dp.component instanceof DockingWindow) {
						sb2.append(
							"->" + dp.component.getClass().getName() + " ("
							+ ((DockingWindow) dp.component).getTitle() + ")"
						);
					} else {
						sb2.append("->" + dp.component.getClass().getName());
					}

				}
				System.out.println(sb2.toString());
				dp.dodebug(p + 5);
			}
		}
	}

	private void layoutPorts() {

		clear();

		if (!ports.isEmpty()) {

			DockingWindow dw = findMaximizedWin();
			if (dw != null) {
				add(dw, BorderLayout.CENTER);
				revalidate();
				doLayout();
				return;
			}

			DockingPort north = ports.get(BorderLayout.NORTH);
			DockingPort south = ports.get(BorderLayout.SOUTH);
			DockingPort west = ports.get(BorderLayout.WEST);
			DockingPort east = ports.get(BorderLayout.EAST);
			DockingPort center = ports.get(BorderLayout.CENTER);

			Component comp = null;
			JSplitPane sp1 = null;
			JSplitPane sp2 = null;
			JSplitPane sp3 = null;

			Component northComp = null;
			Component southComp = null;
			Component westComp = null;
			Component eastComp = null;
			Component centerComp = null;

			if (north != null) {
				north.layoutPorts();
				if (south != null) {
					if (sp1 == null) {
						sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp1.setBorder(SP_BORDER);
						sp1.setResizeWeight(0);
					}
					sp1.add(north, JSplitPane.TOP);
				} else {
					northComp = north;
				}
			}
			if (south != null) {
				south.layoutPorts();
				if (north != null) {
					if (sp1 == null) {
						sp1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp1.setBorder(SP_BORDER);
						sp1.setResizeWeight(1);
					}
					sp1.add(south, JSplitPane.BOTTOM);
				} else {
					southComp = south;
				}
			}
			if (west != null) {
				west.layoutPorts();
				if (east != null) {
					if (sp2 == null) {
						sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
						sp2.setBorder(SP_BORDER);
						sp2.setResizeWeight(1);
					}
					sp2.add(west, JSplitPane.LEFT);
				} else {
					westComp = west;
				}
			}
			if (east != null) {
				east.layoutPorts();
				if (west != null) {
					if (sp2 == null) {
						sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
						sp2.setBorder(SP_BORDER);
						sp2.setResizeWeight(1);
					}
					sp2.add(east, JSplitPane.RIGHT);
				} else {
					eastComp = east;
				}
			}
			if (center != null) {
				center.layoutPorts();
				centerComp = center;

				if (sp2 != null) {
					if (sp2.getLeftComponent() != null && sp2.getRightComponent() != null) {
						JSplitPane sp4 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
						sp4.setBorder(SP_BORDER);
						sp4.setResizeWeight(0);
						sp4.add(sp2.getLeftComponent(), JSplitPane.LEFT);
						sp4.add(center, JSplitPane.RIGHT);

						sp3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
						sp3.setBorder(SP_BORDER);
						sp3.setResizeWeight(1);
						sp3.add(sp4, JSplitPane.LEFT);
						sp3.add(sp2.getRightComponent(), JSplitPane.RIGHT);

						sp2 = null;

					} else if (sp2.getLeftComponent() != null) {
						sp2.add(center, JSplitPane.RIGHT);
						sp3 = sp2;
						sp2 = null;
					} else if (sp2.getRightComponent() != null) {
						sp2.add(center, JSplitPane.LEFT);
						sp3 = sp2;
						sp2 = null;
					}
				} else if (westComp != null) {
					sp3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
					sp3.setBorder(SP_BORDER);
					sp3.setResizeWeight(0);
					sp3.add(westComp, JSplitPane.LEFT);
					sp3.add(center, JSplitPane.RIGHT);

				} else if (eastComp != null) {
					sp3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
					sp3.setBorder(SP_BORDER);
					sp3.setResizeWeight(1);
					sp3.add(center, JSplitPane.LEFT);
					sp3.add(eastComp, JSplitPane.RIGHT);
				}

				if (sp1 != null) {
					if (sp1.getTopComponent() != null && sp1.getBottomComponent() != null) {
						JSplitPane sp4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp4.setBorder(SP_BORDER);
						sp4.setResizeWeight(0);
						sp4.add(sp1.getTopComponent(), JSplitPane.TOP);

						if (sp3 == null) {
							sp4.add(center, JSplitPane.BOTTOM);
						} else {
							sp4.add(sp3, JSplitPane.BOTTOM);
						}

						JSplitPane sp5 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp5.setBorder(SP_BORDER);
						sp5.setResizeWeight(1);
						sp5.add(sp4, JSplitPane.TOP);
						sp5.add(sp1.getBottomComponent(), JSplitPane.BOTTOM);

						sp3 = sp5;
						sp1 = null;

					} else if (sp1.getTopComponent() != null) {
						if (sp3 == null) {
							sp1.add(center, JSplitPane.BOTTOM);
						} else {
							sp1.add(sp3, JSplitPane.BOTTOM);
						}
						sp3 = sp1;
						sp1 = null;
					} else if (sp1.getBottomComponent() != null) {
						if (sp3 == null) {
							sp1.add(center, JSplitPane.TOP);
						} else {
							sp1.add(sp3, JSplitPane.TOP);
						}
						sp3 = sp1;
						sp1 = null;
					}
				} else if (northComp != null) {
					if (sp3 == null) {
						sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp3.setBorder(SP_BORDER);
						sp3.setResizeWeight(0);
						sp3.add(northComp, JSplitPane.TOP);
						sp3.add(center, JSplitPane.BOTTOM);
					} else {
						JSplitPane sp4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp4.setBorder(SP_BORDER);
						sp4.setResizeWeight(1);
						sp4.add(northComp, JSplitPane.TOP);
						sp4.add(sp3, JSplitPane.BOTTOM);
						sp3 = sp4;
					}
				} else if (southComp != null) {
					if (sp3 == null) {
						sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp3.setBorder(SP_BORDER);
						sp3.setResizeWeight(0);
						sp3.add(center, JSplitPane.TOP);
						sp3.add(southComp, JSplitPane.BOTTOM);
					} else {
						JSplitPane sp4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp4.setBorder(SP_BORDER);
						sp4.setResizeWeight(1);
						sp4.add(sp3, JSplitPane.TOP);
						sp4.add(southComp, JSplitPane.BOTTOM);
						sp3 = sp4;
					}
				}
			}

			if (sp3 != null) {
				comp = sp3;

			} else if (sp1 != null && sp2 != null) {
				if (sp1.getTopComponent() != null) {
					JSplitPane sp4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
					sp4.setBorder(SP_BORDER);
					sp4.setResizeWeight(1);
					sp4.add(sp1.getTopComponent(), JSplitPane.TOP);
					sp4.add(sp2, JSplitPane.BOTTOM);
					if (sp1.getBottomComponent() != null) {
						sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp3.setBorder(SP_BORDER);
						sp3.add(sp4, JSplitPane.TOP);
						sp3.add(sp1.getBottomComponent(), JSplitPane.BOTTOM);
					} else {
						sp3 = sp4;
					}
				} else {
					sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
					sp3.setBorder(SP_BORDER);
					sp3.setResizeWeight(0);
					sp3.add(sp2, JSplitPane.TOP);
					sp3.add(sp1.getBottomComponent(), JSplitPane.BOTTOM);
				}
				comp = sp3;

			} else if (sp1 != null && westComp != null) {
				if (sp1.getTopComponent() != null) {
					JSplitPane sp4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
					sp4.setBorder(SP_BORDER);
					sp4.add(sp1.getTopComponent(), JSplitPane.TOP);
					sp4.add(westComp, JSplitPane.BOTTOM);
					if (sp1.getBottomComponent() != null) {
						sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp3.setBorder(SP_BORDER);
						sp3.add(sp4, JSplitPane.TOP);
						sp3.add(sp1.getBottomComponent(), JSplitPane.BOTTOM);
					} else {
						sp3 = sp4;
					}
				} else {
					sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
					sp3.setBorder(SP_BORDER);
					sp3.add(westComp, JSplitPane.TOP);
					sp3.add(sp1.getBottomComponent(), JSplitPane.BOTTOM);
				}
				comp = sp3;

			} else if (sp1 != null && eastComp != null) {
				if (sp1.getTopComponent() != null) {
					JSplitPane sp4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
					sp4.setBorder(SP_BORDER);
					sp4.add(sp1.getTopComponent(), JSplitPane.TOP);
					sp4.add(eastComp, JSplitPane.BOTTOM);
					if (sp1.getBottomComponent() != null) {
						sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
						sp3.setBorder(SP_BORDER);
						sp3.add(sp4, JSplitPane.TOP);
						sp3.add(sp1.getBottomComponent(), JSplitPane.BOTTOM);
					} else {
						sp3 = sp4;
					}
				} else {
					sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
					sp3.setBorder(SP_BORDER);
					sp3.add(eastComp, JSplitPane.TOP);
					sp3.add(sp1.getBottomComponent(), JSplitPane.BOTTOM);
				}
				comp = sp3;

			} else if (sp2 != null && northComp != null) {
				sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				sp3.setBorder(SP_BORDER);
				sp3.add(northComp, JSplitPane.TOP);
				sp3.add(sp2, JSplitPane.BOTTOM);
				comp = sp3;

			} else if (sp2 != null && southComp != null) {
				sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				sp3.setBorder(SP_BORDER);
				sp3.add(sp2, JSplitPane.TOP);
				sp3.add(southComp, JSplitPane.BOTTOM);
				comp = sp3;

			} else if (northComp != null && westComp != null) {
				sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				sp3.setBorder(SP_BORDER);
				sp3.add(northComp, JSplitPane.TOP);
				sp3.add(westComp, JSplitPane.BOTTOM);
				comp = sp3;

			} else if (northComp != null && eastComp != null) {
				sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				sp3.setBorder(SP_BORDER);
				sp3.add(northComp, JSplitPane.TOP);
				sp3.add(eastComp, JSplitPane.BOTTOM);
				comp = sp3;

			} else if (southComp != null && westComp != null) {
				sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				sp3.setBorder(SP_BORDER);
				sp3.add(westComp, JSplitPane.TOP);
				sp3.add(southComp, JSplitPane.BOTTOM);
				comp = sp3;

			} else if (southComp != null && eastComp != null) {
				sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				sp3.setBorder(SP_BORDER);
				sp3.add(eastComp, JSplitPane.TOP);
				sp3.add(southComp, JSplitPane.BOTTOM);
				comp = sp3;

			} else if (sp1 != null) {
				comp = sp1;
			} else if (sp2 != null) {
				comp = sp2;
			} else if (northComp != null) {
				comp = northComp;
			} else if (southComp != null) {
				comp = southComp;
			} else if (westComp != null) {
				comp = westComp;
			} else if (eastComp != null) {
				comp = eastComp;
			} else if (centerComp != null) {
				comp = centerComp;
			}

			if (comp != null) {
				add(comp, BorderLayout.CENTER);
			} else if (component != null) {
				add(component, BorderLayout.CENTER);
			}

			revalidate();
			doLayout();
		}

		if (parent == null) {
			menu.updateWins();
			popup.updateWins();
		}
	}

	private class DockingAction extends AbstractAction {
		private DockingWindow win;

		public DockingAction(DockingWindow win) {
			super(win.getTitle(), win.getIcon());
			this.win = win;
		}

		public DockingAction(String title, Icon icon) {
			super(title, icon);
			this.win = null;
		}

		public void actionPerformed(ActionEvent ae) {
			JMenuItem it = (JMenuItem) ae.getSource();
			if (win != null) {
				if (it.isSelected()) {
					win.getPort().open(win);
				} else {
					win.getPort().close(win);
				}
			} else {
				root.setToDefault();
			}
		}
	}

	private class DockingMenu extends JMenu {

		private List<JMenuItem> items = new ArrayList<JMenuItem>();

		public DockingMenu() {
			super("View");
			setMnemonic(KeyEvent.VK_V);
			JMenuItem item =
				new JMenuItem(new DockingAction("Default", CellsUtil.getImage("Cell")));
			item.setSelected(true);
			items.add(item);
			super.add(item);
			super.addSeparator();
		}

		public void add(DockingWindow win) {
			if (!contains(win)) {
				JCheckBoxMenuItem item = new JCheckBoxMenuItem(new DockingAction(win));
				item.setSelected(true);
				items.add(item);
				super.add(item);
			}
		}

		public void updateWins() {
			for (JMenuItem item : items) {
				DockingAction da = (DockingAction) item.getAction();
				if (da.win != null) {
					item.setSelected(!da.win.isIconified() && !da.win.isClosed());
				}
			}
		}

		private boolean contains(DockingWindow win) {
			for (JMenuItem item : items) {
				DockingAction da = (DockingAction) item.getAction();
				if (da.win == win) {
					return true;
				}
			}
			return false;
		}

		private void selectAll(boolean b) {
			for (JMenuItem item : items) {
				DockingAction da = (DockingAction) item.getAction();
				if (da.win != null) {
					((JCheckBoxMenuItem) item).setSelected(b);
				}
			}
		}
	}

	private class DockingPopup extends JPopupMenu {

		private List<JMenuItem> items = new ArrayList<JMenuItem>();

		public DockingPopup() {
			JMenuItem item =
				new JMenuItem(new DockingAction("Default", CellsUtil.getImage("Cell")));
			item.setSelected(true);
			items.add(item);
			super.add(item);
			super.addSeparator();
		}

		public void add(DockingWindow win) {
			if (!contains(win)) {
				JCheckBoxMenuItem item = new JCheckBoxMenuItem(new DockingAction(win));
				item.setSelected(true);
				items.add(item);
				super.add(item);
			}
		}

		public void updateWins() {
			for (JMenuItem item : items) {
				DockingAction da = (DockingAction) item.getAction();
				if (da.win != null) {
					item.setSelected(!da.win.isIconified() && !da.win.isClosed());
				}
			}
		}

		private boolean contains(DockingWindow win) {
			for (JMenuItem item : items) {
				DockingAction da = (DockingAction) item.getAction();
				if (da.win == win) {
					return true;
				}
			}
			return false;
		}

		private void selectAll(boolean b) {
			for (JMenuItem item : items) {
				DockingAction da = (DockingAction) item.getAction();
				if (da.win != null) {
					((JCheckBoxMenuItem) item).setSelected(b);
				}
			}
		}
	}

	private class DockingHintimage {

		private Image screen;
		private Image image;
		private int x;
		private int y;

		DockingHintimage(DockingWindow win) {

			screen = CellsGraphics.captureScreen(convertRectToScreen(root));
			image = CellsGraphics.captureScreen(convertRectToScreen(win));

			Rectangle r = convertRectangle(win);
			x = r.x;
			y = r.y;
		}
	}
}
