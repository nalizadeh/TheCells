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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
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
public class CellsPopupComponent extends JPanel {

	private static final Color titColor = new Color(153, 188, 239);

	private JPopupMenu popup;
	private JLabel label;

	public CellsPopupComponent(String title, Component comp) {

		setLayout(new BorderLayout());

		popup = new JPopupMenu();
		popup.add(this);
		popup.setRequestFocusEnabled(true);
		popup.setFocusTraversalKeysEnabled(true);
		popup.setFocusCycleRoot(true);

		label = new JLabel();
		label.setOpaque(true);
		label.setBackground(titColor);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Dialog", 1, 12));
		label.setPreferredSize(new Dimension(200, 18));
		label.setIcon(CellsUtil.getImage("Properties"));
		label.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		label.setText(title);

		JButton button = new JButton(CellsUtil.getImage("PopupClose"));
//      button.setBackground(titColor);
		button.setFocusable(false);
		button.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					popup.setVisible(false);
				}
			}
		);

		JPanel p =
			new JPanel(
				new CellsLayout(new double[][] {
						{ CellsLayout.FILL },
						{ CellsLayout.FILL, 18 }
					}
				)
			);

		p.setPreferredSize(new Dimension(100, 18));

		p.add(label, "0,0");
		p.add(button, "0,1");

		add(p, BorderLayout.NORTH);
		add(comp, BorderLayout.CENTER);

		Dimension d = comp.getPreferredSize();
		setPreferredSize(new Dimension(d.width, d.height + 18));
	}

	public void showPopup(Component owner, int x, int y) {
		popup.show(owner, x, y);
		popup.addNotify();
	}

	public void addPopupMenuListener(PopupMenuListener l) {
		popup.addPopupMenuListener(l);
	}

	public void removePopupMenuListener(PopupMenuListener l) {
		popup.removePopupMenuListener(l);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}

		final JFrame frame = new JFrame("Example");
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);

		final CellsPopupComponent pop = new CellsPopupComponent("Insets", new JPanel());
		pop.setPreferredSize(new Dimension(200, 200));

		final JButton b = new JButton("Open Popup");
		b.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					pop.showPopup(b, 10, 10);
				}
			}
		);

		b.setBounds(50, 50, 100, 20);

		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(b);
		frame.setSize(300, 200);
		frame.setVisible(true);
	}
}
