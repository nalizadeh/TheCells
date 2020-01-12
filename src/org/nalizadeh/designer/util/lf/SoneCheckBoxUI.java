package org.nalizadeh.designer.util.lf;

import javax.swing.plaf.metal.MetalCheckBoxUI;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.View;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.ComponentUI;

public class SoneCheckBoxUI extends BasicCheckBoxUI {

  final static SoneCheckBoxUI buttonUI = new SoneCheckBoxUI();
        Icon selectedIcon = SoneLookAndFeel.getImage("checkboxOn.gif");
        Icon deselectedIcon = SoneLookAndFeel.getImage("checkboxOff.gif");

    public synchronized void paint(Graphics g, JComponent c) {

        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        Dimension size = c.getSize();

        int w = size.width;
        int h = size.height;

        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();

        Rectangle viewRect = new Rectangle(size);
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();

        Insets i = c.getInsets();
        viewRect.x += i.left;
        viewRect.y += i.top;
        viewRect.width -= (i.right + viewRect.x);
        viewRect.height -= (i.bottom + viewRect.y);

	b.setIcon(deselectedIcon);
	b.setSelectedIcon(selectedIcon);

        Icon altIcon = b.getIcon();

        String text = SwingUtilities.layoutCompoundLabel(
            c, fm, b.getText(), altIcon != null ? altIcon : getDefaultIcon(),
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect, b.getIconTextGap());

        // fill background
        if(c.isOpaque()) {
            g.setColor(b.getBackground());
            g.fillRect(0,0, size.width, size.height);
        }


        // Paint the radio button
        if(altIcon != null) {

            if(!model.isEnabled()) {
	        if(model.isSelected()) {
                   altIcon = b.getDisabledSelectedIcon();
		} else {
                   altIcon = b.getDisabledIcon();
		}
            } else if(model.isPressed() && model.isArmed()) {
                altIcon = b.getPressedIcon();
                if(altIcon == null) {
                    // Use selected icon
                    altIcon = b.getSelectedIcon();
                }
            } else if(model.isSelected()) {
                if(b.isRolloverEnabled() && model.isRollover()) {
                        altIcon = (Icon) b.getRolloverSelectedIcon();
                        if (altIcon == null) {
                                altIcon = (Icon) b.getSelectedIcon();
                        }
                } else {
                        altIcon = (Icon) b.getSelectedIcon();
                }
            } else if(b.isRolloverEnabled() && model.isRollover()) {
                altIcon = (Icon) b.getRolloverIcon();
            }

            if(altIcon == null) {
                altIcon = b.getIcon();
            }

            altIcon.paintIcon(c, g, iconRect.x, iconRect.y);

        } else {
            getDefaultIcon().paintIcon(c, g, iconRect.x, iconRect.y);
        }


        // Draw the Text
        if(text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
               int mnemIndex = b.getDisplayedMnemonicIndex();
               if(model.isEnabled()) {
                   // *** paint the text normally
                   g.setColor(b.getForeground());
                   BasicGraphicsUtils.drawStringUnderlineCharAt(g,text,
                       mnemIndex, textRect.x, textRect.y + fm.getAscent());
               } else {
                   // *** paint the text disabled
                   g.setColor(Color.lightGray);
                   BasicGraphicsUtils.drawStringUnderlineCharAt(g,text,
                       mnemIndex, textRect.x, textRect.y + fm.getAscent());
               }
               if(b.hasFocus() && b.isFocusPainted() &&
                  textRect.width > 0 && textRect.height > 0 ) {
                   paintFocus(g,textRect,size);
               }
	   }
        }
    }

  public static ComponentUI createUI(JComponent c) {
    return buttonUI;
  }
}
