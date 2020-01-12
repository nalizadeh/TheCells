/*---- Generated by CellsDesigner at 15.06.2007 ----*/

package org.nalizadeh.designer.examples;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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

public class BorderLayoutTest extends JPanel {

	private BorderLayout layout = new BorderLayout(4, 4);

	private BorderLayout panel0_layout = new BorderLayout(4, 4);

	private JComboBox combobox0 = new JComboBox();
	private JLabel label0 = new JLabel();
	private JScrollPane scrollpane0 = new JScrollPane();
	private JTextArea textarea0 = new JTextArea();
	private JPanel panel0 = new JPanel();
	private JButton panel0_button1 = new JButton();
	private JButton panel0_button0 = new JButton();

	/**
	 * @param
	 *
	 * @return
	 *
	 * @exception
	 *
	 * @see
	 */
	public BorderLayoutTest() {
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

		label0.setPreferredSize(new Dimension(47, 14));
		label0.setMaximumSize(new Dimension(47, 14));
		label0.setText("Selection:");
		label0.setVerticalAlignment(new Integer(1));
		label0.setMinimumSize(new Dimension(47, 14));
		scrollpane0.setPreferredSize(new Dimension(2, 20));
		scrollpane0.getViewport().add(textarea0);
		panel0_button1.setText("Cancel");
		panel0_button0.setText("Ok");

		initPanel0();

		add(combobox0, BorderLayout.NORTH);
		add(label0, BorderLayout.WEST);
		add(scrollpane0, BorderLayout.CENTER);
		add(panel0, BorderLayout.SOUTH);
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
		panel0.add(panel0_button1, BorderLayout.EAST);
		panel0.add(panel0_button0, BorderLayout.CENTER);
	}

}