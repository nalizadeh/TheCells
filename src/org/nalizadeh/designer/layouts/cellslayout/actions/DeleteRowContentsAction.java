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
import org.nalizadeh.designer.layouts.cellslayout.CellsLayoutContainer;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayoutDesigner;

import java.awt.Component;
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
public class DeleteRowContentsAction extends CellsAction {

	private CellsDesigner designer;

	public DeleteRowContentsAction(CellsDesigner designer) {
		super("Delete Row Content");
		this.designer = designer;
	}

	public void init() {
		putValue("Column", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {
		int row = (Integer) getValue("Row");
		Boolean undo = (Boolean) getValue("Undo");

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayoutContainer co = (CellsLayoutContainer) df.getContainer();
		CellsLayout la = (CellsLayout) co.getLayout();

		List<CellsLayout.Cell> cells = new ArrayList<CellsLayout.Cell>();
		for (Component comp : co.getComponents()) {
			CellsLayout.Cell cell = la.getCell(comp);
			if (cell.row == row) {
				cells.add(cell.clone());
				co.remove(comp);
			}
		}

		designer.deselectAll();
		df.getRowPanel().selectRow(row);
		designer.update();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new DeleteRowContentsUndoRedoAction(designer, row, cells)
			);
		}
	}
}


class DeleteRowContentsUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private int row;
	private List<CellsLayout.Cell> cells;

	public DeleteRowContentsUndoRedoAction(
		CellsDesigner		   designer,
		int					   row,
		List<CellsLayout.Cell> cells
	) {

		this.designer = designer;
		this.row = row;
		this.cells = cells;
	}

	public String getPresentationName() {
		return "Delete Row Content";
	}

	public void undo() {
		super.undo();

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayoutContainer co = (CellsLayoutContainer) df.getContainer();

		for (CellsLayout.Cell cell : cells) {
			if (cell.comp != null) {
				co.add(cell.comp, cell);
			}
		}

		designer.deselectAll();
		df.getRowPanel().selectRow(row);
		designer.getActionManager().update();
		designer.getProperty().update();
		designer.redraw();
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("DeleteRowContents");
		action.putValue("Row", row);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);

		designer.getActionManager().update();
	}
}
