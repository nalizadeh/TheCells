/*
 * Copyright 2007 N.A.J. nalizadeh.org - All rights reserved. nalizadeh.org
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */

package org.nalizadeh.designer.util.otable.test;

import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

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
public class JOriginalTableTest1 {
	private final ImageIcon iconLevel1 = JOriginalTable.getImage("level1.gif");
	private final ImageIcon iconLevel2 = JOriginalTable.getImage("level2.gif");
	private final ImageIcon iconLevel3 = JOriginalTable.getImage("level3.gif");

	private static final String[] columnNames = { "Attribute", "Value", "Time" };
	private static final Object[][] data =
		{
			{ "Cell", "", "" },
			{ "Cell", "", "A", "cell" },
			{ "Cell", "", "B", "rowOverlapping" },
			{ "Cell", "", "C", "colOverlapping" },
			{ "Swing", "", "" },
			{ "Swing", "", "1", "background" },
			{ "Swing", "", "2", "background", "red" },
			{ "Swing", "", "3", "background", "red", "Hello" },
			{ "Swing", "", "4", "background", "green" },
			{ "Swing", "", "5", "background", "blue" },
			{ "Swing", "", "6", "border" },
		};

	private static final int k1 = 0;
	private static final int k2 = 3;
	private static final int k3 = 4;
	private static final int k4 = 5;

	JOriginalTable.JOriginalTableNodeAdapter myNodeListener =
		new JOriginalTable.JOriginalTableNodeAdapter() {

			public Object createKey(Object[] data, int aggrigationLevel, int level) {
				if (aggrigationLevel == 1) {
					switch (level) {

						case 1 :
							return data.length > k1 ? data[k1] : null;

						case 2 :
							return data.length > k2 ? data[k2] : null;

						case 3 :
							return data.length > k3 ? data[k3] : null;

						case 4 :
							return data.length > k4 ? data[k4] : null;

						default :
							break;
					}

				} else if (aggrigationLevel == 2) {
					switch (level) {

						case 1 :
							return data.length > k2 ? data[k1] + " / " + data[k2] : null;

						case 2 :
							return data.length > k3 ? data[k3] : null;

						case 3 :
							return data.length > k4 ? data[k4] : null;

						default :
							break;
					}

				} else if (aggrigationLevel == 3) {
					switch (level) {

						case 1 :
							return data.length > k3 ? data[k1] + "/" + data[k2] + "/" + data[k3]
							: null;

						case 2 :
							return data.length > k4 ? data[k4] : null;

						default :
							break;
					}

				}
				return null;
			}

			public ImageIcon getIcon(JOriginalTable.JOriginalTableNode node) {
				int level = node.getLevel();
				int aggrigationLevel = node.getAggrigationLevel();

				if (aggrigationLevel == 1) {
					switch (level) {

						case 1 :
							return iconLevel1;

						case 2 :
							return iconLevel2;

						case 3 :
							return iconLevel3;

						default :
							break;
					}

				} else if (aggrigationLevel == 2) {
					switch (level) {

						case 1 :
							return iconLevel2;

						case 2 :
							return iconLevel3;

						default :
							break;
					}

				} else if (aggrigationLevel == 3) {
					switch (level) {

						case 1 :
							return iconLevel3;

						default :
							break;
					}
				}
				return null;

			}
		};

	private JOriginalTable table = null;

	public JOriginalTable makeTreeTable() {

//      TreeTable.TreeTableNode node = table.addNode("Cell", new Object[] { "", "", "" }, 1);
//
//      node.addNode("cell", new Object[] { "", "", "" }, 1);
//      node.addNode("rowOverlapping", new Object[] { "", "", "" }, 1);
//      node.addNode("colOverlapping", new Object[] { "", "", "" }, 1);
//
//      node = table.addNode("Swing", new Object[] { "", "", "" }, 1);
//      node.addNode("background", new Object[] { "", "", "" }, 1);
//
//      TreeTable.TreeTableNode n1 =
//          node.addNode("foreground", new Object[] { "", "", "" }, 1);
//      n1.addNode("red", new Object[] { "", "", "" }, 1);
//      n1.addNode("green", new Object[] { "", "", "" }, 1);
//      n1.addNode("blue", new Object[] { "", "", "" }, 1);
//
//      node.addNode("border", new Object[] { "", "", "" }, 1);
//
//      table.init();

		table = new JOriginalTable(columnNames, myNodeListener, true);
		table.init(data, 1);
		table.combineCells(new int[] { 2, 3, 4 }, new int[] { 1 });
		table.combineCells(new int[] { 7, 8 }, new int[] { 2 });
		return table;
	}

	void setLevel(int lv) {
		table.init(data, lv);
	}

	public static JPanel asPanel() {
		final JOriginalTableTest1 test = new JOriginalTableTest1();

		JOriginalTable treetable = test.makeTreeTable();

		treetable.setAutoResizeMode(JOriginalTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
		treetable.setRowHeight(20);

		JScrollPane sp = new JScrollPane();
		sp.getViewport().add(treetable);

		JButton b1 = new JButton("Level1");
		JButton b2 = new JButton("Level2");
		JButton b3 = new JButton("Level3");
		b1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					test.setLevel(1);
				}
			}
		);
		b2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					test.setLevel(2);
				}
			}
		);
		b3.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					test.setLevel(3);
				}
			}
		);

		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(b1);
		p.add(b2);
		p.add(b3);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(sp, BorderLayout.CENTER);
		panel.add(p, BorderLayout.SOUTH);
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

		frame.pack();
		frame.setSize(400, 300);
		frame.setVisible(true);
	}
}
