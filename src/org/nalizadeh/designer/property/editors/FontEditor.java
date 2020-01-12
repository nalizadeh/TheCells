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

import org.nalizadeh.designer.util.CellsFontChooser;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;

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
public class FontEditor extends JOriginalTable.DefaultEditorComponent {

	private static final ImageIcon icon = JOriginalTable.getImage("fonteditor.gif");

	public FontEditor() {
		super(icon);
	}

	protected Object startCustomEditor(Component parent, Object value) {
		Font f = CellsFontChooser.showDialog(parent, (Font) value);
		return f == null ? value : f;
	}

	protected String convertToString(Object value) {
		if (value == null) {
			return null;
		}

		Font font = (Font) value;
		return font.getFamily() + "," + font.getSize() + "," + font.getStyle();
	}
}
