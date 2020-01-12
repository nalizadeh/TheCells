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
import org.nalizadeh.designer.DebugPanel;
import org.nalizadeh.designer.DesktopPanel;
import org.nalizadeh.designer.ResourceManager;

import org.nalizadeh.designer.base.Designer;
import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.boxlayout.BoxLayout2;
import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;
import org.nalizadeh.designer.layouts.freelayout.FreeLayout;

import org.nalizadeh.designer.property.Property;
import org.nalizadeh.designer.property.PropertySheet;
import org.nalizadeh.designer.property.jbeans.JBeansProperty;

import org.nalizadeh.designer.util.beans.JCalendar;
import org.nalizadeh.designer.util.beans.JTime;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.JFileChooser;

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
 * Organization:
 * </p>
 * 
 * @author Nader Alizadeh
 * @version 1.0
 */
public class CellsUtil {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String FILE_SEPARATOR = "/"; // File.separator;

	public static final String PROPERTYFILE = "TheCells.properties";
	public static final String BEANSFILE = "TheBeans.properties";

	public static final String EXM_PATH = "org.nalizadeh.designer.examples";
	public static final String TMP_PATH = "temp";
	public static final String IMG_PATH = "resources" + FILE_SEPARATOR + "images" + FILE_SEPARATOR;
	public static final String LOG_PATH = "log";

	public static String CELL_HOME = "";
	public static String JAVA_HOME = "";
	public static String EXAM_HOME = "";
	public static String TEMP_HOME = "";
	public static String LOG_FILE = "";

	public static String CLASSPATH = "";

	private static List<String> componentNames = new ArrayList<String>();
	private static List<String> containerNames = new ArrayList<String>();
	private static List<String> beanNames = new ArrayList<String>();

	private static Map<String, Class> dxComponents = new HashMap<String, Class>();

