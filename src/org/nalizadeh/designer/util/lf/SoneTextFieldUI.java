/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.metal.MetalTextFieldUI;
import javax.swing.text.JTextComponent;
import javax.swing.text.Highlighter;
import javax.swing.text.Caret;
import java.beans.PropertyChangeEvent;;


public class SoneTextFieldUI extends BasicTextFieldUI {

    public static ComponentUI createUI(JComponent c) {
	return new SoneTextFieldUI();
    }

    public void propertyChange(PropertyChangeEvent evt) {
	super.propertyChange(evt);
    }


    protected void paintBackground(Graphics g) {
        g.setColor(new Color(255, 240, 158));
        g.fillRect(0, 0, getComponent().getWidth(), getComponent().getHeight());
    }

}
