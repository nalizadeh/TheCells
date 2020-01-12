package org.nalizadeh.designer.util.lf;

import javax.swing.*;
import javax.swing.text.View;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.MetalButtonUI;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Rectangle2D;
import javax.swing.table.TableCellRenderer;
import javax.accessibility.AccessibleContext;

/**
 * @created   27 avril 2002
 * @version   $Revision: 1.8 $, $Date: 2002/04/27 11:45:08 $
 */
public class SoneInternalFrameUI extends BasicInternalFrameUI {

        private SoneInternalFrameTitlePane titlePane;
        public static ComponentUI createUI(JComponent b)    {
                return new SoneInternalFrameUI((JInternalFrame)b);
        }

        public SoneInternalFrameUI(JInternalFrame b)   {
                super(b);
        }

        protected JComponent createNorthPane(JInternalFrame w) {
                titlePane = new SoneInternalFrameTitlePane(w);
                return titlePane;
        }

        protected void uninstallComponents(){
                super.uninstallComponents();
                if(titlePane != null) {
                        titlePane.uninstallDefaults();
                }
                titlePane = null;
        }
}
