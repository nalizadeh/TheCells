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

package org.nalizadeh.designer.layouts.gridbaglayout;

import org.nalizadeh.designer.base.DesignerConstraints;
import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.property.Property;

import org.nalizadeh.designer.util.CellsGraphics;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

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
public class GridBagLayoutContainer extends DesignerContainer {

	public GridBagLayoutContainer(DesignerFrame frame, boolean isRoot, Container container) {
		super(frame, isRoot, new GridBagLayout(), container);
	}

	public void copy(Container src, Container dst) {
		dst.removeAll();
		GridBagLayout la = (GridBagLayout) src.getLayout();
		for (Component comp : src.getComponents()) {
			GridBagConstraints cons = la.getConstraints(comp);
			src.remove(comp);
			dst.add(CellsUtil.designerOrNotDesigner(this, comp), cons);
		}
	}

	public void convert(Container src) {
		int x = 0;
		for (Component comp : src.getComponents()) {
			add(
				comp,
				new GridBagConstraints(
					x++,
					0,
					1,
					1,
					0,
					0,
					GridBagConstraints.CENTER,
					GridBagConstraints.NONE,
					new Insets(0, 0, 0, 0),
					0,
					0
				)
			);
		}
	}

	protected void showHint(Graphics g, Component comp, int x, int y, Rectangle bounds, boolean rollover) {

		if (!rollover && getComponentCount() > 0) {

			GridBagLayout la = (GridBagLayout) getLayout();
			int[][] dat = la.getLayoutDimensions();
                        Point p = la.getLayoutOrigin();

			int w = getWidth();
			int h = getHeight();
			int[] wi = dat[0];
			int[] hi = dat[1];
                        int xs = p.x;
                        int ys = p.y;

			CellsGraphics.drawDashLine(g, xs, 0, xs, h, Color.LIGHT_GRAY);
			CellsGraphics.drawDashLine(g, 0, ys, w, ys, Color.LIGHT_GRAY);

			for (int i = 0; i < wi.length; i++) {
				xs += wi[i];
				CellsGraphics.drawDashLine(g, xs, 0, xs, h, Color.LIGHT_GRAY);
			}
			for (int i = 0; i < hi.length; i++) {
				ys += hi[i];
				CellsGraphics.drawDashLine(g, 0, ys, w, ys, Color.LIGHT_GRAY);
			}

                        GridBagConstraints cons = (GridBagConstraints)getLayoutConstraints(null, x, y, null);
                        if (cons != null) {
                                CellsGraphics.showHintText(g, x + 10, y + 10,
                                        "gridx = " + cons.gridx + " gridy = " + cons.gridy);
                        }
		}
	}

        protected Component getLayoutComponent(Component comp, Rectangle bounds) {
                GridBagLayout la = (GridBagLayout) getLayout();
                Point p = la.location(bounds.x, bounds.y);
                for (Component co : getComponents()) {
                        GridBagConstraints ct = la.getConstraints(co);
                        if (ct.gridx == p.x && ct.gridy == p.y) {
                                if (p.x == 0) {
                                        shiftColumns();
                                        return null;
                                }
                                if (p.y == 0) {
                                        shiftRows();
                                        return null;
                                }
                                return null;
                        }
                }
                return null;
        }

        protected Object getLayoutConstraints(Component comp, int x, int y, Rectangle bounds) {
		GridBagLayout la = (GridBagLayout) getLayout();
                GridBagConstraints cons = comp == null ? new GridBagConstraints() : la.getConstraints(comp);
                Point p = la.location(x, y);
                cons.gridx = p.x;
                cons.gridy = p.y;
                return cons;
	}

	protected Object cloneLayoutConstraints(Object cons) {
		if (cons != null) {
			GridBagConstraints src = (GridBagConstraints) cons;
			return new GridBagConstraints(
				src.gridx,
				src.gridy,
				src.gridwidth,
				src.gridheight,
				src.weightx,
				src.weighty,
				src.anchor,
				src.fill,
				src.insets,
				src.ipadx,
				src.ipady
			);
		}
		return null;
	}

	public void copyLayoutConstraints(Object src, Object dst) {
		GridBagConstraints csrc = (GridBagConstraints) src;
		GridBagConstraints cdst = (GridBagConstraints) dst;
		cdst.gridwidth = csrc.gridwidth;
		cdst.gridheight = csrc.gridheight;
		cdst.weightx = csrc.weightx;
		cdst.weighty = csrc.weighty;
		cdst.anchor = csrc.anchor;
		cdst.fill = csrc.fill;
		cdst.insets = csrc.insets;
		cdst.ipadx = csrc.ipadx;
		cdst.ipady = csrc.ipady;
	}

