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

package org.nalizadeh.designer.property;

import org.nalizadeh.designer.property.editors.CustomEditor;

import org.nalizadeh.designer.util.CellsUtil;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;

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
public class EventSheetTable extends JOriginalTable {

	private static final Icon iconLeaf = CellsUtil.getImage("ptableaf.gif");
	private static final Icon iconNotLeaf = CellsUtil.getImage("ptabLeve1.gif");

	private static final Font font = new Font("Dialog", Font.BOLD | Font.ITALIC, 12);
	private static final Color color = new Color(120, 120, 120);

	private static final String[] colNames = { "Event", "Value" };

	private PropertySheet sheet;
	private Map<String, Property> events;

	public EventSheetTable(PropertySheet psheet) {
		super(colNames);
		this.sheet = psheet;

		setShowCheckBox(true);
		setShowGroupSelection(true);
		setShowDetailPopup(false);
		setRowHeight(18);

		setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

		getColumn("Event").setPreferredWidth(180);
		getColumn("Value").setPreferredWidth(100);

		getTree().setCellRenderer(new MyTreeRenderer());
	}

	protected void update(Map<String, Property> events) {

		this.events = events;

		ArrayList<TreeNode> checked = new ArrayList<TreeNode>();
		int s = getSelectedRow();
		clear();
		
		int index = 0;
		for (Property prop : events.values()) {

			if (prop.comp != null) {
				JOriginalTableNode node = addNode(prop, new Object[] { "", "" }, 1, index++);

				if (prop.changed) {
					checked.add(node);
				}

				Method[] ms = prop.ed.getListenerMethods();
				for (Method m : ms) {
					prop = events.get(m.getName());
					node.addNode(prop, new Object[] { "", "" }, 1, index++);
				}
			}
		}

		init();
		getTree().check(checked);

		if (s != -1 && s < getRowCount()) {
			setRowSelectionInterval(s, s);
		}
	}

	protected JOriginalTableNodeListener createDefaultNodeListener() {

		return new JOriginalTableNodeAdapter() {

			public boolean isEditable(JOriginalTableNode node, int row, int column) {
				if (column == 1) {
					Property ac = events.get(node.getKey().toString());
					return !ac.readOnly;
				}
				return false;
			}

			public EditorComponent getEditor(JOriginalTableNode node, int row, int column) {
				EditorComponent ec = null;
				if (column == 1) {
					Property ac = events.get(node.getKey().toString());
					if (!ac.readOnly) {
						ec = new CustomEditor();
					}
				}
				return ec;
			}

			public void apply(JOriginalTableNode node) {
			}

			public boolean isCheckBoxEnabled(JOriginalTableNode node) {
				Property ac = events.get(node.getKey().toString());
				return !ac.readOnly;
			}

			public void checkBoxValueChanged(JOriginalTableNode node) {
				Property ac = events.get(node.getKey().toString());
				ac.changed = getTree().isChecked(node);
			}
		};
	}

	private class MyTreeRenderer extends JOriginalTable.JOriginalTableTreeCellRenderer {

		public Component getTreeCellRendererComponent(
			JTree   tree,
			Object  value,
			boolean selected,
			boolean expanded,
			boolean leaf,
			int		row,
			boolean hasFocus
		) {
			super.getTreeCellRendererComponent(
				tree,
				value,
				selected,
				expanded,
				leaf,
				row,
				hasFocus
			);

			JOriginalTable.JOriginalTableNode node = ((JOriginalTable.JOriginalTableTree) tree).getNodeAtRow(row);

			setIcon(
				tree,
				value,
				leaf,
				node == null ? null : node.isLeaf() ? iconLeaf : iconNotLeaf
			);

			setText("");

			if (node != null) {
				Property prop = events.get(node.getKey().toString());
				if (prop != null) {
					if (prop.readOnly) {
						setForeground(color);
					} else if (prop.changed) {
						setFont(font);
					}
				}
				setText(node.getKey().toString());
			}

			return this;
		}
	}
}
