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

import org.nalizadeh.designer.CellsDesigner;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import org.nalizadeh.designer.ResourceManager;

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
public class CellsImagePool {

	private static final CellsImagePool instance = new CellsImagePool();

	private Map<String, ImageIcon> pool = new HashMap<String, ImageIcon>();

	public static CellsImagePool shared() {
		return instance;
	}

	public synchronized void clear() {
		pool.clear();
	}

	public void put(String name, ImageIcon icon) {
		pool.put(name, icon);
	}

	public ImageIcon get(String name, String filename) {
		ImageIcon icon = pool.get(name);
		if (icon == null && filename != null) {
			icon = getImage(filename);
			if (icon != null) {
				pool.put(name, icon);
			}
		}
		return icon;
	}

	public static ImageIcon getImage(String name) {
		ImageIcon icon = instance.get(name, null);
		if (icon == null) {
			icon = ResourceManager.createIcon(CellsUtil.IMG_PATH + name);
		}
		return icon;
	}
}
