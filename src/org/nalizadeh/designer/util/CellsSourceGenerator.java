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

import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.boxlayout.BoxLayout2;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;
import org.nalizadeh.designer.layouts.freelayout.FreeLayout;

import org.nalizadeh.designer.property.Property;
import org.nalizadeh.designer.property.jbeans.JBeansProperty;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007 N.A.J
 * </p>
 * 
 * <p>
 * Organisation:
 * </p>
 * 
 * @author Nader Alizadeh
 * @version 1.0
 */
public class CellsSourceGenerator {

	private static final String newline = CellsUtil.LINE_SEPARATOR;

	private static final String[] fills = { "NONE", "BOTH", "HORIZONTAL",
			"VERTICAL" };

	private static final int[] fillsv = { GridBagConstraints.NONE,
			GridBagConstraints.BOTH, GridBagConstraints.HORIZONTAL,
			GridBagConstraints.VERTICAL };

	private static final String[] anchors = { "CENTER", "NORTH", "NORTHEAST",
			"EAST", "SOUTHEAST", "SOUTH", "SOUTHWEST", "WEST", "NORTHWEST",
			"PAGE_START", "PAGE_END", "LINE_START", "LINE_END",
			"FIRST_LINE_START", "FIRST_LINE_END", "LAST_LINE_START",
			"LAST_LINE_END" };

	private static final int[] anchorsv = { GridBagConstraints.CENTER,
			GridBagConstraints.NORTH, GridBagConstraints.NORTHEAST,
			GridBagConstraints.EAST, GridBagConstraints.SOUTHEAST,
			GridBagConstraints.SOUTH, GridBagConstraints.SOUTHWEST,
			GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,
			GridBagConstraints.PAGE_START, GridBagConstraints.PAGE_END,
			GridBagConstraints.LINE_START, GridBagConstraints.LINE_END,
			GridBagConstraints.FIRST_LINE_START,
			GridBagConstraints.FIRST_LINE_END,
			GridBagConstraints.LAST_LINE_START,
			GridBagConstraints.LAST_LINE_END };

	private static final String[] aligmentX = { "CENTER_ALIGNMENT",
			"LEFT_ALIGNMENT", "RIGHT_ALIGNMENT" };

	private static final Object[] aligmentXv = { Component.CENTER_ALIGNMENT,
			Component.LEFT_ALIGNMENT, Component.RIGHT_ALIGNMENT };

	private static final String[] aligmentY = { "CENTER_ALIGNMENT",
			"TOP_ALIGNMENT", "BOTTOM_ALIGNMENT" };

	private static final Object[] aligmentYv = { Component.CENTER_ALIGNMENT,
			Component.TOP_ALIGNMENT, Component.BOTTOM_ALIGNMENT };

	private static final String[] hAligment = { "LEFT", "CENTER", "RIGHT",
			"LEADING", "TRAILING" };

	private static final Object[] hAligmentv = { SwingConstants.LEFT,
			SwingConstants.CENTER, SwingConstants.RIGHT,
			SwingConstants.LEADING, SwingConstants.TRAILING };

	private static final String[] vAligment = { "TOP", "CENTER", "BOTTOM" };

	private static final Object[] vAligmentv = { SwingConstants.TOP,
			SwingConstants.CENTER, SwingConstants.BOTTOM };

	private static final String[] orientation = { "LEFT_TO_RIGHT",
			"RIGHT_TO_LEFT" };

	private static final Object[] orientationv = {
			ComponentOrientation.LEFT_TO_RIGHT,
			ComponentOrientation.RIGHT_TO_LEFT };

	private static final String[] splitorientation = { "VERTICAL_SPLIT",
			"HORIZONTAL_SPLIT" };

	private static final Object[] splitorientationv = {
			JSplitPane.VERTICAL_SPLIT, JSplitPane.HORIZONTAL_SPLIT };

	private static final String[] tabplacement = { "TOP", "LEFT", "RIGHT",
			"BOTTOM" };

	private static final Integer[] tabplacementv = { JTabbedPane.TOP,
			JTabbedPane.LEFT, JTabbedPane.RIGHT, JTabbedPane.BOTTOM };

	private CellsDesigner designer;
	private Map<String, String> imports = new TreeMap<String, String>();
	private Map<String, StringBuffer> procs = new TreeMap<String, StringBuffer>();
	private Map<String, StringBuffer> incls = new TreeMap<String, StringBuffer>();
	private Container contentPane;

