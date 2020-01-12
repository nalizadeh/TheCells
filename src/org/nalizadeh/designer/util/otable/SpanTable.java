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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class SpanTable extends JTable {

	private SpanTableModel model;

	public SpanTable(String[] columns, Object[][] data) {

		model = new SpanTableModel(columns, data);
		setSpanModel(model);

		setUI(new SpanTableUI());
		getTableHeader().setReorderingAllowed(false);
		setCellSelectionEnabled(true);
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setDefaultRenderer(Object.class, new SpanTableCellRenderer());
	}

	public void setSpanModel(TableModel model) {
		setModel(model);
	}

	public SpanTableModel getSpanModel() {
		return model;
	}

	public Rectangle getCellRect(int row, int column, boolean includeSpacing) {
		Rectangle sRect = super.getCellRect(row, column, includeSpacing);
		if ((row < 0) || (column < 0) || (getRowCount() <= row) || (getColumnCount() <= column)) {
			return sRect;
		}

		if (!getSpanModel().isVisible(row, column)) {
			int temp_row = row;
			int temp_column = column;
			row += getSpanModel().getSpan(temp_row, temp_column)[0];
			column += getSpanModel().getSpan(temp_row, temp_column)[1];
		}
		int[] n = getSpanModel().getSpan(row, column);

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
			TableColumn aColumn = (TableColumn) enumeration.nextElement();
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

	private int[] rowColumnAtPoint(Point point) {
		int[] retValue = { -1, -1 };
		int row = point.y / (rowHeight + rowMargin);
		if ((row < 0) || (getRowCount() <= row)) {
			return retValue;
		}
		int column = getColumnModel().getColumnIndexAtX(point.x);

		if (getSpanModel().isVisible(row, column)) {
			retValue[1] = column;
			retValue[0] = row;
			return retValue;
		}
		retValue[0] = row + getSpanModel().getSpan(row, column)[0];
		retValue[1] = column + getSpanModel().getSpan(row, column)[1];
		return retValue;
	}

	public int rowAtPoint(Point point) {
		return rowColumnAtPoint(point)[0];
	}

	public int columnAtPoint(Point point) {
		return rowColumnAtPoint(point)[1];
	}

	public void columnSelectionChanged(ListSelectionEvent e) {
		repaint();
	}

	public void valueChanged(ListSelectionEvent e) {
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

	//===============================================================================

	private class SpanTableModel extends AbstractTableModel {

		private Object[] columns;
		private Object[][] data;

		private int rowSize;
		private int columnSize;
		private int[][][] span;

		public SpanTableModel(String[] columns, Object[][] data) {

			this.columns = columns;
			this.data = data;

			initSize(data.length, columns.length);
		}

		public void initSize(int rows, int cols) {
			rowSize = rows;
			columnSize = cols;
			span = new int[rowSize][columnSize][2];

			for (int i = 0; i < span.length; i++) {
				for (int j = 0; j < span[i].length; j++) {
					span[i][j][0] = 1;
					span[i][j][1] = 1;
				}
			}
		}

		public int[] getSpan(int row, int column) {
			if (isOutOfBounds(row, column)) {
				return new int[] { 1, 1 };
			}
			return span[row][column];
		}

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

						//System.out.println("can't combine");
						return;
					}
				}
			}
			for (int i = 0, ii = 0; i < rowSpan; i++, ii--) {
				for (int j = 0, jj = 0; j < columnSpan; j++, jj--) {
					span[startRow + i][startColumn + j][0] = ii;
					span[startRow + i][startColumn + j][1] = jj;
					//System.out.println("r " +ii +"  c " +jj);
				}
			}
			span[startRow][startColumn][0] = rowSpan;
			span[startRow][startColumn][1] = columnSpan;
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

		public String getColumnName(int column) {
			return columns[column].toString();
		}

		public int getRowCount() {
			return data == null ? 0 : data.length;
		}

		public int getColumnCount() {
			return columns == null ? 0 : columns.length;
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public boolean isCellEditable(int row, int column) {
			return true;
		}

		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}

	private class SpanTableUI extends BasicTableUI {

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

			int numRows = table.getRowCount();
			int numColumns = table.getColumnCount();

			for (int column = 0; column < numColumns; column++) {
				Rectangle cellRect = table.getCellRect(row, column, true);
				int cellRow, cellColumn;
				if (getSpanModel().isVisible(row, column)) {
					cellRow = row;
					cellColumn = column;
				} else {
					cellRow = row + getSpanModel().getSpan(row, column)[0];
					cellColumn = column + getSpanModel().getSpan(row, column)[1];
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

	private class SpanTableCellRenderer extends JLabel implements TableCellRenderer {

		private Border noFocusBorder;

		public SpanTableCellRenderer() {
			noFocusBorder = new EmptyBorder(1, 2, 1, 2);
			setOpaque(true);
			setBorder(noFocusBorder);
		}

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {
			Color foreground = null;
			Color background = null;
			Font font = null;

			if (isSelected) {
				setForeground((foreground != null) ? foreground : table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground((foreground != null) ? foreground : table.getForeground());
				setBackground((background != null) ? background : table.getBackground());
			}
			setFont((font != null) ? font : table.getFont());

			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					setForeground(
						(foreground != null) ? foreground
						: UIManager.getColor("Table.focusCellForeground")
					);
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(noFocusBorder);
			}
			setValue(value);
			return this;
		}

		protected void setValue(Object value) {
			setText((value == null) ? "" : value.toString());
		}
	}

	//=========================================================================================

	public static JPanel asPanel() {

		String[] cols = { "Attribute", "Value", "Time" };
		Object[][] rows =
			{
				{ "Cell", "", "" },
				{ "Cell", "A", "cell" },
				{ "Cell", "B", "rowOverlapping" },
				{ "Cell", "C", "colOverlapping" },
				{ "Swing", "", "" },
				{ "Swing", "1", "background" },
				{ "Swing", "2", "background" },
				{ "Swing", "3", "background" },
				{ "Swing", "4", "background" },
				{ "Swing", "5", "background" },
				{ "Swing", "6", "border" },
			};

		SpanTable table = new SpanTable(cols, rows);
		table.getSpanModel().combine(new int[] { 3, 4, 5 }, new int[] { 1 });

		JScrollPane sp = new JScrollPane(table);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(sp, BorderLayout.CENTER);
		return panel;
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("TreeTable");
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(asPanel(), BorderLayout.CENTER);

		frame.setSize(400, 300);
		frame.pack();
		frame.setVisible(true);
	}

}
