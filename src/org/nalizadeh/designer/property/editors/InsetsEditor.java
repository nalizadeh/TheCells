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

import org.nalizadeh.designer.util.CellsInsetsChooser;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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
public class InsetsEditor extends JOriginalTable.DefaultEditorComponent implements PopupMenuListener {

	private CellsInsetsChooser chooser = new CellsInsetsChooser();

	protected Object startCustomEditor(Component parent, Object value) {
		chooser.open(this, (Insets) value, 5, 20);
		return value;
	}

	protected boolean isModalCustomEditor() {
		return false;
	}

	protected String convertToString(Object value) {
		if (value instanceof Insets) {
			Insets in = (Insets) value;
			return in == null ? "" : (in.top + "," + in.left + "," + in.bottom + "," + in.right);
		}
		return null;
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		setEditorValue(chooser.getValue());
		fireEditingStopped(this);
	}

	public void popupMenuCanceled(PopupMenuEvent e) {
	}
}
