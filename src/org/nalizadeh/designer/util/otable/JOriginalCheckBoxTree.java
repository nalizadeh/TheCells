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

package org.nalizadeh.designer.util.otable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

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
public class JOriginalCheckBoxTree extends JTree {

	private static final long serialVersionUID = 1L;
	
	private static final int NOT_SELECTED = 0;
	private static final int PART_SELECTED = 1;
	private static final int ALL_SELECTED = 2;

	private Map<TreeNode, Integer> groups = new HashMap<TreeNode, Integer>();
	private ArrayList<TreeNode> items = new ArrayList<TreeNode>();

	private List<Object> listeners = new ArrayList<Object>();

	private TreePath selectionPath = null;
	private boolean showCheckBox = true;
	private boolean adjust = false;

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public JOriginalCheckBoxTree() {
		super();
		init();
	}

	public JOriginalCheckBoxTree(Object[] value) {
		super(value);
		init();
	}

	public JOriginalCheckBoxTree(Vector<Object> value) {
		super(value);
		init();
	}

	public JOriginalCheckBoxTree(Hashtable<Object, Object> value) {
		super(value);
		init();
	}

	public JOriginalCheckBoxTree(TreeNode root) {
		super(root);
		init();
	}

	public JOriginalCheckBoxTree(TreeNode root, boolean ac) {
		super(root, ac);
		init();
	}

