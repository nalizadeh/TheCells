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

package org.nalizadeh.designer.layouts.boxlayout;

import java.awt.Component;
import java.awt.Container;

import javax.swing.BoxLayout;

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
public class BoxLayout2 extends BoxLayout {

	private Container container;
	private int axis;

	public BoxLayout2(Container container) {
		this(container, X_AXIS);
	}

	public BoxLayout2(Container container, int axis) {
		super(container, axis);
		this.container = container;
		this.axis = axis;
	}

	public void setAxis2(int axis) {
		if (this.axis != axis && (axis == X_AXIS || axis == Y_AXIS)) {
			Component[] comps = container.getComponents();
			container.removeAll();
			container.setLayout(new BoxLayout2(container, axis));
			for (Component co : comps) {
				container.add(co, null);
			}
			this.axis = axis;
		}
	}

	public int getAxis2() {
		return axis;
	}
}
