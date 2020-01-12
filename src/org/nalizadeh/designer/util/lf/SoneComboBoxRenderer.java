package org.nalizadeh.designer.util.lf;

import javax.swing.*;
import java.awt.*;

import javax.swing.plaf.basic.BasicComboBoxRenderer;



public class SoneComboBoxRenderer extends BasicComboBoxRenderer {

    private SoneComboBoxUI comboBoxUI;

    public SoneComboBoxRenderer(SoneComboBoxUI comboBoxUI) {
        super();
        this.comboBoxUI = comboBoxUI;
    }

    public void paint(Graphics g) {
	if (comboBoxUI.isPopupVisible(null)) {
          super.paint(g);
          return;
       }

	Color c1 = UIManager.getColor("window");
	Color c2 = UIManager.getColor("control");

	Rectangle r = getBounds();

    	Graphics2D g2d = (Graphics2D) g;
	g2d.setPaint(new GradientPaint(r.x,r.y, c1, r.width+120,r.height, c2, true));
        g2d.fill(r);

        Color c3 = new Color(238,238,238);
        Color c4 = Color.darkGray;

        g.setColor(c3);
        g.drawLine(r.x, r.y, r.x, r.height-1);
        g.drawLine(r.x, r.y, r.width-1, r.y);

        g.setColor(c4);
        g.drawLine(r.x, r.height-1, r.width, r.height-1);
//        g.drawLine(r.width-1, r.y, r.width-1, r.height);

        g.setFont(UIManager.getFont("SoneLF.sysFont"));
        g.setColor(Color.black);
	g.drawString(getText(), 5, 15);
    }

    /**
     * A subclass of SoneComboBoxRenderer that implements UIResource.
     * SoneComboBoxRenderer doesn't implement UIResource
     * directly so that applications can safely override the
     * cellRenderer property with BasicListCellRenderer subclasses.
     * <p>
     */
    public static class UIResource extends SoneComboBoxRenderer implements javax.swing.plaf.UIResource {
    	UIResource(SoneComboBoxUI comboBoxUI) {
              super(comboBoxUI);
        }
    }
}


