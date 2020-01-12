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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
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
public class DefaultCellRenderer extends AbstractCellRenderer {

	private JPanel panel;
	private JButton button;
	private ActionListener listener;

	public DefaultCellRenderer() {

		panel = new JPanel(new BorderLayout(0, 0));
		panel.setOpaque(false);
		panel.add(this, BorderLayout.CENTER);

		Icon editorIcon = getEditorIcon();

		if (editorIcon != null) {

			button = new JButton();
			button.setBorder(null);
			button.setIcon(editorIcon);
			button.setPreferredSize(new Dimension(16, 30));
			button.setMargin(new Insets(0, 0, 0, 0));
			button.setFocusable(false);
			button.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (listener != null) {
							listener.actionPerformed(e);
						}
					}
				}
			);
			panel.add(button, BorderLayout.EAST);
		}
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (button != null) {
			if (!enabled) {
				panel.remove(button);
			} else {
				panel.add(button, BorderLayout.EAST);
			}
		}
	}

	protected Icon getEditorIcon() {
		return JBeansUtils.TEXTEDIT;
	}

	protected void setEditorIcon(Icon icon) {
		if (button != null) {
			button.setIcon(icon);
		}
	}

	public void setActionListener(ActionListener listener) {
		this.listener = listener;
	}

	public Component getEditorComponent() {
		return panel;
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
		if (button != null) {
			button.setBackground(getBackground());
		}
		return panel;
	}

	public void setValue(Object value) {
		String text = convertToString(value);
		Icon icon = convertToIcon(value);

		setText(text == null ? "" : text);
		setIcon(icon);
	}

	protected String convertToString(Object value) {
		return value == null ? "" : value.toString();
	}

	protected Icon convertToIcon(Object value) {
		return null;
	}
}
