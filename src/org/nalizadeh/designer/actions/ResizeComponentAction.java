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

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.undo.AbstractUndoableEdit;
import org.nalizadeh.designer.base.DesignerFrame;

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
public class ResizeComponentAction extends CellsAction {

	private CellsDesigner designer;

	public ResizeComponentAction(CellsDesigner designer) {
		super("Resize Component");
		this.designer = designer;
	}

	public void init() {
		putValue("Cons", null);
		putValue("NewCons", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		DesignerConstraints cons = (DesignerConstraints) getValue("Cons");
		DesignerConstraints newCons = (DesignerConstraints) getValue("NewCons");
		Boolean undo = (Boolean) getValue("Undo");

		cons.getContainer().remove(cons.getComponent());
		cons.getContainer().add(cons.getComponent(), newCons.getConstraints());

                DesignerFrame df = designer.getDesktop().getDesigner();
                df.getContainer().selectDesignerConstraints(newCons);
                designer.update();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new ResizeComponentUndoRedoAction(designer, cons, newCons)
			);
		}
	}
}


class ResizeComponentUndoRedoAction extends AbstractUndoableEdit {

	private CellsDesigner designer;
	private DesignerConstraints cons;
	private DesignerConstraints newCons;

	public ResizeComponentUndoRedoAction(
		CellsDesigner		designer,
		DesignerConstraints cons,
		DesignerConstraints newCons
	) {
		this.designer = designer;
		this.cons = cons;
		this.newCons = newCons;
	}

	public String getPresentationName() {
		return "Resize Component";
	}

	public void undo() {
		super.undo();

		Action action = designer.getActionManager().getAction("ResizeComponent");
		action.putValue("Cons", newCons);
		action.putValue("NewCons", cons);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}

	public void redo() {
		super.redo();

		Action action = designer.getActionManager().getAction("ResizeComponent");
		action.putValue("Cons", cons);
		action.putValue("NewCons", newCons);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
