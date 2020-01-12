package org.nalizadeh.designer.util.lf;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

public class SoneTabbedPaneUI extends MetalTabbedPaneUI {

    private Image img1Top = ((ImageIcon)UIManager.getIcon("TabbedPane.top.img1")).getImage();
    private Image img2Top = ((ImageIcon)UIManager.getIcon("TabbedPane.top.img2")).getImage();
    private Image img3Top = ((ImageIcon)UIManager.getIcon("TabbedPane.top.img3")).getImage();
    private Image img4Top = ((ImageIcon)UIManager.getIcon("TabbedPane.top.img4")).getImage();
    private Image img5Top = ((ImageIcon)UIManager.getIcon("TabbedPane.top.img5")).getImage();
    private Image img6Top = ((ImageIcon)UIManager.getIcon("TabbedPane.top.img6")).getImage();

    public static ComponentUI createUI( JComponent x ) {
        return new SoneTabbedPaneUI();
    }

    protected void paintTabBackground( Graphics g, int tabPlacement,
                                       int tabIndex, int x, int y, int w, int h, boolean isSelected ) {

	g.drawImage(isSelected ? img1Top : img2Top, x, y, null);
	g.drawImage(isSelected ? img5Top : img6Top, isSelected ? x+20 : x+11, y, isSelected ? w-20-3 : w-11-3, 20, null);
	g.drawImage(isSelected ? img3Top : img4Top, x+w-3, y, null);
    }

    protected void paintTabBorder( Graphics g, int tabPlacement,
                                   int tabIndex, int x, int y, int w, int h,
                                   boolean isSelected) {
    }

    protected void paintFocusIndicator(Graphics g, int tabPlacement,
                                   Rectangle[] rects, int tabIndex,
                                   Rectangle iconRect, Rectangle textRect,
                                   boolean isSelected) {
    }

    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
    	return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 20;
    }
}
