/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class SoneToolBar extends JPanel {

	private static final Color co = new Color(203,203,189);
	private static final ImageIcon img0 = SoneLookAndFeel.getImage("toolbar0.gif");
	private static final ImageIcon img1 = SoneLookAndFeel.getImage("toolbar1.gif");
	private static final ImageIcon img2 = SoneLookAndFeel.getImage("toolbar2.gif");
	private static final ImageIcon img3 = SoneLookAndFeel.getImage("toolbar3.gif");

	public SoneToolBar() {
		setPreferredSize(new Dimension(10, 26));
	}

	public void add(Action action) {
		JButton b = new ToolBarButton(action);
		super.add(b);
	}

	public void addSeparator() {}

	public void paint(Graphics g) {
		int w = getWidth();
		int h = getHeight();

		g.drawImage(img1.getImage(), 0,0, w, h, null);
		g.setColor(co);
		g.fillRect(0, 4, w, 20);
		g.drawImage(img0.getImage(), w-24,5, null);

		for (int i=0,x=5; i < getComponentCount(); i++,x+=24) {
			JButton b = (JButton) this.getComponent(i);
			b.setBounds(x, 4, 19,19);
			b.repaint();
                }
	}

	class ToolBarButton extends JButton {
          public ToolBarButton(Action a) {
		super(a);
		setBorder(null);
		setRolloverEnabled(true);
	  }

          public void paint(Graphics g) {
		ButtonModel model = getModel();

		if (model.isRollover()) {
                  	g.drawImage(model.isPressed() ? img3.getImage() : img2.getImage(), 0,0, null);
                }
		else {
			g.setColor(co);
			g.fillRect(0,0,getWidth(), getHeight());
		}

          	Icon ic = isEnabled() ?
		(isSelected() ? getSelectedIcon() : getIcon()) :
		(isSelected() ? getDisabledSelectedIcon() : getDisabledIcon());

		ic.paintIcon(this, g, 2,2);
          }
        }
}
