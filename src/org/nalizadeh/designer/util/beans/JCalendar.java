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

package org.nalizadeh.designer.util.beans;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.MemoryImageSource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
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
public class JCalendar extends JPanel {

	private static int m1 = 255 << 16;
	private static int m2 = 255 << 24;
	private static int m3 = 0xff0000ff;
	private static int m4 = 0xfff3f3f3;

	private static int r = 0xffff0000;
	private static int g = 0xff00ff00;
	private static int b = 0xff0000ff;

	// Bitmap für Image
	private static int[] fpix =
		{
			m2, m2, m2, m2, m2, m2, m2, m2, m2, m2, m1, m1, m1, m1, m2, m4, m1, m4, m1, m4, m1, m4,
			m1, m2, m1, m1, m1, m1, m2, m1, m4, m2, m2, m2, m2, m2, m2, m2, m2, m2, m2, m2, m2, m4,
			m1, m2, m4, m1, m4, m1, m4, m1, m4, m1, m4, m2, m2, m1, m4, m2, m1, m4, m1, m4, m1, m4,
			m1, m4, m1, m2, m2, m4, m1, m2, m4, m1, m3, m3, m3, m3, m3, m1, m4, m2, m2, m1, m4, m2,
			m1, m4, m3, m3, m3, m3, m3, m4, m1, m2, m2, m4, m1, m2, m4, m1, m4, m1, m4, m3, m3, m1,
			m4, m2, m2, m1, m4, m2, m1, m4, m1, m4, m3, m3, m1, m4, m1, m2, m2, m4, m1, m2, m4, m1,
			m4, m1, m3, m3, m4, m1, m4, m2, m2, m1, m4, m2, m1, m4, m1, m3, m3, m4, m1, m4, m1, m2,
			m2, m2, m2, m2, m4, m1, m4, m3, m3, m1, m4, m1, m4, m2, m1, m1, m1, m2, m1, m4, m1, m4,
			m1, m4, m1, m4, m1, m2, m1, m1, m1, m2, m2, m2, m2, m2, m2, m2, m2, m2, m2, m2,
		};

	// Image selber als Bitmap bauen
	private static Image fimg =
		java.awt.Toolkit.getDefaultToolkit().createImage(
			new MemoryImageSource(14, 14, fpix, 0, 14)
		);

	/*
	 * private static int[] fpix2 = { m2,m2,m2,m2,m2,m2,m2, m1,m2,m2,m2,m2,m2,m1,
	 * m1,m1,m2,m2,m2,m1,m1, m1,m1,m1,m2,m1,m1,m1};
	 *
	 * private static Image fimg2 = Toolkit.getDefaultToolkit(). createImage(new
	 * MemoryImageSource(7,4,fpix,0,7));
	 */

