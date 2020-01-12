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

package org.nalizadeh.designer.base;

import org.nalizadeh.designer.actions.CellsAction;

import org.nalizadeh.designer.property.Property;

import org.nalizadeh.designer.util.CellsTextPopup;
import org.nalizadeh.designer.util.CellsUtil;
import org.nalizadeh.designer.util.beans.JCalendar;
import org.nalizadeh.designer.util.beans.JTime;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.JViewport;

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
public abstract class DesignerContainer extends Designer {

	private int iLab = 0;
	private int iBut = 0;
	private int iTbt = 0;
	private int iTxt = 0;
	private int iPas = 0;
	private int iRad = 0;
	private int iChk = 0;
	private int iCom = 0;
	private int iSpi = 0;
	private int iTar = 0;
	private int iSep = 0;
	private int iLst = 0;
	private int iTab = 0;
	private int iTre = 0;
	private int iPba = 0;
	private int iSli = 0;
	private int iScr = 0;
	private int iSpl = 0;
	private int iTpa = 0;
	private int iPan = 0;
	private int iCal = 0;
	private int iTim = 0;
	private int iBea = 0;

	private DesignerFrame frame;
	private CellsTextPopup textPopup;
	private List<DesignerListener> listeners;

	private boolean selectAdjusting = false;

