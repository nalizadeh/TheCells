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

package org.nalizadeh.designer;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2007 N.A.J</p>
 *
 * <p>Organization:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class SwingsList extends JScrollPane {

	private CellsDesigner designer;
	private JList list;
	private int mode;
	private boolean slectAdjusting;

	public SwingsList(CellsDesigner dsg, int m) {

		this.designer = dsg;
		this.mode = m;
		this.slectAdjusting = false;
	
		this.list = new JList(m == 1 ? CellsUtil.getComponentNames() : m == 2 ? 
				CellsUtil.getContainerNames() : CellsUtil.getBeanNames());

		list.setBackground(Color.LIGHT_GRAY);
		list.setCellRenderer(new SwingsListRenderer());
		list.addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (list.getSelectedIndex() != -1 && !slectAdjusting) {
						slectAdjusting = true;
						designer.deselectAll();
						int i = list.getSelectedIndex();
						String s = mode == 1 ? CellsUtil.getComponentNames()[i] : mode == 2 ? 
								CellsUtil.getContainerNames()[i] : CellsUtil.getBeanNames()[i];
						Action action = designer.getActionManager().getAction(s);
						action.actionPerformed(null);
						slectAdjusting = false;
					}
				}
			}
		);

		getViewport().add(list);
	}

	public void setEnabled(boolean b) {
		super.setEnabled(b);
		list.setEnabled(b);
	}

	public void clear() {
		if (!slectAdjusting) {
			list.clearSelection();
		}
	}

	class SwingsListRenderer extends JLabel implements ListCellRenderer {

		private Border b1 = BorderFactory.createEtchedBorder();
		private Border b2 = BorderFactory.createRaisedBevelBorder();
		private Dimension d = new Dimension(100, 22);
		private Font font1 = new Font("Arial", 0, 11);
		private Font font2 = new Font("Arial", 1, 11);
		private Color c1 = Color.LIGHT_GRAY;
		private Color c2 = Color.WHITE;

		public Component getListCellRendererComponent(
			JList   list,
			Object  value,
			int		index,
			boolean isSelected,
			boolean cellHasFocus
		) {
			setText((String) value);
			setIcon(CellsUtil.getImage(value.toString()));
			setBorder(isSelected ? b2 : b1);
			setForeground(Color.BLACK);
			setFont(isSelected ? font2 : font1);
			setPreferredSize(d);
			return this;
		}

		public void paint(Graphics g) {
			Rectangle r = getBounds();

			Graphics2D g2D = (Graphics2D) g;
			g2D.setPaint(new GradientPaint(0, 0, c1, r.width + 20, r.height + 20, c2, true));

			g2D.fillRect(0, 0, r.width, r.height);

			super.paint(g);
		}
	}
}