	private JCalendarBox cb;
	private JTextField tx;
	private JButton bt;
	private String value;
	private Vector actionListeners;

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public JCalendar() {
		super(new BorderLayout());

		cb = new JCalendarBox(this);

		tx = new JTextField();
		tx.addFocusListener(
			new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					setDate(tx.getText());
				}
			}
		);

		bt = new JButton(CellsUtil.getImage("JCalendar"));
		bt.setPreferredSize(new Dimension(20, 18));
		bt.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					open();
				}
			}
		);

		setBorder(tx.getBorder());
		tx.setBorder(null);

		add(tx, BorderLayout.CENTER);
		add(bt, BorderLayout.EAST);

		Dimension d = new Dimension(90, 22);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		actionListeners = new Vector();

		addFocusListener(
			new FocusListener() {
				public void focusGained(FocusEvent e) {
					tx.requestFocus();
				}

				public void focusLost(FocusEvent e) {
				}
			}
		);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setDate(GregorianCalendar v) {
		if (cb.setDate(v) != null) {
			value = cb.getDateString();
			tx.setText(value);
		} else {
			value = null;
			tx.setText("");
			cb.setDate(new GregorianCalendar());
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setDate(String v) {
		if (cb.setDate(v) != null) {
			value = v;
			tx.setText(value);
		} else {
			value = null;
			tx.setText("");
			cb.setDate(new GregorianCalendar());
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public GregorianCalendar getDate() {
		return value == null ? null : cb.getDate();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public String getDateString() {
		return value == null ? "" : value;
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public int getDay() {
		return cb.getDay();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public String getDayString() {
		return cb.getDayString();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public int getMonth() {
		return cb.getMonth();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public String getMonthString() {
		return cb.getMonthString();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected String getMonthName() {
		return cb.getMonthName();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public int getYear() {
		return cb.getYear();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public String getYearString() {
		return cb.getYearString();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setEditable(boolean b) {
		tx.setEditable(b);
		bt.setEnabled(b);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean isEditable() {
		return tx.isEditable();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setEnabled(boolean b) {
		tx.setEnabled(b);
		bt.setEnabled(b);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean isEnabled() {
		return tx.isEnabled();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setVisible(boolean b) {
		tx.setVisible(b);
		bt.setVisible(b);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean isVisible() {
		return tx.isVisible();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	void open() {
		cb.setDate(value);
		cb.open(0, getHeight());
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	void close() {
		value = cb.getDateString();
		tx.setText(value);

		// alle Listeners benachrichtigen
		for (int i = 0; i < actionListeners.size(); i++) {
			ActionListener al = (ActionListener) actionListeners.elementAt(i);
			al.actionPerformed(new ActionEvent(this, 0, null));
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void addActionListener(ActionListener al) {
		actionListeners.add(al);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void removeActionListener(ActionListener al) {
		actionListeners.remove(al);
	}

	/**
	 * Ein kleines Testprogramm
	 *
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}

		JFrame frame = new JFrame("JCalendar Example");
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);

		frame.getContentPane().setLayout(null);
		JCalendar b = new JCalendar();
		b.setDate("31.12.1967");
		b.setBounds(40, 40, 90, 22);
		frame.getContentPane().add(b);
		frame.pack();
		frame.setSize(300, 200);
		frame.setVisible(true);
	}
}


/**
 * CalendarBox
 */

class JCalendarBox extends JPopupMenu {

	private GregorianCalendar date;
	private JCalendarTable days;
	private JMonSpinn months;
	private JDigSpinn years;
	private JButton today = new JButton(CellsUtil.getImage("JCalendar"));
	private JCalendar parent;
	private boolean initial = false;

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	JCalendarBox(JCalendar parent) {
		super();
		this.parent = parent;

		months = new JMonSpinn();
		months.setPreferredSize(new Dimension(80, 20));
		months.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					updateDate();
				}
			}
		);

		years = new JDigSpinn(1900, 9999);
		years.setPreferredSize(new Dimension(60, 20));
		years.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					updateDate();
				}
			}
		);

		today.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					setDate(new GregorianCalendar());
				}
			}
		);
		today.setPreferredSize(new Dimension(20, 20));
		today.setFocusable(false);
		today.setBorder(new JLightButtonBorder(1));

		days = new JCalendarTable();

		days.addKeyListener(new CalendarKeyListener());
		days.addMouseListener(new CalendarMouseListener());

		JPanel buttonPanel1 = new JPanel(new BorderLayout(1, 1));
		buttonPanel1.add(months, BorderLayout.CENTER);
		buttonPanel1.add(years, BorderLayout.EAST);

		JPanel buttonPanel2 = new JPanel(new BorderLayout(1, 1));
		buttonPanel2.add(buttonPanel1, BorderLayout.CENTER);
		buttonPanel2.add(today, BorderLayout.EAST);

                Dimension d1 = buttonPanel2.getPreferredSize();
                Dimension d2 = days.getPreferredSize();
		setPreferredSize(new Dimension(d1.width + 12, d1.height + d2.height + 27));//178, 173));

		JPanel p = new JPanel(new BorderLayout(1, 1));
		p.add(buttonPanel2, BorderLayout.NORTH);
		p.add(new JScrollPane(days), BorderLayout.CENTER);
		add(p);

		setDate(new GregorianCalendar());
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected void open(int x, int y) {
		show(parent, x, y);
		transferFocus();
		days.requestFocusInWindow();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected void close() {
		setVisible(false);
		if (!getDayString().equals("")) {
			parent.close();
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected GregorianCalendar getDate() {
		return date;
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected String getDateString() {
		return getDayString() + "." + getMonthString() + "." + getYearString();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected int getDay() {
		int col = days.getSelectedColumn();
		int row = days.getSelectedRow();
		return Integer.parseInt((String) days.getValueAt(row, col));
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected String getDayString() {
		int col = days.getSelectedColumn();
		int row = days.getSelectedRow();
		String d = (String) days.getValueAt(row, col);
		return d.length() == 0 ? "" : (d.length() < 2 ? ("0" + d) : d);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected int getMonth() {
		return months.getSelectedIndex();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected String getMonthString() {
		String m = "" + (months.getSelectedIndex() + 1);
		return m.length() < 2 ? ("0" + m) : m;
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected String getMonthName() {
		return (String) months.getSelectedItem();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected int getYear() {
		return years.getValue();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected String getYearString() {
		return "" + years.getValue();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected String setDate(String v) {
		try {
			if (v == null) {
				return null;
			}
			if (v.length() != 10) {
				return null;
			}
			if (v.charAt(2) != '.' && v.charAt(5) != '.') {
				return null;
			}

			int d = Integer.parseInt(v.substring(0, 2));
			int m = Integer.parseInt(v.substring(3, 5));
			int y = Integer.parseInt(v.substring(6, 10));

			return setDate(new GregorianCalendar(y, m - 1, d));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected String setDate(GregorianCalendar gc) {
		int y = gc.get(Calendar.YEAR);
		int m = gc.get(Calendar.MONTH);
		int d = gc.get(Calendar.DAY_OF_MONTH);

		date = new GregorianCalendar(y, m, d);

		initial = true;
		years.setValue(y);
		months.setSelectedIndex(m);
		initial = false;

		updateDate();

		return d + "." + m + "." + y;
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	private void updateDate() {
		if (initial) {
			return;
		}

		int d = date.get(Calendar.DAY_OF_MONTH);
		int m = months.getSelectedIndex();
		int y = years.getValue();

		days.update(d, m, y);

		repaint();
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	private void change(int n) {
		date.add(Calendar.DAY_OF_MONTH, n);
		setDate(date);
	}

	//##########################################################
	// Inner Classes
	//##########################################################

	class CalendarKeyListener implements KeyListener {

		public void keyPressed(KeyEvent e) {

			int kc = e.getKeyCode();

			e.consume();

			switch (kc) {

				case KeyEvent.VK_LEFT :
					change(-1);
					break;

				case KeyEvent.VK_RIGHT :
					change(+1);
					break;

				case KeyEvent.VK_UP :
					change(-7);
					break;

				case KeyEvent.VK_DOWN :
					change(+7);
					break;

				case KeyEvent.VK_ENTER :
					close();
					break;

				case KeyEvent.VK_ESCAPE :
					close();
					break;

				default :
					break;
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
	}

	class CalendarMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && !getDayString().equals("")) {
				close();
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (!getDayString().equals("")) {
				close();
			}
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}
}


/**
 * JCalendarTable
 */

class JCalendarTable extends JTable {

	private final String[] days = { "Mo", "Di", "Mi", "Do", "Fr", "Sa", "So" };

	private final Object[][] data =
		{
			{ "", "", "", "", "", "", "" },
			{ "", "", "", "", "", "", "" },
			{ "", "", "", "", "", "", "" },
			{ "", "", "", "", "", "", "" },
			{ "", "", "", "", "", "", "" },
			{ "", "", "", "", "", "", "" }
		};

	/**
	 * @param
	 *
	 * @see
	 */
	JCalendarTable() {

		setModel(new JCalendarTableModel());

		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(false);
		setCellSelectionEnabled(true);
		setShowGrid(true);
		setShowHorizontalLines(true);
		setShowVerticalLines(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setRowHeight(getRowHeight() + 4);
		getTableHeader().setResizingAllowed(false);
		getTableHeader().setReorderingAllowed(false);

		HeaderRenderer hr = new HeaderRenderer();
		SaturdaySundayRenderer cr1 = new SaturdaySundayRenderer();
		EverydayRenderer cr2 = new EverydayRenderer();

		for (int col = 0; col < getColumnCount(); col++) {

			TableColumn column = getColumn(days[col]);
			column.setHeaderRenderer(hr);
            column.setPreferredWidth(22);

			if (col < getColumnCount() - 2) {
				column.setCellRenderer(cr1);
			} else {
				column.setCellRenderer(cr2);
			}
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	protected void update(int d, int m, int y) {

		for (int row = 0; row < getRowCount(); row++) {
			for (int col = 0; col < getColumnCount(); col++) {
				data[row][col] = "";
			}
		}

		GregorianCalendar gc = new GregorianCalendar(y, m, 1);
		int[] fisrDayColumnNumber = { 6, 0, 1, 2, 3, 4, 5 };
		int firstDay = fisrDayColumnNumber[gc.get(Calendar.DAY_OF_WEEK) - 1];

		int dayCount = 1;
		m++;

		for (int row = 0; row < getRowCount(); row++) {

			for (int col = firstDay; col < getColumnCount(); col++) {

				setValueAt(new Integer(dayCount).toString(), row, col);

				if (dayCount == d) {
					setRowSelectionInterval(row, row);
					setColumnSelectionInterval(col, col);
				}

				if (dayCount >= 31) {
					return;
				}
				if ((dayCount >= 30) && ((m == 4) || (m == 6) || (m == 9) || (m == 11))) {
					return;
				}
				if ((dayCount >= 29) && (m == 2)) {
					return;
				}
				if ((dayCount >= 28) && (m == 2) && (!gc.isLeapYear(y))) {
					return;
				}

				dayCount++;
			}
			firstDay = 0;
		}
	}

	class JCalendarTableModel extends AbstractTableModel {
		public int getColumnCount() {
			return days.length;
		}

		public int getRowCount() {
			return 6;
		}

		public String getColumnName(int col) {
			return days[col];
		}

		public Class getColumnClass(int col) {
			return String.class;
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
		}
	}

	class SaturdaySundayRenderer extends DefaultTableCellRenderer {
		private Color bc = new Color(254, 253, 224);

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {

			setValue(value);
			setHorizontalAlignment(CENTER);
			setFont(getFont().deriveFont(12f));
			setFont(getFont().deriveFont(Font.ITALIC));

			if (((String) value).length() == 0) {
				setForeground(Color.black);
				setBackground(Color.white);
			} else if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(Color.black);
				setBackground(bc);
			}
			return this;
		}
	}

	class EverydayRenderer extends DefaultTableCellRenderer {
		private Color bc = new Color(223, 255, 223);

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {

			setValue(value);
			setHorizontalAlignment(CENTER);
			setFont(getFont().deriveFont(12f));
			setFont(getFont().deriveFont(Font.ITALIC));

			if (((String) value).length() == 0) {
				setForeground(Color.black);
				setBackground(Color.white);
			} else if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(Color.black);
				setBackground(bc);
			}
			return this;
		}
	}

	class HeaderRenderer extends DefaultTableCellRenderer {

		private JLabel label = null;
		private Border border = new JLightButtonBorder(1);

		public HeaderRenderer() {
			super();

			label = new JLabel();
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setBorder(border);
			label.setFont(new Font("Dialog", 0, 12));
			label.setBackground(new Color(239, 210, 143));
			label.setPreferredSize(new Dimension(80, 22));
			label.setOpaque(true);
		}

		public Component getTableCellRendererComponent(
			JTable  table,
			Object  value,
			boolean isSelected,
			boolean hasFocus,
			int		row,
			int		column
		) {
			label.setText(value != null ? value.toString() : "");
			return label;
		}
	}
}


/**
 * Eine neue Swing-Komponente, die eine beliebigen Integer-Wert inkrementiert oder dekrementiret.
 */
class JDigSpinn extends JPanel {
	private static int m1 = 255 << 16;
	private static int m2 = 255 << 24;

	private static int[] pix1 = { m1, m1, m1, m2, m1, m1, m1, m1, m1, m2, m2, m2, m1, m1 };

	private static int[] pix2 = { m1, m1, m2, m2, m2, m1, m1, m1, m1, m1, m2, m1, m1, m1 };

	private static Image img1 =
		java.awt.Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(7, 2, pix1, 0, 7));

	private static Image img2 =
		java.awt.Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(7, 2, pix2, 0, 7));

	private JButton but1;
	private JButton but2;
	private JTextField txf;

	protected ActionListener action;
	protected ActionListener actionListener;

	private int m_start, m_end, val;

	private Vector listeners = new Vector();

	/**
	 * @param
	 *
	 * @see
	 */
	JDigSpinn(int start, int end) {
		super();
		m_start = start;
		m_end = end;
		setLayout(new BorderLayout(1, 1));

		but1 = new JButton(new ImageIcon(img1));
		but1.setHorizontalAlignment(0);
		but1.setVerticalAlignment(0);
		but1.setPreferredSize(new Dimension(20, 10));
		but1.setFocusable(false);
		but1.setBorder(new JLightButtonBorder(1));
		but1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					setText(1);
				}
			}
		);

		but2 = new JButton(new ImageIcon(img2));
		but2.setHorizontalAlignment(0);
		but2.setVerticalAlignment(0);
		but2.setPreferredSize(new Dimension(20, 10));
		but2.setFocusable(false);
		but2.setBorder(new JLightButtonBorder(1));
		but2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					setText(2);
				}
			}
		);

		JPanel pan = new JPanel(new BorderLayout());
		pan.add(but1, BorderLayout.NORTH);
		pan.add(but2, BorderLayout.SOUTH);

		txf = new JTextField();
		txf.setEditable(true);
		txf.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txf.setFocusable(false);
		txf.setForeground(Color.black);
		txf.setBackground(new Color(240, 250, 255));
		txf.setHorizontalAlignment(JTextField.CENTER);
		txf.setBorder(new JLightButtonBorder(0));
		add(txf, BorderLayout.CENTER);
		add(pan, BorderLayout.EAST);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setValue(int v) {
		if (v >= m_start && v <= m_end) {
			val = v;
			setText(0);
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public int getValue() {
		return val;
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void addActionListener(ActionListener al) {
		if (!listeners.contains(al)) {
			listeners.add(al);
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void removeActionListener(ActionListener al) {
		if (listeners.contains(al)) {
			listeners.remove(al);
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	private void setText(int mode) {
		switch (mode) {

			case 0 :
				break;

			case 1 :
				if (val < m_end) {
					val++;
				}
				break;

			case 2 :
				if (val > m_start) {
					val--;
				}
				break;
		}
		txf.setText((new Integer(val)).toString());
		txf.repaint();

		for (int i = 0; i < listeners.size(); i++) {
			ActionListener al = (ActionListener) listeners.elementAt(i);
			al.actionPerformed(new ActionEvent(this, 0, null));
		}
	}
}


/**
 * Eine neue Swing-Komponente, die eine beliebigen Integer-Wert inkrementiert oder dekrementiret.
 */
class JMonSpinn extends JPanel {
	private static final String[] months =
		{
			"Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September",
			"Oktober", "November", "Dezember"
		};

	private static int m1 = 255 << 16;
	private static int m2 = 255 << 24;

	private static int[] pix1 = { m1, m1, m1, m2, m1, m1, m1, m1, m1, m2, m2, m2, m1, m1 };

	private static int[] pix2 = { m1, m1, m2, m2, m2, m1, m1, m1, m1, m1, m2, m1, m1, m1 };

	private static Image img1 =
		java.awt.Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(7, 2, pix1, 0, 7));

	private static Image img2 =
		java.awt.Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(7, 2, pix2, 0, 7));

	private JButton but1;
	private JButton but2;
	private JTextField txf;

	private int val;

	private Vector listeners = new Vector();

	/**
	 * @param
	 *
	 * @see
	 */
	JMonSpinn() {
		super();
		setLayout(new BorderLayout(1, 1));

		but1 = new JButton(new ImageIcon(img1));
		but1.setHorizontalAlignment(0);
		but1.setVerticalAlignment(0);
		but1.setPreferredSize(new Dimension(20, 10));
		but1.setFocusable(false);
		but1.setBorder(new JLightButtonBorder(1));
		but1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					setText(1);
				}
			}
		);

		but2 = new JButton(new ImageIcon(img2));
		but2.setHorizontalAlignment(0);
		but2.setVerticalAlignment(0);
		but2.setPreferredSize(new Dimension(20, 10));
		but2.setFocusable(false);
		but2.setBorder(new JLightButtonBorder(1));
		but2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent ev) {
					setText(2);
				}
			}
		);

		JPanel pan = new JPanel(new BorderLayout());
		pan.add(but1, BorderLayout.NORTH);
		pan.add(but2, BorderLayout.SOUTH);

		txf = new JTextField();
		txf.setEditable(false);
		txf.setFont(new Font("SansSerif", Font.PLAIN, 12));
		txf.setFocusable(false);
		txf.setForeground(Color.black);
		txf.setBackground(new Color(240, 250, 255));
		txf.setHorizontalAlignment(JTextField.CENTER);
		txf.setBorder(new JLightButtonBorder(0));
		add(txf, BorderLayout.CENTER);
		add(pan, BorderLayout.EAST);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setSelectedIndex(int v) {
		v++;
		if (v < 1 || v > 12) {
			return;
		}
		val = v;
		setText(0);
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void setSelectedItem(String v) {
		for (int i = 0; i < months.length; i++) {
			if (v.equals(months[i])) {
				val = i + 1;
				setText(0);
				break;
			}
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public int getSelectedIndex() {
		return val - 1;
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public String getSelectedItem() {
		return months[val - 1];
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void addActionListener(ActionListener al) {
		if (!listeners.contains(al)) {
			listeners.add(al);
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	public void removeActionListener(ActionListener al) {
		if (listeners.contains(al)) {
			listeners.remove(al);
		}
	}

	/**
	 * @param
	 *
	 * @return
	 *
	 * @see
	 */
	private void setText(int mode) {
		switch (mode) {

			case 0 :
				break;

			case 1 :
				if (val < 12) {
					val++;
				} else {
					val = 1;
				}
				break;

			case 2 :
				if (val > 1) {
					val--;
				} else {
					val = 12;
				}
				break;
		}
		txf.setText(months[val - 1]);
		txf.repaint();

		for (int i = 0; i < listeners.size(); i++) {
			ActionListener al = (ActionListener) listeners.elementAt(i);
			al.actionPerformed(new ActionEvent(this, 0, null));
		}
	}
}


class JLightButtonBorder extends AbstractBorder implements UIResource {
	private UIDefaults table = UIManager.getLookAndFeelDefaults();
	private Color topleft = table.getColor("controlShadow");
	private Color bottomright = table.getColor("controlLtHighlight");
	private int dir = 0;

	public JLightButtonBorder(int dir) {
		this.dir = dir;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
		Color oldColor = g.getColor();
		g.translate(x, y);

		g.setColor(dir == 1 ? bottomright : topleft);
		g.drawLine(0, 0, w - 1, 0);
		g.drawLine(0, 1, 0, h - 1);

		g.setColor(dir == 1 ? topleft : bottomright);
		g.drawLine(w - 1, 0, w - 1, h - 1);
		g.drawLine(0, h - 1, w - 1, h - 1);

		g.translate(-x, -y);
		g.setColor(oldColor);
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(2, 3, 2, 3);
	}
}
