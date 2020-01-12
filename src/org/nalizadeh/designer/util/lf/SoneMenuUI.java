/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;


public class SoneMenuUI extends BasicMenuUI {

    public static ComponentUI createUI(JComponent c) {
	return new SoneMenuUI();
    }

    protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
	ButtonModel model = menuItem.getModel();
	Color oldColor = g.getColor();
        int menuWidth = menuItem.getWidth();
        int menuHeight = menuItem.getHeight();

	g.setColor(model.isArmed()|| model.isSelected() ?
		UIManager.getColor("MenuItem.selectionBackground") : menuItem.getBackground());
	g.fillRect(0, 0, menuWidth, menuHeight);

	if (model.isArmed()|| model.isSelected()) {
          g.setColor(UIManager.getColor("MenuItem.lineHiglight"));
          g.drawLine(0, 0, 0, menuHeight - 2);
          g.drawLine(0, 0, menuWidth-1, 0);

          g.setColor(UIManager.getColor("MenuItem.lineShadow"));
          g.drawLine(0, menuHeight - 1, menuWidth-1, menuHeight - 1);
          g.drawLine(menuWidth-1, 0, menuWidth-1, menuHeight - 1);
        }
    }

    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text) {
	ButtonModel model = menuItem.getModel();
	FontMetrics fm = g.getFontMetrics();
	int mnemIndex = menuItem.getDisplayedMnemonicIndex();

	if(!model.isEnabled()) {
	    // *** paint the text disabled
	    if ( UIManager.get("MenuItem.disabledForeground") instanceof Color ) {
		g.setColor( UIManager.getColor("MenuItem.disabledForeground") );
		BasicGraphicsUtils.drawStringUnderlineCharAt(g,text,mnemIndex,
					      textRect.x,
					      textRect.y + fm.getAscent());
	    } else {
		g.setColor(menuItem.getBackground().brighter());
		BasicGraphicsUtils.drawStringUnderlineCharAt(g,text,mnemIndex,
					      textRect.x,
					      textRect.y + fm.getAscent());
		g.setColor(menuItem.getBackground().darker());
		BasicGraphicsUtils.drawStringUnderlineCharAt(g,text,mnemIndex,
					      textRect.x - 1,
					      textRect.y + fm.getAscent() - 1);
	    }
	} else {
	    // *** paint the text normally
	    if (model.isArmed()|| (menuItem instanceof JMenu && model.isSelected())) {
		g.setColor(selectionForeground); // Uses protected field.
	    }
            else g.setColor(UIManager.getColor("MenuItem.foreground"));
	    BasicGraphicsUtils.drawStringUnderlineCharAt(g,text, mnemIndex,
					  textRect.x,
					  textRect.y + fm.getAscent());
	}
    }
}

