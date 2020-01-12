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

import org.nalizadeh.designer.util.CellsDialog;
import org.nalizadeh.designer.util.CellsUtil;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
public class CustomEditor extends JOriginalTable.DefaultEditorComponent {

	protected Object startCustomEditor(Component parent, Object value) {
		Window window = SwingUtilities.windowForComponent(parent);

		if (window instanceof Frame) {
			return DefaultPropertyDialog.showDialog((Frame) window, "Default Editor", value);
		} else {
			return DefaultPropertyDialog.showDialog((Dialog) window, "Default Editor", value);
		}
	}
}


class DefaultPropertyDialog extends CellsDialog {

	private JLabel label;
	private JTextArea input;

	private String name;
	private Object value;

	public DefaultPropertyDialog(Frame parent, String name, Object value) {
		super(parent, "Property Editor", false, true);
		this.name = name;
		this.value = value;
		initClass();
	}

	public DefaultPropertyDialog(Dialog parent, String name, Object value) {
		super(parent, "Property Editor", false, true);
		this.name = name;
		this.value = value;
		initClass();
	}

	private void initClass() {
		setSize(500, 300);

		input = new JTextArea(value == null ? "" : value.toString());
		label = new JLabel("Property name: " + name + "    (This feature is still not available!)");

		JPanel p1 = new JPanel(new BorderLayout(4, 8));
		p1.add(label, BorderLayout.NORTH);
		p1.add(new JScrollPane(input), BorderLayout.CENTER);
		setUserPanel(p1);
	}

	public Icon getActivityIcon() {
		return CellsUtil.getImage("Border32");
	}

	public String getActivityTitle() {
		return "Changing value of specified property";
	}

	public String getActivityDescription() {
		return "Your input will be used as source code to modify the property.";
	}

	public void doYesAction() {
//      value = input.getText();
	}

	public void doCancelAction() {
//                value = null;
	}

	public Object getValue() {
		return value;
	}

	public static Object showDialog(Frame parent, String name, Object value) {
		DefaultPropertyDialog f = new DefaultPropertyDialog(parent, name, value);
		f.setVisible(true);
		return f.getValue();
	}

	public static Object showDialog(Dialog parent, String name, Object value) {
		DefaultPropertyDialog f = new DefaultPropertyDialog(parent, name, value);
		f.setVisible(true);
		return f.getValue();
	}
}
