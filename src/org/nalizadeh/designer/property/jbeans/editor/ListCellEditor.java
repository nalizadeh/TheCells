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

import org.nalizadeh.designer.property.jbeans.renderer.ListCellRenderer;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
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
public class ListCellEditor extends DefaultCellEditor {

	private ListCellRenderer renderer;
	private Object oldValue;

	public ListCellEditor(ListCellRenderer renderer) {
		this.renderer = renderer;

		JComboBox co =
			new JComboBox(renderer.getItems()) {
				public void setSelectedItem(Object anObject) {
					oldValue = getSelectedItem();
					super.setSelectedItem(anObject);
				}
			};

		co.setRenderer(new Renderer());
		co.addPopupMenuListener(
			new PopupMenuListener() {
				public void popupMenuCanceled(PopupMenuEvent e) {
				}

				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					changeProperty();
				}

				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				}
			}
		);

		co.addKeyListener(
			new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						changeProperty();
					}
				}
			}
		);

		editor = co;
	}

	public Object getValue() {
		return renderer.translate(((JComboBox) editor).getSelectedItem());
	}

	public void setValue(Object value) {
		JComboBox combo = (JComboBox) editor;
		Object current = null;
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = combo.getModel().getElementAt(i);
			if (value == current || (current != null && current.equals(value))) {
				((JComboBox) editor).setSelectedIndex(i);
				break;
			}
		}
	}

	private void changeProperty() {
		Object value = getValue();
		if (value != null) {
			firePropertyChange(oldValue, value);
		}
	}

	public class Renderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(
			JList   list,
			Object  value,
			int		index,
			boolean isSelected,
			boolean cellHasFocus
		) {
			Icon[] icons = renderer.getIcons();

			Component component =
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (icons != null && index >= 0 && component instanceof JLabel) {
				((JLabel) component).setIcon(icons[index]);
			}
			return component;
		}
	}
}
