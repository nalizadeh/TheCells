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
package org.nalizadeh.designer.util.dockwin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

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
public class DockingWindow extends JPanel implements ContainerListener {

	private DockingPort port;
	private DockingTitle docktitle;
	private Component dockcomp;

	private boolean selected;
	private boolean maximized;
	private boolean minimized;
	private boolean iconified;
	private boolean closed;

	private String constraints;

	public DockingWindow(String title, Icon icon, Component panel, boolean withScroll) {
		super(new BorderLayout(0, 0));

		setBorder(null);

		docktitle = new DockingTitle(title, icon);
		dockcomp = panel == null ? new JPanel() : panel;

		add(docktitle, BorderLayout.NORTH);
		
		if (withScroll) {
			JScrollPane scroller = new JScrollPane();
			scroller.getViewport().add(dockcomp);
			scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroller.getHorizontalScrollBar().setUnitIncrement(16);
			scroller.getVerticalScrollBar().setUnitIncrement(16);
			dockcomp.setPreferredSize(new Dimension(1000, 1000));
			add(scroller, BorderLayout.CENTER);
		}
		else {
			add(dockcomp, BorderLayout.CENTER);
		}
		
		setBorder(BorderFactory.createRaisedBevelBorder());
		
		if (dockcomp instanceof Container) {
			((Container)dockcomp).addContainerListener(this);
		}

		maximized = false;
		minimized = false;
		iconified = false;
		closed = false;
		
		select(false);
	}

	private void initMouseEvents(Component comp, boolean adding) {

		comp.removeMouseListener(port.getRoot());
		comp.removeMouseMotionListener(port.getRoot());
		
		if (adding) {
			comp.addMouseListener(port.getRoot());
			comp.addMouseMotionListener(port.getRoot());
		}

		if (comp instanceof Container) {
			Container cont = (Container) comp;
			cont.addContainerListener(null);
			for (Component co : cont.getComponents()) {
				initMouseEvents(co, adding);
			}
		}
	}

    public void componentAdded(ContainerEvent e) {
    	initMouseEvents(e.getComponent(), true);
    }

    public void componentRemoved(ContainerEvent e) {
    	initMouseEvents(e.getComponent(), false);
    }

	public String getTitle() {
		return docktitle.getTitle();
	}

	public Icon getIcon() {
		return docktitle.getIcon();
	}

	public void select(boolean b) {
		selected = b;
		docktitle.setFocused(b);
	}

	public void maximize() {
		iconified = false;
		minimized = false;
		maximized = true;
		closed = false;
	}

	public void minimize() {
		iconified = false;
		minimized = true;
		maximized = false;
		closed = false;
	}

	public void iconify() {
		iconified = true;
		minimized = false;
		maximized = false;
		closed = false;
	}

	public void close() {
		iconified = false;
		minimized = false;
		maximized = false;
		closed = true;
	}

	public boolean isSelected() {
		return selected;
	}

	public boolean isMaximized() {
		return maximized;
	}

	public boolean isIconified() {
		return iconified;
	}

	public boolean isMinimized() {
		return minimized;
	}

	public boolean isClosed() {
		return closed;
	}

	public boolean canDrag(MouseEvent e) {
		
		Component src = e.getComponent();
	
		if (!maximized && !(src instanceof DockingButton)) {
			MouseEvent me = SwingUtilities.convertMouseEvent(src, e, this); 
			int x = me.getX();
			int y = me.getY();
			return docktitle.contains(x, y) && x > 2 && x < getWidth() - 46;
		}
		return false;
	}

	public void setPort(DockingPort port, String constraints) {
		this.port = port;
		this.constraints = constraints;

		initMouseEvents(this, true);
	}

	public DockingPort getPort() {
		return port;
	}

	public String getConstraints() {
		return constraints;
	}

	private class DockingTitle extends JPanel implements ActionListener {
		private String title;
		private Icon icon;

		private JLabel lbIcon;
		private JLabel lbTitle;

		private DockingButton btIconify;
		private DockingButton btMinimize;
		private DockingButton btMaximize;
		private DockingButton btClose;

		private JPanel p1;
		private JPanel p2;

		private Color color1 = new Color(193, 214, 230);
		private Color color2 = new Color(44, 73, 136);
		private Font font1 = new Font("Arial", 0, 11);
		private Font font2 = new Font("Arial", 1, 11);

		private DockingTitle(String title, Icon icon) {
			super(new BorderLayout(0, 0));

			this.title = title;
			this.icon = icon;

			lbIcon = new JLabel(icon);
			lbIcon.setAlignmentY(JLabel.CENTER_ALIGNMENT);

			lbTitle = new JLabel(title);
			lbTitle.setAlignmentY(JLabel.CENTER_ALIGNMENT);

			btIconify = new DockingButton(DockingPort.getImage("dockicon.gif"));
			btMinimize = new DockingButton(DockingPort.getImage("dockmin.gif"));
			btMaximize = new DockingButton(DockingPort.getImage("dockmax.gif"));
			btClose = new DockingButton(DockingPort.getImage("dockclose.gif"));

			btIconify.setPreferredSize(new Dimension(11, 11));
			btMinimize.setPreferredSize(new Dimension(11, 11));
			btMaximize.setPreferredSize(new Dimension(11, 11));
			btClose.setPreferredSize(new Dimension(11, 11));

			btIconify.addActionListener(this);
			btMinimize.addActionListener(this);
			btMaximize.addActionListener(this);
			btClose.addActionListener(this);

			btMinimize.setVisible(false);

			lbTitle.setForeground(Color.WHITE);

			p1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
			p1.add(lbIcon);
			p1.add(lbTitle);

			p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 4));
			p2.add(btIconify);
			p2.add(btMinimize);
			p2.add(btMaximize);
			p2.add(btClose);

			add(p1, BorderLayout.WEST);
			add(p2, BorderLayout.CENTER);

			setOpaque(true);
			setPreferredSize(new Dimension(100, 18));
			setFocused(false);
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btMaximize) {
				btMinimize.setVisible(true);
				btMaximize.setVisible(false);
				port.maximize(DockingWindow.this);

			} else if (e.getSource() == btMinimize) {
				btMinimize.setVisible(false);
				btMaximize.setVisible(true);
				port.minimize(DockingWindow.this);

			} else if (e.getSource() == btIconify) {
				port.iconify(DockingWindow.this);

			} else if (e.getSource() == btClose) {
				port.close(DockingWindow.this);
			}
		}

		private String getTitle() {
			return title;
		}

		private Icon getIcon() {
			return icon;
		}

		private void setFocused(boolean focused) {
			lbTitle.setFont(focused ? font2 : font1);
			lbTitle.setForeground(focused ? Color.WHITE : Color.BLACK);

			Color bc = focused ? color2 : color1;
			btIconify.setBackground(bc);
			btMinimize.setBackground(bc);
			btMaximize.setBackground(bc);
			btClose.setBackground(bc);
			p1.setBackground(bc);
			p2.setBackground(bc);
			setBackground(bc);
		}
	}

	private class DockingButton extends JButton {

		private Icon icon;

		private DockingButton(Icon icon) {
			this.icon = icon;
		}

		public void paint(Graphics g) {
			int p = getModel().isPressed() ? 1 : 0;
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			g.drawImage(
				((ImageIcon) icon).getImage(),
				p,
				p,
				icon.getIconWidth(),
				icon.getIconHeight(),
				null
			);
		}
	}
}
