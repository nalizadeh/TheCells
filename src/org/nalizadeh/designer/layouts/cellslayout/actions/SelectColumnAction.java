/*
 * Copyright 2007 N.A.J. nalizadeh.org All rights reserved.
 * NALIZADEH.COM PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
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

package org.nalizadeh.designer.layouts.cellslayout.actions;

import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.actions.CellsAction;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayoutDesigner;

import java.awt.event.ActionEvent;

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
public class SelectColumnAction extends CellsAction {

	private CellsDesigner designer;

	public SelectColumnAction(CellsDesigner designer) {
		super("Select Column");
		this.designer = designer;
	}

	public void init() {
		putValue("Column", null);
	}

	public void actionPerformed(ActionEvent evt) {
		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		Integer col = (Integer) getValue("Column");
		designer.deselectAll();
		df.getColumnPanel().selectColumn(col);
		designer.update();
	}
}
