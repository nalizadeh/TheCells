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

package org.nalizadeh.designer.util;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import java.awt.Component;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
public class CellsInsetsChooser extends JPanel {

	private JSpinner top = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
	private JSpinner left = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
	private JSpinner bottom = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
	private JSpinner right = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));

	public CellsInsetsChooser() {
		super(
			new CellsLayout(
				new double[][] {
					{ 22, 22, 22, 22 },
					{ CellsLayout.PREFERRED, 50 }
				},
				4,
				4
			)
		);

		add(new JLabel("Top:"), "0,0");
		add(top, "0,1");
		add(new JLabel("Left:"), "1,0");
		add(left, "1,1");
		add(new JLabel("Bottom:"), "2,0");
		add(bottom, "2,1");
		add(new JLabel("Right:"), "3,0");
		add(right, "3,1");
	}

	public Insets getValue() {
		return new Insets(
			(Integer) top.getValue(),
			(Integer) left.getValue(),
			(Integer) bottom.getValue(),
			(Integer) right.getValue()
		);
	}

	public void open(Component owner, Insets insets, int x, int y) {

		top.setValue(insets.top);
		left.setValue(insets.left);
		bottom.setValue(insets.bottom);
		right.setValue(insets.right);

		CellsPopupComponent popup = new CellsPopupComponent("Insets", this);
		popup.addPopupMenuListener((PopupMenuListener) owner);
		popup.showPopup(owner, x, y);
	}
}
