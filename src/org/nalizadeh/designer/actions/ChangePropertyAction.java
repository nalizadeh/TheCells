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

import org.nalizadeh.designer.property.PropertySheet;

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
public class ChangePropertyAction extends CellsAction {

	private CellsDesigner designer;

	public ChangePropertyAction(CellsDesigner designer) {
		super("Change Property");
		this.designer = designer;
	}

	public void init() {
		putValue("Cons", null);
		putValue("Name", null);
		putValue("Value", null);
		putValue("OldValue", null);
		putValue("Type", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent e) {

		DesignerConstraints cons = (DesignerConstraints) getValue("Cons");
		String name = (String) getValue("Name");
		Object value = (Object) getValue("Value");
		Object oldValue = (Object) getValue("OldValue");
		Class type = (Class) getValue("Type");
		Boolean undo = (Boolean) getValue("Undo");

		PropertySheet ps = designer.getProperty();

		Object ov = ps.changeProperty(cons, name, value, type);

		if (oldValue == null) {
			oldValue = ov;
		}

		ps.update();
		ps.revalidate();
		ps.repaint();

		cons.getContainer().revalidate();
                cons.getContainer().selectDesignerConstraints(cons);

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new PropertyChangeUndoRedoAction(designer, cons, name, value, oldValue, type)
			);
		}

                designer.update();
	}
}


class PropertyChangeUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private DesignerConstraints cons;
	private String name;
	private Object value;
	private Object oldValue;
	private Class type;

	public PropertyChangeUndoRedoAction(
		CellsDesigner		designer,
		DesignerConstraints cons,
		String				name,
		Object				value,
		Object				oldValue,
		Class				type
	) {
		this.designer = designer;
		this.cons = cons;
		this.name = name;
		this.value = value;
		this.oldValue = oldValue;
		this.type = type;
	}

	public String getPresentationName() {
		return "Property Change (" + name + ")";
	}

	public void undo() {
		super.undo();
		Action action = designer.getActionManager().getAction("ChangeProperty");
		action.putValue("Cons", cons);
		action.putValue("Name", name);
		action.putValue("Value", oldValue);
		action.putValue("Type", type);
		action.putValue("OldValue", value);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("ChangeProperty");
		action.putValue("Cons", cons);
		action.putValue("Name", name);
		action.putValue("Value", value);
		action.putValue("Type", type);
		action.putValue("OldValue", oldValue);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
