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

package org.nalizadeh.designer.util;

import org.nalizadeh.designer.ActionManager;
import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.base.DesignerConstraints;
import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.freelayout.FreeLayoutContainer;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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
public class CellsPopupMenu implements PopupMenuListener {

	private CellsDesigner designer;
	private JPopupMenu popup;
	private Action changeLayoutAction;
	private Action layoutPanelAction;
	private Component[] popupComps;

	private Action addTabAction;
	private Action insertTabAction;
	private Action deleteTabAction;
	private JTabbedPane tabbedpane;

	private Action aliglAction;
	private Action aligrAction;
	private Action aligtAction;
	private Action aligbAction;

	public CellsPopupMenu(CellsDesigner dsg) {

		this.designer = dsg;
		this.popup = new JPopupMenu();

		ActionManager am = designer.getActionManager();

		changeLayoutAction =
			new AbstractAction("Change Layout", CellsUtil.getImage("LayoutProp")) {
				public void actionPerformed(ActionEvent e) {
					designer.getDesktop().changeLayout();
				}
			};

		layoutPanelAction =
			new AbstractAction("Layout Container", CellsUtil.getImage("DoLayout")) {
				public void actionPerformed(ActionEvent e) {
					designer.getDesktop().layoutSubContainer(popupComps[0]);
				}
			};

		addTabAction =
			new AbstractAction("Add Tab", CellsUtil.getImage("JTabbedPane")) {
				public void actionPerformed(ActionEvent e) {
					designer.getDesktop().getDesigner().getContainer().changeTabbedPane(
						tabbedpane,
						1
					);
				}
			};

		insertTabAction =
			new AbstractAction("Insert Tab", CellsUtil.getImage("JTabbedPane")) {
				public void actionPerformed(ActionEvent e) {
					designer.getDesktop().getDesigner().getContainer().changeTabbedPane(
						tabbedpane,
						2
					);
				}
			};

		deleteTabAction =
			new AbstractAction("Delete Tab", CellsUtil.getImage("JTabbedPane")) {
				public void actionPerformed(ActionEvent e) {
					designer.getDesktop().getDesigner().getContainer().changeTabbedPane(
						tabbedpane,
						3
					);
				}
			};

		aliglAction =
			new AbstractAction("Ordering on Left", CellsUtil.getImage("AligL")) {
				public void actionPerformed(ActionEvent e) {
					setAlignment(0);
				}
			};

		aligrAction =
			new AbstractAction("Ordering on Right", CellsUtil.getImage("AligR")) {
				public void actionPerformed(ActionEvent e) {
					setAlignment(1);
				}
			};

		aligtAction =
			new AbstractAction("Ordering on Top", CellsUtil.getImage("AligT")) {
				public void actionPerformed(ActionEvent e) {
					setAlignment(2);
				}
			};

		aligbAction =
			new AbstractAction("Ordering on Bottom", CellsUtil.getImage("AligB")) {
				public void actionPerformed(ActionEvent e) {
					setAlignment(3);
				}
			};

		changeLayoutAction.setEnabled(false);
		layoutPanelAction.setEnabled(false);
		addTabAction.setEnabled(false);
		deleteTabAction.setEnabled(false);

		aliglAction.setEnabled(false);
		aligrAction.setEnabled(false);
		aligtAction.setEnabled(false);
		aligbAction.setEnabled(false);


		popup.add(new JMenuItem(am.getAction("Cut")));
		popup.add(new JMenuItem(am.getAction("Copy")));
		popup.add(new JMenuItem(am.getAction("Paste")));
		popup.add(new JMenuItem(am.getAction("Delete")));
		popup.addSeparator();
		popup.add(new JMenuItem(addTabAction));
		popup.add(new JMenuItem(insertTabAction));
		popup.add(new JMenuItem(deleteTabAction));
		popup.addSeparator();
		popup.add(new JMenuItem(aliglAction));
		popup.add(new JMenuItem(aligrAction));
		popup.add(new JMenuItem(aligtAction));
		popup.add(new JMenuItem(aligbAction));
		popup.addSeparator();
		popup.add(new JMenuItem(am.getAction("Designer")));
		popup.add(new JMenuItem(am.getAction("Source")));
		popup.add(new JMenuItem(changeLayoutAction));
		popup.addSeparator();
		popup.add(new JMenuItem(layoutPanelAction));

		popup.addPopupMenuListener(this);
	}

	public void showPopup(Component owner, Component[] comps, int x, int y) {
		this.popupComps = comps;

		boolean isContainer = false;
		boolean isTabbedPane = false;

		changeLayoutAction.setEnabled(false);
		layoutPanelAction.setEnabled(false);
		addTabAction.setEnabled(false);
		insertTabAction.setEnabled(false);
		deleteTabAction.setEnabled(false);

		aliglAction.setEnabled(false);
		aligrAction.setEnabled(false);
		aligtAction.setEnabled(false);
		aligbAction.setEnabled(false);

		if (popupComps.length > 1) {
			if (comps.length > 1) {

				DesignerContainer cont = designer.getDesktop().getDesigner().getContainer();
				if (cont instanceof FreeLayoutContainer) {
					aliglAction.setEnabled(true);
					aligrAction.setEnabled(true);
					aligtAction.setEnabled(true);
					aligbAction.setEnabled(true);
				}
			}
		} else if (popupComps.length > 0) {
			if (CellsUtil.isPanel(popupComps[0])) {
				isContainer = true;

			} else if (popupComps[0] instanceof JTabbedPane) {
				isContainer = true;
				isTabbedPane = true;
				tabbedpane = ((JTabbedPane) popupComps[0]);
				popupComps[0] = tabbedpane.getSelectedComponent();

			} else if (popupComps[0] instanceof JSplitPane) {
				isContainer = true;
				JSplitPane sp = (JSplitPane) popupComps[0];
				popupComps[0] = sp.getComponentAt(x - sp.getX(), y - sp.getY());
			}
		}

		DesignerFrame df = designer.getDesktop().getDesigner();
		DesignerConstraints cons = df.getContainer().getSelectedDesignerConstraints();

		designer.getActionManager().getAction("Source").setEnabled(cons.getComponent() == null);
		changeLayoutAction.setEnabled(cons.getComponent() == null);
		layoutPanelAction.setEnabled(isContainer && popupComps[0] != null);
		addTabAction.setEnabled(isTabbedPane);
		insertTabAction.setEnabled(isTabbedPane);
		deleteTabAction.setEnabled(isTabbedPane && tabbedpane.getComponentCount() > 1);

		popup.show(owner, x, y);
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	}

	public void popupMenuCanceled(PopupMenuEvent e) {
	}

	private void setAlignment(int m) {
		designer.getDesktop().getDesigner().getContainer().orderComponentsAligment(popupComps, m);
	}
}
