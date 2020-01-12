/*
 * Adapted from SoneRootPaneUI comments below were copied from that class
 */

package org.nalizadeh.designer.util.lf;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class SoneStatusBar extends JLabel {

	private static final Color c1 = new Color(114,114,106);
	private static final Color c2 = new Color(142,142,132);
	private static final Color c3 = new Color(173,172,159);
	private static final Color c4 = new Color(160,159,149);
	private static final Color c5 = new Color(193,192,178);
	private static final Color c6 = new Color(203,203,189);
	private static final Color c7 = new Color(244,243,227);

	private static final ImageIcon img0 = SoneLookAndFeel.getImage("status0.gif");
	private static final ImageIcon img1 = SoneLookAndFeel.getImage("status1.gif");
	private static final ImageIcon img2 = SoneLookAndFeel.getImage("status2.gif");
	private static final ImageIcon img3 = SoneLookAndFeel.getImage("status3.gif");
	private static final ImageIcon img4 = SoneLookAndFeel.getImage("status4.gif");
	private static final ImageIcon img5 = SoneLookAndFeel.getImage("status5.gif");

	private ArrayList comps = new ArrayList();
	private boolean closed = false;
	private int wx = 0;

	public SoneStatusBar() {
		setPreferredSize(new Dimension(10, 20));
		addItem(new StatusBarItem("OVR"));
		addItem(new StatusBarItem("Statusbar"));
		addItem(new StatusBarItem("TKeasy Version 2008", "menu"));

		this.addMouseListener(new MouseListener() {

		    public void mouseClicked(MouseEvent e) {
                    	int x = e.getX();
			int y = e.getY();
			int w = getWidth();
			int iw = img3.getIconWidth();
			int ih = img3.getIconHeight();

			if (x > w-wx-16 && x < w-wx-16+iw && y > 4 && y < ih + 4) {
                          closed = !closed;
                          repaint();
                        }
                   }

		    public void mouseReleased(MouseEvent e) {}
		    public void mousePressed(MouseEvent e) {}
		    public void mouseEntered(MouseEvent e) {}
		    public void mouseExited(MouseEvent e) {}
		}
            );
	}

	public void addItem(StatusBarItem item) {
		comps.add(item);
	}

	public void paint(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		int iw = img2.getIconWidth();
		int ih = img2.getIconHeight();

		g.setColor(c6);
		g.fillRect(0, 0, w-iw, h);
  		g.setColor(c1);
		g.drawRect(0, 0, w-iw-1, h-1);
		g.drawImage(img2.getImage(), w-iw-1, 0, null);

		wx = iw;

		if (closed) {
			g.drawImage(img4.getImage(), w-wx-16, 4, null);
                  	return;
                }

		FontMetrics fm = sun.awt.windows.WToolkit.getDefaultToolkit().getFontMetrics(g.getFont());

		for (int i=0; i < comps.size(); i++) {
			StatusBarItem it = (StatusBarItem)comps.get(i);
			int mw = it.isMenu() ? 20 : 0;
			int tw = fm.stringWidth(it.getTitle())+12;
			wx += tw + mw;
			g.drawImage(img0.getImage(), w-wx-1, 0, tw + mw, 20, null);
			g.setColor(Color.black);
			g.drawString(it.getTitle(), w-wx+5, 15);
			if (it.isMenu())
				g.drawImage(img5.getImage(), w-wx+tw+1, 3, null);
			g.setColor(c4);
			g.drawLine(w-wx-1, 0, w-wx-1, 20);
		}
		g.drawImage(img1.getImage(), w-wx-4, 0, null);
		g.drawImage(img3.getImage(), w-wx-16, 5, null);
	}

	public static class StatusBarItem {
		private String title;
		private Object menu;
		private boolean ismenu;
		public StatusBarItem(String title) {
			this.title = title;
			this.ismenu = false;
		}

		public StatusBarItem(String title, Object menu) {
			this(title);
			this.menu = menu;
			this.ismenu = true;
		}

		public String getTitle() { return title; }
		public boolean isMenu() { return ismenu; }
        }
}
