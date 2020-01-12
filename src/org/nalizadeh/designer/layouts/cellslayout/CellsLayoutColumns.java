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

package org.nalizadeh.designer.layouts.cellslayout;

import org.nalizadeh.designer.ActionManager;

import org.nalizadeh.designer.base.Designer;

import org.nalizadeh.designer.util.CellsGraphics;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Point;

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
public class CellsLayoutColumns extends Designer {

	private CellsLayoutDesigner frame;
	private ActionManager actManager;

	private int selected = -1;
	private int hx1, hx2, hy1, hy2;

	private Icon icL = CellsUtil.getImage("FL");
	private Icon icR = CellsUtil.getImage("FR");

	private JPopupMenu popup;

        public CellsLayoutColumns(CellsLayoutContainer cont) {
                this((CellsLayoutDesigner)cont.getFrame());
        }

	public CellsLayoutColumns(CellsLayoutDesigner des) {
		super(true, null);
		setResizeMode(HORIZONTAL_RESIZEABLE);

		this.frame = des;
		this.actManager = frame.getDesigner().getActionManager();

		Action a1 =
			new AbstractAction("Insert new Column before", CellsUtil.getImage("InsertColBefore")) {
				public void actionPerformed(ActionEvent e) {
					Action action = actManager.getAction("InsertColumnBefore");
					action.putValue("Column", selected);
					action.putValue("OrigWidth", 20.0);
					action.actionPerformed(null);
				}
			};

		Action a2 =
			new AbstractAction("Insert new Column after", CellsUtil.getImage("InsertColAfter")) {
				public void actionPerformed(ActionEvent e) {
					Action action = actManager.getAction("InsertColumnAfter");
					action.putValue("Column", selected);
					action.putValue("OrigWidth", 20.0);
					action.actionPerformed(null);
				}
			};

		Action a3 =
			new AbstractAction("Delete Contents", CellsUtil.getImage("DelColContents")) {
				public void actionPerformed(ActionEvent e) {
					Action action = actManager.getAction("DeleteColumnContents");
					action.putValue("Column", selected);
					action.actionPerformed(null);
				}
			};

		Action a4 =
			new AbstractAction("Delete Column", CellsUtil.getImage("DelRowCol")) {
				public void actionPerformed(ActionEvent e) {
					Action action = actManager.getAction("DeleteColumn");
					action.putValue("Column", selected);
					action.actionPerformed(null);
				}
			};

		popup = new JPopupMenu();
		popup.add(new JMenuItem(a1));
		popup.add(new JMenuItem(a2));
		popup.add(new JMenuItem(a3));
		popup.add(new JMenuItem(a4));
	}

	public void doLayout() {
		super.doLayout();

		if (getComponents().length != 0) {
			frame.getContainer().doLayout();
			CellsLayout layout = (CellsLayout) frame.getContainer().getLayout();
			List<CellsLayout.CellColumn> cols = layout.getColumns();
			int x = layout.getHgap();
			for (int i = 0; i < cols.size(); i++) {
				CellsLayout.CellColumn col = cols.get(i);
				getComponent(i).setBounds(x, 0, col.width, CellsLayoutDesigner.ROW_HEIGHT - 1);
				x += col.width + layout.getHgap();
			}
		}
	}

	public void update(boolean force) {
		int n = ((CellsLayout) frame.getContainer().getLayout()).getColumns().size();
		if (n != getComponentCount() || force) {
			removeAll();
			for (int i = 0; i < n; i++) {
				add(new JLabel(), null);
			}
		}
		doLayout();
	}

        protected boolean isGuiComponent(Component comp) {
                return CellsUtil.isGuiComponent(comp);
        }

        protected boolean isGuiContainer(Component comp) {
                return CellsUtil.isContainer(comp);
        }

	protected void showHint(Graphics g, Component comp, int x, int y, Rectangle bounds, boolean ro) {

		int hinted = -1;
		int rollover = -1;

		if (ro) {
			rollover = getComponentZOrder(comp);
			if (rollover != -1) {
				hx1 = comp.getBounds().x + 1;
				hx2 = comp.getBounds().x + comp.getBounds().width - 6;
				hy1 = 5;
				hy2 = 5;
			}
		} else if (comp != null) {
                        hinted = getComponentZOrder(comp);
		}

		int n = 0;
		Component[] comps = getComponents();
		for (Component c : comps) {
			CellsGraphics.drawRect(g, c.getBounds(), n == selected, n == rollover, n == hinted, n);
			if (n == rollover) {
				icL.paintIcon(this, g, hx1, hy1);
				icR.paintIcon(this, g, hx2, hy2);
			}
			n++;
		}
	}

	protected final void selectionChanged(Component comp) {
		int n = getComponentZOrder(comp);
		if (n != -1) {
			Action action = actManager.getAction("SelectColumn");
			action.putValue("Column", n);
			action.actionPerformed(null);
		}
	}

        protected boolean moveComponent(
                Component comp,
                Container oldContainer,
                Container newContainer,
                Point oldLocation,
                Point newLocation,
                Rectangle oldBounds,
                Rectangle newBounds
                ) {
		Component[] comps = getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] != comp && comps[i].getBounds().contains(newLocation.x, newLocation.y)) {
				Action action = actManager.getAction("MoveColumn");
				action.putValue("SrcColumn", selected);
				action.putValue("DstColumn", i < selected ? i : i + 1);
				action.actionPerformed(null);
				return true;
			}
		}
                return false;
	}

	protected boolean resizeComponent(Component comp, Rectangle oldBounds, Rectangle newBounds) {
		Action action = actManager.getAction("ResizeColumn");
		action.putValue("Column", selected);
		action.putValue("ColumnWidth", newBounds.width);
		action.actionPerformed(null);
                return true;
	}

        protected boolean insertComponent(Component comp, Rectangle bounds) {
		return true;
	}

	protected boolean deleteComponent(Component comp) {
		return true;
	}

	protected final void showPopup(Component[] comps, int x, int y) {
		popup.show(this, x, y);
	}

	public void selectColumn(int s) {
		super.select(s == -1 ? null : getComponent(s));
		selected = s;
	}

	public int getSelectedColumn() {
		return selected;
	}
}
