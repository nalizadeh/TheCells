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

import javax.swing.JLabel;
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
public class NumberCellEditor extends DefaultCellEditor {

	private SpinnerNumberModel model;
	private Object oldValue;

	public NumberCellEditor() {
		this(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));

	}

	public NumberCellEditor(SpinnerNumberModel model) {
		this.model = model;

		JSpinner sp =
			new JSpinner(model) {
				public void setValue(Object value) {
					oldValue = getValue();
					super.setValue(value);
				}
			};

		sp.addChangeListener(
			new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					firePropertyChange(oldValue, getValue());
					editor.transferFocus();
				}
			}
		);

		sp.setBorder(null);
		sp.setFocusable(false);
		JSpinner.DefaultEditor ed = (JSpinner.DefaultEditor) sp.getEditor();
		ed.getTextField().setHorizontalAlignment(JLabel.LEFT);

		editor = sp;
	}

	public void setMinimum(Comparable min) {
		model.setMinimum(min);
	}

	public void setMaximum(Comparable max) {
		model.setMaximum(max);
	}

	public Object getValue() {
		return ((JSpinner) editor).getValue();
	}

	public void setValue(Object value) {
		((JSpinner) editor).setValue(value);
	}

	public static class ShortCellEditor extends NumberCellEditor {
		public ShortCellEditor() {
			super(new SpinnerNumberModel(0, Short.MIN_VALUE, Short.MAX_VALUE, 1));
		}
	}

	public static class IntegerCellEditor extends NumberCellEditor {
		public IntegerCellEditor() {
			super(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
		}
	}

	public static class LongCellEditor extends NumberCellEditor {
		public LongCellEditor() {
			super(new SpinnerNumberModel(0, Long.MIN_VALUE, Long.MAX_VALUE, 1));
		}
	}

	public static class DoubleCellEditor extends NumberCellEditor {
		public DoubleCellEditor() {
			super(new SpinnerNumberModel(0, Double.MIN_VALUE, Double.MAX_VALUE, 1));
		}
	}

	public static class FloatCellEditor extends NumberCellEditor {
		public FloatCellEditor() {
			super(new SpinnerNumberModel(0.0, Float.MIN_VALUE, Float.MIN_VALUE, 1.0));
		}
	}
}
