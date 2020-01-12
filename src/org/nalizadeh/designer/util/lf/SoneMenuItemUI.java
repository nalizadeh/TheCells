/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import java.awt.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import java.awt.event.KeyEvent;
import javax.swing.text.View;
import javax.swing.plaf.basic.BasicHTML;


public class SoneMenuItemUI extends BasicMenuItemUI {

    public static ComponentUI createUI(JComponent c) {
	return new SoneMenuItemUI();
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
          g.setColor(UIManager.getColor("MenuItem.lineShadow"));
          g.drawLine(0, 0, 0, menuHeight - 2);
          g.drawLine(0, 0, menuWidth-1, 0);

          g.setColor(UIManager.getColor("MenuItem.lineHiglight"));
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










    /* Client Property keys for text and accelerator text widths */
    static final String MAX_TEXT_WIDTH =  "maxTextWidth";
    static final String MAX_ACC_WIDTH  =  "maxAccWidth";

    // these rects are used for painting and preferredsize calculations.
    // they used to be regenerated constantly.  Now they are reused.
    static Rectangle zeroRect = new Rectangle(0,0,0,0);
    static Rectangle iconRect = new Rectangle();
    static Rectangle textRect = new Rectangle();
    static Rectangle acceleratorRect = new Rectangle();
    static Rectangle checkIconRect = new Rectangle();
    static Rectangle arrowIconRect = new Rectangle();
    static Rectangle viewRect = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
    static Rectangle r = new Rectangle();

    String acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");

    private void resetRects() {
        iconRect.setBounds(zeroRect);
        textRect.setBounds(zeroRect);
        acceleratorRect.setBounds(zeroRect);
        checkIconRect.setBounds(zeroRect);
        arrowIconRect.setBounds(zeroRect);
        viewRect.setBounds(0,0,Short.MAX_VALUE, Short.MAX_VALUE);
        r.setBounds(zeroRect);
    }

    protected void paintMenuItem(Graphics g, JComponent c,
                                     Icon checkIcon, Icon arrowIcon,
                                     Color background, Color foreground,
                                     int defaultTextIconGap) {
        JMenuItem b = (JMenuItem) c;
        ButtonModel model = b.getModel();

        //   Dimension size = b.getSize();
        int menuWidth = b.getWidth();
        int menuHeight = b.getHeight();
        Insets i = c.getInsets();

        resetRects();

        viewRect.setBounds( 0, 0, menuWidth, menuHeight );

        viewRect.x += i.left;
        viewRect.y += i.top;
        viewRect.width -= (i.right + viewRect.x);
        viewRect.height -= (i.bottom + viewRect.y);


        Font holdf = g.getFont();
        Font f = c.getFont();
        g.setFont( f );
        FontMetrics fm = g.getFontMetrics( f );
        FontMetrics fmAccel = g.getFontMetrics( acceleratorFont );

        // get Accelerator text
        KeyStroke accelerator =  b.getAccelerator();
        String acceleratorText = "";
        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                //acceleratorText += "-";
                acceleratorText += acceleratorDelimiter;
	    }

            int keyCode = accelerator.getKeyCode();
            if (keyCode != 0) {
                acceleratorText += KeyEvent.getKeyText(keyCode);
            } else {
                acceleratorText += accelerator.getKeyChar();
            }
        }

        // layout the text and icon
        String text = layoutMenuItem(
            fm, b.getText(), fmAccel, acceleratorText, b.getIcon(),
            checkIcon, arrowIcon,
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect, acceleratorRect,
            checkIconRect, arrowIconRect,
            b.getText() == null ? 0 : defaultTextIconGap,
            defaultTextIconGap
        );

        // Paint background
	paintBackground(g, b, background);

        Color holdc = g.getColor();

        // Paint the Check
        if (checkIcon != null) {
            if(model.isArmed() || (c instanceof JMenu && model.isSelected())) {
                g.setColor(foreground);
            } else {
                g.setColor(holdc);
            }
            if( useCheckAndArrow() )
		checkIcon.paintIcon(c, g, checkIconRect.x, checkIconRect.y);
            g.setColor(holdc);
        }

