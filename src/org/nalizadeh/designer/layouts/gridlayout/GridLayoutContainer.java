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

package org.nalizadeh.designer.layouts.gridlayout;

import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.property.Property;

import org.nalizadeh.designer.util.CellsGraphics;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

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
public class GridLayoutContainer extends DesignerContainer {

	public GridLayoutContainer(DesignerFrame frame, boolean isRoot, Container container) {
		super(frame, isRoot, new GridLayout(), container);
	}

	public void copy(Container src, Container dst) {
		dst.removeAll();
		GridLayout la = (GridLayout) src.getLayout();
		GridLayout lb = (GridLayout) dst.getLayout();
		lb.setRows(la.getRows());
		lb.setColumns(la.getColumns());
		lb.setHgap(la.getHgap());
		lb.setVgap(la.getVgap());
		for (Component comp : src.getComponents()) {
			src.remove(comp);
			dst.add(CellsUtil.designerOrNotDesigner(this, comp), null);
		}
	}

	public void convert(Container src) {
		for (Component comp : src.getComponents()) {
			add(comp, null);
		}
	}

	protected void showHint(
		Graphics  g,
		Component comp,
		int		  x,
		int		  y,
		Rectangle bounds,
		boolean   rollover
	) {
		if (!rollover) {
			int hint = getZOrder(comp, x, y);
			if (hint != -1) {
				Component hc = getComponent(hint);

				for (Component co : getComponents()) {
					if (co == hc) {
						Rectangle b = co.getBounds();
						CellsGraphics.drawCompositeRect(g, b, Color.GREEN);
						CellsGraphics.showHintText(g, x, y, "index = " + hint);
						break;
					}
				}
			}
		}
	}

	protected Component getLayoutComponent(Component comp, Rectangle bounds) {
		Component c1 = getComponentAt(bounds.x, bounds.y);
		Component c2 = null;
		if (c1 instanceof JScrollPane && comp instanceof Scrollable) {
			c2 = c1;
		}
		return c2;
	}

	protected Object getLayoutConstraints(Component comp, int x, int y, Rectangle bounds) {
                Component co = x == -1 && y == -1 ? comp : getComponentAt(x, y);
                Object rc = co == null || co == this ? null : getComponentZOrder(co);
                return rc == null ? getComponentCount() : rc;
	}

	protected Object cloneLayoutConstraints(Object cons) {
		return cons == null ? null : new Integer((Integer) cons);
	}

	protected void copyLayoutConstraints(Object src, Object dst) {
	}

	//==================================================================
	// PropertySheet Implementation
	//==================================================================

	public void updateSheets() {
	}

	public JPanel createRowSheet() {
		return null;
	}

	public JPanel createColumnSheet() {
		return null;
	}

	public void updateLayout(Object... arg) {
		GridLayout layout = (GridLayout) getLayout();
		layout.setRows((Integer) arg[0]);
		layout.setColumns((Integer) arg[1]);
		layout.setHgap((Integer) arg[2]);
		layout.setVgap((Integer) arg[3]);
	}

	public Object[] getLayoutProperty() {
		GridLayout layout = (GridLayout) getLayout();
		Object[] props = new Object[6];
		props[0] = layout.getRows();
		props[1] = layout.getColumns();
		props[2] = layout.getHgap();
		props[3] = layout.getVgap();
		props[4] = 0;
		props[5] = 0;
		return props;
	}

	public Property[] getConstraintsProperties(Component comp) {
		return new Property[] {
			new Property("Index", String.class, getComponentZOrder(comp), true, true)
		};
	}

	public boolean isConstraintsProperty(Component comp, String name) {
		return false;
	}

	public Object changeConstraintsProperty(Component comp, String name, Object value) {
		return null;
	}
}