	public CellsSourceGenerator(CellsDesigner designer) {
		this.designer = designer;
	}

	public StringBuffer generate() {

		DesignerFrame df = designer.getDesktop().getDesigner();

		StringBuffer sb = new StringBuffer();
		StringBuffer lines = new StringBuffer();

		imports.clear();
		procs.clear();
		incls.clear();

		if (df != null) {
			contentPane = df.getContainer();
			doGenerate(df.getName(), contentPane, lines);

			GregorianCalendar gc = new GregorianCalendar();
			int y = gc.get(Calendar.YEAR);
			int m = gc.get(Calendar.MONTH) + 1;
			int d = gc.get(Calendar.DAY_OF_MONTH);
			String ds = (d < 10 ? ("0" + d) : d) + "."
					+ (m < 10 ? ("0" + m) : m) + "." + y;

			sb.append("/*---- Generated by The Cells at " + ds + " ----*/"
					+ newline + newline);
			sb.append("package " + df.getPackage() + ";" + newline + newline);

			Set<String> im = imports.keySet();
			for (Iterator<String> it = im.iterator(); it.hasNext();) {
				sb.append("import " + it.next() + ";" + newline);
			}

			sb.append(newline);

			sb.append("/**" + newline);
			sb.append("* <p>Title:</p>" + newline);
			sb.append("*" + newline);
			sb.append("* <p>Description:</p>" + newline);
			sb.append("*" + newline);
			sb.append("* <p>Copyright: Copyright (c) 2007</p>" + newline);
			sb.append("*" + newline);
			sb.append("* <p>Organisation:</p>" + newline);
			sb.append("*" + newline);
			sb.append("* @author   undefined" + newline);
			sb.append("* @version  1.0" + newline);
			sb.append("*/" + newline + newline);

			sb.append(lines);
			sb.append(newline);
		}
		return sb;
	}

	private void doGenerate(String title, Container container, StringBuffer lines) {

		imports.put(JPanel.class.getName(), "");

		lines.append("public class " + title + " extends JPanel {" + newline + newline);

		scanLayout(container, lines, false);
		scanVariables(container, lines);

		lines.append(newline);
		lines.append(space(4) + "/**" + newline);
		lines.append(space(5) + "* @param" + newline);
		lines.append(space(5) + "*" + newline);
		lines.append(space(5) + "* @return" + newline);
		lines.append(space(5) + "*" + newline);
		lines.append(space(5) + "* @exception" + newline);
		lines.append(space(5) + "*" + newline);
		lines.append(space(5) + "* @see" + newline);
		lines.append(space(5) + "*/" + newline);

		lines.append(space(5) + "public " + title + "() {" + newline);
		lines.append(space(10) + "doInit();" + newline);
		lines.append(space(5) + "}" + newline + newline);

		lines.append(space(4) + "/**" + newline);
		lines.append(space(5) + "* @param" + newline);
		lines.append(space(5) + "*" + newline);
		lines.append(space(5) + "* @return" + newline);
		lines.append(space(5) + "*" + newline);
		lines.append(space(5) + "* @exception" + newline);
		lines.append(space(5) + "*" + newline);
		lines.append(space(5) + "* @see" + newline);
		lines.append(space(5) + "*/" + newline);

		lines.append(space(5) + "private void doInit() {" + newline);
		lines.append(space(10) + "setLayout(layout);" + newline);
		lines.append(newline);

		scanComponents(container, lines);
		lines.append(newline);

		scanListeners(container, lines);

		Collection<String> procNames = procs.keySet();
		for (String s : procNames) {
			lines.append(space(10) + s + "();" + newline);
		}
		lines.append(newline);

		scanConstraints(container, lines, false);

		lines.append(space(5) + "}" + newline + newline);

		for (String s : procNames) {
			lines.append(space(4) + "/**" + newline);
			lines.append(space(5) + "* @param" + newline);
			lines.append(space(5) + "*" + newline);
			lines.append(space(5) + "* @return" + newline);
			lines.append(space(5) + "*" + newline);
			lines.append(space(5) + "* @exception" + newline);
			lines.append(space(5) + "*" + newline);
			lines.append(space(5) + "* @see" + newline);
			lines.append(space(5) + "*/" + newline);

			lines.append(space(5) + "private void " + s + "() {" + newline);
			lines.append(procs.get(s));
			lines.append(space(5) + "}" + newline + newline);
		}

		Collection<String> inclNames = incls.keySet();

		for (String s : inclNames) {
			lines.append(space(4) + "/**" + newline);
			lines.append(space(5) + "*" + newline);
			lines.append(space(5) + "*" + newline);
			lines.append(space(5) + "*/" + newline);

			String sub = s.substring(s.lastIndexOf("_") + 1);

			lines.append(space(5) + "private class " + s + " implements " + sub
					+ " {" + newline);
			lines.append(incls.get(s));
			lines.append(space(5) + "}" + newline + newline);
		}

		lines.append("}" + newline);
	}

