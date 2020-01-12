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

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import org.nalizadeh.designer.property.jbeans.JBeansUtils;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
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
public class DimensionCellRenderer extends DefaultCellRenderer {

	private JPanel panel;
	private JLabel label1;
	private JLabel label2;

	private JLabel lb1 = new JLabel(JBeansUtils.NUMEDIT);
	private JLabel lb2 = new JLabel(JBeansUtils.NUMEDIT);

	public DimensionCellRenderer() {

		label1 = new JLabel();
		label2 = new JLabel();
		label1.setOpaque(true);
		label2.setOpaque(true);

		lb1.setOpaque(true);
		lb2.setOpaque(true);

		panel =
			new JPanel(
				new CellsLayout(
					new double[][] {
						{ 17 },
						{
							CellsLayout.FILL, CellsLayout.PREFERRED, 4, CellsLayout.FILL,
							CellsLayout.PREFERRED
						}
					}
				)
			);

		panel.add(label1, "0,0");
		panel.add(lb1, "0,1");
		panel.add(label2, "0,3");
		panel.add(lb2, "0,4");
	}

	public void setValue(Object value) {
		if (value != null) {
			Dimension d = (Dimension) value;
			label1.setText("" + (int) d.getWidth());
			label2.setText("" + (int) d.getHeight());
		}
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
		label1.setBackground(getBackground());
		label1.setForeground(getForeground());
		label2.setBackground(getBackground());
		label2.setForeground(getForeground());
		lb1.setBackground(getBackground());
		lb1.setForeground(getForeground());
		lb2.setBackground(getBackground());
		lb2.setForeground(getForeground());
		panel.setBackground(getBackground());
		return panel;
	}

	public Component getEditorComponent() {
		return panel;
	}
}
