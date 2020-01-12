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

import org.nalizadeh.designer.base.DesignerConstraints;
import org.nalizadeh.designer.base.DesignerFrame;
import org.nalizadeh.designer.base.DesignerListener;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import org.nalizadeh.designer.property.PropertySheet;

import org.nalizadeh.designer.util.CellsCopyrightDialog;
import org.nalizadeh.designer.util.CellsDialog;
import org.nalizadeh.designer.util.CellsExceptionWrapper;
import org.nalizadeh.designer.util.CellsOutlookBar;
import org.nalizadeh.designer.util.CellsPopupMenu;
import org.nalizadeh.designer.util.CellsSourceGenerator;
import org.nalizadeh.designer.util.CellsStatusBar;
import org.nalizadeh.designer.util.CellsUtil;
import org.nalizadeh.designer.util.ConfirmClosingDialog;
import org.nalizadeh.designer.util.dockwin.DockingPort;
import org.nalizadeh.designer.util.dockwin.DockingWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;
import java.util.List;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2008 N.A.J</p>
 *
 * <p>Organization:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class CellsDesigner extends JFrame implements DesignerListener, InternalFrameListener,
	VetoableChangeListener
{
	public static final String lf_windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	public static final String lf_osx = "apple.laf.AquaLookAndFeel";
	public static final String lf_metal = "javax.swing.plaf.metal.MetalLookAndFeel";
	public static final String lf_motif = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	public static final String lf_gtk = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
	public static final String lf_sone = "org.nalizadeh.designer.util.lf.SoneLookAndFeel";
	public static final String lf_liquid = "com.birosoft.liquid.LiquidLookAndFeel";
	public static final String lf_kunststoff = "com.incors.plaf.kunststoff.KunststoffLookAndFeel";
	public static final String lf_skin = "com.l2fprod.gui.plaf.skin.SkinLookAndFeel";
	public static final String lf_synth = "SynthLookAndFeel";
	public static final String lf_default = lf_windows;

	private String lookAndFeel;
	private JPanel contentPane = null;
	
	private ActionManager actions = null;
	private DesktopPanel desktop = null;
	private InspectorPanel inspector = null;
	private StructurPanel structur = null;
	private PropertySheet property = null;
	private DebugPanel debuger = null;

	private SwingsList swings1 = null;
	private SwingsList swings2 = null;
	private SwingsList beans = null;
	private CellsOutlookBar obar = null;

	private CellsSourceGenerator generator = null;
	private CellsPopupMenu popupMenu = null;
	private CellsStatusBar statusbar = null;

	private DockingPort root;
	private DockingWindow west1;
	private DockingWindow west2;
	private DockingWindow westSouth;
	private DockingWindow south;
	private DockingWindow center;
	private DockingWindow east;

	/**
	 * Default constructor
	 *
	 */
	public CellsDesigner() {
		
		setTitle("The Cells");
        setLookAndFeel(lf_default);
        
		CellsCopyrightDialog.SplashWindow sp = new CellsCopyrightDialog.SplashWindow(this, 8);
		sp.setVisible(true);
		sp.doWait(2000);

		sp.setText("Loading Actions...");
		actions = new ActionManager(this);

		sp.setText("Loading Desktop...");
		desktop = new DesktopPanel(this);

		sp.setText("Loading Inspector...");
		inspector = new InspectorPanel(this);

		sp.setText("Loading Structur...");
		structur = new StructurPanel(this);

		sp.setText("Loading Propreties...");
		property = new PropertySheet(this);

		sp.setText("Loading Debuger...");
		debuger = new DebugPanel(this);

		sp.setText("Loading Plugins...");
		sp.doWait(1000);

		sp.setText("Starting GUI...");
		obar = new CellsOutlookBar();

		swings1 = new SwingsList(this, 1);
		swings2 = new SwingsList(this, 2);
		beans = new SwingsList(this, 3);

		obar.addItem("Components", CellsUtil.getImage("Plugin"), swings1);
		obar.addItem("Containers", CellsUtil.getImage("Container"), swings2);
		obar.addItem("Beans", CellsUtil.getImage("Bean"), beans);
		obar.setEnabled(false);

		property.setPreferredSize(new Dimension(340, 380));
		debuger.setMinimumSize(new Dimension(300, 150));
		inspector.setMinimumSize(new Dimension(170, 300));

		west1 = new DockingWindow("Inspector", CellsUtil.getImage("Inspector"), inspector, false);
		west2 = new DockingWindow("Structur", CellsUtil.getImage("Structur"), structur, false);
		westSouth = new DockingWindow("Swings", CellsUtil.getImage("Swings"), obar, false);
		south = new DockingWindow("Console", CellsUtil.getImage("Console"), debuger, false);
		center = new DockingWindow("Designer", CellsUtil.getImage("Designer"), desktop, true);
		east = new DockingWindow("Propertysheet", CellsUtil.getImage("Property"), property, false);

		root = new DockingPort() {
				public void initPorts() {
					DockingPort dp = null;
					addDockingWindow(west1, BorderLayout.WEST);
					dp = addDockingWindow(west2, BorderLayout.WEST);
					dp.addDockingPort(westSouth, BorderLayout.SOUTH);
					addDockingWindow(south, BorderLayout.SOUTH);
					dp = addDockingWindow(center, BorderLayout.CENTER);
					dp.addDockingPort(east, BorderLayout.EAST);
				}
			};

		root.setPopupEnabled(false);
		center.select(true);
		
		generator = new CellsSourceGenerator(this);
		popupMenu = new CellsPopupMenu(this);
		statusbar = new CellsStatusBar(this);

		//===== ToolBar

		JToolBar toolbar = new JToolBar();
		toolbar.setBorder(null);
		toolbar.setFloatable(false);
		toolbar.setRollover(true);
		toolbar.setBorderPainted(false);

		toolbar.add(actions.getAction("New"));
		toolbar.add(actions.getAction("Open"));
		toolbar.add(actions.getAction("Save"));
		toolbar.add(actions.getAction("SaveAs"));
		toolbar.add(actions.getAction("SaveAll"));

		toolbar.addSeparator();
		toolbar.add(actions.getAction("Undo"));
		toolbar.add(actions.getAction("Redo"));
		toolbar.add(actions.getAction("Cut"));
		toolbar.add(actions.getAction("Copy"));
		toolbar.add(actions.getAction("Paste"));
		toolbar.add(actions.getAction("Delete"));

		toolbar.addSeparator();
		toolbar.add(actions.getAction("Cursor"));
		toolbar.add(actions.getAction("Marqueeing"));
		toolbar.add(actions.getAction("Grid"));

		toolbar.addSeparator();
		toolbar.add(actions.getAction("Designer"));
		toolbar.add(actions.getAction("Source"));
		toolbar.add(actions.getAction("Html"));
		toolbar.add(actions.getAction("Aspx"));
		toolbar.add(actions.getAction("HtmlPreview"));

		toolbar.add(actions.getAction("RunTest"));
		toolbar.add(actions.getAction("StopTest"));

		//===== Menu

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);

		menuItem = new JMenuItem(actions.getAction("New"));
		menu.add(menuItem);

		menuItem = new JMenuItem(actions.getAction("Open"));
		menu.add(menuItem);

		menuItem = new JMenuItem(actions.getAction("Save"));
		menu.add(menuItem);

		menuItem = new JMenuItem(actions.getAction("SaveAs"));
		menu.add(menuItem);

		menuItem = new JMenuItem(actions.getAction("SaveAll"));
		menu.add(menuItem);

        menu.addSeparator();

		menuItem = new JMenuItem(actions.getAction("Exit"));
		menu.add(menuItem);

		menu = new JMenu("Edit");
		menu.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menu);

		menuItem = new JMenuItem(actions.getAction("Undo"));
		menu.add(menuItem);

		menuItem = new JMenuItem(actions.getAction("Redo"));
		menu.add(menuItem);

		menu.addSeparator();

		menuItem = new JMenuItem(actions.getAction("Cut"));
		menu.add(menuItem);

		menuItem = new JMenuItem(actions.getAction("Copy"));
		menu.add(menuItem);

		menuItem = new JMenuItem(actions.getAction("Paste"));
		menu.add(menuItem);

		menuItem = new JMenuItem(actions.getAction("Delete"));
		menu.add(menuItem);

		JMenu submenu = new JMenu("Change Look & Feel");
		submenu.setMnemonic(KeyEvent.VK_L);
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem rb1 = new JRadioButtonMenuItem(actions.getAction("Windows"));
		JRadioButtonMenuItem rb2 = new JRadioButtonMenuItem(actions.getAction("Metal"));
		JRadioButtonMenuItem rb3 = new JRadioButtonMenuItem(actions.getAction("Motif"));
		JRadioButtonMenuItem rb4 = new JRadioButtonMenuItem(actions.getAction("Skin"));
		JRadioButtonMenuItem rb5 = new JRadioButtonMenuItem(actions.getAction("Kunststoff"));
		JRadioButtonMenuItem rb6 = new JRadioButtonMenuItem(actions.getAction("Liquid"));
		JRadioButtonMenuItem rb7 = new JRadioButtonMenuItem(actions.getAction("OSX (Aqua)"));
		JRadioButtonMenuItem rb8 = new JRadioButtonMenuItem(actions.getAction("GTK"));
		JRadioButtonMenuItem rb9 = new JRadioButtonMenuItem(actions.getAction("Sone (SAP)"));
		JRadioButtonMenuItem rb10 = new JRadioButtonMenuItem(actions.getAction("Synth"));
		rb1.setSelected(true);
		group.add(rb1);
		group.add(rb2);
		group.add(rb3);
		group.add(rb4);
		group.add(rb5);
		group.add(rb6);
		group.add(rb7);
		group.add(rb8);
		group.add(rb9);
		group.add(rb10);
		submenu.add(rb1);
		submenu.add(rb2);
		submenu.add(rb3);
		submenu.add(rb4);
		submenu.add(rb5);
		submenu.add(rb6);
		submenu.add(rb7);
		submenu.add(rb8);
		submenu.add(rb9);
		submenu.addSeparator();
		submenu.add(rb10);

		menu.addSeparator();
		menu.add(submenu);

		menuBar.add(root.getMenu());

		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(menu);
		menuItem = new JMenuItem(actions.getAction("About The Cells"));
		menu.add(menuItem);

		contentPane = new JPanel();
		contentPane.setLayout(
			new CellsLayout(new double[][] {
					{ 26, CellsLayout.FILL, 22 },
					{ CellsLayout.FILL }
				}
			)
		);

		contentPane.add(toolbar, "0,0");
		contentPane.add(root, "1,0");
		contentPane.add(statusbar, "2,0");

		enableActions();

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(contentPane, BorderLayout.CENTER);
		setIconImage(CellsUtil.getImage("Designer").getImage());

