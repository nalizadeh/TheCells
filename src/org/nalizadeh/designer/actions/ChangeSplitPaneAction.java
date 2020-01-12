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
import javax.swing.JSplitPane;

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
public class ChangeSplitPaneAction extends CellsAction {

        private CellsDesigner designer;

        public ChangeSplitPaneAction(CellsDesigner designer) {
                super("Change SplitPane");
                this.designer = designer;
        }

        public void init() {
                putValue("Cons", null);
                putValue("Comp", null);
                putValue("Mode", null);
                putValue("Add", null);
                putValue("Undo", null);
        }

        public void actionPerformed(ActionEvent evt) {

                DesignerConstraints cons = (DesignerConstraints) getValue("Cons");
                Component comp = (Component) getValue("Comp");
                int mode = (Integer) getValue("Mode");
                Boolean add = (Boolean) getValue("Add");
                Boolean undo = (Boolean) getValue("Undo");

                JSplitPane sp = (JSplitPane) cons.getComponent();

                if (add) {
                        if (mode == 1) {
                                sp.setLeftComponent(comp);
                        } else {
                                sp.setRightComponent(comp);
                        }
                } else {
                        sp.remove(comp);
                }

                designer.getDesktop().getDesigner().getContainer().selectDesignerConstraints(cons);
                designer.update();

                if (undo == null) {
                        designer.getActionManager().undoableEditHappened(
                                new ChangeSplitPaneRedoAction(designer, cons, comp, mode, add)
                        );
                }
        }
}


class ChangeSplitPaneRedoAction extends AbstractUndoableEdit {

        private CellsDesigner designer;
        private DesignerConstraints cons;
        private Component comp;
        private int mode;
        private boolean add;

        public ChangeSplitPaneRedoAction(
                CellsDesigner		designer,
                DesignerConstraints cons,
                Component			comp,
                int					mode,
                boolean				add
        ) {
                this.designer = designer;
                this.cons = cons;
                this.comp = comp;
                this.mode = mode;
                this.add = add;
        }

        public String getPresentationName() {
                return "Change TabbedPane";
        }

        public void undo() {
                super.undo();

                Action action = designer.getActionManager().getAction("ChangeSplitPane");
                action.putValue("Cons", cons);
                action.putValue("Comp", comp);
                action.putValue("Mode", mode);
                action.putValue("Add", !add);
                action.putValue("Undo", Boolean.TRUE);
                action.actionPerformed(null);
        }

        public void redo() {
                super.redo();

                Action action = designer.getActionManager().getAction("ChangeSplitPane");
                action.putValue("Cons", cons);
                action.putValue("Comp", comp);
                action.putValue("Mode", mode);
                action.putValue("Add", add);
                action.putValue("Undo", Boolean.TRUE);
                action.actionPerformed(null);
        }
}
