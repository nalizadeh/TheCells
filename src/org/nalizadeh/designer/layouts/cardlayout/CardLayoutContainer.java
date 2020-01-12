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

package org.nalizadeh.designer.layouts.cardlayout;

import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.property.Property;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;

import javax.swing.JPanel;
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
public class CardLayoutContainer extends DesignerContainer {

	public CardLayoutContainer(DesignerFrame frame, boolean isRoot, Container container) {
		super(frame, isRoot, new CardLayout(), container);
	}

	public void remove(Component comp) {
		comp.setVisible(true);
		super.remove(comp);
	}

	public void copy(Container src, Container dst) {

		dst.removeAll();
		CardLayout la1 = (CardLayout) src.getLayout();
		CardLayout la2 = (CardLayout) dst.getLayout();
		la2.setHgap(la1.getHgap());
		la2.setVgap(la1.getVgap());

		int n = 0;

		for (Component comp : src.getComponents()) {
			src.remove(comp);
			dst.add(CellsUtil.designerOrNotDesigner(this, comp), "card " + n++);
		}
	}

	public void convert(Container src) {
		for (Component comp : src.getComponents()) {
			add(comp, "");
		}
	}

        protected Component getLayoutComponent(Component comp, Rectangle bounds) {
                return null;
        }

        protected Object getLayoutConstraints(Component comp, int x, int y, Rectangle bounds) {
                return comp == null ? null : getComponentZOrder(comp) + "";
        }

	protected Object cloneLayoutConstraints(Object cons) {
		return cons;
	}

	protected void copyLayoutConstraints(Object src, Object dst) {
	}

	protected void update() {

		Component comp = getSelectedComponent();
                if (comp != null && comp.getParent() == this) {

                        CardLayout la = (CardLayout) getLayout();
                        la.first(this);
                        int z = getComponentZOrder(comp);
                        while (z > 0) {
                                la.next(this);
                                z--;
                        }
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
                return false;
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
		CardLayout la = (CardLayout) getLayout();
		la.setHgap((Integer) arg[2]);
		la.setVgap((Integer) arg[3]);
	}

	public Object[] getLayoutProperty() {
		CardLayout layout = (CardLayout) getLayout();
		Object[] props = new Object[6];
		props[0] = 0;
		props[1] = 0;
		props[2] = layout.getHgap();
		props[3] = layout.getVgap();
		props[4] = 0;
		props[5] = 0;
		return props;
	}

	public Property[] getConstraintsProperties(Component comp) {
		return new Property[] {
			new Property("Card", String.class, getComponentZOrder(comp), true, true)
		};
	}

	public boolean isConstraintsProperty(Component comp, String name) {
		return false;
	}

	public Object changeConstraintsProperty(Component comp, String name, Object value) {
		return null;
	}
}
