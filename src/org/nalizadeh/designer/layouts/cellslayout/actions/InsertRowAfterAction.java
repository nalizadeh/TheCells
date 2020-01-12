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
public class InsertRowAfterAction extends CellsAction {

	private CellsDesigner designer;

	public InsertRowAfterAction(CellsDesigner designer) {
		super("Insert new Row after");
		this.designer = designer;
	}

	public void init() {
		putValue("Row", null);
		putValue("OrigHeight", null);
		putValue("DoSelect", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();

		int row = (Integer) getValue("Row") + 1;
		double origHeight = (Double) getValue("OrigHeight");
		Boolean sel = (Boolean) getValue("DoSelect");
		Boolean undo = (Boolean) getValue("Undo");

		designer.deselectAll();
		lo.insertRow(new CellsLayout.CellRow(row, origHeight, 0));
		designer.update();
		if (sel == null || sel) {
			df.getRowPanel().selectRow(row);
		}

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new InsertRowBeforeUndoRedoAction(designer, row, origHeight)
			);
		}
	}
}


class InsertRowAfterUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private int row;
	private double origHeight;

	public InsertRowAfterUndoRedoAction(CellsDesigner designer, int row, double origHeight) {
		this.designer = designer;
		this.row = row;
		this.origHeight = origHeight;
	}

	public String getPresentationName() {
		return "Insert Row After";
	}

	public void undo() {
		super.undo();
		Action action = designer.getActionManager().getAction("DeleteRow");
		action.putValue("Row", row);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("InsertRowAfter");
		action.putValue("Row", row - 1);
		action.putValue("OrigHeight", origHeight);
		action.putValue("DoSelect", Boolean.TRUE);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}
}
