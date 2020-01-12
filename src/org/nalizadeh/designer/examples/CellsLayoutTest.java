/*---- Generated by CellsDesigner at 21.03.2008 ----*/

package org.nalizadeh.designer.examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;
import org.nalizadeh.designer.util.beans.JCalendar;
import org.nalizadeh.designer.util.beans.JTime;

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

public class CellsLayoutTest extends JPanel {

     private double[][] constraints = {
          {CellsLayout.FILL, 145.0, 43.0, },
          {158.0, CellsLayout.FILL, CellsLayout.PREFERRED, CellsLayout.PREFERRED, }
     };

     private CellsLayout layout = new CellsLayout(constraints, 4, 4);

     private LayoutManager panel0_layout = null;

     private double[][] tabbedpane0_panel1_constraints = {
          {20.0, 20.0, CellsLayout.FILL, },
          {60.0, 60.0, CellsLayout.FILL, }
     };

     private CellsLayout tabbedpane0_panel1_layout = new CellsLayout(tabbedpane0_panel1_constraints, 4, 4);

     private double[][] tabbedpane0_panel2_constraints = {
          {20.0, },
          {60.0, }
     };

     private CellsLayout tabbedpane0_panel2_layout = new CellsLayout(tabbedpane0_panel2_constraints, 4, 4);

     private BorderLayout panel3_layout = new BorderLayout(4, 4);

     private FlowLayout panel4_layout = new FlowLayout(FlowLayout.LEFT, 5, 5);

     private JLabel label0 = new JLabel();
     private JScrollPane scrollpane0 = new JScrollPane();
     private JTextArea scrollpane0_textarea0 = new JTextArea();
     private JTime time1 = new JTime();
     private JCalendar calendar1 = new JCalendar();
      JPanel panel0 = new JPanel();
     private JScrollPane panel0_scrollpane1 = new JScrollPane();
     private JTree panel0_scrollpane1_tree0 = new JTree();
     private JLabel panel0_label1 = new JLabel();
     private JTabbedPane tabbedpane0 = new JTabbedPane();
      JPanel tabbedpane0_panel1 = new JPanel();
     private JLabel tabbedpane0_panel1_label2 = new JLabel();
     private JTextField tabbedpane0_panel1_textfield1 = new JTextField();
     private JPanel tabbedpane0_panel2 = new JPanel();
     private JSpinner tabbedpane0_panel2_spinner0 = new JSpinner();
      JPanel panel3 = new JPanel();
     private JLabel panel3_label3 = new JLabel();
     private JRadioButton panel3_radiobutton0 = new JRadioButton();
     private JTextArea panel3_textarea1 = new JTextArea();
     private JTree panel3_tree1 = new JTree();
     private JPanel panel4 = new JPanel();
     private JLabel panel4_label4 = new JLabel();
     private JTextField panel4_textfield0 = new JTextField();
     private JButton button0 = new JButton();
     private JButton button1 = new JButton();

    /**
     * @param
     *
     * @return
     *
     * @exception
     *
     * @see
     */
     public CellsLayoutTest() {
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

          setBackground(new Color(240, 239, 192));
          label0.setText("Hello");
          scrollpane0.getViewport().add(scrollpane0_textarea0);
          scrollpane0_textarea0.setText("");
          panel0_scrollpane1.getViewport().add(panel0_scrollpane1_tree0);
          panel0_label1.setText("Hello World");
          tabbedpane0_panel1.setBackground(new Color(255, 204, 204));
          tabbedpane0_panel1_label2.setText("Text");
          tabbedpane0_panel1_textfield1.setText("JTextField");
          panel3_label3.setText("Border");
          panel3_radiobutton0.setText("Radiobutton");
          panel3_textarea1.setText("");
          panel4_label4.setPreferredSize(new Dimension(31, 14));
          panel4_label4.setText("Name:");
          panel4_textfield0.setPreferredSize(new Dimension(55, 20));
          panel4_textfield0.setText("JTextField");
          button0.setText("Ok");
          button1.setText("Cancel");

          initPanel0();
          initPanel3();
          initPanel4();
          initTabbedpane0_panel1();
          initTabbedpane0_panel2();

          add(label0, new CellsLayout.Cell(0, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(scrollpane0, new CellsLayout.Cell(0, 1, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(time1, new CellsLayout.Cell(0, 2, true, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(calendar1, new CellsLayout.Cell(0, 3, true, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(panel0, new CellsLayout.Cell(1, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(tabbedpane0, new CellsLayout.Cell(1, 1, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(panel3, new CellsLayout.Cell(1, 2, false, 0, 1, CellsLayout.LEFT, CellsLayout.TOP));
          add(panel4, new CellsLayout.Cell(2, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(button0, new CellsLayout.Cell(2, 1, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          add(button1, new CellsLayout.Cell(2, 3, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
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
     private void initPanel0() {
          panel0.setLayout(panel0_layout);
          panel0.add(panel0_scrollpane1);
          panel0_scrollpane1.setBounds(new Rectangle(16, 29, 92, 73));
          panel0.add(panel0_label1);
          panel0_label1.setBounds(new Rectangle(7, 9, 70, 20));
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
     private void initPanel3() {
          panel3.setLayout(panel3_layout);
          panel3.add(panel3_label3, BorderLayout.NORTH);
          panel3.add(panel3_radiobutton0, BorderLayout.SOUTH);
          panel3.add(panel3_textarea1, BorderLayout.CENTER);
          panel3.add(panel3_tree1, BorderLayout.WEST);
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
     private void initPanel4() {
          panel4.setLayout(panel4_layout);
          panel4.add(panel4_label4);
          panel4_label4.setPreferredSize(new Dimension(31, 14));
          panel4.add(panel4_textfield0);
          panel4_textfield0.setPreferredSize(new Dimension(55, 20));
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
     private void initTabbedpane0_panel1() {
          tabbedpane0_panel1.setLayout(tabbedpane0_panel1_layout);
          tabbedpane0_panel1.add(tabbedpane0_panel1_label2, new CellsLayout.Cell(0, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          tabbedpane0_panel1.add(tabbedpane0_panel1_textfield1, new CellsLayout.Cell(0, 1, false, 2, 1, CellsLayout.LEFT, CellsLayout.TOP));
          tabbedpane0.add("tab1", tabbedpane0_panel1);
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
     private void initTabbedpane0_panel2() {
          tabbedpane0_panel2.setLayout(tabbedpane0_panel2_layout);
          tabbedpane0_panel2.add(tabbedpane0_panel2_spinner0, new CellsLayout.Cell(0, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          tabbedpane0.add("tab2", tabbedpane0_panel2);
     }

}

