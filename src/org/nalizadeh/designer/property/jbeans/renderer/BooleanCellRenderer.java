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

package org.nalizadeh.designer.property.jbeans.renderer;

import org.nalizadeh.designer.property.jbeans.JBeansUtils;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTable;

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
public class BooleanCellRenderer extends DefaultCellRenderer {

	protected Icon getEditorIcon() {
		return null;
	}

	public Component getTableCellRendererComponent(
		JTable  table,
		Object  value,
		boolean isSelected,
		boolean hasFocus,
		int		row,
		int		column
	) {
		setEditorIcon(Boolean.TRUE.equals(value) ? JBeansUtils.BOOLEDIT2 : JBeansUtils.BOOLEDIT1);
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	protected String convertToString(Object value) {
		if (value == null) {
			return null;
		}
		return Boolean.TRUE.equals(value) ? "true" : "false";
	}

	protected Icon convertToIcon(Object value) {
		if (value == null) {
			return null;
		}

		return Boolean.TRUE.equals(value) ? JBeansUtils.CHECKBOX1 : JBeansUtils.CHECKBOX2;
	}
}