        // Paint the Icon
        if(b.getIcon() != null) {
            Icon icon;
            if(!model.isEnabled()) {
                icon = (Icon) b.getDisabledIcon();
            } else if(model.isPressed() && model.isArmed()) {
                icon = (Icon) b.getPressedIcon();
                if(icon == null) {
                    // Use default icon
                    icon = (Icon) b.getIcon();
                }
            } else {
                icon = (Icon) b.getIcon();
            }


            if (icon!=null) {
              if (model.isArmed() || model.isSelected()) {
                Icon disabled = b.getDisabledIcon();
                if (disabled != null) {
                  disabled.paintIcon(c, g, iconRect.x + 1, iconRect.y + 1);
                  icon.paintIcon(c, g, iconRect.x - 1, iconRect.y - 1);
                }
                else {
                  icon.paintIcon(c, g, iconRect.x, iconRect.y);
                }
              }
              else {
                icon.paintIcon(c, g, iconRect.x, iconRect.y);
              }
            }
        }

        // Draw the Text
        if(text != null) {
 	    View v = (View) c.getClientProperty(BasicHTML.propertyKey);
 	    if (v != null) {
 		v.paint(g, textRect);
 	    } else {
		paintText(g, b, textRect, text);
	    }
	}

        // Draw the Accelerator Text
        if(acceleratorText != null && !acceleratorText.equals("")) {

	  //Get the maxAccWidth from the parent to calculate the offset.
	  int accOffset = 0;
	  Container parent = menuItem.getParent();
	  if (parent != null && parent instanceof JComponent) {
	    JComponent p = (JComponent) parent;
	    Integer maxValueInt = (Integer) p.getClientProperty(MAX_ACC_WIDTH);
	    int maxValue = maxValueInt != null ?
                maxValueInt.intValue() : acceleratorRect.width;

	    //Calculate the offset, with which the accelerator texts will be drawn with.
	    accOffset = maxValue - acceleratorRect.width;
	  }

	  g.setFont( acceleratorFont );
            if(!model.isEnabled()) {
                // *** paint the acceleratorText disabled
	      if ( disabledForeground != null )
		  {
                  g.setColor( disabledForeground );
                  BasicGraphicsUtils.drawString(g,acceleratorText,0,
                                                acceleratorRect.x - accOffset,
                                                acceleratorRect.y + fmAccel.getAscent());
                }
                else
                {
                  g.setColor(b.getBackground().brighter());
                  BasicGraphicsUtils.drawString(g,acceleratorText,0,
                                                acceleratorRect.x - accOffset,
						acceleratorRect.y + fmAccel.getAscent());
                  g.setColor(b.getBackground().darker());
                  BasicGraphicsUtils.drawString(g,acceleratorText,0,
                                                acceleratorRect.x - accOffset - 1,
						acceleratorRect.y + fmAccel.getAscent() - 1);
                }
            } else {
                // *** paint the acceleratorText normally
                if (model.isArmed()|| (c instanceof JMenu && model.isSelected())) {
                    g.setColor( acceleratorSelectionForeground );
                } else {
                    g.setColor( acceleratorForeground );
                }
                BasicGraphicsUtils.drawString(g,acceleratorText, 0,
                                              acceleratorRect.x - accOffset,
                                              acceleratorRect.y + fmAccel.getAscent());
            }
        }

