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

import javax.swing.Icon;

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
public class ListCellRenderer extends DefaultCellRenderer {

	private Object[] items;
	private Object[] values;
	private Icon[] icons;

	public ListCellRenderer(Object[] items, Object[] values, Icon[] icons) {
		this.items = items;
		this.values = values;
		this.icons = icons;
	}

	public Object[] getItems() {
		return items;
	}

	public Object[] getValues() {
		return values;
	}

	public Icon[] getIcons() {
		return icons;
	}

	protected Icon getEditorIcon() {
		return JBeansUtils.LISTEDIT;
	}

	protected String convertToString(Object value) {
		if (value == null) {
			return null;
		}
		int n = 0;
		for (Object v : values) {
			if (v.equals(value)) {
				return items[n].toString();
			}
			n++;
		}
		return "";
	}

	protected Icon convertToIcon(Object value) {
		if (value == null || icons == null) {
			return null;
		}
		int n = 0;
		for (Object v : values) {
			if (v.equals(value)) {
				return icons[n];
			}
			n++;
		}
		return null;
	}

	public Object translate(Object value) {
		int v = 0;
		for (Object o : items) {
			if (o.equals(value)) {
				return values[v];
			}
			v++;
		}
		return null;
	}
}
