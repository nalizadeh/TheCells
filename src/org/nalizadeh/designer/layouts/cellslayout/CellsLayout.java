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

package org.nalizadeh.designer.layouts.cellslayout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JLabel;

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
public class CellsLayout implements LayoutManager2 {

	public static final double PREFERRED = -1.0;
	public static final double FILL = -2.0;

	public static final int LEFT = JLabel.LEFT;
	public static final int CENTER = JLabel.CENTER;
	public static final int RIGHT = JLabel.RIGHT;
	public static final int TOP = JLabel.TOP;
	public static final int BOTTOM = JLabel.BOTTOM;

	/**
	 * horizontaler Abstand zwischen Komponenten
	 */
	private int hgap = 0;

	/**
	 * vertikaler Abstand zwischen Komponenten
	 */
	private int vgap = 0;

	/**
	 * Container, welchen wir layouten
	 */
	private Container container = null;

	/**
	 * Zailen
	 */
	private List<CellRow> rows = null;

	/**
	 * Spalten
	 */
	private List<CellColumn> cols = null;

	/**
	 * Zellen
	 */
	private List<Cell> cells = null;

	/**
	 * Default-Konstruktor
	 *
	 * @exception
	 *
	 * @see
	 */
	public CellsLayout() {
		this(0, 0, 0, 0);
	}

	/**
	 * Konstruktor
	 *
	 * @param      -  hgap ist der horizontale Abstand zwischen Komponenten in Pixel.
	 * @param      -  vgap ist der vertikale Abstand zwischen Komponenten in Pixel.
	 *
	 * @exception
	 *
	 * @see
	 */
	public CellsLayout(int hgap, int vgap) {
		this(0, 0, hgap, vgap);
	}

	/**
	 * Konstruktor
	 *
	 * @param      -  hgap ist der horizontale Abstand zwischen Komponenten in Pixel.
	 * @param      -  vgap ist der vertikale Abstand zwischen Komponenten in Pixel.
	 *
	 * @exception
	 *
	 * @see
	 */
	public CellsLayout(int rows, int cols, int hgap, int vgap) {

		this.rows = new ArrayList<CellRow>();
		this.cols = new ArrayList<CellColumn>();
		this.cells = new ArrayList<Cell>();
		this.hgap = hgap;
		this.vgap = vgap;

		for (int i = 0; i < rows; i++) {
			addRow(20);
		}
		for (int i = 0; i < cols; i++) {
			addColumn(60);
		}
		createCells();
	}

	/**
	 * Default-Konstruktor
	 *
	 * @exception
	 *
	 * @see
	 */
	public CellsLayout(double[][] data) {
		this(data, 0, 0);
	}

	/**
	 * Default-Konstruktor
	 *
	 * @exception
	 *
	 * @see
	 */
	public CellsLayout(double[][] data, int hgap, int vgap) {

		this(0, 0, hgap, vgap);

		if (data.length == 2) {
			for (double d : data[0]) {
				addRow(d);
			}
			for (double d : data[1]) {
				addColumn(d);
			}

			createCells();
		}
	}

	/**
	 * Liefert den horizontalen Abstand zwischen den Komponenten zurück.
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see
	 */
	public int getHgap() {
		return hgap;
	}

	/**
	 * Setzt den horizontalen Abstand zwischen den Komponenten.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see
	 */
	public void setHgap(int hgap) {
		this.hgap = hgap;
	}

	/**
	 * Liefert den vertikalen Abstand zwischen den Komponenten zurück.
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see
	 */
	public int getVgap() {
		return vgap;
	}

