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
public class JOriginalTableTest2 {

	private final ImageIcon iconLevel1 = JOriginalTable.getImage("level1.gif");
	private final ImageIcon iconLevel2 = JOriginalTable.getImage("level2.gif");
	private final ImageIcon iconLevel3 = JOriginalTable.getImage("level3.gif");

	private static final String[] columnNames = { "A", "B", "C", "D", "E", "F" };
	private static final Object[][] data =
		{
			{ "A1", "B1", "C1", "D1", "E1", "F1", "000", "0001" },
			{ "A2", "B2", "C2", "D2", "E2", "F2", "000", "0001" },
			{ "A3", "B3", "C3", "D3", "E3", "F3", "000", "0001" },
			{ "A4", "B4", "C4", "D4", "E4", "F4", "000", "0001" },
			{ "A1", "B4", "C4", "D4", "E4", "F4", "000", "0002" },
			{ "A3", "B4", "C4", "D4", "E4", "F4", "001", "0001" },
			{ "A3", "B4", "C4", "D4", "E4", "F4", "002", "0001" },
			{ "A3", "B4", "C4", "D4", "E4", "F4", "002", "0002" },
		};

	private static final int k1 = 0;
	private static final int k2 = 6;
	private static final int k3 = 7;
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
		table = new JOriginalTable(columnNames, myNodeListener);
		table.init(data, 1);
		return table;
	}

	void setLevel(int lv) {
		table.init(data, lv);
	}

	public static JPanel asPanel() {
		final JOriginalTableTest2 test = new JOriginalTableTest2();

		JOriginalTable treetable = test.makeTreeTable();
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
		frame.setSize(400, 200);
		frame.setVisible(true);
	}
}
