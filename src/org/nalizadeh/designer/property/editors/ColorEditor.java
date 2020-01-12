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

package org.nalizadeh.designer.property.editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.nalizadeh.designer.util.otable.JOriginalTable;

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
public class ColorEditor extends JOriginalTable.DefaultEditorComponent {

	private static final ImageIcon icon = JOriginalTable.getImage("coloreditor.gif");

	public ColorEditor() {
		super(icon);
	}

	protected String convertToString(Object value) {
		if (value == null) {
			return null;
		}

		Color color = (Color) value;
		return "R:" + color.getRed() + " G:" + color.getGreen() + " B:" + color.getBlue() + " - "
		+ toHex(color);
	}

	protected Icon convertToIcon(Object value) {
		if (value == null) {
			return null;
		}

		return new ColorIcon((Color) value);
	}

	protected Object startCustomEditor(Component parent, Object value) {
		Window window = SwingUtilities.windowForComponent(parent);
		Color c = JColorChooser.showDialog(window, "pick a color", (Color) value);
		return c == null ? value : c;
	}

	private String toHex(Color color) {
		String red = Integer.toHexString(color.getRed());
		String green = Integer.toHexString(color.getGreen());
		String blue = Integer.toHexString(color.getBlue());

		if (red.length() == 1) {
			red = "0" + red;
		}
		if (green.length() == 1) {
			green = "0" + green;
		}
		if (blue.length() == 1) {
			blue = "0" + blue;
		}
		return ("#" + red + green + blue).toUpperCase();
	}

	public static class ColorIcon implements Icon {
		private Color color;
		private int width;
		private int height;

		public ColorIcon(Color color) {
			this(color, 10, 10);
		}

		public ColorIcon(Color color, int width, int height) {
			this.color = color;
			this.width = width;
			this.height = height;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public Color getColor() {
			return color;
		}

		public int getIconHeight() {
			return width;
		}

		public int getIconWidth() {
			return height;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color oldColor = g.getColor();

			if (color != null) {
				g.setColor(color);
				g.fillRect(x, y, width, height);
			}

			g.setColor(UIManager.getColor("controlDkShadow"));
			g.drawRect(x, y, width, height);

			g.setColor(oldColor);
		}
	}
}