//      frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
//      frame.setUndecorated(true);
//      frame.setDefaultLookAndFeelDecorated(false);

		sp.doWait(1000);
		sp.setText("Welcome to The Cells");

		setSize(800, 600);
		setLocationRelativeTo(null);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);

		sp.doWait(2000);
		sp.dispose();

		CellsExceptionWrapper.start(debuger, false);

		statusbar.startMemoryReporting();
	}

	public String getVersion() {
		return "2.80";
	}

	public String getBuild() {
		return "N20090904-0280";
	}

	public String getURL() {
		return "http://www.nalizadeh.org/cells";
	}

	public ActionManager getActionManager() {
		return actions;
	}

	public DesktopPanel getDesktop() {
		return desktop;
	}

	public InspectorPanel getInspector() {
		return inspector;
	}

	public StructurPanel getStructur() {
		return structur;
	}

	public PropertySheet getProperty() {
		return property;
	}

	public DebugPanel getDebuger() {
		return debuger;
	}

	public CellsSourceGenerator getSourceGenerator() {
		return generator;
	}

	public CellsStatusBar getStatusBar() {
		return statusbar;
	}

	public CellsPopupMenu getPopupMenu() {
		return popupMenu;
	}

	public JPopupMenu getDockingPopup() {
		return root.getPopup();
	}

	public void selectionChanged(DesignerConstraints cons) {
		if (cons.getComponent() == null) {
			deselectAll();
		}
		update();
	}

	public void update() {
		desktop.getDesigner().update();
		actions.update();
		inspector.update();
		structur.update();
		property.update();
		redraw();
	}

	public void deselectAll() {
		desktop.deselectAll();
		inspector.update();
		structur.update();
		property.update();
		swings1.clear();
		swings2.clear();
		beans.clear();
		actions.update();
	}

	public void redraw() {
		desktop.redraw();
		inspector.revalidate();
		structur.revalidate();
		property.revalidate();
		obar.revalidate();
		swings1.revalidate();
		swings2.revalidate();
		beans.revalidate();
		contentPane.revalidate();
		repaint();
	}

	public void setVisible(boolean b) {
		if (b) {
			super.setVisible(b);

		} else if (showCloseDialog()) {

			statusbar.stopMemoryReporting();

			super.setVisible(b);
			dispose();
			System.exit(0);
		}
	}

	private boolean showCloseDialog() {
		List<DesignerFrame> files = new ArrayList<DesignerFrame>();
		JInternalFrame[] dfs = desktop.getAllFrames();
		for (int i = 0; i < dfs.length; i++) {
			DesignerFrame df = (DesignerFrame) dfs[i];
			if (!df.isSaved()) {
				files.add(df);
			}
		}
		if (files.size() > 0) {
			ConfirmClosingDialog dlg = new ConfirmClosingDialog(this, files);
			dlg.setVisible(true);
			if (dlg.isApproved()) {

				List<?> ls = dlg.getChecked();
				for (int i = 0; i < ls.size(); i++) {
					desktop.save((DesignerFrame) ls.get(i));
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public void internalFrameOpened(InternalFrameEvent e) {
	}

	public void internalFrameClosing(InternalFrameEvent e) {
		DesignerFrame df = (DesignerFrame) e.getInternalFrame();
		if (df.isModal()) {
			df.abort();

		} else if (!df.isBlocked() && !df.isSaved()) {

			int o =
				CellsDialog.showCompletConfirmDialog(
					this,
					"Save",
					"Save file ...",
					"File " + df.getTitle() + ".java is changed. Save it?",
					350,
					200
				);

			switch (o) {

				case CellsDialog.APPROVE_OPTION :
					desktop.setSelectedFrame(df);
					desktop.save();
					break;

				case CellsDialog.DISCARD_OPTION :
					df.abort();
					break;

				case CellsDialog.CANCEL_OPTION :
					df.cancel();
					break;
			}
		}
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		((DesignerFrame) e.getInternalFrame()).destroy();
		enableActions();
	}

	public void internalFrameIconified(InternalFrameEvent e) {
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		enableActions();
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
		enableActions();
	}

	public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
		DesignerFrame df = (DesignerFrame) evt.getSource();
		if (evt.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY)) {
			if (df.isCanceled()) {
				throw new PropertyVetoException("", evt);
			}
		}
	}

	private void enableActions() {
		boolean b = desktop.getAllFrames().length > 0 && desktop.getSelectedFrame() != null;

		if (!b) {
			obar.collapseAll();
			DebugPanel.clear();
		} else if (!obar.isEnabled()) {
			obar.expand("Components");
		}
		obar.setEnabled(b);

		deselectAll();
		redraw();
	}

        public void setLookAndFeel(String lookAndFeel) {
                this.lookAndFeel = lookAndFeel;
                CellsUtil.setLookAndFeel(lookAndFeel, this);
        }

        public String getLookAndFeel() {
                return lookAndFeel;
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
	public static void main(String[] args) {
		new CellsDesigner();
	}
}
