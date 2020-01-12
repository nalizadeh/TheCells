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

import org.nalizadeh.designer.base.DesignerConstraints;
import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.property.Property;

import org.nalizadeh.designer.util.CellsGraphics;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
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
public class CellsLayoutContainer extends DesignerContainer {

	private CellsLayoutDesigner myFrame = null;
	private CellsLayout layout;

	public CellsLayoutContainer(DesignerFrame frame, boolean isRoot, Container container) {
		this(frame, isRoot, true, container);
	}

	public CellsLayoutContainer(DesignerFrame frame, boolean isRoot, boolean withFrame, Container container) {
		super(frame, isRoot, new CellsLayout(4, 4), container);

		if (withFrame && frame instanceof CellsLayoutDesigner) {
			myFrame = (CellsLayoutDesigner) frame;
		}

                layout = (CellsLayout)getLayout();

                if (container == null) {
                        layout.addColumn(60);
                        layout.addRow(20);
                        layout.createCells();
                }
	}

        public void paint(Graphics g) {
                super.paint(g);
        }

	public void copy(Container src, Container dst) {
		CellsLayout l1 = (CellsLayout) src.getLayout();
		CellsLayout l2 = (CellsLayout) dst.getLayout();

		List<CellsLayout.CellRow> rows = l1.getRows();
		List<CellsLayout.CellColumn> cols = l1.getColumns();
		List<CellsLayout.Cell> cells = l1.getCells();

		l2.clear();
		dst.removeAll();

		l2.setHgap(l1.getHgap());
		l2.setVgap(l1.getVgap());

		for (CellsLayout.CellRow row : rows) {
			l2.addRow(row.origHeight);
		}
		for (CellsLayout.CellColumn col : cols) {
			l2.addColumn(col.origWidth);
		}
		l2.createCells();

		for (CellsLayout.Cell cell : cells) {
			if (cell.comp != null) {
				dst.add(CellsUtil.designerOrNotDesigner(this, cell.comp), cell.clone());
			}
		}

		l1.clear();
		src.removeAll();
	}

	public void convert(Container src) {
		CellsLayout la = (CellsLayout) getLayout();
		la.clear();
		la.addRow(20);

		int n = Math.max(src.getComponentCount(), 1);
		for (int i = 0; i < n; i++) {
			la.addColumn(80);
		}
		la.createCells();
		int c = 0;
		for (Component comp : src.getComponents()) {
			add(comp, "0," + c);
			c++;
		}
	}

	protected void showHint(Graphics g, Component comp, int x, int y, Rectangle bounds, boolean rollover) {

		CellsLayout.Cell hint =
			rollover ? null : (CellsLayout.Cell) getLayoutConstraints(null, x, y, bounds);

		List<CellsLayout.Cell> lc = ((CellsLayout) getLayout()).getCells();
		for (CellsLayout.Cell c : lc) {

			if (c.overlapped == null) {

				if (myFrame != null) {

					int rs = myFrame.getRowPanel().getSelectedRow();
					int cs = myFrame.getColumnPanel().getSelectedColumn();

					if (c.equals(hint) || c.row == rs || c.col == cs) {
						CellsGraphics.drawCompositeRect(g, c.bounds, Color.GREEN);
					}
				} else if (c.equals(hint)) {
					CellsGraphics.drawCompositeRect(g, c.bounds, Color.GREEN);
				}

				if (c.comp == null) {
					Rectangle r = c.bounds;
					g.setColor(Color.LIGHT_GRAY);
					g.drawRect(r.x, r.y, r.width, r.height);
				}
			}
		}
	}

        protected Component getLayoutComponent(Component comp, Rectangle bounds) {
		CellsLayout.Cell cell = (CellsLayout.Cell)getLayoutConstraints(comp, bounds.x, bounds.y, null);
		return cell == null ? null : cell.comp;
        }

