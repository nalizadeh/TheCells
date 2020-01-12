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
import javax.swing.JSplitPane;

import org.nalizadeh.designer.html.AspxGenerator;
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
public class AspxPanel extends JPanel {

	private CellsDesigner designer;
	private AspxGenerator generator;
	private JEditorPane editorPane1;
	private JEditorPane editorPane2;

	public AspxPanel(CellsDesigner designer) {
		super(new BorderLayout());

		this.designer = designer;		
		this.generator = new AspxGenerator();
		this.editorPane1 = new JEditorPane();    	
		this.editorPane2 = new JEditorPane();    	
		
		editorPane1.setEditable(false);
		editorPane1.setBackground(new Color(255,255,153));
		
		editorPane2.setEditable(false);
		editorPane2.setBackground(new Color(255,155,153));
		        
		JPanel p = new JPanel(new BorderLayout());
		p.add(editorPane1, BorderLayout.CENTER);

		JScrollPane sp1 = new JScrollPane(editorPane1);
		JScrollPane sp2 = new JScrollPane(editorPane2);
		
		JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		sp.setDividerLocation(1.0);
		sp.setResizeWeight(0.5);
		sp.setOneTouchExpandable(true);
		sp.setLeftComponent(sp1);
		sp.setRightComponent(sp2);

		add(sp, BorderLayout.CENTER);
	}

	public void generateSource() {

		StringBuffer buff1 = new StringBuffer();
		StringBuffer buff2 = new StringBuffer();
		
		generator.generateAspx(designer.getDesktop().getDesigner().getContainer(), buff1, buff2);
        		
        editorPane1.setText(buff1.toString());
 		editorPane1.setCaretPosition(0);
        editorPane2.setText(buff2.toString());
 		editorPane2.setCaretPosition(0);
	}
}
