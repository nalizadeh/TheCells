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

import org.nalizadeh.designer.property.jbeans.renderer.AbstractCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.BooleanCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.BorderCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.ColorCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.DebugGraphicsOptionCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.DefaultCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.DimensionCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.FontCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.HAlignmentCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.LocaleCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.NumberCellRenderer;
import org.nalizadeh.designer.property.jbeans.renderer.VAlignmentCellRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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
public class JBeansTableCellRenderer {

	private static final int HOTSPOT_SIZE = 10;

	private static final Color CATEGORY_BACKCOLOR = new Color(200, 200, 200);
	private static final Color CATEGORY_FORECOLOR = Color.BLACK;

	private TableCellRenderer categoryRenderer;
	private TableCellRenderer nameRenderer;

	private Map propertyToRenderer;
	private Map typeToRenderer;

	public JBeansTableCellRenderer(JBeansTable table) {

		table.addMouseListener(new CategoryVisibilityToggle());

		// table header not being visible, make sure we can still resize the columns
		new HeaderlessColumnResizer(table);

		categoryRenderer = new CategoryValueRenderer();
		nameRenderer = new NameRenderer();

		propertyToRenderer = new HashMap();
		typeToRenderer = new HashMap();

		registerDefaults();
	}

	public synchronized void registerRenderer(Class type, Class rendererClass) {
		typeToRenderer.put(type, rendererClass);
	}

	public synchronized void registerRenderer(Class type, TableCellRenderer renderer) {
		typeToRenderer.put(type, renderer);
	}

	public synchronized void unregisterRenderer(Class type) {
		typeToRenderer.remove(type);
	}

	public synchronized void registerRenderer(String name, Class rendererClass) {
		propertyToRenderer.put(name, rendererClass);
	}

	public synchronized void registerRenderer(String name, TableCellRenderer renderer) {
		propertyToRenderer.put(name, renderer);
	}

	public synchronized void unregisterRenderer(String name) {
		propertyToRenderer.remove(name);
	}