	static {

		readProperties();

		// for actions
		CellsImagePool.shared().get("Undo", "undo.gif");
		CellsImagePool.shared().get("Redo", "redo.gif");
		CellsImagePool.shared().get("Cut", "cut.gif");
		CellsImagePool.shared().get("Copy", "copy.gif");
		CellsImagePool.shared().get("Paste", "paste.gif");

		CellsImagePool.shared().get("New", "new.gif");
		CellsImagePool.shared().get("Open", "open.gif");
		CellsImagePool.shared().get("Save", "save.gif");
		CellsImagePool.shared().get("SaveAs", "saveas.gif");
		CellsImagePool.shared().get("SaveAll", "saveall.gif");
		CellsImagePool.shared().get("About The Cells", "info.gif");

		CellsImagePool.shared().get("Cursor", "cursor.gif");
		CellsImagePool.shared().get("Marqueeing", "marqueeing.gif");
		CellsImagePool.shared().get("Marqueeing2", "marqueeing2.gif");
		CellsImagePool.shared().get("Grid", "grid.gif");

		CellsImagePool.shared().get("RunTest", "run.gif");
		CellsImagePool.shared().get("StopTest", "stop.gif");
		CellsImagePool.shared().get("ClearConsole", "conclear.gif");

		CellsImagePool.shared().get("Copyright", "copyright.jpg");
		CellsImagePool.shared().get("CopyrightDlg", "copyrightDlg.jpg");
		CellsImagePool.shared().get("Inspector", "inspector.gif");
		CellsImagePool.shared().get("Structur", "structur.gif");
		CellsImagePool.shared().get("Designer", "designer.gif");
		CellsImagePool.shared().get("Source", "javasrc.gif");
		CellsImagePool.shared().get("Html", "htmlsrc.gif");
		CellsImagePool.shared().get("Aspx", "aspxsrc.gif");
		CellsImagePool.shared().get("HtmlPreview", "htmlpreview.gif");
		CellsImagePool.shared().get("Swings", "swings.gif");
		CellsImagePool.shared().get("Preview", "preview.gif");
		CellsImagePool.shared().get("Console", "console.gif");

		CellsImagePool.shared().get("Port", "port.gif");
		CellsImagePool.shared().get("Cell", "cell.gif");
		CellsImagePool.shared().get("LayoutProp", "layoutprop.gif");
		CellsImagePool.shared().get("CompProp", "compprop.gif");
		CellsImagePool.shared().get("RowProp", "rowprop.gif");
		CellsImagePool.shared().get("ColProp", "colprop.gif");
		CellsImagePool.shared().get("Properties", "properties.gif");
		CellsImagePool.shared().get("Property", "property.gif");
		CellsImagePool.shared().get("Event", "events.gif");
		CellsImagePool.shared().get("Plugin", "plugin.gif");
		CellsImagePool.shared().get("ViewAgent", "viewagent.gif");
		CellsImagePool.shared().get("DoLayout", "layout.gif");
		CellsImagePool.shared().get("RootPane", "rootpane.gif");
		CellsImagePool.shared().get("ContentPane", "contentpane.gif");
		CellsImagePool.shared().get("Container", "container.gif");
		CellsImagePool.shared().get("Bean", "bean.gif");
		CellsImagePool.shared().get("Light", "light.gif");
		CellsImagePool.shared().get("Question", "question.gif");

		// for row/column Popup
		CellsImagePool.shared().get("FL", "fl.gif");
		CellsImagePool.shared().get("FR", "fr.gif");
		CellsImagePool.shared().get("FU", "fu.gif");
		CellsImagePool.shared().get("FD", "fd.gif");

		CellsImagePool.shared().get("InsertColBefore", "insertcolb.gif");
		CellsImagePool.shared().get("InsertColAfter", "insertcola.gif");
		CellsImagePool.shared().get("InsertRowBefore", "insertrowb.gif");
		CellsImagePool.shared().get("InsertRowAfter", "insertrowa.gif");
		CellsImagePool.shared().get("DelColContents", "delcolcont.gif");
		CellsImagePool.shared().get("DelRowContents", "delrowcont.gif");
		CellsImagePool.shared().get("DelRowCol", "delrowcol.gif");

		// for Dialog New
		CellsImagePool.shared().get("DialogTitle", "dlgtit.gif");
		CellsImagePool.shared().get("NewDialog", "newDialog.gif");
		CellsImagePool.shared().get("NewFrame", "newFrame.gif");
		CellsImagePool.shared().get("NewPanel", "newPanel.gif");

		CellsImagePool.shared().get("New32", "new32.gif");
		CellsImagePool.shared().get("Font32", "font32.gif");
		CellsImagePool.shared().get("Border32", "border32.gif");

		CellsImagePool.shared().get("MemUpdate", "memUpdate.gif");
		CellsImagePool.shared().get("StartGC", "startGC.gif");

		CellsImagePool.shared().get("PopupClose", "popupclose.gif");

		CellsImagePool.shared().get("AligL", "aligl.gif");
		CellsImagePool.shared().get("AligR", "aligr.gif");
		CellsImagePool.shared().get("AligT", "aligt.gif");
		CellsImagePool.shared().get("AligB", "aligb.gif");

		CellsImagePool.shared().get("Status0", "status0.gif");
		CellsImagePool.shared().get("Status1", "status1.gif");
		CellsImagePool.shared().get("Status2", "status2.gif");
		CellsImagePool.shared().get("Status3", "status3.gif");
		CellsImagePool.shared().get("Status4", "status4.gif");
		CellsImagePool.shared().get("Status5", "status5.gif");

		CellsImagePool.shared().get("Indicator1", "indicator1.gif");
		CellsImagePool.shared().get("Indicator2", "indicator2.gif");

		// ===== Swings

		addComponent("JLabel", JLabel.class, "label.gif", 1);
		addComponent("JButton", JButton.class, "button.gif", 1);
		addComponent("JToggleButton", JToggleButton.class, "togglebutton.gif",
				1);
		addComponent("JTextField", JTextField.class, "textfield.gif", 1);
		addComponent("JPasswordField", JPasswordField.class,
				"passwordfield.gif", 1);
		addComponent("JRadioButton", JRadioButton.class, "radiobutton.gif", 1);
		addComponent("JCheckBox", JCheckBox.class, "checkbox.gif", 1);
		addComponent("JComboBox", JComboBox.class, "combobox.gif", 1);
		addComponent("JSpinner", JSpinner.class, "spinner.gif", 1);
		addComponent("JTextArea", JTextArea.class, "textarea.gif", 1);
		addComponent("JList", JList.class, "list.gif", 1);
		addComponent("JTable", JTable.class, "table.gif", 1);
		addComponent("JTree", JTree.class, "tree.gif", 1);
		addComponent("JSlider", JSlider.class, "slider.gif", 1);
		addComponent("JProgressBar", JProgressBar.class, "progressbar.gif", 1);
		addComponent("JSeparator", JSeparator.class, "separator.gif", 1);
		addComponent("JTabbedPane", JTabbedPane.class, "tabbedpane.gif", 2);
		addComponent("JScrollPane", JScrollPane.class, "scrollpane.gif", 2);
		addComponent("JSplitPane", JSplitPane.class, "splitpane.gif", 2);
		addComponent("JPanel", JPanel.class, "panel.gif", 2);

		addComponent("JCalendar", JCalendar.class, "calendar.gif", 3);
		addComponent("JTime", JTime.class, "time.gif", 3);

		// ===== Beans or User components

		readBeans();
	}

	private static void addComponent(String name, Class glass, String image,
			int type) {

		dxComponents.put(name, glass);
		CellsImagePool.shared().get(name, image);

		switch (type) {
		case 1:
			componentNames.add(name);
			break;
		case 2:
			containerNames.add(name);
			break;
		case 3:
			beanNames.add(name);
			break;
		default:
			break;
		}
	}