	private void scanLayout(Container container, StringBuffer lines,
			boolean isSub) {

		if (CellsUtil.isPanel(container)) {

			LayoutManager lm = container.getLayout();

			String s = isSub ? container.getName() + "_" : "";

			if (lm instanceof FreeLayout || lm == null) {
				imports.put(Rectangle.class.getName(), "");
				imports.put(LayoutManager.class.getName(), "");
				lines.append(space(5) + "private LayoutManager " + s
						+ "layout = null;" + newline + newline);
			} else if (lm instanceof CellsLayout) {

				imports.put(CellsLayout.class.getName(), "");

				lines.append(space(5) + "private double[][] " + s
						+ "constraints = {" + newline);
				lines.append(space(10) + "{");

				CellsLayout l = (CellsLayout) lm;

				for (CellsLayout.CellRow row : l.getRows()) {
					lines.append(row.toString() + ", ");
				}
				lines.append("}," + newline);
				lines.append(space(10) + "{");

				for (CellsLayout.CellColumn col : l.getColumns()) {
					lines.append(col.toString() + ", ");
				}
				lines.append("}" + newline);
				lines.append(space(5) + "};" + newline + newline);

				lines.append(space(5) + "private CellsLayout " + s
						+ "layout = new CellsLayout(" + s + "constraints, "
						+ l.getHgap() + ", " + l.getVgap() + ");" + newline
						+ newline);

			} else if (lm instanceof BorderLayout) {

				imports.put(BorderLayout.class.getName(), "");
				BorderLayout la = (BorderLayout) lm;

				lines.append(space(5) + "private BorderLayout " + s
						+ "layout = new BorderLayout(" + la.getHgap() + ", "
						+ la.getVgap() + ");" + newline + newline);

			} else if (lm instanceof CardLayout) {

				imports.put(CardLayout.class.getName(), "");
				CardLayout la = (CardLayout) lm;

				lines.append(space(5) + "private CardLayout " + s
						+ "layout = new CardLayout(" + la.getHgap() + ", "
						+ la.getVgap() + ");" + newline + newline);

			} else if (lm instanceof FlowLayout) {
				String[] al = { "LEFT", "CENTER", "RIGHT", "LEADING",
						"TRAILING" };

				imports.put(FlowLayout.class.getName(), "");

				FlowLayout la = (FlowLayout) lm;

				lines.append(space(5) + "private FlowLayout " + s
						+ "layout = new FlowLayout(" + "FlowLayout."
						+ al[la.getAlignment()] + ", " + la.getHgap() + ", "
						+ la.getVgap() + ");" + newline + newline);
			} else if (lm instanceof BoxLayout2) {
				String[] al = { "X_AXIS", "Y_AXIS" };

				imports.put(BoxLayout2.class.getName(), "");

				BoxLayout2 la = (BoxLayout2) lm;

				lines.append(space(5) + "private BoxLayout2 " + s
						+ "layout = new BoxLayout2(" + "this, " + "BoxLayout2."
						+ al[la.getAxis2()] + ");" + newline + newline);
			} else if (lm instanceof GridLayout) {
				imports.put(GridLayout.class.getName(), "");

				GridLayout la = (GridLayout) lm;

				lines.append(space(5) + "private GridLayout " + s
						+ "layout = new GridLayout(" + +la.getRows() + ", "
						+ la.getColumns() + ", " + la.getHgap() + ", "
						+ la.getVgap() + ");" + newline + newline);
			} else if (lm instanceof GridBagLayout) {
				imports.put(GridBagLayout.class.getName(), "");

				lines.append(space(5) + "private GridBagLayout " + s
						+ "layout = new GridBagLayout();" + newline + newline);
			}
		}

		Component[] comps = container.getComponents();
		for (Component comp : comps) {
			if (CellsUtil.isPanel(comp)) {
				scanLayout((Container) comp, lines, true);

			} else if (comp instanceof JScrollPane) {
				JScrollPane sp = (JScrollPane) comp;
				if (sp.getViewport().getComponentCount() > 0) {
					Component co = sp.getViewport().getComponent(0);
					if (CellsUtil.isContainer(co)) {
						scanLayout((Container) co, lines, true);
					}
				}
			} else if (comp instanceof JTabbedPane) {
				JTabbedPane tp = (JTabbedPane) comp;
				for (int i = 0; i < tp.getComponentCount(); i++) {
					Component co = tp.getComponent(i);
					if (CellsUtil.isContainer(co)) {
						scanLayout((Container) co, lines, true);
					}
				}
			} else if (comp instanceof JSplitPane) {
				JSplitPane st = (JSplitPane) comp;
				Component[] cp = new Component[2];
				if (st.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
					cp[0] = st.getLeftComponent();
					cp[1] = st.getRightComponent();
				} else {
					cp[0] = st.getTopComponent();
					cp[1] = st.getBottomComponent();
				}

				for (int i = 0; i < 2; i++) {
					if (CellsUtil.isContainer(cp[i])) {
						scanLayout((Container) cp[i], lines, true);
					}
				}
			}
		}
	}

