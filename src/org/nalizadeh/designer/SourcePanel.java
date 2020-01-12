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

import org.nalizadeh.designer.util.CellsCodeStyler;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

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
public class SourcePanel extends JPanel {

	private CellsDesigner designer;
	private JTextPane txt1 = new JTextPane();
	private JTextPane txt2 = new JTextPane();

	public SourcePanel(CellsDesigner designer) {
		super(new BorderLayout());

		this.designer = designer;
		txt2.setPreferredSize(new Dimension(55, 100));
		txt2.setEditable(false);

		JPanel p = new JPanel(new BorderLayout());
		p.add(txt2, BorderLayout.WEST);
		p.add(txt1, BorderLayout.CENTER);

		JScrollPane sp = new JScrollPane(p);
		sp.getVerticalScrollBar().setUnitIncrement(20);

		add(sp, BorderLayout.CENTER);
	}

	public void generateSource() {

		txt1.setText("");
		txt2.setText("");

		StringBuffer sb = designer.getSourceGenerator().generate();

		StyledDocument doc1 = txt1.getStyledDocument();
		StyledDocument doc2 = txt2.getStyledDocument();

		try {
			new CellsCodeStyler().style(sb, doc1, doc2);

		} catch (Exception ex) {
			DebugPanel.printStackTrace(ex);
		}
		txt1.setCaretPosition(0);
		txt2.setCaretPosition(0);
	}

	public String getSourceCode() {
		if (isVisible()) {
			generateSource();
			return txt1.getText();
		}
		return null;
	}
}