	private static void readProperties() {

		String chome = null;
		String jhome = null;
		String cpath = null;

		// ===== read properties

		InputStream in;

		try {
			in = new FileInputStream(new File(PROPERTYFILE));
			Properties p = new Properties();
			p.load(in);
			chome = p.getProperty("THE_CELLS_HOME");
			jhome = p.getProperty("JAVA_HOME");
			cpath = p.getProperty("CLASSPATH");
			
		} catch (Exception ex1) {
			try {
				in = ResourceManager.class.getResourceAsStream(PROPERTYFILE);
				
				if (in != null) {
					Properties p = new Properties();
					p.load(in);
					chome = p.getProperty("THE_CELLS_HOME");
					jhome = p.getProperty("JAVA_HOME");
					cpath = p.getProperty("CLASSPATH");

				} else {
					System.out.println("TheCells.properties does not exists, trying without it!");
				}

			} catch (Exception ex2) {
				System.out.println("TheCells.properties does not exists, trying without it!");
			}
		}

		if (chome == null || chome.length() == 0) {
			URL url = ResourceManager.class.getResource("/");
			chome = url == null ? "./" : url.toString();
		}
		if (jhome == null) {
			jhome = "";
		}
		if (cpath == null) {
			cpath = "";
		}

		CELL_HOME = chome;
		JAVA_HOME = jhome;
		CLASSPATH = cpath;
		EXAM_HOME = CELL_HOME + FILE_SEPARATOR + EXM_PATH.replace(".", FILE_SEPARATOR);
		TEMP_HOME = CELL_HOME + FILE_SEPARATOR + TMP_PATH.replace(".", FILE_SEPARATOR);
		LOG_FILE = CELL_HOME + FILE_SEPARATOR + LOG_PATH + FILE_SEPARATOR + "log.txt";
	}

	private static void readBeans() {

		InputStream in;

		try {
			in = new FileInputStream(new File(BEANSFILE));
		} catch (IOException ex1) {
			System.out.println("Could not find the beans file, trying again via ResourceManager!");

			try {
				in = ResourceManager.class.getResourceAsStream(BEANSFILE);
				if (in == null) {
					System.out.println("Could not find the beans file!");
					return;
				}
				BufferedReader bin = new BufferedReader(new InputStreamReader(in));

				while (true) {
					String line = bin.readLine();
					if (line == null) {
						break;
					}
					line = line.trim();
					if ("".equals(line) || line.startsWith("#")) {
						continue;
					}

					String[] ls = line.split(",");
					if (ls.length == 4) {

						String name = ls[0].trim();
						String cls = ls[1].trim();
						String img = ls[2].trim();
						String type = ls[3].trim().toUpperCase();

						try {
							Class glass = Class.forName(cls);
							File f = new File(img);

							if (!f.exists()) {
								CellsImagePool.shared().get(name, img);
							} else {
								CellsImagePool.shared().put(name,
										new ImageIcon(img));
							}

							dxComponents.put(name, glass);

							componentNames.remove(name);
							containerNames.remove(name);
							beanNames.remove(name);

							if (type.equals("1")) {
								componentNames.add(name);
							} else if (type.equals("2")) {
								containerNames.add(name);
							} else if (type.equals("3")) {
								beanNames.add(name);
							}
						} catch (ClassNotFoundException ex) {
							System.out.println(ex.getMessage());
						}
					}
				}

			} catch (IOException ex2) {
				System.out.println(ex2.getMessage());
			}
		}
	}

	public static String formatFilename(String name) {
		return name.replace("\\", FILE_SEPARATOR);
	}

	public static ImageIcon getImage(String name) {
		return CellsImagePool.getImage(name);
	}

	public static String[] getComponentNames() {
		return componentNames.toArray(new String[componentNames.size()]);
	}

	public static String[] getContainerNames() {
		return containerNames.toArray(new String[containerNames.size()]);
	}

	public static String[] getBeanNames() {
		return beanNames.toArray(new String[beanNames.size()]);
	}