	private void scanVariables(Container container, StringBuffer lines) {

		Component[] comps = container.getComponents();
		for (Component comp : comps) {

			doScanVariables(comp, lines);

			if (CellsUtil.isPanel(comp)) {
				scanVariables((Container) comp, lines);

			} else if (comp instanceof JScrollPane) {
				JScrollPane sp = (JScrollPane) comp;
				if (sp.getViewport().getComponentCount() > 0) {
					Component co = sp.getViewport().getComponent(0);
					doScanVariables(co, lines);
					if (CellsUtil.isContainer(co)) {
						scanVariables((Container) co, lines);
					}
				}
			} else if (comp instanceof JTabbedPane) {
				JTabbedPane tp = (JTabbedPane) comp;
				Component[] cos = tp.getComponents();

				List<Component> coms = new ArrayList<Component>();
				for (int i = 0; i < cos.length; i++) {
					String t = tp.getTitleAt(i);
					if (t != null) {
						coms.add(cos[i]);
					}
				}

				for (Component co : coms) {
					doScanVariables(co, lines);
					if (co instanceof JScrollPane) {
						JScrollPane sp = (JScrollPane) comp;
						if (sp.getViewport().getComponentCount() > 0) {
							Component co2 = sp.getViewport().getComponent(0);
							doScanVariables(co2, lines);
							if (CellsUtil.isContainer(co2)) {
								scanVariables((Container) co2, lines);
							}
						}
					} else if (CellsUtil.isContainer(co)) {
						scanVariables((Container) co, lines);
					}
				}
			} else if (comp instanceof JSplitPane) {
				JSplitPane st = (JSplitPane) comp;
				Component[] cp = new Component[2];
				if (st.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
					cp[0] = st.getLeftComponent();
					cp[1] = st.getRightComponent();
				} else {
					cp[0] = st.getTopComponent();
					cp[1] = st.getBottomComponent();
				}

				for (int i = 0; i < 2; i++) {
					doScanVariables(cp[i], lines);
					if (cp[i] instanceof JScrollPane) {
						JScrollPane sp = (JScrollPane) comp;
						if (sp.getViewport().getComponentCount() > 0) {
							Component co2 = sp.getViewport().getComponent(0);
							doScanVariables(co2, lines);
							if (CellsUtil.isContainer(co2)) {
								scanVariables((Container) co2, lines);
							}
						}
					} else if (CellsUtil.isContainer(cp[i])) {
						scanVariables((Container) cp[i], lines);
					}
				}
			}
		}
	}

	private void doScanVariables(Component comp, StringBuffer lines) {

		String cl = comp.getClass().getName();
		if (cl.startsWith("org.nalizadeh.designer.layouts.")) {
			cl = "javax.swing.JPanel";
		}

		imports.put(cl, "");

		String vs = designer.getProperty().getVisibilityName(comp);
		if (vs.equals("package protected")) {
			vs = "";
		} else {
			vs = vs + " ";
		}

		cl = cl.substring(cl.lastIndexOf(".") + 1, cl.length());
		lines.append(space(5) + vs + cl + " " + comp.getName() + " = new " + cl
				+ "();" + newline);
	}

