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

package org.nalizadeh.designer.property.jbeans;

import org.nalizadeh.designer.util.CellsUtil;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.lang.reflect.Method;

import javax.swing.ImageIcon;

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
public class JBeansUtils {

	public static final String TREE_EXPANDED_ICON_KEY = "Tree.expandedIcon";
	public static final String TREE_COLLAPSED_ICON_KEY = "Tree.collapsedIcon";
	public static final String TABLE_BACKGROUND_COLOR_KEY = "Table.background";
	public static final String TABLE_FOREGROUND_COLOR_KEY = "Table.foreground";
	public static final String TABLE_SELECTED_BACKGROUND_COLOR_KEY = "Table.selectionBackground";
	public static final String TABLE_SELECTED_FOREGROUND_COLOR_KEY = "Table.selectionForeground";
	public static final String PANEL_BACKGROUND_COLOR_KEY = "Panel.background";

	public static final ImageIcon TEXTEDIT = JOriginalTable.getImage("texteditor.gif");
	public static final ImageIcon NUMEDIT = JOriginalTable.getImage("numeditor.gif");
	public static final ImageIcon LISTEDIT = JOriginalTable.getImage("listeditor.gif");
	public static final ImageIcon BOOLEDIT1 = JOriginalTable.getImage("checkboxOff.gif");
	public static final ImageIcon BOOLEDIT2 = JOriginalTable.getImage("checkboxOn.gif");
	public static final ImageIcon COLOREDIT = JOriginalTable.getImage("coloreditor.gif");
	public static final ImageIcon FONTEDIT = JOriginalTable.getImage("fonteditor.gif");
	public static final ImageIcon BORDEREDIT = JOriginalTable.getImage("bordereditor.gif");
	public static final ImageIcon DATEEDIT = JOriginalTable.getImage("dateeditor.gif");
	public static final ImageIcon TIMEEDIT = JOriginalTable.getImage("timeeditor.gif");

	public static final ImageIcon CHECKBOX1 = JOriginalTable.getImage("checkboxOn.gif");
	public static final ImageIcon CHECKBOX2 = JOriginalTable.getImage("checkboxOff.gif");

	public static final ImageIcon DIRTYPROP = CellsUtil.getImage("ptabchangedLeaf.gif");
	public static final ImageIcon CLEANPROP = CellsUtil.getImage("ptabunchangedLeaf.gif");

	public static Method getReadMethod(Class clazz, String propertyName) {
		Method readMethod = null;
		String base = capitalize(propertyName);

		try {
			readMethod = clazz.getMethod("is" + base, new Class[0]);
		} catch (Exception getterExc) {
			try {

				// no "is" method, so look for a "get" method.
				readMethod = clazz.getMethod("get" + base, new Class[0]);
			} catch (Exception e) {
				// no is and no get, we will return null
			}
		}

		return readMethod;
	}

	public static Method getWriteMethod(Class clazz, String propertyName, Class propertyType) {
		Method writeMethod = null;
		String base = capitalize(propertyName);

		Class[] params = { propertyType };
		try {
			writeMethod = clazz.getMethod("set" + base, params);
		} catch (Exception e) {
			// no write method
		}

		return writeMethod;
	}

	private static String capitalize(String s) {
		if (s.length() == 0) {
			return s;
		} else {
			char[] chars = s.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			return String.valueOf(chars);
		}
	}
}
