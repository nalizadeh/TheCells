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
public class CellsLayoutRows extends Designer {

	private CellsLayoutDesigner frame;
	private ActionManager actManager;

	private int selected = -1;
	private int hx1, hx2, hy1, hy2;

	private Icon icU = CellsUtil.getImage("FU");
	private Icon icD = CellsUtil.getImage("FD");

	private JPopupMenu popup;

        public CellsLayoutRows(CellsLayoutContainer cont) {
                this((CellsLayoutDesigner)cont.getFrame());
        }

	public CellsLayoutRows(CellsLayoutDesigner des) {
		super(true, null);
		setResizeMode(VERTICAL_RESIZEABLE);

		this.frame = des;
		this.actManager = frame.getDesigner().getActionManager();

		Action a1 =
			new AbstractAction("Insert new Row before", CellsUtil.getImage("InsertRowBefore")) {
				public void actionPerformed(ActionEvent e) {
					Action action = actManager.getAction("InsertRowBefore");
					action.putValue("Row", selected);
					action.putValue("OrigHeight", 20.0);
					action.actionPerformed(null);
				}
			};

		Action a2 =
			new AbstractAction("Insert new Row after", CellsUtil.getImage("InsertRowAfter")) {
				public void actionPerformed(ActionEvent e) {
					Action action = actManager.getAction("InsertRowAfter");
					action.putValue("Row", selected);
					action.putValue("OrigHeight", 20.0);
					action.actionPerformed(null);
				}
			};

		Action a3 =
			new AbstractAction("Delete Contents", CellsUtil.getImage("DelRowContents")) {
				public void actionPerformed(ActionEvent e) {
					Action action = actManager.getAction("DeleteRowContents");
					action.putValue("Row", selected);
					action.actionPerformed(null);
				}
			};

		Action a4 =
			new AbstractAction("Delete Row", CellsUtil.getImage("DelRowCol")) {
				public void actionPerformed(ActionEvent e) {
					Action action = actManager.getAction("DeleteRow");
					action.putValue("Row", selected);
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
			List<CellsLayout.CellRow> rows = layout.getRows();
			int y = layout.getVgap();
			for (int i = 0; i < rows.size(); i++) {
				CellsLayout.CellRow row = rows.get(i);
				getComponent(i).setBounds(0, y, CellsLayoutDesigner.COL_WIDTH - 1, row.height);
				y += row.height + layout.getVgap();
			}
		}
	}

	public void update(boolean force) {
		int n = ((CellsLayout) frame.getContainer().getLayout()).getRows().size();
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
				hx1 = 5;
				hx2 = 5;
				hy1 = comp.getBounds().y + 1;
				hy2 = comp.getBounds().y + comp.getBounds().height - 7;
			}
		} else if (comp != null) {
                        hinted = getComponentZOrder(comp);
		}

		int n = 0;
		Component[] comps = getComponents();
		for (Component c : comps) {
			CellsGraphics.drawRect(g, c.getBounds(), n == selected, n == rollover, n == hinted, n);
			if (n == rollover) {
				icU.paintIcon(this, g, hx1, hy1);
				icD.paintIcon(this, g, hx2, hy2);
			}
			n++;
		}
	}

	protected final void selectionChanged(Component comp) {
		int n = getComponentZOrder(comp);
		if (n != -1) {
			Action action = actManager.getAction("SelectRow");
			action.putValue("Row", n);
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
                                Action action = actManager.getAction("MoveRow");
                                action.putValue("SrcRow", selected);
                                action.putValue("DstRow", i < selected ? i : i + 1);
                                action.actionPerformed(null);
                                return true;
                        }
                }
                return false;
        }

	protected boolean resizeComponent(Component comp, Rectangle oldBounds, Rectangle newBounds) {
		Action action = actManager.getAction("ResizeRow");
		action.putValue("Row", selected);
		action.putValue("RowHeight", newBounds.height);
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

	public void selectRow(int s) {
		super.select(s == -1 ? null : getComponent(s));
		selected = s;
	}

	public int getSelectedRow() {
		return selected;
	}
}
