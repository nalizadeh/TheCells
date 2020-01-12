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
import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerDragListener;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import sun.swing.DefaultLookup;

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
public class InspectorPanel extends JPanel implements TreeSelectionListener {

	private CellsDesigner designer;
	private JTree tree;
	private DragSource dragSource;

	private boolean selectAdjusting = false;

	public InspectorPanel(CellsDesigner dsg) {
		super(new CellsLayout(new double[][] { { CellsLayout.FILL }, { CellsLayout.FILL } }));

		this.designer = dsg;

		tree = new JTree();
		tree.setModel(null);
		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new InspectorTreeRenderer());
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					openPopup(e.getX(), e.getY());
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					openPopup(e.getX(), e.getY());
				}
			}
		});

		dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(tree,
				DnDConstants.ACTION_COPY_OR_MOVE, new DesignerDragListener(designer));

		add(new JScrollPane(tree), "0,0");
	}

	private void openPopup(int x, int y) {
		tree.setSelectionPath(tree.getPathForLocation(x, y));

		DesignerFrame df = designer.getDesktop().getDesigner();
		if (df != null) {
			DesignerConstraints cons = df.getContainer().getSelectedDesignerConstraints();
			
			designer.getPopupMenu().showPopup(tree,
					cons == null ? null : new Component[] { cons.getComponent() }, x, y);
		}
	}

	public void update() {
		selectAdjusting = true;

		tree.removeAll();
		tree.setModel(null);

		DesignerFrame df = designer.getDesktop().getDesigner();

		if (df != null) {
			DefaultMutableTreeNode top = new DefaultMutableTreeNode(df);
			DefaultMutableTreeNode con = new DefaultMutableTreeNode(df.getContainer());
			top.add(con);
			createCompTree(df.getContainer(), con);
			tree.setModel(new DefaultTreeModel(top));
			tree.setExpandsSelectedPaths(true);

			DesignerConstraints cons = df.getContainer()
					.getSelectedDesignerConstraints();

			if (cons != null && cons.getComponent() != null) {

				DefaultMutableTreeNode node = findNode((TreeNode) tree
						.getModel().getRoot(), cons.getComponent());

				if (node != null) {
					TreePath path = new TreePath(node.getPath());
					tree.setSelectionPath(path);
					tree.expandPath(path);
					tree.scrollPathToVisible(path);
				}

			} else {
				df.getContainer().selectDesignerConstraints(null);
				tree.clearSelection();
				tree.setSelectionRow(1);
				tree.expandRow(1);
			}
		}

		selectAdjusting = false;
	}

	private void createCompTree(Container container, DefaultMutableTreeNode parent) {
		Component[] comps = container.getComponents();
		for (int i = 0; i < comps.length; i++) {
			if (CellsUtil.isGuiComponent(comps[i])) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(comps[i]);

				if (comps[i] instanceof JScrollPane) {
					JScrollPane sp = (JScrollPane) comps[i];
					if (sp.getViewport().getComponentCount() > 0) {
						node.add(new DefaultMutableTreeNode(sp.getViewport().getComponent(0)));
					}
				} else if (CellsUtil.isContainer(comps[i])) {
					createCompTree((Container) comps[i], node);
				}
				parent.add(node);
			}
		}
	}

	private DefaultMutableTreeNode findNode(TreeNode parent, Object comp) {
		if (parent instanceof DefaultMutableTreeNode) {
			if (comp.equals(((DefaultMutableTreeNode) parent).getUserObject())) {
				return (DefaultMutableTreeNode) parent;
			}
			if (!parent.isLeaf()) {
				Enumeration en = parent.children();
				while (en.hasMoreElements()) {
					DefaultMutableTreeNode node = findNode((TreeNode) en.nextElement(), comp);
					if (node != null) {
						return node;
					}
				}
			}
		}
		return null;
	}

	private void expandAll(TreeNode node) {
		if (!node.isLeaf()) {
			Enumeration en = node.children();
			while (en.hasMoreElements()) {
				expandAll((TreeNode) en.nextElement());
			}
			tree.expandPath(new TreePath(((DefaultMutableTreeNode) node).getPath()));
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		if (selectAdjusting) {
			return;
		}

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		
		if (node != null) {
			DesignerFrame df = designer.getDesktop().getDesigner();
			DesignerConstraints cons = null;
			Object o = node.getUserObject();

			int n = -1;
			if (o instanceof DesignerFrame) {
				n = 0;

			} else if (o instanceof DesignerContainer && o == df.getContainer()) {
				n = 1;

			} else if (o instanceof Component) {
				cons = new DesignerConstraints(null, (Component) o,
						(DesignerContainer) df.getContainer().findDesigner((Component) o));
			}

			df.getContainer().selectDesignerConstraints(cons);
			designer.update();

			if (n != -1) {
				selectAdjusting = true;
				tree.setSelectionRow(n);
				df.getContainer().selectDesignerConstraints(null);
				selectAdjusting = false;
			}
		}
	}

	class InspectorTreeRenderer extends DefaultTreeCellRenderer {

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
			
			setBackgroundNonSelectionColor(DefaultLookup.getColor(this, ui, "Tree.textBackground", Color.WHITE));

			if (value != null && value instanceof DefaultMutableTreeNode) {
				Object comp = ((DefaultMutableTreeNode) value).getUserObject();

				if (comp instanceof DesignerFrame) {
					setIcon(CellsUtil.getImage("RootPane"));
					setForeground(sel ? Color.WHITE : Color.BLUE.darker());

				} else if (comp instanceof DesignerContainer) {
					setIcon(CellsUtil.getImage("ContentPane"));
					setForeground(sel ? Color.WHITE : Color.BLUE.darker());

				} else if (comp instanceof Component) {
					ImageIcon ic = CellsUtil.getComponentIcon(comp);

					if (ic != null) {
						String s = ((Component) comp).getName();
						setIcon(ic);
						setToolTipText(comp.getClass().getSimpleName());
						setText(s == null || s.equals("") ? comp.toString() : s);
						setForeground(sel ? Color.WHITE : Color.BLACK);
					} else {
						setText(comp.getClass().getName());
						setForeground(sel ? Color.WHITE : Color.LIGHT_GRAY);
					}

				} else {
					setForeground(sel ? Color.WHITE : Color.LIGHT_GRAY);
				}
			}
			return this;
		}
	}
}