	public static Component createComponent(String name) {

		try {
			Component comp = null;

			if (name.equals("Cursor")) {
				return null;
			} else if (name.equals("JLabel")) {
				JLabel lb = (JLabel) dxComponents.get("JLabel").newInstance();
				lb.setText("JLabel:");
				comp = lb;
			} else if (name.equals("JButton")) {
				JButton bt = (JButton) dxComponents.get("JButton").newInstance();
				bt.setText("JButton:");
				comp = bt;
			} else if (name.equals("JToggleButton")) {
				JToggleButton tb = (JToggleButton) dxComponents.get("JToggleButton").newInstance();
				tb.setText("JToggleButton:");
				comp = tb;
			} else if (name.equals("JTextField")) {
				JTextField tf = (JTextField) dxComponents.get("JTextField").newInstance();
				tf.setText("JTextField:");
				comp = tf;
			} else if (name.equals("JPasswordField")) {
				JPasswordField pf = (JPasswordField) dxComponents.get(
						"JPasswordField").newInstance();
				pf.setText("JPasswordField:");
				comp = pf;
			} else if (name.equals("JCheckBox")) {
				JCheckBox cb = (JCheckBox) dxComponents.get("JCheckBox")
						.newInstance();
				cb.setText("JCheckBox:");
				comp = cb;
			} else if (name.equals("JRadioButton")) {
				JRadioButton rb = (JRadioButton) dxComponents.get(
						"JRadioButton").newInstance();
				rb.setText("JRadioButton:");
				comp = rb;
			} else if (name.equals("JComboBox")) {
				comp = (JComboBox) dxComponents.get("JComboBox").newInstance();
			} else if (name.equals("JSpinner")) {
				comp = (JSpinner) dxComponents.get("JSpinner").newInstance();
			} else if (name.equals("JTextArea")) {
				comp = (JTextArea) dxComponents.get("JTextArea").newInstance();
			} else if (name.equals("JSeparator")) {
				comp = (JSeparator) dxComponents.get("JSeparator")
						.newInstance();
			} else if (name.equals("JList")) {
				final String[] st = new String[] { "Item 1", "Item 2",
						"Item 3", "Item 4" };

				JList ls = (JList) dxComponents.get("JList").newInstance();
				ls.setModel(new AbstractListModel() {
					public int getSize() {
						return st.length;
					}

					public Object getElementAt(int i) {
						return st[i];
					}
				});
				comp = ls;

			} else if (name.equals("JTable")) {
				JTable table = (JTable) dxComponents.get("JTable")
						.newInstance();
				table.setModel(new DefaultTableModel(
						new Object[][] { { null, null, null, null },
								{ null, null, null, null },
								{ null, null, null, null },
								{ null, null, null, null } }, new String[] {
								"Title 1", "Title 2", "Title 3", "Title 4" }));
				comp = table;

			} else if (name.equals("JTree")) {
				comp = (JTree) dxComponents.get("JTree").newInstance();

			} else if (name.equals("JSlider")) {
				comp = (JSlider) dxComponents.get("JSlider").newInstance();
			} else if (name.equals("JProgressBar")) {
				comp = (JProgressBar) dxComponents.get("JProgressBar")
						.newInstance();
			} else if (name.equals("JScrollPane")) {
				comp = (JScrollPane) dxComponents.get("JScrollPane")
						.newInstance();
				comp.setPreferredSize(new Dimension(50, 50));

			} else if (name.equals("JPanel")) {
				JPanel pa = (JPanel) dxComponents.get("JPanel").newInstance();
				pa.setLayout(null);
				pa.setPreferredSize(new Dimension(80, 50));
				comp = pa;

			} else if (name.equals("JTabbedPane")) {
				JPanel p1 = new JPanel(null);
				JPanel p2 = new JPanel(null);

				JTabbedPane tp = (JTabbedPane) dxComponents.get("JTabbedPane")
						.newInstance();
				tp.add("tab1", p1);
				tp.add("tab2", p2);
				comp = tp;
				comp.setPreferredSize(new Dimension(150, 80));

			} else if (name.equals("JSplitPane")) {
				JPanel p1 = new JPanel(null);
				JPanel p2 = new JPanel(null);

				JSplitPane sp = (JSplitPane) dxComponents.get("JSplitPane")
						.newInstance();
				sp.setLeftComponent(p1);
				sp.setRightComponent(p2);
				p2.setPreferredSize(new Dimension(40, 40));
				sp.setResizeWeight(1);
				comp = sp;

			} else if (name.equals("JCalendar")) {
				comp = (JCalendar) dxComponents.get("JCalendar").newInstance();

			} else if (name.equals("JTime")) {
				comp = (JTime) dxComponents.get("JTime").newInstance();

			} else {
				Object o = dxComponents.get(name).newInstance();
				if (!(o instanceof Component)) {
					o = createComponent("JPanel");
				}
				comp = (Component) o;
			}

			Dimension d = comp.getPreferredSize();
			int w = Math.max(20, d.width);
			int h = Math.max(20, d.height);
			comp.setBounds(0, 0, w, h);
			comp.setPreferredSize(new Dimension(w, h));
			comp.setMinimumSize(new Dimension(w, h));

			return comp;

		} catch (InstantiationException ex) {
			DebugPanel.printStackTrace(ex);
		} catch (IllegalAccessException ex) {
			DebugPanel.printStackTrace(ex);
		}
		return null;
	}

