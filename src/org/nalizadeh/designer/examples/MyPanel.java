/*---- Generated by The Cells at 28.09.2013 ----*/

package org.nalizadeh.designer.examples;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

/**
* <p>Title:<p>
*
* <p>Description:<p>
*
* <p>Copyright: Copyright (c) 2007<p>
*
* <p>Organisation:<p>
*
* @author   undefined
* @version  1.0
*/

public class MyPanel extends JPanel {

     private double[][] constraints = {
          {20.0, 20.0, 20.0, 20.0, CellsLayout.FILL, 20.0, },
          {60.0, 60.0, CellsLayout.FILL, 186.0, }
     };

     private CellsLayout layout = new CellsLayout(constraints, 4, 4);

     protected JLabel label1 = new JLabel();
     public JTextField textfield1 = new JTextField();
     JButton button1 = new JButton();
     JScrollPane scrollpane1 = new JScrollPane();
     JTextArea scrollpane1_textarea1 = new JTextArea();

    /**
     * @param
     *
     * @return
     *
     * @exception
     *
     * @see
     */
     public MyPanel() {
          doInit();
     }

    /**
     * @param
     *
     * @return
     *
     * @exception
     *
     * @see
     */
     private void doInit() {
          setLayout(layout);

          label1.setFont(new Font("Tahoma Fett", 1, 11));
          label1.setText("JLabel:");
          textfield1.setText("JTextField");
          button1.setText("JButton:");
          scrollpane1.getViewport().add(scrollpane1_textarea1);
          scrollpane1_textarea1.setText("");


          add(label1, new CellsLayout.Cell(0, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(textfield1, new CellsLayout.Cell(0, 1, false, 0, 2, CellsLayout.LEFT, CellsLayout.TOP));
          add(button1, new CellsLayout.Cell(5, 3, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(scrollpane1, new CellsLayout.Cell(1, 1, false, 3, 2, CellsLayout.LEFT, CellsLayout.TOP));
     }

}

