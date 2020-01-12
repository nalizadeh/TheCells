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

package org.nalizadeh.designer.actions;

import javax.swing.AbstractAction;
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
public abstract class CellsAction extends AbstractAction {

	public CellsAction(String name) {
		this(name, null);
	}

	public CellsAction(String name, Icon icon) {
		super(name, icon);
		putValue("ExecuteStatus", Boolean.TRUE);
	}

	protected void setExecuted(boolean b) {
		putValue("ExecuteStatus", b);
	}

	public boolean isExecuted() {
		return (Boolean) getValue("ExecuteStatus");
	}

	public abstract void init();
}