	public static Component designerOrNotDesigner(DesignerContainer dc,
			Component comp) {

		DesignerFrame df = dc.getFrame();
		DesktopPanel dp = df.getDesigner().getDesktop();
		PropertySheet ps = df.getDesigner().getProperty();

		Component newComp = comp;

		if (isPanel(comp) && !(comp instanceof DesignerContainer)) {
			newComp = dp.newDesignerContainer(df, (Container) comp, false);
			ps.loadComponent(newComp, comp);

		} else if (comp instanceof JTabbedPane) {
			ps.loadComponent(comp, null);

			JTabbedPane tp = (JTabbedPane) comp;
			Component[] comps = tp.getComponents();

			List<String> kes = new ArrayList<String>();
			List<Component> cos = new ArrayList<Component>();
			for (int i = 0; i < comps.length; i++) {
				String t = tp.getTitleAt(i);
				if (t != null) {
					kes.add(t);
					cos.add(comps[i]);
				}
			}

			tp.removeAll();
			int i = 0;
			for (Component co : cos) {
				String key = kes.get(i++);
				if (isPanel(co)) {
					Component nc = dp.newDesignerContainer(df, (Container) co,
							false);
					tp.add(key, nc);
					ps.loadComponent(nc, co);
				} else {
					tp.add(key, co);
				}
			}

		} else if (comp instanceof JSplitPane) {
			ps.loadComponent(comp, null);
			JSplitPane st = (JSplitPane) comp;
			if (st.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
				Component co = st.getLeftComponent();
				if (isPanel(co)) {
					Component nc = dp.newDesignerContainer(df, (Container) co,
							false);
					st.setLeftComponent(nc);
					ps.loadComponent(nc, co);
				}
				co = st.getRightComponent();
				if (isPanel(co)) {
					Component nc = dp.newDesignerContainer(df, (Container) co,
							false);
					st.setRightComponent(nc);
					ps.loadComponent(nc, co);
				}
			} else {
				Component co = st.getTopComponent();
				if (isPanel(co)) {
					Component nc = dp.newDesignerContainer(df, (Container) co,
							false);
					st.setTopComponent(nc);
					ps.loadComponent(nc, co);
				}
				co = st.getBottomComponent();
				if (isPanel(co)) {
					Component nc = dp.newDesignerContainer(df, (Container) co,
							false);
					st.setBottomComponent(nc);
					ps.loadComponent(nc, co);
				}
			}

		} else if (comp instanceof JScrollPane) {

			ps.loadComponent(comp, null);

			JScrollPane sp = (JScrollPane) comp;
			if (sp.getViewport().getComponentCount() > 0
					&& sp.getViewport().getComponent(0) != null) {
				ps.loadComponent(sp.getViewport().getComponent(0), null);
			}

		} else {
			ps.loadComponent(comp, null);
		}

		return newComp;
	}