	/**
	 * Adds default renderers. This method is called by the constructor but may be called later to
	 * reset any customizations made through the <code>registerRenderer</code> methods. <b>Note: if
	 * overriden, <code>super.registerDefaults()</code> must be called before plugging custom
	 * defaults.</b>
	 */
	public void registerDefaults() {
		typeToRenderer.clear();
		propertyToRenderer.clear();

		// use the default renderer for Object and all primitives
		DefaultCellRenderer renderer = new DefaultCellRenderer();
		BooleanCellRenderer booleanRenderer = new BooleanCellRenderer();
		NumberCellRenderer numberRenderer = new NumberCellRenderer();
		ColorCellRenderer colorRenderer = new ColorCellRenderer();
		FontCellRenderer fontRenderer = new FontCellRenderer();
		BorderCellRenderer borderRenderer = new BorderCellRenderer();
		HAlignmentCellRenderer haligRenderer = new HAlignmentCellRenderer();
		VAlignmentCellRenderer valigRenderer = new VAlignmentCellRenderer();
		DimensionCellRenderer dimRenderer = new DimensionCellRenderer();
		DebugGraphicsOptionCellRenderer dgRenderer = new DebugGraphicsOptionCellRenderer();
		LocaleCellRenderer localeRenderer = new LocaleCellRenderer();

		registerRenderer("horizontalAlignment", haligRenderer);
		registerRenderer("horizontalTextPosition", haligRenderer);
		registerRenderer("verticalAlignment", valigRenderer);
		registerRenderer("verticalTextPosition", valigRenderer);
		registerRenderer("debugGraphicsOptions", dgRenderer);

		registerRenderer(Object.class, renderer);
		registerRenderer(boolean.class, booleanRenderer);
		registerRenderer(Boolean.class, booleanRenderer);
		registerRenderer(Color.class, colorRenderer);
		registerRenderer(Font.class, fontRenderer);
		registerRenderer(Border.class, borderRenderer);
		registerRenderer(byte.class, renderer);
		registerRenderer(Byte.class, renderer);
		registerRenderer(char.class, renderer);
		registerRenderer(Character.class, renderer);
		registerRenderer(short.class, numberRenderer);
		registerRenderer(Short.class, numberRenderer);
		registerRenderer(int.class, numberRenderer);
		registerRenderer(Integer.class, numberRenderer);
		registerRenderer(long.class, numberRenderer);
		registerRenderer(Long.class, numberRenderer);
		registerRenderer(double.class, numberRenderer);
		registerRenderer(Double.class, numberRenderer);
		registerRenderer(float.class, numberRenderer);
		registerRenderer(Float.class, numberRenderer);
		registerRenderer(Dimension.class, dimRenderer);
		registerRenderer(Locale.class, localeRenderer);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#getCellRenderer(int, int)
	 */
	public TableCellRenderer getCellRenderer(JBeansTable table, int row, int column) {
		JBeansTableModel model = (JBeansTableModel) table.getModel();
		JBeansTableModel.Item item = model.getItem(row);

		switch (column) {

			case JBeansTableModel.NAME_COLUMN :

				// name column gets a custom renderer
				return nameRenderer;

			case JBeansTableModel.VALUE_COLUMN : {
				if (!item.isProperty()) {
					return categoryRenderer;
				}

				// property value column gets the renderer from the factory, but wrapped
				JBeansProperty property = item.getProperty();
				TableCellRenderer renderer = getRenderer(property);
				if (renderer == null) {
					renderer = getRenderer(property.getType());
				}
				return new WrappedRenderer(renderer);
			}

			default :

				// when will this happen, given the above?
				return table.getCellRenderer(row, column);
		}
	}

	/**
	 * Gets a renderer for the given property property.
	 */
	public synchronized TableCellRenderer getRenderer(JBeansProperty property) {
		TableCellRenderer renderer = null;
		Object value = propertyToRenderer.get(property.getName());
		if (value instanceof TableCellRenderer) {
			renderer = (TableCellRenderer) value;
		} else if (value instanceof Class) {
			try {
				renderer = (TableCellRenderer) ((Class) value).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			renderer = getRenderer(property.getType());
		}
		return renderer == null ? getRenderer(Object.class) : renderer;
	}

	/**
	 * Gets a renderer for the given property type.
	 */
	public synchronized TableCellRenderer getRenderer(Class type) {
		TableCellRenderer renderer = null;
		Object value = typeToRenderer.get(type);
		if (value instanceof TableCellRenderer) {
			renderer = (TableCellRenderer) value;
		} else if (value instanceof Class) {
			try {
				renderer = (TableCellRenderer) ((Class) value).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return renderer;
	}

	private static class CellBorder implements Border {

		private Color background;
		private int indentWidth; // space before hotspot
		private boolean showToggle;
		private boolean toggleState;
		private Icon expandedIcon = (Icon) UIManager.get(JBeansUtils.TREE_EXPANDED_ICON_KEY);
		private Icon collapsedIcon = (Icon) UIManager.get(JBeansUtils.TREE_COLLAPSED_ICON_KEY);
		private Insets insets = new Insets(1, 0, 1, 1);
		private boolean isProperty;

		public void configure(JBeansTable table, JBeansTableModel.Item item, Color background) {

			isProperty = item.isProperty();
			toggleState = item.isVisible();
			showToggle = item.hasToggle();

			indentWidth = getIndent(table, item) + 2;
			insets.left = indentWidth + (showToggle ? HOTSPOT_SIZE : 0) + 2;

			this.background = background;
		}

		public Insets getBorderInsets(Component c) {
			return insets;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			if (!isProperty) {
				Color oldColor = g.getColor();
				g.setColor(background);
				g.fillRect(x, y, x + HOTSPOT_SIZE - 2, y + height);
				g.setColor(oldColor);
			}

			if (showToggle) {
				Icon drawIcon = (toggleState ? expandedIcon : collapsedIcon);
				drawIcon.paintIcon(
					c,
					g,
					x + indentWidth + (HOTSPOT_SIZE - 2 - drawIcon.getIconWidth()) / 2,
					y + (height - drawIcon.getIconHeight()) / 2
				);
			}
		}

		public boolean isBorderOpaque() {
			return true;
		}
	}

	/**
	 * NameRenderer
	 */
	private static class NameRenderer extends AbstractCellRenderer {

		private CellBorder border = new CellBorder();

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {
			Component co = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (value == null) {
				return co;
			}

			JBeansTableModel.Item item = (JBeansTableModel.Item) value;
			border.configure(
				(JBeansTable) table,
				item,
				isSelected ? getBackground() : CATEGORY_BACKCOLOR
			);
			setBorder(border);

			setText(item.getName());

			if (!item.isProperty()) {
				JBeansTableModel model = ((JBeansTable) table).getBeansModel();

				setEnabled(true);
				setFont(getFont().deriveFont(Font.BOLD));
				setIcon(model.getCategoryIcon(item.getName()));

				if (!isSelected) {
					setBackground(CATEGORY_BACKCOLOR);
					setForeground(CATEGORY_FORECOLOR);
				}

			} else {
				setEnabled(isSelected ? true : item.getProperty().isEditable());
				setFont(getFont().deriveFont(Font.PLAIN));
				setIcon(
					item.getProperty().isChanged() ? JBeansUtils.DIRTYPROP : JBeansUtils.CLEANPROP
				);
			}
			return this;
		}
	}

	/**
	 * CategoryValueRenderer
	 */
	private static class CategoryValueRenderer extends AbstractCellRenderer {

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (!isSelected) {
				setBackground(CATEGORY_BACKCOLOR);
				setForeground(CATEGORY_FORECOLOR);
			}
			setText("");
			return this;
		}
	}

	/**
	 * WrappedRenderer
	 */
	private static class WrappedRenderer implements TableCellRenderer {
		private TableCellRenderer renderer;

		public WrappedRenderer(TableCellRenderer renderer) {
			this.renderer = renderer;
		}

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {
			JBeansTableModel model = ((JBeansTable) table).getBeansModel();
			JBeansTableModel.Item item = (JBeansTableModel.Item) model.getItem(row);
			((Component) renderer).setEnabled(item.isProperty() && item.getProperty().isEditable());
			return renderer.getTableCellRendererComponent(
				table,
				value,
				isSelected,
				hasFocus,
				row,
				column
			);
		}
	}

	private static class CategoryVisibilityToggle extends MouseAdapter {
		public void mouseReleased(MouseEvent event) {
			JBeansTable table = (JBeansTable) event.getComponent();
			int row = table.rowAtPoint(event.getPoint());
			int column = table.columnAtPoint(event.getPoint());
			if (row != -1 && column == 0) {
				JBeansTableModel.Item item = table.getBeansModel().getItem(row);
				int x = event.getX() - getIndent(table, item);
				if (x > 0 && x < HOTSPOT_SIZE) {
					item.toggle();
				}
			}
		}
	}

	static int getIndent(JBeansTable table, JBeansTableModel.Item item) {
		int indent = 0;

		if (item.isProperty()) {

			// it is a property, it has no parent or a category, and no child
			if ((item.getParent() == null || !item.getParent().isProperty()) && !item.hasToggle()) {
				indent = table.getUseExtraIndent() ? HOTSPOT_SIZE : 0;
			} else {

				// it is a property with children
				if (item.hasToggle()) {
					indent = item.getDepth() * HOTSPOT_SIZE;
				} else {
					indent = (item.getDepth() + 1) * HOTSPOT_SIZE;
				}
			}

			if (
				table.getBeansModel().getMode() == JBeansTableModel.VIEW_CATEGORIES
				&& table.getUseExtraIndent()
			) {
				indent += HOTSPOT_SIZE;
			}

		} else {

			// category has no indent
			indent = 0;
		}
		return indent;
	}

	/**
	 * HeaderlessColumnResizer.<br>
	 */
	private static class HeaderlessColumnResizer extends MouseInputAdapter {

		private static Cursor resizeCursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);

		private int mouseXOffset;
		private Cursor otherCursor = resizeCursor;

		private JTable table;

		public HeaderlessColumnResizer(JTable table) {
			this.table = table;
			table.addMouseListener(this);
			table.addMouseMotionListener(this);
		}

		private boolean canResize(TableColumn column) {
			return (column != null) && table.getTableHeader().getResizingAllowed()
			&& column.getResizable();
		}

		private TableColumn getResizingColumn(Point p) {
			return getResizingColumn(p, table.columnAtPoint(p));
		}

		private TableColumn getResizingColumn(Point p, int column) {
			if (column == -1) {
				return null;
			}
			int row = table.rowAtPoint(p);
			Rectangle r = table.getCellRect(row, column, true);
			r.grow(-3, 0);
			if (r.contains(p)) {
				return null;
			}
			int midPoint = r.x + r.width / 2;
			int columnIndex;
			if (table.getTableHeader().getComponentOrientation().isLeftToRight()) {
				columnIndex = (p.x < midPoint) ? column - 1 : column;
			} else {
				columnIndex = (p.x < midPoint) ? column : column - 1;
			}
			if (columnIndex == -1) {
				return null;
			}
			return table.getTableHeader().getColumnModel().getColumn(columnIndex);
		}

		public void mousePressed(MouseEvent e) {
			table.getTableHeader().setDraggedColumn(null);
			table.getTableHeader().setResizingColumn(null);
			table.getTableHeader().setDraggedDistance(0);

			Point p = e.getPoint();

			// First find which header cell was hit
			int index = table.columnAtPoint(p);

			if (index != -1) {

				// The last 3 pixels + 3 pixels of next column are for resizing
				TableColumn resizingColumn = getResizingColumn(p, index);
				if (canResize(resizingColumn)) {
					table.getTableHeader().setResizingColumn(resizingColumn);
					if (table.getTableHeader().getComponentOrientation().isLeftToRight()) {
						mouseXOffset = p.x - resizingColumn.getWidth();
					} else {
						mouseXOffset = p.x + resizingColumn.getWidth();
					}
				}
			}
		}

		private void swapCursor() {
			Cursor tmp = table.getCursor();
			table.setCursor(otherCursor);
			otherCursor = tmp;
		}

		public void mouseMoved(MouseEvent e) {
			if (canResize(getResizingColumn(e.getPoint())) != (table.getCursor() == resizeCursor)) {
				swapCursor();
			}
		}

		public void mouseDragged(MouseEvent e) {
			int mouseX = e.getX();

			TableColumn resizingColumn = table.getTableHeader().getResizingColumn();

			boolean headerLeftToRight =
				table.getTableHeader().getComponentOrientation().isLeftToRight();

			if (resizingColumn != null) {
				int oldWidth = resizingColumn.getWidth();
				int newWidth;
				if (headerLeftToRight) {
					newWidth = mouseX - mouseXOffset;
				} else {
					newWidth = mouseXOffset - mouseX;
				}
				resizingColumn.setWidth(newWidth);

				Container container;
				if (
					(table.getTableHeader().getParent() == null)
					|| ((container = table.getTableHeader().getParent().getParent()) == null)
					|| !(container instanceof JScrollPane)
				) {
					return;
				}

				if (!container.getComponentOrientation().isLeftToRight() && !headerLeftToRight) {
					if (table != null) {
						JViewport viewport = ((JScrollPane) container).getViewport();
						int viewportWidth = viewport.getWidth();
						int diff = newWidth - oldWidth;
						int newHeaderWidth = table.getWidth() + diff;

						/* Resize a table */
						Dimension tableSize = table.getSize();
						tableSize.width += diff;
						table.setSize(tableSize);

						/*
						 * If this table is in AUTO_RESIZE_OFF mode and has a horizontal scrollbar,
						 * we need to update a view's position.
						 */
						if (
							(newHeaderWidth >= viewportWidth)
							&& (table.getAutoResizeMode() == JTable.AUTO_RESIZE_OFF)
						) {
							Point p = viewport.getViewPosition();
							p.x = Math.max(0, Math.min(newHeaderWidth - viewportWidth, p.x + diff));
							viewport.setViewPosition(p);

							/* Update the original X offset value. */
							mouseXOffset += diff;
						}
					}
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			table.getTableHeader().setResizingColumn(null);
			table.getTableHeader().setDraggedColumn(null);
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
}
