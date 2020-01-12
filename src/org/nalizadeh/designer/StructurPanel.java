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
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import org.nalizadeh.designer.util.CellsUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
public class StructurPanel extends JPanel implements TreeSelectionListener {

	private CellsDesigner designer;
	private JTree tree;

	private boolean selectAdjusting = false;

	public StructurPanel(CellsDesigner dsg) {
		super(new CellsLayout(new double[][] { { CellsLayout.FILL }, { CellsLayout.FILL } }));

		this.designer = dsg;

		tree = new JTree();
		tree.setModel(null);
		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new StructurTreeRenderer());

		add(new JScrollPane(tree), "0,0");
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
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(comps[i]);

			if (comps[i] instanceof Container) {
				createCompTree((Container) comps[i], node);
			}
			parent.add(node);
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
			tree.expandPath(new TreePath(((DefaultMutableTreeNode) node)
					.getPath()));
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		if (selectAdjusting) {
			return;
		}

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
				.getLastSelectedPathComponent();
		
		if (node != null) {
			DesignerFrame df = designer.getDesktop().getDesigner();
			Object o = node.getUserObject();

			if (o instanceof Component) {
				Component c = (Component)o;
				if (CellsUtil.isGuiComponent(c)) {
					DesignerConstraints cons = new DesignerConstraints(null, (Component) o,
						(DesignerContainer) df.getContainer().findDesigner(c));
					df.getContainer().selectDesignerConstraints(cons);
					designer.update();
				}
			}
		}
	}

	class StructurTreeRenderer extends DefaultTreeCellRenderer {

		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

			if (value != null && value instanceof DefaultMutableTreeNode) {
				Object comp = ((DefaultMutableTreeNode) value).getUserObject();
				setText(comp.getClass().getName());
				if (comp instanceof Component) {
					if (!CellsUtil.isGuiComponent((Component)comp)) {
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
