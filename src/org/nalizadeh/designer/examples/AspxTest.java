/*---- Generated by The Cells at 14.12.2013 ----*/

package org.nalizadeh.designer.examples;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;

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

public class AspxTest extends JPanel {

     private LayoutManager layout = null;

     JLabel label1 = new JLabel();
     JTextField textfield1 = new JTextField();
     JRadioButton radiobutton1 = new JRadioButton();
     JRadioButton radiobutton2 = new JRadioButton();
     JTextArea textarea1 = new JTextArea();
     JButton button1 = new JButton();
     JButton button2 = new JButton();
     JCheckBox checkbox1 = new JCheckBox();
     JComboBox combobox1 = new JComboBox();
     JList list1 = new JList();
     JTable table1 = new JTable();
     JScrollPane scrollpane1 = new JScrollPane();
     JTree scrollpane1_tree1 = new JTree();

    /**
     * @param
     *
     * @return
     *
     * @exception
     *
     * @see
     */
     public AspxTest() {
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
          textfield1.setText("JTextField:");
          radiobutton1.setText("Favoriten");
          radiobutton2.setText("Optionen");
          textarea1.setText("");
          button1.setText("Cancel");
          button2.setText("Ok");
          checkbox1.setText("JCheckBox:");
          table1.setModel(new DefaultTableModel( 
               new Object [][] {
                    {null, null, null, null, },
                    {null, null, null, null, },
                    {null, null, null, null, },
                    {null, null, null, null, },
                    
               },
               new String [] {
                    "Title 1", "Title 2", "Title 3", "Title 4", 
               }
          ));
          scrollpane1.getViewport().add(scrollpane1_tree1);


          add(label1);
          label1.setBounds(new Rectangle(22, 25, 43, 20));
          add(textfield1);
          textfield1.setBounds(new Rectangle(74, 23, 259, 20));
          add(radiobutton1);
          radiobutton1.setBounds(new Rectangle(76, 133, 93, 23));
          add(radiobutton2);
          radiobutton2.setBounds(new Rectangle(76, 155, 93, 23));
          add(textarea1);
          textarea1.setBounds(new Rectangle(74, 50, 259, 64));
          add(button1);
          button1.setBounds(new Rectangle(91, 275, 111, 23));
          add(button2);
          button2.setBounds(new Rectangle(216, 275, 116, 23));
          add(checkbox1);
          checkbox1.setBounds(new Rectangle(76, 190, 81, 23));
          add(combobox1);
          combobox1.setBounds(new Rectangle(217, 135, 114, 20));
          add(list1);
          list1.setBounds(new Rectangle(214, 164, 118, 80));
          add(table1);
          table1.setBounds(new Rectangle(348, 39, 259, 85));
          add(scrollpane1);
          scrollpane1.setBounds(new Rectangle(343, 151, 215, 95));
     }

}

