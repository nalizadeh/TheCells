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

import org.nalizadeh.designer.property.editors.AligmentXEditor;
import org.nalizadeh.designer.property.editors.AligmentYEditor;
import org.nalizadeh.designer.property.editors.AnchorEditor;
import org.nalizadeh.designer.property.editors.BorderEditor;
import org.nalizadeh.designer.property.editors.ColorEditor;
import org.nalizadeh.designer.property.editors.CustomEditor;
import org.nalizadeh.designer.property.editors.DimensionEditor;
import org.nalizadeh.designer.property.editors.FillEditor;
import org.nalizadeh.designer.property.editors.FontEditor;
import org.nalizadeh.designer.property.editors.HorizontalAligmentEditor;
import org.nalizadeh.designer.property.editors.InsetsEditor;
import org.nalizadeh.designer.property.editors.LocaleEditor;
import org.nalizadeh.designer.property.editors.OrientationEditor;
import org.nalizadeh.designer.property.editors.SplitOrientationEditor;
import org.nalizadeh.designer.property.editors.TabPlacementEditor;
import org.nalizadeh.designer.property.editors.VerticalAligmentEditor;
import org.nalizadeh.designer.property.editors.HorizontalScrollPolicyEditor;
import org.nalizadeh.designer.property.editors.VerticalScrollPolicyEditor;

import org.nalizadeh.designer.util.CellsUtil;
import org.nalizadeh.designer.util.otable.JOriginalCheckBoxTree;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.Locale;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
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
public class PropertySheetTable extends JOriginalTable {

	private static final Icon iconLeaf = CellsUtil.getImage("ptableaf.gif");

	private static final Icon iconUnchangedLeaf =
		new JOriginalCheckBoxTree.DoubleIcon(iconLeaf, CellsUtil.getImage("ptabunchangedLeaf.gif"));

	private static final Icon iconChangedLeaf =
		new JOriginalCheckBoxTree.DoubleIcon(iconLeaf, CellsUtil.getImage("ptabchangedLeaf.gif"));

	private static final Font font1 = new Font("Arial", 1, 12);
	private static final Font font2 = new Font("Arial", Font.ITALIC, 12);
	private static final Font font3 = new Font("Arial", Font.BOLD | Font.ITALIC, 12);
	private static final Color color1 = new Color(210, 210, 210);
	private static final Color color2 = new Color(140, 140, 140);

	private static final String[] colNames = { "Attribute", "Value" };

	private PropertySheet sheet;
	private Map<String, Property> props;
	private MyTableCellRenderer myTableRenderer;

	public PropertySheetTable(PropertySheet psheet) {
		super(colNames);

		this.sheet = psheet;

		setShowCheckBox(false);
		setShowGroupSelection(false);
		setShowDetailPopup(false);
		setRowHeight(18);

		setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		getColumn("Attribute").setPreferredWidth(180);
		getColumn("Value").setPreferredWidth(100);

		myTableRenderer = new MyTableCellRenderer();

		getTree().setCellRenderer(new MyTreeCellRenderer());

		JOriginalTableModel model = (JOriginalTableModel) getModel();

		model.registerEditor(Object.class, CustomEditor.class);
		model.registerEditor(Color.class, ColorEditor.class);
		model.registerEditor(Font.class, FontEditor.class);
		model.registerEditor(Border.class, BorderEditor.class);
		model.registerEditor(Dimension.class, DimensionEditor.class);
		model.registerEditor(Insets.class, InsetsEditor.class);
		model.registerEditor(Locale.class, LocaleEditor.class);
		model.registerEditor("horizontalAlignment", HorizontalAligmentEditor.class);
		model.registerEditor("verticalAlignment", VerticalAligmentEditor.class);
		model.registerEditor("hAlignment", HorizontalAligmentEditor.class);
		model.registerEditor("vAlignment", VerticalAligmentEditor.class);
		model.registerEditor("horizontalTextPosition", HorizontalAligmentEditor.class);
		model.registerEditor("verticalTextPosition", VerticalAligmentEditor.class);
		model.registerEditor("alignmentX", AligmentXEditor.class);
		model.registerEditor("alignmentY", AligmentYEditor.class);
		model.registerEditor("componentOrientation", OrientationEditor.class);
		model.registerEditor("orientation", SplitOrientationEditor.class);
		model.registerEditor("tabPlacement", TabPlacementEditor.class);
                model.registerEditor("horizontalScrollBarPolicy", HorizontalScrollPolicyEditor.class);
                model.registerEditor("verticalScrollBarPolicy", VerticalScrollPolicyEditor.class);
		model.registerEditor("Anchor", AnchorEditor.class);
		model.registerEditor("Fill", FillEditor.class);
		model.registerEditor("rowOverlapping", OverlappingEditor.class);
		model.registerEditor("columnOverlapping", OverlappingEditor.class);
	}

