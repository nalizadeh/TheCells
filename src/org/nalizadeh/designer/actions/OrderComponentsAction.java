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

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
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
public class OrderComponentsAction extends CellsAction {

	private CellsDesigner designer;

	public OrderComponentsAction(CellsDesigner designer) {
		super("Order Components");
		this.designer = designer;
	}

	public void init() {
		putValue("Cont", null);
		putValue("Components", null);
		putValue("Locations", null);
		putValue("Undo", null);
	}

	public void actionPerformed(ActionEvent e) {

		Container cont = (Container) getValue("Cont");
		Component[] comps = (Component[]) getValue("Components");
		Point[] locs = (Point[]) getValue("Locations");
		Boolean undo = (Boolean) getValue("Undo");

		Point[] oldLocs = new Point[comps.length];

		for (int i = 0; i < comps.length; i++) {
			oldLocs[i] = comps[i].getLocation();
			comps[i].setLocation(locs[i].x, locs[i].y);
		}

		cont.repaint();

		if (undo == null) {
			designer.getActionManager().undoableEditHappened(
				new OrderComponentsUndoRedoAction(designer, cont, comps, oldLocs, locs)
			);
		}
	}
}


class OrderComponentsUndoRedoAction extends AbstractUndoableEdit {
	private CellsDesigner designer;
	private Container cont;
	private Component[] comps;
	private Point[] oldLocs;
	private Point[] newLocs;

	public OrderComponentsUndoRedoAction(
		CellsDesigner designer,
		Container	  cont,
		Component[]   comps,
		Point[]		  oldLocs,
		Point[]		  newLocs
	) {
		this.designer = designer;
		this.cont = cont;
		this.comps = comps;
		this.oldLocs = oldLocs;
		this.newLocs = newLocs;
	}

	public String getPresentationName() {
		return "Order Components";
	}

	public void undo() {
		super.undo();
		Action action = designer.getActionManager().getAction("OrderComponents");
		action.putValue("Cont", cont);
		action.putValue("Components", comps);
		action.putValue("Locations", oldLocs);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}

	public void redo() {
		super.redo();
		Action action = designer.getActionManager().getAction("OrderComponents");
		action.putValue("Cont", cont);
		action.putValue("Components", comps);
		action.putValue("Locations", newLocs);
		action.putValue("Undo", Boolean.TRUE);
		action.actionPerformed(null);
	}
}
