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

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayoutDesigner;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.undo.AbstractUndoableEdit;

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
public class InsertColumnBeforeAction extends CellsAction {

	private CellsDesigner designer;

	public InsertColumnBeforeAction(CellsDesigner designer) {
		super("Insert new Column before");
		this.designer = designer;
	}

	public void init() {
		putValue("Column", null);
		putValue("OrigWidth", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();

		int col = (Integer) getValue("Column");
		double origWidth = (Double) getValue("OrigWidth");
		Boolean undo = (Boolean) getValue("Undo");

		designer.deselectAll();
		lo.insertColumn(new CellsLayout.CellColumn(col, origWidth, 0));
		designer.update();
		df.getColumnPanel().selectColumn(col);

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new InsertColumnBeforeUndoRedoAction(designer, col, origWidth)
			);
		}
	}
}


class InsertColumnBeforeUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private int col;
	private double origWidth;

	public InsertColumnBeforeUndoRedoAction(CellsDesigner designer, int col, double origWidth) {
		this.designer = designer;
		this.col = col;
		this.origWidth = origWidth;
	}

	public String getPresentationName() {
		return "Insert Column Before";
	}

	public void undo() {
		super.undo();
		Action action = designer.getActionManager().getAction("DeleteColumn");
		action.putValue("Column", col);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("InsertColumnBefore");
		action.putValue("Column", col);
		action.putValue("OrigWidth", origWidth);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}
}