	protected boolean resizeComponent(Component comp, Rectangle oldBounds, Rectangle newBounds) {
		int w = newBounds.width - comp.getWidth();
		int h = newBounds.height - comp.getHeight();
		return doResizeComponent(new DesignerConstraints(null, comp, this), new Point(w, h), null);
	}

        private void shiftRows() {
                GridBagLayout la = (GridBagLayout) getLayout();
                for (Component co : getComponents()) {
                        GridBagConstraints ct = la.getConstraints(co);
                        ct.gridy++;
                        remove(co);
                        add(co, ct);
                }
        }

        private void shiftColumns() {
                GridBagLayout la = (GridBagLayout) getLayout();
                for (Component co : getComponents()) {
                        GridBagConstraints ct = la.getConstraints(co);
                        ct.gridx++;
                        remove(co);
                        add(co, ct);
                }
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
	}

	public Object[] getLayoutProperty() {
		Object[] props = new Object[6];
		props[0] = 0;
		props[1] = 0;
		props[2] = 0;
		props[3] = 0;
		props[4] = 0;
		props[5] = 0;
		return props;
	}

	public Property[] getConstraintsProperties(Component comp) {

		GridBagLayout la = (GridBagLayout) getLayout();
		GridBagConstraints con = la.getConstraints(comp);

		return new Property[] {
			new Property("Grid X", Integer.class, con.gridx, false, true),
			new Property("Grid Y", Integer.class, con.gridy, false, true),
			new Property("Grid Width", Integer.class, con.gridwidth, false, true),
			new Property("Grid Height", Integer.class, con.gridheight, false, true),
			new Property("Fill", Integer.class, con.fill, false, true),
			new Property("Internal Padding X", Integer.class, con.ipadx, false, true),
			new Property("Internal Padding Y", Integer.class, con.ipady, false, true),
			new Property("Anchor", Integer.class, con.anchor, false, true),
			new Property("Weight X", Double.class, con.weightx, false, true),
			new Property("Weight Y", Double.class, con.weighty, false, true),
			new Property("Insets", Insets.class, con.insets, false, true)
		};
	}

	public boolean isConstraintsProperty(Component comp, String name) {
		return name.equals("Grid X") || name.equals("Grid Y") || name.equals("Grid Width")
		|| name.equals("Grid Height") || name.equals("Internal Padding X")
		|| name.equals("Internal Padding Y") || name.equals("Anchor") || name.equals("Weight X")
		|| name.equals("Weight Y") || name.equals("Insets") || name.equals("Fill")
		|| name.equals("Resizing");
	}

	public Object changeConstraintsProperty(Component comp, String name, Object value) {
		Object oldValue = null;

		GridBagLayout la = (GridBagLayout) getLayout();
		GridBagConstraints con = la.getConstraints(comp);

		boolean b = true;

		if (name.equals("Grid X")) {
			oldValue = con.gridx;
			con.gridx = (Integer) value;
		} else if (name.equals("Grid Y")) {
			oldValue = con.gridy;
			con.gridy = (Integer) value;
		} else if (name.equals("Grid Width")) {
			oldValue = con.gridwidth;
			con.gridwidth = (Integer) value;
		} else if (name.equals("Grid Height")) {
			oldValue = con.gridheight;
			con.gridheight = (Integer) value;
		} else if (name.equals("Fill")) {
			oldValue = con.fill;
			con.fill = (Integer) value;
		} else if (name.equals("Internal Padding X")) {
			oldValue = con.ipadx;
			con.ipadx = (Integer) value;
		} else if (name.equals("Internal Padding Y")) {
			oldValue = con.ipady;
			con.ipady = (Integer) value;
		} else if (name.equals("Anchor")) {
			oldValue = con.anchor;
			con.anchor = (Integer) value;
		} else if (name.equals("Weight X")) {
			oldValue = con.weightx;
			con.weightx = (Double) value;
		} else if (name.equals("Weight Y")) {
			oldValue = con.weighty;
			con.weighty = (Double) value;
		} else if (name.equals("Insets")) {
			oldValue = con.insets;
			con.insets = (Insets) value;
		} else if (name.equals("Resizing")) {

			Point p = (Point) value;
			con.ipadx += p.x;
			con.ipady += p.y;

			oldValue = new Point(-p.x, -p.y);

			if (con.ipadx < 0) {
				con.ipadx = 0;
			}

			if (con.ipady < 0) {
				con.ipady = 0;
			}
		} else {
			b = false;
		}
		if (b) {
			la.setConstraints(comp, con);
		}

		return oldValue;
	}
}