	public JOriginalCheckBoxTree(TreeModel newModel) {
		super(newModel);
		init();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void setModel(TreeModel model) {
		adjust = true;
		super.setModel(model);
		adjust = false;
		model.addTreeModelListener(new ChechBoxTreeModelListener());
		preapareNodes();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void init() {
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getModel().addTreeModelListener(new ChechBoxTreeModelListener());
		addTreeSelectionListener(new CheckBoxTreeSelectionListener());
		addKeyListener(new CheckBoxTreeKeyAdapter());
		addMouseListener(new CheckBoxTreeMouseAdapter());
		setRootVisible(true);
		setShowsRootHandles(true);
		setCellRenderer(new CheckBoxTreeCellRenderer());
		preapareNodes();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void setShowCheckBox(boolean show) {
		showCheckBox = show;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean getShowCheckBox() {
		return showCheckBox;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean getShowCheckBox(Object node) {
		return true;
	}

	/**
	 * Part-Selection retrieves true also
	 *
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public boolean isChecked(TreeNode node) {
		Integer m = groups.get(node);
		return items.contains(node) || (m != null && !m.equals(NOT_SELECTED));
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void checkAll(boolean doCheck) {
		if (adjust && !doCheck) {
			return;
		}

		clearChecks();
		if (doCheck) {
			doCheck(ALL_SELECTED, (TreeNode) getModel().getRoot(), null);
		}
		preapareNodes();
		revalidate();
		informListeners(null);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void check(TreeNode node, boolean check) {
		if (check) {
			check(node);
		} else {
			uncheck(node);
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void check(TreeNode node) {
		if (node.isLeaf()) {
			items.add(node);
		} else {
			doCheck(ALL_SELECTED, node, null);
		}

		preapareNodes();
		informListeners(node);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void uncheck(TreeNode node) {
		if (node.isLeaf()) {
			items.remove(node);
		} else {
			doCheck(NOT_SELECTED, node, null);
		}

		preapareNodes();
		informListeners(node);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void check(ArrayList<TreeNode> nodes) {
		clearChecks();
		for (int i = 0; i < nodes.size(); i++) {
			TreeNode node = nodes.get(i);
			if (node.isLeaf()) {
				items.add(node);
			} else {
				doCheck(ALL_SELECTED, node, null);
			}
		}
		preapareNodes();
		revalidate();
		informListeners(null);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public TreeNode getCheckedNodes() {
		return doGetCheckedNodes((DefaultMutableTreeNode) getModel().getRoot());
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private TreeNode doGetCheckedNodes(DefaultMutableTreeNode node) {

		Integer m = groups.get(node);

		if (m == null || m.equals(NOT_SELECTED)) {
			return null;
		}

		DefaultMutableTreeNode grp = new DefaultMutableTreeNode(node.getUserObject());
		@SuppressWarnings("rawtypes")
		Enumeration en = node.children();

		while (en.hasMoreElements()) {
			DefaultMutableTreeNode item = (DefaultMutableTreeNode) en.nextElement();
			if (item.isLeaf() && items.contains(item)) {
				DefaultMutableTreeNode newItem = new DefaultMutableTreeNode(item.getUserObject());
				grp.add(newItem);
			} else if (!item.isLeaf()) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode) doGetCheckedNodes(item);
				if (child != null) {
					grp.add(child);
				}
			}
		}
		return grp;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void removeChecks() {

		adjust = true;
		DefaultTreeModel model = (DefaultTreeModel) getModel();
		List<Object> v1 = new ArrayList<Object>();
		List<Object> v2 = new ArrayList<Object>();

		for (Iterator<TreeNode> it = items.iterator(); it.hasNext();) {
			MutableTreeNode itm = (MutableTreeNode) it.next();
			model.removeNodeFromParent(itm);
			v1.add(itm);
		}

		for (Iterator<TreeNode> it = groups.keySet().iterator(); it.hasNext();) {
			MutableTreeNode grp = (MutableTreeNode) it.next();
			Integer sm = groups.get(grp);
			if (sm.equals(ALL_SELECTED) && grp.getParent() != null) {
				model.removeNodeFromParent(grp);
				v2.add(sm);
			}
		}

		for (int i = 0; i < v1.size(); i++) {
			items.remove(v1.get(i));
		}

		for (int i = 0; i < v2.size(); i++) {
			groups.remove(v2.get(i));
		}

		adjust = false;
		preapareNodes();
		model.reload();
		informListeners(null);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void clearChecks() {
		groups.clear();
		items.clear();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void preapareNodes() {
		if (groups != null && items != null) {
			doPreapareNodes((TreeNode) getModel().getRoot());
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void doPreapareNodes(TreeNode node) {
		int not = 0;
		int all = 0;
		int con = 0;
		@SuppressWarnings("rawtypes")
		Enumeration en = node.children();
		while (en.hasMoreElements()) {
			TreeNode no = (TreeNode) en.nextElement();
			con++;
			if (no.isLeaf()) {
				if (items.contains(no)) {
					all++;
				} else {
					not++;
				}
			} else {
				doPreapareNodes(no);
				Integer m = groups.get(no);
				if (m.equals(NOT_SELECTED)) {
					not++;
				}

				if (m.equals(ALL_SELECTED)) {
					all++;
				}
			}
		}

		Integer m = not == con ? NOT_SELECTED : (all == con ? ALL_SELECTED : PART_SELECTED);
		groups.put(node, m);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public List<TreeNode> getCheckedGroups() {
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for (Iterator<TreeNode> it = groups.keySet().iterator(); it.hasNext();) {
			TreeNode grp = it.next();
			if (!groups.get(grp).equals(NOT_SELECTED)) {
				nodes.add(grp);
			}
		}
		return nodes;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public List<TreeNode> getCheckedItems() {
		return items;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public List<TreeNode> getAllChecked() {
		List<TreeNode> nodes = getCheckedGroups();
		nodes.addAll(items);
		return nodes;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public int getCheckedCount() {
		return getCheckedGroups().size() + getCheckedItems().size();
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void addCheckBoxTreeListener(CheckBoxTreeListener listener) {
		listeners.add(listener);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public void removeCheckBoxTreeListener(CheckBoxTreeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void doCheck(Integer mode, TreeNode group, TreeNode item) {
		groups.put(group, mode);
		@SuppressWarnings("rawtypes")
		Enumeration en = group.children();

		switch (mode) {

			case NOT_SELECTED :
				while (en.hasMoreElements()) {
					TreeNode it = (TreeNode) en.nextElement();
					if (it.isLeaf()) {
						if (items.contains(it)) {
							items.remove(it);
						}
					} else {
						doCheck(mode, it, null);
					}
				}
				break;

			case PART_SELECTED :
				if (item == null) {
					doCheck(NOT_SELECTED, group, null);
				} else {
					int n = 0, k = 0;
					while (en.hasMoreElements()) {
						TreeNode it = (TreeNode) en.nextElement();
						if (it.isLeaf()) {
							if (items.contains(it)) {
								n++;
							}
						} else {
							Integer m = groups.get(it);
							if (m.equals(ALL_SELECTED)) {
								n++;
							} else if (m.equals(PART_SELECTED)) {
								k = 1;
							}
						}
					}

					if (items.contains(item)) {
						if (n == 1 && k == 0) {
							doCheck(NOT_SELECTED, group, null);
						}
						items.remove(item);

					} else {
						if (n == group.getChildCount() - 1) {
							doCheck(ALL_SELECTED, group, null);
						} else {
							items.add(item);
						}
					}
				}

				break;

			case ALL_SELECTED :

				while (en.hasMoreElements()) {
					TreeNode it = (TreeNode) en.nextElement();
					if (it.isLeaf()) {
						if (!items.contains(it)) {
							items.add(it);
						}
					} else {
						doCheck(mode, it, null);
					}
				}
				break;
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void checkParents(TreeNode group) {

		TreeNode parent = group.getParent();
		if (parent == null) {
			return;
		}
		int not = 0;
		int all = 0;
		int count = 0;

		@SuppressWarnings("rawtypes")
		Enumeration en = parent.children();
		while (en.hasMoreElements()) {
			TreeNode it = (TreeNode) en.nextElement();
			count++;
			if (it.isLeaf()) {
				if (items.contains(it)) {
					all++;
				} else {
					not++;
				}
			} else {
				Integer m = groups.get(it);
				if (m.equals(NOT_SELECTED)) {
					not++;
				}
				if (m.equals(ALL_SELECTED)) {
					all++;
				}
			}
		}

		Integer m = not == count ? NOT_SELECTED : (all == count ? ALL_SELECTED : PART_SELECTED);
		groups.put(parent, m);
		checkParents(parent);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	protected boolean isEnabled(TreeNode node) {
		return true;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void changeCheck(TreePath path) {

		TreeNode node = (TreeNode) path.getLastPathComponent();

		if (!isEnabled(node)) {
			return;
		}

		TreeNode item = null;
		TreeNode group = null;

		if (node.isLeaf()) {
			item = node;
		}

		group = item == null ? node : item.getParent();
		if (group == null) {
			return;
		}

		Integer m = groups.get(group);

		switch (m) {

			case NOT_SELECTED :
				if (item != null) {
					doCheck(PART_SELECTED, group, item);
				} else {
					doCheck(ALL_SELECTED, group, null);
				}
				break;

			case ALL_SELECTED :

				if (item != null) {
					doCheck(PART_SELECTED, group, item);
				} else {
					doCheck(NOT_SELECTED, group, null);
				}

				break;

			case PART_SELECTED :
				doCheck(PART_SELECTED, group, item);
				break;

		}

		checkParents(group);
		revalidate();
		informListeners(node);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	private void informListeners(TreeNode node) {
		for (int i = 0; i < listeners.size(); i++) {
			CheckBoxTreeListener tl = (CheckBoxTreeListener) listeners.get(i);
			tl.checkBoxValueChanged(this, node);
		}
	}

	/**
	 * CheckBoxTreeKeyAdapter
	 */
	private class CheckBoxTreeKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				if (!isSelectionEmpty()) {
					selectionPath = getSelectionPath();
				}
				changeCheck(selectionPath);
			}
		}
	}

	/**
	 * CheckBoxTreeMouseAdapter
	 */
	private class CheckBoxTreeMouseAdapter extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			TreePath path = getPathForLocation(e.getX(), e.getY());
			if (path != null && SwingUtilities.isLeftMouseButton(e)) {
				changeCheck(path);
			}
		}
	}

