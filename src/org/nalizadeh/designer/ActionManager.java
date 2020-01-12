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

import org.nalizadeh.designer.actions.CellsAction;
import org.nalizadeh.designer.actions.ChangeJBeansPropertyAction;
import org.nalizadeh.designer.actions.ChangePropertyAction;
import org.nalizadeh.designer.actions.ChangeSplitPaneAction;
import org.nalizadeh.designer.actions.ChangeTabbedPaneAction;
import org.nalizadeh.designer.actions.CopyAction;
import org.nalizadeh.designer.actions.CutAction;
import org.nalizadeh.designer.actions.DeleteAction;
import org.nalizadeh.designer.actions.DeleteComponentAction;
import org.nalizadeh.designer.actions.InsertComponentAction;
import org.nalizadeh.designer.actions.MoveComponentAction;
import org.nalizadeh.designer.actions.OrderComponentsAction;
import org.nalizadeh.designer.actions.PasteAction;
import org.nalizadeh.designer.actions.RedoAction;
import org.nalizadeh.designer.actions.ReplaceComponentAction;
import org.nalizadeh.designer.actions.ResizeComponentAction;
import org.nalizadeh.designer.actions.UndoAction;

import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.cellslayout.actions.DeleteColumnAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.DeleteColumnContentsAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.DeleteRowAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.DeleteRowContentsAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.InsertColumnAfterAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.InsertColumnBeforeAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.InsertRowAfterAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.InsertRowBeforeAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.MoveColumnAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.MoveRowAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.ResizeColumnAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.ResizeRowAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.SelectColumnAction;
import org.nalizadeh.designer.layouts.cellslayout.actions.SelectRowAction;

import org.nalizadeh.designer.util.CellsCopyrightDialog;
import org.nalizadeh.designer.util.CellsUtil;
import org.nalizadeh.designer.util.NewDesignerDialog;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoManager;

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
public class ActionManager extends HashMap<String, Action> {

	private final CellsDesigner designer;
	private Object clipboard = null;
	private boolean undoredoEnabled = true;

	public ActionManager(CellsDesigner designer) {
		this.designer = designer;

		put("Undo", new UndoAction(designer));
		put("Redo", new RedoAction(designer));
		put("Cut", new CutAction(designer));
		put("Copy", new CopyAction(designer));
		put("Paste", new PasteAction(designer));
		put("Delete", new DeleteAction(designer));

		addAction("New", "Create new Java class", KeyEvent.VK_N, KeyStroke
				.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK), true);