	public DesignerContainer(
		DesignerFrame frame,
		boolean		  isRoot,
		LayoutManager layout,
		Container	  container
	) {
		super(isRoot, layout);

		this.frame = frame;
		this.textPopup = new CellsTextPopup(this);
		this.listeners = new ArrayList<DesignerListener>();

		setName("contentPane");

		if (isRoot) {
			addDesignerListener(frame.getDesigner());
			new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DesignerDropListener(this));

		}
		if (container != null) {
			String name = container.getName();
			setName(name == null ? "contentPane" : name);
			copyFrom(container);
		}
	}

	public DesignerFrame getFrame() {
		return frame;
	}

	public void add(Component comp, Object constraints) {
		if (constraints instanceof Integer) {
			int i = Math.min((Integer) constraints, getComponentCount());
			super.add(comp, constraints, i);

		} else if (constraints instanceof Rectangle) {
			super.add(comp, null);

			Rectangle r = (Rectangle) constraints;
			if (r.width == -1 || r.height == -1) {
				r.width = comp.getWidth();
				r.height = comp.getHeight();
			}
			comp.setBounds(r);
		} else {
			super.add(comp, constraints);
		}
	}

	public void copyFrom(Container src) {

		for (Component comp : src.getComponents()) {
			setComponentName(src, comp, getName());
		}

		copy(src, this);
	}

	public void copyTo(Container dst) {
		copy(this, dst);
	}

	protected abstract void copy(Container cont, Container dst);

	public abstract void convert(Container src);

	public final void selectDesignerConstraints(DesignerConstraints cons) {

		Component comp = null;
		if (cons != null && cons.getComponent() != null) {
			comp = cons.getComponent();
			selectTabbedPane(comp);
		}

		selectAdjusting = true;
		findRoot().select(comp);
		selectAdjusting = false;
	}

	public final DesignerConstraints getSelectedDesignerConstraints() {

		Component comp = findRoot().getSelectedComponent();
		if (comp != null) {
                        DesignerContainer cont = (DesignerContainer) findDesigner(comp);
			return new DesignerConstraints(
                                cont.getLayoutConstraints(comp, -1, -1, null), comp, cont);
		}
		return new DesignerConstraints();
	}

	public final DesignerConstraints cloneDesignerConstraints(DesignerConstraints cons) {
		return new DesignerConstraints(
			cloneLayoutConstraints(cons.getConstraints()),
			cons.getComponent(),
			cons.getContainer()
		);
	}

	public final void deleteDesignerConstraints() {

                DesignerConstraints cons = getSelectedDesignerConstraints();

                Component comp = cons.getComponent();
                Component parent = comp.getParent();

                if (parent instanceof JTabbedPane) {
                        changeTabbedPane((JTabbedPane) parent, 0);
                } else if (parent instanceof JSplitPane) {
                        deleteSplitPaneComponent(comp);
                } else {
                        doDeleteComponent(cons);
                }
	}

	protected abstract Component getLayoutComponent(Component comp, Rectangle bounds);

	protected abstract Object getLayoutConstraints(Component comp, int x, int y, Rectangle bounds);

	protected abstract Object cloneLayoutConstraints(Object cons);

	protected abstract void copyLayoutConstraints(Object src, Object dst);

	protected final int getZOrder(Component comp, int x, int y) {
		Component[] comps = getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] != comp && comps[i].getBounds().contains(x, y)) {
				return i;
			}
		}
		return -1;
	}

	protected final void selectionChanged(Component comp) {
		if (!selectAdjusting) {
			fireSelectionChanged(new DesignerConstraints(null, comp, this));
		}
		update();
	}

	protected boolean deleteComponent(Component comp) {
                DesignerConstraints cons = new DesignerConstraints(null, comp,
                        (DesignerContainer)findDesigner(comp));

		return doDeleteComponent(cons);
	}

	protected boolean insertComponent(Component comp, Rectangle bounds) {
		Object cons = getLayoutConstraints(comp, bounds.x, bounds.y, bounds);
		Component co = getLayoutComponent(comp, bounds);
		return doInsertComponent(new DesignerConstraints(cons, co, this), comp);
	}

	protected boolean moveComponent(
		Component comp,
		Container oldContainer,
		Container newContainer,
		Point	  oldLocation,
		Point	  newLocation,
		Rectangle oldBounds,
		Rectangle newBounds
	) {

		if (!oldBounds.equals(newBounds)) {
			DesignerContainer srcCont = (DesignerContainer) oldContainer;
			DesignerContainer dstCont = (DesignerContainer) newContainer;

			Object srcCons =
				srcCont.getLayoutConstraints(comp, oldLocation.x, oldLocation.y, oldBounds);
			Object dstCons =
				dstCont.getLayoutConstraints(null, newLocation.x, newLocation.y, newBounds);

			if (srcCons != null && dstCons != null && !srcCons.equals(dstCons)) {

				if (srcCont.getClass().equals(dstCont.getClass())) {
					copyLayoutConstraints(srcCons, dstCons);
				}

				Component co =
					dstCont.getLayoutComponent(
						null,
						new Rectangle(newLocation.x, newLocation.y, 1, 1)
					);

				if (co != comp) {
					return doMoveComponent(
						new DesignerConstraints(srcCons, comp, srcCont),
						new DesignerConstraints(dstCons, co, dstCont)
					);
				}
			}
		}
		return false;
	}

	protected boolean resizeComponent(Component comp, Rectangle oldBounds, Rectangle newBounds) {
		return doResizeBounds(new DesignerConstraints(null, comp, this), oldBounds, newBounds);
	}

	protected void paintInsertingComponent(Graphics g, Component comp) {
		if (comp instanceof JPanel || comp instanceof JSplitPane) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, comp.getWidth(), comp.getHeight());
		} else {
			comp.paint(g);
		}
	}

	protected void showHint(
		Graphics  g,
		Component comp,
		int		  x,
		int		  y,
		Rectangle bounds,
		boolean   rollover
	) {
	}

	protected final void showPopup(Component[] comps, int x, int y) {
		frame.getDesigner().getPopupMenu().showPopup(this, comps, x, y);
	}

	protected void doubleClicked(Component comp, int x, int y) {
		textPopup.show(comp, x, y);
	}

	protected boolean isGuiComponent(Component comp) {
		return CellsUtil.isGuiComponent(comp);
	}

	protected boolean isGuiContainer(Component comp) {
		return CellsUtil.isContainer(comp);
	}

	protected boolean isMoveable(Component comp) {
		Component pa = comp.getParent();
		if (pa instanceof JViewport || pa instanceof JTabbedPane || pa instanceof JSplitPane) {
			return false;
		}
		return true;
	}

	protected boolean isResizeable(Component comp) {
		Component pa = comp.getParent();
		if (pa instanceof JViewport || pa instanceof JTabbedPane || pa instanceof JSplitPane) {
			return false;
		}
		return true;
	}

	public String toString() {
		return getName();
	}

	protected void update() {
	}

	protected final boolean doInsertComponent(DesignerConstraints cons, Component comp) {
		Action action = frame.getDesigner().getActionManager().getAction("InsertComponent");
		action.putValue("Cons", cons);
		action.putValue("Comp", comp);
		action.actionPerformed(null);
		return ((CellsAction) action).isExecuted();
	}

	protected final boolean doMoveComponent(DesignerConstraints src, DesignerConstraints dst) {
		Action action = frame.getDesigner().getActionManager().getAction("MoveComponent");
		action.putValue("SrcCons", src);
		action.putValue("DstCons", dst);
		action.actionPerformed(null);
		return ((CellsAction) action).isExecuted();
	}

	protected final boolean doResizeComponent(
		DesignerConstraints cons1,
		DesignerConstraints cons2
	) {
		Action action = frame.getDesigner().getActionManager().getAction("ResizeComponent");
		action.putValue("Cons", cons1);
		action.putValue("NewCons", cons2);
		action.actionPerformed(null);
		return true;
	}

	protected final boolean doResizeBounds(
		DesignerConstraints cons,
		Rectangle			oldBound,
		Rectangle			newBound
	) {
		Action action = frame.getDesigner().getActionManager().getAction("ChangeProperty");
		action.putValue("Cons", cons);
		action.putValue("Name", "Bounds");
		action.putValue("Value", newBound);
		action.putValue("OldValue", oldBound);
		action.putValue("Type", Rectangle.class);
		action.actionPerformed(null);
		return true;
	}

	protected final boolean doResizeComponent(DesignerConstraints cons, Point p1, Point p2) {
		Action action = frame.getDesigner().getActionManager().getAction("ChangeProperty");
		action.putValue("Cons", cons);
		action.putValue("Name", "Resizing");
		action.putValue("Value", p1);
		action.putValue("Type", Point.class);
		action.putValue("OldValue", p2);
		action.actionPerformed(null);
		return true;
	}

	private boolean doDeleteComponent(DesignerConstraints cons) {
		Action action = frame.getDesigner().getActionManager().getAction("DeleteComponent");
		action.putValue("Cons", cons);
		action.actionPerformed(null);
		return true;
	}

	public final void replaceComponent(Component oldComp, Component newComp) {

                DesignerContainer cont = (DesignerContainer) findDesigner(oldComp);

		DesignerConstraints oldCons = new DesignerConstraints(
                              cont.getLayoutConstraints(oldComp, -1, -1, null), oldComp, cont);

		DesignerConstraints newCons = new DesignerConstraints(oldCons);
		newCons.setComponent(newComp);

		Action action = frame.getDesigner().getActionManager().getAction("ReplaceComponent");
		action.putValue("OldCons", oldCons);
		action.putValue("NewCons", newCons);
		action.actionPerformed(null);
	}

	public final void orderComponentsAligment(Component[] comps, int mode) {

		int xs = Integer.MAX_VALUE;
		int ys = Integer.MAX_VALUE;
		int xe = Integer.MIN_VALUE;
		int ye = Integer.MIN_VALUE;
		for (Component comp : comps) {
			xs = Math.min(xs, comp.getX());
			ys = Math.min(ys, comp.getY());
			xe = Math.max(xe, comp.getX() + comp.getWidth());
			ye = Math.max(ye, comp.getY() + comp.getHeight());
		}

		Point[] locs = new Point[comps.length];
		int i = 0;

		for (Component c : comps) {
			switch (mode) {

				case 0 :
					locs[i++] = new Point(xs, c.getY());
					break;

				case 1 :
					locs[i++] = new Point(xe - c.getWidth(), c.getY());
					break;

				case 2 :
					locs[i++] = new Point(c.getX(), ys);
					break;

				case 3 :
					locs[i++] = new Point(c.getX(), ye - c.getHeight());
					break;
			}
		}

		Action action = frame.getDesigner().getActionManager().getAction("OrderComponents");
		action.putValue("Cont", this);
		action.putValue("Components", comps);
		action.putValue("Locations", locs);
		action.actionPerformed(null);
	}

	public final void changeTabbedPane(Component comp, int mode) {

		JPanel panel = null;

		boolean add = mode == 1 || mode == 2;
		int index = -1;

		if (add) {
			JTabbedPane tp = (JTabbedPane) comp;
			panel = new JPanel(null);
			setComponentName(null, panel, tp.getName());
			index = mode == 1 ? tp.getComponentCount() : tp.getSelectedIndex();
		}

		if (mode == 4) {
			return;
		}

		DesignerContainer cont = (DesignerContainer) findDesigner(comp);

		Action action = frame.getDesigner().getActionManager().getAction("ChangeTabbedPane");
		action.putValue("Cons", new DesignerConstraints(null, comp, cont));
		action.putValue("Comp", panel);
		action.putValue("Title", "New Tab");
		action.putValue("Index", index);
		action.putValue("Add", add);
		action.actionPerformed(null);
	}

	private void selectTabbedPane(Component comp) {

		Component p1 = comp;
		Component p2 = comp.getParent();

		while (p2 != null) {
			if (p2 instanceof JTabbedPane) {
				break;
			}
			p1 = p2;
			p2 = p2.getParent();
		}
		if (p2 != null) {
			JTabbedPane tp = (JTabbedPane) p2;
			tp.setSelectedComponent(p1);
		}
	}

        private void deleteSplitPaneComponent(Component comp) {

                JSplitPane sp = (JSplitPane) comp.getParent();
                int mode = comp == sp.getLeftComponent() ? 1 : 2;

                DesignerContainer cont = (DesignerContainer) findDesigner(comp);

                Action action = frame.getDesigner().getActionManager().getAction("ChangeSplitPane");
                action.putValue("Cons", new DesignerConstraints(null, sp, cont));
                action.putValue("Comp", comp);
                action.putValue("Mode", mode);
                action.putValue("Add", Boolean.FALSE);
                action.actionPerformed(null);
        }

	public void setComponentName(Object src, Component comp, String parentName) {
		String compName = comp.getName();
		if (src != null && compName == null) {
			compName = CellsUtil.getInstanceName(src, comp);
		}
		if (parentName.equals("contentPane")) {
			parentName = null;
		}
		setComponentName(src, comp, compName, parentName);
	}

	private void setComponentName(Object src, Component comp, String compName, String parentName) {

		StringBuffer name = new StringBuffer();

		if (parentName != null) {
			name.append(parentName + "_");
		}

		if (comp instanceof JLabel) {
			iLab = calcName("label", iLab, compName);
			name.append("label" + iLab);
		} else if (comp instanceof JButton) {
			iBut = calcName("button", iBut, compName);
			name.append("button" + iBut);
		} else if (comp instanceof JRadioButton) {
			iRad = calcName("radiobutton", iRad, compName);
			name.append("radiobutton" + iRad);
		} else if (comp instanceof JCheckBox) {
			iChk = calcName("checkbox", iChk, compName);
			name.append("checkbox" + iChk);
		} else if (comp instanceof JToggleButton) {
			iTbt = calcName("togglebutton", iTbt, compName);
			name.append("togglebutton" + iTbt);
		} else if (comp instanceof JPasswordField) {
			iPas = calcName("passwordfield", iPas, compName);
			name.append("passwordfield" + iPas);
		} else if (comp instanceof JTextField) {
			iTxt = calcName("textfield", iTxt, compName);
			name.append("textfield" + iTxt);
		} else if (comp instanceof JComboBox) {
			iCom = calcName("combobox", iCom, compName);
			name.append("combobox" + iCom);
		} else if (comp instanceof JSpinner) {
			iSpi = calcName("spinner", iSpi, compName);
			name.append("spinner" + iSpi);
		} else if (comp instanceof JTextArea) {
			iTar = calcName("textarea", iTar, compName);
			name.append("textarea" + iTar);
		} else if (comp instanceof JSeparator) {
			iSep = calcName("separator", iSep, compName);
			name.append("separator" + iSep);
		} else if (comp instanceof JList) {
			iLst = calcName("list", iLst, compName);
			name.append("list" + iLst);
		} else if (comp instanceof JTable) {
			iTab = calcName("table", iTab, compName);
			name.append("table" + iTab);
		} else if (comp instanceof JTree) {
			iTre = calcName("tree", iTre, compName);
			name.append("tree" + iTre);
		} else if (comp instanceof JSlider) {
			iSli = calcName("slider", iSli, compName);
			name.append("slider" + iSli);
		} else if (comp instanceof JProgressBar) {
			iPba = calcName("progressbar", iPba, compName);
			name.append("progressbar" + iPba);
		} else if (comp instanceof JCalendar) {
			iCal = calcName("calendar", iCal, compName);
			name.append("calendar" + iCal);
		} else if (comp instanceof JTime) {
			iTim = calcName("time", iTim, compName);
			name.append("time" + iTim);
		} else if (comp instanceof JPanel) {
			iPan = calcName("panel", iPan, compName);
			name.append("panel" + iPan);

			String nm = compName == null ? name.toString() : compName;

			JPanel pa = (JPanel) comp;
			for (Component co : pa.getComponents()) {
				setComponentName(src, co, nm);
			}
		} else if (comp instanceof JScrollPane) {
			iScr = calcName("scrollpane", iScr, compName);
			name.append("scrollpane" + iScr);

			JScrollPane sp = (JScrollPane) comp;
			if (sp.getViewport().getComponentCount() > 0) {
				String nm = compName == null ? name.toString() : compName;
				setComponentName(src, sp.getViewport().getComponent(0), nm);
			}
		} else if (comp instanceof JTabbedPane) {
			iTpa = calcName("tabbedpane", iTpa, compName);
			name.append("tabbedpane" + iTpa);

			String nm = compName == null ? name.toString() : compName;
			JTabbedPane tp = (JTabbedPane) comp;
			for (Component co : tp.getComponents()) {
				setComponentName(src, co, nm);
			}
		} else if (comp instanceof JSplitPane) {
			iSpl = calcName("splitpane", iSpl, compName);
			name.append("splitpane" + iSpl);

			JSplitPane sp = (JSplitPane) comp;
			String nm = compName == null ? name.toString() : compName;

			if (sp.getLeftComponent() != null) {
				setComponentName(src, sp.getLeftComponent(), nm);
			}
			if (sp.getRightComponent() != null) {
				setComponentName(src, sp.getRightComponent(), nm);
			}
			if (sp.getTopComponent() != null) {
				setComponentName(src, sp.getTopComponent(), nm);
			}
			if (sp.getBottomComponent() != null) {
				setComponentName(src, sp.getBottomComponent(), nm);
			}
		} else {
			iBea = calcName("bean", iBea, compName);
			name.append("bean" + iBea);
		}

		comp.setName(compName == null ? name.toString() : compName);
	}

	private int calcName(String prefStr, int prefNum, String name) {
		if (name != null) {
			do {
				String s = prefStr + prefNum++;
				if (!name.equals(s)) {
					return prefNum;
				}
			} while (true);
		}
		return prefNum + 1;
	}

	public void addDesignerListener(DesignerListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeDesignerListener(DesignerListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	private void fireSelectionChanged(DesignerConstraints cons) {
		for (DesignerListener listener : listeners) {
			listener.selectionChanged(cons);
		}
	}

	//===== Propertysheet interfaces

	public abstract void updateSheets();

	public abstract JPanel createRowSheet();

	public abstract JPanel createColumnSheet();

	public abstract void updateLayout(Object... arg);

	public abstract Object[] getLayoutProperty();

	public abstract Property[] getConstraintsProperties(Component comp);

	public abstract boolean isConstraintsProperty(Component comp, String name);

	public abstract Object changeConstraintsProperty(Component comp, String name, Object value);
}
