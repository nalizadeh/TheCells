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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

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
public class CellsFontChooser extends CellsDialog implements ActionListener, ChangeListener {

	private static final String[] allFonts =
		GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

	private JComboBox fontName;
	private JCheckBox fontBold;
	private JCheckBox fontItalic;
	private JSpinner fontSize;
	private JLabel previewLabel;

	private SimpleAttributeSet attributes;
	private Font font;

	public CellsFontChooser(Frame parent, Font font) {
		super(parent, "Pick a Font", false, true);
		this.font = font;
		initClass();
	}

	public CellsFontChooser(Dialog parent, Font font) {
		super(parent, "Pick a Font", false, true);
		this.font = font;
		initClass();
	}

	private void initClass() {

		setSize(500, 300);

		attributes = new SimpleAttributeSet();

		JPanel fontPanel = new JPanel();
		fontName = new JComboBox(allFonts);
		fontName.setSelectedIndex(1);
		fontName.addActionListener(this);

		fontSize =
			new JSpinner(
				new SpinnerNumberModel(
					new Integer(0),
					new Integer(0),
					new Integer(74),
					new Integer(1)
				)
			);

		fontSize.addChangeListener(this);

		fontBold = new JCheckBox("Bold");
		fontBold.setSelected(false);
		fontBold.addActionListener(this);
		fontItalic = new JCheckBox("Italic");
		fontItalic.addActionListener(this);

		fontPanel.add(fontName);
		fontPanel.add(new JLabel(" Size: "));
		fontPanel.add(fontSize);
		fontPanel.add(fontBold);
		fontPanel.add(fontItalic);

		JPanel previewPanel = new JPanel(new BorderLayout());
		previewLabel =
			new JLabel("If you want change the world, start with the man in the mirror!");
		previewLabel.setHorizontalAlignment(JLabel.CENTER);
		previewPanel.add(previewLabel, BorderLayout.CENTER);
		previewPanel.setBorder(BorderFactory.createEtchedBorder());

		JPanel p = new JPanel(new BorderLayout());
		p.add(fontPanel, BorderLayout.NORTH);
		p.add(previewPanel, BorderLayout.CENTER);
		setUserPanel(p);
		setFont(font);
	}

	public void actionPerformed(ActionEvent ae) {
		if (!StyleConstants.getFontFamily(attributes).equals(fontName.getSelectedItem())) {
			StyleConstants.setFontFamily(attributes, (String) fontName.getSelectedItem());
		}

		if (StyleConstants.getFontSize(attributes) != ((Integer) fontSize.getValue())) {
			StyleConstants.setFontSize(attributes, ((Integer) fontSize.getValue()));
		}

		if (StyleConstants.isBold(attributes) != fontBold.isSelected()) {
			StyleConstants.setBold(attributes, fontBold.isSelected());
		}

		if (StyleConstants.isItalic(attributes) != fontItalic.isSelected()) {
			StyleConstants.setItalic(attributes, fontItalic.isSelected());
		}

		updatePreviewFont();
	}

	public void stateChanged(ChangeEvent e) {
		actionPerformed(null);
	}

	protected void updatePreviewFont() {
		String name = StyleConstants.getFontFamily(attributes);
		boolean bold = StyleConstants.isBold(attributes);
		boolean ital = StyleConstants.isItalic(attributes);
		int size = StyleConstants.getFontSize(attributes);

		Font f = new Font(name, (bold ? Font.BOLD : 0) + (ital ? Font.ITALIC : 0), size);
		previewLabel.setFont(f);
	}

	public void setFont(Font f) {
		if (f != null) {
			String s = f.getName();
			for (int i = 0; i < allFonts.length; i++) {
				if (s.equals(allFonts[i])) {
					fontName.setSelectedIndex(i);
					break;
				}
			}
			fontSize.setValue(new Integer(f.getSize()));
			fontBold.setSelected(f.isBold());
			fontItalic.setSelected(f.isItalic());
			actionPerformed(null);
		}
	}

	public Font getFont() {
		return font;
	}

	public AttributeSet getAttributes() {
		return attributes;
	}

	public Icon getActivityIcon() {
		return CellsUtil.getImage("Font32");
	}

	public String getActivityTitle() {
		return "Choose a Font";
	}

	public String getActivityDescription() {
		return "Choose a new font for the component.";
	}

	public void doYesAction() {
		font = previewLabel.getFont();
	}

	public void doCancelAction() {
		font = null;
	}

	public static Font showDialog(Component parent, Font font) {
		CellsFontChooser fc;
		Window window = SwingUtilities.windowForComponent(parent);
		if (window instanceof Frame) {
			fc = new CellsFontChooser((Frame) window, font);
		} else {
			fc = new CellsFontChooser((Dialog) window, font);
		}

		fc.setVisible(true);
		return fc.getFont();
	}
}
