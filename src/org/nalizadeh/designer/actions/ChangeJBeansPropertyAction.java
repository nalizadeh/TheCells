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

import org.nalizadeh.designer.property.jbeans.JBeansProperty;

import java.awt.Component;
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
public class ChangeJBeansPropertyAction extends CellsAction {

	private CellsDesigner designer;

	public ChangeJBeansPropertyAction(CellsDesigner designer) {
		super("Change JBeansProperty");
		this.designer = designer;
	}

	public void init() {
		putValue("Comp", null);
		putValue("Prop", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent e) {
		Component comp = (Component) getValue("Comp");
		JBeansProperty prop = (JBeansProperty) getValue("Prop");
		Boolean undo = (Boolean) getValue("Undo");

		prop.writeToObject(comp);
		comp.repaint();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new ChangeJBeansPropertyUndoRedoAction(designer, comp, prop)
			);
		}

		designer.getProperty().setUpdateAdjusting(true);
		designer.update();
		designer.getProperty().setUpdateAdjusting(false);
	}
}


class ChangeJBeansPropertyUndoRedoAction extends AbstractUndoableEdit {

	private CellsDesigner designer;
	private Component comp;
	private JBeansProperty prop;
	private Object newValue;
	private Object oldValue;

	public ChangeJBeansPropertyUndoRedoAction(
		CellsDesigner  designer,
		Component	   comp,
		JBeansProperty prop
	) {
		this.designer = designer;
		this.comp = comp;
		this.prop = prop;
		this.newValue = prop.getValue();
		this.oldValue = prop.getOldValue();
	}

	public String getPresentationName() {
		return "Property Change (" + prop.getName() + ")";
	}

	public void undo() {
		super.undo();

		prop.changeValue(oldValue);

		Action action = designer.getActionManager().getAction("ChangeJBeansProperty");
		action.putValue("Comp", comp);
		action.putValue("Prop", prop);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}

	public void redo() {
		super.redo();

		prop.changeValue(newValue);

		Action action = designer.getActionManager().getAction("ChangeJBeansProperty");
		action.putValue("Comp", comp);
		action.putValue("Prop", prop);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
