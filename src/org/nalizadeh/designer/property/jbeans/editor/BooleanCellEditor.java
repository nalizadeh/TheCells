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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

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
public class BooleanCellEditor extends DefaultCellEditor {

	public BooleanCellEditor() {
		JCheckBox cb = new JCheckBox();
		cb.setOpaque(false);
		cb.setBorder(null);
		cb.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					firePropertyChange(
						((JCheckBox) editor).isSelected() ? Boolean.FALSE : Boolean.TRUE,
						((JCheckBox) editor).isSelected() ? Boolean.TRUE : Boolean.FALSE
					);

					editor.transferFocus();
				}
			}
		);

		editor = cb;
	}

	public Object getValue() {
		return ((JCheckBox) editor).isSelected() ? Boolean.TRUE : Boolean.FALSE;
	}

	public void setValue(Object value) {
		((JCheckBox) editor).setSelected(Boolean.TRUE.equals(value));
	}
}
