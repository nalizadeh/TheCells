/*---- Generated by CellsDesigner at 21.03.2008 ----*/

package org.nalizadeh.designer.examples;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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

public class MixExample extends JPanel {

     private BorderLayout layout = new BorderLayout(4, 4);

     private double[][] tabbedpane0_panel0_constraints = {
          {20.0, 10.0, 20.0, 5.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, },
          {77.0, 60.0, 60.0, 69.0, 60.0, 60.0, 60.0, 60.0, }
     };

     private CellsLayout tabbedpane0_panel0_layout = new CellsLayout(tabbedpane0_panel0_constraints, 4, 4);

     private LayoutManager tabbedpane0_panel1_layout = null;

     private JTabbedPane tabbedpane0 = new JTabbedPane();
     JPanel tabbedpane0_panel0 = new JPanel();
     private JLabel tabbedpane0_panel0_label0 = new JLabel();
     private JLabel tabbedpane0_panel0_label1 = new JLabel();
     private JTextField tabbedpane0_panel0_textfield0 = new JTextField();
     private JButton tabbedpane0_panel0_button0 = new JButton();
     private JLabel tabbedpane0_panel0_label2 = new JLabel();
     JPanel tabbedpane0_panel1 = new JPanel();

    /**
     * @param
     *
     * @return
     *
     * @exception
     *
     * @see
     */
     public MixExample() {
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

          tabbedpane0_panel0_label0.setFont(new Font("Tahoma Fett", 1, 12));
          tabbedpane0_panel0_label0.setText("Company:");
          tabbedpane0_panel0_label1.setText("Zip-Code:");
          tabbedpane0_panel0_textfield0.setText("");
          tabbedpane0_panel0_button0.setText("Search");
          tabbedpane0_panel0_label2.setAlignmentX(Component.RIGHT_ALIGNMENT);
          tabbedpane0_panel0_label2.setText("Adress:");
          tabbedpane0_panel0_label2.setVerticalTextPosition(SwingConstants.TOP);

          initTabbedpane0_panel0();
          initTabbedpane0_panel1();

          add(tabbedpane0, BorderLayout.CENTER);
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
     private void initTabbedpane0_panel0() {
          tabbedpane0_panel0.setLayout(tabbedpane0_panel0_layout);
          tabbedpane0_panel0.add(tabbedpane0_panel0_label0, new CellsLayout.Cell(0, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          tabbedpane0_panel0.add(tabbedpane0_panel0_label1, new CellsLayout.Cell(2, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          tabbedpane0_panel0.add(tabbedpane0_panel0_textfield0, new CellsLayout.Cell(2, 1, false, 0, 1, CellsLayout.LEFT, CellsLayout.TOP));
          tabbedpane0_panel0.add(tabbedpane0_panel0_button0, new CellsLayout.Cell(2, 3, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          tabbedpane0_panel0.add(tabbedpane0_panel0_label2, new CellsLayout.Cell(4, 0, false, 0, 0, CellsLayout.LEFT, CellsLayout.TOP));
          tabbedpane0.add("Common", tabbedpane0_panel0);
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
          tabbedpane0.add("Special", tabbedpane0_panel1);
     }

}

