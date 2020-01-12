package org.nalizadeh.designer.util.lf;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GradientPaint;

public class SoneScrollBarUI extends BasicScrollBarUI {

    private static Color thumbColor;
    private static Color thumbShadow;
    private static Color thumbHighlightColor;

    private ImageIcon bumpsh;
    private ImageIcon bumpsv;

    protected SoneBumps bumps;

    public static ComponentUI createUI(JComponent c) {
	return new SoneScrollBarUI();
    }

    protected void installDefaults() {
	super.installDefaults();
        thumbColor          = UIManager.getColor("ScrollBar.thumb");
        thumbShadow         = UIManager.getColor("ScrollBar.thumbShadow");
        thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");

	bumps = new SoneBumps( 10, 10, thumbHighlightColor, thumbShadow, thumbColor );

	bumpsh = (ImageIcon)UIManager.getIcon("ScrollBar.bumpsh");
	bumpsv = (ImageIcon)UIManager.getIcon("ScrollBar.bumpsv");
    }

    protected JButton createDecreaseButton(int orientation)  {
        return new SoneArrowButton(orientation,
				    UIManager.getColor("ScrollBar.thumb"),
				    UIManager.getColor("ScrollBar.thumbShadow"),
				    UIManager.getColor("ScrollBar.thumbDarkShadow"),
				    UIManager.getColor("ScrollBar.thumbHighlight"));
    }