	private void scanComponents(Container container, StringBuffer lines) {

		if (container == contentPane) {
			Collection<JBeansProperty> props = designer.getProperty()
					.getContainerProperties(container);
			for (JBeansProperty p : props) {
				if (p.isChanged()) {
					String n = p.getName();
					String wm = "set" + n.toUpperCase().charAt(0)
							+ n.substring(1);
					lines.append(space(10) + wm + "(");
					scanProperty(n, p.getValue(), null, lines);
					lines.append(");" + newline);
				}
			}
		}

		for (Component comp : container.getComponents()) {

			scanSetter(comp, lines);

			if (CellsUtil.isPanel(comp)) {
				scanComponents((Container) comp, lines);
				StringBuffer newlines = new StringBuffer();
				scanConstraints((Container) comp, newlines, true);
				String name = "init"
						+ comp.getName().substring(0, 1).toUpperCase()
						+ comp.getName().substring(1);
				procs.put(name, newlines);

			} else if (comp instanceof JTabbedPane) {
				JTabbedPane tp = (JTabbedPane) comp;
				Component[] coms = tp.getComponents();

				List<String> kes = new ArrayList<String>();
				List<Component> cos = new ArrayList<Component>();
				for (int i = 0; i < coms.length; i++) {
					String t = tp.getTitleAt(i);
					if (t != null) {
						kes.add(t);
						cos.add(coms[i]);
					}
				}

				int i = 0;
				for (Component co : cos) {
					scanSetter(co, lines);
					if (CellsUtil.isContainer(co)) {
						scanComponents((Container) co, lines);
						StringBuffer newlines = new StringBuffer();
						scanConstraints((Container) co, newlines, true);
						String name = "init"
								+ co.getName().substring(0, 1).toUpperCase()
								+ co.getName().substring(1);
						procs.put(name, newlines);
						newlines.append(space(10) + tp.getName() + ".add(\""
								+ kes.get(i++) + "\", " + co.getName() + ");"
								+ newline);
					}
				}
			} else if (comp instanceof JSplitPane) {
				JSplitPane sp = (JSplitPane) comp;
				Component[] cp = new Component[2];
				if (sp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
					cp[0] = sp.getLeftComponent();
					cp[1] = sp.getRightComponent();
				} else {
					cp[0] = sp.getTopComponent();
					cp[1] = sp.getBottomComponent();
				}

				for (int i = 0; i < 2; i++) {
					scanSetter(cp[i], lines);
					if (CellsUtil.isContainer(cp[i])) {
						scanComponents((Container) cp[i], lines);
						StringBuffer newlines = new StringBuffer();
						scanConstraints((Container) cp[i], newlines, true);

						String name = "init"
								+ cp[i].getName().substring(0, 1).toUpperCase()
								+ cp[i].getName().substring(1);
						procs.put(name, newlines);
						String s = "";
						if (sp.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
							s = i == 0 ? ".setLeftComponent("
									: ".setRightComponent(";
						} else {
							s = i == 0 ? ".setTopComponent("
									: ".setBottomComponent(";
						}
						newlines.append(space(10) + sp.getName() + s
								+ cp[i].getName() + ");" + newline);
					}
				}
			} else if (comp instanceof JScrollPane) {
				JScrollPane sp = (JScrollPane) comp;
				if (sp.getViewport().getComponentCount() > 0) {
					Component co = sp.getViewport().getComponent(0);
					lines.append(space(10) + sp.getName()
							+ ".getViewport().add(" + co.getName() + ");"
							+ newline);
					scanSetter(co, lines);
				}
			}
		}
	}

	private void scanSetter(Component comp, StringBuffer lines) {
		Collection<Property> props = designer.getProperty()
				.getComponentProperties(comp);

		if (props != null) {
			for (Property prop : props) {
				if (!prop.isLayoutProperty && prop.changed) {
					if (prop.newValue != null) {
						String wm = "set" + prop.name.toUpperCase().charAt(0)
								+ prop.name.substring(1);

						lines.append(space(10) + comp.getName() + "." + wm
								+ "(");
						scanProperty(prop.name, prop.newValue, prop.comp, lines);
						lines.append(");" + newline);
					}
				}
			}
		}
	}

