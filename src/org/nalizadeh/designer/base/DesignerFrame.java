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

import org.nalizadeh.designer.AspxPanel;
import org.nalizadeh.designer.CellsDesigner;
import org.nalizadeh.designer.HtmlPanel;
import org.nalizadeh.designer.SourcePanel;

import org.nalizadeh.designer.html.HtmlViewer;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import org.nalizadeh.designer.util.CellsModalInternalFrame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameListener;
import javax.swing.undo.UndoManager;

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
public abstract class DesignerFrame extends CellsModalInternalFrame {

	private CellsDesigner designer;
	private UndoManager undoManager;
	private DesignerContainer container;
	private SourcePanel sourcer = null;
	private HtmlPanel html = null;
	private AspxPanel aspx = null;
	private HtmlViewer htmlViewer = null;
	private DesignerContSrcWrapper conSrcWrapper;
	private boolean showingSource; 
	private boolean showingHtml; 
	private boolean showingAspx; 
	private String packageName;

	private InternalFrameListener specialListener;

	private int saveState = 1;

	private Component postSelection = null;

	public DesignerFrame(
		String		   title,
		String		   name,
		Container	   cont,
		CellsDesigner  designer,
		JInternalFrame parent
	) {

		super(title, parent);

		this.setName(name == null ? cont.getName() : name);

		this.designer = designer;
		this.container = null;
		this.conSrcWrapper = null;
        this.packageName = null;
		this.undoManager = new UndoManager();
		this.sourcer = new SourcePanel(designer);
		this.html = new HtmlPanel(designer);
		this.aspx = new AspxPanel(designer);
		this.htmlViewer = new HtmlViewer(designer);
		this.showingSource = false;
		this.showingHtml = false;
		this.showingAspx = false;
	}

	public void initUserArea(DesignerContainer container, Container userPanel) {

		this.container = container;
		this.conSrcWrapper = new DesignerContSrcWrapper(userPanel == null ? container : userPanel);

		CellsLayout layout = new CellsLayout();

		layout.addRow(CellsLayout.FILL);
		layout.addColumn(CellsLayout.FILL);

		setLayout(layout);
		layout.createCells();
		
		add(conSrcWrapper, "0,0");
		
		setLocation(10, 10);
		if (isModal()) {
			setLocation(getParentFrame().getX() + 20, getParentFrame().getY() + 20);
		}

		setSize(500, 300);
		setVisible(true);
	}

	public void setEnabled(boolean enabled) {
		container.setEnabled(enabled);
	}

	public CellsDesigner getDesigner() {
		return designer;
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public DesignerContainer getContainer() {
		return container;
	}

	public void showSource(boolean show) {
		this.showingSource = show;
		if (show) {
			sourcer.generateSource();
		}
		conSrcWrapper.showSource(show, sourcer);
	}
	
	public boolean sourceShowing() {
		return showingSource;
	}
		
	public SourcePanel getSourcer() {
		return sourcer;
	}
	
	public void showHtml(boolean show) {
		this.showingHtml = show;
		if (show) {
			html.generateSource();
		}
		conSrcWrapper.showSource(show, html);
	}
	
	public boolean htmlShowing() {
		return showingHtml;
	}
	
	public HtmlPanel getHtml() {
		return html;
	}
	
	public void showAspx(boolean show) {
		this.showingAspx = show;
		if (show) {
			aspx.generateSource();
		}
		conSrcWrapper.showSource(show, aspx);
	}

	public boolean aspxShowing() {
		return showingHtml;
	}
		
	public AspxPanel getAspx() {
		return aspx;
	}

	public void getPreviewHtml() {
		htmlViewer.show();
	}

	public HtmlViewer getHtmlViewer() {
		return htmlViewer;
	}

	public boolean canCutCopy() {
		return container.getSelectedComponents().length == 1;
	}

	public boolean canPaste() {
		return true;
	}

	public boolean canSave() {
		return saveState != 1 && !isModal();
	}

	public boolean isCanceled() {
		return saveState == 0;
	}

	public boolean isSaved() {
		return saveState == 1;
	}

	public boolean isAborted() {
		return saveState == 2;
	}

	public void cancel() {
		saveState = 0;
	}

	public void save() {
		saveState = 1;
	}

	public void abort() {
		saveState = 2;
	}

	public String getPackage() {
		return packageName;
	}

	public void setPackage(String packageName) {
		this.packageName = packageName;
	}

	public void setDirty(boolean dirty) {
		saveState = dirty ? 0 : saveState;
	}

	public void deselectAll() {
		if (postSelection != null) {
			container.selectDesignerConstraints(
				new DesignerConstraints(null, postSelection, container)
			);
			postSelection = null;

		} else {
			container.selectDesignerConstraints(null);
		}
	}

	public void redraw() {
		container.revalidate();
		revalidate();
	}

	public void update() {
	}

	public void destroy() {
		if (!isModal()) {
			designer.getProperty().unloadProperties(container);
		}
		super.destroy();
	}

	public void setPostSelecion(Component comp) {
		postSelection = comp;
	}

	public void addSpecialListener(InternalFrameListener l) {
		if (l != null) {
			super.addInternalFrameListener(l);
			specialListener = l;
		}
	}

	public void removeSpecialListener() {
		if (specialListener != null) {
			super.removeInternalFrameListener(specialListener);
		}
	}

	public InternalFrameListener getSpecialListener() {
		return specialListener;
	}

	public String toString() {
		return title;
	}

	/**
	 * DesignerContSrcWrapper
	 *
	 * @author   Nader Alizadeh
	 *
	 */
	private static class DesignerContSrcWrapper extends JPanel {

		private Container container;
		private Container sourcer;
		
		DesignerContSrcWrapper(Container cont) {
			
			this.container = cont;
			this.sourcer = new JPanel(new BorderLayout(0,0));
			
			setLayout(new BorderLayout(0,0));
			add(container, BorderLayout.CENTER);
		}
		
		void showSource(boolean show, Container srcCont) {
			removeAll();
			sourcer.removeAll();
			if (show) {
				sourcer.add(srcCont, BorderLayout.CENTER);
				add(sourcer, BorderLayout.CENTER);
			}
			else {
				add(container, BorderLayout.CENTER);
			}
			validate();
			repaint();
		}
	}
}
