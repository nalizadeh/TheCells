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

import org.nalizadeh.designer.CellsDesigner;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
public class CellsCopyrightDialog extends JDialog {

	public CellsCopyrightDialog(CellsDesigner designer) {
		super(designer, "The Cells", true);
		setResizable(false);

		JLabel icLab = new JLabel(CellsUtil.getImage("CopyrightDlg"));
		JLabel cpLab = new JLabel(
				"<html><b>The Cells Designer</b><br><br>" +
				"Version: " + designer.getVersion() + "<br>" +  
				"Build id: " + designer.getBuild() + "<br><br>" +
				"(c) Copyright nalizadeh.org. All rights reserved.<br>" +
				"Visit <a href=\"http://www.nalizadeh.org/cells.htm\">" + designer.getURL() + "</a></html>");
		
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
		panel.setBackground(Color.WHITE);
		panel.add(icLab);
		panel.add(new JLabel(" "));
		panel.add(cpLab);

		setLayout(new BorderLayout(0, 0));
		add(panel, BorderLayout.CENTER);
		setSize(panel.getPreferredSize().width + 40, panel.getPreferredSize().height + 40);
	}

	public void setVisible(boolean b) {
		int w = getParent().getWidth() / 2 - getWidth() / 2;
		int h = getParent().getHeight() / 2 - getHeight() / 2;
		setLocation(getParent().getX() + w, getParent().getY() + h);
		super.setVisible(b);
	}

	//==================================

	public static class SplashWindow extends Window {

		private Color color1 = new Color(89,137,202);
		private Color color2 = new Color(183,204,220);
		
		private CopyrightPanel panel;
		private JPanel progress;
		private JLabel label;
		private int steps, step;
		
		public SplashWindow(CellsDesigner designer, int stps) {
			super(designer);

			this.step = 0;
			this.steps = stps;
		
			panel = new CopyrightPanel(designer);
			label = new JLabel("Loading Modules...              ");
			progress = new JPanel(new FlowLayout(FlowLayout.LEFT,1,1)) {
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					
					g.setColor(color1);
					g.fillRect(0, 0, getWidth(), getHeight());

					int w = getWidth() / steps * step;
					g.setColor(color2);
					g.fillRect(0, 0, w, getHeight());
				}
			};
			progress.add(label);
					
			setLayout(new BorderLayout(0, 0));
			add(panel, BorderLayout.CENTER);
			add(progress, BorderLayout.SOUTH);

			int imgWidth = panel.getIcon().getIconWidth();
			int imgHeight = panel.getIcon().getIconHeight() + progress.getPreferredSize().height - 4;
			setSize(imgWidth, imgHeight);
			Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation((screenDim.width - imgWidth) / 2, (screenDim.height - imgHeight) / 2);
		}

		public void setText(String text) {
			step++;
			label.setText(text);
			progress.repaint();
		}

		public void doWait(long n) {
			try {
				Thread.sleep(n);
			} catch (InterruptedException ex) {

			}
		}
	}

	public static class CopyrightPanel extends JPanel {

		private String version;
		private String build;
		private ImageIcon icon = CellsUtil.getImage("Copyright");

		public CopyrightPanel(CellsDesigner designer) {
			version = designer.getVersion();
			build = designer.getBuild();
		}

		public void paint(Graphics g) {
			g.drawImage(icon.getImage(), 0, 0, null);
			g.drawString("Version: " + version + "  Build-ID: " + build, 25, 80);
		}

		public ImageIcon getIcon() {
			return icon;
		}
	}
}
