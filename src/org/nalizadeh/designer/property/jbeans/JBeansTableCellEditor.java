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

import org.nalizadeh.designer.property.jbeans.editor.BooleanCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.BorderCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.ColorCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.DebugGraphicsOptionCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.DimensionCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.FontCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.HAlignmentCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.LocaleCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.NumberCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.StringCellEditor;
import org.nalizadeh.designer.property.jbeans.editor.VAlignmentCellEditor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

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
public class JBeansTableCellEditor {

	private Map typeToEditor;
	private Map propertyToEditor;

	public JBeansTableCellEditor(JBeansTable table) {
		typeToEditor = new HashMap();
		propertyToEditor = new HashMap();
		registerDefaults();
	}

	public TableCellEditor getCellEditor(JBeansTable table, int row, int column) {
		if (column == 0) {
			return null;
		}

		JBeansTableModel.Item item = table.getBeansModel().getItem(row);
		if (!item.isProperty()) {
			return null;
		}

		TableCellEditor result = null;
		JBeansProperty propery = item.getProperty();
		PropertyEditor editor = getEditor(propery);
		if (editor != null) {
			result = new CellEditorAdapter(editor);
		}

		return result;
	}

	/**
	 * Gets an editor for the given property.
	 */
	public synchronized PropertyEditor getEditor(JBeansProperty property) {

		PropertyEditor editor = null;
		Object value = propertyToEditor.get(property.getName());

		if (value instanceof PropertyEditor) {
			return (PropertyEditor) value;

		} else if (value instanceof Class) {
			try {
				editor = (PropertyEditor) ((Class) value).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			editor = getEditor(property.getType());
		}
		return editor;
	}

	/**
	 * Gets an editor for the given property type.
	 */
	public synchronized PropertyEditor getEditor(Class type) {
		PropertyEditor editor = null;
		Object value = typeToEditor.get(type);
		if (value instanceof PropertyEditor) {
			editor = (PropertyEditor) value;
		} else if (value instanceof Class) {
			try {
				editor = (PropertyEditor) ((Class) value).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return editor;
	}

	public synchronized void registerEditor(Class type, Class editorClass) {
		typeToEditor.put(type, editorClass);
	}

	public synchronized void registerEditor(Class type, PropertyEditor editor) {
		typeToEditor.put(type, editor);
	}

	public synchronized void unregisterEditor(Class type) {
		typeToEditor.remove(type);
	}

	public synchronized void registerEditor(String name, Class editorClass) {
		propertyToEditor.put(name, editorClass);
	}

	public synchronized void registerEditor(String name, PropertyEditor editor) {
		propertyToEditor.put(name, editor);
	}

	public synchronized void unregisterEditor(String name) {
		propertyToEditor.remove(name);
	}

	/**
	 * Adds default editors. This method is called by the constructor but may be called later to
	 * reset any customizations made through the <code>registerEditor</code> methods. <b>Note: if
	 * overriden, <code>super.registerDefaults()</code> must be called before plugging custom
	 * defaults.</b>
	 */
	public void registerDefaults() {
		typeToEditor.clear();
		propertyToEditor.clear();

		registerEditor(String.class, StringCellEditor.class);
		registerEditor(boolean.class, BooleanCellEditor.class);
		registerEditor(Boolean.class, BooleanCellEditor.class);
		registerEditor(Color.class, ColorCellEditor.class);
		registerEditor(Font.class, FontCellEditor.class);
		registerEditor(Border.class, BorderCellEditor.class);
		registerEditor(Dimension.class, DimensionCellEditor.class);

		registerEditor(short.class, NumberCellEditor.ShortCellEditor.class);
		registerEditor(Short.class, NumberCellEditor.ShortCellEditor.class);
		registerEditor(int.class, NumberCellEditor.IntegerCellEditor.class);
		registerEditor(Integer.class, NumberCellEditor.IntegerCellEditor.class);
		registerEditor(long.class, NumberCellEditor.LongCellEditor.class);
		registerEditor(Long.class, NumberCellEditor.LongCellEditor.class);
		registerEditor(double.class, NumberCellEditor.DoubleCellEditor.class);
		registerEditor(Double.class, NumberCellEditor.DoubleCellEditor.class);
		registerEditor(Locale.class, LocaleCellEditor.class);

		registerEditor("horizontalAlignment", HAlignmentCellEditor.class);
		registerEditor("horizontalTextPosition", HAlignmentCellEditor.class);
		registerEditor("verticalAlignment", VAlignmentCellEditor.class);
		registerEditor("verticalTextPosition", VAlignmentCellEditor.class);
		registerEditor("debugGraphicsOptions", DebugGraphicsOptionCellEditor.class);
	}

	/**
	 * Allows to use any PropertyEditor as a Table or Tree cell editor.<br>
	 */
	public static class CellEditorAdapter extends AbstractCellEditor implements TableCellEditor,
		TreeCellEditor
	{
		protected PropertyEditor editor;
		protected int clickCountToStart = 1;

		public CellEditorAdapter(PropertyEditor editor) {
			this.editor = editor;

			Component component = editor.getCustomEditor();

			if (component instanceof JTextField) {
				JTextField field = (JTextField) component;
				field.addFocusListener(new SelectOnFocus());
				field.addActionListener(new CommitEditing());
				field.registerKeyboardAction(
					new CancelEditing(),
					KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
					JComponent.WHEN_FOCUSED
				);
			}

			// when the editor notifies a change, commit the changes
			editor.addPropertyChangeListener(
				new PropertyChangeListener() {
					public void propertyChange(PropertyChangeEvent evt) {
						stopCellEditing();
					}
				}
			);
		}

		public Component getTreeCellEditorComponent(
			JTree   tree,
			Object  value,
			boolean selected,
			boolean expanded,
			boolean leaf,
			int		row
		) {
			return getEditor(value);
		}

		public Component getTableCellEditorComponent(
			JTable  table,
			Object  value,
			boolean selected,
			int		row,
			int		column
		) {
			return getEditor(value);
		}

		public void setClickCountToStart(int count) {
			clickCountToStart = count;
		}

		public int getClickCountToStart() {
			return clickCountToStart;
		}

		public Object getCellEditorValue() {
			return editor.getValue();
		}

		public boolean isCellEditable(EventObject event) {
			if (event instanceof MouseEvent) {
				return ((MouseEvent) event).getClickCount() >= clickCountToStart;
			}
			return true;
		}

		public boolean shouldSelectCell(EventObject event) {
			return true;
		}

		public boolean stopCellEditing() {
			fireEditingStopped();
			return true;
		}

		public void cancelCellEditing() {
			fireEditingCanceled();
		}

		private Component getEditor(Object value) {
			editor.setValue(value);

			final Component cellEditor = editor.getCustomEditor();

			// request focus later so the editor can be used to enter value as soon as
			// made visible
			SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						cellEditor.requestFocus();
					}
				}
			);

			return cellEditor;
		}

		class CommitEditing implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				stopCellEditing();
			}
		}

		class CancelEditing implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				cancelCellEditing();
			}
		}

		/**
		 * Select all text when focus gained, deselect when focus lost.
		 */
		class SelectOnFocus implements FocusListener {
			public void focusGained(final FocusEvent e) {
				if (!(e.getSource() instanceof JTextField)) {
					return;
				}
				SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							((JTextField) e.getSource()).selectAll();
						}
					}
				);
			}

			public void focusLost(final FocusEvent e) {
				if (!(e.getSource() instanceof JTextField)) {
					return;
				}
				SwingUtilities.invokeLater(
					new Runnable() {
						public void run() {
							((JTextField) e.getSource()).select(0, 0);
						}
					}
				);
			}
		}
	}
}
