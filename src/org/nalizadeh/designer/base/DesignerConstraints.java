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

package org.nalizadeh.designer.base;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

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
public class DesignerConstraints implements Transferable {

	private Object constraints;
	private Component component;
	private DesignerContainer container;
	private Rectangle bounds;

	public DesignerConstraints() {
		this(null, null, null);
	}

	public DesignerConstraints(Object constraints, DesignerContainer container) {
		this(constraints, null, container);
	}

	public DesignerConstraints(Object constraints, Component component, DesignerContainer container) {
		this.constraints = constraints;
		this.component = component;
		this.container = container;
		this.bounds = new Rectangle();
	}

	public DesignerConstraints(DesignerConstraints cons) {
		this.constraints = cons.constraints;
		this.component = cons.component;
		this.container = cons.container;
		this.bounds = new Rectangle(cons.bounds);
	}

	public Object getConstraints() {
		return constraints;
	}

	public void setConstraints(Object constraints) {
		this.constraints = constraints;
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public DesignerContainer getContainer() {
		return container;
	}

	public void setContainer(DesignerContainer container) {
		this.container = container;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds.setBounds(bounds);
	}

	public boolean equals(DesignerConstraints other) {
		return container == other.container //
		&& component == other.component //
		&& constraints.equals(other.constraints);
	}

	// Drag and Drop interfaces Transferable

	public static final DataFlavor CONSTRAINTS_FLAVOR =
		new DataFlavor(DesignerConstraints.class, "Constraints Type");

	private static DataFlavor[] flavors = { CONSTRAINTS_FLAVOR };

	public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
		if (!df.equals(CONSTRAINTS_FLAVOR)) {
			throw new UnsupportedFlavorException(df);
		}
		return this;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (int i = 0; i < flavors.length; i++) {
			if (flavors[i].equals(flavor)) {
				return true;
			}
		}
		return false;
	}
}
