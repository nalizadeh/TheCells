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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

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
public class CellsModalInternalFrame extends JInternalFrame implements InternalFrameListener,
	VetoableChangeListener
{

	private CellsModalInternalFrame parent;
	private CellsModalInternalFrame child;
	private GlassPane newglass = new GlassPane();
	private Component oldglass = null;
	private boolean isModal = false;

	public CellsModalInternalFrame(String title, JInternalFrame parent) {
		super(title, true, true, true, true);

		this.parent = (CellsModalInternalFrame) parent;
		this.child = null;

                this.addInternalFrameListener(this);

		if (parent != null) {
			this.isModal = true;
                        this.parent.child = this;
		}
	}

	public void setSelected(boolean selected) throws PropertyVetoException {
		if (selected && child != null) {
			throw new PropertyVetoException("", null);
		}
		super.setSelected(selected);
	}

        protected void beeingSelected() {
        }

	public boolean isModal() {
		return isModal;
	}

	public boolean isBlocked() {
		return hasChild();
	}

	public boolean hasChild() {
		return child != null;
	}

	public JInternalFrame getParentFrame() {
		return parent;
	}

        public void destroy() {
                if (parent != null) {
                        removeInternalFrameListener(this);
                        parent.removeVetoableChangeListener(this);
                        parent.child = null;
                }
        }

	public void internalFrameOpened(InternalFrameEvent e) {
                if (parent != null) {
                        // block the desktop panel
                        oldglass = parent.getGlassPane();
                        parent.setGlassPane(newglass);
                        parent.addVetoableChangeListener(this);
                        parent.setEnabled(false);
                }
	}

	public void internalFrameClosed(InternalFrameEvent e) {
                if (parent != null) {
                        // unblock the panel on hidding the inner frame
                        parent.removeVetoableChangeListener(this);
                        parent.setGlassPane(oldglass);
                        parent.child = null;
                        parent.setEnabled(true);
                }
	}

	public void internalFrameClosing(InternalFrameEvent e) {
                if (parent != null) {
                        parent.removeVetoableChangeListener(this);
                        parent.setGlassPane(oldglass);
                        parent.child = null;
                        parent.setEnabled(true);
                }
	}

	public void internalFrameIconified(InternalFrameEvent e) {
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	public void internalFrameActivated(InternalFrameEvent e) {
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
		if (evt.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) ||
                    evt.getPropertyName().equals(JInternalFrame.IS_ICON_PROPERTY) ||
                    evt.getPropertyName().equals(JInternalFrame.IS_MAXIMUM_PROPERTY)) {

			throw new PropertyVetoException("", evt);
		}
	}

	class GlassPane extends JPanel {

		public GlassPane() {

			// block the desktop panel
			addKeyListener(new KeyAdapter() {
			});

			addMouseListener(new MouseAdapter() {
            });

            addMouseMotionListener(new MouseMotionAdapter() {
            });

			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
	}
}
