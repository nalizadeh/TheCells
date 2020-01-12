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

package org.nalizadeh.designer.util;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import java.util.List;
import java.awt.event.KeyAdapter;

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
public class CellsCheckBoxList extends JList {

        private static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        private boolean[] selected;

        /**
         * Class constructor.
         * @param items Items with which to populate the list.
         */
        public CellsCheckBoxList(Object[] items) {
                super(items);

                selected = new boolean[items.length];
                for (int i = 0; i < items.length; i++) {
                        selected[i] = false;
                }

                setCellRenderer(new CheckBoxListCellRenderer2());
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                addMouseListener(
                        new MouseAdapter() {
                        public void mousePressed(MouseEvent e) {
                                doCheck();
                        }
                }
                );

                addKeyListener(
                        new KeyAdapter() {
                        public void keyPressed(KeyEvent e) {
                                if (e.getKeyChar() == ' ') {
                                        doCheck();
                                }
                        }
                }
                );
        }

        /**
         * Returns an array of the objects that have been selected.
         * Overrides the JList method.
         */
        public Object[] getSelectedValues() {
                return getSelected().toArray();
        }

        public List getSelected() {
                List list = new ArrayList(this.selected.length);
                for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                                list.add(getModel().getElementAt(i));
                        }
                }

                return list;
        }

        public void selectAll(boolean b) {
                for (int i = 0; i < selected.length; i++) {
                        selected[i] = b;
                }
                repaint();
        }

        private class CheckBoxListCellRenderer1 extends JCheckBox implements ListCellRenderer {

                public CheckBoxListCellRenderer1() {
                        setOpaque(true);
                        setBorder(noFocusBorder);
                }

                public Component getListCellRendererComponent(JList list,
                        Object value, int index, boolean isSelected, boolean cellHasFocus) {

                        setText(value.toString());
                        setSelected(selected[index]);
                        setFont(list.getFont());

                        if (isSelected) {
                                setBackground(list.getSelectionBackground());
                                setForeground(list.getSelectionForeground());
                        } else {
                                setBackground(list.getBackground());
                                setForeground(list.getForeground());
                        }

                        setBorder(noFocusBorder);

                        return this;
                }
        }


        private class CheckBoxListCellRenderer2 extends JComponent implements ListCellRenderer {

                DefaultListCellRenderer defaultComp;
                JCheckBox checkbox;

                public CheckBoxListCellRenderer2() {
                        setLayout(new BorderLayout());
                        defaultComp = new DefaultListCellRenderer();
                        checkbox = new JCheckBox();
                        add(checkbox, BorderLayout.WEST);
                        add(defaultComp, BorderLayout.CENTER);
                }

                public Component getListCellRendererComponent(JList list,
                        Object value,
                        int index,
                        boolean isSelected,
                        boolean cellHasFocus) {

                        defaultComp.getListCellRendererComponent(list, value, index,
                                isSelected, cellHasFocus);

                        checkbox.setSelected(selected[index]);
                        Component[] comps = getComponents();
                        for (int i = 0; i < comps.length; i++) {
                                comps[i].setForeground(list.getForeground());
                                comps[i].setBackground(list.getBackground());
                        }
                        return this;
                }
        }


        private void doCheck() {
                int index = getSelectedIndex();
                if (index != -1) {
                        selected[index] = !selected[index];
                }
                repaint();
        }

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		JFrame frame = new JFrame("CellsCheckBoxList Example");
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);
		frame.getContentPane().setLayout(null);

		CellsCheckBoxList ohr =
			new CellsCheckBoxList(new String[] { "One", "Tow", "Tree", "four", "fove", });

		ohr.setBounds(40, 40, 100, 200);
		frame.getContentPane().add(ohr);
		frame.setSize(300, 200);
		frame.setVisible(true);
	}
}