	public static void saveToFile(String name, String txt) throws IOException {
		File f = new File(name);
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdir();
			}
			f.createNewFile();
		}
		FileWriter fw = new FileWriter(f);
		fw.write(txt);
		fw.close();
	}

	public static void readFromFile(String name, List<String> buf)
			throws IOException {
		FileWriter fw = new FileWriter(name);
		for (String s : buf) {
			fw.write(s);
		}
		fw.close();
	}

	public static String getTextOf(Component comp) {
		if (comp instanceof JTabbedPane) {
			JTabbedPane tp = (JTabbedPane) comp;
			return tp.getTitleAt(tp.getSelectedIndex());
		} else {
			try {
				return (String) CellsUtil.invokeMethod("getText", new Class[0],
						comp);
			} catch (Exception e) {
				return null;
			}
		}
	}

	public static void setTextOf(Component comp, String txt, DesignerFrame df) {

		if (comp instanceof JTabbedPane) {
			df.getDesigner().getProperty().changeProperty("titleAt", null, txt);
		} else {
			df.getDesigner().getProperty().changeProperty("text", null, txt);
		}
	}

	// a little bit reflection

	public static String getInstanceName(Object source, Object variable) {

		Field[] fs = source.getClass().getDeclaredFields();
		try {
			for (Field f : fs) {
				if (f.getModifiers() != Modifier.PRIVATE) {
					Object o = f.get(source);
					if (variable.equals(o)) {
						return f.getName();
					}
				}
			}
		} catch (IllegalAccessException ex) {
			DebugPanel.appendWarning(ex.getMessage());
		} catch (SecurityException ex) {
			DebugPanel.appendWarning(ex.getMessage());
		}
		return null;
	}

	public static Object invokeMethod(String method, Class[] parameterTypes,
			Object comp, Object... arg) throws Exception {
		Method m = comp.getClass().getMethod(method, parameterTypes);
		return m.invoke(comp, arg);
	}

	public static Object invokeMethod(Method method, Object comp, Object... arg)
			throws Exception {
		return method.invoke(comp, arg);
	}

	public static Component clone(Component comp, boolean load,
			CellsDesigner designer) {

		try {
			Component newObject = null;

			if (isPanel(comp)) {
				newObject = new JPanel();
				copyContainer((Container) comp, (Container) newObject, load,
						designer);

			} else {
				newObject = comp.getClass().newInstance();

				if (comp instanceof JScrollPane) {
					JScrollPane sp = (JScrollPane) comp;
					if (sp.getViewport().getComponentCount() > 0) {
						Component oldComp = sp.getViewport().getComponent(0);
						Component newComp = (Component) clone(oldComp, load,
								designer);
						if (oldComp instanceof JTable) {
							((JTable) newComp).setModel(((JTable) oldComp)
									.getModel());
						}
						((JScrollPane) newObject).getViewport().add(newComp);
					}
				} else if (comp instanceof JTabbedPane) {
					JTabbedPane tp = (JTabbedPane) comp;
					JTabbedPane np = (JTabbedPane) newObject;
					np.getModel().setSelectedIndex(-1);

					Component[] comps = tp.getComponents();
					List<String> kes = new ArrayList<String>();
					List<Component> cos = new ArrayList<Component>();
					for (int i = 0; i < comps.length; i++) {
						String t = tp.getTitleAt(i);
						if (t != null) {
							kes.add(t);
							cos.add(comps[i]);
						}
					}
					int i = 0;
					for (Component co : cos) {
						Component nc = (Component) clone(co, load, designer);
						np.add(kes.get(i++), nc);
					}
				} else if (comp instanceof JSplitPane) {
					JSplitPane st = (JSplitPane) comp;
					JSplitPane ns = (JSplitPane) newObject;
					if (st.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
						ns.setLeftComponent((Component) clone(
								st.getLeftComponent(), load, designer));
						ns.setRightComponent((Component) clone(
								st.getRightComponent(), load, designer));
					} else {
						ns.setTopComponent((Component) clone(
								st.getTopComponent(), load, designer));
						ns.setBottomComponent((Component) clone(
								st.getBottomComponent(), load, designer));
					}
				}
			}

			if (load) {
				designer.getProperty().loadComponent(newObject, comp);
			}

			designer.getProperty().copyProperties(comp, newObject);

			newObject.setBounds(comp.getBounds());
			newObject.setName(comp.getName());

			return newObject;

		} catch (Exception ex) {
			DebugPanel.printStackTrace(ex);
		}
		return null;
	}

	private static void copyContainer(Container src, Container dst,
			boolean load, CellsDesigner designer) {

		LayoutManager lm = src.getLayout();

		if (lm instanceof FreeLayout || lm == null) {
			dst.setLayout(new FreeLayout());
			for (Component comp : src.getComponents()) {
				Component co = (Component) clone(comp, load, designer);
				co.setBounds(comp.getBounds());
				dst.add(co);
			}

		} else if (lm instanceof BorderLayout) {
			BorderLayout la = (BorderLayout) lm;
			dst.setLayout(new BorderLayout(la.getHgap(), la.getVgap()));

			for (Component comp : src.getComponents()) {
				Component co = (Component) clone(comp, load, designer);
				Object cons = la.getConstraints(comp);
				dst.add(co, cons);
			}

		} else if (lm instanceof CardLayout) {
			CardLayout la = (CardLayout) lm;
			dst.setLayout(new CardLayout(la.getHgap(), la.getVgap()));

			for (Component comp : src.getComponents()) {
				Component co = (Component) clone(comp, load, designer);
				dst.add(co, "" + src.getComponentZOrder(co));
			}

		} else if (lm instanceof FlowLayout) {
			FlowLayout la = (FlowLayout) lm;
			dst.setLayout(new FlowLayout(la.getAlignment(), la.getHgap(), la
					.getVgap()));
			for (Component comp : src.getComponents()) {
				Component co = (Component) clone(comp, load, designer);
				dst.add(co, null);
			}

		} else if (lm instanceof BoxLayout2) {
			BoxLayout2 la = (BoxLayout2) lm;
			dst.setLayout(new BoxLayout2(dst, la.getAxis2()));
			for (Component comp : src.getComponents()) {
				Component co = (Component) clone(comp, load, designer);
				dst.add(co, null);
			}

		} else if (lm instanceof GridLayout) {
			GridLayout la = (GridLayout) lm;
			dst.setLayout(new GridLayout(la.getRows(), la.getColumns(), la
					.getHgap(), la.getVgap()));
			for (Component comp : src.getComponents()) {
				Component co = (Component) clone(comp, load, designer);
				dst.add(co, null);
			}

		} else if (lm instanceof GridBagLayout) {
			GridBagLayout la = (GridBagLayout) lm;
			dst.setLayout(new GridBagLayout());
			for (Component comp : src.getComponents()) {
				Component co = (Component) clone(comp, load, designer);
				GridBagConstraints cons = la.getConstraints(comp);
				dst.add(co, cons);
			}

		} else if (lm instanceof CellsLayout) {
			CellsLayout la = (CellsLayout) lm;
			dst.setLayout(new CellsLayout(la.getHgap(), la.getVgap()));

			CellsLayout l1 = (CellsLayout) src.getLayout();
			CellsLayout l2 = (CellsLayout) dst.getLayout();

			List<CellsLayout.CellRow> rows = l1.getRows();
			List<CellsLayout.CellColumn> cols = l1.getColumns();
			List<CellsLayout.Cell> cells = l1.getCells();

			l2.setHgap(l1.getHgap());
			l2.setVgap(l1.getVgap());

			for (CellsLayout.CellRow row : rows) {
				l2.addRow(row.origHeight);
			}
			for (CellsLayout.CellColumn col : cols) {
				l2.addColumn(col.origWidth);
			}
			l2.createCells();

			for (CellsLayout.Cell cell : cells) {
				if (cell.comp != null) {
					Component co = (Component) clone(cell.comp, load, designer);
					dst.add(co, cell.clone());
				}
			}
		} else {
			DebugPanel.appendWarning("unknown layout type");
			return;
		}
	}

	public static Container scanContainer(File file) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(
				file.getAbsolutePath()));
		String classname = "";

		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			if (line.startsWith("package")) {
				String s = file.getName();
				int i = s.indexOf(".");
				if (i != -1) {
					s = s.substring(0, i);
				}

				classname = line.substring(7, line.length() - 1).trim() + "."
						+ s;
				break;
			}
		}
		br.close();

		try {
			Class c = Class.forName(classname);
			Object o = c.newInstance();

			if (o instanceof Container) {
				if (o instanceof JDialog) {
					o = ((JDialog) o).getContentPane();
				} else if (o instanceof JFrame) {
					o = ((JFrame) o).getContentPane();
				}
				return (Container) o;
			} else {
				DebugPanel.appendWarning("No Container was found");
			}

		} catch (ClassNotFoundException ex) {
			DebugPanel.printStackTrace(ex);
		} catch (InstantiationException ex) {
			DebugPanel.printStackTrace(ex);
		} catch (IllegalAccessException ex) {
			DebugPanel.printStackTrace(ex);
		}
		return null;
	}

	public static void scanSource(File file, DesignerFrame df, PropertySheet ps)
			throws IOException {

		df.setPackage(scanPackage(file));

		BufferedReader br = new BufferedReader(new FileReader(
				file.getAbsolutePath()));

		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}

			String s = line.trim();
			if (s.length() == 0 || s.startsWith("//") || s.startsWith("/*")
					|| s.startsWith("*/") || s.startsWith("*")
					|| s.startsWith("package") || s.startsWith("import")) {
				continue;
			}
			if (s.startsWith("public") || s.startsWith("protected")
					|| s.startsWith("private")) {
				scanModifier(s, df.getContainer(), ps);
				continue;
			}

			scanContainerSetter(s, df.getContainer(), ps);
			scanComponentSetter(s, df.getContainer(), ps);
			scanEventSetter(s, df.getContainer(), ps);
		}
		br.close();
	}

	private static boolean scanModifier(String line, Container cont,
			PropertySheet ps) {
		for (Component co : cont.getComponents()) {
			if (doScanModifier(line, co, ps)) {
				return true;
			}

			if (CellsUtil.isContainer(co)) {
				if (co instanceof JScrollPane) {
					co = ((JScrollPane) co).getViewport().getComponent(0);
					if (doScanModifier(line, co, ps)) {
						return true;
					}
				} else if (scanModifier(line, (Container) co, ps)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean doScanModifier(String line, Component co,
			PropertySheet ps) {

		String nm = co.getName();
		String c1 = co.getClass().getSimpleName();
		String c2 = co.getClass().getName();

		if (nm != null && line.indexOf(" " + nm) != -1) {
			String pr1 = "private " + c1 + " " + nm;
			String pr2 = "private " + c2 + " " + nm;
			String po1 = "protected " + c1 + " " + nm;
			String po2 = "protected " + c2 + " " + nm;
			String pu1 = "public " + c1 + " " + nm;
			String pu2 = "public " + c2 + " " + nm;

			if (line.startsWith(pr1) || line.startsWith(pr2)) {
				ps.setVisibility(co, PropertySheet.MODIFIER_PRIVATE);
				return true;
			} else if (line.startsWith(po1) || line.startsWith(po2)) {
				ps.setVisibility(co, PropertySheet.MODIFIER_PROTECTED);
				return true;
			} else if (line.startsWith(pu1) || line.startsWith(pu2)) {
				ps.setVisibility(co, PropertySheet.MODIFIER_PUBLIC);
				return true;
			}
		}
		return false;
	}

	private static void scanContainerSetter(String line, Container cont,
			PropertySheet ps) {
		Collection<JBeansProperty> props = ps.getContainerProperties(cont);
		if (props != null) {
			for (JBeansProperty p : props) {
				String n = p.getName();
				String s1 = "set" + n.substring(0, 1).toUpperCase()
						+ n.substring(1);
				String s2 = "this." + s1;
				if (line.trim().indexOf(s1) == 0
						|| line.trim().indexOf(s2) == 0) {
					p.forceChanged();
					p.writeToObject(cont);
				}
			}
		}
	}

	private static boolean scanComponentSetter(String line, Container cont,
			PropertySheet ps) {

		for (Component co : cont.getComponents()) {

			Collection<Property> props = ps.getComponentProperties(co);

			if (props != null) {
				for (Property prop : props) {
					String pn = prop.name;
					if (pn.equals("layout") || pn.equals("bounds")) {
						continue;
					}

					String cn = co.getName() == null ? "" : co.getName() + ".";
					String s = cn + "set" + pn.substring(0, 1).toUpperCase()
							+ pn.substring(1);

					int i = line.indexOf(s);
					if (i != -1 && (i == 0 || line.charAt(i - 1) == ' ')) {

						prop.changed = true;
						prop.achanged = true;

						if (prop.isReplaced) {

							prop.isReplaced = false;

							try {
								invokeMethod(
										prop.pd.getWriteMethod().getName(),
										new Class[] { prop.type }, prop.comp,
										prop.value);
							} catch (Exception ex) {
								DebugPanel.printStackTrace(ex);
							}

							if (prop.comp instanceof JPanel) {
								Map<String, JBeansProperty> bprops = ps
										.getContainerPropertiesMap(prop.comp);
								JBeansProperty bprop = bprops.get(prop.name);
								if (bprop != null) {
									bprop.forceChanged();
								}
							}
						}
						return true;
					}
				}
			}
			if (isContainer(co)) {
				if (scanComponentSetter(line, (Container) co, ps)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean scanEventSetter(String line, Container cont,
			PropertySheet ps) {

		for (Component co : cont.getComponents()) {

			Collection<Property> events = ps.getEventsProperties(co);

			if (events != null) {
				for (Property prop : events) {
					String pn = prop.name;
					String cn = co.getName() == null ? "" : co.getName() + ".";
					String s = cn + "add" + pn.substring(0, 1).toUpperCase()
							+ pn.substring(1);

					int i = line.indexOf(s);
					if (i != -1 && (i == 0 || line.charAt(i - 1) == ' ')) {
						prop.changed = true;
						return true;
					}
				}
			}
			if (isContainer(co)) {
				if (scanEventSetter(line, (Container) co, ps)) {
					return true;
				}
			}
		}
		return false;
	}

	public static String scanPackage(File file) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(
				file.getAbsolutePath()));

		while (true) {
			String line = br.readLine();
			if (line == null) {
				break;
			}
			if (line.startsWith("package")) {
				br.close();
				return line.substring(7, line.length() - 1).trim();
			}
		}
		br.close();
		return null;
	}

	public static boolean isPanel(Component comp) {
		return (comp instanceof JPanel && !(comp instanceof JCalendar) && !(comp instanceof JTime));
	}

	public static boolean isContainer(Component comp) {
		return (isPanel(comp) || (comp instanceof JScrollPane)
				|| (comp instanceof JTabbedPane) || (comp instanceof JSplitPane));
	}

	public static boolean isScrollable(Component comp) {
		return comp instanceof JTextArea || comp instanceof JList
				|| comp instanceof JTable || comp instanceof JTree;
	}

	public static boolean isGuiComponent(Component comp) {
		return (comp instanceof Designer)
				|| dxComponents.containsValue(comp.getClass());
	}

	public static ImageIcon getComponentIcon(Object comp) {
		for (String key : dxComponents.keySet()) {
			if (comp.getClass().equals(dxComponents.get(key))) {
				return CellsImagePool.getImage(key);
			}
		}
		return null;
	}

	public static void setLookAndFeel(String laf, Component comp) {

		if (laf.equals(CellsDesigner.lf_synth)) {
			JFileChooser fc = new JFileChooser(CELL_HOME);
			if (fc.showOpenDialog(comp) == JFileChooser.APPROVE_OPTION) {
				try {
					SynthLookAndFeel synth = new SynthLookAndFeel();
					synth.load(new FileInputStream(fc.getSelectedFile()
							.getAbsoluteFile()), CellsDesigner.class);
					UIManager.setLookAndFeel(synth);
					SwingUtilities.updateComponentTreeUI(comp);
				} catch (Exception ex) {
					DebugPanel.appendError(ex.getMessage());
				}
			}
			return;
		}
		try {
			UIManager.setLookAndFeel(laf);
			SwingUtilities.updateComponentTreeUI(comp);
		} catch (Exception ex) {
			DebugPanel.printStackTrace(ex);
			DebugPanel.appendError("Could not find the look & feel: " + laf);
		}
	}
}