	private void scanConstraints(Container container, StringBuffer lines,
			boolean isSub) {

		if (isSub) {
			lines.append(space(10) + container.getName() + ".setLayout("
					+ container.getName() + "_layout);" + newline);
		}

		String n = isSub ? container.getName() + "." : "";

		for (Component comp : container.getComponents()) {
			LayoutManager lm = container.getLayout();

			if (lm instanceof FreeLayout || lm == null) {
				Rectangle b = comp.getBounds();
				String s = "new Rectangle(" + b.x + ", " + b.y + ", " + b.width
						+ ", " + b.height + ")";
				lines.append(space(10) + n + "add(" + comp.getName() + ");"
						+ newline);
				lines.append(space(10) + comp.getName() + ".setBounds(" + s
						+ ");" + newline);
			} else if (lm instanceof CardLayout) {
				CardLayout l = (CardLayout) lm;
				lines
						.append(space(10) + n + "add(" + comp.getName()
								+ ", \"" + container.getComponentZOrder(comp)
								+ "\");" + newline);
			} else if (lm instanceof CellsLayout) {
				CellsLayout l = (CellsLayout) lm;
				String s = ((CellsLayout.Cell) l.getConstraints(comp))
						.toDesignerString();
				lines.append(space(10) + n + "add(" + comp.getName() + ", " + s
						+ ");" + newline);

			} else if (lm instanceof BorderLayout) {
				BorderLayout l = (BorderLayout) lm;
				String s = "BorderLayout."
						+ l.getConstraints(comp).toString().toUpperCase();
				lines.append(space(10) + n + "add(" + comp.getName() + ", " + s
						+ ");" + newline);

			} else if (lm instanceof CardLayout) {
				lines.append(space(10) + n + "add(" + comp.getName() + ");"
						+ newline);

			} else if (lm instanceof FlowLayout) {

				imports.put(Dimension.class.getName(), "");

				Dimension d = comp.getPreferredSize();
				String s = "new Dimension(" + d.width + ", " + d.height + ")";
				lines.append(space(10) + n + "add(" + comp.getName() + ");"
						+ newline);
				lines.append(space(10) + comp.getName() + ".setPreferredSize("
						+ s + ");" + newline);

			} else if (lm instanceof BoxLayout2) {
				lines.append(space(10) + n + "add(" + comp.getName() + ");"
						+ newline);

			} else if (lm instanceof GridLayout) {
				lines.append(space(10) + n + "add(" + comp.getName() + ");"
						+ newline);

			} else if (lm instanceof GridBagLayout) {

				imports.put(GridBagConstraints.class.getName(), "");
				imports.put(Insets.class.getName(), "");

				GridBagLayout l = (GridBagLayout) lm;
				GridBagConstraints cons = l.getConstraints(comp);

				String f = "";
				for (int i = 0; i < fillsv.length; i++) {
					if (cons.fill == fillsv[i]) {
						f = fills[i];
						break;
					}
				}

				String a = "";
				for (int i = 0; i < anchorsv.length; i++) {
					if (cons.anchor == anchorsv[i]) {
						a = anchors[i];
						break;
					}
				}

				String s = "new GridBagConstraints(" + cons.gridx + ", "
						+ cons.gridy + ", " + cons.gridheight + ", "
						+ cons.gridheight + ", "
						+ ("" + cons.weightx).substring(0, 2) + ", "
						+ ("" + cons.weighty).substring(0, 2) + "," + newline
						+ space(15) + "GridBagConstraints." + a
						+ ", GridBagConstraints." + f + ", " + "new Insets("
						+ cons.insets.top + ", " + cons.insets.left + ", "
						+ cons.insets.bottom + ", " + cons.insets.right + "), "
						+ cons.ipadx + ", " + cons.ipady + ")";

				lines.append(space(10) + n + "add(" + comp.getName() + ", " + s
						+ ");" + newline);
			}
		}
	}

