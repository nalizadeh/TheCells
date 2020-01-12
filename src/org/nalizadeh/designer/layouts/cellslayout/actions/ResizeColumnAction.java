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
public class ResizeColumnAction extends CellsAction {

	private CellsDesigner designer;

	public ResizeColumnAction(CellsDesigner designer) {
		super("Resize Column");
		this.designer = designer;
	}

	public void init() {
		putValue("Column", null);
		putValue("ColumnWidth", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		int col = (Integer) getValue("Column");
		int colWidth = (Integer) getValue("ColumnWidth");
		Boolean undo = (Boolean) getValue("Undo");

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();
		CellsLayout.CellColumn c = lo.getColumns().get(col);

		int oldWidth = c.width;

		designer.deselectAll();
		c.origWidth = colWidth;
		designer.update();
		df.getColumnPanel().selectColumn(col);

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new ResizeColumnUndoRedoAction(designer, col, oldWidth)
			);
		}
	}
}


class ResizeColumnUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private int col;
	private int oldWidth;
	private int lastWidth;

	public ResizeColumnUndoRedoAction(CellsDesigner designer, int col, int oldWidth) {

		this.designer = designer;
		this.col = col;
		this.oldWidth = oldWidth;
	}

	public String getPresentationName() {
		return "Resize Column";
	}

	public void undo() {
		super.undo();

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();
		CellsLayout.CellColumn c = lo.getColumns().get(col);

		lastWidth = c.width;
		Action action = designer.getActionManager().getAction("ResizeColumn");
		action.putValue("Column", col);
		action.putValue("ColumnWidth", oldWidth);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("ResizeColumn");
		action.putValue("Column", col);
		action.putValue("ColumnWidth", lastWidth);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}
}