		addAction("Open", "Open existing Java class", KeyEvent.VK_O, KeyStroke
				.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK), true);

		addAction("Save", "Save the form", KeyEvent.VK_S, KeyStroke
				.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK), false);

		addAction("SaveAs", "Save current file as", KeyEvent.VK_A, KeyStroke
				.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK), false);

		addAction("SaveAll", "Save all changed files", KeyEvent.VK_L, KeyStroke
				.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK), false);

		addAction("Exit", "Exiting Designer", KeyEvent.VK_X, KeyStroke
				.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK), true);

		addAction("About The Cells", "Show Copyright and Version Information", true);

		addAction("Windows", null, true);
		addAction("Synth", null, true);
		addAction("Metal", null, true);
		addAction("Motif", null, true);
		addAction("Skin", null, true);
		addAction("Kunststoff", null, true);
		addAction("Liquid", null, true);
		addAction("OSX (Aqua)", null, true);
		addAction("GTK", null, true);
		addAction("Sone (SAP)", null, true);

		addAction("Cursor", null, true);
		addAction("Marqueeing", null, true);
		addAction("Grid", null, true);

		addAction("JLabel", null, true);
		addAction("JButton", null, true);
		addAction("JToggleButton", null, true);
		addAction("JTextField", null, true);
		addAction("JPasswordField", null, true);
		addAction("JRadioButton", null, true);
		addAction("JCheckBox", null, true);
		addAction("JComboBox", null, true);
		addAction("JSpinner", null, true);
		addAction("JTextArea", null, true);
		addAction("JList", null, true);
		addAction("JTable", null, true);
		addAction("JTree", null, true);
		addAction("JSlider", null, true);
		addAction("JProgressBar", null, true);
		addAction("JSeparator", null, true);
		addAction("JPanel", null, true);
		addAction("JTabbedPane", null, true);
		addAction("JScrollPane", null, true);
		addAction("JSplitPane", null, true);

		addAction("JCalendar", null, true);
		addAction("JTime", null, true);

		String[] bx = CellsUtil.getBeanNames();
		for (String s : bx) {
			addAction(s, null, true);
		}

		addAction("Designer", "Change to the design mode", false);
		addAction("Source", "Generate and show Java source code", false);
		addAction("Html", "Generate and show HTML code", false);
		addAction("Aspx", "Generate and show ASPX code", false);
		addAction("HtmlPreview", "Preview in HTML browser", false);

		addAction("RunTest", "Show preview window", false);
		addAction("StopTest", "Close preview window", false);
		addAction("ClearConsole", "Clear console", true);

		put("InsertComponent", new InsertComponentAction(designer));
		put("MoveComponent", new MoveComponentAction(designer));
		put("ResizeComponent", new ResizeComponentAction(designer));
		put("DeleteComponent", new DeleteComponentAction(designer));
		put("ReplaceComponent", new ReplaceComponentAction(designer));
		put("OrderComponents", new OrderComponentsAction(designer));

		put("SelectRow", new SelectRowAction(designer));
		put("DeleteRow", new DeleteRowAction(designer));
		put("DeleteRowContents", new DeleteRowContentsAction(designer));
		put("InsertRowBefore", new InsertRowBeforeAction(designer));
		put("InsertRowAfter", new InsertRowAfterAction(designer));
		put("MoveRow", new MoveRowAction(designer));
		put("ResizeRow", new ResizeRowAction(designer));

		put("SelectColumn", new SelectColumnAction(designer));
		put("DeleteColumn", new DeleteColumnAction(designer));
		put("DeleteColumnContents", new DeleteColumnContentsAction(designer));
		put("InsertColumnBefore", new InsertColumnBeforeAction(designer));
		put("InsertColumnAfter", new InsertColumnAfterAction(designer));
		put("MoveColumn", new MoveColumnAction(designer));
		put("ResizeColumn", new ResizeColumnAction(designer));

		put("ChangeProperty", new ChangePropertyAction(designer));
		put("ChangeJBeansProperty", new ChangeJBeansPropertyAction(designer));
		put("ChangeTabbedPane", new ChangeTabbedPaneAction(designer));
		put("ChangeSplitPane", new ChangeSplitPaneAction(designer));
	}

	public void addAction(String name, String tooltip, boolean state) {
		addAction(name, tooltip, -1, null, state);
	}

	public void addAction(String name, String tooltip, int mnemonic, 
			KeyStroke keyStroke, boolean state) {

		Action ac = new CellsAction(name) {
			public void init() {

			}

			public void actionPerformed(ActionEvent e) {
				executeAction(this, e);
			}
		};

		ac.setEnabled(state);
		ac.putValue(Action.NAME, name);
		ac.putValue(Action.SHORT_DESCRIPTION, tooltip == null ? name : tooltip);
		ac.putValue(Action.SMALL_ICON, CellsUtil.getImage(name));
		ac.putValue(Action.MNEMONIC_KEY, mnemonic);
		ac.putValue(Action.ACCELERATOR_KEY, keyStroke);

		put(name, ac);
	}

	public Action getAction(String name) {
		CellsAction ac = (CellsAction) get(name);
		ac.init();
		return ac;
	}

	public void setEnabled(String name, boolean b) {
		getAction(name).setEnabled(b);
	}

	public void setClipboard(Object clipboard) {
		this.clipboard = clipboard;
	}

	public Object getClipboard() {
		return clipboard;
	}

	public UndoManager getUndoManager() {
		DesignerFrame df = designer.getDesktop().getDesigner();
		return df == null ? null : df.getUndoManager();
	}

	public void undoRedoEnabled(boolean undoredoEnabled) {
		this.undoredoEnabled = undoredoEnabled;
	}

	public void undoableEditHappened(AbstractUndoableEdit edit) {
		if (undoredoEnabled) {

			DesignerFrame df = designer.getDesktop().getDesigner();
			df.getUndoManager().undoableEditHappened(
					new UndoableEditEvent(df, edit));

			df.setDirty(true);

			getAction("Undo").setEnabled(df.getUndoManager().canUndo());
			getAction("Redo").setEnabled(df.getUndoManager().canRedo());

			getAction("Undo").putValue(AbstractAction.SHORT_DESCRIPTION,
					df.getUndoManager().getUndoPresentationName());

			getAction("Redo").putValue(AbstractAction.SHORT_DESCRIPTION,
					df.getUndoManager().getRedoPresentationName());
		}
	}

	public void update() {

		setEnabled("Undo", false);
		setEnabled("Redo", false);
		setEnabled("Cut", false);
		setEnabled("Copy", false);
		setEnabled("Delete", false);

		setEnabled("Cursor", false);
		setEnabled("Marqueeing", false);
		setEnabled("Grid", false);

		setEnabled("Designer", false);
		setEnabled("Source", false);
		setEnabled("Html", false);
		setEnabled("Aspx", false);
		setEnabled("HtmlPreview", false);

		boolean b1 = designer.getDesktop().getAllFrames().length > 0;
		boolean b2 = designer.getDesktop().getSelectedFrame() != null;
		boolean b3 = b1 && b2;
		boolean b4 = designer.getDesktop().testRunning();
		
		setEnabled("Save", b3);
		setEnabled("SaveAs", b3);
		setEnabled("SaveAll", b3);
		setEnabled("RunTest", b3);

		setEnabled("RunTest", !b4 && b2);
		setEnabled("StopTest", b4);
		// setEnabled("ClearConsole", !DebugPanel.isEmpty());

		if (designer.getDesktop().getDesigner() != null) {
			boolean b5 = designer.getDesktop().getDesigner().sourceShowing();
			boolean b6 = designer.getDesktop().getDesigner().htmlShowing();
			boolean b7 = designer.getDesktop().getDesigner().aspxShowing();
			setEnabled("Designer", b5|b6|b7);
			setEnabled("Source", !b5);
			setEnabled("Html", !b6);
			setEnabled("Aspx", !b7);
			setEnabled("HtmlPreview", true);
		}

		setEnabled("JCalendar", false);
		setEnabled("JTime", false);

		if (b2) {

			DesignerFrame df = designer.getDesktop().getDesigner();

			setEnabled("Cut", df.canCutCopy());
			setEnabled("Copy", df.canCutCopy());
			setEnabled("Paste", df.canPaste() && clipboard != null);
			setEnabled("Delete", df.canCutCopy());

			setEnabled("Undo", df.getUndoManager().canUndo());
			setEnabled("Redo", df.getUndoManager().canRedo());

			getAction("Undo").putValue(AbstractAction.SHORT_DESCRIPTION,
					df.getUndoManager().getUndoPresentationName());

			getAction("Redo").putValue(AbstractAction.SHORT_DESCRIPTION,
					df.getUndoManager().getRedoPresentationName());

			setEnabled("Save", df.canSave());
			setEnabled("SaveAll", df.canSave());

			boolean b = df.getContainer().getSelectedDesignerConstraints() != null;

			setEnabled("Cursor", b);
			setEnabled("Marqueeing", b);
			setEnabled("Grid", df.getContainer().isGridEnabled());

			setEnabled("JCalendar", b);
			setEnabled("JTime", b);
		}

		// activate MemoryReporter manually
		designer.getStatusBar().updateMemoryReporting();
	}

	public void executeAction(Action action, ActionEvent e) {

		DesignerFrame df = designer.getDesktop().getDesigner();

		String name = (String) action.getValue(Action.NAME);

		if (name.equals("About The Cells")) {
			CellsCopyrightDialog dlg = new CellsCopyrightDialog(designer);
			dlg.setVisible(true);

		} else if (name.equals("Exit")) {
			designer.setVisible(false);

		} else if (name.equals("New")) {
			NewDesignerDialog dlg = new NewDesignerDialog(designer, designer);
			dlg.setVisible(true);

		} else if (name.equals("Open")) {
			designer.getDesktop().open();

		} else if (name.equals("Designer")) {
			designer.getDesktop().getDesigner().showSource(false);
			designer.getDesktop().getDesigner().showHtml(false);
			designer.getDesktop().getDesigner().showAspx(false);
			getAction("Designer").setEnabled(false);
			getAction("Source").setEnabled(true);
			getAction("Html").setEnabled(true);
			getAction("Aspx").setEnabled(true);
			
		} else if (name.equals("Source")) {
			designer.getDesktop().getDesigner().showHtml(false);
			designer.getDesktop().getDesigner().showAspx(false);
			designer.getDesktop().getDesigner().showSource(true);
			getAction("Source").setEnabled(false);
			getAction("Designer").setEnabled(true);
			getAction("Html").setEnabled(true);
			getAction("Aspx").setEnabled(true);
			
		} else if (name.equals("Html")) {
			designer.getDesktop().getDesigner().showSource(false);
			designer.getDesktop().getDesigner().showAspx(false);
			designer.getDesktop().getDesigner().showHtml(true);
			getAction("Html").setEnabled(false);
			getAction("Designer").setEnabled(true);
			getAction("Source").setEnabled(true);
			getAction("Aspx").setEnabled(true);
			
		} else if (name.equals("Aspx")) {
			designer.getDesktop().getDesigner().showSource(false);
			designer.getDesktop().getDesigner().showHtml(false);
			designer.getDesktop().getDesigner().showAspx(true);
			getAction("Aspx").setEnabled(false);
			getAction("Designer").setEnabled(true);
			getAction("Source").setEnabled(true);
			getAction("Html").setEnabled(true);
			
		} else if (name.equals("HtmlPreview")) {
			designer.getDesktop().getDesigner().getPreviewHtml();

		} else if (name.equals("RunTest")) {
			getAction("RunTest").setEnabled(false);
			getAction("StopTest").setEnabled(true);
			designer.getDesktop().startPreview();

		} else if (name.equals("StopTest")) {
			getAction("StopTest").setEnabled(false);
			getAction("RunTest").setEnabled(true);
			designer.getDesktop().stopPreview();

		} else if (name.equals("Synth")) {
			designer.setLookAndFeel(CellsDesigner.lf_synth);
		} else if (name.equals("Windows")) {
			designer.setLookAndFeel(CellsDesigner.lf_windows);
		} else if (name.equals("Metal")) {
			designer.setLookAndFeel(CellsDesigner.lf_metal);
		} else if (name.equals("Motif")) {
			designer.setLookAndFeel(CellsDesigner.lf_motif);
		} else if (name.equals("Skin")) {
			designer.setLookAndFeel(CellsDesigner.lf_skin);
		} else if (name.equals("Kunststoff")) {
			designer.setLookAndFeel(CellsDesigner.lf_kunststoff);
		} else if (name.equals("Liquid")) {
			designer.setLookAndFeel(CellsDesigner.lf_liquid);
		} else if (name.equals("OSX (Aqua)")) {
			designer.setLookAndFeel(CellsDesigner.lf_osx);
		} else if (name.equals("GTK")) {
			designer.setLookAndFeel(CellsDesigner.lf_gtk);
		} else if (name.equals("GTK")) {
			designer.setLookAndFeel(CellsDesigner.lf_gtk);
		} else if (name.equals("Sone (SAP)")) {
			designer.setLookAndFeel(CellsDesigner.lf_sone);

		} else if (name.equals("ClearConsole")) {
			DebugPanel.clear();
			update();

		} else if (df != null) {

			if (name.equals("Cursor")) {
				df.getContainer().setMarqueeingEnabled(false);
				df.getContainer().setInsertingComponent(null);
				designer.getStatusBar().updateItem(2, "", null);
				designer.deselectAll();
				designer.redraw();

			} else if (name.equals("Marqueeing")) {
				df.getContainer().setMarqueeingEnabled(true);
				df.getContainer().setInsertingComponent(null);
				designer.getStatusBar().updateItem(2, "Marqueeing is activ",
						CellsUtil.getImage("Marqueeing2"));
				designer.deselectAll();
				designer.redraw();

			} else if (name.equals("Grid")) {
				df.getContainer().setGridPainted(
						!df.getContainer().isGridPainted());

			} else if (name.equals("Save")) {
				designer.getDesktop().save();

			} else if (name.equals("SaveAs")) {
				designer.getDesktop().saveAs();

			} else if (name.equals("SaveAll")) {
				designer.getDesktop().saveAll();

			} else {
				Component comp = CellsUtil.createComponent(name);
				df.getContainer().setInsertingComponent(comp);
			}
		}
	}
}
