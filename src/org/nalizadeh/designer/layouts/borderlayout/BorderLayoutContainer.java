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

package org.nalizadeh.designer.layouts.borderlayout;

import org.nalizadeh.designer.base.DesignerConstraints;
import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.property.Property;

import org.nalizadeh.designer.util.CellsGraphics;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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
public class BorderLayoutContainer extends DesignerContainer {

	private List<DesignerConstraints> cells = new ArrayList<DesignerConstraints>();

	public BorderLayoutContainer(DesignerFrame frame, boolean isRoot, Container container) {
		super(frame, isRoot, new BorderLayout(4, 4), container);

		cells.add(new DesignerConstraints("North", this));
		cells.add(new DesignerConstraints("South", this));
		cells.add(new DesignerConstraints("West", this));
		cells.add(new DesignerConstraints("East", this));
		cells.add(new DesignerConstraints("Center", this));
	}

	public void doLayout() {
		super.doLayout();

		Insets in = getInsets();
		Rectangle b = getBounds();

		int w1 = b.width - in.right - in.left;
		int h1 = b.height * 10 / 100;

		int w2 = b.width * 10 / 100;
		int h2 = b.height - in.top - in.bottom - h1 * 2;

		int w3 = b.width - in.left - in.right - (w2 * 2 + 4);
		int h3 = b.height - in.top - in.bottom - (h1 * 2 + 4);

		// north, south
		cells.get(0).setBounds(new Rectangle(in.left, in.top, w1, h1));
		cells.get(1).setBounds(new Rectangle(in.left, b.height - in.bottom - h1, w1, h1));

		// west, east
		cells.get(2).setBounds(new Rectangle(in.left, in.top + h1, w2, h2));
		cells.get(3).setBounds(new Rectangle(b.width - in.right - w2, in.top + h1, w2, h2));

		// center
		cells.get(4).setBounds(new Rectangle(in.left + w2 + 2, in.top + h1 + 2, w3, h3));
	}

	public void copy(Container src, Container dst) {

		if (!(dst.getLayout() instanceof BorderLayout)) {
			dst.setLayout(new BorderLayout());
		}

		dst.removeAll();
		BorderLayout laSrc = (BorderLayout) src.getLayout();
		BorderLayout laDst = (BorderLayout) dst.getLayout();
		laDst.setHgap(laSrc.getHgap());
		laDst.setVgap(laSrc.getVgap());
		for (Component comp : src.getComponents()) {
			Object cons = laSrc.getConstraints(comp);
			src.remove(comp);
			dst.add(CellsUtil.designerOrNotDesigner(this, comp), cons);
		}
	}

	public void convert(Container src) {
		String[] constraints =
			{
				BorderLayout.CENTER, //
				BorderLayout.NORTH, //
				BorderLayout.SOUTH, //
				BorderLayout.WEST, //
				BorderLayout.EAST, //
			};

		int n = 0;
		for (Component comp : src.getComponents()) {
			src.remove(comp);
			add(comp, constraints[n++]);
			if (n == 5) {
				break;
			}
		}
	}

	protected void showHint(Graphics g, Component comp, int x, int y, Rectangle bounds, boolean rollover) {
		if (!rollover) {
			String hint = (String) getLayoutConstraints(null, x, y, bounds);
			for (DesignerConstraints cell : cells) {
				if (cell.getConstraints().equals(hint)) {
					String s = cell.getConstraints().toString();
					Rectangle b = cell.getBounds();
					g.setColor(Color.RED);
					g.drawString(s, b.x + b.width / 2 - 10, b.y + b.height / 2);
					CellsGraphics.drawCompositeRect(g, b, Color.GREEN);
				}
			}
		}
	}

	protected Component getLayoutComponent(Component comp, Rectangle bounds) {
                for (DesignerConstraints cell : cells) {
                        if (cell.getBounds().contains(bounds.x, bounds.y)) {
                                BorderLayout la = (BorderLayout) getLayout();
                                return la.getLayoutComponent(cell.getConstraints());
                        }
                }
                return null;
        }

        protected Object getLayoutConstraints(Component comp, int x, int y, Rectangle bounds) {
                if (comp != null) {
                        BorderLayout la = (BorderLayout) getLayout();
                        Object cons = la.getConstraints(comp);
                        if (cons != null) {
                                return cons;
                        }
                }
		for (DesignerConstraints cell : cells) {
			if (cell.getBounds().contains(x, y)) {
				return cell.getConstraints();
			}
		}
                return null;
	}

        protected Object cloneLayoutConstraints(Object cons) {
                return cons == null ? null : new String((String) cons);
        }

        protected void copyLayoutConstraints(Object src, Object dst) {
        }

	protected boolean resizeComponent(Component comp, Rectangle oldBounds, Rectangle newBounds) {
                return false;
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
		int hgap = (Integer) arg[2];
		int vgap = (Integer) arg[3];
		BorderLayout la = (BorderLayout) getLayout();
		la.setHgap(hgap);
		la.setVgap(vgap);
	}

	public Object[] getLayoutProperty() {
		BorderLayout layout = (BorderLayout) getLayout();
		Object[] props = new Object[6];
		props[0] = 0;
		props[1] = 1;
		props[2] = layout.getHgap();
		props[3] = layout.getVgap();
		props[4] = 0;
		props[5] = 0;
		return props;
	}

	public Property[] getConstraintsProperties(Component comp) {
		BorderLayout layout = (BorderLayout) getLayout();
		return new Property[] {
			new Property("Constraint", String.class, layout.getConstraints(comp), true, true)
		};
	}

	public boolean isConstraintsProperty(Component comp, String name) {
		return false;
	}

	public Object changeConstraintsProperty(Component comp, String name, Object value) {
		return null;
	}
}
