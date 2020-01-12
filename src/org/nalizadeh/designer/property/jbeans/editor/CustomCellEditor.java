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

package org.nalizadeh.designer.property.jbeans.editor;

import org.nalizadeh.designer.property.jbeans.renderer.DefaultCellRenderer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public abstract class CustomCellEditor extends DefaultCellEditor implements ActionListener {

	private Object value;
	private DefaultCellRenderer renderer;

	public CustomCellEditor(DefaultCellRenderer renderer) {
		this.renderer = renderer;
		this.renderer.setActionListener(this);
		this.editor = renderer;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		renderer.setValue(value);
	}

	public Component getCustomEditor() {
		return renderer.getEditorComponent();
	}

	public void actionPerformed(ActionEvent e) {

		Object newValue = startCustomEditor(value);

		if (newValue != null) {
			Object oldValue = value;
			setValue(newValue);
			firePropertyChange(oldValue, newValue);
		}
	}

	protected abstract Object startCustomEditor(Object value);
}
