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
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

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
public class CopyAction extends CellsAction {

	private CellsDesigner designer;

	public CopyAction(CellsDesigner designer) {
		super("Copy", CellsUtil.getImage("Copy"));
		this.designer = designer;
		this.setEnabled(false);
		putValue(SHORT_DESCRIPTION, "Copy");
		putValue(MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
	}

	public void init() {
	}

	public void actionPerformed(ActionEvent e) {
		DesignerFrame df = designer.getDesktop().getDesigner();
		DesignerConstraints cons = df.getContainer().getSelectedDesignerConstraints();
                Component comp = (Component) CellsUtil.clone(cons.getComponent(), true, designer);
                clearNames(comp);
		cons.setComponent(comp);
		designer.getActionManager().setClipboard(cons);
		designer.getActionManager().update();
	}

        private void clearNames(Component comp) {
                comp.setName(null);
                if (comp instanceof Container) {
                        for (Component co : ((Container)comp).getComponents()) {
                                clearNames(co);
                        }
                }
        }
}
