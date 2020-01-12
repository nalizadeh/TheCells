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

package org.nalizadeh.designer;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * <p>Title:</p>
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
public class DebugPanel extends JPanel {

	private static final boolean DEBUG = true;

	private static final int BUFFER_SIZE = 1000;
	private static final ImageIcon icDesView = CellsUtil.getImage("Preview");

	private static MyListModel msgModel;
	private static JList<String> msgList;
	private static JScrollPane msgScroll;
	private static Logger logger;

	private CellsDesigner designer;
	
	public DebugPanel(CellsDesigner designer) {
		this.designer = designer;
		
		initLogger();

		JToolBar runToolbar = new JToolBar(JToolBar.HORIZONTAL);
		runToolbar.setBorder(null);
		runToolbar.setFloatable(false);
		runToolbar.setRollover(true);
		runToolbar.add(designer.getActionManager().getAction("RunTest"));
		runToolbar.add(designer.getActionManager().getAction("StopTest"));
		runToolbar.add(designer.getActionManager().getAction("ClearConsole"));

		msgModel = new MyListModel();
		msgList = new JList<String>(msgModel);
		msgList.setCellRenderer(new MyCellRenderer());

		msgScroll = new JScrollPane(msgList);
		msgScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		msgScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		msgScroll.setBorder(BorderFactory.createEtchedBorder());

		JPanel runPanel =
			new JPanel(
				new CellsLayout(new double[][] {
						{ 40, CellsLayout.FILL },
						{ CellsLayout.FILL }
					}
				)
			);

		runPanel.add(runToolbar, "0,0");
		runPanel.add(msgScroll, "1,0");

		JTabbedPane tp = new JTabbedPane();
		tp.add("Run/Test", runPanel);
		tp.setIconAt(0, icDesView);
		tp.setTabPlacement(JTabbedPane.BOTTOM);

		setLayout(new CellsLayout(new double[][] {
					{ CellsLayout.FILL },
					{ CellsLayout.FILL }
				}
			)
		);

		add(tp, "0,0");
	}

	public static void clear() {
		msgModel.clear();
		msgList.repaint();
	}

	public static boolean isEmpty() {
		return msgModel.getSize() == 0;
	}

	public static void printStackTrace(Exception ex) {
		appendWarning(">>>> an exception occurred:");
		if (DEBUG) {
			ex.printStackTrace();
		} else {
			appendError(ex.getMessage());
		}
	}

	public static void appendMessage(String msg) {
		if (msg != null && msg.length() != 0) {
			append("#I" + msg);
		}
	}

	public static void appendWarning(String msg) {
		if (msg != null && msg.length() != 0) {
			append("#W" + msg);
		}
	}

	public static void appendError(String msg) {
		if (msg != null && msg.length() != 0 && !msg.equals(CellsUtil.LINE_SEPARATOR)) {
			append("#E" + msg);
			if (logger != null) {
				logger.log(Level.WARNING, msg);
			}
		}
	}

	private static synchronized void append(final String msg) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					msgModel.append(msg);
					int p = Math.max(msgModel.getSize() - 1, 0);
					msgList.scrollRectToVisible(msgList.getCellBounds(p, p));
				}
			}
		);
	}

	class MyCellRenderer implements ListCellRenderer {

		public Component getListCellRendererComponent(
			JList   list,
			Object  value,
			int		index,
			boolean isSelected,
			boolean cellHasFocus
		) {
			JLabel lb = new JLabel();
			if (value != null) {
				String st = (String) value;
				boolean warning = st.startsWith("#W");
				boolean error = st.startsWith("#E");
				lb.setText(st.substring(2));
				lb.setOpaque(true);
				lb.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
				lb.setForeground(
					isSelected ? list.getSelectionForeground()
					: error ? Color.RED : warning ? Color.BLUE : getForeground()
				);
			}
			return lb;
		}
	}

	private static class MyListModel implements ListModel<String> {
		List<String> data = new ArrayList<String>();
		List<ListDataListener> listeners = new ArrayList<ListDataListener>();

		public void append(String msg) {
			if (data.size() > BUFFER_SIZE) {
				data.remove(0);
			}
			data.add(msg);
			fireListDataChanged();
		}

		private void clear() {
			data.clear();
		}

		public int getSize() {
			return data.size();
		}

		public String getElementAt(int index) {
			return data.get(index);
		}

		public void addListDataListener(ListDataListener l) {
			listeners.add(l);
		}

		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}

		private void fireListDataChanged() {
			ListDataEvent ev =
				new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, data.size() - 1);
			for (ListDataListener l : listeners) {
				l.contentsChanged(ev);
			}
		}
	}
	
	private void initLogger() {
		try {
			logger = Logger.getLogger( "TheCells-Logging" );
			Handler ch = new ConsoleHandler();
			ch.setLevel(Level.WARNING);
			Handler fh = new FileHandler(CellsUtil.LOG_FILE, 512*1024, 1);
			fh.setLevel(Level.ALL);
			logger.addHandler(ch);
			logger.addHandler(fh);
		
		} catch(IOException ex) {
			logger = null;
		}
	}
}
