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

package org.nalizadeh.designer.base;

import org.nalizadeh.designer.CellsDesigner;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;

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
public class DesignerDragListener implements DragGestureListener, DragSourceListener {

	private CellsDesigner designer = null;

	public DesignerDragListener(CellsDesigner designer) {
		this.designer = designer;
	}

	public void dragGestureRecognized(DragGestureEvent e) {

		DesignerFrame df = designer.getDesktop().getDesigner();
		if (df != null) {

			DesignerConstraints cons = df.getContainer().getSelectedDesignerConstraints();

			if (cons != null) {
				try {
					e.startDrag(DragSource.DefaultCopyDrop, cons, this);
				} catch (InvalidDnDOperationException ex) {
				}
			}
		}
	}

	public void dragEnter(java.awt.dnd.DragSourceDragEvent dsde) {
	}

	public void dragExit(java.awt.dnd.DragSourceEvent dse) {
	}

	public void dragOver(java.awt.dnd.DragSourceDragEvent dsde) {
	}

	public void dragDropEnd(java.awt.dnd.DragSourceDropEvent e) {
	}

	public void dropActionChanged(java.awt.dnd.DragSourceDragEvent e) {
	}
}
