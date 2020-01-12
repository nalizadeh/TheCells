package org.nalizadeh.designer.util.lf;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.font.*;

/*
** Defines a class called GradientTest which takes the basic JApplet
** class as a template, and adds to it to create our Applet.
*/
public class GradientTest extends JFrame  {

  public void init() {
    setBackground(Color.lightGray);   // Set background colour to light grey
    setForeground(Color.black);   // Sets foreground colour of Applet's	canvas to Black
  }

  /* paint method to determine how to paint on the JApplet's canvas */
  public void paint (Graphics g) {
    Graphics2D g2 = (Graphics2D) g;     // Cast parameter g from Graphics to Graphics2D

    /* Outside border - resizable */
    g2.setPaint(Color.lightGray);  // Sets paint style to light grey colour
    g2.draw3DRect(0, 0, getSize().width-1, getSize().height-1, true);
//raised
    g2.draw3DRect(5, 5, getSize().width-11, getSize().height-11, false);
//lowered

    /* Blue to White Horizontal */
    GradientPaint bluetowhiteHoriz = new GradientPaint(20,20,Color.blue,120,20,Color.white);
    g2.setPaint(bluetowhiteHoriz);
    g2.fill(new RoundRectangle2D.Double(20,20,100,50,5,5));

    /* Blue to White Vertical */
    GradientPaint bluetowhiteVert = new GradientPaint(150,20,Color.blue,150,70,Color.white);
    g2.setPaint(bluetowhiteVert);
    g2.fill(new RoundRectangle2D.Double(150,20,100,50,5,5));

    /* Blue to White Horizontal Cyclic */
    GradientPaint bluetowhiteCycle2 = new GradientPaint(300,20,Color.blue,
      350,20,Color.white,true);
    g2.setPaint(bluetowhiteCycle2);
    g2.fill(new RoundRectangle2D.Double(300,20,100,50,5,5));

    /* White To Blue Horizontal Cyclic (4 times) */
    GradientPaint whitetoblueCycle4 = new GradientPaint(450,20,Color.white,
      475,20,Color.blue,true);
    g2.setPaint(whitetoblueCycle4);
    g2.fill(new RoundRectangle2D.Double(450,20,100,50,5,5));

  }

  /*
  ** Define information about the applet - in Appletviewer, see Applet |
Info menu
  */
  public String getAppletInfo() {
    return "Title: GradientTest\nAuthor: Simon Huggins\n"+
      "Simple Graphics2D Applet showing 3D-effect rectangle plus gradient-filled shapes";
  }

  public static void main (String args[]) {

	System.setProperty("awt.toolkit", "original.lf.SoneToolkit");

         GradientTest frame = new GradientTest();
	 frame.pack();
	 frame.setVisible(true);
  }

}
