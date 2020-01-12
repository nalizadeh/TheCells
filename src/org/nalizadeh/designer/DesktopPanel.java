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

import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.borderlayout.BorderLayoutContainer;
import org.nalizadeh.designer.layouts.borderlayout.BorderLayoutDesigner;
import org.nalizadeh.designer.layouts.boxlayout.BoxLayoutContainer;
import org.nalizadeh.designer.layouts.boxlayout.BoxLayoutDesigner;
import org.nalizadeh.designer.layouts.cardlayout.CardLayoutContainer;
import org.nalizadeh.designer.layouts.cardlayout.CardLayoutDesigner;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayoutContainer;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayoutDesigner;
import org.nalizadeh.designer.layouts.flowlayout.FlowLayoutContainer;
import org.nalizadeh.designer.layouts.flowlayout.FlowLayoutDesigner;
import org.nalizadeh.designer.layouts.freelayout.FreeLayoutContainer;
import org.nalizadeh.designer.layouts.freelayout.FreeLayoutDesigner;
import org.nalizadeh.designer.layouts.gridbaglayout.GridBagLayoutContainer;
import org.nalizadeh.designer.layouts.gridbaglayout.GridBagLayoutDesigner;
import org.nalizadeh.designer.layouts.gridlayout.GridLayoutContainer;
import org.nalizadeh.designer.layouts.gridlayout.GridLayoutDesigner;