        // Paint the Arrow
        if (arrowIcon != null) {
            if(model.isArmed() || (c instanceof JMenu &&model.isSelected()))
                g.setColor(foreground);
            if(useCheckAndArrow())
                arrowIcon.paintIcon(c, g, arrowIconRect.x, arrowIconRect.y);
        }
        g.setColor(holdc);
        g.setFont(holdf);
    }

    /**
     * Compute and return the location of the icons origin, the
     * location of origin of the text baseline, and a possibly clipped
     * version of the compound labels string.  Locations are computed
     * relative to the viewRect rectangle.
     */

    private String layoutMenuItem(
        FontMetrics fm,
        String text,
        FontMetrics fmAccel,
        String acceleratorText,
        Icon icon,
        Icon checkIcon,
        Icon arrowIcon,
        int verticalAlignment,
        int horizontalAlignment,
        int verticalTextPosition,
        int horizontalTextPosition,
        Rectangle viewRect,
        Rectangle iconRect,
        Rectangle textRect,
        Rectangle acceleratorRect,
        Rectangle checkIconRect,
        Rectangle arrowIconRect,
        int textIconGap,
        int menuItemGap
        )
    {

        SwingUtilities.layoutCompoundLabel(
                            menuItem, fm, text, icon, verticalAlignment,
                            horizontalAlignment, verticalTextPosition,
                            horizontalTextPosition, viewRect, iconRect, textRect,
                            textIconGap);

        /* Initialize the acceelratorText bounds rectangle textRect.  If a null
         * or and empty String was specified we substitute "" here
         * and use 0,0,0,0 for acceleratorTextRect.
         */
        if( (acceleratorText == null) || acceleratorText.equals("") ) {
            acceleratorRect.width = acceleratorRect.height = 0;
            acceleratorText = "";
        }
        else {
            acceleratorRect.width = SwingUtilities.computeStringWidth( fmAccel, acceleratorText );
            acceleratorRect.height = fmAccel.getHeight();
        }

        /* Initialize the checkIcon bounds rectangle's width & height.
         */

	if( useCheckAndArrow()) {
	    if (checkIcon != null) {
		checkIconRect.width = checkIcon.getIconWidth();
		checkIconRect.height = checkIcon.getIconHeight();
	    }
	    else {
		checkIconRect.width = checkIconRect.height = 0;
	    }

	    /* Initialize the arrowIcon bounds rectangle width & height.
	     */

	    if (arrowIcon != null) {
		arrowIconRect.width = arrowIcon.getIconWidth();
		arrowIconRect.height = arrowIcon.getIconHeight();
	    } else {
		arrowIconRect.width = arrowIconRect.height = 0;
	    }
        }

        Rectangle labelRect = iconRect.union(textRect);
        if( isLeftToRight(menuItem) ) {
            textRect.x += menuItemGap;
            iconRect.x += menuItemGap;

            // Position the Accelerator text rect
            acceleratorRect.x = viewRect.x + viewRect.width - arrowIconRect.width
                             - menuItemGap - acceleratorRect.width;

            // Position the Check and Arrow Icons
            if (useCheckAndArrow()) {
                checkIconRect.x = viewRect.x + menuItemGap;
                textRect.x += menuItemGap + checkIconRect.width;
                iconRect.x += menuItemGap + checkIconRect.width;
                arrowIconRect.x = viewRect.x + viewRect.width - menuItemGap
                                  - arrowIconRect.width;
            }
        } else {
            textRect.x -= menuItemGap;
            iconRect.x -= menuItemGap;

            // Position the Accelerator text rect
            acceleratorRect.x = viewRect.x + arrowIconRect.width + menuItemGap;

            // Position the Check and Arrow Icons
            if (useCheckAndArrow()) {
                checkIconRect.x = viewRect.x + viewRect.width - menuItemGap
                                  - checkIconRect.width;
                textRect.x -= menuItemGap + checkIconRect.width;
                iconRect.x -= menuItemGap + checkIconRect.width;
                arrowIconRect.x = viewRect.x + menuItemGap;
            }
        }

        // Align the accelertor text and the check and arrow icons vertically
        // with the center of the label rect.
        acceleratorRect.y = labelRect.y + (labelRect.height/2) - (acceleratorRect.height/2);
        if( useCheckAndArrow() ) {
            arrowIconRect.y = labelRect.y + (labelRect.height/2) - (arrowIconRect.height/2);
            checkIconRect.y = labelRect.y + (labelRect.height/2) - (checkIconRect.height/2);
        }

        /*
        System.out.println("Layout: text="+menuItem.getText()+"\n\tv="
                           +viewRect+"\n\tc="+checkIconRect+"\n\ti="
                           +iconRect+"\n\tt="+textRect+"\n\tacc="
                           +acceleratorRect+"\n\ta="+arrowIconRect+"\n");
        */

        return text;
    }

    private boolean useCheckAndArrow(){
	boolean b = true;
	if((menuItem instanceof JMenu) &&
	   (((JMenu)menuItem).isTopLevelMenu())) {
	    b = false;
	}
	return b;
    }

    static boolean isLeftToRight( Component c ) {
        return c.getComponentOrientation().isLeftToRight();
    }
}

