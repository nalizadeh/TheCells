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

package org.nalizadeh.designer.util.otable;

import org.nalizadeh.designer.util.beans.JCalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * <p>Title:</p>
 *
 * TreeTable displays the hierarchical data in a table and provides for sorting and editing data.
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2007 N.A.J</p>
 *
 * <p>Organization:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class JOriginalTable extends JTable implements TreeExpansionListener {

	private static final long serialVersionUID = 1L;

	private static final String IMGDIR = "images/";

	private static final Color ERROR_COLOR = new Color(255, 192, 192);
	private static final Color MANDATORY_COLOR = new Color(255, 240, 192);

	private static final ImageIcon expIcon = getImage("expand.gif");
	private static final ImageIcon colIcon = getImage("collapse.gif");
	private static final ImageIcon iconAsc = getImage("ascanding.gif");
	private static final ImageIcon iconDes = getImage("descanding.gif");

	// colors and fonts to rendering the cells
	private static final Color color1 = new Color(242, 240, 236);
	private static final Color color2 = new Color(234, 232, 228);
	private static final Color color3 = new Color(200, 200, 200);
	private static final Color color4 = new Color(200, 200, 200);

	private static final Font font1 = new Font("Arial", 0, 12);
	private static final Font font2 = new Font("Arial", 1, 12);

	public static final Font EDITOR_FONT = new Font("Arial", 0, 12);

	// sorting
	public static final int SORTDIR_ASCENDING = 1;
	public static final int SORTDIR_DESCENDING = -1;
	public static final int SORTDIR_NO = 0;
	private int sortDir = SORTDIR_NO;
	private int sortingColumn = -1;

	// the comparators used by sorting
	private Map<Class<Object>, Comparator<Object>> defaultComparators = new HashMap<Class<Object>, Comparator<Object>>();
	private Map<Integer, Comparator<Object>> columnComparators = new HashMap<Integer, Comparator<Object>>();

	// expand/collapse buttons
	private int expandingLevel;
	private JButton expandButton;
	private JButton collapseButton;

	// titles of columns
	private String[] columnNames;

	// the tree within the table
	private JOriginalTableTree tree;

	// the main renderer and editor
	private JOriginalTableCellRenderer renderer;
	private JOriginalTableCellEditor editor;

	private JOriginalCellAttribute attributes;

	private boolean showDetailPopup = false;
	private boolean showGroupSelection = true;

	private boolean selectionAdjusting = false;

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTable(String[] columnNames) {
		this.columnNames = columnNames;
		initClass(createDefaultNodeListener());
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTable(String[] columnNames, JOriginalTableNodeListener listener) {
		this(columnNames, listener, false);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTable(
		String[]				   columnNames,
		JOriginalTableNodeListener listener,
		boolean					   attributive
	) {
		this.columnNames = columnNames;
		this.attributes = attributive ? new JOriginalCellAttribute() : null;
		initClass(listener);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void initClass(JOriginalTableNodeListener listener) {

		tree = new JOriginalTableTree(listener);
		tree.setRowHeight(getRowHeight());
		tree.setShowCheckBox(true);
		tree.addTreeExpansionListener(this);

		renderer = new JOriginalTableCellRenderer();
		editor = new JOriginalTableCellEditor(this);

		setModel(new JOriginalTableModel(listener));
		setAutoResizeMode(AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setIntercellSpacing(new Dimension(1, 1));
		setSurrendersFocusOnKeystroke(true);
		setShowGrid(false);

		if (attributes != null) {
			setUI(new JOriginalAttributiveTableUI());
			getTableHeader().setReorderingAllowed(false);
//          setCellSelectionEnabled(true);
//          setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		}

		//===================

		// this is my implementation of the selection sharing
		// between table and tree, it works excellent!

		getSelectionModel().addListSelectionListener(
			new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!selectionAdjusting) {

						selectionAdjusting = true;

						tree.setSelectionRows(getSelectedRows());

						// making of Block-Selektion
						JOriginalTableNode[] nodes = getSelectedNodes();
						if (nodes != null) {
							tree.select(nodes);
							revalidate();
							repaint();
						}
						selectionAdjusting = false;
					}
				}
			}
		);

		tree.setSelectionModel(
			new DefaultTreeSelectionModel() {

				private static final long serialVersionUID = 1L;

				public void setSelectionPaths(TreePath[] paths) {
					super.setSelectionPaths(paths);
					selectRows(tree.getSelectionRows());
				}
			}
		);

		//===================

		addMouseListener(new JOriginalTableMouseListener());
		addKeyListener(new JOriginalTableKeyListener());

		JOriginalTableHeaderListener columnNamesListener = new JOriginalTableHeaderListener();
		getTableHeader().addMouseListener(columnNamesListener);

		TableColumnModel cmodel = getColumnModel();
		cmodel.addColumnModelListener(columnNamesListener);

		Enumeration<TableColumn> columns = cmodel.getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			column.setHeaderRenderer(new JOriginalTableHeaderRenderer(column.getModelIndex()));
		}

		// expand / collapse Buttons
		AbstractAction expandAction =
			new AbstractAction("", expIcon) {

				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					if (expandingLevel < tree.getDepth()) {
						expandToLevel(expandingLevel + 1);
					}
				}
			};

		AbstractAction collapseAction =
			new AbstractAction("", colIcon) {

				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
					if (expandingLevel > 1) {
						collapseToLevel(expandingLevel - 1);
					}
				}
			};

		expandButton = new JButton(expandAction);
		collapseButton = new JButton(collapseAction);

		expandButton.setBorder(null);
		collapseButton.setBorder(null);
		expandButton.setEnabled(false);
		collapseButton.setEnabled(false);
		expandButton.setFocusable(false);
		collapseButton.setFocusable(false);
		
		expandButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		collapseButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));	
	}

	public void setSelectionMode(int mode) {

		super.setSelectionMode(mode);

		switch (mode) {

			case ListSelectionModel.SINGLE_SELECTION :
				tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				break;

			case ListSelectionModel.SINGLE_INTERVAL_SELECTION :
				tree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.CONTIGUOUS_TREE_SELECTION
				);
				break;

			case ListSelectionModel.MULTIPLE_INTERVAL_SELECTION :
				tree.getSelectionModel().setSelectionMode(
					TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION
				);
				break;
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public TableCellEditor getCellEditor(int row, int column) {
		return editor;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public TableCellRenderer getCellRenderer(int row, int column) {
		return renderer;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public int getEditingRow() {
		return (getColumnClass(editingColumn) == JOriginalTableTree.class) ? -1 : editingRow;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	protected JOriginalTableNodeListener createDefaultNodeListener() {
		return new JOriginalTableNodeAdapter();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void initExpandCollapseButtonsState() {
		expandButton.setEnabled(tree.getRowCount() > 0 && expandingLevel < tree.getDepth());
		collapseButton.setEnabled(tree.getRowCount() > 0 && expandingLevel > 1);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void treeExpanded(TreeExpansionEvent event) {
		initExpandCollapseButtonsState();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void treeCollapsed(TreeExpansionEvent event) {
		initExpandCollapseButtonsState();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void setShowCheckBox(boolean show) {
		tree.setShowCheckBox(show);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void setShowDetailPopup(boolean show) {
		this.showDetailPopup = show;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void setShowGroupSelection(boolean showGroupSelection) {
		this.showGroupSelection = showGroupSelection;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void init(Object[][] data, int aggrigationLevel) {
		tree.init(data, aggrigationLevel);
		reset();

		if (attributes != null) {
			attributes.setSize(new Dimension(data.length, data[0].length));
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void init(List<Object[]> data, int aggrigationLevel) {
		tree.init(data, aggrigationLevel);
		reset();

		if (attributes != null) {
			attributes.setSize(new Dimension(data.size(), data.get(0).length));
		}
	}
	
	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void init() {
		tree.init();
		reset();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTableNode addNode(Object key, Object[] data, int aggrigationLevel, int index) {
		return tree.addNode(key, data, aggrigationLevel, index);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void clear() {
		tree.clear();
		reset();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public Map<Object, Object[]> getData() {
		return tree.getData();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void setRowHeight(int rowHeight) {
		super.setRowHeight(rowHeight);
		if (tree != null) {
			tree.setRowHeight(rowHeight);
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void sort(int columnIndex) {

		if (tree.getRowCount() == 0) {
			return;
		}

		if (sortingColumn != -1 && sortingColumn != columnIndex) {
			sortDir = SORTDIR_NO;
		}
		sortDir =
			sortDir == SORTDIR_NO ? SORTDIR_ASCENDING
			: sortDir == SORTDIR_ASCENDING ? SORTDIR_DESCENDING : SORTDIR_NO;

		sortingColumn = columnIndex;

		tree.sort(getComparator(sortingColumn), sortingColumn, sortDir);

		scrollSelectionToVisible();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void setComparators(
		Map<Class<Object>, Comparator<Object>>   defaultComparators,
		Map<Integer, Comparator<Object>> columnComparators
	) {
		this.defaultComparators.putAll(defaultComparators);
		this.columnComparators.putAll(columnComparators);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private Comparator<Object> getComparator(int columnIndex) {
		Comparator<Object> comparator = columnComparators.get(columnIndex);
		if (comparator == null) {
			Class<?> cl = getModel().getColumnClass(columnIndex);
			comparator = defaultComparators.get(cl);
			if (comparator == null) {
				comparator = defaultComparators.get(cl.getSuperclass());
			}
			if (comparator == null) {
				comparator = new JOriginalTableComprator<Object>();
			}
		}
		return comparator;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public Object getValue(String columnName) {
		for (int i = 0; i < columnNames.length; i++) {
			if (columnNames[i].equals(columnName)) {
				return getModel().getValueAt(getSelectedRow(), i);
			}
		}
		return null;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTableNode getSelectedNode() {
		return getNodeAtRow(getSelectedRow());
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTableNode[] getSelectedNodes() {
		JOriginalTableNode[] nodes = null;
		int[] sels = getSelectedRows();
		if (sels != null && sels.length > 0) {
			nodes = new JOriginalTableNode[sels.length];
			for (int i = 0; i < sels.length; i++) {
				nodes[i] = tree.getNodeAtRow(sels[i]);
			}
		}
		return nodes;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public TreePath[] getSelectedPaths() {
		JOriginalTableNode[] nodes = getSelectedNodes();
		if (nodes != null && nodes.length > 0) {
			TreePath[] paths = new TreePath[nodes.length];
			for (int i = 0; i < nodes.length; i++) {
				paths[i] = new TreePath(nodes[i].getPath());
			}
			return paths;
		}
		return null;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void selectRows(int[] rows) {
		clearSelection();
		if (rows != null) {
			for (int row : rows) {
				addRowSelectionInterval(row, row);
			}
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTableNode getNodeAtRow(int row) {
		return tree.getNodeAtRow(row);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTableNode getNode(Object key) {
		return tree.getNode(key);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTableTree getTree() {
		return tree;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalTableModel getTreeTableModel() {
		return (JOriginalTableModel) getModel();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public int getNodeCount() {
		return tree.getNodeCount();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean doubleClickAllowed() {
		int r = getSelectedRow();
		int c = getSelectedColumn();
		return r != -1 && getColumnClass(c) != JOriginalTableTree.class;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void expandToLevel(int level) {
		if (level <= tree.getDepth()) {
			expandingLevel = level;
			tree.expandToLevel(level);
			redraw();
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void collapseToLevel(int level) {
		if (level >= 1) {
			expandingLevel = level;
			tree.collapseToLevel(level);
			redraw();
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void reset() {
		sortDir = SORTDIR_NO;
		sortingColumn = -1;
		expandingLevel = 1;
		redraw();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void redraw() {
		initExpandCollapseButtonsState();
		tree.revalidate();
		revalidate();
		getTableHeader().repaint();
		repaint();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void scrollSelectionToVisible() {
		int row = getSelectedRow();
		int column = getSelectedColumn();
		if (row >= 0 && column >= 0) {
			scrollRectToVisible(getCellRect(row, column, true));
		}
		getTableHeader().repaint();
		repaint();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void combineCells(int[] rows, int[] columns) {
		if (attributes != null) {
			attributes.combine(rows, columns);
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void splitCells(int row, int column) {
		if (attributes != null) {
			attributes.split(row, column);
		}
	}

	//=====================================================================
	// TreeTableComprator
	//=====================================================================

	private class JOriginalTableComprator<T> implements Comparator<T> {
		public int compare(T o1, T o2) {
			if (o1 == null && o2 == null) {
				return 0;
			}
			if (o1 == null && o2 != null) {
				return -1;
			}
			if (o1 != null && o2 == null) {
				return 1;
			}
			return o1.toString().compareTo(o2.toString());
		}

		public boolean equals(Object obj) {
			return obj == null ? false : toString().equals(obj.toString());
		}
	}

	//=====================================================================
	// TreeTableModel
	//=====================================================================

	public class JOriginalTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private JOriginalTableNodeListener listener;

		private Map<Class<?>, EditorComponent> typeToRenderer;
		private Map<Object, EditorComponent> nameToRenderer;

		private Map<Class<?>, Class<?>> typeToEditor;
		private Map<Object, Class<?>> nameToEditor;

		public JOriginalTableModel(JOriginalTableNodeListener listener) {
			this.listener = listener;

			typeToRenderer = new HashMap<Class<?>, EditorComponent>();
			nameToRenderer = new HashMap<Object, EditorComponent>();

			typeToEditor = new HashMap<Class<?>, Class<?>>();
			nameToEditor = new HashMap<Object, Class<?>>();

			registerDefaultEditors();
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		public int getColumnCount() {
			return columnNames.length;
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		public int getRowCount() {
			return tree.getRowCount();
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		public Object getValueAt(int row, int column) {
			return tree.getNodeAtRow(row).getData()[column];
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		public void setValueAt(Object value, int row, int column) {
			JOriginalTableNode node = tree.getNodeAtRow(row);
			Object oldValue = node.getData()[column];
			node.getData()[column] = value;
			listener.editorValueChanged(node, oldValue, value);
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		public String getColumnName(int column) {
			return columnNames[column];
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		public Class<? extends Object> getColumnClass(int column) {
			if (column == 0) return tree.getClass();
			Object v = getValueAt(0, column);
			return v == null ? null : v.getClass();
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		public boolean isCellEditable(int row, int column) {
			if (column == 0) {
				return true;
			}
			return listener.isEditable(tree.getNodeAtRow(row), row, column);
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		private EditorComponent getRendererAt(
			JOriginalTableNode node,
			int				   row,
			int				   column,
			boolean			   isEditable
		) {
			EditorComponent editor = listener.getEditor(node, row, column);
			if (editor == null) {
				editor = getRenderer(node.getKey());
				if (editor == null) {
					editor = getRenderer(listener.getEditorType(node, row, column));
					if (editor == null && isEditable) {
						editor = getRenderer(Object.class);
					}
				}
			}
			return editor;
		}

		/**
		 * @param
		 *
		 * @return
		 *
		 * @exception
		 *
		 * @see
		 */
		private EditorComponent getEditorAt(JOriginalTableNode node, int row, int column) {
			EditorComponent editor = listener.getEditor(node, row, column);
			if (editor != null) {
				editor = listener.getEditorInstance(editor);
			} else {
				editor = getEditor(node.getKey());
				if (editor == null) {
					editor = getEditor(listener.getEditorType(node, row, column));
					if (editor == null) {
						editor = getEditor(Object.class);
					}
				}
			}
			return editor;
		}

		public synchronized void registerEditor(Class<?> type, Class<?> editorClass) {
			typeToEditor.put(type, editorClass);
			try {
				registerRenderer(type, (EditorComponent) editorClass.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public synchronized void registerEditor(Object key, Class<?> editorClass) {
			nameToEditor.put(key, editorClass);
			try {
				registerRenderer(key, (EditorComponent) editorClass.newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public synchronized void unregisterEditor(Class<?> type) {
			typeToEditor.remove(type);
			typeToRenderer.remove(type);
		}

		public synchronized void unregisterEditor(Object key) {
			nameToEditor.remove(key);
			nameToRenderer.remove(key);
		}

		public synchronized void registerRenderer(Class<?> type, EditorComponent editor) {
			typeToRenderer.put(type, editor);
		}

		public synchronized void registerRenderer(Object key, EditorComponent editor) {
			nameToRenderer.put(key, editor);
		}

		public synchronized EditorComponent getRenderer(Class<?> type) {
			return typeToRenderer.get(type);
		}

		public synchronized EditorComponent getRenderer(Object key) {
			return nameToRenderer.get(key);
		}

		public synchronized EditorComponent getEditor(Class<?> type) {
			EditorComponent editor = null;
			Class<?> c = typeToEditor.get(type);
			if (c != null) {
				try {
					editor = (EditorComponent) c.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return editor;
		}

		public synchronized EditorComponent getEditor(Object key) {
			EditorComponent editor = null;
			Class<?> c = nameToEditor.get(key);
			if (c != null) {
				try {
					editor = (EditorComponent) c.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return editor;
		}

		public void registerDefaultEditors() {
			unregisterDefaultEditors();
			registerEditor(String.class, TextEditorComponent.class);
			registerEditor(boolean.class, BooleanEditorComponent.class);
			registerEditor(Boolean.class, BooleanEditorComponent.class);
			registerEditor(short.class, NumberEditorComponent.ShortEditorComponent.class);
			registerEditor(Short.class, NumberEditorComponent.ShortEditorComponent.class);
			registerEditor(int.class, NumberEditorComponent.IntegerEditorComponent.class);
			registerEditor(Integer.class, NumberEditorComponent.IntegerEditorComponent.class);
			registerEditor(long.class, NumberEditorComponent.LongEditorComponent.class);
			registerEditor(Long.class, NumberEditorComponent.LongEditorComponent.class);
			registerEditor(double.class, NumberEditorComponent.DoubleEditorComponent.class);
			registerEditor(Double.class, NumberEditorComponent.DoubleEditorComponent.class);
			registerEditor(float.class, NumberEditorComponent.FloatEditorComponent.class);
			registerEditor(Float.class, NumberEditorComponent.FloatEditorComponent.class);
		}

		public void unregisterDefaultEditors() {
			typeToRenderer.clear();
			nameToRenderer.clear();
			typeToEditor.clear();
			nameToEditor.clear();
		}
	}

	//=====================================================================
	// TreeTableCellRenderer
	//=====================================================================

	public class JOriginalTableCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {
			setValue(value);

			JOriginalTableNode node = tree.getNodeAtRow(row);
			Color c1, c2;

			if (isSelected) {
				c1 = table.getSelectionForeground();
				c2 = table.getSelectionBackground();
			} else {
				c1 = node == null ? color1 : node.selected && showGroupSelection ? color3 : color1;
				c2 = node == null ? color2 : node.selected && showGroupSelection ? color4 : color2;

				c2 = row % 2 == 0 ? c1 : c2;
				c1 = table.getForeground();
			}

			// rendering tree column
			if (table.getColumnClass(column) == JOriginalTableTree.class) {
				tree.paintingRow = row;
				tree.setForeground(c1);
				tree.setBackground(c2);
				return tree;
			}

			// rendering via editor, if any exists
			int col = convertColumnIndexToModel(column);
			JOriginalTableModel model = (JOriginalTableModel) table.getModel();
			boolean editable = model.isCellEditable(row, col);
			EditorComponent editor = model.getRendererAt(node, row, col, editable);

			if (editor != null) {
				return editor.getRendererComponent(table, value, editable, c1, c2);
			}

			String tooltip = null;
			if (value != null) {
				StringBuffer sb = new StringBuffer();
				createToolTip(value.toString(), sb);
				tooltip = "<html>" + sb.toString() + "</html>";
			}

			// the rest of columns
			setForeground(c1);
			setBackground(c2);
			setFont(font1);
			setToolTipText(tooltip);

			if (!isSelected && node != null && node.selected && node.level == 1) {
				setFont(font2);
			}

			return this;
		}

		private void createToolTip(String s, StringBuffer sb) {
			int n = Math.min(s.length(), 60);
			sb.append(s.substring(0, n) + "<br>");
			if (n == 60) {
				createToolTip(s.substring(n), sb);
			}
		}
	}

	//=====================================================================
	// TreeTableCellEditor
	//=====================================================================

	public class JOriginalTableCellEditor implements TableCellEditor {

		private JOriginalTable treetable;
		private EditorComponent editor;
		private ChangeEvent changeEvent;

		public JOriginalTableCellEditor(JOriginalTable treetable) {
			this.treetable = treetable;
			this.changeEvent = new ChangeEvent(this);
		}

		public Component getTableCellEditorComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			int		row,
			int		column
		) {
			if (getColumnClass(column) == JOriginalTableTree.class) {
				return tree;
			}

			JOriginalTableNode node = tree.getNodeAtRow(row);
			JOriginalTableModel model = (JOriginalTableModel) table.getModel();
			editor = model.getEditorAt(node, row, convertColumnIndexToModel(column));
			if (editor != null) {
				Color c1 = table.getSelectionForeground();
				Color c2 = table.getSelectionBackground();
				editor.setEditorValue(value);
				editor.setEditorListener(this);
				editor.getRendererComponent(table, value, true, c1, c2);
				return editor.getEditorComponent();
			}
			return null;
		}

		public boolean isCellEditable(EventObject e) {

			if (e instanceof MouseEvent) {

				MouseEvent me = (MouseEvent) e;

				for (int i = 0; i < getColumnCount(); i++) {
					if (getColumnClass(i) == JOriginalTableTree.class) {
						Rectangle rect = getCellRect(0, i, true);
						if (me.getX() >= rect.x && me.getX() <= (rect.x + rect.width)) {

							// forward event to the tree
							tree.dispatchEvent(
								new MouseEvent(
									tree,
									me.getID(),
									me.getWhen(),
									me.getModifiers(),
									me.getX() - rect.x,
									me.getY() - rect.y,
									me.getClickCount(),
									me.isPopupTrigger()
								)
							);

							// mark table as changed
							JOriginalTable.this.revalidate();
							return false;
						}
					}
				}
			}
			return true; // return false if keyboard problem
		}

		public Object getCellEditorValue() {
			return editor == null ? null : editor.getEditorValue();
		}

		public boolean shouldSelectCell(EventObject evt) {
			return true;
		}

		public boolean stopCellEditing() {
			fireEditingStopped();
			return true;
		}

		public void cancelCellEditing() {
			fireEditingCanceled();
		}

		public void addCellEditorListener(CellEditorListener l) {
		}

		public void removeCellEditorListener(CellEditorListener l) {
		}

		private void fireEditingStopped() {
			treetable.editingStopped(changeEvent);
		}

		private void fireEditingCanceled() {
			treetable.editingCanceled(changeEvent);
		}
	}

	//=====================================================================
	// TreeTableTreeCellRenderer
	//=====================================================================

	public class JOriginalTableTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;

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

			JOriginalTableNode node = ((JOriginalTableTree) tree).getNodeAtRow(row);
			if (node != null) {
				setText(node.getKey().toString());
				setIcon(tree, value, leaf, node.listener.getIcon(node));
			}

			setFont(font1);

			if (selected) {
				setTextSelectionColor(getSelectionForeground());
				setBackgroundSelectionColor(getSelectionBackground());
			} else {
				Color c1 =
					node == null ? color1 : node.selected && showGroupSelection ? color3 : color1;
				Color c2 =
					node == null ? color2 : node.selected && showGroupSelection ? color4 : color2;

				setBackgroundNonSelectionColor(row % 2 == 0 ? c1 : c2);

				if (!selected && node != null && node.selected && node.getLevel() == 1) {
					setFont(font2);
				}
			}

			// Bug of JTree
			setPreferredSize(new Dimension(800, tree.getRowHeight()));

			return this;
		}

		protected void setIcon(JTree tree, Object value, boolean leaf, Icon icon) {
			super.setIcon(((JOriginalTableTree) tree).createDoubleIcon(value, leaf, icon));
		}
	}

	//=====================================================================
	// TreeTableHeaderRenderer
	//=====================================================================

	private class JOriginalTableHeaderRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;
		private int modelIndex;

		public JOriginalTableHeaderRenderer() {
			this(-1);
		}

		public JOriginalTableHeaderRenderer(int modelIndex) {
			this.modelIndex = modelIndex;
		}

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			JTableHeader th = table.getTableHeader();
			setForeground(th.getForeground());
			setBackground(th.getBackground());
			setFont(th.getFont());
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(SwingConstants.CENTER);

			if (sortingColumn == modelIndex) {
				ImageIcon icon = null;
				if (sortDir == SORTDIR_ASCENDING) {
					icon = iconAsc;
				} else if (sortDir == SORTDIR_DESCENDING) {
					icon = iconDes;
				}

				setIcon(icon);
			} else {
				setIcon(null);
			}

			// Show Expand/Collapse-Buttons
			if (convertColumnIndexToModel(column) == 0) {
				setBorder(null);

				JPanel pa = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
				pa.setPreferredSize(new Dimension(200, 20));
				pa.setForeground(th.getForeground());
				pa.setBackground(th.getBackground());
				pa.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
				pa.add(expandButton);
				pa.add(collapseButton);
				pa.add(this);
				return pa;
			}

			return this;
		}
	}

	//=====================================================================
	// TreeTableHeaderListener
	//=====================================================================

	private class JOriginalTableHeaderListener extends MouseAdapter
		implements TableColumnModelListener
	{
		public void mouseClicked(MouseEvent e) {
			if ((e.getClickCount() == 1) && (e.getModifiers() == InputEvent.BUTTON1_MASK)) {

				int column = getColumnModel().getColumnIndexAtX(e.getX());
				int columnIndex = convertColumnIndexToModel(column);

				if (columnIndex == 0) {

					int x = e.getX() - getTableHeader().getHeaderRect(column).x;

					if (expandButton.getBounds().contains(x, e.getY())) {
						expandButton.doClick();
						e.consume();
						return;
					} else if (collapseButton.getBounds().contains(x, e.getY())) {
						collapseButton.doClick();
						e.consume();
						return;
					}
				}

				sort(columnIndex);
			}
		}

		public void columnAdded(TableColumnModelEvent e) {
			TableColumn column = ((TableColumnModel) e.getSource()).getColumn(e.getToIndex());
			column.setHeaderRenderer(new JOriginalTableHeaderRenderer(column.getModelIndex()));
		}

		public void columnRemoved(TableColumnModelEvent e) {
		}

		public void columnMoved(TableColumnModelEvent e) {
		}

		public void columnMarginChanged(ChangeEvent e) {
		}

		public void columnSelectionChanged(ListSelectionEvent e) {
		}
	}

	//=====================================================================
	// TreeTableMouseListener
	//=====================================================================

	private class JOriginalTableMouseListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			if (showDetailPopup && e.isPopupTrigger()) {

				int row = tree.getRowForLocation(e.getX(), e.getY());

				if (row != -1) {
					setRowSelectionInterval(row, row);
					JPopupMenu popup = new JPopupMenu();
					popup.add(getDetailPopupPanel(row));
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}
	}

	//=====================================================================
	// TreeTableKeyListener
	//=====================================================================

	private class JOriginalTableKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			int row = getSelectedRow();
			if (row != -1) {
				if (e.getKeyCode() == KeyEvent.VK_PLUS) {
					tree.expandRow(row);
					setRowSelectionInterval(row, row);
					redraw();
				} else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
					tree.collapseRow(row);
					setRowSelectionInterval(row, row);
					redraw();
				}
			}
		}

		public void keyReleased(KeyEvent e) {
		}
	}

	//=====================================================================
	// Detail-Popup
	//=====================================================================

	private JPanel getDetailPopupPanel(int row) {

		JOriginalTableNode node =
			(JOriginalTableNode) tree.getPathForRow(row).getLastPathComponent();

		List<Object> keys = new ArrayList<Object>();
		node.getKeyPath(keys);
		String tit = "";
		for (int i = 1; i < keys.size(); i++) {
			tit = tit + keys.get(i) + " ";
		}

		final Object[] data = node.getData();
		final String titel = tit;

		DefaultTableModel dm =
			new DefaultTableModel(columnNames.length, 2) {

				private static final long serialVersionUID = 1L;

				public Object getValueAt(int r, int c) {
					return c == 0 ? columnNames[r] : data[r];
				}

				public String getColumnName(int c) {
					return c == 0 ? titel : "...";
				}

				public boolean isCellEditable(int r, int c) {
					return false;
				}
			};

		JTable table = new JTable(dm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

//      table.setDefaultRenderer(Object.class, new MyTableCellRenderer());
//      table.setRowHeight(20);
//      table.setIntercellSpacing(new Dimension(1, 1));
//      table.setShowGrid(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumn(titel).setPreferredWidth(150);
		table.getColumn("...").setPreferredWidth(200);

		JOriginalTableHeaderRenderer hr = new JOriginalTableHeaderRenderer();
		table.getColumn(titel).setHeaderRenderer(hr);
		table.getColumn("...").setHeaderRenderer(hr);

		JScrollPane scroll = new JScrollPane(table);
		JPanel panel = new JPanel(new BorderLayout());
		int w = table.getPreferredSize().width + 5;
		int h = (data.length - 1) * table.getRowHeight() + 8;
		panel.setPreferredSize(new Dimension(w, h));
		panel.add(scroll, BorderLayout.CENTER);
		return panel;
	}

	//=====================================================================
	// TreeTableTree
	//=====================================================================

	public class JOriginalTableTree extends JOriginalCheckBoxTree
		implements JOriginalCheckBoxTree.CheckBoxTreeListener
	{
		private static final long serialVersionUID = 1L;
		private JOriginalTableNode root;
		private DefaultTreeModel model;
		private int paintingRow;
		private Object lastSelection;

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public JOriginalTableTree(JOriginalTableNodeListener listener) {
			this.root = new JOriginalTableNode(listener);
			this.model = new DefaultTreeModel(root);
			this.lastSelection = null;

			setModel(model);
			setEditable(false);
			setRootVisible(false);
			setShowsRootHandles(true);
			setCellRenderer(new JOriginalTableTreeCellRenderer());
			setShowCheckBox(true);

			addCheckBoxTreeListener(this);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private JOriginalTableNode addNode(Object key, Object[] data, int aggrigationLevel, int index) {
			return root.addNode(key, data, aggrigationLevel, index);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void init(Object[][] data, int aggrigationLevel) {

			clear();
			root.clear();

			for (int i = 0; i < data.length; i++) {
				root.addNode(data[i], aggrigationLevel, i);
			}
			root.init();

			model.setRoot(root);

			if (lastSelection != null) {
				root.select(this, lastSelection, aggrigationLevel);
				lastSelection = null;
			}
			reset();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void init(List<Object[]> data, int aggrigationLevel) {

			clear();
			root.clear();

			for (int i = 0; i < data.size(); i++) {
				root.addNode(data.get(i), aggrigationLevel, i);
			}
			root.init();

			model.setRoot(root);

			if (lastSelection != null) {
				root.select(this, lastSelection, aggrigationLevel);
				lastSelection = null;
			}
			reset();
		}
		
		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void init() {
			root.init();
			model.setRoot(root);
			revalidate();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void clear() {
			lastSelection = null;
			JOriginalTableNode[] nodes = getSelectedNodes();
			if (nodes != null) {
				Object[] sels = new Object[nodes.length];
				for (int i = 0; i < nodes.length; i++) {
					sels[i] = new Object();
					sels[i] = nodes[i].getData();
				}
				lastSelection = sels;
			}
			root.clear();
			model.setRoot(root);
			revalidate();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private Map<Object, Object[]> getData() {
			HashMap<Object, Object[]> map = new HashMap<Object, Object[]>();
			root.getData(map);
			return map;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public boolean getShowCheckBox(Object node) {
			return ((JOriginalTableNode)node).isCheckable();
		}
		
		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void sort(Comparator<Object> comparator, int columnIndex, int sortDirection) {

			JOriginalTableNode node = getSelectedNode();
			TreePath path = node == null ? null : new TreePath(node.getPath());

			Enumeration<TreePath> en = getExpandedDescendants(new TreePath(root));

			if (sortDirection == SORTDIR_NO) {
				root.init();
			} else {
				root.sort(comparator, columnIndex, sortDirection);
			}

			model.reload();

			// expand the last expanded rows
			while (en != null && en.hasMoreElements()) {
				expandPath((TreePath) en.nextElement());
			}

			// select the last selected row
			if (path != null) {
				setSelectionPath(path);
			}

			revalidate();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void expandToLevel(int level) {
			JOriginalTableNode node = getSelectedNode();
			TreePath path = node == null ? null : new TreePath(node.getPath());

			root.expand(this, level);

			if (path != null) {
				setSelectionPath(path);
			}

			revalidate();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void collapseToLevel(int level) {
			JOriginalTableNode node = getSelectedNode();
			TreePath path = node == null ? null : new TreePath(node.getPath());

			root.collapse(this, level);

			if (path != null) {
				setSelectionPath(path);
			}
			revalidate();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public JOriginalTableNode getNodeAtRow(int row) {
			TreePath path = getPathForRow(row);
			if (path != null) {
				Object o = path.getLastPathComponent();
				if (o instanceof JOriginalTableNode) {
					return (JOriginalTableNode) o;
				}
			}
			return null;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public JOriginalTableNode getNode(Object key) {
			return root.getNode(key);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private int getDepth() {
			return root.getDepth();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void select(JOriginalTableNode node) {
			JOriginalTableNode n = node;
			while (!root.equals(n) && !root.equals(n.parentNode)) {
				n = n.parentNode;
			}

			n.setSelected(true);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void select(JOriginalTableNode[] nodes) {
			root.setSelected(false);
			for (JOriginalTableNode node : nodes) {
				select(node);
			}
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private int getNodeCount() {
			return root.getChilds().size();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void setRowHeight(int rowHeight) {
			if (rowHeight > 0) {
				super.setRowHeight(rowHeight);
				if (JOriginalTable.this.getRowHeight() != rowHeight) {
					JOriginalTable.this.setRowHeight(getRowHeight());
				}
			}
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void setBounds(int x, int y, int w, int h) {
			super.setBounds(x, y, w, JOriginalTable.this.getHeight());
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void paint(Graphics g) {
			g.translate(0, -paintingRow * getRowHeight());
			super.paint(g);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		protected boolean isEnabled(TreeNode node) {
			JOriginalTableNode nd = (JOriginalTableNode) node;
			return nd.listener.isCheckBoxEnabled(nd);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void checkBoxValueChanged(JTree tree, TreeNode node) {
			if (node != null) {
				JOriginalTableNode nd = (JOriginalTableNode) node;
				nd.listener.checkBoxValueChanged(nd);
			}
		}
	}

	//=====================================================================
	// TreeTableNode
	//=====================================================================

	public static class JOriginalTableNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = 1L;
		
		private Object key;
		private Object[] data;

		private JOriginalTableNode parentNode = null;
		private List<JOriginalTableNode> childNodes = null;

		// level of node
		private int level = -1;

		// aggregation level of node
		private int aggrigationLevel = -1;

		// index of node
		private int index = -1;

		// the selection state
		private boolean selected = false;

		// the listener for initialize the nodes
		private JOriginalTableNodeListener listener = null;

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public JOriginalTableNode(JOriginalTableNodeListener listener) {
			this(0, 0, 0, "root", null, listener);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private JOriginalTableNode(
			int						   level,
			int						   aggrigationLevel,
			int 					   index,
			Object					   key,
			Object[]				   data,
			JOriginalTableNodeListener listener
		) {
			super(key);
			this.level = level;
			this.aggrigationLevel = aggrigationLevel;
			this.index = index;
			this.key = key;
			this.data = cloneData(key, data);
			this.listener = listener;
			this.childNodes = new ArrayList<JOriginalTableNode>();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private static Object[] cloneData(Object key, Object[] data) {
			if (data != null) {
				Object[] v = new Object[data.length];
				System.arraycopy(data, 0, v, 0, data.length);
//              v[0] = key;
				return v;
			}
			return null;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public Object getKey() {
			return key;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public Object[] getData() {
			return data;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public int getLevel() {
			return level;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public int getAggrigationLevel() {
			return aggrigationLevel;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public int getIndex() {
			return index;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public boolean isSelected() {
			return selected;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void setSelected(boolean selected) {
			for (JOriginalTableNode node : childNodes) {
				node.setSelected(selected);
			}
			this.selected = selected;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public boolean hasChild() {
			return !childNodes.isEmpty();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public List<JOriginalTableNode> getChilds() {
			return childNodes;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public boolean isCheckable() {
			return listener.isCheckable(this);
		}
		
		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void init() {
			removeAllChildren();
			for (JOriginalTableNode node : childNodes) {
				node.init();
				add(node);
			}

			// now call the user back
			initData();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void clear() {
			for (JOriginalTableNode node : childNodes) {
				node.clear();
			}

			// this must be here!!
			removeAllChildren();
			childNodes.clear();
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public JOriginalTableNode addNode(Object[] data, int aggrigationLevel, int index) {

			Object key = listener.createKey(data, aggrigationLevel, level + 1);

			if (key != null) {
				JOriginalTableNode node = getNode(key);
				if (node != null) {
					return node.addNode(data, aggrigationLevel, index++);
				}
				return addNode(key, data, aggrigationLevel, index);
			}
			return null;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public JOriginalTableNode addNode(Object key, Object[] data, int aggrigationLevel, int index) {
			JOriginalTableNode node =
				new JOriginalTableNode(level + 1, aggrigationLevel, index, key, data, listener);
			node.parentNode = this;
			childNodes.add(node);
			return node;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public JOriginalTableNode getNode(Object key) {
			if (this.key.equals(key)) {
				return this;
			}

			for (JOriginalTableNode node : childNodes) {
				JOriginalTableNode n = node.getNode(key);
				if (n != null) {
					return n;
				}
			}
			return null;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private void select(JOriginalTableTree tree, Object data, int aggrigationLevel) {
			List<JOriginalTableNode> nodes = new ArrayList<JOriginalTableNode>();
			Object[] sels = (Object[]) data;
			for (int i = 0; i < sels.length; i++) {
				JOriginalTableNode node = select((Object[]) sels[i], aggrigationLevel);
				if (node != null) {
					nodes.add(node);
				}
			}
			TreePath[] paths = new TreePath[nodes.size()];
			for (int i = 0; i < nodes.size(); i++) {
				paths[i] = new TreePath(nodes.get(i).getPath());
			}
			tree.setSelectionPaths(paths);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		private JOriginalTableNode select(Object data, int aggrigationLevel) {
			for (JOriginalTableNode node : childNodes) {
				Object key = listener.createKey((Object[]) data, aggrigationLevel, node.level);
				if (node.key.equals(key)) {
					JOriginalTableNode nc = node.select(data, aggrigationLevel);
					return nc == null ? node : nc;
				}
			}
			return null;
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void expand(JTree tree, int level) {
			tree.expandPath(new TreePath(getPath()));
			for (JOriginalTableNode node : childNodes) {
				if (node.getLevel() < level) {
					tree.expandPath(new TreePath(node.getPath()));
					node.expand(tree, level);
				}
			}
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void collapse(JTree tree, int level) {
			for (JOriginalTableNode node : childNodes) {
				node.collapse(tree, level);
				if (node.getLevel() >= level) {
					tree.collapsePath(new TreePath(node.getPath()));
				}
			}
		}

		/**
		 * The Bubblesort
		 *
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void sort(Comparator<Object> comparator, int columnIndex, int sortDirection) {

			// first all children
			for (JOriginalTableNode node : childNodes) {
				node.sort(comparator, columnIndex, sortDirection);
			}

			// and now sorting itself
			JOriginalTableNode[] rows =
				childNodes.toArray(
				new JOriginalTableNode[childNodes.size()]
			);

			for (int i = rows.length; i > 1; i--) {
				for (int j = 1; j < i; j++) {
					JOriginalTableNode r1 = rows[j - 1];
					JOriginalTableNode r2 = rows[j];

					Object o1 = r1.data[columnIndex];
					Object o2 = r2.data[columnIndex];
					int co = comparator.compare(o1, o2);
					if (
						(sortDirection == SORTDIR_ASCENDING && co > 0)
						|| (sortDirection == SORTDIR_DESCENDING && co < 0)
					) {
						rows[j - 1] = r2;
						rows[j] = r1;
					}
				}
			}

			// finally order the child nodes again
			removeAllChildren();
			for (int i = 0; i < rows.length; i++) {
				add(rows[i]);
			}
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void getKeyPath(List<Object> keys) {
			if (parentNode != null) {
				parentNode.getKeyPath(keys);
			}
			keys.add(key);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void initData() {
			for (JOriginalTableNode node : childNodes) {
				node.initData();
			}
			listener.initData(this);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void search(Object condition, List<Object> results) {
			for (JOriginalTableNode node : childNodes) {
				node.search(condition, results);
			}
			listener.searchData(this, condition, results);
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void getData(Map<Object, Object[]> map) {
			map.put(key, data);
			for (JOriginalTableNode node : childNodes) {
				node.getData(map);
			}
		}

		/**
		 * @param
		 *
		 * @exception
		 *
		 * @return
		 *
		 * @see
		 */
		public void print(String prefix) {
			System.out.println(prefix + " " + key);
			for (JOriginalTableNode node : childNodes) {
				node.print(prefix + "---");
			}
		}
	}

	//=====================================================================
	// TreeTableNodeListener
	//
	// This is the callback interface. Provides for initialization of the
	// nodes. Through this interface the user can determine most properties of
	// the nodes, for example icons, colors, fonts, etc.
	//=====================================================================
	
	public static interface JOriginalTableNodeListener {
		public Object createKey(Object[] data, int aggrigationLevel, int level);
		public void initData(JOriginalTableNode node);
		public ImageIcon getIcon(JOriginalTableNode node);
		public void searchData(JOriginalTableNode node, Object condition, List<Object> results);
		public boolean isCheckBoxEnabled(JOriginalTableNode node);
		public boolean isCheckable(JOriginalTableNode node);
		public void checkBoxValueChanged(JOriginalTableNode node);
		public boolean isEditable(JOriginalTableNode node, int row, int column);
		public EditorComponent getEditor(JOriginalTableNode node, int row, int column);
		public EditorComponent getEditorInstance(EditorComponent editor);
		public Class<?> getEditorType(JOriginalTableNode node, int row, int column);
		public void editorValueChanged(JOriginalTableNode node, Object oldValue, Object newValue);
	}

	//=============================================================================
	// TreeTableNodeAdapter (the default implementation of TreeTableNodeListener)
	//=============================================================================

	public static class JOriginalTableNodeAdapter implements JOriginalTableNodeListener {

		public Object createKey(Object[] data, int aggrigationLevel, int level) {
			return null;
		}

		public void initData(JOriginalTableNode node) {
		}

		public ImageIcon getIcon(JOriginalTableNode node) {
			return null;
		}

		public void searchData(JOriginalTableNode node, Object condition, List<Object> results) {
		}

		public boolean isCheckBoxEnabled(JOriginalTableNode node) {
			return true;
		}

		public boolean isCheckable(JOriginalTableNode node) {
			return true;
		}
		
		public void checkBoxValueChanged(JOriginalTableNode node) {
		}

		public boolean isEditable(JOriginalTableNode node, int row, int column) {
			return false;
		}

		public EditorComponent getEditor(JOriginalTableNode node, int row, int column) {
			return null;
		}
		
		public EditorComponent getEditorInstance(EditorComponent editor) {
			try {
				return editor == null ? null : editor.getClass().newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public Class<?> getEditorType(JOriginalTableNode node, int row, int column) {
			return node.getData()[column] == null ? null : node.getData()[column].getClass();
		}

		public void editorValueChanged(JOriginalTableNode node, Object oldValue, Object newValue) {
		}
	}

	public static ImageIcon getImage(String name) {
		URL url = JOriginalTable.class.getResource(IMGDIR + name);
		if (url != null) {
			return new ImageIcon(url);
		}
		return null;
	}

	//############################################################################
	//#
	//# Supporting for attributive Table
	//#
	//############################################################################

	public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
		if (attributes == null) {
			return super.getCellRect(row, column, includeSpacing);
		}

		Rectangle sRect = super.getCellRect(row, column, includeSpacing);
		if ((row < 0) || (column < 0) || (getRowCount() <= row) || (getColumnCount() <= column)) {
			return sRect;
		}

		if (!attributes.isVisible(row, column)) {
			int temp_row = row;
			int temp_column = column;
			row += attributes.getSpan(temp_row, temp_column)[0];
			column += attributes.getSpan(temp_row, temp_column)[1];
		}
		int[] n = attributes.getSpan(row, column);

		int index = 0;
		int columnMargin = getColumnModel().getColumnMargin();
		int aCellHeight = rowHeight + rowMargin;

		Rectangle bound = new Rectangle();
		bound.y = row * aCellHeight;
		bound.height = n[0] * aCellHeight;

		Enumeration<TableColumn> enumeration = getColumnModel().getColumns();
		while (enumeration.hasMoreElements()) {
			TableColumn aColumn = enumeration.nextElement();
			bound.width = aColumn.getWidth() + columnMargin;
			if (index == column) {
				break;
			}
			bound.x += bound.width;
			index++;
		}
		for (int i = 0; i < n[1] - 1; i++) {
			TableColumn aColumn = enumeration.nextElement();
			bound.width += aColumn.getWidth() + columnMargin;
		}

		if (!includeSpacing) {
			Dimension spacing = getIntercellSpacing();
			bound.setBounds(
				bound.x + spacing.width / 2,
				bound.y + spacing.height / 2,
				bound.width - spacing.width,
				bound.height - spacing.height
			);
		}
		return bound;
	}

	public int rowAtPoint(Point point) {
		if (attributes == null) {
			return super.rowAtPoint(point);
		}
		return rowColumnAtPoint(point)[0];
	}

	public int columnAtPoint(Point point) {
		if (attributes == null) {
			return super.columnAtPoint(point);
		}
		return rowColumnAtPoint(point)[1];
	}

	public void valueChangedXX(ListSelectionEvent e) {
		if (attributes == null) {
			super.valueChanged(e);
			return;
		}
		int firstIndex = e.getFirstIndex();
		int lastIndex = e.getLastIndex();
		if (firstIndex == -1 && lastIndex == -1) { // Selection cleared.
			repaint();
		}
		Rectangle dirtyRegion = getCellRect(firstIndex, 0, false);
		int numCoumns = getColumnCount();
		int index = firstIndex;
		for (int i = 0; i < numCoumns; i++) {
			dirtyRegion.add(getCellRect(index, i, false));
		}
		index = lastIndex;
		for (int i = 0; i < numCoumns; i++) {
			dirtyRegion.add(getCellRect(index, i, false));
		}
		repaint(dirtyRegion.x, dirtyRegion.y, dirtyRegion.width, dirtyRegion.height);
	}

	private int[] rowColumnAtPoint(Point point) {
		int[] retValue = { -1, -1 };
		int row = point.y / (rowHeight + rowMargin);
		if ((row < 0) || (getRowCount() <= row)) {
			return retValue;
		}
		int column = getColumnModel().getColumnIndexAtX(point.x);

		if (attributes.isVisible(row, column)) {
			retValue[1] = column;
			retValue[0] = row;
			return retValue;
		}
		retValue[0] = row + attributes.getSpan(row, column)[0];
		retValue[1] = column + attributes.getSpan(row, column)[1];
		return retValue;
	}

	private static class JOriginalCellAttribute {

		private int rowSize;
		private int columnSize;
		private int[][][] span;
		private List<int[]> combined = new ArrayList<int[]>();

		public JOriginalCellAttribute() {
			this(0, 0);
		}

		public JOriginalCellAttribute(int numRows, int numColumns) {
			setSize(new Dimension(numColumns, numRows));
		}

		@SuppressWarnings("unused")
		public Dimension getSize() {
			return new Dimension(rowSize, columnSize);
		}

		public void setSize(Dimension size) {
			rowSize = size.width;
			columnSize = size.height;
			span = new int[rowSize][columnSize][2];

			for (int i = 0; i < span.length; i++) {
				for (int j = 0; j < span[i].length; j++) {
					span[i][j][0] = 1;
					span[i][j][1] = 1;
				}
			}

			if (!combined.isEmpty()) {
				for (int i = 0; i < combined.size(); i++) {
					combine(combined.get(i), combined.get(++i));
				}
			}
		}

		public int[] getSpan(int row, int column) {
			if (isOutOfBounds(row, column)) {
				return new int[] { 1, 1 };
			}
			return span[row][column];
		}

		@SuppressWarnings("unused")
		public void setSpan(int[] span, int row, int column) {
			if (isOutOfBounds(row, column)) {
				return;
			}
			this.span[row][column] = span;
		}

		public boolean isVisible(int row, int column) {
			if (isOutOfBounds(row, column)) {
				return false;
			}
			if (span[row][column][0] < 1 || span[row][column][1] < 1) {
				return false;
			}
			return true;
		}

		public void combine(int[] rows, int[] columns) {
			if (isOutOfBounds(rows, columns)) {
				return;
			}
			int rowSpan = rows.length;
			int columnSpan = columns.length;
			int startRow = rows[0];
			int startColumn = columns[0];
			for (int i = 0; i < rowSpan; i++) {
				for (int j = 0; j < columnSpan; j++) {
					if (
						span[startRow + i][startColumn + j][0] != 1
						|| span[startRow + i][startColumn + j][1] != 1
					) {
						return;
					}
				}
			}
			for (int i = 0, ii = 0; i < rowSpan; i++, ii--) {
				for (int j = 0, jj = 0; j < columnSpan; j++, jj--) {
					span[startRow + i][startColumn + j][0] = ii;
					span[startRow + i][startColumn + j][1] = jj;
				}
			}
			span[startRow][startColumn][0] = rowSpan;
			span[startRow][startColumn][1] = columnSpan;

			combined.add(rows);
			combined.add(columns);
		}

		public void split(int row, int column) {
			if (isOutOfBounds(row, column)) {
				return;
			}
			int rowSpan = span[row][column][0];
			int columnSpan = span[row][column][1];
			for (int i = 0; i < rowSpan; i++) {
				for (int j = 0; j < columnSpan; j++) {
					span[row + i][column + j][0] = 1;
					span[row + i][column + j][1] = 1;
				}
			}

			for (int i = 0; i < combined.size(); i++) {
				int[] rows = combined.get(i);
				boolean breaked = false;
				for (int r : rows) {
					if (r == row) {
						combined.remove(i);
						combined.remove(i + 1);
						breaked = true;
						break;
					}
				}
				if (breaked) {
					break;
				}
			}
		}

		private boolean isOutOfBounds(int row, int column) {
			if (row < 0 || rowSize <= row || column < 0 || columnSize <= column) {
				return true;
			}
			return false;
		}

		private boolean isOutOfBounds(int[] rows, int[] columns) {
			for (int i = 0; i < rows.length; i++) {
				if (rows[i] < 0 || rowSize <= rows[i]) {
					return true;
				}
			}
			for (int i = 0; i < columns.length; i++) {
				if (columns[i] < 0 || columnSize <= columns[i]) {
					return true;
				}
			}
			return false;
		}
	}

	private class JOriginalAttributiveTableUI extends BasicTableUI {

		public void paint(Graphics g, JComponent c) {
			Rectangle oldClipBounds = g.getClipBounds();
			Rectangle clipBounds = new Rectangle(oldClipBounds);
			int tableWidth = table.getColumnModel().getTotalColumnWidth();
			clipBounds.width = Math.min(clipBounds.width, tableWidth);
			g.setClip(clipBounds);

			int firstIndex = table.rowAtPoint(new Point(0, clipBounds.y));
			int lastIndex = table.getRowCount() - 1;

			Rectangle rowRect =
				new Rectangle(0, 0, tableWidth, table.getRowHeight() + table.getRowMargin());
			rowRect.y = firstIndex * rowRect.height;

			for (int index = firstIndex; index <= lastIndex; index++) {
				if (rowRect.intersects(clipBounds)) {
					paintRow(g, index);
				}
				rowRect.y += rowRect.height;
			}
			g.setClip(oldClipBounds);
		}

		private void paintRow(Graphics g, int row) {
			Rectangle rect = g.getClipBounds();
			boolean drawn = false;

			int numColumns = table.getColumnCount();

			for (int column = 0; column < numColumns; column++) {
				Rectangle cellRect = table.getCellRect(row, column, true);
				int cellRow, cellColumn;
				if (attributes.isVisible(row, column)) {
					cellRow = row;
					cellColumn = column;
				} else {
					cellRow = row + attributes.getSpan(row, column)[0];
					cellColumn = column + attributes.getSpan(row, column)[1];
				}
				if (cellRect.intersects(rect)) {
					drawn = true;
					paintCell(g, cellRect, cellRow, cellColumn);
				} else {
					if (drawn) {
						break;
					}
				}
			}
		}

		private void paintCell(Graphics g, Rectangle cellRect, int row, int column) {
			int spacingHeight = table.getRowMargin();
			int spacingWidth = table.getColumnModel().getColumnMargin();

			Color c = g.getColor();
			g.setColor(table.getGridColor());
			g.drawRect(cellRect.x, cellRect.y, cellRect.width - 1, cellRect.height - 1);
			g.setColor(c);

			cellRect.setBounds(
				cellRect.x + spacingWidth / 2,
				cellRect.y + spacingHeight / 2,
				cellRect.width - spacingWidth,
				cellRect.height - spacingHeight
			);

			if (
				table.isEditing()
				&& table.getEditingRow() == row
				&& table.getEditingColumn() == column
			) {
				Component component = table.getEditorComponent();
				component.setBounds(cellRect);
				component.validate();
			} else {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component component = table.prepareRenderer(renderer, row, column);

				if (component.getParent() == null) {
					rendererPane.add(component);
				}
				rendererPane.paintComponent(
					g,
					component,
					table,
					cellRect.x,
					cellRect.y,
					cellRect.width,
					cellRect.height,
					true
				);
			}
		}
	}

	//
	// support for Cell Editing
	//

	//###############################################################################
	// EditorComponent
	//###############################################################################

	public interface EditorComponent {

		public Component getEditorComponent();

		public Object getEditorValue();

		public void setEditorValue(Object value);

		public String getValidState(Object value);

		public Component getRendererComponent(
			Component parent,
			Object    value,
			boolean   isEditable,
			Color	  fc,
			Color	  bc
		);

		public void setEditorListener(JOriginalTableCellEditor listener);

		public void fireEditingStopped(EditorComponent editor);

		public void fireEditingCanceled(EditorComponent editor);
	}

	//###############################################################################
	// DefaultEditorComponent
	//###############################################################################

	public abstract static class DefaultEditorComponent extends JPanel implements EditorComponent,
		ActionListener
	{

		private static final long serialVersionUID = 1L;

		private static final Icon icon = getImage("defeditor.gif");

		private Object value;

		private JLabel label;
		private JButton button;
		private Component parent;

		private JOriginalTableCellEditor listener;

		public DefaultEditorComponent() {
			this(icon);
		}

		public DefaultEditorComponent(Icon icon) {
			setLayout(new BorderLayout(0, 0));
			setOpaque(true);

			label = new JLabel();
			label.setOpaque(true);

			add(label, BorderLayout.CENTER);

			if (icon != null) {

				Dimension d = new Dimension(16, 16);

				button = new JButton();
				button.setPreferredSize(d);
				button.setOpaque(true);
				button.setBorder(null);
				button.setBorderPainted(false);
				button.setIcon(icon);
				button.setFocusable(false);
				button.addActionListener(this);

				add(button, BorderLayout.EAST);
			}
		}

		public void setForeground(Color fg) {
			super.setForeground(fg);
			if (label != null) {
				label.setForeground(fg);
			}
		}

		public void setBackground(Color bg) {
			super.setBackground(bg);
			if (label != null) {
				label.setBackground(bg);
			}
		}

		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			if (label != null) {
				label.setEnabled(enabled);
			}
		}

		public Object getEditorValue() {
			return value;
		}

		public void setEditorValue(Object value) {
			this.value = value;
		}

		public Component getEditorComponent() {
			return this;
		}

		public String getValidState(Object value) {
			return null;
		}

		public Component getRendererComponent(
			Component parent,
			Object    value,
			boolean   isEditable,
			Color	  fc,
			Color	  bc
		) {
			this.parent = parent;

			String va = value == null ? null : convertToString(value);
			String sp = getValidState(value);
			Icon ic = convertToIcon(value, isEditable);

			bc = sp == null ? bc : va == null ? MANDATORY_COLOR : ERROR_COLOR;

			label.setText(va);
			label.setIcon(ic);
			label.setFont(EDITOR_FONT);
			label.setForeground(fc);
			label.setBackground(bc);
			//label.setEnabled(isEditable);

			setForeground(fc);
			setBackground(bc);
			setToolTipText(sp == null ? va : sp);

			if (button != null) {
				button.setBackground(bc);
				button.setVisible(isEditable);
			}

			return this;
		}

		public void setEditorListener(JOriginalTableCellEditor listener) {
			this.listener = listener;
		}

		public void fireEditingStopped(EditorComponent editor) {
			if (listener != null) {
				listener.stopCellEditing();
			}
		}

		public void fireEditingCanceled(EditorComponent editor) {
			if (listener != null) {
				listener.cancelCellEditing();
			}
		}

		protected String convertToString(Object value) {
			return value.toString();
		}

		protected Icon convertToIcon(Object value, boolean isEditable) {
			return null;
		}

		protected Object startCustomEditor(Component parent, Object value) {
			return null;
		}

		public void actionPerformed(ActionEvent e) {
			Object value = startCustomEditor(parent, getEditorValue());
			if (value != getEditorValue()) {
				setEditorValue(value);
				fireEditingStopped(this);
			}
		}
	}

	//###############################################################################
	// EmptyEditorComponent
	//###############################################################################
	
	public static class EmptyEditorComponent extends DefaultEditorComponent {

		private static final long serialVersionUID = 1L;
		private JLabel lb;

		public EmptyEditorComponent() {
			super(null);

			lb = new JLabel();
			lb.setText("xxx");
		}

		public Component getEditorComponent() {
			return lb;
		}

		public Object getEditorValue() {
			return "";
		}

		public void setEditorValue(Object value) {
		}
		
		public Component getRendererComponent(
				Component parent,
				Object    value,
				boolean   isEditable,
				Color	  fc,
				Color	  bc
			) {	
			Component co = super.getRendererComponent(parent, value, isEditable, fc, bc);
			((JPanel)co).removeAll();
			return co;
		}
	}
	
	//###############################################################################
	// TextEditorComponent
	//###############################################################################

	public static class TextEditorComponent extends DefaultEditorComponent {

		private static final long serialVersionUID = 1L;

		private static final ImageIcon icon = getImage("texteditor.gif");

		private JTextField tx;

		public TextEditorComponent() {
			super(icon);

			tx = new JTextField();
			tx.addFocusListener(
				new FocusListener() {
					public void focusGained(final FocusEvent e) {
						tx.selectAll();
					}

					public void focusLost(final FocusEvent e) {
						tx.select(0, 0);
						fireEditingStopped(TextEditorComponent.this);
					}
				}
			);

			tx.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fireEditingStopped(TextEditorComponent.this);
					}
				}
			);

			tx.registerKeyboardAction(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fireEditingStopped(TextEditorComponent.this);
					}
				},
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_FOCUSED
			);

			tx.registerKeyboardAction(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fireEditingCanceled(TextEditorComponent.this);
					}
				},
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_FOCUSED
			);
		}

		public Component getEditorComponent() {
			return tx;
		}

		public Object getEditorValue() {
			return tx.getText();
		}

		public void setEditorValue(Object value) {
			tx.setText(value == null ? "" : value.toString());
		}
	}

	//###############################################################################
	// BooleanEditorComponent
	//###############################################################################

	public static class BooleanEditorComponent extends DefaultEditorComponent {

		private static final long serialVersionUID = 1L;
		private static final Icon ic1 = getImage("checkboxOff.gif");
		private static final Icon ic2 = getImage("checkboxOn.gif");
		private static final Icon ic3 = getImage("check.png");

		private JCheckBox cb;

		public BooleanEditorComponent() {
			super(null);
			cb = new JCheckBox();
			cb.setBorder(null);
			cb.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fireEditingStopped(BooleanEditorComponent.this);
					}
				}
			);
		}

		public Component getEditorComponent() {
			return cb;
		}

		public Object getEditorValue() {
			return Boolean.valueOf(cb.isSelected());
		}

		public void setEditorValue(Object value) {
			cb.setSelected(value == null ? false : ((Boolean) value).booleanValue());
		}

		protected String convertToString(Object value) {
			return ""; //value.equals(Boolean.TRUE) ? "True" : "False";
		}

		protected Icon convertToIcon(Object value, boolean isEditable) {
			boolean b = value != null && value.equals(Boolean.TRUE);
			if (isEditable) return  b ? ic2 : ic1;
			return  b ? ic3 : null;
		}
	}

	//###############################################################################
	// NumberEditorComponent
	//###############################################################################

	public static class NumberEditorComponent extends DefaultEditorComponent {

		private static final long serialVersionUID = 1L;

		private static final ImageIcon icon = getImage("numeditor.gif");

		private boolean adjusting = false;
		private JSpinner sp;

		public NumberEditorComponent() {
			this(
				new SpinnerNumberModel(
					new Integer(0),
					new Integer(Integer.MIN_VALUE),
					new Integer(Integer.MAX_VALUE),
					new Integer(1)
				)
			);
		}

		public NumberEditorComponent(SpinnerNumberModel md) {
			super(icon);

			sp = new JSpinner(md);
			sp.setFocusable(false);

			JSpinner.DefaultEditor ed = (JSpinner.DefaultEditor) sp.getEditor();
			ed.getTextField().setHorizontalAlignment(JLabel.LEFT);

			sp.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						if (!adjusting) {
							fireEditingStopped(NumberEditorComponent.this);
						}
					}
				}
			);
		}

		public Component getEditorComponent() {
			return sp;
		}

		public Object getEditorValue() {
			return sp.getValue();
		}

		public void setEditorValue(Object value) {
			adjusting = true;
			sp.setValue(value == null ? 0 : value);
			adjusting = false;
		}

		public static class ShortEditorComponent extends NumberEditorComponent {

			private static final long serialVersionUID = 1L;

			public ShortEditorComponent() {
				super(new SpinnerNumberModel(0, Short.MIN_VALUE, Short.MAX_VALUE, 1));
			}

			public ShortEditorComponent(short value, short min, short max, short step) {
				super(new SpinnerNumberModel(value, min, max, step));
			}
		}

		public static class IntegerEditorComponent extends NumberEditorComponent {

			private static final long serialVersionUID = 1L;

			public IntegerEditorComponent() {
				super(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
			}

			public IntegerEditorComponent(int value, int min, int max, int step) {
				super(new SpinnerNumberModel(value, min, max, step));
			}
		}

		public static class LongEditorComponent extends NumberEditorComponent {

			private static final long serialVersionUID = 1L;

			public LongEditorComponent() {
				super(new SpinnerNumberModel(0, Long.MIN_VALUE, Long.MAX_VALUE, 1));
			}

			public LongEditorComponent(long value, long min, long max, long step) {
				super(new SpinnerNumberModel(value, min, max, step));
			}
		}

		public static class DoubleEditorComponent extends NumberEditorComponent {

			private static final long serialVersionUID = 1L;

			public DoubleEditorComponent() {
				super(new SpinnerNumberModel(0, -Double.MIN_VALUE, Double.MAX_VALUE, 0.5f));
			}

			public DoubleEditorComponent(double value, double min, double max, double step) {
				super(new SpinnerNumberModel(value, min, max, step));
			}
		}

		public static class FloatEditorComponent extends NumberEditorComponent {

			private static final long serialVersionUID = 1L;

			public FloatEditorComponent() {
				super(new SpinnerNumberModel(0, -Float.MIN_VALUE, Float.MIN_VALUE, 0.5f));
			}

			public FloatEditorComponent(float value, float min, float max, float step) {
				super(new SpinnerNumberModel(value, min, max, step));
			}
		}
	}

	//###############################################################################
	// ListEditorComponent
	//###############################################################################

	public static class ListEditorComponent extends DefaultEditorComponent {

		private static final long serialVersionUID = 1L;

		private static final ImageIcon icon = getImage("listeditor.gif");

		private Object[] items;
		private Object[] values;
		private Icon[] icons;
		
		private JComboBox<?> co;

		public ListEditorComponent() {
			this(new String[0], new String[0], null);
		}
		
		public ListEditorComponent(Object[] items, Object[] values, Icon[] icons) {
			super(icon);
			setData(items, values, icons);
		}
			
		public void setData(Object[] items, Object[] values, Icon[] icons) {

			this.items = items;
			this.values = values;
			this.icons = icons;

			co = new JComboBox<Object>(items);
			co.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fireEditingStopped(ListEditorComponent.this);
					}
				}
			);
			co.addKeyListener(
				new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							fireEditingStopped(ListEditorComponent.this);
						}
					}
				}
			);
		}

		public Component getEditorComponent() {
            return co;
		}

		public Object getEditorValue() {
			return translate(co.getSelectedItem());
		}

		public void setEditorValue(Object value) {
			co.setSelectedItem(convertToString(value));
		}

		protected String convertToString(Object value) {
			if (value == null) {
				return null;
			}
			int n = 0;
			for (Object v : values) {
				if (v.equals(value)) {
					return items[n].toString();
				}
				n++;
			}
			return "";
		}

		protected Icon convertToIcon(Object value, boolean isEditable) {
			if (value == null || icons == null) {
				return null;
			}
			int n = 0;
			for (Object v : values) {
				if (v.equals(value)) {
					return icons[n];
				}
				n++;
			}
			return null;
		}

		public Object translate(Object value) {
			int v = 0;
			for (Object o : items) {
				if (o.equals(value)) {
					return values[v];
				}
				v++;
			}
			return null;
		}
	}

	//###############################################################################
	// DateEditorComponent
	//###############################################################################

	public static class DateEditorComponent extends DefaultEditorComponent {

		private static final long serialVersionUID = 1L;

		private static final Icon icon = getImage("dateeditor.gif");

		private JCalendar ca;

		public DateEditorComponent() {
			super(icon);

			ca = new JCalendar();
			ca.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						fireEditingStopped(DateEditorComponent.this);
					}
				}
			);
		}

		public Component getEditorComponent() {
			return ca;
		}

		public Object getEditorValue() {
			return ca.getDateString();
		}

		public void setEditorValue(Object value) {
			ca.setDate((String) value);
		}
	}
}
