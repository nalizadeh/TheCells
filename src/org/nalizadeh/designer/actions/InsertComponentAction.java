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
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JScrollPane;
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
public class InsertComponentAction extends CellsAction {

	private CellsDesigner designer;

	public InsertComponentAction(CellsDesigner designer) {
		super("Insert Component");
		this.designer = designer;
	}

	public void init() {
		putValue("Cons", null);
		putValue("Comp", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent evt) {

		DesignerConstraints dst = (DesignerConstraints) getValue("Cons");
		Component comp = (Component) getValue("Comp");
		Boolean undo = (Boolean) getValue("Undo");

		if (dst.getConstraints() == null) {
			return;
		}

		Component oldScrollComp = null;
		boolean scrollDirty = false;
		boolean approved = true;

		DesignerFrame df = designer.getDesktop().getDesigner();
		Component dstComp = dst.getComponent();
		Component copyComp = comp;

		if (dst.getContainer() == null) {
			dst.setContainer(df.getContainer());
		}

		DesignerContainer cont = dst.getContainer();

		boolean dstScroll = false;

		if (dstComp != null) {

			if (dstComp instanceof JScrollPane && CellsUtil.isScrollable(comp)) {

				int a = CellsDialog.APPROVE_OPTION;
				if (undo == null) {
					a = CellsDialog.showDialog(
							designer,
							"Adding to ScrollPane",
							"Cell contains ScrollPane ...",
							"<html>The inserting component is scrollable and the cell containts ScrollPane.<br>"
							+ "Do you want to add it to <b>ViewPort</b> of ScrollPane?",
							true,
							true,
							"Add to ViewPort",
							"Overwrite",
							"Cancel",
							480,
							250
						);
				}
				if (a == CellsDialog.APPROVE_OPTION) {
					scrollDirty = true;
					JScrollPane sp = (JScrollPane) dstComp;
					if (
						sp.getViewport().getComponentCount() > 0
						&& sp.getViewport().getComponent(0) != null
					) {
						oldScrollComp = sp.getViewport().getComponent(0);
					}
					sp.getViewport().add(comp, 0);
					comp = sp;
					cont = (DesignerContainer) sp.getParent();

					dstScroll = true;
				}

				approved = a != CellsDialog.CANCEL_OPTION;

			} else if (undo == null) {
				approved =
					CellsDialog.showConfirmDialog(
						designer,
						"Overwrite",
						"Cell contains Component, overwrite it ...",
						"Overwrite Component in Cell [" + dstComp.getName() + "] ?",
						380,
						200
					);
			}

			if (approved) {
				cont.remove(dstComp);
			}
		}

		if (approved) {

			if (undo == null && !dstScroll && CellsUtil.isScrollable(comp)) {
				int a =
					CellsDialog.showDialog(
						designer,
						"Adding Scrollable",
						"Adding scrollable component...",
						"<html>The component you are inserting is scrollable, do you want <br>"
						+ "to add it within ScrollPane?",
						"Use ScrollPane",
						"Add without ScrollPane",
						410,
						250
					);

				if (a == CellsDialog.APPROVE_OPTION) {
					comp = new JScrollPane(comp);
					copyComp = comp;
				}
			}

			cont.add(comp, dst.getConstraints());
                        df.getContainer().selectDesignerConstraints(new DesignerConstraints(null, comp, cont));
                        designer.getActionManager().setClipboard(null);
                        designer.update();

			if (undo == null) {

				cont.setComponentName(null, copyComp, cont.getName());

				designer.getActionManager().undoableEditHappened(
					new InsertComponentUndoRedoAction(
						designer,
						dst,
						comp,
						scrollDirty,
						oldScrollComp
					)
				);
			}
		}
		setExecuted(approved);
	}
}


class InsertComponentUndoRedoAction extends AbstractUndoableEdit {

	private CellsDesigner designer;

	private DesignerConstraints cons;
	private Component comp;
	private Component oldScrollComp;
	boolean scrollDirty;

	public InsertComponentUndoRedoAction(
		CellsDesigner		designer,
		DesignerConstraints cons,
		Component			comp,
		boolean				scrollDirty,
		Component			oldScrollComp
	) {
		this.designer = designer;
		this.cons = cons;
		this.comp = comp;
		this.scrollDirty = scrollDirty;
		this.oldScrollComp = oldScrollComp;
	}

	public String getPresentationName() {
		return "Insert Component";
	}

	public void undo() {
		super.undo();

		DesignerConstraints newCons = new DesignerConstraints(cons);
		newCons.setComponent(comp);

		Action action = designer.getActionManager().getAction("DeleteComponent");
		action.putValue("Cons", newCons);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);

		if (cons.getComponent() != null) {

			if (cons.getComponent() instanceof JScrollPane) {
				JScrollPane sp = (JScrollPane) cons.getComponent();
				if (
					scrollDirty
					&& sp.getViewport().getComponentCount() > 0
					&& sp.getViewport().getComponent(0) != null
				) {
					comp = sp.getViewport().getComponent(0);
					sp.getViewport().remove(0);
					if (oldScrollComp != null) {
						sp.getViewport().add(oldScrollComp, 0);
					}
				}
			}

			action = designer.getActionManager().getAction("InsertComponent");
			action.putValue("Cons", cons);
			action.putValue("Comp", cons.getComponent());
			action.putValue("Undo", Boolean.TRUE);
			action.actionPerformed(null);
		}
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("InsertComponent");
		action.putValue("Cons", cons);
		action.putValue("Comp", comp);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
