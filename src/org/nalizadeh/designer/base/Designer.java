/*
 * Copyright 2007 N.A.J. nalizadeh.org - All rights reserved.
 * nalizadeh.org PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as showHintpublished by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributeselectionChangedd in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package org.nalizadeh.designer.base;

import org.nalizadeh.designer.util.CellsGraphics;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
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
public class Designer extends JPanel implements ContainerListener, MouseListener,
	MouseMotionListener, KeyListener
{

	public static final int RESIZEABLE = 0;
	public static final int HORIZONTAL_RESIZEABLE = 1;
	public static final int VERTICAL_RESIZEABLE = 2;

	private static final ImageIcon PORTICON = CellsUtil.getImage("port.gif");
	private static final Color MCOLOR = new Color(153, 153, 204);
	private static final Color GCOLOR = new Color(222, 222, 222);

	private Map<Component, Cell> cells = new HashMap<Component, Cell>();

	private boolean isRoot = false;
	private boolean isEnabled = true;
	private int resizeMode = RESIZEABLE;

	private int mx = -1;
	private int my = -1;
	private boolean isPopupTrigger = false;
	private Point oldLocation = null;
	private Rectangle oldBounds = null;

	private Component hintComp = null;
	private Rectangle hintBounds = new Rectangle();
	private Point hintPoint = new Point();
	private boolean hintRollover = true;

	private Component insertingComp = null;
	private boolean paintinsertComp = false;

	private boolean marqueeingEnabled = false;
	private boolean marqueeingStartet = false;
	private boolean marqueeing = false;
	private int mqXs = -1;
	private int mqYs = -1;
	private int mqXe = -1;
	private int mqYe = -1;

	private boolean gridEnabled = false;
	private boolean gridPainted = false;
	private int gridXd = 10;
	private int gridYd = 10;

	private boolean selectionAdjusting = false;
	private boolean multiSelection = false;
	
	public Designer(boolean isRoot, LayoutManager layout) {
		super(layout, true);

		this.isRoot = isRoot;

		if (isRoot) {
			addContainerListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			addKeyListener(this);
		}
	}

	public void paint(Graphics g) {

		super.paint(g);

		if (insertingComp != null) {
			if (paintinsertComp) {
				g.translate(mx, my);
				paintInsertingComponent(g, insertingComp);
				g.translate(-mx, -my);
			}

		} else if (marqueeing) {
			int xs = Math.min(mqXs, mqXe);
			int xe = Math.max(mqXs, mqXe);
			int ys = Math.min(mqYs, mqYe);
			int ye = Math.max(mqYs, mqYe);
			CellsGraphics.drawDashRect(g, new Rectangle(xs, ys, xe - xs, ye - ys), MCOLOR);
		}

		showHint(g, hintComp, hintPoint.x, hintPoint.y, hintBounds, hintRollover);

		for (Cell cell : cells.values()) {
			cell.paint(g);
		}
	}

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		if (gridEnabled && gridPainted) {
			g.setColor(GCOLOR);

			int w = getWidth();
			int h = getHeight();
			int x = 0;
			int y = 0;

			while (x < w) {
				g.drawLine(x, 0, x, h);
				x += gridXd;
			}
			while (y < h) {
				g.drawLine(0, y, w, y);
				y += gridYd;
			}
		}
	}

	public void componentAdded(ContainerEvent e) {
		Component comp = e.getChild();
		if (!cells.containsKey(comp)) {
			initListener(comp, true);
		}
	}

	public void componentRemoved(ContainerEvent e) {
		Component comp = e.getChild();
		if (cells.containsKey(comp)) {
			initListener(comp, false);
		}
	}

	public void setEnabled(boolean enabled) {

		this.isEnabled = enabled;

		Cursor c = enabled ? Cursor.getDefaultCursor() : new Cursor(Cursor.WAIT_CURSOR);
		setCursor(c);
		for (Cell cell : cells.values()) {
			cell.setCursor(c);
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (isEnabled) {
			translate(e);
			if (insertingComp != null) {
				paintinsertComp = true;
				initHints(mx, my, null, insertingComp, false);
				return;
			}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (isEnabled) {
			paintinsertComp = false;
			clearHints(true);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (isEnabled) {
			translate(e);
			if (isEventForMultiSelection(e)) {
				return;
			}
			if (e.getClickCount() == 2 && !marqueeingEnabled && insertingComp == null) {
				Cell cell = findCell(this);
				if (cell != null) {
					cell.mouseClicked(e);
					if (e.isConsumed()) {
						doubleClicked(cell.comp, mx, my);
					}
				}
			}
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (isEnabled) {
			translate(e);
			if (multiSelection || marqueeingEnabled) {
				return;
			}

			if (insertingComp != null) {
				insertingComp.setLocation(mx, my);
				initHints(mx, my, null, insertingComp, false);
				return;
			}

			Cell cell = findCell(this);
			if (cell != null) {
				cell.mouseMoved(e);
				if (e.isConsumed()) {
					initHints(mx, my, cell, cell.comp, true);
					return;
				}
			}

			clearHints(true);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isEnabled) {
			translate(e);
			
			isPopupTrigger = e.isPopupTrigger();

			if (isEventForMultiSelection(e)) {
				return;
			}

			// CTRL_DOWN_MASK and BUTTON1_DOWN_MASK without SHIFT_DOWN_MASK

			multiSelection = false;

			int onmask = MouseEvent.CTRL_DOWN_MASK | MouseEvent.BUTTON1_DOWN_MASK;
			int offmask = MouseEvent.SHIFT_DOWN_MASK;

			if ((e.getModifiersEx() & (onmask | offmask)) == onmask) {
				boolean found = false;
				Cell cell = findCell(this);
				if (cell != null) {
					cell.mousePressed(e);
					if (e.isConsumed()) {
						selectMultiple(cell);
						repaint();
						found = true;
					}
				}
				if (found && getSelectedComponents().length > 0) {
					multiSelection = true;
					return;
				}
			}

			// this is happened, when the mouse dragged and
			// at the same time the right mouse button pressed
			if (e.getButton() == MouseEvent.BUTTON3) {

				if (insertingComp != null) {
					setInsertingComponent(null);
					select(null);
					clearHints(true);
					return;
				}

				Cell cell = getSelectedCell();
				if (cell != null) {
					boolean moving = cell.port.moving;
					boolean resizing = cell.port.resizing;

					cell.mouseReleased(e);

					if (e.isConsumed() && (moving || resizing)) {
						Designer d = findDesigner(cell.comp);
						cell.bounds.setBounds(oldBounds);
						cell.comp.setBounds(translateBoundsTo(d, oldBounds));
						oldLocation = null;
						oldBounds = null;
						select(null);
						clearHints(true);
						return;
					}
				}
			}

			select(null);

			if (marqueeingEnabled) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					marqueeingStartet = true;
					mqXs = mx;
					mqYs = my;
				}
				repaint();
				return;
			}

			if (insertingComp != null) {
				insert(insertingComp);
				return;
			}
			
			Cell cell = findCell(this);
			if (cell != null) {
				cell.mousePressed(e);
				if (e.isConsumed()) {
					oldLocation = new Point(mx, my);
					oldBounds = new Rectangle(cell.bounds);
					select(cell.comp);
					repaint();
					return;
				}
			}
			clearHints(true);
		}
	}

	public void mouseDragged(MouseEvent e) {
		if (isEnabled) {
			translate(e);

			if (multiSelection) {
				return;
			}

			if (marqueeingStartet) {
				mqXe = mx;
				mqYe = my;
				marqueeing = true;
				repaint();
				return;
			}

			if (insertingComp != null) {
				insertingComp.setLocation(mx, my);
				initHints(mx, my, null, insertingComp, false);
				return;
			}

			Cell cell = getSelectedCell();
			if (cell != null) {
				cell.mouseDragged(e);
				if (e.isConsumed()) {
					initHints(mx, my, cell, cell.comp, false);
					return;
				}
			}
			clearHints(true);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isEnabled) {
			translate(e);

			if (marqueeing && !e.isPopupTrigger()) {

				selectMarqueeing();
				repaint();

				marqueeing = false;
				marqueeingStartet = false;
				mqXs = -1;
				mqYs = -1;
				mqXe = -1;
				mqYe = -1;
				return;
			}

			if (isPopupTrigger || e.isPopupTrigger()) {

				if (insertingComp != null) {
					setInsertingComponent(null);
					clearHints(true);

				} else {
					e.consume();
					showPopup(getSelectedComponents(), mx, my);
				}
				return;
			}

			if (multiSelection) {
				return;
			}

			Cell cell = getSelectedCell();
			if (cell != null) {
				boolean moving = cell.port.moving;
				boolean resizing = cell.port.resizing;

				cell.mouseReleased(e);

				if (e.isConsumed() && (moving || resizing)) {

					if (moving) {
						move(cell);

					} else if (resizing) {
						resize(cell);
					}
					oldLocation = null;
					oldBounds = null;
				}
			}
			clearHints(true);
		}
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			Component co = getSelectedComponent();
			if (co != null) {
				deleteComponent(co);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	private void initListener(Component comp, boolean set) {

		if (set) {
			if (isGuiComponent(comp)) {
				cells.put(comp, new Cell(this, comp, resizeMode));
			}
			comp.addMouseListener(this);
			comp.addMouseMotionListener(this);
			comp.addKeyListener(this);
		} else {
			cells.remove(comp);
			comp.removeMouseListener(this);
			comp.removeMouseMotionListener(this);
			comp.removeKeyListener(this);
		}

		if (comp instanceof Container) {
			Container cont = (Container) comp;
			if (isGuiContainer(comp)) {
				if (set) {
					cont.addContainerListener(this);
				} else {
					cont.removeContainerListener(this);
				}
			}
			for (Component co : cont.getComponents()) {
				initListener(co, set);
			}
		}
	}

	private Cell findCell(Component parent) {
		for (Cell cell : cells.values()) {
			if (cell.comp.isVisible() && cell.contains(mx, my) && cell.comp.getParent() == parent) {

				if (isGuiContainer(cell.comp)) {
					Cell c = findCell(cell.comp);
					if (c != null) {
						return c;
					}
				}
				return cell;
			}
		}
		return null;
	}

	private Designer findContainer(int x, int y, Component parent, Component ignor) {
		for (Cell cell : cells.values()) {
			if (
				cell.comp != ignor
				&& cell.comp.isVisible()
				&& cell.comp instanceof Designer
				&& cell.contains(x, y)
				&& findDesigner(cell.comp) == parent
			) {

				Designer co = findContainer(x, y, cell.comp, ignor);
				return co == null ? (Designer) cell.comp : co;
			}
		}
		return parent == this ? this : null;
	}

	public final Designer findDesigner(Component comp) {
		Container parent = comp.getParent();
		while (parent != null) {
			if (parent instanceof Designer) {
				return (Designer) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	protected final Designer findRoot() {
		Designer root = this;
		while (!root.isRoot) {
			root = findDesigner(root);
		}
		return root;
	}

	private void initHints(int x, int y, Cell cell, Component comp, boolean rollover) {
		clearHints(false);
		Designer cont = findContainer(x, y, this, comp);
		if (cont != null) {

			Rectangle b = cell == null ? comp.getBounds() : cell.bounds;

			cont.hintComp = comp;
			cont.hintRollover = rollover;
			cont.hintPoint = translatePointTo(cont, x, y);
			cont.hintBounds = translateBoundsTo(cont, b);
		}
		repaint();
	}

	private void clearHints(boolean paint) {
		for (Cell c : cells.values()) {
			if (c.comp instanceof Designer) {
				((Designer) c.comp).clearHints(false);
			}
		}
		hintComp = null;
		hintRollover = true;
		hintBounds.setLocation(-1, -1);
		hintPoint.move(-1, -1);
		if (paint) {
			setCursor(Cursor.getDefaultCursor());
			repaint();
		}
	}

	public final void select(Component comp) {
		if (!selectionAdjusting) {

			selectionAdjusting = true;

			for (Cell cell : cells.values()) {
				cell.select(false);
			}
			if (comp != null) {
				Cell cell = cells.get(comp);
				if (cell != null) {
					cell.select(true);
				}
			}
			selectionChanged(comp);

			selectionAdjusting = false;
		}
	}

	public final Component getSelectedComponent() {
		List<Cell> cells = getSelectedCells();
		if (cells.size() == 1) {
			return cells.get(0).comp;
		}
		return null;
	}

	public final Component[] getSelectedComponents() {
		List<Cell> cells = getSelectedCells();
		Component[] comps = new Component[cells.size()];
		for (int i = 0; i < cells.size(); i++) {
			comps[i] = cells.get(i).comp;
		}
		return comps;
	}

	private Cell getSelectedCell() {
		for (Cell cell : cells.values()) {
			if (cell.selected) {
				return cell;
			}
		}
		return null;
	}

	private List<Cell> getSelectedCells() {
		List<Cell> result = new ArrayList<Cell>();
		for (Cell cell : cells.values()) {
			if (cell.selected) {
				result.add(cell);
			}
		}
		return result;
	}

	private void selectMultiple(Cell cell) {
		if (!selectionAdjusting) {
			selectionAdjusting = true;
			cell.select(!cell.selected);
			selectionChanged(null);
			selectionAdjusting = false;
		}
	}

	private void selectMarqueeing() {
		int xs = Math.min(mqXs, mqXe);
		int xe = Math.max(mqXs, mqXe);
		int ys = Math.min(mqYs, mqYe);
		int ye = Math.max(mqYs, mqYe);

		Rectangle r = new Rectangle(xs, ys, xe, ye);
		for (Cell cell : cells.values()) {
			cell.selected = r.contains(cell.bounds);
		}
	}

	private void insert(Component comp) {
		Designer cont = findContainer(mx, my, this, null);
		if (cont != null) {
			Rectangle bounds = translateBoundsTo(cont, comp.getBounds());
			if (cont.insertComponent(comp, bounds)) {
				doLayout();
				select(comp);
			}
		}
		setInsertingComponent(null);
		clearHints(true);
	}

	private void move(Cell cell) {

		Designer src = findDesigner(cell.comp);
		Designer dst = findContainer(mx, my, this, cell.comp);
		cell.comp.setBounds(translateBoundsTo(src, oldBounds));

		if (isMoveable(cell.comp) && canMove(cell.comp, dst)) {
			if (
				src.moveComponent(
					cell.comp,
					src,
					dst,
					translatePointTo(src, oldLocation.x, oldLocation.y),
					translatePointTo(dst, mx, my),
					translateBoundsTo(src, oldBounds),
					translateBoundsTo(dst, cell.bounds)
				)
			) {
				select(cell.comp);
			}
		}
		validate();
	}

	private void resize(Cell cell) {

		Designer src = findDesigner(cell.comp);
		cell.comp.setBounds(translateBoundsTo(src, oldBounds));

		if (isResizeable(cell.comp)) {
			if (
				src.resizeComponent(
					cell.comp,
					translateBoundsTo(src, oldBounds),
					translateBoundsTo(src, cell.bounds)
				)
			) {
				select(cell.comp);
			}
		}
		validate();
	}

	public final void dargHints(int x, int y, Component comp) {
		clearHints(false);
		Designer cont = findContainer(x, y, this, comp);
		Cell cell = cells.get(comp);

		if (cont != null && cell != null) {

			Rectangle b = cell.bounds;

			cont.hintComp = comp;
			cont.hintRollover = false;
			cont.hintPoint = translatePointTo(cont, x, y);
			cont.hintBounds = translateBoundsTo(cont, b);
			cont.hintBounds.setLocation(cont.hintPoint.x, cont.hintPoint.y);
		}
		repaint();
	}

	public final void dragMove(Component comp, int x, int y) {
		Designer src = findDesigner(comp);
		Designer dst = findContainer(x, y, this, comp);

		if (isMoveable(comp) && canMove(comp, dst)) {

			Cell cell = cells.get(comp);
			Point p = translatePointTo(dst, x, y);
			Rectangle r1 = translateBoundsTo(src, cell.bounds);
			Rectangle r2 = r1.getBounds();
			r2.setLocation(p.x, p.y);

			if (
				src.moveComponent(
					comp,
					src,
					dst,
					new Point(r1.x, r1.y),
					new Point(r2.x, r2.y),
					r1,
					r2
				)
			) {
				doLayout();
				select(comp);
			}
		}
	}

	public void setResizeMode(int mode) {
		resizeMode = mode;
	}

	public void setInsertingComponent(Component comp) {
		insertingComp = comp;
		paintinsertComp = false;
		for (Cell cell : cells.values()) {
			cell.setCursor(Cursor.getDefaultCursor());
		}
	}

	public void setMarqueeingEnabled(boolean b) {
		setCursor(b ? new Cursor(Cursor.CROSSHAIR_CURSOR) : Cursor.getDefaultCursor());
		marqueeingEnabled = b;
	}

	public boolean isMarqueeingEnabled() {
		return marqueeingEnabled;
	}

	public void setGridEnabled(boolean b) {
		gridEnabled = b;
	}

	public boolean isGridEnabled() {
		return gridEnabled;
	}

	public void setGridPainted(boolean b) {
		gridPainted = b;
		repaint();
	}

	public boolean isGridPainted() {
		return gridPainted;
	}

	public void setGridSize(int dx, int dy) {
		gridXd = dx;
		gridYd = dy;
	}

	private boolean isEventForMultiSelection(MouseEvent e) {
		if (multiSelection || marqueeingEnabled) {
			for (Cell cell : cells.values()) {
				if (cell.selected && cell.contains(mx, my)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean canMove(Component comp, Designer dst) {

		// do not move parent to child, this can only
		// be happened by moving via drag and drop

		Component parent = dst.getParent();
		while (parent != null) {
			if (comp == parent) {
				return false;
			}
			parent = parent.getParent();
		}
		return true;
	}

	private Point translatePointTo(Component comp, int x, int y) {
		Point p = new Point(x, y);
		while (comp != this) {
			p.translate(-comp.getX(), -comp.getY());
			comp = comp.getParent();
		}
		return p;
	}

	private Point translatePointFrom(Component comp, int x, int y) {
		Point p = new Point(x, y);
		while (comp != this) {
			p.translate(comp.getX(), comp.getY());
			comp = comp.getParent();
		}
		return p;
	}

	private Rectangle translateBoundsTo(Component comp, Rectangle bounds) {
		Point p = translatePointTo(comp, bounds.x, bounds.y);
		return new Rectangle(p.x, p.y, bounds.width, bounds.height);
	}

	private void translate(MouseEvent e) {
		Component co = e.getComponent();
		while (co != null && co != this) {
			e.translatePoint(co.getX(), co.getY());
			co = co.getParent();
		}
		mx = e.getX();
		my = e.getY();
	}

	//
	// following methods should be overwritten by subclasses
	//

	protected void paintInsertingComponent(Graphics g, Component comp) {
		comp.paint(g);
	}

	protected boolean isGuiComponent(Component comp) {
		return true;
	}

	protected boolean isGuiContainer(Component comp) {
		return true;
	}

	protected boolean isMoveable(Component comp) {
		return true;
	}

	protected boolean isResizeable(Component comp) {
		return true;
	}

	protected void selectionChanged(Component comp) {
	}

	protected void showHint(
		Graphics  g,
		Component comp,
		int		  x,
		int		  y,
		Rectangle bounds,
		boolean   rollover
	) {
	}

	protected void showPopup(Component[] comps, int x, int y) {
	}

	protected void doubleClicked(Component comp, int x, int y) {
	}

	protected boolean insertComponent(Component comp, Rectangle bounds) {
		return true;
	}

	protected boolean deleteComponent(Component comp) {
		return true;
	}

	protected boolean moveComponent(
		Component comp,
		Container oldContainer,
		Container newContainer,
		Point	  oldLocation,
		Point	  newLocation,
		Rectangle oldBounds,
		Rectangle newBounds
	) {
		return true;
	}

	protected boolean resizeComponent(Component comp, Rectangle oldBounds, Rectangle newBounds) {
		return true;
	}

	/*
	 *
	 * CELL
	 *
	 */
	private class Cell {

		private Container owner;
		private Component comp;
		private Rectangle bounds;
		private Port port;
		private Cursor cursor;

		private boolean selected = false;
		private boolean moveable = true;
		private boolean resizeable = true;

		protected Cell(Container owner, Component comp) {
			this(owner, comp, RESIZEABLE);
		}

		protected Cell(Container owner, Component comp, int resizeMode) {
			this.owner = owner;
			this.comp = comp;
			this.bounds = comp.getBounds();
			this.selected = false;
			this.port = new Port(this, resizeMode);
			this.cursor = Cursor.getDefaultCursor();
			this.moveable = true;
			this.resizeable = true;
		}

		private void paint(Graphics g) {
			initBounds();
			if (selected) {
				CellsGraphics.drawDashRect(g, bounds, Color.ORANGE);
				port.paint(g);
			}
		}

		private void initBounds() {
			if (!port.moving && !port.resizing) {
				bounds.setBounds(comp.getBounds());
				Component parent = comp.getParent();
				while (parent != null && parent != owner) {
					bounds.x += parent.getX();
					bounds.y += parent.getY();
					parent = parent.getParent();
				}
			}
			port.initBounds();
		}

		private void initComponentBounds() {
			Rectangle b = new Rectangle(bounds);
			Component parent = comp.getParent();
			while (parent != null && parent != owner) {
				b.x -= parent.getX();
				b.y -= parent.getY();
				parent = parent.getParent();
			}
			comp.setBounds(b);
		}

		private boolean contains(int x, int y) {
			return bounds.contains(x, y);
		}

		private void select(boolean s) {
			selected = s;
			port.moving = false;
			port.resizing = false;
		}

		private void setCursor(Cursor c) {
			cursor = c;
			comp.setCursor(c);
			owner.setCursor(c);
		}

		private void move(int dx, int dy) {
			bounds.setLocation(dx, dy);
			port.initBounds();
			initComponentBounds();
		}

		private void resize(int dx, int dy) {

			switch (cursor.getType()) {

				case Cursor.NW_RESIZE_CURSOR :
					int w = bounds.width - dx + bounds.x;
					int h = bounds.height - dy + bounds.y;
					if (w < 1) {
						w = 1;
					}

					if (h < 1) {
						h = 1;
					}
					bounds.setBounds(dx, dy, w, h);
					break;

				case Cursor.NE_RESIZE_CURSOR :
					w = dx - bounds.x;
					h = bounds.height - dy + bounds.y;
					if (w < 1) {
						w = 1;
					}
					if (h < 1) {
						h = 1;
					}
					bounds.setBounds(bounds.x, dy, w, h);
					break;

				case Cursor.SW_RESIZE_CURSOR :
					w = bounds.width - dx + bounds.x;
					h = dy - bounds.y;
					if (w < 1) {
						w = 1;
					}
					if (h < 1) {
						h = 1;
					}
					bounds.setBounds(dx, bounds.y, w, h);
					break;

				case Cursor.SE_RESIZE_CURSOR :
					w = dx - bounds.x;
					h = dy - bounds.y;
					if (w < 1) {
						w = 1;
					}
					if (h < 1) {
						h = 1;
					}
					bounds.setBounds(bounds.x, bounds.y, w, h);
					break;

				case Cursor.N_RESIZE_CURSOR :
					h = bounds.height - (dy - bounds.y);
					if (h < 1) {
						h = 1;
					}
					bounds.setBounds(bounds.x, dy, bounds.width, h);
					break;

				case Cursor.S_RESIZE_CURSOR :
					h = dy - bounds.y;
					if (h < 1) {
						h = 1;
					}
					bounds.setBounds(bounds.x, bounds.y, bounds.width, h);
					break;

				case Cursor.W_RESIZE_CURSOR :
					w = bounds.width - dx + bounds.x;
					if (w < 1) {
						w = 1;
					}
					bounds.setBounds(dx, bounds.y, w, bounds.height);
					break;

				case Cursor.E_RESIZE_CURSOR :
					w = dx - bounds.x;
					if (w < 1) {
						w = 1;
					}
					bounds.setBounds(bounds.x, bounds.y, w, bounds.height);
					break;
			}

			bounds.setBounds(bounds);
			port.initBounds();
			initComponentBounds();
		}

		private void mouseClicked(MouseEvent e) {
			port.mouseClicked(e);
		}

		private void mouseEntered(MouseEvent e) {
			port.mouseEntered(e);
		}

		private void mouseExited(MouseEvent e) {
			port.mouseExited(e);
		}

		private void mouseMoved(MouseEvent e) {
			port.mouseMoved(e);
		}

		private void mousePressed(MouseEvent e) {
			port.mousePressed(e);
		}

		private void mouseDragged(MouseEvent e) {
			if (selected) {
				port.mouseDragged(e);

				if (moveable && port.moving) {
					move(e.getX() - port.mx, e.getY() - port.my);

				} else if (resizeable && port.resizing) {
					resize(e.getX(), e.getY());
				}
			}
		}

		private void mouseReleased(MouseEvent e) {
			port.mouseReleased(e);
			comp.validate();
		}
	}

	/*
	 *
	 * PORT
	 *
	 */
	private class Port {

		private final int wd = 3;
		private final Color color = new Color(153, 153, 204);

		private final Cursor NW = new Cursor(Cursor.NW_RESIZE_CURSOR);
		private final Cursor NE = new Cursor(Cursor.NE_RESIZE_CURSOR);
		private final Cursor SW = new Cursor(Cursor.SW_RESIZE_CURSOR);
		private final Cursor SE = new Cursor(Cursor.SE_RESIZE_CURSOR);
		private final Cursor NO = new Cursor(Cursor.N_RESIZE_CURSOR);
		private final Cursor SO = new Cursor(Cursor.S_RESIZE_CURSOR);
		private final Cursor WE = new Cursor(Cursor.W_RESIZE_CURSOR);
		private final Cursor EA = new Cursor(Cursor.E_RESIZE_CURSOR);
		private final Cursor MO = new Cursor(Cursor.MOVE_CURSOR);
		private final Cursor HA = new Cursor(Cursor.HAND_CURSOR);
		private final Cursor DF = Cursor.getDefaultCursor();

		private final Cell cell;
		private final Cursor cursor;
		private final Rectangle bounds;

		private boolean mov, moving;
		private boolean res, resizing;
		private int cx, mx;
		private int cy, my;

		private final List<Port> ports;

		private Port(Cell cell, int resizeMode) {
			this.cell = cell;
			this.bounds = cell.bounds;
			this.ports = new ArrayList<Port>();
			this.cursor = MO;
			initPorts(resizeMode);
			initBounds();
		}

		private Port(Cell cell, Cursor cursor) {
			this.cell = cell;
			this.cursor = cursor;
			this.bounds = new Rectangle(0, 0, wd * 2, wd * 2);
			this.ports = null;
		}

		private void initPorts(int resizeMode) {
			switch (resizeMode) {

				case RESIZEABLE :
					ports.add(new Port(cell, NW));
					ports.add(new Port(cell, NE));
					ports.add(new Port(cell, SW));
					ports.add(new Port(cell, SE));
					ports.add(new Port(cell, NO));
					ports.add(new Port(cell, SO));
					ports.add(new Port(cell, WE));
					ports.add(new Port(cell, EA));
					break;

				case HORIZONTAL_RESIZEABLE :
					ports.add(new Port(cell, WE));
					ports.add(new Port(cell, EA));
					break;

				case VERTICAL_RESIZEABLE :
					ports.add(new Port(cell, NO));
					ports.add(new Port(cell, SO));
					break;
			}
		}

		private void initBounds() {
			Rectangle b = cell.bounds;
			bounds.setBounds(b);

			for (Port port : ports) {

				switch (port.cursor.getType()) {

					case Cursor.NW_RESIZE_CURSOR :
						port.bounds.setLocation(b.x - wd, b.y - wd);
						break;

					case Cursor.NE_RESIZE_CURSOR :
						port.bounds.setLocation(b.x + b.width - wd, b.y - wd);
						break;

					case Cursor.SW_RESIZE_CURSOR :
						port.bounds.setLocation(b.x - wd, b.y + b.height - wd);
						break;

					case Cursor.SE_RESIZE_CURSOR :
						port.bounds.setLocation(b.x + b.width - wd, b.y + b.height - wd);
						break;

					case Cursor.N_RESIZE_CURSOR :
						port.bounds.setLocation(b.x + b.width / 2 - wd, b.y - wd);
						break;

					case Cursor.S_RESIZE_CURSOR :
						port.bounds.setLocation(b.x + b.width / 2 - wd, b.y + b.height - wd);
						break;

					case Cursor.W_RESIZE_CURSOR :
						port.bounds.setLocation(b.x - wd, b.y + b.height / 2 - wd);
						break;

					case Cursor.E_RESIZE_CURSOR :
						port.bounds.setLocation(b.x + b.width - wd, b.y + b.height / 2 - wd);
						break;
				}
			}
		}

		private void paint(Graphics g) {
			g.setColor(color);
			for (Port port : ports) {
				g.drawImage(PORTICON.getImage(), port.bounds.x, port.bounds.y, null);
			}
		}

		private void mouseEntered(MouseEvent e) {
		}

		private void mouseExited(MouseEvent e) {
		}

		private void mouseMoved(MouseEvent e) {

			cx = e.getX();
			cy = e.getY();

			if (cell.selected) {
				for (Port port : ports) {
					if (port.bounds.contains(cx, cy)) {
						cell.setCursor(port.cursor);
						e.consume();
						return;
					}
				}

				if (bounds.contains(cx, cy)) {
					cell.setCursor(MO);
					e.consume();
					return;
				}
			}

			if (bounds.contains(cx, cy)) {
				cell.setCursor(HA);
				e.consume();
				return;
			}

			cell.setCursor(DF);
		}

		private void mousePressed(MouseEvent e) {

			cx = e.getX();
			cy = e.getY();

			for (Port port : ports) {
				if (port.bounds.contains(cx, cy)) {
					res = e.getButton() == MouseEvent.BUTTON1;
					mov = false;
					e.consume();
					return;
				}
			}

			if (bounds.contains(cx, cy)) {
				mx = e.getX() - cell.bounds.x;
				my = e.getY() - cell.bounds.y;
				mov = e.getButton() == MouseEvent.BUTTON1;
				res = false;
				e.consume();
			}
		}

		private void mouseClicked(MouseEvent e) {

			for (Port port : ports) {
				if (port.bounds.contains(cx, cy)) {
					e.consume();
					return;
				}
			}
			if (bounds.contains(cx, cy)) {
				e.consume();
			}
		}

		private void mouseDragged(MouseEvent e) {
			if (mov || res) {
				if (cx != e.getX() || cy != e.getY()) {
					moving = mov;
					resizing = res;
				}
				e.consume();
			}
		}

		private void mouseReleased(MouseEvent e) {
			if (moving || resizing) {
				e.consume();
			}
			mov = moving = false;
			res = resizing = false;
			cell.setCursor(DF);
		}
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("NewDesigner");
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);

		Designer designer = new Designer(true, null);
		TestDesigner p1 = new TestDesigner("MyPanel 1");
		TestDesigner p2 = new TestDesigner("MyPanel 2");

		JButton b1 = new JButton("Hello");
		JRadioButton b2 = new JRadioButton("Cancel");
		JLabel l1 = new JLabel("Hello World");
		JSpinner sp = new JSpinner();
		b1.setBounds(10, 10, 80, 22);
		b2.setBounds(10, 30, 100, 22);
		l1.setBounds(110, 210, 100, 22);
		sp.setBounds(110, 240, 100, 22);

		p1.setBounds(100, 50, 200, 100);
		p1.setBackground(Color.GRAY);
		p1.add(b2);
		p1.add(sp);

		p2.setBounds(300, 50, 400, 300);
		p2.setBackground(Color.LIGHT_GRAY);
		p2.add(p1);

		designer.add(b1);
		designer.add(l1);
		designer.add(p2);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(designer, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}


class TestDesigner extends Designer {

	private String title;

	public TestDesigner(String title) {
		super(false, null);
		this.title = title;
	}

	protected void showHint(Graphics g, int x, int y, Rectangle bounds, boolean rollover) {
		System.out.println(title);
	}
}