	/**
	 * CheckBoxTreeSelectionListener
	 */
	private class CheckBoxTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			if (e.getNewLeadSelectionPath() == null) {
				setSelectionPath(selectionPath);
			}
		}
	}

	/**
	 * CheckBoxTreeListener
	 */
	public static interface CheckBoxTreeListener extends EventListener {
		void checkBoxValueChanged(JTree tree, TreeNode node);
	}

	/**
	 * ChechBoxTreeModelListener
	 */
	private class ChechBoxTreeModelListener implements TreeModelListener {

		public void treeNodesChanged(TreeModelEvent e) {
			checkAll(false);
		}

		public void treeNodesInserted(TreeModelEvent e) {
			checkAll(false);
		}

		public void treeNodesRemoved(TreeModelEvent e) {
			checkAll(false);
		}

		public void treeStructureChanged(TreeModelEvent e) {
			checkAll(false);
		}

	}

	/*
	 * Icons zur Darstellung von CheckBox
	 *
	 */

//	private static final Icon checkedIcon1 = new CheckBoxIcon(1, true);
//	private static final Icon uncheckedIcon1 = new CheckBoxIcon(1, false);
//	private static final Icon partcheckedIcon1 = new CheckBoxIcon(2, true);

	private static final Icon checkedIcon = JOriginalTable.getImage("checkboxOn.gif");
	private static final Icon uncheckedIcon = JOriginalTable.getImage("checkboxOff.gif");
	private static final Icon partcheckedIcon = JOriginalTable.getImage("checkboxPartOn.gif");

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	protected Icon getCheckedIcon(Icon icon) {
		if (icon == null) {
			return checkedIcon;
		}
		return new DoubleIcon(checkedIcon, icon);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	protected Icon getUncheckedIcon(Icon icon) {
		if (icon == null) {
			return uncheckedIcon;
		}
		return new DoubleIcon(uncheckedIcon, icon);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	protected Icon getPartCheckedIcon(Icon icon) {
		if (icon == null) {
			return partcheckedIcon;
		}
		return new DoubleIcon(partcheckedIcon, icon);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @return
	 *
	 * @see
	 */
	public Icon createDoubleIcon(Object value, boolean leaf, Icon icon1) {

		Icon icon2 = icon1;

		// should checkbox icons be displayed ?
		if (getShowCheckBox() && getShowCheckBox(value)) {

			// do not display icons in internal adjusting mode
			if (!adjust) {

				if (leaf) {
					if (items.contains(value)) {
						icon2 = getCheckedIcon(icon1);
					} else {
						icon2 = getUncheckedIcon(icon1);
					}
				} else {
					Integer m = groups.get(value);

					if (m != null) {
						switch (m) {

							case (ALL_SELECTED) :
								icon2 = getCheckedIcon(icon1);
								break;

							case NOT_SELECTED :
								icon2 = getUncheckedIcon(icon1);
								break;

							case PART_SELECTED :
								icon2 = getPartCheckedIcon(icon1);
								break;
						}
					}
				}
			}
		}

		return icon2;
	}

	private class CheckBoxTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;
		
		private Icon selectedOpenIcon = getCheckedIcon(getOpenIcon());
		private Icon selectedClosedIcon = getCheckedIcon(getClosedIcon());
		private Icon unselectedOpenIcon = getUncheckedIcon(getOpenIcon());
		private Icon unselectedClosedIcon = getUncheckedIcon(getClosedIcon());
		private Icon partselectedOpenIcon = getPartCheckedIcon(getOpenIcon());
		private Icon partselectedClosedIcon = getPartCheckedIcon(getClosedIcon());

		public Component getTreeCellRendererComponent(
			JTree   tree,
			Object  value,
			boolean sel,
			boolean expanded,
			boolean leaf,
			int		row,
			boolean hasFocus
		) {
			JLabel label =
				(JLabel) super.getTreeCellRendererComponent(
					tree,
					value,
					sel,
					expanded,
					leaf,
					row,
					hasFocus
				);

			Object object = ((DefaultMutableTreeNode) value).getUserObject();
			label.setText(object == null ? "" : object.toString());

			// should icons be displayed ?
			if (!showCheckBox) {
				return label;
			}

			// do not display icons in internal adjusting mode
			if (adjust) {
				label.setIcon(unselectedClosedIcon);
				return label;
			}

			if (leaf) {
				if (items.contains(value)) {
					label.setIcon(checkedIcon);
				} else {
					label.setIcon(uncheckedIcon);
				}
			} else {
				Integer m = groups.get(value);
				if (m != null) {
					switch (m) {

						case ALL_SELECTED :
							if (expanded) {
								setIcon(selectedOpenIcon);
							} else {
								setIcon(selectedClosedIcon);
							}

							break;

						case NOT_SELECTED :
							setIcon(uncheckedIcon);
							if (expanded) {
								setIcon(unselectedOpenIcon);
							} else {
								setIcon(unselectedClosedIcon);
							}

							break;

						case PART_SELECTED :
							if (expanded) {
								setIcon(partselectedOpenIcon);
							} else {
								setIcon(partselectedClosedIcon);
							}
							break;
					}
				}
			}

			return label;
		}
	}

	// ========== Inner-Classess =========

	/**
	 * Class CheckBoxIcon
	 */
	protected static class CheckBoxIcon implements Icon {
		private static final int csize = 13;
		private int mode = 1;
		private boolean selected = false;

		public CheckBoxIcon(int mode, boolean selected) {
			this.mode = mode;
			this.selected = selected;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(new Color(0xC0C0C0));
			g.fill3DRect(x, y, csize, csize, false);
			g.setColor(new Color(0x808080));
			g.fill3DRect(x + 1, y + 1, csize - 2, csize - 2, false);
			g.setColor(new Color(mode == 1 ? 0xFFFFFF : 0xC0C0C0));
			g.fillRect(x + 2, y + 2, csize - 4, csize - 4);
			g.setColor(new Color(0x000000));
			if (selected) {
				g.drawLine(x + 9, y + 3, x + 9, y + 3);
				g.drawLine(x + 8, y + 4, x + 9, y + 4);
				g.drawLine(x + 7, y + 5, x + 9, y + 5);
				g.drawLine(x + 6, y + 6, x + 8, y + 6);
				g.drawLine(x + 3, y + 7, x + 7, y + 7);
				g.drawLine(x + 4, y + 8, x + 6, y + 8);
				g.drawLine(x + 5, y + 9, x + 5, y + 9);
				g.drawLine(x + 3, y + 5, x + 3, y + 5);
				g.drawLine(x + 3, y + 6, x + 4, y + 6);
			}
		}

		public int getIconWidth() {
			return csize;
		}

		public int getIconHeight() {
			return csize;
		}
	}

	/**
	 * Class DoubleIcon
	 */
	public static class DoubleIcon implements Icon {

		private Icon leftIcon = null;
		private Icon rightIcon = null;
		private int gap = 3;

		public DoubleIcon(Icon leftIcon, Icon rightIcon) {
			this.leftIcon = leftIcon;
			this.rightIcon = rightIcon;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			int leftIconHeight = leftIcon.getIconHeight();
			int rightIconHeight = rightIcon.getIconHeight();
			int leftYOffset = 0;
			int rightYOffset = 0;
			if (leftIconHeight < rightIconHeight) {
				leftYOffset = (rightIconHeight - leftIconHeight) / 2;
			}

			if (rightIconHeight < leftIconHeight) {
				rightYOffset = (leftIconHeight - rightIconHeight) / 2;
			}

			leftIcon.paintIcon(c, g, x, y + leftYOffset);
			rightIcon.paintIcon(c, g, x + leftIcon.getIconWidth() + gap, y + rightYOffset);
		}

		public int getIconWidth() {
			return leftIcon.getIconWidth() + gap + rightIcon.getIconWidth();
		}

		public int getIconHeight() {
			return Math.max(leftIcon.getIconHeight(), rightIcon.getIconHeight());
		}
	}
}
