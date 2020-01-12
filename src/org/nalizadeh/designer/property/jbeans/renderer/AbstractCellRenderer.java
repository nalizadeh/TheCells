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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

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
public abstract class AbstractCellRenderer extends DefaultTableCellRenderer {

	private static final Color oddBackgroundColor = new Color(242, 240, 236);
	private static final Color evenBackgroundColor = new Color(234, 232, 228);

	private static final Font font = new Font("Dialog", 0, 12);

	private boolean showOddAndEvenRows = true;

	public void setShowOddAndEvenRows(boolean b) {
		showOddAndEvenRows = b;
	}

	public Component getTableCellRendererComponent(
		JTable  table,
		Object  value,
		boolean isSelected,
		boolean hasFocus,
		int		row,
		int		column
	) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (showOddAndEvenRows && !isSelected) {
			if (row % 2 == 0) {
				setBackground(oddBackgroundColor);
			} else {
				setBackground(evenBackgroundColor);
			}
		}

		setFont(font);

		setValue(value);
		return this;
	}
}
