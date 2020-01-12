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

package org.nalizadeh.designer.layouts.freelayout;

import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.property.Property;

import org.nalizadeh.designer.util.CellsGraphics;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
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
public class FreeLayoutContainer extends DesignerContainer {

	public FreeLayoutContainer(DesignerFrame frame, boolean isRoot, Container container) {
		super(frame, isRoot, new FreeLayout(), container);
		setGridEnabled(true);
	}

	public void copy(Container src, Container dst) {
		dst.removeAll();
		for (Component comp : src.getComponents()) {
			src.remove(comp);
			dst.add(CellsUtil.designerOrNotDesigner(this, comp), comp.getBounds());
		}
	}

	public void convert(Container src) {
		for (Component comp : src.getComponents()) {
			add(comp, null);
		}
	}

	protected void showHint(Graphics g, Component comp, int x, int y, Rectangle bounds, boolean rollover) {
		if (rollover) {
			return;
		}

		int xs, ys, xe, ye;

		Rectangle h = bounds;
		Rectangle b = getBounds();
		xs = h.x;
		ys = h.y;
		xe = h.x + h.width;
		ye = h.y + h.height;
		CellsGraphics.drawDashLine(g, 0, ys, b.width, ys, Color.LIGHT_GRAY);
		CellsGraphics.drawDashLine(g, 0, ye, b.width, ye, Color.LIGHT_GRAY);
		CellsGraphics.drawDashLine(g, xs, 0, xs, b.height, Color.LIGHT_GRAY);
		CellsGraphics.drawDashLine(g, xe, 0, xe, b.height, Color.LIGHT_GRAY);

		// hinting (make it slower, but it is good)
		showHinting1(g, comp, xs, ys, xe, ye);
//                showHinting2(g, xs, ys, xe, ye, x, y);

		CellsGraphics.showHintText(
			g,
			xs,
			ys - 18,
			"x:" + xs + " ,y:" + ys + " ,width:" + (xe - xs) + " ,height:" + (ye - ys)
		);
	}

	private void showHinting1(Graphics g, Component comp, int xs, int ys, int xe, int ye) {
		for (Component co : getComponents()) {
			if (co != comp) {
				Rectangle r = co.getBounds();
				int xx = r.x;
				if (xx == xs || xx == xe) {
					CellsGraphics.drawSolidLine(g, xx, r.y, xx, r.y + r.height, Color.ORANGE, 4);
				}

				xx += r.width;
				if (xx == xs || xx == xe) {
					CellsGraphics.drawSolidLine(g, xx, r.y, xx, r.y + r.height, Color.ORANGE, 4);
				}

				int yy = r.y;
				if (yy == ys || yy == ye) {
					CellsGraphics.drawSolidLine(g, r.x, yy, r.x + r.width, yy, Color.ORANGE, 4);
				}

				yy += r.height;
				if (yy == ys || yy == ye) {
					CellsGraphics.drawSolidLine(g, r.x, yy, r.x + r.width, yy, Color.ORANGE, 4);
				}
			}
		}
	}

	private void showHinting2(Graphics g, int xs, int ys, int xe, int ye, int mx, int my) {
		Component se = getSelectedComponent();

		if (se == null) {
			return;
		}

		int dist = 10;

		for (Component co : getComponents()) {
			if (co != se) {
				Rectangle r = co.getBounds();
				int xx = r.x;
				int dx = Math.abs(xx - xs);
				if (dx <= dist) {
					se.setLocation(xx, ys);
					if (dx == 0) {
						CellsGraphics.drawSolidLine(
							g,
							xx,
							r.y,
							xx,
							r.y + r.height,
							Color.ORANGE,
							4
						);
					}
				}

				xx += r.width;
				dx = Math.abs(xx - xe);
				if (dx <= dist) {
					se.setLocation(xs + dx, ys);
					if (dx == 0) {
						CellsGraphics.drawSolidLine(
							g,
							xx,
							r.y,
							xx,
							r.y + r.height,
							Color.ORANGE,
							4
						);
					}
				}

				int yy = r.y;
				int dy = Math.abs(yy - ys);
				if (dy <= dist) {
					se.setLocation(xs, yy);
					if (dy == 0) {
						CellsGraphics.drawSolidLine(g, r.x, yy, r.x + r.width, yy, Color.ORANGE, 4);
					}
				}

				yy += r.height;
				dy = Math.abs(yy - ye);
				if (dy <= dist) {
					se.setLocation(xs, ys + dy);
					if (dy == 0) {
						CellsGraphics.drawSolidLine(g, r.x, yy, r.x + r.width, yy, Color.ORANGE, 4);
					}
				}
			}
		}
	}

        protected Component getLayoutComponent(Component comp, Rectangle bounds) {
                Component c1 = getComponentAt(bounds.x, bounds.y);
                if (this != c1) {
                        Component c2 = null;
                        if (c1 instanceof JScrollPane && comp instanceof Scrollable) {
                                c2 = c1;
                                bounds.setBounds(c2.getBounds());
                        }
                        return c2;
                }
                return null;
        }

        protected Object getLayoutConstraints(Component comp, int x, int y, Rectangle bounds) {
                return x == -1 && y == -1 ? comp == null ? null : comp.getBounds() : bounds;
	}

	protected Object cloneLayoutConstraints(Object cons) {
		return new Rectangle((Rectangle) cons);
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
		Rectangle r = comp.getBounds();
		return new Property[] {
			new Property("bounds x", Integer.class, r.x, true, true),
			new Property("bounds y", Integer.class, r.y, true, true),
			new Property("bounds width", Integer.class, r.width, true, true),
			new Property("bounds height", Integer.class, r.height, true, true),
		};
	}

	public boolean isConstraintsProperty(Component comp, String name) {
		return name.equals("Bounds");
	}

	public Object changeConstraintsProperty(Component comp, String name, Object value) {
		if (name.equals("Bounds")) {
			Rectangle b = (Rectangle) value;
			Rectangle r = comp.getBounds();
			comp.setBounds(b);
			comp.setPreferredSize(new Dimension(b.width, b.height));
			return r;
		}
		return null;
	}
}
