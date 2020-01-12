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

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.JComponent;

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
public class CellsDialog extends JDialog {

        public static final int APPROVE_OPTION = 0;
        public static final int DISCARD_OPTION = 1;
        public static final int CANCEL_OPTION = 2;

        private int closedState = APPROVE_OPTION;

        private JButton yesButton;
        private JButton noButton;
        private JButton cancelButton;

        public CellsDialog(Frame parent, String title) {
                this(parent, title, false, false);
        }

        public CellsDialog(Frame parent, String title, boolean useCancel) {
                this(parent, title, false, useCancel);
        }

        public CellsDialog(Frame parent, String title, boolean useNo, boolean useCancel) {
                super(parent, title, true);
                initClass(useNo, useCancel);
        }

        public CellsDialog(Dialog parent, String title) {
                this(parent, title, false, false);
        }

        public CellsDialog(Dialog parent, String title, boolean useCancel) {
                this(parent, title, false, useCancel);
        }

        public CellsDialog(Dialog parent, String title, boolean useNo, boolean useCancel) {
                super(parent, title, true);
                initClass(useNo, useCancel);
        }

        private void initClass(boolean useNo, boolean useCancel) {
                setSize(300, 200);

                addWindowListener(
                        new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                                closeAndCancel();
                        }
                }
                );

                ActionListener listener = new ActionListener() {
                        public final void actionPerformed(final ActionEvent e) {
                                closeAndCancel();
                        }
                };

                KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
                getRootPane().registerKeyboardAction(listener, keyStroke,
                        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

                Icon icon = getActivityIcon();
                String s1 = getActivityTitle();
                String s2 = getActivityDescription();

                JLabel lt = new JLabel(CellsUtil.getImage("DialogTitle"));
                JLabel l0 = new JLabel(icon);
                JLabel l1 = new JLabel(s1);
                JLabel l2 = new JLabel(s2);

                l1.setFont(new Font("Dialog", 1, 12));

                JPanel p0 =
                        new JPanel(
                                new CellsLayout(
                                        new double[][] { {15, CellsLayout.PREFERRED, CellsLayout.PREFERRED},
                                        {CellsLayout.FILL}
                }
                        )
                        );

                p0.add(l1, "1,0");
                p0.add(l2, "2,0");

                JPanel p1 =
                        new JPanel(
                                new CellsLayout(
                                        new double[][] { {60}, {5, CellsLayout.PREFERRED, 10, CellsLayout.FILL, 98}
                }
                        )
                        );

                p0.setBackground(Color.WHITE);
                p1.setBackground(Color.WHITE);
                p1.add(l0, "0,1");
                p1.add(p0, "0,3");
                p1.add(lt, "0,4");

                String t1 = getYesTitle();
                String t2 = useNo ? getNoTitle() : "";
                String t3 = useCancel ? getCancelTitle() : "";

                yesButton = new JButton(t1);
                yesButton.addActionListener(
                        new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                                closeAndYes();
                        }
                }
                );

                noButton = new JButton(t2);
                noButton.addActionListener(
                        new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                                closeAndNo();
                        }
                }
                );

                cancelButton = new JButton(t3);
                cancelButton.addActionListener(
                        new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                                closeAndCancel();
                        }
                }
                );

                getRootPane().setDefaultButton(yesButton);

                CellsLayout layout = new CellsLayout(4, 4);
                JPanel p2 = new JPanel(layout);
                layout.addRow(CellsLayout.PREFERRED);
                layout.addColumn(CellsLayout.FILL);

                int w1 = yesButton.getPreferredSize().width;
                int w2 = noButton.getPreferredSize().width;
                int w3 = cancelButton.getPreferredSize().width;

                int l = Math.max(w1, Math.max(w2, w3));

                layout.addColumn(l);
                if (useNo) {
                        layout.addColumn(l);
                        if (useCancel) {
                                layout.addColumn(l);
                        }
                } else if (useCancel) {
                        layout.addColumn(l);
                }

                layout.createCells();

                p2.add(yesButton, "0,1");

                if (useNo) {
                        p2.add(noButton, "0,2");
                        if (useCancel) {
                                p2.add(cancelButton, "0,3");
                        }
                } else if (useCancel) {
                        p2.add(cancelButton, "0,2");
                }

                setLayout(
                        new CellsLayout(
                                new double[][] { {60, CellsLayout.PREFERRED, CellsLayout.FILL, 30, 5},
                                {CellsLayout.FILL}
                }
                        )
                        );

                add(p1, "0,0");
                add(new JSeparator(), "1,0");
                add(p2, "3,0");
        }

        public Icon getActivityIcon() {
                return null;
        }

        public String getActivityTitle() {
                return "Your Activity Title gose here";
        }

        public String getActivityDescription() {
                return "Your Activity Description gose here";
        }

        public void setUserPanel(Component comp) {
                add(comp, "2,0");
        }

        private void closeAndYes() {
                closedState = APPROVE_OPTION;
                doYesAction();
                setVisible(false);
        }

        private void closeAndNo() {
                closedState = DISCARD_OPTION;
                doNoAction();
                setVisible(false);
        }

        private void closeAndCancel() {
                closedState = CANCEL_OPTION;
                doCancelAction();
                setVisible(false);
        }

        protected void doYesAction() {
        }

        protected void doNoAction() {
        }

        protected void doCancelAction() {
        }

        protected void setYesEnabled(boolean b) {
                yesButton.setEnabled(b);
        }

        protected void setNoEnabled(boolean b) {
                noButton.setEnabled(b);
        }

        protected void setCancelEnabled(boolean b) {
                cancelButton.setEnabled(b);
        }

        public boolean isApproved() {
                return closedState == APPROVE_OPTION;
        }

        public boolean isDiscarded() {
                return closedState == DISCARD_OPTION;
        }

        public boolean isCanceled() {
                return closedState == CANCEL_OPTION;
        }

        public String getYesTitle() {
                return "Ok";
        }

        public String getNoTitle() {
                return "Discard";
        }

        public String getCancelTitle() {
                return "Cancel";
        }

        public void setVisible(boolean b) {
                int w = getParent().getWidth() / 2 - getWidth() / 2;
                int h = getParent().getHeight() / 2 - getHeight() / 2;
                setLocation(getParent().getX() + w, getParent().getY() + h);
                super.setVisible(b);
        }

        //================== S E R V I C E

        public static void showMessageDialog(
                final Frame parent,
                final String title,
                final String description,
                final String msg,
                final int width,
                final int height
                ) {

                CellsDialog dlg =
                        new CellsDialog(parent, title, false, false) {
                        public Icon getActivityIcon() {
                                return CellsUtil.getImage("Question");
                        }

                        public String getActivityTitle() {
                                return title;
                        }

                        public String getActivityDescription() {
                                return description;
                        }

                };

                JLabel lab = new JLabel(msg);
                lab.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
                JPanel p = new JPanel();
                p.add(lab);
                dlg.setUserPanel(p);
                dlg.setSize(new Dimension(width, height));
                dlg.setVisible(true);

        }

        public static boolean showConfirmDialog(
                final Frame parent,
                final String title,
                final String description,
                final String msg,
                final int width,
                final int height
                ) {

                CellsDialog dlg =
                        new CellsDialog(parent, title, false, true) {
                        public Icon getActivityIcon() {
                                return CellsUtil.getImage("Question");
                        }

                        public String getActivityTitle() {
                                return title;
                        }

                        public String getActivityDescription() {
                                return description;
                        }

                };

                JLabel lab = new JLabel(msg);
                lab.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
                JPanel p = new JPanel();
                p.add(lab);
                dlg.setUserPanel(p);
                dlg.setSize(new Dimension(width, height));
                dlg.setVisible(true);

                return dlg.isApproved();
        }

        public static int showCompletConfirmDialog(
                final Frame parent,
                final String title,
                final String description,
                final String msg,
                final int width,
                final int height
                ) {

                CellsDialog dlg =
                        new CellsDialog(parent, title, true, true) {
                        public Icon getActivityIcon() {
                                return CellsUtil.getImage("Question");
                        }

                        public String getActivityTitle() {
                                return title;
                        }

                        public String getActivityDescription() {
                                return description;
                        }

                };

                JLabel lab = new JLabel(msg);
                lab.setBorder(BorderFactory.createEmptyBorder(20, 5, 5, 5));
                JPanel p = new JPanel();
                p.add(lab);
                dlg.setUserPanel(p);
                dlg.setSize(new Dimension(width, height));
                dlg.setVisible(true);

                return dlg.closedState;
        }

        public static int showDialog(
                final Frame parent,
                final String title,
                final String description,
                final String msg,
                final String yesText,
                final String noText,
                final int width,
                final int height
                ) {

                CellsDialog dlg =
                        new CellsDialog(parent, title, true, false) {
                        public Icon getActivityIcon() {
                                return CellsUtil.getImage("Question");
                        }

                        public String getActivityTitle() {
                                return title;
                        }

                        public String getActivityDescription() {
                                return description;
                        }

                        public String getYesTitle() {
                                return yesText;
                        }

                        public String getNoTitle() {
                                return noText;
                        }
                };

                JLabel lb = new JLabel(msg);
                lb.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 5));
                lb.setFont(new Font("Dialog", 0, 12));

                dlg.setUserPanel(lb);
                dlg.setSize(new Dimension(width, height));
                dlg.setVisible(true);

                return dlg.closedState;
        }

        public static int showDialog(
                final Frame parent,
                final String title,
                final String description,
                final String msg,
                final boolean useNo,
                final boolean useCancel,
                final String yesText,
                final String noText,
                final String cancelText,
                final int width,
                final int height
                ) {

                CellsDialog dlg =
                        new CellsDialog(parent, title, useNo, useCancel) {
                        public Icon getActivityIcon() {
                                return CellsUtil.getImage("Question");
                        }

                        public String getActivityTitle() {
                                return title;
                        }

                        public String getActivityDescription() {
                                return description;
                        }

                        public String getYesTitle() {
                                return yesText;
                        }

                        public String getNoTitle() {
                                return noText;
                        }

                        public String getCancelTitle() {
                                return cancelText;
                        }
                };

                JLabel lb = new JLabel(msg);
                lb.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 5));
                lb.setFont(new Font("Dialog", 0, 12));

                dlg.setUserPanel(lb);
                dlg.setSize(new Dimension(width, height));
                dlg.setVisible(true);

                return dlg.closedState;
        }
}
