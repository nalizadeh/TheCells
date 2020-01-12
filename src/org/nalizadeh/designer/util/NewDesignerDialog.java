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

import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

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
public class NewDesignerDialog extends CellsDialog {
	private CellsDesigner designer;

	private JRadioButton rb1 = new JRadioButton("");
	private JRadioButton rb2 = new JRadioButton("");
	private JRadioButton rb3 = new JRadioButton("");

	private JLabel lb1 = new JLabel(CellsUtil.getImage("NewFrame"));
	private JLabel lb2 = new JLabel(CellsUtil.getImage("NewDialog"));
	private JLabel lb3 = new JLabel(CellsUtil.getImage("NewPanel"));

	private JLabel lbName = new JLabel("Name:");
	private JTextField tfName = new JTextField();

	private JLabel lbPackage = new JLabel("Package:");
	private JTextField tfPackage = new JTextField();

	private JLabel lbLayout = new JLabel("Layout:");
	private JComboBox<String> coLayout = new JComboBox<String>(CellsLayoutChooser.LayoutsNames);

	private JLabel lbBClass = new JLabel("Base class:");
	private JComboBox<String> coBClass =
		new JComboBox<String>(
			new String[] {
				"javax.swing.JPanel", //
				"javax.swing.JDialog", //
				"javax.swing.JFrame" //
			}
		);

	public NewDesignerDialog(Frame parent, CellsDesigner designer) {

		super(parent, "Create new Java Form class", false, true);

		this.designer = designer;

		setSize(350, 370);
		setYesEnabled(false);

		rb1.setToolTipText("javax.swing.JFrame");
		rb2.setToolTipText("javax.swing.JDialog");
		rb3.setToolTipText("javax.swing.JPanel");

		rb1.setHorizontalAlignment(JRadioButton.CENTER);
		rb2.setHorizontalAlignment(JRadioButton.CENTER);
		rb3.setHorizontalAlignment(JRadioButton.CENTER);

		ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		bg.add(rb3);
		rb3.setSelected(true);

		tfPackage.setText(CellsUtil.EXM_PATH);

		// till next release
		rb1.setEnabled(false);
		rb2.setEnabled(false);
		coBClass.setEnabled(false);

		rb1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					coBClass.setSelectedIndex(2);
				}
			}
		);

		rb2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					coBClass.setSelectedIndex(1);
				}
			}
		);

		rb3.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					coBClass.setSelectedIndex(0);
				}
			}
		);

		tfName.addKeyListener(
			new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					setYesEnabled(tfName.getText().length() > 0);
				}
			}
		);

		JPanel p1 =
			new JPanel(
				new CellsLayout(
					new double[][] {
						{ 22, 22, 22, 22 },
						{
							CellsLayout.FILL, CellsLayout.PREFERRED, 50, CellsLayout.PREFERRED, 50,
							20, CellsLayout.FILL
						}
					},
					4,
					4
				)
			);

		p1.add(lbName, "0, 1, 0, 0, 0, 2, 1");
		p1.add(tfName, "0, 2, 0, 0, 3, 2, 1");
		p1.add(lbPackage, "1, 1, 0, 0, 0, 2, 1");
		p1.add(tfPackage, "1, 2, 0, 0, 3, 2, 1");
		p1.add(lbLayout, "2, 1, 0, 0, 0, 2, 1");
		p1.add(coLayout, "2, 2, 0, 0, 3, 2, 1");
		p1.add(lbBClass, "3, 1, 0, 0, 0, 2, 1");
		p1.add(coBClass, "3, 2, 0, 0, 3, 2, 1");

		p1.setPreferredSize(new Dimension(300, 270));

		JPanel p2 =
			new JPanel(
				new CellsLayout(
					new double[][] {
						{
							10, CellsLayout.PREFERRED, CellsLayout.PREFERRED, 10,
							CellsLayout.PREFERRED
						},
						{ 40, 80, 80, 80, 40 }
					},
					4,
					4
				)
			);

		p2.add(lb1, "1,1");
		p2.add(lb2, "1,2");
		p2.add(lb3, "1,3");
		p2.add(rb1, "2,1");
		p2.add(rb2, "2,2");
		p2.add(rb3, "2,3");
		p2.add(p1, "4,1,0,0,2");

		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					tfName.requestFocus();
				}
			}
		);

		setUserPanel(p2);
	}

	public Icon getActivityIcon() {
		return CellsUtil.getImage("New32");
	}

	public String getActivityTitle() {
		return "Creating new Java class";
	}

	public String getActivityDescription() {
		return "Choose a new Form and Layout";
	}

	public void doYesAction() {
		if (!tfName.getText().equals("")) {
			designer.getDesktop().newDesignerFrame(
				tfName.getText(),
				tfPackage.getText(),
				rb3.isSelected() ? 1 : rb2.isSelected() ? 2 : rb1.isSelected() ? 3 : 1,
				(String) coLayout.getSelectedItem()
			);
		}
	}
}
