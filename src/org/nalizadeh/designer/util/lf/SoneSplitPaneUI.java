package org.nalizadeh.designer.util.lf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

public class SoneSplitPaneUI extends MetalSplitPaneUI
{
    /**
      * Creates a new MetalSplitPaneUI instance
      */
    public static ComponentUI createUI(JComponent x) {
	return new SoneSplitPaneUI();
    }

    /**
      * Creates the default divider.
      */
    public BasicSplitPaneDivider createDefaultDivider() {
	return new SoneSplitPaneDivider(this);
    }
}
