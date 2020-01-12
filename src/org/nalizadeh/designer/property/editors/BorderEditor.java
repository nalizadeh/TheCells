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

package org.nalizadeh.designer.property.editors;

import org.nalizadeh.designer.util.CellsBorderChooser;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

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
public class BorderEditor extends JOriginalTable.DefaultEditorComponent {

	private static final ImageIcon icon = JOriginalTable.getImage("bordereditor.gif");

	public BorderEditor() {
		super(icon);
	}

	protected Object startCustomEditor(Component parent, Object value) {
		Border b = CellsBorderChooser.showDialog(parent, (Border) value);
		return b == null ? value : (b instanceof CellsBorderChooser.NullBorder) ? null : b;
	}

	protected String convertToString(Object value) {
		if (value == null) {
			return null;
		}
		return CellsBorderChooser.getBorderName((Border) value);
	}
}
