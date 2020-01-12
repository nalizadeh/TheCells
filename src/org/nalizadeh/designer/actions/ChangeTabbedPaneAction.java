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
import javax.swing.JPanel;
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
public class ChangeTabbedPaneAction extends CellsAction {

	private CellsDesigner designer;

	public ChangeTabbedPaneAction(CellsDesigner designer) {
		super("Change TabbedPane");
		this.designer = designer;
	}

	public void init() {
		putValue("Cons", null);
		putValue("Comp", null);
		putValue("Title", null);
		putValue("Index", null);
		putValue("Add", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		DesignerConstraints cons = (DesignerConstraints) getValue("Cons");
		Component comp = (JPanel) getValue("Comp");
		String title = (String) getValue("Title");
		int index = (Integer) getValue("Index");
		Boolean add = (Boolean) getValue("Add");
		Boolean undo = (Boolean) getValue("Undo");

		JTabbedPane tp = (JTabbedPane) cons.getComponent();

		if (add) {
			Component[] comps = tp.getComponents();
			if (comps.length == 0 || index >= comps.length) {
				tp.addTab(title, comp);
			} else {
				String[] tits = new String[comps.length];
				for (int i = 0; i < comps.length; i++) {
					tits[i] = tp.getTitleAt(i);
				}
				tp.removeAll();
				for (int i = 0; i < comps.length; i++) {
					if (i == index) {
						tp.addTab(title, comp);
					}
					tp.add(tits[i], comps[i]);
				}
			}
		} else {
			if (index == -1) {
				index = tp.getSelectedIndex();
			}
			if (index == -1) {
				return;
			}
			comp = tp.getComponentAt(index);
			title = tp.getTitleAt(index);
			tp.remove(index);
		}

                designer.getDesktop().getDesigner().getContainer().selectDesignerConstraints(cons);
                designer.update();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new ChangeTabbedPaneRedoAction(designer, cons, comp, title, index, add)
			);
		}
	}
}


class ChangeTabbedPaneRedoAction extends AbstractUndoableEdit {

	private CellsDesigner designer;
	private DesignerConstraints cons;
	private Component comp;
	private String title;
	private int index;
	private boolean add;

	public ChangeTabbedPaneRedoAction(
		CellsDesigner		designer,
		DesignerConstraints cons,
		Component			comp,
		String				title,
		int					index,
		boolean				add
	) {
		this.designer = designer;
		this.cons = cons;
		this.comp = comp;
		this.title = title;
		this.index = index;
		this.add = add;
	}

	public String getPresentationName() {
		return "Change TabbedPane";
	}

	public void undo() {
		super.undo();

		Action action = designer.getActionManager().getAction("ChangeTabbedPane");
		action.putValue("Cons", cons);
		action.putValue("Comp", comp);
		action.putValue("Title", title);
		action.putValue("Index", index);
		action.putValue("Add", !add);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}

	public void redo() {
		super.redo();

		Action action = designer.getActionManager().getAction("ChangeTabbedPane");
		action.putValue("Cons", cons);
		action.putValue("Comp", comp);
		action.putValue("Title", title);
		action.putValue("Index", index);
		action.putValue("Add", add);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
