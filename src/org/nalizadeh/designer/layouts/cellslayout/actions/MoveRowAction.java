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
public class MoveRowAction extends CellsAction {

	private CellsDesigner designer;

	public MoveRowAction(CellsDesigner designer) {
		super("Move Row");
		this.designer = designer;
	}

	public void init() {
		putValue("SrcRow", null);
		putValue("DstRow", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		CellsLayoutDesigner df = (CellsLayoutDesigner) designer.getDesktop().getDesigner();
		CellsLayout lo = (CellsLayout) df.getContainer().getLayout();

		int src = (Integer) getValue("SrcRow");
		int dst = (Integer) getValue("DstRow");
		Boolean undo = (Boolean) getValue("Undo");

		designer.deselectAll();
		lo.moveRow(src, dst);
		df.getRowPanel().update(true);
                df.getRowPanel().selectRow(dst < src ? dst : dst - 1);
                designer.update();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new MoveRowUndoRedoAction(designer, src, dst)
			);
		}
	}
}


class MoveRowUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private int src;
	private int dst;

	public MoveRowUndoRedoAction(CellsDesigner designer, int src, int dst) {
		this.designer = designer;
		this.src = src;
		this.dst = dst;
	}

	public String getPresentationName() {
		return "Move Row";
	}

	public void undo() {
		super.undo();
		Action action = designer.getActionManager().getAction("MoveRow");
		action.putValue("SrcRow", dst > src ? dst - 1 : dst);
		action.putValue("DstRow", src > dst ? src + 1 : src);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("MoveRow");
		action.putValue("SrcRow", src);
		action.putValue("DstRow", dst);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
		designer.getActionManager().update();
	}
}
