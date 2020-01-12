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

import org.nalizadeh.designer.base.DesignerContainer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
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
public class CellsTextPopup implements PopupMenuListener {

	private DesignerContainer owner;
	private Component comp;
	private JTextField tf = new JTextField();
	private JPopupMenu popup = new JPopupMenu();
	private String oldText;

	public CellsTextPopup(DesignerContainer owner) {

		this.owner = owner;

		tf.setPreferredSize(new Dimension(100, 22));
		tf.setBorder(null);
		tf.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					popup.setVisible(false);
				}
			}
		);

		tf.registerKeyboardAction(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					popup.setVisible(false);
				}
			},
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
			JComponent.WHEN_FOCUSED
		);

		tf.registerKeyboardAction(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					tf.setText(oldText);
					popup.setVisible(false);
				}
			},
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_FOCUSED
		);

		popup.add(tf);
		popup.setBorder(null);
		popup.setRequestFocusEnabled(true);
		popup.setFocusTraversalKeysEnabled(true);
		popup.setFocusCycleRoot(true);
		popup.setSize(new Dimension(100, 22));
		popup.addPopupMenuListener(this);
	}

	public void show(Component comp, int x, int y) {
		oldText = CellsUtil.getTextOf(comp);
		if (oldText != null) {

			this.comp = comp;

			tf.setText(oldText);
			tf.selectAll();

			popup.show(owner, x, y);
			popup.addNotify();
			tf.selectAll();
			tf.requestFocusInWindow();
		}
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		CellsUtil.setTextOf(comp, tf.getText(), owner.getFrame());
		owner.getFrame().getDesigner().redraw();
	}

	public void popupMenuCanceled(PopupMenuEvent e) {
	}
}
