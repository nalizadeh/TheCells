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

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JViewport;
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
public class DeleteComponentAction extends CellsAction {

	private CellsDesigner designer;

	public DeleteComponentAction(CellsDesigner designer) {
		super("Delete Component");
		this.designer = designer;
	}

	public void init() {
		putValue("Cons", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		DesignerConstraints cons = (DesignerConstraints) getValue("Cons");
		Boolean undo = (Boolean) getValue("Undo");

		designer.deselectAll();

		Component comp = cons.getComponent();
		Component parent = comp.getParent();

		if (parent instanceof JViewport) {
			JViewport vp = (JViewport) parent;
			vp.remove(comp);
			cons.setComponent(vp.getParent());

		} else {
			cons.getContainer().remove(comp);
		}

		designer.update();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new DeleteComponentUndoRedoAction(designer, cons, comp)
			);
		}
	}
}


class DeleteComponentUndoRedoAction extends AbstractUndoableEdit {

	private CellsDesigner designer;
	private DesignerConstraints cons;
	private Component comp;

	public DeleteComponentUndoRedoAction(
		CellsDesigner		designer,
		DesignerConstraints cons,
		Component			comp
	) {
		this.designer = designer;
		this.cons = cons;
		this.comp = comp;
	}

	public String getPresentationName() {
		return "Delete Component";
	}

	public void undo() {
		super.undo();

		Action action = designer.getActionManager().getAction("InsertComponent");
		action.putValue("Cons", cons);
		action.putValue("Comp", comp);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}

	public void redo() {
		super.redo();

		cons.setComponent(comp);

		Action action = designer.getActionManager().getAction("DeleteComponent");
		action.putValue("Cons", cons);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
