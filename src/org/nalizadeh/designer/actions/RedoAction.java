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

import org.nalizadeh.designer.ActionManager;
import org.nalizadeh.designer.CellsDesigner;
import org.nalizadeh.designer.DebugPanel;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

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
public class RedoAction extends CellsAction {

	private CellsDesigner designer;

	public RedoAction(CellsDesigner designer) {
		super("Redo", CellsUtil.getImage("Redo"));
		this.designer = designer;
		putValue(SHORT_DESCRIPTION, "Redo");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_W));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		setEnabled(false);
	}

	public void init() {
	}

	public void actionPerformed(ActionEvent e) {
		ActionManager am = designer.getActionManager();
		UndoManager um = am.getUndoManager();
		if (um != null) {
			try {
				um.redo();
			} catch (CannotRedoException ex) {
				DebugPanel.printStackTrace(ex);
			}

			am.getAction("Undo").setEnabled(um.canUndo());
			am.getAction("Redo").setEnabled(um.canRedo());
		}
	}
}
