/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicMenuBarUI;


public class SoneMenuBarUI extends BasicMenuBarUI {

    public static ComponentUI createUI(JComponent c) {
	return new SoneMenuBarUI();
    }

    public void paint(Graphics g, JComponent c) {
	JMenuBar b = (JMenuBar)c;
	g.setColor(UIManager.getColor("MenuBar.background"));
	g.fillRect(0, 0, c.getWidth(), c.getHeight());
    }
}