        protected Object getLayoutConstraints(Component comp, int x, int y, Rectangle bounds) {
		CellsLayout.Cell cell = x == -1 && y == -1 ? layout.getCell(comp) : layout.getCellAt(x, y);
                if (cell != null && cell.overlapped != null) {
                        cell = cell.overlapped;
                }
		return cell == null ? null : cell.clone();
	}

	public Object cloneLayoutConstraints(Object cons) {
		return cons == null ? null : ((CellsLayout.Cell) cons).clone();
	}

	public void copyLayoutConstraints(Object src, Object dst) {
		CellsLayout.Cell s = (CellsLayout.Cell) src;
		CellsLayout.Cell d = (CellsLayout.Cell) dst;
		d.copy(s, false);
	}

	protected boolean resizeComponent(Component comp, Rectangle oldBounds, Rectangle newBounds) {
                Rectangle b = newBounds;
		CellsLayout.Cell se = layout.getCell(comp);
		CellsLayout.Cell c1 = layout.getCellAt(b.x + 1, b.y + 1);
		CellsLayout.Cell c2 = layout.getCellAt(b.x + b.width - 2, b.y + b.height - 2);

		if (se != null && !se.usePreferred && c1 != null && c2 != null) {

                        se.comp = comp;

			CellsLayout.Cell newCell = c1.clone();
			newCell.comp = comp;
			newCell.rowOverlapp = c2.row - c1.row;
			newCell.colOverlapp = c2.col - c1.col;

			return doResizeComponent(
				new DesignerConstraints(se.clone(), se.comp, this),
				new DesignerConstraints(newCell, newCell.comp, this)
			);
		}
                return false;
	}

	//==================================================================
	// PropertySheet Implementation
	//==================================================================

	private CellsLayout.CellRow row;
	private CellsLayout.CellColumn col;

	private JPanel rowSheetPanel = null;
	private JPanel colSheetPanel = null;

	private boolean initAdjusting = false;

	//=== RowSheet ====

	private JTextField tfNumber = new JTextField();
	private JLabel lbRowCol = new JLabel("Row:");
	private JLabel lbWidHei = new JLabel("Height:");
	private JSpinner tfFix =
		new JSpinner(
			new SpinnerNumberModel(
				new Integer(0),
				new Integer(0),
				new Integer(Integer.MAX_VALUE),
				new Integer(1)
			)
		);

	private JSpinner tfRelativ =
		new JSpinner(
			new SpinnerNumberModel(
				new Double(0.1),
				new Double(0.1),
				new Double(1.0),
				new Double(0.1)
			)
		);

	private JTextField tfPreferred = new JTextField();
	private JRadioButton rb1 = new JRadioButton("fix");
	private JRadioButton rb2 = new JRadioButton("relativ (%)");
	private JRadioButton rb3 = new JRadioButton("preferred");
	private JRadioButton rb4 = new JRadioButton("fill");

	//=== ColumnSheet ====

	private JTextField tfNumber2 = new JTextField();
	private JLabel lbRowCol2 = new JLabel("Column:");
	private JLabel lbWidHei2 = new JLabel("Width:");
	private JSpinner tfFix2 =
		new JSpinner(
			new SpinnerNumberModel(
				new Integer(0),
				new Integer(0),
				new Integer(Integer.MAX_VALUE),
				new Integer(1)
			)
		);

	private JSpinner tfRelativ2 =
		new JSpinner(
			new SpinnerNumberModel(
				new Double(0.1),
				new Double(0.1),
				new Double(1.0),
				new Double(0.1)
			)
		);

	private JTextField tfPreferred2 = new JTextField();
	private JRadioButton rb12 = new JRadioButton("fix");
	private JRadioButton rb22 = new JRadioButton("relativ (%)");
	private JRadioButton rb32 = new JRadioButton("preferred");
	private JRadioButton rb42 = new JRadioButton("fill");

