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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.nalizadeh.designer.html.HtmlGenerator;


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
public class HtmlPanel extends JPanel {

	private CellsDesigner designer;
	private HtmlGenerator generator;
	private JEditorPane editorPane;

	public HtmlPanel(CellsDesigner designer) {
		super(new BorderLayout());

		this.designer = designer;		
		this.generator = new HtmlGenerator();
		this.editorPane = new JEditorPane();    	
		
		editorPane.setEditable(false);
		editorPane.setBackground(new Color(230,255,230));
		        
		JScrollPane sp = new JScrollPane(editorPane);
		sp.getVerticalScrollBar().setUnitIncrement(20);

		add(sp, BorderLayout.CENTER);
	}

	public void generateSource() {

		StringBuffer html = generator.generateHTML(
				designer.getDesktop().getDesigner().getContainer());
        		
        editorPane.setText(html.toString());
 		editorPane.setCaretPosition(0);
	}

	public String getSourceCode() {
		if (isVisible()) {
			generateSource();
			return editorPane.getText();
		}
		return null;
	}
}
