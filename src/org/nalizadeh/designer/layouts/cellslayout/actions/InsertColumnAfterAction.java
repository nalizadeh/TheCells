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
public class InsertColumnAfterAction extends CellsAction {

	private CellsDesigner designer;

	public InsertColumnAfterAction(CellsDesigner designer) {
		super("Insert new Column after");
		this.designer = designer;
	}

	public void init() {
		putValue("Column", null);
		putValue("OrigWidth", null);
		putValue("DoSelect", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();

		int col = (Integer) getValue("Column") + 1;
		double origWidth = (Double) getValue("OrigWidth");
		Boolean sel = (Boolean) getValue("DoSelect");
		Boolean undo = (Boolean) getValue("Undo");

		designer.deselectAll();
		lo.insertColumn(new CellsLayout.CellColumn(col, origWidth, 0));
		designer.update();
		if (sel == null || sel) {
			df.getColumnPanel().selectColumn(col);
		}

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new InsertColumnAfterUndoRedoAction(designer, col, origWidth)
			);
		}
	}
}


class InsertColumnAfterUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private int col;
	private double origWidth;

	public InsertColumnAfterUndoRedoAction(CellsDesigner designer, int col, double origWidth) {
		this.designer = designer;
		this.col = col;
		this.origWidth = origWidth;
	}

	public String getPresentationName() {
		return "Insert Column After";
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
		Action action = designer.getActionManager().getAction("InsertColumnAfter");
		action.putValue("Column", col - 1);
		action.putValue("OrigWidth", origWidth);
		action.putValue("DoSelect", Boolean.TRUE);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}
}