	private void scanProperty(String name, Object value, Component comp,
			StringBuffer lines) {

		if (name.equals("horizontalAlignment") || name.equals("hAlignment")
				|| name.equals("horizontalTextPosition")) {
			imports.put(SwingConstants.class.getName(), "");
			for (int i = 0; i < hAligmentv.length; i++) {
				if (value.equals(hAligmentv[i])) {
					lines.append("SwingConstants." + hAligment[i]);
					break;
				}
			}

		} else if (name.equals("verticalAlignment")
				|| name.equals("vAlignment")
				|| name.equals("verticalTextPosition")) {
			imports.put(SwingConstants.class.getName(), "");
			for (int i = 0; i < vAligmentv.length; i++) {
				if (value.equals(vAligmentv[i])) {
					lines.append("SwingConstants." + vAligment[i]);
					break;
				}
			}
		} else if (name.equals("alignmentX")) {
			imports.put(Component.class.getName(), "");
			for (int i = 0; i < aligmentXv.length; i++) {
				if (value.equals(aligmentXv[i])) {
					lines.append("Component." + aligmentX[i]);
					break;
				}
			}

		} else if (name.equals("alignmentY")) {
			imports.put(Component.class.getName(), "");
			for (int i = 0; i < aligmentYv.length; i++) {
				if (value.equals(aligmentYv[i])) {
					lines.append("Component." + aligmentY[i]);
					break;
				}
			}

		} else if (name.equals("componentOrientation")) {
			imports.put(ComponentOrientation.class.getName(), "");
			for (int i = 0; i < orientationv.length; i++) {
				if (value.equals(orientationv[i])) {
					lines.append("ComponentOrientation." + orientation[i]);
					break;
				}
			}

		} else if (name.equals("orientation")) {
			imports.put(JSplitPane.class.getName(), "");
			for (int i = 0; i < splitorientationv.length; i++) {
				if (value.equals(splitorientationv[i])) {
					lines.append("JSplitPane." + splitorientation[i]);
					break;
				}
			}
		} else if (name.equals("tabPlacement")) {
			imports.put(JTabbedPane.class.getName(), "");
			for (int i = 0; i < tabplacementv.length; i++) {
				if (value.equals(tabplacementv[i])) {
					lines.append("JTabbedPane." + tabplacement[i]);
					break;
				}
			}
		} else if (name.equals("locale")) {
			imports.put(Locale.class.getName(), "");
			lines.append("new Locale(\"" + value + "\")");

		} else if (name.equals("leftComponent")
				|| name.equals("rightComponent") || name.equals("topComponent")
				|| name.equals("bottomComponent")) {
			lines.append(((Component) value).getName());

		} else if (name.equals("model")) {
			if (comp instanceof JTable) {
				imports.put(DefaultTableModel.class.getName(), "");
				JTable tb = (JTable) comp;
				TableModel md = tb.getModel();
				if (md == null) {

					lines
							.append("new DefaultTableModel( "
									+ newline
									+ space(15)
									+ "new Object [][] {"
									+ newline
									+ space(20)
									+ "{null, null, null, null},"
									+ newline
									+ space(20)
									+ "{null, null, null, null},"
									+ newline
									+ space(20)
									+ "{null, null, null, null},"
									+ newline
									+ space(20)
									+ "{null, null, null, null}"
									+ newline
									+ space(15)
									+ "},"
									+ newline
									+ space(15)
									+ "new String [] {"
									+ newline
									+ space(20)
									+ "\"Title 1\", \"Title 2\", \"Title 3\", \"Title 4\""
									+ newline + space(15) + "}" + newline
									+ space(10) + ")");
				} else {

					lines.append("new DefaultTableModel( " + newline
							+ space(15) + "new Object [][] {" + newline
							+ space(20));

					for (int r = 0; r < md.getRowCount(); r++) {
						StringBuffer sb = new StringBuffer("{");
						for (int c = 0; c < md.getColumnCount(); c++) {
							sb.append("null, ");
						}
						sb.append("}," + newline + space(20));
						lines.append(sb.toString());
					}
					lines.append(newline + space(15) + "},");
					lines.append(newline + space(15) + "new String [] {"
							+ newline + space(20));

					StringBuffer sb = new StringBuffer();
					for (int c = 0; c < md.getColumnCount(); c++) {
						sb.append("\"" + md.getColumnName(c) + "\", ");
					}
					lines.append(sb.toString());
					lines.append(newline + space(15) + "}" + newline
							+ space(10) + ")");
				}
			}

		} else if (value instanceof Integer) {
			Integer i = (Integer) value;
			lines.append("new Integer(" + i.intValue() + ")");

		} else if (value instanceof Double) {
			Double d = (Double) value;
			lines.append("new Double(" + d.doubleValue() + ")");

		} else if (value instanceof Float) {
			Float f = (Float) value;
			lines.append("new Float(" + f.floatValue() + ")");

		} else if (value instanceof String) {
			lines.append("\"" + value.toString() + "\"");

		} else if (value instanceof Boolean) {
			boolean b = ((Boolean) value).booleanValue();
			lines.append("Boolean." + (b ? "TRUE" : "FALSE"));

		} else if (value instanceof Dimension) {
			imports.put(Dimension.class.getName(), "");
			Dimension d = (Dimension) value;
			lines.append("new Dimension(" + (int) d.getWidth() + ", "
					+ (int) d.getHeight() + ")");

		} else if (value instanceof Color) {
			imports.put(Color.class.getName(), "");
			Color c = (Color) value;
			lines.append("new Color(" + c.getRed() + ", " + c.getGreen() + ", "
					+ c.getBlue() + ")");

		} else if (value instanceof Font) {
			imports.put(Font.class.getName(), "");
			Font f = (Font) value;
			lines.append("new Font(\"" + f.getFontName() + "\", "
					+ f.getStyle() + ", " + f.getSize() + ")");

		} else if (value instanceof Border) {
			imports.put(BorderFactory.class.getName(), "");
			Border b = (Border) value;
			if (b instanceof CompoundBorder) {
				lines.append("BorderFactory.createCompoundBorder()");
			} else if (b instanceof EtchedBorder) {
				lines.append("BorderFactory.createEtchedBorder()");
			} else if (b instanceof BevelBorder) {
				BevelBorder bb = (BevelBorder) b;
				if (bb.getBevelType() == BevelBorder.RAISED) {
					lines.append("BorderFactory.createRaisedBevelBorder()");
				} else {
					lines.append("BorderFactory.createLoweredBevelBorder()");
				}
			} else if (b instanceof TitledBorder) {
				lines.append("BorderFactory.createTitledBorder(\""
						+ ((TitledBorder) b).getTitle() + "\")");
			} else if (b instanceof LineBorder) {
				imports.put(Color.class.getName(), "");
				LineBorder lb = (LineBorder) b;
				Color c = lb.getLineColor();
				String s = "new Color(" + c.getRed() + ", " + c.getGreen()
						+ ", " + c.getBlue() + "), " + lb.getThickness();
				lines.append("BorderFactory.createLineBorder(" + s + ")");

			} else if (b instanceof MatteBorder) {
				imports.put(Color.class.getName(), "");
				MatteBorder eb = (MatteBorder) b;
				Insets in = eb.getBorderInsets();
				Color c = eb.getMatteColor();
				String s1 = in.top + ", " + in.left + ", " + in.bottom + ", "
						+ in.right;
				String s2 = "new Color(" + c.getRed() + ", " + c.getGreen()
						+ ", " + c.getBlue() + ")";
				lines.append("BorderFactory.createMatteBorder(" + s1 + ", "
						+ s2 + ")");

			} else if (b instanceof EmptyBorder) {
				EmptyBorder eb = (EmptyBorder) b;
				Insets in = eb.getBorderInsets();
				String s = in.top + ", " + in.left + ", " + in.bottom + ", "
						+ in.right;
				lines.append("BorderFactory.createEmptyBorder(" + s + ")");
			}
		}
	}

