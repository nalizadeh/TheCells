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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import java.util.List;

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
public class ConfirmClosingDialog extends CellsDialog {

	private CellsCheckBoxList cb;

	public ConfirmClosingDialog(CellsDesigner designer, List dfs) {

		super((Frame) designer, "Save", false, true);

		setSize(350, 350);

		cb = new CellsCheckBoxList(dfs.toArray());
		cb.selectAll(true);

		JRadioButton r1 = new JRadioButton("Save all");
		r1.setSelected(true);
		r1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					cb.selectAll(true);
				}
			}
		);

		JRadioButton r2 = new JRadioButton("Discard all");
		r2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					cb.selectAll(false);
				}
			}
		);

		ButtonGroup bg = new ButtonGroup();
		bg.add(r1);
		bg.add(r2);

		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(r1);
		p1.add(r2);

		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));
		p.add(new JScrollPane(cb), BorderLayout.CENTER);
		p.add(p1, BorderLayout.SOUTH);
		setUserPanel(p);
	}

	public Icon getActivityIcon() {
		return CellsUtil.getImage("New32");
	}

	public String getActivityTitle() {
		return "Save...";
	}

	public String getActivityDescription() {
		return "Save changed files";
	}

	public List getChecked() {
		return cb.getSelected();
	}
}
