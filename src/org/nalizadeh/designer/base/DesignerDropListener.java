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

import org.nalizadeh.designer.DebugPanel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

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
public class DesignerDropListener implements DropTargetListener {

	private DesignerContainer container = null;

	public DesignerDropListener(DesignerContainer container) {
		this.container = container;
	}

	public void drop(java.awt.dnd.DropTargetDropEvent e) {
		try {
			e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

			DataFlavor consFlavor = DesignerConstraints.CONSTRAINTS_FLAVOR;
			Transferable tr = e.getTransferable();

			if (e.isDataFlavorSupported(consFlavor)) {
				DesignerConstraints cons = (DesignerConstraints) tr.getTransferData(consFlavor);
				if (cons != null) {
					DebugPanel.appendMessage("Received Constraints (" + cons + ")");
					container.dragMove(cons.getComponent(), e.getLocation().x, e.getLocation().y);
				}
			}
		} catch (IOException ioe) {
		} catch (UnsupportedFlavorException ue) {

		}
	}

	public void dragExit(java.awt.dnd.DropTargetEvent e) {
	}

	public void dragOver(java.awt.dnd.DropTargetDragEvent e) {
		DataFlavor[] flavors = e.getCurrentDataFlavors();
		if (flavors != null && flavors.length > 0) {
			Transferable tr = e.getTransferable();
			try {
				DesignerConstraints cons = (DesignerConstraints) tr.getTransferData(flavors[0]);

				if (cons != null) {
					container.dargHints(e.getLocation().x, e.getLocation().y, cons.getComponent());
				}
			} catch (IOException ioe) {
			} catch (UnsupportedFlavorException ue) {
			}
		}
	}

	public void dragEnter(java.awt.dnd.DropTargetDragEvent e) {
	}

	public void dropActionChanged(java.awt.dnd.DropTargetDragEvent e) {
	}
}