    protected JButton createIncreaseButton(int orientation)  {
        return new SoneArrowButton(orientation,
				    UIManager.getColor("ScrollBar.thumb"),
				    UIManager.getColor("ScrollBar.thumbShadow"),
				    UIManager.getColor("ScrollBar.thumbDarkShadow"),
				    UIManager.getColor("ScrollBar.thumbHighlight"));
    }


    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds)
    {
        Color c1 = Color.white;
        Color c2 = UIManager.getColor("textHighlight");

        Graphics2D g2d = (Graphics2D) g;
	if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
	        g2d.setPaint(new GradientPaint(trackBounds.x, trackBounds.y,
	 		c1, trackBounds.x, trackBounds.height+180, c2, false));
        }
	else {
	        g2d.setPaint(new GradientPaint(trackBounds.x, trackBounds.y,
			c1, trackBounds.width+180, trackBounds.y, c2, false));

        }

        g2d.fill(trackBounds);
    }


    protected void paintThumb( Graphics g, JComponent c, Rectangle thumbBounds )
    {
        if (!c.isEnabled()) {
	    return;
	}

        g.translate( thumbBounds.x, thumbBounds.y );

	if ( scrollbar.getOrientation() == JScrollBar.VERTICAL )
	{
	    g.setColor( thumbColor );
	    g.fillRect( 0, 0, thumbBounds.width, thumbBounds.height );

	    g.setColor( thumbShadow );
	    g.drawRect( 0, 0, thumbBounds.width-1, thumbBounds.height-1);

	    g.setColor( thumbHighlightColor );
	    g.drawLine( 0, 0, thumbBounds.width - 2, 0 );
	    g.drawLine( 0, 0, 0, thumbBounds.height-2 );

            if (bumpsv != null) {
                bumpsv.paintIcon(c, g,
			thumbBounds.width/2 - bumpsv.getIconWidth()/2 + 1,
			thumbBounds.height/2 - bumpsv.getIconHeight()/2 + 1);
            }
            else {
              bumps.setBumpArea(thumbBounds.width - 5, thumbBounds.height - 6);
              bumps.paintIcon(c, g, 3, 4);
            }
	}
	else  // HORIZONTAL
	{
	    g.setColor( thumbColor );
	    g.fillRect( 0, 0, thumbBounds.width, thumbBounds.height );

	    g.setColor( thumbShadow );
	    g.drawRect( 0, 0, thumbBounds.width - 1, thumbBounds.height - 1 );

	    g.setColor( thumbHighlightColor );
	    g.drawLine( 0, 0, thumbBounds.width - 2, 0 );
	    g.drawLine( 0, 0, 0, thumbBounds.height - 2 );

            if (bumpsh != null) {
                bumpsh.paintIcon(c, g,
			thumbBounds.width/2 - bumpsh.getIconWidth()/2 + 1,
			thumbBounds.height/2 - bumpsh.getIconHeight()/2 + 1);
            }
            else {
              bumps.setBumpArea(thumbBounds.width - 6, thumbBounds.height - 5);
              bumps.paintIcon(c, g, 4, 3);
            }
	}

        g.translate( -thumbBounds.x, -thumbBounds.y );
    }






   class SoneArrowButton extends JButton {
        private int direction;
        private Color shadow;
        private Color darkShadow;
        private Color highlight;

        public SoneArrowButton(int direction, Color background, Color shadow,
			 Color darkShadow, Color highlight) {

	    setRequestFocusEnabled(false);
            setBackground(background);

            this.direction = direction;
	    this.shadow = shadow;
	    this.darkShadow = darkShadow;
	    this.highlight = highlight;
	}

        public SoneArrowButton(int direction) {
	    this(direction, UIManager.getColor("control"), UIManager.getColor("controlShadow"),
		 UIManager.getColor("controlDkShadow"), UIManager.getColor("controlLtHighlight"));
        }

        public Dimension getPreferredSize() {
            return new Dimension(16, 16);
        }

        public Dimension getMinimumSize() {
            return new Dimension(5, 5);
        }

        public Dimension getMaximumSize() {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

    	public boolean isFocusTraversable() {
	  return false;
	}

	public void paint(Graphics g) {

           super.paint(g);

	    Color origColor;
	    boolean isPressed, isEnabled;
	    int w, h, size;

            w = getSize().width;
            h = getSize().height;
	    origColor = g.getColor();
	    isPressed = getModel().isPressed();
	    isEnabled = isEnabled();


            if (isPressed) {
                g.translate(1, 1);
            }

            // Draw the arrow
            size = Math.min((h - 4) / 3, (w - 4) / 3);
            size = Math.max(size, 2);

	    paintTriangle(g, (w - size) / 2, (h - size) / 2, size, direction, isEnabled);

            // Reset the Graphics back to it's original settings
            if (isPressed) {
                g.translate(-1, -1);
	    }
	    g.setColor(origColor);

        }

	public void paintTriangle(Graphics g, int x, int y, int size,
					int direction, boolean isEnabled) {
	    Color oldColor = g.getColor();
	    int mid, i, j;

	    j = 0;
            size = Math.max(size, 2);
	    mid = (size / 2) - 1;

	    g.translate(x, y);
	    if(isEnabled)
		g.setColor(darkShadow);
	    else
		g.setColor(shadow);

            switch(direction)       {
            case NORTH:
                for(i = 0; i < size; i++)      {
                    g.drawLine(mid-i, i, mid+i, i);
                }
                if(!isEnabled)  {
                    g.setColor(highlight);
                    g.drawLine(mid-i+2, i, mid+i, i);
                }
                break;
            case SOUTH:
                if(!isEnabled)  {
                    g.translate(1, 1);
                    g.setColor(highlight);
                    for(i = size-1; i >= 0; i--)   {
                        g.drawLine(mid-i, j, mid+i, j);
                        j++;
                    }
		    g.translate(-1, -1);
		    g.setColor(shadow);
		}

		j = 0;
                for(i = size-1; i >= 0; i--)   {
                    g.drawLine(mid-i, j, mid+i, j);
                    j++;
                }
                break;
            case WEST:
                for(i = 0; i < size; i++)      {
                    g.drawLine(i, mid-i, i, mid+i);
                }
                if(!isEnabled)  {
                    g.setColor(highlight);
                    g.drawLine(i, mid-i+2, i, mid+i);
                }
                break;
            case EAST:
                if(!isEnabled)  {
                    g.translate(1, 1);
                    g.setColor(highlight);
                    for(i = size-1; i >= 0; i--)   {
                        g.drawLine(j, mid-i, j, mid+i);
                        j++;
                    }
		    g.translate(-1, -1);
		    g.setColor(shadow);
                }

		j = 0;
                for(i = size-1; i >= 0; i--)   {
                    g.drawLine(j, mid-i, j, mid+i);
                    j++;
                }
		break;
            }
	    g.translate(-x, -y);
	    g.setColor(oldColor);
	}
   }

}
