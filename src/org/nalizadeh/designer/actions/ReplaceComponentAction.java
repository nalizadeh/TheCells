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
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
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
public class ReplaceComponentAction extends CellsAction {

	private CellsDesigner designer;

	public ReplaceComponentAction(CellsDesigner designer) {
		super("Replace Component");
		this.designer = designer;
	}

	public void init() {
		putValue("OldCons", null);
		putValue("NewCons", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		DesignerConstraints oldCons = (DesignerConstraints) getValue("OldCons");
		DesignerConstraints newCons = (DesignerConstraints) getValue("NewCons");
		Boolean undo = (Boolean) getValue("Undo");

		Container cont = oldCons.getComponent().getParent();

		if (cont instanceof JTabbedPane) {
			JTabbedPane tp = (JTabbedPane) cont;
			int n = tp.indexOfComponent(oldCons.getComponent());

			Component[] comps = tp.getComponents();
			comps[n] = newCons.getComponent();

			String[] tits = new String[comps.length];
			for (int i = 0; i < comps.length; i++) {
				tits[i] = tp.getTitleAt(i);
			}

			tp.removeAll();
			for (int i = 0; i < comps.length; i++) {
				tp.add(comps[i], tits[i]);
			}
			tp.setSelectedIndex(n);

		} else if (cont instanceof JSplitPane) {
			JSplitPane sp = (JSplitPane) cont;
			Component comp = oldCons.getComponent();

			if (sp.getLeftComponent() == comp) {
				sp.remove(comp);
				sp.add(newCons.getComponent(), JSplitPane.LEFT);
			} else if (sp.getRightComponent() == comp) {
				sp.remove(comp);
				sp.add(newCons.getComponent(), JSplitPane.RIGHT);
			} else if (sp.getTopComponent() == comp) {
				sp.remove(comp);
				sp.add(newCons.getComponent(), JSplitPane.TOP);
			} else if (sp.getBottomComponent() == comp) {
				sp.remove(comp);
				sp.add(newCons.getComponent(), JSplitPane.BOTTOM);
			}
		} else {
                        oldCons.getContainer().remove(oldCons.getComponent());
                        newCons.getContainer().add(newCons.getComponent(), newCons.getConstraints());
		}

                designer.getDesktop().getDesigner().getContainer().selectDesignerConstraints(newCons);
                designer.update();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new ReplaceComponentUndoRedoAction(designer, oldCons, newCons)
			);
		}
	}
}


class ReplaceComponentUndoRedoAction extends AbstractUndoableEdit {

	private CellsDesigner designer;
	private DesignerConstraints newCons;
	private DesignerConstraints oldCons;

	public ReplaceComponentUndoRedoAction(
		CellsDesigner		designer,
		DesignerConstraints oldCons,
		DesignerConstraints newCons
	) {
		this.designer = designer;
		this.oldCons = oldCons;
		this.newCons = newCons;
	}

	public String getPresentationName() {
		return "Layout sub container";
	}

	public void undo() {
		super.undo();

		Action action = designer.getActionManager().getAction("ReplaceComponent");
		action.putValue("OldCons", newCons);
		action.putValue("NewCons", oldCons);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}

	public void redo() {
		super.redo();

		Action action = designer.getActionManager().getAction("ReplaceComponent");
		action.putValue("OldCons", oldCons);
		action.putValue("NewCons", newCons);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
