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

import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

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
public class DeleteRowAction extends CellsAction {

	private CellsDesigner designer;

	public DeleteRowAction(CellsDesigner designer) {
		super("Delete Row");
		this.designer = designer;
	}

	public void init() {
		putValue("Row", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		int row = (Integer) getValue("Row");
		Boolean undo = (Boolean) getValue("Undo");

		DesignerFrame df = designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();
		CellsLayout.CellRow r = lo.getRows().get(row);

		List<CellsLayout.Cell> c1 = lo.getCellsInRow(row);
		List<CellsLayout.Cell> c2 = new ArrayList<CellsLayout.Cell>();
		for (CellsLayout.Cell cell : c1) {
			c2.add(cell.clone());
		}

		designer.deselectAll();
		lo.removeRow(new CellsLayout.CellRow(row, 0, 0));
		designer.update();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new DeleteRowUndoRedoAction(designer, row, c2, r.origHeight)
			);
		}
	}
}


class DeleteRowUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private int row;
	private List<CellsLayout.Cell> cells;
	private double origHeight;

	public DeleteRowUndoRedoAction(
		CellsDesigner		   designer,
		int					   row,
		List<CellsLayout.Cell> cells,
		double				   origHeight
	) {
		this.designer = designer;
		this.row = row;
		this.cells = cells;
		this.origHeight = origHeight;
	}

	public String getPresentationName() {
		return "Delete Row";
	}

	public void undo() {
		super.undo();
		Action action = designer.getActionManager().getAction("InsertRowBefore");
		action.putValue("Row", row);
		action.putValue("OrigHeight", origHeight);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);

		DesignerFrame df = designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();
		lo.addCells(cells);
		designer.redraw();

		designer.getActionManager().update();
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("DeleteRow");
		action.putValue("Row", row);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}
}
