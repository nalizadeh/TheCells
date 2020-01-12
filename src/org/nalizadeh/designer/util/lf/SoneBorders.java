/*--- (C) 1999-2006 Techniker Krankenkasse ---*/

/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;

public class SoneBorders {

	public static Border getTextFieldBorder() {
		UIDefaults table = UIManager.getLookAndFeelDefaults();
		Border textFieldBorder =
			new SoneBorders.TextFieldBorder(
				table.getColor("TextField.shadow"),
				table.getColor("TextField.darkShadow"),
				table.getColor("TextField.light"),
				table.getColor("TextField.highlight")
			);
		return textFieldBorder;
	}

	public static Border getMenuBarBorder() {
		return new MenuBarBorder();
	}

	public static Border getPopupMenuBorder() {
		return new PopupMenuBorder();
	}

	public static Border getButtonBorder() {
		return new ButtonBorder();
	}

	public static Border getToolBarBorder() {
		return null;
//  return new ToolBarBorder();
	}

	public static class FrameBorder extends AbstractBorder implements UIResource {

		private static final Insets insets = new Insets(4, 4, 4, 4);

		public FrameBorder() {

		}

		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {

			Color background;

			Window window = SwingUtilities.getWindowAncestor(c);
			if (window != null && window.isActive()) {
				background = UIManager.getColor("RootPane.titlePaneActiveBackground");
			} else {
				background = new Color(196, 196, 225);
			}

//            g.setColor(new Color(70,163,255));
//            for (int i = 0; i < 4; i++) {
//                g.drawRect(x+i,y+i,w-(i*2)-1, h-(i*2)-1);
//            }
//
			g.setColor(UIManager.getColor("RootPane.background"));
			for (int i = 0; i < 4; i++) {
				g.drawRect(x + i, y + i, w - (i * 2) - 1, h - (i * 2) - 1);
			}

			g.setColor(UIManager.getColor("RootPane.highlight"));
			g.drawLine(x + 1, y + 1, x + w - 2, y + 1);
			g.drawLine(x + 1, y + 1, x + 1, y + h - 2);

			g.setColor(UIManager.getColor("RootPane.shadow1"));
			g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
			g.drawLine(x + w - 1, y, x + w - 1, y + h - 1);

			g.setColor(UIManager.getColor("RootPane.shadow2"));
			g.drawLine(x + 1, y + h - 2, x + w - 2, y + h - 2);
			g.drawLine(x + w - 2, y + 1, x + w - 2, y + h - 2);
		}

		public Insets getBorderInsets(Component c) {
			return insets;
		}

		public Insets getBorderInsets(Component c, Insets newInsets) {
			newInsets.top = insets.top;
			newInsets.left = insets.left;
			newInsets.bottom = insets.bottom;
			newInsets.right = insets.right;
			return newInsets;
		}
	}

	public static class TextFieldBorder extends AbstractBorder implements UIResource {
		protected Color shadow;
		protected Color darkShadow;
		protected Color highlight;
		protected Color lightHighlight;

		public TextFieldBorder(
			Color shadow,
			Color darkShadow,
			Color highlight,
			Color lightHighlight
		) {
			this.shadow = shadow;
			this.highlight = highlight;
			this.darkShadow = darkShadow;
			this.lightHighlight = lightHighlight;
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.setColor(Color.white);
			g.drawRect(x, y, width - 1, height - 1);
//            BasicGraphicsUtils.drawEtchedRect(g, x, y, width, height, shadow, darkShadow, highlight, lightHighlight);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			Insets margin = null;
			if (c instanceof JTextComponent) {
				margin = ((JTextComponent) c).getMargin();
			}
			insets.top = margin != null ? 2 + margin.top : 2;
			insets.left = margin != null ? 2 + margin.left : 2;
			insets.bottom = margin != null ? 2 + margin.bottom : 2;
			insets.right = margin != null ? 2 + margin.right : 2;

			return insets;
		}
	}

	public static class MenuBarBorder extends AbstractBorder implements UIResource {
		protected Color c1 = new Color(140, 140, 140);
		protected Color c2 = new Color(183, 183, 183);
		protected Color c3 = new Color(216, 216, 216);

		public MenuBarBorder() {
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.setColor(c3);
			g.drawLine(x, height - 3, width - 1, height - 3);
			g.setColor(c2);
			g.drawLine(x, height - 2, width - 1, height - 2);
			g.setColor(c1);
			g.drawLine(x, height - 1, width - 1, height - 1);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			insets.top = 0;
			insets.left = 0;
			insets.bottom = 3;
			insets.right = 0;
			return insets;
		}
	}

	public static class PopupMenuBorder extends AbstractBorder implements UIResource {
		protected Color shadow = new Color(203, 203, 189);
		protected Color darkShadow = new Color(160, 159, 149);
		protected Color deepShadow = new Color(63, 63, 59);
		protected Color highlight = new Color(252, 252, 237);
		protected Color lightHighlight = Color.white;
		protected Color deepHighlight = new Color(234, 233, 215);

		public PopupMenuBorder() {
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			g.setColor(darkShadow);
			g.drawLine(x, y, width, y);
			g.drawLine(x, y, x, height);
			g.setColor(shadow);
			g.drawLine(x + 1, y + 1, width - 2, y + 1);
			g.setColor(deepHighlight);
			g.drawLine(x + 1, y + 2, width - 2, y + 2);

			g.setColor(highlight);
			g.drawLine(x + 1, y + 1, x + 1, height - 1);
			g.setColor(lightHighlight);
			g.drawLine(x + 2, y + 2, x + 2, height - 2);

			g.setColor(shadow);
			g.drawLine(x + 2, y + 1, width - 2, y + 1);
			g.drawLine(x + 2, height - 2, width - 2, height - 2);
			g.drawLine(width - 2, y + 1, width - 2, height - 2);
			g.setColor(deepShadow);
			g.drawLine(x + 1, height - 1, width - 1, height - 1);
			g.drawLine(width - 1, y, width - 1, height - 1);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			insets.top = 3;
			insets.left = 3;
			insets.bottom = 2;
			insets.right = 2;
			return insets;
		}
	}

	public static class ToolBarBorder extends AbstractBorder implements UIResource {

		private static final Color co = new Color(203, 203, 189);
		private static final ImageIcon img0 =
			SoneLookAndFeel.getImage("toolbar1.gif");

		public ToolBarBorder() {
		}

		public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {

			g.setColor(co);
			g.fillRect(0, 0, w - 1, h);
			g.drawImage(img0.getImage(), 0, 1, w - 1, h - 1, null);
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			insets.top = 3;
			insets.left = 3;
			insets.bottom = 3;
			insets.right = 3;
			return insets;
		}
	}

	public static class ButtonBorder extends AbstractBorder implements UIResource {

		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		}

		public Insets getBorderInsets(Component c) {
			return getBorderInsets(c, new Insets(0, 0, 0, 0));
		}

		public Insets getBorderInsets(Component c, Insets insets) {
			insets.top = 2;
			insets.left = insets.bottom = insets.right = 3;
			return insets;
		}

	}
}

/*--- Formatiert nach TK Code Konventionen vom 05.03.2002 ---*/
