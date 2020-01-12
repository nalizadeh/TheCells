/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.metal.MetalTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;


public class SoneTreeUI extends MetalTreeUI {

        public static ComponentUI createUI(JComponent c) {
                return new SoneTreeUI();
        }

        protected TreeCellRenderer createDefaultCellRenderer() {
                return new SoneDefaultTreeCellRenderer();
        }

        public class SoneDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
                public SoneDefaultTreeCellRenderer() {
                        super();
                        setLeafIcon(SoneLookAndFeel.getImage("leaf.gif"));
                        setClosedIcon(SoneLookAndFeel.getImage("folder.gif"));
                        setOpenIcon(SoneLookAndFeel.getImage("folderopen.gif"));
                }
        }

        protected void installDefaults() {
                super.installDefaults();
                setExpandedIcon(SoneLookAndFeel.getImage("collapse.gif"));
                setCollapsedIcon(SoneLookAndFeel.getImage("expand.gif"));
        }
}
