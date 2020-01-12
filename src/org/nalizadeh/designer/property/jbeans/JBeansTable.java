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

package org.nalizadeh.designer.property.jbeans;

import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

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
public class JBeansTable extends JTable {

        private CellsDesigner designer;

        private JBeansTableModel model;

        private JBeansTableCellRenderer renderer;
        private JBeansTableCellEditor editor;

        private boolean useExtraIndent = false;

        /**
         * Konstruktor. Erzeugt eine Tabelle mit Überschrieften.
         *
         * @param
         *
         * @exception
         *
         * @return
         *
         * @see
         */
        public JBeansTable(CellsDesigner designer, int mode) {

                this.designer = designer;

                model = new JBeansTableModel(this, mode);
                setModel(model);

                setRowHeight(18);

                // select only one property at a time
                getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                // hide the table header, we do not need it
                Dimension nullSize = new Dimension(0, 0);
                getTableHeader().setPreferredSize(nullSize);
                getTableHeader().setMinimumSize(nullSize);
                getTableHeader().setMaximumSize(nullSize);
                getTableHeader().setVisible(false);

                setGridColor(UIManager.getColor(JBeansUtils.PANEL_BACKGROUND_COLOR_KEY));
                setIntercellSpacing(new Dimension(1, 1));
                setSurrendersFocusOnKeystroke(true);
                setShowGrid(false);

                // force the JTable to commit the edit when it losts focus
                putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

                renderer = new JBeansTableCellRenderer(this);
                editor = new JBeansTableCellEditor(this);
        }

        public final JBeansTableModel getBeansModel() {
                return (JBeansTableModel) getModel();
        }

        public void setComponent(String category, Component comp, Component oldComp) {
                setComponent(category, comp, oldComp);
        }

        public void setComponent(String category, Component comp, Component oldComp, String[] includes) {

                // cancel pending edits
                TableCellEditor editor = getCellEditor();
                if (editor != null) {
                        editor.cancelCellEditing();
                }

                model.setComponent(category, comp, oldComp, includes);
        }

        public void clear() {
                model.removeAll();
        }

        public boolean getUseExtraIndent() {
                return useExtraIndent;
        }

        public void setUseExtraIndent(boolean useExtraIndent) {
                this.useExtraIndent = useExtraIndent;
                repaint();
        }

        public TableCellEditor getCellEditor(int row, int column) {
                return editor.getCellEditor(this, row, column);
        }

        public TableCellRenderer getCellRenderer(int row, int column) {
                return renderer.getCellRenderer(this, row, column);
        }

        public void changeProperty(JBeansProperty prop) {

                // everytime a property change, update the comp with it

                if (designer == null) {

                        // standalone modus
                        prop.writeToObject(prop.getOwner());
                        prop.getOwner().repaint();
                        repaint();
                        return;
                }

                designer.getDebuger().appendMessage(
                        "change property (" + prop.getName() + "): "
                        + prop.getOldValue() + " --> "
                        + prop.getValue() + "\n");

                Action action = designer.getActionManager().getAction("ChangeJBeansProperty");
                action.putValue("Comp", prop.getOwner());
                action.putValue("Prop", prop);
                action.actionPerformed(null);
        }

        /**
         * @param
         *
         * @return
         *
         * @see
         */
        public static void main(String[] args) {

                JBeansProperty[] props = {
                        new JBeansDefaultProperty(
                                "Layout",
                                "foreground",
                                "foreground",
                                "",
                                Object.class,
                                "",
                                true
                        ),
                        new JBeansDefaultProperty(
                                "Layout",
                                "background",
                                "Background",
                                "",
                                Object.class,
                                "",
                                true
                        ),
                        new JBeansDefaultProperty(
                                "Property",
                                "Insets",
                                "Insets",
                                "",
                                Object.class,
                                "",
                                true
                        ),
                        new JBeansDefaultProperty(
                                "Property",
                                "Pref",
                                "description",
                                "",
                                Object.class,
                                "",
                                true
                        ),
                };

                try {
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception e) {
                }
                JFrame frame = new JFrame("JTime Example");
                frame.addWindowListener(
                        new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                                System.exit(0);
                        }
                }
                );

                JButton button = new JButton("Change my properties!");

                frame.getContentPane().setLayout(new BorderLayout());

                JBeansTable table = new JBeansTable(null, JBeansTableModel.VIEW_CATEGORIES);

                table.setComponent("Properties", button, null);
                table.getBeansModel().setCategoryIcon("Properties", CellsUtil.getImage("Property"));
                table.setUseExtraIndent(true);

                JScrollPane sp = new JScrollPane(table);

                frame.getContentPane().add(sp, BorderLayout.CENTER);
                frame.getContentPane().add(button, BorderLayout.SOUTH);

                frame.setSize(300, 600);
                frame.setVisible(true);
        }
}