	public void updateSheets() {

		int r = -1;
		int c = -1;
		if (myFrame != null) {
			r = myFrame.getRowPanel().getSelectedRow();
			c = myFrame.getColumnPanel().getSelectedColumn();
		}

		row = r == -1 ? null : (CellsLayout.CellRow) layout.getRows().get(r);
		col = c == -1 ? null : (CellsLayout.CellColumn) layout.getColumns().get(c);

		initAdjusting = true;

		updateRowSheet();
		updateColumnSheet();

		initAdjusting = false;
	}

	public JPanel createRowSheet() {
		if (row == null) {
			return null;
		}
		if (rowSheetPanel == null) {

			ButtonGroup bg = new ButtonGroup();
			bg.add(rb1);
			bg.add(rb2);
			bg.add(rb3);
			bg.add(rb4);

			rb1.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tfFix.setEnabled(true);
						tfFix.setValue(row.height);
						tfRelativ.setEnabled(false);
						tfRelativ.setValue(0.1);
						apply(1);
					}
				}
			);

			rb2.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tfFix.setEnabled(false);
						tfFix.setValue(0);
						tfRelativ.setEnabled(true);
						tfRelativ.setValue(row.origHeight);
						apply(1);
					}
				}
			);

			rb3.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tfFix.setEnabled(false);
						tfFix.setValue(0);
						tfRelativ.setEnabled(false);
						tfRelativ.setValue(0.1);
						apply(1);
					}
				}
			);

			rb4.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tfFix.setEnabled(false);
						tfFix.setValue(0);
						tfRelativ.setEnabled(false);
						tfRelativ.setValue(0.1);
						apply(1);
					}
				}
			);

			//========================================

			tfFix.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						apply(1);
					}
				}
			);

			tfRelativ.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						apply(1);
					}
				}
			);

			double[][] dat = {
					{ 10, 22, 22, 22, 22, 22, 10 },
					{ 40, 80, 60 }
				};

			rowSheetPanel = new JPanel(new CellsLayout(dat, 4, 4));
			rowSheetPanel.add(lbRowCol, "1,0");
			rowSheetPanel.add(tfNumber, "1,1");
			rowSheetPanel.add(lbWidHei, "2,0");
			rowSheetPanel.add(rb1, "2,1");
			rowSheetPanel.add(tfFix, "2,2");
			rowSheetPanel.add(rb2, "3,1");
			rowSheetPanel.add(tfRelativ, "3,2");
			rowSheetPanel.add(rb3, "4,1");
			rowSheetPanel.add(tfPreferred, "4,2");
			rowSheetPanel.add(rb4, "5,1");
		}
		return rowSheetPanel;
	}

	public JPanel createColumnSheet() {
		if (col == null) {
			return null;
		}
		if (colSheetPanel == null) {

			ButtonGroup bg = new ButtonGroup();
			bg.add(rb12);
			bg.add(rb22);
			bg.add(rb32);
			bg.add(rb42);

			rb12.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tfFix2.setEnabled(true);
						tfFix2.setValue(col.width);
						tfRelativ2.setEnabled(false);
						tfRelativ2.setValue(0.1);
						apply(2);
					}
				}
			);

			rb22.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tfFix2.setEnabled(false);
						tfFix2.setValue(0);
						tfRelativ2.setEnabled(true);
						tfRelativ2.setValue(col.origWidth);
						apply(2);
					}
				}
			);

			rb32.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tfFix2.setEnabled(false);
						tfFix2.setValue(0);
						tfRelativ2.setEnabled(false);
						tfRelativ2.setValue(0.1);
						apply(2);
					}
				}
			);

			rb42.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tfFix2.setEnabled(false);
						tfFix2.setValue(0);
						tfRelativ2.setEnabled(false);
						tfRelativ2.setValue(0.1);
						apply(2);
					}
				}
			);

			//========================================

			tfFix2.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						apply(2);
					}
				}
			);

			tfRelativ2.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						apply(2);
					}
				}
			);

			double[][] dat = {
					{ 10, 22, 22, 22, 22, 22, 10 },
					{ 40, 80, 60 }
				};

			colSheetPanel = new JPanel(new CellsLayout(dat, 4, 4));
			colSheetPanel.add(lbRowCol2, "1,0");
			colSheetPanel.add(tfNumber2, "1,1");
			colSheetPanel.add(lbWidHei2, "2,0");
			colSheetPanel.add(rb12, "2,1");
			colSheetPanel.add(tfFix2, "2,2");
			colSheetPanel.add(rb22, "3,1");
			colSheetPanel.add(tfRelativ2, "3,2");
			colSheetPanel.add(rb32, "4,1");
			colSheetPanel.add(tfPreferred2, "4,2");
			colSheetPanel.add(rb42, "5,1");
		}
		return colSheetPanel;
	}

	public void updateLayout(Object... arg) {

		CellsLayout la = (CellsLayout) getLayout();

		la.setHgap((Integer) arg[2]);
		la.setVgap((Integer) arg[3]);

		int r = (Integer) arg[0];
		int c = (Integer) arg[1];
		int rz = layout.getRows().size();
		int cz = layout.getColumns().size();

		if (r > rz) {
			for (int i = 0; i < r - rz; i++) {
				Action action =
					getFrame().getDesigner().getActionManager().getAction("InsertRowAfter");
				action.putValue("Row", rz + i - 1);
				action.putValue("OrigHeight", 20.0);
				action.putValue("DoSelect", Boolean.FALSE);
				action.actionPerformed(null);
			}
		} else {
			while (r < rz) {
				Action action = getFrame().getDesigner().getActionManager().getAction("DeleteRow");
				action.putValue("Row", --rz);
				action.actionPerformed(null);
			}
		}

		if (c > cz) {
			for (int i = 0; i < c - cz; i++) {
				Action action =
					getFrame().getDesigner().getActionManager().getAction("InsertColumnAfter");
				action.putValue("Column", cz + i - 1);
				action.putValue("OrigWidth", 60.0);
				action.putValue("DoSelect", Boolean.FALSE);
				action.actionPerformed(null);
			}
		} else {
			while (c < cz) {
				Action action =
					getFrame().getDesigner().getActionManager().getAction("DeleteColumn");
				action.putValue("Column", --cz);
				action.actionPerformed(null);
			}

		}
	}

	public Object[] getLayoutProperty() {
		CellsLayout layout = (CellsLayout) getLayout();
		Object[] props = new Object[6];
		props[0] = layout.getRows().size();
		props[1] = layout.getColumns().size();
		props[2] = layout.getHgap();
		props[3] = layout.getVgap();
		props[4] = 0;
		props[5] = 0;
		return props;
	}

	public Property[] getConstraintsProperties(Component comp) {
		CellsLayout layout = (CellsLayout) getLayout();
		CellsLayout.Cell cell = (CellsLayout.Cell) layout.getConstraints(comp);

		if (cell == null) {
			return new Property[0];
		}
		Property[] ats =
			{
				new Property("row", Integer.class, cell.row, true, true),
				new Property("column", Integer.class, cell.col, true, true),
				new Property("usePreferredSize", Boolean.class, cell.usePreferred, false, true),
				new Property(
					"rowOverlapping",
					Integer.class,
					cell.rowOverlapp,
					cell.usePreferred ? true : false,
					true
				),
				new Property(
					"columnOverlapping",
					Integer.class,
					cell.colOverlapp,
					cell.usePreferred ? true : false,
					true
				),
				new Property(
					"hAlignment",
					Integer.class,
					cell.hAlign,
					cell.usePreferred ? false : true,
					true
				),
				new Property(
					"vAlignment",
					Integer.class,
					cell.vAlign,
					cell.usePreferred ? false : true,
					true
				), new Property("isOverlapped", Boolean.class, cell.overlapped != null, true, true)
			};

		return ats;
	}

	public boolean isConstraintsProperty(Component comp, String name) {
		return name.equals("usePreferredSize") || name.equals("rowOverlapping")
		|| name.equals("columnOverlapping") || name.equals("hAlignment")
		|| name.equals("vAlignment");
	}

	public Object changeConstraintsProperty(Component comp, String name, Object value) {

		Object oldValue = null;

                CellsLayout.Cell cell = (CellsLayout.Cell) layout.getConstraints(comp);

		if (name.equals("usePreferredSize")) {
			oldValue = cell.usePreferred;
			cell.usePreferred = (Boolean) value;

		} else if (name.equals("rowOverlapping")) {
			oldValue = cell.rowOverlapp;
			layout.setOverlapping(cell, null, null);
			cell.rowOverlapp = (Integer) value;

		} else if (name.equals("columnOverlapping")) {
			oldValue = cell.colOverlapp;
			layout.setOverlapping(cell, null, null);
			cell.colOverlapp = (Integer) value;

		} else if (name.equals("hAlignment")) {
			oldValue = cell.hAlign;
			cell.hAlign = (Integer) value;

		} else if (name.equals("vAlignment")) {
			oldValue = cell.vAlign;
			cell.vAlign = (Integer) value;
		}

		return oldValue;
	}

	private void updateRowSheet() {

		tfNumber.setEnabled(false);
		tfNumber.setText("");
		tfFix.setValue(0);
		tfRelativ.setValue(0.0);
		tfPreferred.setEnabled(false);
		tfPreferred.setText("");

		if (row != null) {

			tfNumber.setText("" + row.num);

			double d = row.origHeight;
			int p = row.height;

			rb1.setSelected(d > 1.0);
			rb2.setSelected(d > 0.0 && d <= 1.0);
			rb3.setSelected(d == CellsLayout.PREFERRED);
			rb4.setSelected(d == CellsLayout.FILL);

			tfFix.setEnabled(d > 1.0);
			tfFix.setValue(d > 1.0 ? p : 0);

			tfRelativ.setEnabled(d > 0.0 && d <= 1.0);
			tfRelativ.setValue(d > 0.0 && d <= 1.0 ? d : 0.0);

			tfPreferred.setEnabled(false);
			tfPreferred.setText(d == CellsLayout.PREFERRED ? (p + "") : "");
		}
	}

	private void updateColumnSheet() {

		tfNumber2.setEnabled(false);
		tfNumber2.setText("");
		tfFix2.setValue(0);
		tfRelativ2.setValue(0.0);
		tfPreferred2.setEnabled(false);
		tfPreferred2.setText("");

		if (col != null) {

			tfNumber2.setText("" + col.num);

			double d = col.origWidth;
			int p = col.width;

			rb12.setSelected(d > 1.0);
			rb22.setSelected(d > 0.0 && d <= 1.0);
			rb32.setSelected(d == CellsLayout.PREFERRED);
			rb42.setSelected(d == CellsLayout.FILL);

			tfFix2.setEnabled(d > 1.0);
			tfFix2.setValue(d > 1.0 ? p : 0);

			tfRelativ2.setEnabled(d > 0.0 && d <= 1.0);
			tfRelativ2.setValue(d > 0.0 && d <= 1.0 ? d : 0.0);

			tfPreferred2.setEnabled(false);
			tfPreferred2.setText(d == CellsLayout.PREFERRED ? (p + "") : "");
		}
	}

	private void apply(int m) {
		if (!initAdjusting) {
			if (m == 1) {
				double d =
					rb1.isSelected() ? (Integer) tfFix.getValue()
					: rb2.isSelected() ? (Double) tfRelativ.getValue()
					: rb3.isSelected() ? CellsLayout.PREFERRED
					: rb4.isSelected() ? CellsLayout.FILL : 0.0;

				row.origHeight = d;
			} else {
				double d =
					rb12.isSelected() ? (Integer) tfFix2.getValue()
					: rb22.isSelected() ? (Double) tfRelativ2.getValue()
					: rb32.isSelected() ? CellsLayout.PREFERRED
					: rb42.isSelected() ? CellsLayout.FILL : 0.0;
				col.origWidth = d;
			}

			getFrame().getDesigner().redraw();
		}
	}
}
