package org.nalizadeh.designer.util.lf;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Component;

public class SoneComboBoxUI extends BasicComboBoxUI {

    public static ComponentUI createUI(JComponent c) {
	return new SoneComboBoxUI();
    }

    protected JButton createArrowButton() {
        return new JButton(UIManager.getIcon("SoneLF.comboBoxArrow"));
    }

    protected ListCellRenderer createRenderer() {
        return new SoneComboBoxRenderer.UIResource(this);
    }

}
