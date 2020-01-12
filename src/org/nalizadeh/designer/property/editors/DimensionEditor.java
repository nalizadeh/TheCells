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

package org.nalizadeh.designer.property.editors;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
public class DimensionEditor extends JOriginalTable.DefaultEditorComponent {

	private static final ImageIcon icon = JOriginalTable.getImage("numeditor.gif");

	private JPanel panel;
	private JSpinner sp1;
	private JSpinner sp2;

	private JPanel rendpanel;
	private JLabel label1;
	private JLabel label2;
	private JLabel iclab1;
	private JLabel iclab2;

	private boolean adjusting = false;

	public DimensionEditor() {
		sp1 =
			new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1)) {
				public void setValue(Object value) {
					super.setValue(value);
				}
			};

		sp2 =
			new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1)) {
				public void setValue(Object value) {
					super.setValue(value);
				}
			};

		sp1.addChangeListener(
			new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (!adjusting) {
						fireEditingStopped(DimensionEditor.this);
						sp1.transferFocus();
					}
				}
			}
		);

		sp2.addChangeListener(
			new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (!adjusting) {
						fireEditingStopped(DimensionEditor.this);
						sp2.transferFocus();
					}
				}
			}
		);

		sp1.setBorder(null);
		sp2.setBorder(null);
		sp1.setFocusable(false);
		sp2.setFocusable(false);

		JSpinner.DefaultEditor ed1 = (JSpinner.DefaultEditor) sp1.getEditor();
		ed1.getTextField().setHorizontalAlignment(JLabel.LEFT);

		JSpinner.DefaultEditor ed2 = (JSpinner.DefaultEditor) sp2.getEditor();
		ed2.getTextField().setHorizontalAlignment(JLabel.LEFT);

		panel =
			new JPanel(
				new CellsLayout(new double[][] {
						{ 16 },
						{ CellsLayout.FILL, CellsLayout.FILL }
					}
				)
			);

		panel.add(sp1, "0,0");
		panel.add(sp2, "0,1");

		label1 = new JLabel();
		label2 = new JLabel();
		label1.setOpaque(true);
		label2.setOpaque(true);

		iclab1 = new JLabel(icon);
		iclab2 = new JLabel(icon);

		rendpanel =
			new JPanel(
				new CellsLayout(
					new double[][] {
						{ 16 },
						{ CellsLayout.FILL, 16, CellsLayout.FILL, 16 }
					}
				)
			);

		rendpanel.setOpaque(true);
		rendpanel.setBorder(null);

		rendpanel.add(label1, "0,0");
		rendpanel.add(iclab1, "0,1");
		rendpanel.add(label2, "0,2");
		rendpanel.add(iclab2, "0,3");
	}

	public Component getEditorComponent() {
		return panel;
	}

	public Object getEditorValue() {
		return new Dimension(
			((Integer) sp1.getValue()).intValue(),
			((Integer) sp2.getValue()).intValue()
		);
	}

	public void setEditorValue(Object value) {
		adjusting = true;
		Dimension d = (Dimension) value;
		sp1.setValue(new Double(d.getWidth()).intValue());
		sp2.setValue(new Double(d.getHeight()).intValue());
		label1.setText(sp1.getValue().toString());
		label2.setText(sp2.getValue().toString());
		adjusting = false;
	}

	public Component getRendererComponent(
		Component parent,
		Object    value,
		boolean   isEditable,
		Color	  fc,
		Color	  bc
	) {
		setEditorValue(value);

		label1.setBackground(bc);
		label2.setBackground(bc);
		label1.setForeground(fc);
		label2.setForeground(fc);
		label1.setEnabled(isEditable);
		label2.setEnabled(isEditable);
		iclab1.setEnabled(isEditable);
		iclab2.setEnabled(isEditable);
		rendpanel.setBackground(bc);
		return rendpanel;
	}
}
