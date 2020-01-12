/*---- Generated by CellsDesigner at 14.06.2008 ----*/

package org.nalizadeh.designer.examples;

import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

/**
* <p>Title:</p>
*
* <p>Description:</p>
*
* <p>Copyright: Copyright (c) 2007</p>
*
* <p>Organisation:</p>
*
* @author   undefined
* @version  1.0
*/

public class CardLayoutTest extends JPanel {

     private CardLayout layout = new CardLayout(4, 4);

     private double[][] panel1_constraints = {
          {20.0, 20.0, CellsLayout.FILL, },
          {60.0, 60.0, CellsLayout.FILL, }
     };

     private CellsLayout panel1_layout = new CellsLayout(panel1_constraints, 4, 4);

     private JLabel label1 = new JLabel();
     private JButton button1 = new JButton();
     private JTextField textfield1 = new JTextField();
     JPanel panel1 = new JPanel();
     private JScrollPane panel1_scrollpane1 = new JScrollPane();
     private JTable panel1_table1 = new JTable();
     private JLabel panel1_label1 = new JLabel();

    /**
     * @param
     *
     * @return
     *
     * @exception
     *
     * @see
     */
     public CardLayoutTest() {
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

          label1.setText("JLabel:");
          button1.setText("JButton");
          textfield1.setText("JTextField");
          panel1_scrollpane1.getViewport().add(panel1_table1);
          panel1_table1.setModel(new DefaultTableModel( 
               new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
               },
               new String [] {
                    "Title 1", "Title 2", "Title 3", "Title 4"
               }
          ));
          panel1_label1.setText("JLabel:");

          initPanel1();

          add(label1, "0");
          add(button1, "1");
          add(textfield1, "2");
          add(panel1, "3");
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
     private void initPanel1() {
          panel1.setLayout(panel1_layout);
          panel1.add(panel1_scrollpane1, new CellsLayout.Cell(0, 1, false, 2, 1, CellsLayout.LEFT, CellsLayout.TOP));
          panel1.add(panel1_label1, new CellsLayout.Cell(0, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
     }

}

