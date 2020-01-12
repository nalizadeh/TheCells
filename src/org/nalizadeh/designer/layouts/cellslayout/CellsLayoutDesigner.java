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

package org.nalizadeh.designer.layouts.cellslayout;

import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.base.DesignerFrame;

import java.awt.Container;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

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
public class CellsLayoutDesigner extends DesignerFrame {

        protected static final int ROW_HEIGHT = 16;
        protected static final int COL_WIDTH = 16;

        private CellsLayoutContainer container = null;
        private CellsLayoutRows rowPanel = null;
        private CellsLayoutColumns colPanel = null;

        public CellsLayoutDesigner(
                String title,
                String name,
                Container container,
                CellsDesigner designer,
                JInternalFrame parent
                ) {
                super(title, name, container, designer, parent);
                this.container = new CellsLayoutContainer(this, true, container);
                initClass();
        }

        private void initClass() {
                rowPanel = new CellsLayoutRows(this);
                colPanel = new CellsLayoutColumns(this);

                CellsLayout layout = new CellsLayout();

                layout.addRow(CellsLayoutDesigner.ROW_HEIGHT);
                layout.addRow(CellsLayout.FILL);
                layout.addColumn(CellsLayoutDesigner.COL_WIDTH);
                layout.addColumn(CellsLayout.FILL);
                layout.createCells();

                JPanel panel = new JPanel();
                panel.setLayout(layout);
                panel.add(colPanel, "0,1");
                panel.add(rowPanel, "1,0");
                panel.add(container, "1,1");

                initUserArea(container, panel);

                update();
        }

        public CellsLayoutRows getRowPanel() {
                return rowPanel;
        }

        public CellsLayoutColumns getColumnPanel() {
                return colPanel;
        }

        public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);
                rowPanel.setEnabled(enabled);
                colPanel.setEnabled(enabled);
        }

        public void update() {
                super.update();
                rowPanel.update(false);
                colPanel.update(false);
        }

        public void deselectAll() {
                super.deselectAll();
                rowPanel.selectRow(-1);
                colPanel.selectColumn(-1);
        }

        public void redraw() {
                super.redraw();
                rowPanel.revalidate();
                colPanel.revalidate();
        }
}
