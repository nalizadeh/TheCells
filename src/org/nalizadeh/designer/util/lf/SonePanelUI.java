/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicPanelUI;


public class SonePanelUI extends BasicPanelUI {

    // Shared UI object
    private static PanelUI panelUI = null;

    public static ComponentUI createUI(JComponent c) {
	if(panelUI == null) {
            panelUI = new SonePanelUI();
	}
        return panelUI;
    }
}
