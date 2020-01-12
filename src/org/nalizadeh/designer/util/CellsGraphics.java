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

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * Java Graphics drawing utilities
 *
 * <p>Copyright: Copyright (c) 2007 N.A.J</p>
 *
 * <p>Organisation:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class CellsGraphics {

	private static final Color sColor = new Color(255, 255, 153);

	private static Robot robot;

	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println("no graphic device found!");
			System.exit(1);
		}
	}

	public static Robot getRobot() {
		return robot;
	}

	public static void drawCompositeRect(Graphics g, Rectangle rect, Color color) {
		Graphics2D g2d = (Graphics2D) g;
		Composite originalComposite = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1F));
		g2d.setColor(color);
		g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
		g2d.setComposite(originalComposite);
	}

	public static void drawDashRect(Graphics g, Rectangle r, Color c) {
		Graphics2D g2d = (Graphics2D) g;
		Stroke originalStroke = g2d.getStroke();
		g2d.setColor(c);
		Stroke stroke =
			new BasicStroke(
				2,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL,
				0,
				new float[] { 4, 4 },
				0
			);

		g2d.setStroke(stroke);
		g2d.draw(r);
		g2d.setStroke(originalStroke);
	}

	public static void drawDashLine(Graphics g, int x1, int y1, int x2, int y2, Color color) {
		drawSizedDashLine(g, x1, y1, x2, y2, color, 2, 4);
	}

	public static void drawSizedDashLine(
		Graphics g,
		int		 x1,
		int		 y1,
		int		 x2,
		int		 y2,
		Color    color,
		int		 size,
		int		 tick
	) {

		Graphics2D g2d = (Graphics2D) g;
		Stroke originalStroke = g2d.getStroke();
		g2d.setColor(color);
		Stroke stroke =
			new BasicStroke(
				size,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_BEVEL,
				0,
				new float[] { tick, tick },
				0
			);

		g2d.setStroke(stroke);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.setStroke(originalStroke);
	}

	public static void drawSolidLine(
		Graphics g,
		int		 x1,
		int		 y1,
		int		 x2,
		int		 y2,
		Color    color,
		int		 size
	) {
		Graphics2D g2d = (Graphics2D) g;
		Stroke originalStroke = g2d.getStroke();
		g2d.setColor(color);
		Stroke stroke = new BasicStroke(size, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);

		g2d.setStroke(stroke);
		g2d.drawLine(x1, y1, x2, y2);
		g2d.setStroke(originalStroke);
	}

	public static void showHintText(Graphics g, int x, int y, String txt) {
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int w = fm.stringWidth(txt) + 10;
		int h = fm.getHeight();
		g.setColor(sColor);
		g.fillRect(x, y, w, h);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, w, h);
		g.drawString(txt, x + 5, y + h - 3);
	}

	public static void drawRect(
		Graphics  g,
		Rectangle r,
		boolean   selected,
		boolean   rollover,
		boolean   hinted,
		int		  num
	) {
		g.setColor(selected ? Color.ORANGE : hinted ? sColor : Color.LIGHT_GRAY);
		g.fillRect(r.x, r.y, r.width, r.height);

		g.setColor(rollover ? Color.DARK_GRAY : Color.WHITE);
		g.drawLine(r.x, r.y, r.x + r.width, r.y);
		g.drawLine(r.x, r.y, r.x, r.y + r.height);

		g.setColor(rollover ? Color.WHITE : Color.DARK_GRAY);
		g.drawLine(r.x, r.y + r.height, r.x + r.width, r.y + r.height);
		g.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height);

		String s = num + "";
		g.setColor(rollover ? Color.WHITE : Color.BLACK);
		g.drawString(s, r.x + r.width / 2 - (s.length() * 4 / 2), r.y + r.height / 2 + 5);
	}

	public static void fillGradient(Graphics g, Rectangle bounds, Color color1, Color color2) {
		Graphics2D g2 = (Graphics2D) g;
		GradientPaint gpaint = new GradientPaint(0, 0, color1, bounds.width, 0, color2, true);
		g2.setPaint(gpaint);
		g2.fill(new Rectangle(0, 0, bounds.width, bounds.height));
	}

	public static Image captureScreen(Rectangle bounds) {
		return robot.createScreenCapture(bounds);
	}

	public static Image captureComponent(Component component) {
		Dimension size = component.getSize();
		BufferedImage image =
			new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		component.paint(image.getGraphics());
		return image;
	}

	public static Image captureFrame(JFrame frame) {
		BufferedImage image = robot.createScreenCapture(frame.getBounds());
		return image;
	}
	
	public static void setMouse(int x, int y) {
		robot.mouseMove(x, y);
	}
}