	private void scanListeners(Container container, StringBuffer lines) {
		doScanListeners(container, lines);
		for (Component comp : container.getComponents()) {
			if (CellsUtil.isContainer(comp)) {
				scanListeners((Container) comp, lines);
			} else {
				doScanListeners(comp, lines);
			}
		}
	}

	private void doScanListeners(Component comp, StringBuffer lines) {

		Collection<Property> props = designer.getProperty()
				.getEventsProperties(comp);

		if (props != null) {
			for (Property prop : props) {
				if (prop.changed) {
					Method alm = prop.ed.getAddListenerMethod();
					Class[] tp = alm.getParameterTypes();

					imports.put(tp[0].getName(), "");

					String name = prop.comp.getName() + "_"
							+ tp[0].getSimpleName();
					StringBuffer newlines = new StringBuffer();
					incls.put(name, newlines);

					lines
							.append(space(10) + prop.comp.getName() + "."
									+ alm.getName() + "(new " + name + "());"
									+ newline);

					Method[] ms = prop.ed.getListenerMethods();
					for (Method m : ms) {
						Class[] p = m.getParameterTypes();
						imports.put(p[0].getName(), "");
						newlines.append(newline + space(10) + "public void "
								+ m.getName() + "(" + p[0].getSimpleName()
								+ " e) {" + newline);
						newlines.append(space(10) + "}" + newline);
					}
				}
			}
		}
	}

	private String space(int n) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < n; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private void print(String title, StringBuffer sb) {

		PrintStream ps = System.out;

		ps.println("============= Saving Form (" + title + ")===============");
		ps.println();

		ps.println("package org.nalizadeh.designer;");
		ps.println();

		Set<String> im = imports.keySet();
		for (Iterator<String> it = im.iterator(); it.hasNext();) {
			String s = it.next();
			ps.println("import " + s);
		}

		ps.println(sb);
		ps
				.println("===========================================================");
	}
}