import org.nalizadeh.designer.util.CellsDialog;
import org.nalizadeh.designer.util.CellsLayoutChooser;
import org.nalizadeh.designer.util.CellsSourceCompiler;
import org.nalizadeh.designer.util.CellsSourceGenerator;
import org.nalizadeh.designer.util.CellsSwingWorker;
import org.nalizadeh.designer.util.CellsUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.JRootPane;

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
public class DesktopPanel extends JDesktopPane implements
		CellsSwingWorker.Invoker {

	private static final Color BKCOLOR = new Color(245, 255, 245);

	private CellsDesigner designer;
	private JFileChooser fc;

	public DesktopPanel(CellsDesigner designer) {
		this.designer = designer;

		setBackground(BKCOLOR);

		try {
			fc = new JFileChooser(CellsUtil.EXAM_HOME);
		} catch (Exception ex) {
			// we are running via Web-Start!
			fc = null;
		}
	}

	// new
	public DesignerFrame newDesignerFrame(String name, String packageName,
			int mode, String ln) {
		if (mode == 1) {
			DesignerFrame df = newDesignerFrame(CellsLayoutChooser.getLM()
					.getLayoutType(ln), name, name, null, null);
			df.setPackage(packageName);
			return df;
		}
		return null;
	}

	// sublayouting
	private DesignerFrame newDesignerFrame(Container container,
			JInternalFrame parent) {

		CellsLayoutChooser.Layouts lt = CellsLayoutChooser.getLM()
				.getLayoutType(container);
		if (container.getLayout() == null || lt == null) {
			CellsLayoutChooser lc = new CellsLayoutChooser(designer);
			lt = null;
			if (lc.isApproved()) {
				lc.getLM().setLayoutType(container);
				lt = lc.getLM().getLayoutType();
			}
		}
		if (lt != null) {

			return newDesignerFrame(lt, "layouting sub container ("
					+ container.getName() + ")", null, (Container) CellsUtil
					.clone(container, true, designer), parent);
		}
		return null;
	}

	// open
	private DesignerFrame newDesignerFrame(CellsLayoutChooser.Layouts layout,
			String title, String name, Container container,
			JInternalFrame parent) {
		
		DesignerFrame df = null;

		if (layout == CellsLayoutChooser.Layouts.FreeLayout) {
			df = new FreeLayoutDesigner(title, name, container, designer,
					parent);

		} else if (layout == CellsLayoutChooser.Layouts.BorderLayout) {
			df = new BorderLayoutDesigner(title, name, container, designer,
					parent);

		} else if (layout == CellsLayoutChooser.Layouts.CardLayout) {
			df = new CardLayoutDesigner(title, name, container, designer,
					parent);

		} else if (layout == CellsLayoutChooser.Layouts.FlowLayout) {
			df = new FlowLayoutDesigner(title, name, container, designer,
					parent);

		} else if (layout == CellsLayoutChooser.Layouts.BoxLayout) {
			df = new BoxLayoutDesigner(title, name, container, designer, parent);

		} else if (layout == CellsLayoutChooser.Layouts.GridLayout) {
			df = new GridLayoutDesigner(title, name, container, designer,
					parent);

		} else if (layout == CellsLayoutChooser.Layouts.GridBagLayout) {
			df = new GridBagLayoutDesigner(title, name, container, designer,
					parent);

		} else if (layout == CellsLayoutChooser.Layouts.CellsLayout) {
			df = new CellsLayoutDesigner(title, name, container, designer,
					parent);
		} else {
			return null;
		}

		df.addInternalFrameListener(designer);
		df.addVetoableChangeListener(designer);

		add(df);

		try {
			df.setSelected(true);

		} catch (PropertyVetoException ex) {
			DebugPanel.printStackTrace(ex);
			return null;
		}

		designer.getProperty().copyProperties(container, df.getContainer());
		designer.update();

		return df;
	}

	public DesignerContainer newDesignerContainer(DesignerFrame df,
			Container container, boolean isRoot) {

		DesignerContainer newContainer = null;
		CellsLayoutChooser.Layouts lt = CellsLayoutChooser.getLM()
				.getLayoutType(container);

		if (lt == CellsLayoutChooser.Layouts.FreeLayout) {
			newContainer = new FreeLayoutContainer(df, isRoot, container);

		} else if (lt == CellsLayoutChooser.Layouts.BorderLayout) {
			newContainer = new BorderLayoutContainer(df, isRoot, container);

		} else if (lt == CellsLayoutChooser.Layouts.CardLayout) {
			newContainer = new CardLayoutContainer(df, isRoot, container);

		} else if (lt == CellsLayoutChooser.Layouts.FlowLayout) {
			newContainer = new FlowLayoutContainer(df, isRoot, container);

		} else if (lt == CellsLayoutChooser.Layouts.BoxLayout) {
			newContainer = new BoxLayoutContainer(df, isRoot, container);

		} else if (lt == CellsLayoutChooser.Layouts.GridLayout) {
			newContainer = new GridLayoutContainer(df, isRoot, container);

		} else if (lt == CellsLayoutChooser.Layouts.GridBagLayout) {
			newContainer = new GridBagLayoutContainer(df, isRoot, container);

		} else if (lt == CellsLayoutChooser.Layouts.CellsLayout) {
			newContainer = new CellsLayoutContainer(df, isRoot, false,
					container);
		} else {
			return null;
		}

		designer.getProperty().loadComponent(newContainer, container);
		designer.getProperty().copyProperties(container, newContainer);

		return newContainer;
	}

	public void open() {
		if (fc != null
				&& fc.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
			compile();
		}
	}

	public void save() {
		DesignerFrame df = (DesignerFrame) getSelectedFrame();
		if (df != null) {
			save(df);
			designer.getActionManager().update();
		}
	}

	public void save(DesignerFrame df) {

		setSelectedFrame(df);

		String s = designer.getDesktop().getDesigner().getSourcer().getSourceCode();
		if (s == null) {
			CellsSourceGenerator sg = designer.getSourceGenerator();
			StringBuffer sb = sg.generate();
			s = sb.toString();
		}

		String fn = CellsUtil.CELL_HOME + File.separator
				+ df.getPackage().replace(".", File.separator) + File.separator
				+ df.getTitle() + ".java";

		try {
			CellsUtil.saveToFile(fn, s);
			designer.redraw();
			df.save();

		} catch (IOException ex) {
			DebugPanel.printStackTrace(ex);
		}
	}

	public void saveAs() {
		DesignerFrame df = (DesignerFrame) getSelectedFrame();
		if (df != null) {
			if (fc != null
					&& fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				
				String fn = fc.getSelectedFile().getAbsolutePath();
				
				setSelectedFrame(df);

				String s = designer.getDesktop().getDesigner().getSourcer().getSourceCode();
				if (s == null) {
					CellsSourceGenerator sg = designer.getSourceGenerator();
					StringBuffer sb = sg.generate();
					s = sb.toString();
				}

				try {
					CellsUtil.saveToFile(fn, s);
					designer.redraw();
					df.save();

				} catch (IOException ex) {
					DebugPanel.printStackTrace(ex);
				}
			}
		}
	}

	public void saveAs2() {
		DesignerFrame df = (DesignerFrame) getSelectedFrame();
		if (df != null) {
			if (fc != null
					&& fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				String s = fc.getSelectedFile().getName();
				if (s.endsWith(".java")) {
					s = s.substring(0, s.indexOf(".java"));
				}
				df.setTitle(s);
				df.setName(s);
				save(df);
			}
		}
	}

	public void saveAll() {
		for (Component comp : getComponents()) {
			DesignerFrame df = (DesignerFrame) comp;
			if (df.canSave()) {
				save(df);
			}
		}
	}

	public DesignerFrame getDesigner() {
		return (DesignerFrame) getSelectedFrame();
	}

	public void deselectAll() {
		DesignerFrame df = (DesignerFrame) getSelectedFrame();
		if (df != null) {
			df.deselectAll();
		}
	}

	public void redraw() {
		DesignerFrame df = (DesignerFrame) getSelectedFrame();
		if (df != null) {
			df.redraw();
		}
	}

	public void changeLayout() {

		int o = CellsDialog
				.showDialog(
						designer,
						"Changing Layout",
						"Change Layout",
						"<html><b>Warning:</b><br>"
								+ "Changing layout causes that all settings of this Form are lost.<br>"
								+ "Continue?</html>", false, true, "Change",
						"", "Cancel", 410, 250);

		if (o == CellsDialog.APPROVE_OPTION) {

			CellsLayoutChooser lc = new CellsLayoutChooser(designer);
			if (lc.isApproved()) {
				DesignerFrame df = (DesignerFrame) getSelectedFrame();

				if (lc.getLM().getLayoutType() == CellsLayoutChooser.Layouts.BorderLayout
						&& df.getContainer().getComponentCount() > 5) {
					o = CellsDialog
							.showDialog(
									designer,
									"Changing Layout",
									"Change to BorderLayout",
									"<html><b>Warning:</b><br>"
											+ "The Container using BorderLayout can only have up to 5 components.<br>"
											+ "Continue?</html>", false, true,
									"Change", "", "Cancel", 450, 250);

					if (o != CellsDialog.APPROVE_OPTION) {
						return;
					}
				}

				InternalFrameListener sl = df.getSpecialListener();
				df.removeSpecialListener();

				remove(df);
				df.destroy();

				DesignerFrame nf = newDesignerFrame(lc.getLM().getLayoutType(),
						df.getName(), df.getName(), null, df.getParentFrame());

				nf.addSpecialListener(sl);
				nf.getContainer().setName(df.getContainer().getName());
				nf.getContainer().convert(df.getContainer());
				nf.update();
				nf.setDirty(true);

				designer.getActionManager().update();
				designer.deselectAll();
				designer.redraw();
			}
		}
	}

	public void layoutSubContainer(Component comp) {

		DesignerFrame df = (DesignerFrame) getSelectedFrame();
		DesignerFrame nf = newDesignerFrame((Container) comp, df);

		if (nf != null) {
			nf.addSpecialListener(new SpecialClosingListener(df, comp));
		}
	}

	private class SpecialClosingListener extends InternalFrameAdapter {

		private DesignerFrame sourceFrame;
		private Component sourceComponent;

		public SpecialClosingListener(DesignerFrame sourceFrame,
				Component sourceComponent) {
			this.sourceFrame = sourceFrame;
			this.sourceComponent = sourceComponent;
		}

		public void internalFrameClosed(InternalFrameEvent e) {
			DesignerFrame targetFrame = (DesignerFrame) e.getInternalFrame();
			DesignerContainer oc = targetFrame.getContainer();
			DesignerContainer nc = newDesignerContainer(sourceFrame, oc, false);

			oc.selectDesignerConstraints(null);

			sourceFrame.getContainer().replaceComponent(sourceComponent, nc);
			sourceFrame.setPostSelecion(nc);
		}
	}

	// C E L L S - S W I N G W O R K E R

	private String resultFileName;
	private Container resultContainer;

	private JFrame testFrame;
	private boolean testRunning = false;
	private CellsSwingWorker sw;

	private synchronized void compile() {
		CellsSwingWorker sw = new CellsSwingWorker(this, 1, 30, 1000);
		sw.startExecuting();
	}

	private synchronized void scan() {
		CellsSwingWorker sw = new CellsSwingWorker(this, 2, 30, 1000);
		sw.startExecuting();
	}

	public synchronized void startPreview() {
		sw = new CellsSwingWorker(this, 3);
		sw.startExecuting();
	}

	public synchronized void stopPreview() {
		sw.stopExecuting();
	}

	public boolean testRunning() {
		return testRunning;
	}

	// ============= Compiling Task ==========

	public CellsSwingWorker.Worker worker(int handle) {

		if (handle == 1) {
			designer.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			designer.getStatusBar().startBusyLine(60);
			return new CellsSourceCompiler(designer, fc.getSelectedFile());

		} else if (handle == 2) {
			return new ScanWorker();

		} else if (handle == 3) {
			return new PreviewWorker();
		}
		return null;
	}

	public void report(int handle, CellsSwingWorker.States s) {
		if (handle == 1) {
			switch (s) {

			case STARTING:
				designer.getDebuger().appendMessage("compiling the source...");
				break;

			case WORKING:
				designer.getStatusBar().updateBusyLine();
				break;

			case BREAKING:
				designer.getDebuger().appendMessage("compiling failed.");
				break;

			case STOPPING:
				designer.getDebuger().appendMessage("compiling down.");
				break;
			}
		} else if (handle == 2) {
			switch (s) {

			case STARTING:
				designer.getDebuger().appendMessage("scaning components...");
				break;

			case WORKING:
				designer.getStatusBar().updateBusyLine();
				break;

			case BREAKING:
				break;

			case STOPPING:
				designer.getDebuger().appendMessage("scaning down.");
				break;
			}
		} else if (handle == 3) {

		}
	}

	public void terminate(int handle, Object result, boolean aborted) {

		boolean compilingDown = false;

		try {
			if (handle == 1) {
				if (aborted) {

					// could not be able to compile!
					// hope that the class can be found by the system
					// classLoader
					result = CellsUtil.scanContainer(fc.getSelectedFile());
				}

				if (result != null && result instanceof JPanel) {

					resultFileName = fc.getSelectedFile().getName();
					resultContainer = (Container) result;
					compilingDown = true;

					scan();

				} else {
					DebugPanel.appendError("compiling failed!");
				}

			} else if (handle == 2) {

			}

		} catch (Exception ex) {
			DebugPanel.printStackTrace(ex);

		} finally {
			if (!compilingDown) {
				designer.getStatusBar().stopBusyLine();
				designer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	// =========== Scan Task =============

	private class ScanWorker extends CellsSwingWorker.Worker {

		public void run() {
			try {
				int i = resultFileName.indexOf(".");
				if (i != -1) {
					resultFileName = resultFileName.substring(0, i);
				}

				DesignerFrame df = newDesignerFrame(CellsLayoutChooser.getLM()
						.getLayoutType(resultContainer), resultFileName,
						resultFileName, resultContainer, null);

				// scan package and the property setter

				CellsUtil.scanSource(fc.getSelectedFile(), df, designer.getProperty());
				designer.update();

			} catch (Exception ex) {
				DebugPanel.printStackTrace(ex);
			}
		}

		public Object result() {
			return null;
		}

		public void terminate(boolean aborted) {
			designer.getStatusBar().stopBusyLine();
			designer.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	// =========== Preview Task =============

	private class PreviewWorker extends CellsSwingWorker.Worker {

		public void run() {
			DesignerFrame df = (DesignerFrame) getSelectedFrame();

			JPanel panel = (JPanel) CellsUtil.clone(df.getContainer(), false, designer);

			testFrame = new JFrame("Preview " + df.toString()) {
				public void setVisible(boolean b) {
					testRunning = b;
					super.setVisible(b);
					if (!b) {
						designer.getActionManager().update();
					}
				}
			};

			testFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
			
			testFrame.setUndecorated(!designer.getLookAndFeel().equals(CellsDesigner.lf_windows)
					&& !designer.getLookAndFeel().equals(CellsDesigner.lf_motif));
			
			testFrame.setDefaultLookAndFeelDecorated(false);

			testFrame.setLayout(new BorderLayout());
			testFrame.add(panel, BorderLayout.CENTER);
			testFrame.pack();
			testFrame.setSize(500, 300);
			testFrame.setVisible(true);

			while (testRunning) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					testRunning = false;
					break;
				}
			}
		}

		public Object result() {
			return null;
		}

		public void terminate(boolean aborted) {
			testFrame.setVisible(false);
		}
	}
}
