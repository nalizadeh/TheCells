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

import java.awt.GridBagConstraints;

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
public class AnchorEditor extends JOriginalTable.ListEditorComponent {

	private static final String[] items =
		new String[] {
			"Center", "North", "Northeast", "East", "Southeast", "South", "Southwest", "West",
			"Northwest", "Page Start", "Page End", "Line Start", "Line End", "First Line Start",
			"First Line End", "Last Line Start", "Last Line End"
		};

	private static final Integer[] values =
		{
			GridBagConstraints.CENTER, GridBagConstraints.NORTH, GridBagConstraints.NORTHEAST,
			GridBagConstraints.EAST, GridBagConstraints.SOUTHEAST, GridBagConstraints.SOUTH,
			GridBagConstraints.SOUTHWEST, GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,
			GridBagConstraints.PAGE_START, GridBagConstraints.PAGE_END,
			GridBagConstraints.LINE_START, GridBagConstraints.LINE_END,
			GridBagConstraints.FIRST_LINE_START, GridBagConstraints.FIRST_LINE_END,
			GridBagConstraints.LAST_LINE_START, GridBagConstraints.LAST_LINE_END
		};

	public AnchorEditor() {
		super(items, values, null);
	}
}
