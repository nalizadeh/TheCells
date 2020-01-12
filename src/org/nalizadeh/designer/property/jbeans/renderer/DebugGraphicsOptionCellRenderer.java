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

import javax.swing.DebugGraphics;

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
public class DebugGraphicsOptionCellRenderer extends ListCellRenderer {

	private static final String[] items = { "NONE", "LOG", "FLASH", "BUFFERED" };
	private static final Integer[] values =
		{
			DebugGraphics.NONE_OPTION, DebugGraphics.LOG_OPTION, DebugGraphics.FLASH_OPTION,
			DebugGraphics.BUFFERED_OPTION
		};

	public DebugGraphicsOptionCellRenderer() {
		super(items, values, null);
	}
}
