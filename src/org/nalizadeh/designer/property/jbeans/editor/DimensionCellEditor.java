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

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
public class DimensionCellEditor extends DefaultCellEditor {

	private JSpinner sp1;
	private JSpinner sp2;
	private Object oldValue;

	public DimensionCellEditor() {
		sp1 =
			new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1)) {
				public void setValue(Object value) {
					oldValue = getValue();
					super.setValue(value);
				}
			};

		sp2 =
			new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1)) {
				public void setValue(Object value) {
					oldValue = getValue();
					super.setValue(value);
				}
			};

		sp1.addChangeListener(
			new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					firePropertyChange(oldValue, getValue());
					sp1.transferFocus();
				}
			}
		);

		sp2.addChangeListener(
			new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					firePropertyChange(oldValue, getValue());
					sp2.transferFocus();
				}
			}
		);

		sp1.setBorder(null);
		sp1.setFocusable(false);
		sp2.setBorder(null);
		sp2.setFocusable(false);

		JSpinner.DefaultEditor ed1 = (JSpinner.DefaultEditor) sp1.getEditor();
		ed1.getTextField().setHorizontalAlignment(JLabel.LEFT);

		JSpinner.DefaultEditor ed2 = (JSpinner.DefaultEditor) sp2.getEditor();
		ed2.getTextField().setHorizontalAlignment(JLabel.LEFT);

		JPanel panel =
			new JPanel(
				new CellsLayout(new double[][] {
						{ 16 },
						{ CellsLayout.FILL, CellsLayout.FILL }
					}
				)
			);

		panel.add(sp1, "0,0");
		panel.add(sp2, "0,1");

		editor = panel;
	}

	public Object getValue() {
		return new Dimension((Integer) sp1.getValue(), (Integer) sp2.getValue());
	}

	public void setValue(Object value) {
		Dimension d = (Dimension) value;
		sp1.setValue(new Double(d.getWidth()).intValue());
		sp2.setValue(new Double(d.getHeight()).intValue());
	}
}
