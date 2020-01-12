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


public class SoneButtonUI extends BasicButtonUI {

  final static SoneButtonUI buttonUI = new SoneButtonUI();

    private static Rectangle viewRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();

    private static final ImageIcon icon1 = SoneLookAndFeel.getImage("button1.gif");
    private static final ImageIcon icon2 = SoneLookAndFeel.getImage("button2.gif");

    public void paint(Graphics g, JComponent c)
    {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        FontMetrics fm = g.getFontMetrics();

        Dimension d = b.getSize();


        viewRect.x = viewRect.y = 0;
	    viewRect.width = d.width-1;
 	    viewRect.height = d.height-1;
        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        Font f = c.getFont();
        g.setFont(f);

        // layout the text and icon
        String text = SwingUtilities.layoutCompoundLabel(
            c, fm, b.getText(), b.getIcon(),
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect,
	    b.getText() == null ? 0 : b.getIconTextGap());

       clearTextShiftOffset();

        paintButton(g, b, viewRect);

        // Paint the Icon
        if(b.getIcon() != null) {
            paintIcon(g, c, iconRect);
        }

        if (text != null && !text.equals("")){
		    View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		    if (v != null) {
				v.paint(g, textRect);
		    } else {
				paintText(g, b, textRect, text);
	    	}
        }

        if (b.isFocusPainted() && b.hasFocus()) {
            // paint UI specific focus
            paintFocus(g,b,viewRect,textRect,iconRect);
        }
    }


  protected void paintButton(Graphics g, AbstractButton b, Rectangle viewRect) {

    Rectangle r = viewRect;
    ButtonModel model = b.getModel();

    boolean b1 = b.getParent() instanceof JToolBar;
    boolean b2 = b.getParent() instanceof JScrollBar;
    boolean b3 = b instanceof TableCellRenderer;

    if (b1 || b2 || b3) {
        paintButton1(g, b, viewRect);
	return;
    }

    Image img = model.isArmed() && model.isPressed() ? icon2.getImage() : icon1.getImage();
    g.drawImage(img, r.x, r.y+1, r.width, r.height, null);

    g.setColor(new Color(114,114,106));
    g.drawLine(r.x+1, r.y, r.width-1, r.y);
    g.drawLine(r.x, r.y+1, r.x, r.height-1);
    g.drawLine(r.x+1, r.height, r.width-1, r.height);
    g.drawLine(r.width, r.y+1, r.width, r.height-1);

    g.setColor(new Color(244,243,227));
    g.drawLine(r.x+1, r.y+1, r.x+1, r.height-2);
  }

  /**
   * Description of the Method
   *
   * @param g         Description of Parameter
   * @param b         Description of Parameter
   * @param viewRect  Description of Parameter
   * @param textRect  Description of Parameter
   * @param iconRect  Description of Parameter
   */
  protected void paintButton1(Graphics g, AbstractButton b, Rectangle viewRect) {

    Rectangle r = viewRect;

    Color c1 = UIManager.getColor("window");
    Color c2 = Color.darkGray;
    Color c3 = new Color(238, 238, 238);
    Color c4 = Color.darkGray;

    ButtonModel model = b.getModel();

    Graphics2D g2d = (Graphics2D) g;
    g2d.setPaint(new GradientPaint(r.x, r.y,
		model.isPressed() ? c2 : c1,
		r.x, r.height+20,
		model.isPressed() ? c1 : c2, true));

    g2d.fill(r);

    g.setColor(model.isRollover() || (model.isArmed() && model.isPressed()) ? c4 : c3);
    g.drawLine(r.x, r.y, r.x, r.height-1);
    g.drawLine(r.x, r.y, r.width-1, r.y);

    g.setColor(model.isRollover() || (model.isArmed() && model.isPressed()) ? c3 : c4);
    g.drawLine(r.x, r.height, r.width, r.height);
    g.drawLine(r.width, r.y, r.width, r.height);
  }

  // ********************************
  //          Create PLAF
  // ********************************
  /**
   * Description of the Method
   *
   * @pa
   * ram c  Description of Parameter
   * @return   Description of the Returned Value
   */
  public static ComponentUI createUI(JComponent c) {
    return buttonUI;
  }

    protected void installDefaults(AbstractButton b) {
      super.installDefaults(b);
      b.setBorderPainted(false);
    }
}
