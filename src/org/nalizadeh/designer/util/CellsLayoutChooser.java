/*
 * Copyright 2007 N.A.J. nalizadeh.org - All rights reserved.
 * nalizadeh.org PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package org.nalizadeh.designer.util;

import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.base.DesignerContainer;

import org.nalizadeh.designer.layouts.boxlayout.BoxLayout2;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;
import org.nalizadeh.designer.layouts.freelayout.FreeLayout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2007 N.A.J</p>
 *
 * <p>Organisation:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class CellsLayoutChooser extends CellsDialog {

	public static enum Layouts {
		CellsLayout, BorderLayout, FlowLayout, BoxLayout, GridLayout, GridBagLayout, CardLayout,
		FreeLayout
	}

        public static final String[] LayoutsNames = {
                "CellsLayout", //
                "BorderLayout", //
                "FlowLayout", //
                "BoxLayout", //
                "GridLayout", //
                "GridBagLayout", //
                "CardLayout", //
                "FreeLayout" //
        };


	private static LayoutPanel userPanel;

	public CellsLayoutChooser(CellsDesigner designer) {
		super(designer, "Choose Layout", false, true);

		userPanel = new LayoutPanel(designer);
		setUserPanel(userPanel);
		setSize(340, 320);
		setVisible(true);
	}

	public static LayoutPanel getLM() {
		return userPanel;
	}

	public Icon getActivityIcon() {
		return CellsUtil.getImage("New32");
	}

	public String getActivityTitle() {
		return "Choosing Layout";
	}

	public String getActivityDescription() {
		return "Choose layout und set it's properties";
	}

	/*
	 * Layout Panel
	 *
	 */
	public static class LayoutPanel extends JPanel {

		private CellsDesigner designer;
		private DesignerContainer container;

		private JLabel lbBClass;
		private JTextField tfBClass;
		private JButton btBClass;

		private JLabel lbLayout;
		private JComboBox coLayout;
		private JTextField tfLayout;
		private JButton btLayout;

		private JLabel lbHgap;
		private JSpinner spHgap;

		private JLabel lbVgap;
		private JSpinner spVgap;

		private JLabel lbRow;
		private JSpinner spRow;

		private JLabel lbCol;
		private JSpinner spCol;

		private JLabel lbAlign;
		private JComboBox coAlign;

		private JLabel lbAxis;
		private JComboBox coAxis;

		private boolean adjusting;

		private static String[] aligments = { "LEFT", "CENTER", "RIGHT", "LEADING", "TRALING" };
		private static String[] axises = new String[] { "X_AXIS", "Y_AXIS" };

		public LayoutPanel(CellsDesigner ds) {
			this.designer = ds;

			lbBClass = new JLabel("Base class:");
			tfBClass = new JTextField("javax.swing.JPanel");
			tfBClass.setEditable(false);
			btBClass = new JButton("...");
			btBClass.setEnabled(false);

			lbLayout = new JLabel("Layout:");
			tfLayout = new JTextField();
			tfLayout.setEditable(false);

			coLayout = new JComboBox(LayoutsNames);

			btLayout = new JButton("...");
			btLayout.setToolTipText("change layout");
			btLayout.setFocusable(false);
			btLayout.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						designer.getDesktop().changeLayout();
					}
				}
			);

			lbHgap = new JLabel("HGap:");
			spHgap = new JSpinner(new SpinnerNumberModel(4, 0, 100, 1));

			lbVgap = new JLabel("VGap:");
			spVgap = new JSpinner(new SpinnerNumberModel(4, 0, 100, 1));

			lbRow = new JLabel("Rows:");
			spRow = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

			lbCol = new JLabel("Columns:");
			spCol = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

			lbAlign = new JLabel("Alignment:");
			coAlign = new JComboBox(aligments);

			lbAxis = new JLabel("Axis:");
			coAxis = new JComboBox(axises);

			coLayout.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (!adjusting) {
							enableComponents();
						}
					}
				}
			);

			spRow.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						apply();
					}
				}
			);

			spCol.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						apply();
					}
				}
			);

			spHgap.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						apply();
					}
				}
			);

			spVgap.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						apply();
					}
				}
			);

			coAlign.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						apply();
					}
				}
			);

			coAxis.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						apply();
					}
				}
			);

			setLayout(
				new CellsLayout(
					new double[][] {
						{ 10, 22, 22, 22, 22, 22, 22 },
						{
							10, CellsLayout.PREFERRED, 50, CellsLayout.PREFERRED, 50, 20,
							CellsLayout.FILL, 22
						}
					},
					4,
					4
				)
			);

			add(lbBClass, "1, 1, 0, 0, 0");
			add(tfBClass, "1, 2, 0, 0, 2");
			add(btBClass, "1, 5");
			add(lbLayout, "2, 1, 0, 0, 0");
