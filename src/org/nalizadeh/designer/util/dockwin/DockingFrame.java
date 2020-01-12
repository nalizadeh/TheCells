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
package org.nalizadeh.designer.util.dockwin;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
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
public class DockingFrame extends JFrame {

	private DockingPort root;

	public DockingFrame(String title) {
		super(title);
		setLayout(new BorderLayout(0, 0));
		initDockingPorts();
		add(root, BorderLayout.CENTER);
	}

	protected void initDockingPorts() {
		final DockingWindow east =
			new DockingWindow("east", DockingPort.getImage("title.gif"), null, false);
		final DockingWindow west =
			new DockingWindow("west", DockingPort.getImage("title.gif"), null, false);
		final DockingWindow west2 =
			new DockingWindow("west2", DockingPort.getImage("title.gif"), null, false);
		final DockingWindow north =
			new DockingWindow("north", DockingPort.getImage("title.gif"), null, false);
		final DockingWindow south =
			new DockingWindow("south", DockingPort.getImage("title.gif"), null, false);
		final DockingWindow south2 =
			new DockingWindow("south2", DockingPort.getImage("title.gif"), null, false);
		final DockingWindow center =
			new DockingWindow("center", DockingPort.getImage("title.gif"), null, false);

		root =
			new DockingPort() {
				public void initPorts() {

					DockingPort dp = addDockingWindow(west, BorderLayout.WEST);
					addDockingWindow(west2, BorderLayout.WEST);
					dp.addDockingPort(south2, BorderLayout.SOUTH);
					addDockingWindow(south, BorderLayout.SOUTH);
					addDockingWindow(north, BorderLayout.NORTH);
					addDockingWindow(east, BorderLayout.EAST);
					addDockingWindow(center, BorderLayout.CENTER);
				}
			};
	}

	public JPopupMenu getDockingPopup() {
		return root.getPopup();
	}

	public static void main(String[] args) {

		String osx = "apple.laf.AquaLookAndFeel";
		String metal = "javax.swing.plaf.metal.MetalLookAndFeel";
		String motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		String gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
		String sone = "original.lf.SoneLookAndFeel";
		String liquid = "com.birosoft.liquid.LiquidLookAndFeel";
		String kunststoff = "com.incors.plaf.kunststoff.KunststoffLookAndFeel";

		try {
			UIManager.setLookAndFeel(windows);
		} catch (Exception e) {
			e.printStackTrace();
		}

		DockingFrame dw = new DockingFrame("DockingFrame");
		dw.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);
		dw.setSize(600, 500);
		dw.setVisible(true);
	}
}
