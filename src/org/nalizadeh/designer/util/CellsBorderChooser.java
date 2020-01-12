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

import org.nalizadeh.designer.property.editors.ColorEditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
public class CellsBorderChooser extends CellsDialog implements ItemListener, ActionListener,
	ChangeListener, PopupMenuListener
{
	private static final NullBorder NULL = new NullBorder();

	private static final Border[] borders =
		{
			NULL, BorderFactory.createEmptyBorder(), BorderFactory.createCompoundBorder(),
			BorderFactory.createEtchedBorder(), BorderFactory.createLoweredBevelBorder(),
			BorderFactory.createRaisedBevelBorder(), BorderFactory.createTitledBorder("Title"),
			BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
			BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY)
		};

	private static final String[] names =
		{
			"", "EmptyBorder", "CompoundBorder", "EtchedBorder", "LoweredBevelBorder",
			"RaisedBevelBorder", "TitledBorder", "LineBorder", "MatteBorder"
		};

	private JComboBox coBorder;
	private JPanel previewPanel;

	private JPanel paTitle;
	private JLabel lbTitle;
	private JTextField tfTitle;

	private JPanel paInsets;
	private JButton btInsets;
	private JLabel lbInsets;
	private JTextField tfInsets;
	private CellsInsetsChooser inschooser = new CellsInsetsChooser();
	private JButton btColor1;
	private ColorEditor.ColorIcon cicon1;

	private JPanel paTicknes;
	private JLabel lbTicknes;
	private JSpinner spTicknes;
	private JButton btColor2;
	private ColorEditor.ColorIcon cicon2;

	private JPanel proppanel;
	private JPanel userpanel;

	private Border border;

	public CellsBorderChooser(Frame parent, Border border) {
		super(parent, "Border", false, true);
		this.border = border;
		initClass();
	}

	public CellsBorderChooser(Dialog parent, Border border) {
		super(parent, "Border", false, true);
		this.border = border;
		initClass();
	}

	private void initClass() {
		setSize(500, 300);

		coBorder = new JComboBox(names);
		coBorder.addItemListener(this);

		lbTitle = new JLabel("Title:");
		tfTitle = new JTextField();
		tfTitle.setPreferredSize(new Dimension(80, 20));
		tfTitle.addActionListener(this);
		paTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		paTitle.add(lbTitle);
		paTitle.add(tfTitle);

		lbInsets = new JLabel("Insets:");
		tfInsets = new JTextField();
		tfInsets.setPreferredSize(new Dimension(80, 20));
		btInsets = new JButton("...");
		btInsets.setPreferredSize(new Dimension(20, 20));
		btInsets.addActionListener(this);
		cicon1 = new ColorEditor.ColorIcon(Color.BLACK, 10, 10);
		btColor1 = new JButton(cicon1);
		btColor1.setPreferredSize(new Dimension(20, 20));
		btColor1.addActionListener(this);
		paInsets = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		paInsets.add(lbInsets);
		paInsets.add(tfInsets);
		paInsets.add(btInsets);
		paInsets.add(btColor1);

		lbTicknes = new JLabel("Tickness:");
		spTicknes = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
		spTicknes.addChangeListener(this);
		cicon2 = new ColorEditor.ColorIcon(Color.BLACK, 10, 10);
		btColor2 = new JButton(cicon2);
		btColor2.setPreferredSize(new Dimension(20, 20));
		btColor2.addActionListener(this);
		paTicknes = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		paTicknes.add(lbTicknes);
		paTicknes.add(spTicknes);
		paTicknes.add(btColor2);

		proppanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
		proppanel.add(new JLabel("Borders:"));
		proppanel.add(coBorder);

		JLabel lb = new JLabel("Preview Panel");
		lb.setHorizontalAlignment(JLabel.CENTER);
		lb.setVerticalAlignment(JLabel.CENTER);

		previewPanel = new JPanel(new BorderLayout());
		previewPanel.add(lb, BorderLayout.CENTER);
		previewPanel.setBorder(border);

		JPanel pn = new JPanel();
		JPanel ps = new JPanel();
		JPanel pw = new JPanel();
		JPanel pe = new JPanel();
		pn.setPreferredSize(new Dimension(100, 20));
		ps.setPreferredSize(new Dimension(10, 20));
		pw.setPreferredSize(new Dimension(20, 100));
		pe.setPreferredSize(new Dimension(20, 100));

		JPanel p = new JPanel(new BorderLayout());
		p.add(pn, BorderLayout.NORTH);
		p.add(ps, BorderLayout.SOUTH);
		p.add(pw, BorderLayout.WEST);
		p.add(pe, BorderLayout.EAST);
		p.add(previewPanel, BorderLayout.CENTER);

		userpanel = new JPanel(new BorderLayout());
		userpanel.add(proppanel, BorderLayout.NORTH);
		userpanel.add(p, BorderLayout.CENTER);
		setUserPanel(userpanel);

		setBorder(border);
	}

	public Border getBorder() {
		return border;
	}

	public void setBorder(Border border) {
		coBorder.setSelectedItem(getBorderName(border));
		update(border, true);
	}

	public void itemStateChanged(ItemEvent e) {
		update(borders[coBorder.getSelectedIndex()], true);
	}

	public void stateChanged(ChangeEvent e) {
		actionPerformed(new ActionEvent(e.getSource(), 0, ""));
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == tfTitle) {
			((TitledBorder) border).setTitle(tfTitle.getText());

		} else if (ae.getSource() == btInsets) {
			int x = btInsets.getLocationOnScreen().x - getX();
			int y = btInsets.getLocationOnScreen().y - getY() + 20;
			inschooser.open(this, border.getBorderInsets(previewPanel), x, y);

		} else if (ae.getSource() == spTicknes) {
			Color c = ((LineBorder) border).getLineColor();
			Border nb = BorderFactory.createLineBorder(c, (Integer) spTicknes.getValue());
			update(nb, false);

		} else if (ae.getSource() == btColor1) {
			Window window = SwingUtilities.windowForComponent(this);
			Color c1 = ((MatteBorder) border).getMatteColor();
			Color c2 = JColorChooser.showDialog(window, "pick a color", c1);
			if (c2 != null) {
				Insets in = ((MatteBorder) border).getBorderInsets();
				Border nb =
					BorderFactory.createMatteBorder(in.top, in.left, in.bottom, in.right, c2);
				update(nb, false);
			}
		} else if (ae.getSource() == btColor2) {
			Window window = SwingUtilities.windowForComponent(this);
			Color c1 = ((LineBorder) border).getLineColor();
			Color c2 = JColorChooser.showDialog(window, "pick a color", c1);
			if (c2 != null) {
				int t = ((LineBorder) border).getThickness();
				Border nb = BorderFactory.createLineBorder(c2, t);
				update(nb, false);
			}
		}
		repaint();
	}

	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	}

	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		Insets in = inschooser.getValue();
		Border nb;
		if (border instanceof MatteBorder) {
			Color c = ((MatteBorder) border).getMatteColor();
			nb = BorderFactory.createMatteBorder(in.top, in.left, in.bottom, in.right, c);
		} else {
			nb = BorderFactory.createEmptyBorder(in.top, in.left, in.bottom, in.right);
		}
		update(nb, false);
	}

	public void popupMenuCanceled(PopupMenuEvent e) {
	}

	public Icon getActivityIcon() {
		return CellsUtil.getImage("Border32");
	}

	public String getActivityTitle() {
		return "Choose a Border";
	}

	public String getActivityDescription() {
		return "Choose a border for the component.";
	}

	protected void doCancelAction() {
		border = null;
	}

	private void update(Border b, boolean prop) {

		border = b;
		previewPanel.setBorder(b);
		if (prop) {
			proppanel.remove(paTitle);
			proppanel.remove(paInsets);
			proppanel.remove(paTicknes);
		}

		if (b == null) {
			return;
		}
		borders[coBorder.getSelectedIndex()] = b;

		if (b instanceof TitledBorder) {
			if (prop) {
				proppanel.add(paTitle);
			}
			tfTitle.setText(((TitledBorder) b).getTitle());

		} else if (b instanceof LineBorder) {
			if (prop) {
				proppanel.add(paTicknes);
			}
			spTicknes.setValue(((LineBorder) b).getThickness());
			cicon2.setColor(((LineBorder) b).getLineColor());

		} else if (b instanceof MatteBorder) {
			if (prop) {
				proppanel.add(paInsets);
                                btColor1.setVisible(true);
			}
			Insets in = ((MatteBorder) b).getBorderInsets();
			tfInsets.setText(in.top + "," + in.left + "," + in.bottom + "," + in.right);
			cicon1.setColor(((MatteBorder) b).getMatteColor());

		} else if (b instanceof EmptyBorder) {
			if (prop) {
				proppanel.add(paInsets);
                                btColor1.setVisible(false);
			}
			Insets in = ((EmptyBorder) b).getBorderInsets();
			tfInsets.setText(in.top + "," + in.left + "," + in.bottom + "," + in.right);
		}

		repaint();
	}

	public static String getBorderName(Border border) {
		if (border != null) {
			for (int i = 1; i < borders.length; i++) {
				if (border.getClass().equals(borders[i].getClass())) {
					return names[i];
				}
			}
		}
		return "";
	}

	public static Border showDialog(Component parent, Border border) {
		CellsBorderChooser bc;
		Window window = SwingUtilities.windowForComponent(parent);
		if (window instanceof Frame) {
			bc = new CellsBorderChooser((Frame) window, border);
		} else {
			bc = new CellsBorderChooser((Dialog) window, border);
		}

		bc.setVisible(true);
		return bc.getBorder();
	}

	public static class NullBorder implements Border {

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(0, 0, 0, 0);
		}

		public boolean isBorderOpaque() {
			return false;
		}
	}
}