//          add(coLayout, "2, 2, 0, 0, 2");
			add(btLayout, "2, 5");
			add(lbHgap, "3,1");
			add(spHgap, "3,2");
			add(lbVgap, "3,3");
			add(spVgap, "3,4");
			add(lbRow, "4,1");
			add(spRow, "4,2");
			add(lbCol, "4,3");
			add(spCol, "4,4");
			add(lbAlign, "5,1");
			add(coAlign, "5,2,0,0,1");
			add(lbAxis, "6,1");
			add(coAxis, "6,2,0,0,1");

			adjusting = false;
			setContainer(null);
		}

		public void setContainer(DesignerContainer container) {

			this.container = container;

			adjusting = true;

			if (container != null) {
				String ln = determinLayoutName(container);
				if (ln != null) {
					coLayout.setSelectedItem(ln);
					remove(coLayout);
					add(tfLayout, "2, 2, 0, 0, 2");
					tfLayout.setText(ln);

					Object[] props = container.getLayoutProperty();

					btLayout.setVisible(true);
					spRow.setValue((Integer) props[0]);
					spCol.setValue((Integer) props[1]);
					spHgap.setValue((Integer) props[2]);
					spVgap.setValue((Integer) props[3]);
					coAlign.setSelectedItem(aligments[(Integer) props[4]]);
					coAxis.setSelectedItem(axises[(Integer) props[5]]);
				}
			} else {
				remove(tfLayout);
				add(coLayout, "2, 2, 0, 0, 2");

				btLayout.setVisible(false);
				spRow.setValue(1);
				spCol.setValue(1);
				spHgap.setValue(4);
				spVgap.setValue(4);
				coAlign.setSelectedIndex(0);
				coAxis.setSelectedIndex(0);
			}

			enableComponents();

			adjusting = false;
		}

		public String getLayoutName() {
			return (String) coLayout.getSelectedItem();
		}

		public int getRows() {
			return (Integer) spRow.getValue();
		}

		public int getCols() {
			return (Integer) spCol.getValue();
		}

		public int getHGap() {
			return (Integer) spHgap.getValue();
		}

		public int getVGap() {
			return (Integer) spVgap.getValue();
		}

		public int getAlignment() {
			return coAlign.getSelectedIndex();
		}

		public int getAxis() {
			return coAxis.getSelectedIndex();
		}

		public Layouts getLayoutType() {
			return Layouts.valueOf(getLayoutName());
		}

		public static Layouts getLayoutType(String name) {
			return Layouts.valueOf(name);
		}

		public String determinLayoutName(Container container) {
			LayoutManager lm = container.getLayout();
			if (lm instanceof FreeLayout || lm == null) {
				return "FreeLayout";
			} else if (lm instanceof BorderLayout) {
				return "BorderLayout";
			} else if (lm instanceof CardLayout) {
				return "CardLayout";
			} else if (lm instanceof FlowLayout) {
				return "FlowLayout";
			} else if (lm instanceof BoxLayout2) {
				return "BoxLayout";
			} else if (lm instanceof GridLayout) {
				return "GridLayout";
			} else if (lm instanceof GridBagLayout) {
				return "GridBagLayout";
			} else if (lm instanceof CellsLayout) {
				return "CellsLayout";
			}
			return null;
		}

		public static Layouts getLayoutType(Container container) {
			LayoutManager lm = container.getLayout();
			if (lm instanceof FreeLayout || lm == null) {
				return Layouts.FreeLayout;
			} else if (lm instanceof BorderLayout) {
				return Layouts.BorderLayout;
			} else if (lm instanceof CardLayout) {
				return Layouts.CardLayout;
			} else if (lm instanceof FlowLayout) {
				return Layouts.FlowLayout;
			} else if (lm instanceof BoxLayout2) {
				return Layouts.BoxLayout;
			} else if (lm instanceof GridLayout) {
				return Layouts.GridLayout;
			} else if (lm instanceof GridBagLayout) {
				return Layouts.GridBagLayout;
			} else if (lm instanceof CellsLayout) {
				return Layouts.CellsLayout;
			}

			throw new IllegalArgumentException("Unknown Layout Type!");
		}

		public void setLayoutType(Container container) {

			Layouts la = getLayoutType();

			if (la == Layouts.FreeLayout) {
				container.setLayout(new FreeLayout());
			} else if (la == Layouts.BorderLayout) {
				container.setLayout(new BorderLayout(getHGap(), getVGap()));
			} else if (la == Layouts.CardLayout) {
				container.setLayout(new CardLayout(getHGap(), getVGap()));
			} else if (la == Layouts.FlowLayout) {
				container.setLayout(new FlowLayout(getAlignment(), getHGap(), getVGap()));
			} else if (la == Layouts.BoxLayout) {
				container.setLayout(new BoxLayout2(container, getAxis()));
			} else if (la == Layouts.GridLayout) {
				container.setLayout(new GridLayout(getRows(), getCols(), getHGap(), getVGap()));
			} else if (la == Layouts.GridBagLayout) {
				container.setLayout(new GridBagLayout());
			} else if (la == Layouts.CellsLayout) {
				container.setLayout(new CellsLayout(getRows(), getCols(), getHGap(), getVGap()));
			}
		}

		private void enableComponents() {

			tfBClass.setEditable(false);

			spRow.setEnabled(true);
			spCol.setEnabled(true);
			spHgap.setEnabled(true);
			spVgap.setEnabled(true);
			coAlign.setEnabled(true);
			coAxis.setEnabled(true);

			switch (coLayout.getSelectedIndex()) {

				case 0 :
				case 4 :
					coAlign.setEnabled(false);
					coAxis.setEnabled(false);
					break;

				case 1 :
				case 6 :
					coAlign.setEnabled(false);
					coAxis.setEnabled(false);
					spRow.setEnabled(false);
					spCol.setEnabled(false);
					break;

				case 2 :
					spRow.setEnabled(false);
					spCol.setEnabled(false);
					coAxis.setEnabled(false);
					break;

				case 3 :
					spRow.setEnabled(false);
					spCol.setEnabled(false);
					spHgap.setEnabled(false);
					spVgap.setEnabled(false);
					coAlign.setEnabled(false);
					break;

				case 5 :
				case 7 :
					spRow.setEnabled(false);
					spCol.setEnabled(false);
					spHgap.setEnabled(false);
					spVgap.setEnabled(false);
					coAlign.setEnabled(false);
					coAxis.setEnabled(false);
					break;
			}
		}

		private void apply() {
			if (container != null && !adjusting) {
				container.updateLayout(
					spRow.getValue(),
					spCol.getValue(),
					spHgap.getValue(),
					spVgap.getValue(),
					coAlign.getSelectedIndex(),
					coAxis.getSelectedIndex()
				);

				designer.redraw();
			}
		}
	}
}
