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
public class ResizeRowAction extends CellsAction {

	private CellsDesigner designer;

	public ResizeRowAction(CellsDesigner designer) {
		super("Resize Row");
		this.designer = designer;
	}

	public void init() {
		putValue("Row", null);
		putValue("RowHeight", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		int row = (Integer) getValue("Row");
		int rowHeight = (Integer) getValue("RowHeight");
		Boolean undo = (Boolean) getValue("Undo");

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();
		CellsLayout.CellRow r = lo.getRows().get(row);
		int oldHeight = r.height;

		designer.deselectAll();
		r.origHeight = rowHeight;
		designer.update();
		df.getRowPanel().selectRow(row);

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new ResizeRowUndoRedoAction(designer, row, oldHeight)
			);
		}

	}
}


class ResizeRowUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private int row;
	private int oldHeight;
	private int lastHeight;

	public ResizeRowUndoRedoAction(CellsDesigner designer, int row, int oldHeight) {

		this.designer = designer;
		this.row = row;
		this.oldHeight = oldHeight;
	}

	public String getPresentationName() {
		return "Resize Row";
	}

	public void undo() {
		super.undo();

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();
		CellsLayout.CellRow r = lo.getRows().get(row);

		lastHeight = r.height;
		Action action = designer.getActionManager().getAction("ResizeRow");
		action.putValue("Row", row);
		action.putValue("RowHeight", oldHeight);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("ResizeRow");
		action.putValue("Row", row);
		action.putValue("RowHeight", lastHeight);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}
}
