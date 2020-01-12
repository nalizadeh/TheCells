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

import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.base.DesignerConstraints;
import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.util.CellsDialog;

import java.awt.Container;
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
public class MoveComponentAction extends CellsAction {

	private CellsDesigner designer;

	public MoveComponentAction(CellsDesigner designer) {
		super("Move Component");
		this.designer = designer;
	}

	public void init() {
		putValue("SrcCons", null);
		putValue("DstCons", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		DesignerConstraints src = (DesignerConstraints) getValue("SrcCons");
		DesignerConstraints dst = (DesignerConstraints) getValue("DstCons");
		Boolean undo = (Boolean) getValue("Undo");

		boolean approved = true;

		DesignerFrame df = designer.getDesktop().getDesigner();
		Container srcCont = src.getContainer() == null ? df.getContainer() : src.getContainer();
		Container dstCont = dst.getContainer() == null ? df.getContainer() : dst.getContainer();

		if (dst.getComponent() != null) {

			if (undo == null) {

				approved =
					CellsDialog.showConfirmDialog(
						designer,
						"Overwrite",
						"Cell contains Component, overwrite it ...",
						"Overwrite Component in Cell [" + dst.getComponent().getName() + "] ?",
						380,
						200
					);
			}
			if (approved) {
				dstCont.remove(dst.getComponent());
			}
		}

		if (approved) {

			srcCont.remove(src.getComponent());
			dstCont.add(src.getComponent(), dst.getConstraints());
                        df.getContainer().selectDesignerConstraints(src);
                        designer.update();

			if (undo == null) {
				designer.getActionManager().undoableEditHappened(
					new MoveComponentUndoRedoAction(designer, src, dst)
				);
			}
		} else {
			setExecuted(false);
		}
	}
}


class MoveComponentUndoRedoAction extends AbstractUndoableEdit {

	private CellsDesigner designer;

	private DesignerConstraints src;
	private DesignerConstraints dst;

	public MoveComponentUndoRedoAction(
		CellsDesigner		designer,
		DesignerConstraints src,
		DesignerConstraints dst
	) {
		this.designer = designer;
		this.src = src;
		this.dst = dst;
	}

	public String getPresentationName() {
		return "Move Component";
	}

	public void undo() {
		super.undo();

		DesignerContainer dc = (DesignerContainer) dst.getContainer();
		DesignerConstraints cons = dc.cloneDesignerConstraints(dst);
		cons.setComponent(src.getComponent());

		Action action = designer.getActionManager().getAction("MoveComponent");
		action.putValue("SrcCons", cons);
		action.putValue("DstCons", src);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);

		if (dst.getComponent() != null) {
			action = designer.getActionManager().getAction("InsertComponent");
			action.putValue("Cons", dst);
			action.putValue("Comp", dst.getComponent());
			action.putValue("Undo", Boolean.TRUE);
			action.actionPerformed(null);
		}
	}

	public void redo() {
		super.redo();

		Action action = designer.getActionManager().getAction("MoveComponent");
		action.putValue("SrcCons", src);
		action.putValue("DstCons", dst);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