	public TableCellRenderer getCellRenderer(int row, int column) {
		return myTableRenderer;
	}

	protected void update(Map<String, Property> props) {

		this.props = props;

		int index = 0, s = getSelectedRow();
		clear();

		JOriginalTableNode node1 = addNode("Constraints", new Object[] { "", "" }, 1, index++);
		JOriginalTableNode node2 = addNode("Properties", new Object[] { "", "" }, 1, index++);

		for (Property prop : props.values()) {
			if (prop.isLayoutProperty) {
				node1.addNode(prop.name, new Object[] { prop.name, prop.newValue }, 1, index++);
			} else {
				node2.addNode(prop.name, new Object[] { prop.name, prop.newValue }, 1, index++);
			}
		}

		init();
		expandToLevel(2);
		if (s != -1 && s < getRowCount()) {
			setRowSelectionInterval(s, s);
		}
	}

	protected void checkNode(String key, boolean checked) {
		JOriginalTableNode node = getNode(key);
		getTree().check(node, checked);
	}

	protected JOriginalTableNodeListener createDefaultNodeListener() {

		return new JOriginalTableNodeAdapter() {

			public boolean isEditable(JOriginalTableNode node, int row, int column) {
				if (node.getKey().equals("Constraints") || node.getKey().equals("Properties")) {
					return false;
				}
				Property ac = props.get(node.getKey());
				return !ac.readOnly;
			}

			public Class getEditorType(JOriginalTableNode node, int row, int column) {
				Property ac = props.get(node.getKey());
				return ac == null ? Object.class : ac.type;
			}

			public void editorValueChanged(JOriginalTableNode node, Object oldValue, Object newValue) {
				sheet.changeProperty(node.getKey(), oldValue, newValue);
			}
		};
	}

	private class MyTableCellRenderer extends JOriginalTable.JOriginalTableCellRenderer {

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {
			Component c =
				super.getTableCellRendererComponent(
					table,
					value,
					isSelected,
					hasFocus,
					row,
					column
				);

			if (column == 0 && !isSelected) {
				c.setBackground(color1);

			} else {
				JOriginalTable.JOriginalTableNode node = ((JOriginalTable) table).getNodeAtRow(row);
				Object key = node == null ? "" : node.getKey();
				Property ac = props.get(key);
				if (ac != null && ac.readOnly) {
					c.setForeground(color2);
					c.setFont(font2);
				}
			}

			return c;
		}
	}

	private class MyTreeCellRenderer extends JOriginalTable.JOriginalTableTreeCellRenderer {

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
			Object key = node == null ? "" : node.getKey();

			if (key.equals("Constraints")) {
				setFont(font1);
				setIcon(CellsUtil.getImage("Cell"));

			} else if (key.equals("Properties")) {
				setFont(font1);
				setIcon(CellsUtil.getImage("Properties"));

			} else if (!key.equals("")) {
				Property ac = props.get(key);
				if (ac.changed) {
					setFont(font3);
					getTree().check(node);
					setIcon(tree, value, leaf, iconChangedLeaf);

					if (ac.readOnly) {
						setForeground(color2);
					}
				} else {
					if (ac.readOnly) {
						setForeground(color2);
					}
					setIcon(tree, value, leaf, iconUnchangedLeaf);
				}
			}

			if (!selected) {
				setBackgroundNonSelectionColor(color1);
			}

			setText(key.toString());

			return this;
		}
	}

	public static class OverlappingEditor extends NumberEditorComponent.IntegerEditorComponent {
		public OverlappingEditor() {
			super(0, 0, 100, 1);
		}
	}
}