	/**
	 * Setzt den vertikalen Abstand zwischen den Komponenten.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see
	 */
	public void setVgap(int vgap) {
		this.vgap = vgap;
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
	public List<CellRow> getRows() {
		return rows;
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
	public List<CellColumn> getColumns() {
		return cols;
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
	public List<Cell> getCells() {
		return cells;
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
	public List<Cell> getCellsInRow(int row) {
		List<Cell> cls = new ArrayList<Cell>();
		for (Cell cell : cells) {
			if (cell.row == row) {
				cls.add(cell);
			}
		}
		return cls;
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
	public List<Cell> getCellsInColumn(int col) {
		List<Cell> cls = new ArrayList<Cell>();
		for (Cell cell : cells) {
			if (cell.col == col) {
				cls.add(cell);
			}
		}
		return cls;
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
	public Cell getCell(int row, int col) {
		for (Cell c : cells) {
			if (c.row == row && c.col == col) {
				return c;
			}
		}
		return null;
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
	public Cell getCellAt(int x, int y) {
		for (Cell c : cells) {
			if (c.bounds.contains(x, y)) {
				return c;
			}
		}
		return null;
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
	public Cell getCell(Component comp) {
		for (Cell c : cells) {
			if (c.comp != null && findCell(c.comp, comp)) {
				return c;
			}
		}
		return null;
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
	private boolean findCell(Component cellComp, Component comp) {
		if (cellComp instanceof Container) {
			Container cont = (Container) cellComp;
			for (Component co : cont.getComponents()) {
				if (findCell(co, comp)) {
					return true;
				}
			}
		}
		return cellComp == comp;
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
	public Component getLayoutComponent(Object constraints) {
		if (constraints != null) {
			Cell c1 = fromString(constraints.toString());
			Cell c2 = getCell(c1.row, c1.col);
			if (c2 != null) {
				return c2.comp;
			}
		}
		return null;

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
	public Object getConstraints(Component comp) {
		return getCell(comp);
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
	private Cell fromString(String constraints) {

		StringTokenizer st = new StringTokenizer((String) constraints, ",");

		int row = -1;
		int col = -1;
		int usePref = 0;
		int rowOver = 0;
		int colOver = 0;
		int hAlign = LEFT;
		int vAlign = TOP;

		int state = 0;
		boolean end = false;

		while (!end) {
			switch (state) {

				case 0 :
					if (st.hasMoreElements()) {
						row = Integer.parseInt(st.nextToken().trim());
						state++;
					} else {
						state = -1;
					}
					break;

				case 1 :
					if (st.hasMoreElements()) {
						col = Integer.parseInt(st.nextToken().trim());
						state++;
					} else {
						state = -1;
					}
					break;

				case 2 :
					if (st.hasMoreElements()) {
						usePref = Integer.parseInt(st.nextToken().trim());
						state++;
					} else {
						state = 10;
					}
					break;

				case 3 :
					if (st.hasMoreElements()) {
						rowOver = Integer.parseInt(st.nextToken().trim());
						state++;
					} else {
						state = 10;
					}
					break;

				case 4 :
					if (st.hasMoreElements()) {
						colOver = Integer.parseInt(st.nextToken().trim());
						state++;
					} else {
						state = 10;
					}
					break;

				case 5 :
					if (st.hasMoreElements()) {
						hAlign = Integer.parseInt(st.nextToken().trim());
						state++;
					} else {
						state = 10;
					}
					break;

				case 6 :
					if (st.hasMoreElements()) {
						vAlign = Integer.parseInt(st.nextToken().trim());
						state = 10;
					} else {
						state = 10;
					}
					break;

				case 10 :
					end = true;
					break;

				case -1 :
					throw new IllegalArgumentException();
			}
		}

		return new Cell(row, col, usePref == 1, rowOver, colOver, hAlign, vAlign, null);
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
	public void addRow(double h) {
		rows.add(new CellRow(0, h, 0));
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
	public void addColumn(double w) {
		cols.add(new CellColumn(0, w, 0));
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
	public void insertRow(CellRow row) {
		rows.add(row.num, row);

		for (int r = rows.size() - 1; r >= row.num; r--) {
			if (r > row.num) {
				rows.get(r).num++;
			}
			for (int c = 0; c < cols.size(); c++) {
				Cell cell = getCell(r, c);
				if (cell != null) {
					cell.overlapped = null;
					cell.row++;
				}
				if (r == row.num) {
					cells.add(new Cell(r, c, null));
				}
			}
		}
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
	public void insertColumn(CellColumn col) {
		cols.add(col.num, col);
		for (int c = cols.size() - 1; c >= col.num; c--) {
			if (c > col.num) {
				cols.get(c).num++;
			}
			for (int r = 0; r < rows.size(); r++) {
				Cell cell = getCell(r, c);
				if (cell != null) {
					cell.overlapped = null;
					cell.col++;
				}
				if (c == col.num) {
					cells.add(new Cell(r, c, null));
				}
			}
		}
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
	public void addCells(List<Cell> cells) {
		for (Cell cell : cells) {
			if (cell.comp != null) {
				container.add(cell.comp, cell.toString());
			}
		}
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
	public void removeRow(CellRow row) {
		doRemoveRow(row, true);
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
	private void doRemoveRow(CellRow row, boolean removeComp) {
		List<CellRow> nrows = new ArrayList<CellRow>();
		for (CellRow or : rows) {
			if (or.num != row.num) {
				if (or.num > row.num) {
					or.num--;
				}
				nrows.add(or);
			}
		}
		rows.clear();
		rows.addAll(nrows);

		List<Cell> ncells = new ArrayList<Cell>();
		for (Cell cell : cells) {
			if (cell.row != row.num) {
				if (cell.row > row.num) {
					cell.row--;
				}
				ncells.add(cell);
			} else if (removeComp && container != null && cell.comp != null) {
				container.remove(cell.comp);
			}
		}
		cells.clear();
		cells.addAll(ncells);
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
	public void removeColumn(CellColumn col) {
		doRemoveColumn(col, true);
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
	private void doRemoveColumn(CellColumn col, boolean removeComp) {
		List<CellColumn> ncols = new ArrayList<CellColumn>();
		for (CellColumn oc : cols) {
			if (oc.num != col.num) {
				if (oc.num > col.num) {
					oc.num--;
				}
				ncols.add(oc);
			}
		}
		cols.clear();
		cols.addAll(ncols);

		List<Cell> ncells = new ArrayList<Cell>();
		for (Cell cell : cells) {
			if (cell.col != col.num) {
				if (cell.col > col.num) {
					cell.col--;
				}
				ncells.add(cell);
			} else if (removeComp && container != null && cell.comp != null) {
				container.remove(cell.comp);
			}
		}
		cells.clear();
		cells.addAll(ncells);
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
	public void moveRow(int src, int dst) {

		List<Cell> cls1 = getCellsInRow(src);

		CellRow row = rows.get(src);
		insertRow(new CellRow(dst, row.origHeight, row.height));

		List<Cell> cls2 = getCellsInRow(dst);

		doRemoveRow(new CellRow(src < dst ? src : src + 1, 0, 0), false);

		int n = 0;
		for (Cell c1 : cls1) {
			Cell c2 = cls2.get(n++);
			c2.copy(c1, true);
			setOverlapping(c1, null, null);
			c1.comp = null;
			c1.rowOverlapp = 0;
			c1.colOverlapp = 0;
		}
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
	public void moveColumn(int src, int dst) {
		List<Cell> cls1 = getCellsInColumn(src);

		CellColumn col = cols.get(src);
		insertColumn(new CellColumn(dst, col.origWidth, col.width));

		List<Cell> cls2 = getCellsInColumn(dst);

		doRemoveColumn(new CellColumn(src < dst ? src : src + 1, 0, 0), false);

		int n = 0;
		for (Cell c1 : cls1) {
			Cell c2 = cls2.get(n++);
			c2.copy(c1, true);
			setOverlapping(c1, null, null);
			c1.comp = null;
			c1.rowOverlapp = 0;
			c1.colOverlapp = 0;
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public void setOverlapping(Cell cell, Point p, Cell overlaped) {
		for (int r = cell.row; r <= cell.row + cell.rowOverlapp; r++) {
			for (int c = cell.col; c <= cell.col + cell.colOverlapp; c++) {
				Cell cl = getCell(r, c);
				if (cl != null && (r != cell.row || c != cell.col)) {
					cl.overlapped = overlaped;
					if (p != null) {
						if (r == cell.row) {
							p.x += cl.bounds.width + hgap;
						}
						if (c == cell.col) {
							p.y += cl.bounds.height + vgap;
						}
					}
				}
			}
		}
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
	public void clear() {
		rows.clear();
		cols.clear();
		cells.clear();
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
	public void createCells() {
		cells.clear();
		for (int r = 0; r < rows.size(); r++) {
			for (int c = 0; c < cols.size(); c++) {
				cells.add(new Cell(r, c, null));
			}
		}
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
	public void initCells() {
		Dimension dim = container.getSize();
		Insets in = container.getInsets();
		dim.width -= in.left + in.right;
		dim.height -= in.top + in.bottom;

		//==== Rows

		int p = dim.height - vgap * (rows.size() + 1);
		int n = 0;
		int m = 0;
		for (CellRow row : rows) {
			row.num = m;
			if (row.origHeight > 1.0) {
				row.height = (int) row.origHeight;
				p -= row.height;

			} else if (row.origHeight == PREFERRED) {
				int h = 0;
				for (int i = 0; i < cols.size(); i++) {
					Cell cell = getCell(m, i);
					if (cell.comp != null) {
						h = Math.max(h, cell.comp.getPreferredSize().height);
					}
				}
				row.height = h == 0 ? vgap : h;
				p -= row.height;

			} else if (row.origHeight == FILL) {
				n++;
			}
			m++;
		}
		for (CellRow row : rows) {
			if (row.origHeight > 0 && row.origHeight <= 1.0) {
				row.height = (int) (p * row.origHeight);
				p -= row.height;
			}
		}

		for (CellRow row : rows) {
			if (row.origHeight == FILL) {
				row.height = p / n;
			}
		}

		//==== Columns

		p = dim.width - hgap * (cols.size() + 1);
		n = 0;
		m = 0;
		for (CellColumn col : cols) {
			col.num = m;
			if (col.origWidth > 1.0) {
				col.width = (int) col.origWidth;
				p -= col.width;
			} else if (col.origWidth == PREFERRED) {
				int w = 0;
				for (int i = 0; i < rows.size(); i++) {
					Cell cell = getCell(i, m);
					if (cell.comp != null) {
						w = Math.max(w, cell.comp.getPreferredSize().width);
					}
				}
				col.width = w == 0 ? hgap : w;
				p -= col.width;

			} else if (col.origWidth == FILL) {
				n++;
			}
			m++;
		}

		for (CellColumn col : cols) {
			if (col.origWidth > 0 && col.origWidth <= 1.0) {
				col.width = (int) (p * col.origWidth);
				p -= col.width;
			}
		}

		for (CellColumn col : cols) {
			if (col.origWidth == FILL) {
				col.width = p / n;
			}
		}

		//==== Cells ====

		int y = vgap + in.top;
		n = 0;
		for (CellRow or : rows) {
			int x = hgap + in.left;
			m = 0;
			for (CellColumn oc : cols) {
				Cell cell = getCell(n, m);
				cell.setBounds(x, y, oc.width, or.height);
				x += oc.width + hgap;
				m++;
			}
			y += or.height + vgap;
			n++;
		}

		//==== Overlaping ====

		for (int r = 0; r < rows.size(); r++) {
			for (int c = 0; c < cols.size(); c++) {
				Cell cell = getCell(r, c);
				if (cell.comp != null) {

					if (cell.usePreferred) {
						setOverlapping(cell, null, null);
						cell.rowOverlapp = 0;
						cell.colOverlapp = 0;

					} else if (cell.rowOverlapp > 0 || cell.colOverlapp > 0) {

						Point point = new Point(0, 0);
						setOverlapping(cell, point, cell);
						cell.comp.setSize(
							cell.bounds.width + point.x,
							cell.bounds.height + point.y
						);
					}
				}
			}
		}
	}

	/**
	 * Notwendig für den LayoutManager. Wird durch den Container aufgerufen, wenn ein Element zu den
	 * Container eingefügt wird.
	 *
	 * @param      comp  - die neue Komponente, die zum Containter eingefügt wurde.
	 * @param      co    - das Constraints-Objekt, welches die Regeln zum Layouten. enthält.
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public void addLayoutComponent(Component comp, Object constraints) {

		Cell c1 =
			constraints instanceof Cell ? (Cell) constraints : fromString(constraints.toString());

		Cell c2 = getCell(c1.row, c1.col);

		if (c2 != null) {
			c2.copy(c1, false);
			c2.comp = comp;
		}
	}

	/**
	 * Notwendig für den LayoutManager. Wird von TableLayout nicht verwendet. Zum Einfügen der
	 * Elemente zum Container muß immer eine TableConstraints verwendet werden.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public void addLayoutComponent(String name, Component comp) {
		throw new IllegalArgumentException("Cell must be used");
	}

	/**
	 * Notwendig für den LayoutManager. Wird durch den Container aufgerufen, wenn ein Element aus
	 * dem Container entfernt wird.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see
	 */
	public void removeLayoutComponent(Component comp) {
		for (Cell cell : cells) {
			if (cell.comp != null && cell.comp == comp) {
				setOverlapping(cell, null, null);
				cell.clear();
				break;
			}
		}
	}

	/**
	 * Notwendig für den LayoutManager.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public float getLayoutAlignmentX(Container parent) {
		return 0.0f;
	}

	/**
	 * Notwendig für den LayoutManager.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public float getLayoutAlignmentY(Container parent) {
		return 0.0f;
	}

	/**
	 * Notwendig für den LayoutManager.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public void invalidateLayout(Container parent) {
	}

	/**
	 * Notwendig für den LayoutManager. Wird vom Container aufgerufen, sobald die Große des
	 * Containers verändert wird oder durch die Methode 'pack' des Containers.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(1, 1);
	}

	/**
	 * Notwendig für den LayoutManager. Wird vom Container aufgerufen, sobald die Große des
	 * Containers verändert wird oder durch die Methode 'pack' des Containers.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public Dimension maximumLayoutSize(Container parent) {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Notwendig für den LayoutManager. Wird vom Container aufgerufen, sobald die Große des
	 * Containers verändert wird oder durch die Methode 'pack' des Containers.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public Dimension preferredLayoutSize(Container parent) {

		layoutContainer(parent);

		int w = hgap;
		int h = vgap;
		for (CellColumn oc : cols) {
			w += oc.width + hgap;
		}
		for (CellRow or : rows) {
			h += or.height + vgap;
		}
		return new Dimension(w, h);
	}

	/**
	 * Notwendig für den LayoutManager. Wird vom Container aufgerufen, sobald die Große des
	 * Containers verändert wird oder durch die Methode 'pack' des Containers.
	 *
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see        LayoutManager2
	 */
	public void layoutContainer(Container parent) {
		container = parent;
		initCells();
	}

	public static class CellRow {
		public int num;
		public double origHeight;
		public int height;

		public CellRow(int num, double origHeight, int height) {
			this.num = num;
			this.origHeight = origHeight;
			this.height = height;
		}

		public String toString() {
			return origHeight == FILL ? "CellsLayout.FILL"
			: origHeight == PREFERRED ? "CellsLayout.PREFERRED" : ("" + origHeight);
		}
	}

	public static class CellColumn {
		public int num;
		public double origWidth;
		public int width;

		public CellColumn(int num, double origWidth, int width) {
			this.num = num;
			this.origWidth = origWidth;
			this.width = width;
		}

		public String toString() {
			return origWidth == FILL ? "CellsLayout.FILL"
			: origWidth == PREFERRED ? "CellsLayout.PREFERRED" : ("" + origWidth);
		}
	}

	public static class Cell {
		public int row;
		public int col;
		public boolean usePreferred;
		public int rowOverlapp;
		public int colOverlapp;
		public int hAlign;
		public int vAlign;
		public Cell overlapped;
		public Rectangle bounds;
		public Component comp;

		public Cell() {
			clear();
		}

		public Cell(int row, int col) {
			this(row, col, false, 0, 0, LEFT, TOP, null);
		}

		public Cell(int row, int col, boolean usePreferred, int rowOverlapp, int colOverlapp) {
			this(row, col, usePreferred, rowOverlapp, colOverlapp, LEFT, TOP, null);
		}

		public Cell(
			int		row,
			int		col,
			boolean usePreferred,
			int		rowOverlapp,
			int		colOverlapp,
			int		hAlign,
			int		vAlign
		) {
			this(row, col, usePreferred, rowOverlapp, colOverlapp, hAlign, vAlign, null);
		}

		public Cell(int row, int col, Component comp) {
			this(row, col, false, 0, 0, LEFT, TOP, comp);
		}

		public Cell(
			int		  row,
			int		  col,
			boolean   usePreferred,
			int		  rowOverlapp,
			int		  colOverlapp,
			int		  hAlign,
			int		  vAlign,
			Component comp
		) {
			this.row = row;
			this.col = col;
			this.usePreferred = usePreferred;
			this.rowOverlapp = usePreferred ? 0 : rowOverlapp;
			this.colOverlapp = usePreferred ? 0 : colOverlapp;
			this.hAlign = hAlign;
			this.vAlign = vAlign;
			this.comp = comp;
			this.overlapped = null;
			this.bounds = new Rectangle(0, 0, 1, 1);
		}

		public Cell(Cell cell) {
			this(
				cell.row,
				cell.col,
				cell.usePreferred,
				cell.rowOverlapp,
				cell.colOverlapp,
				cell.hAlign,
				cell.vAlign,
				cell.comp
			);
		}

		public void setBounds(int x, int y, int width, int height) {
			bounds.setBounds(x, y, width, height);

			if (comp != null) {
				if (usePreferred) {

					Dimension d = comp.getPreferredSize();

					switch (hAlign) {

						case LEFT :
							break;

						case CENTER :
							x += width / 2 - d.width / 2;
							break;

						case RIGHT :
							x += width - d.width;
							break;
					}
					switch (vAlign) {

						case TOP :
							break;

						case CENTER :
							y += height / 2 - d.height / 2;
							break;

						case BOTTOM :
							y += height - d.height;
							break;
					}

					width = d.width;
					height = d.height;
				}
				comp.setBounds(x, y, width, height);
			}
		}

		public void clear() {
			comp = null;
			usePreferred = false;
			rowOverlapp = 0;
			colOverlapp = 0;
			hAlign = LEFT;
			vAlign = TOP;
		}

		public void copy(Cell c, boolean copyComp) {
			comp = copyComp ? c.comp : comp;
			usePreferred = c.usePreferred;
			rowOverlapp = c.rowOverlapp;
			colOverlapp = c.colOverlapp;
			hAlign = c.hAlign;
			vAlign = c.vAlign;
		}

		public Cell clone() {
			return new Cell(this);
		}

		public int compare(Cell c) {
			return row > c.row || col > c.col ? 1 : row < c.row || col < c.col ? -1 : 0;
		}

		public boolean equals(Object other) {
			if (other != null && other instanceof Cell) {
				Cell c = (Cell) other;
				return row == c.row && col == c.col;
			}
			return false;
		}

		public String toString() {
			return row + ", " + col + ", " + (usePreferred ? "1" : "0") + ", " + rowOverlapp + ", "
			+ colOverlapp + ", " + hAlign + ", " + vAlign;
		}

		public String toDesignerString() {
			String ha = hAlign == LEFT ? "LEFT" : hAlign == RIGHT ? "RIGHT" : "CENTER";
			String va = vAlign == TOP ? "TOP" : hAlign == BOTTOM ? "BOTTOM" : "CENTER";
			String s =
				"new CellsLayout.Cell(" + row + ", " + col + ", "
				+ (usePreferred ? "true" : "false") + ", " + rowOverlapp + ", " + colOverlapp
				+ ", CellsLayout." + ha + ", CellsLayout." + va + ")";
			;
			return s;
		}
	}
}
