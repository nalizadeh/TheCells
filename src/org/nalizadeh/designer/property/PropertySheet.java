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

package org.nalizadeh.designer.property;

import org.nalizadeh.designer.CellsDesigner;
import org.nalizadeh.designer.DebugPanel;

import org.nalizadeh.designer.base.DesignerConstraints;
import org.nalizadeh.designer.base.DesignerContainer;
import org.nalizadeh.designer.base.DesignerFrame;

import org.nalizadeh.designer.layouts.cellslayout.CellsLayout;

import org.nalizadeh.designer.property.jbeans.JBeansProperty;
import org.nalizadeh.designer.property.jbeans.JBeansTable;
import org.nalizadeh.designer.property.jbeans.JBeansTableModel;

import org.nalizadeh.designer.util.CellsLayoutChooser;
import org.nalizadeh.designer.util.CellsUtil;
import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Iterator;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2008 N.A.J</p>
 *
 * <p>Organization:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class PropertySheet extends JPanel {

	public static int MODIFIER_PRIVATE = 0;
	public static int MODIFIER_PROTECTED = 1;
	public static int MODIFIER_PACKAGE = 2;
	public static int MODIFIER_PUBLIC = 3;

	private CellsDesigner designer;
	private DesignerConstraints cons = null;

	private JTabbedPane pane;

	// use JBeans PropertySheet only for container
	private JBeansTable btable;
	private CellsLayoutChooser.LayoutPanel layoutPanel;
	private JScrollPane btableScroll;
	private JTextArea contPropDescTx;
	private JPanel contSheet;

	// use Cells PropertySheet for all other components and events
	private PropertySheetTable propsTable;
	private EventSheetTable eventsTable;
	private JTextField tfType;
	private JTextField tfName;
	private JComboBox coVisibility;
	private boolean changingVisibility = false;

	private JTextArea compPropDescTx;
	private JPanel compSheet;

	private Map<Component, Map> propsMap = new HashMap<Component, Map>();
	private Map<Component, Map> eventsMap = new HashMap<Component, Map>();
	private Map<String, Property> props = null;
	private Map<String, Property> events = null;

	private Map<Component, Integer> modifiers = new HashMap<Component, Integer>();

	private static final String[] MODIFIERS = { "private", "protected",
			"package protected", "public" };

	private static final String[] ROOT_CONTAINER_PROPERTIES = { "background",
			"border", "enabled", "foreground", "maximumSize", "minimumSize",
			"preferredSize", "opaque", "toolTipText", "visible" };

	private static final String[] IGNORED_COPY_PROPERTIES = { "leftComponent",
			"rightComponent", "topComponent", "bottomComponent" };
	
	private boolean updateAdjusting = false;

	public PropertySheet(CellsDesigner dsg) {

		this.designer = dsg;

		propsTable = new PropertySheetTable(this);
		eventsTable = new EventSheetTable(this);

		propsTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						updatePropertyDescription(null);
					}
				});

		btable = new JBeansTable(designer, JBeansTableModel.VIEW_CATEGORIES);
		btable.getBeansModel().setCategoryIcon("Layout",
				CellsUtil.getImage("CellProp"));
		btable.getBeansModel().setCategoryIcon("Properties",
				CellsUtil.getImage("Properties"));
		btable.getBeansModel().setSortingProperties(true);
		btable.setUseExtraIndent(true);
		btable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						updateJBeansPropertyDescription();
					}
				});

		btableScroll = new JScrollPane(btable);
		contPropDescTx = new JTextArea();
		contPropDescTx.setLineWrap(true);
		contPropDescTx.setWrapStyleWord(true);

		pane = new JTabbedPane();
		compPropDescTx = new JTextArea();
		compPropDescTx.setLineWrap(true);
		compPropDescTx.setWrapStyleWord(true);

		final JTabbedPane ts = new JTabbedPane();
		ts.add("Properties", new JScrollPane(propsTable));
		ts.add("Events", new JScrollPane(eventsTable));
		ts.add("Beans", new JPanel());
		ts.add("Plugins", new JPanel());
		ts.setIconAt(0, CellsUtil.getImage("Property"));
		ts.setIconAt(1, CellsUtil.getImage("Event"));
		ts.setIconAt(2, CellsUtil.getImage("ViewAgent"));
		ts.setIconAt(3, CellsUtil.getImage("Light"));

		ts.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (ts.getSelectedIndex() == 0) {
					updatePropertyDescription(null);
				} else {
					updatePropertyDescription("");
				}
			}
		});

		layoutPanel = new CellsLayoutChooser.LayoutPanel(designer);

		JLabel desc1Label = new JLabel("Description");
		desc1Label.setFont(new Font("Dialog", 1, 12));
		contPropDescTx.setFont(new Font("Dialog", 0, 11));

		contSheet = new JPanel(new CellsLayout(new double[][] {
				{ CellsLayout.PREFERRED, 15, CellsLayout.FILL, 10, 18, 80 },
				{ CellsLayout.FILL } }));

		contSheet.add(layoutPanel, "0,0");
		contSheet.add(btableScroll, "2,0");
		contSheet.add(desc1Label, "4,0");
		contSheet.add(contPropDescTx, "5,0");

		JLabel desc2Label = new JLabel("Description");
		desc2Label.setFont(new Font("Dialog", 1, 12));
		compPropDescTx.setFont(new Font("Dialog", 0, 11));

		tfType = new JTextField();
		tfName = new JTextField();
		coVisibility = new JComboBox(MODIFIERS);

		tfType.setEnabled(false);
		tfName.setEnabled(true);

		tfName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				changeProperty("name", cons.getComponent().getName(), tfName
						.getText());
			}
		});

		tfName.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {

				// this if sine actionListener
				if (cons != null && !cons.getComponent().getName().equals(tfName.getText())) {
					changeProperty("name", cons.getComponent().getName(),
							tfName.getText());
				}
			}
		});

		coVisibility.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (!changingVisibility) {
					changeProperty("modifier", modifiers.get(cons
							.getComponent()), coVisibility.getSelectedIndex());
				}
			}
		});

		JPanel p = new JPanel(new CellsLayout(new double[][] {
				{ 10, 22, 22, 22, 5 },
				{ CellsLayout.PREFERRED, CellsLayout.FILL } }, 4, 4));

		p.add(new JLabel("type:"), "1,0");
		p.add(tfType, "1,1");
		p.add(new JLabel("name:"), "2,0");
		p.add(tfName, "2,1");
		p.add(new JLabel("modifier:"), "3,0");
		p.add(coVisibility, "3,1");

		compSheet = new JPanel(new CellsLayout(new double[][] {
				{ CellsLayout.PREFERRED, CellsLayout.FILL, 8, 18, 50 },
				{ CellsLayout.FILL } }));

		compSheet.add(p, "0,0");
		compSheet.add(ts, "1,0");
		compSheet.add(desc2Label, "3,0");
		compSheet.add(new JScrollPane(compPropDescTx), "4,0");

		pane.add("Container", null);
		pane.add("Component", null);
		pane.add("Row", null);
		pane.add("Column", null);
		pane.setIconAt(0, CellsUtil.getImage("LayoutProp"));
		pane.setIconAt(1, CellsUtil.getImage("CompProp"));
		pane.setIconAt(2, CellsUtil.getImage("RowProp"));
		pane.setIconAt(3, CellsUtil.getImage("ColProp"));

		pane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (pane.getSelectedIndex() == 0) {
					designer.deselectAll();
					designer.redraw();
				}
			}
		});

		setLayout(new BorderLayout(0, 0));
		add(pane, BorderLayout.CENTER);
	}

	public PropertySheetTable getPropertyTable() {
		return propsTable;
	}

	public EventSheetTable getEventsTable() {
		return eventsTable;
	}

	public JBeansTable getBeansTable() {
		return btable;
	}

	public Map<String, Property> getComponentPropertiesMap(Component comp) {
		return propsMap.get(comp);
	}

	public Map<String, Property> getEventsPropertiesMap(Component comp) {
		return eventsMap.get(comp);
	}

	public Map<String, JBeansProperty> getContainerPropertiesMap(Component comp) {
		return btable.getBeansModel().getPropertiesMap(comp);
	}

	public Collection<Property> getComponentProperties(Component comp) {
		Map<String, Property> props = propsMap.get(comp);
		return props == null ? null : props.values();
	}

	public Collection<Property> getEventsProperties(Component comp) {
		Map<String, Property> events = eventsMap.get(comp);
		return events == null ? null : events.values();
	}

	public Collection<JBeansProperty> getContainerProperties(Component comp) {
		return btable.getBeansModel().getProperties(comp);
	}

	public Collection<Property> createComponentProprties(Component comp) {
		Map<String, Property> props = new TreeMap<String, Property>();
		scanLayoutProperties(comp, props);
		scanBeanProperties(comp, null, props);
		return props.values();
	}

	public Collection<Property> createEventProprties(Component comp) {
		Map<String, Property> events = new TreeMap<String, Property>();
		scanEventProperties(comp, events);
		return events.values();
	}

	public Collection<JBeansProperty> createContainerProperties(Component comp) {
		return btable.getBeansModel().createProperties("Properties", comp,
				ROOT_CONTAINER_PROPERTIES);
	}

	private void writeJBeanProperties(Component comp,
			Collection<JBeansProperty> props) {
		btable.getBeansModel().writeProperties(comp, props);
	}

	private void copyJBeanProperties(Component src, Component dst) {
		try {
			Collection<JBeansProperty> props = createContainerProperties(src);
			writeJBeanProperties(dst, props);
		} catch (Exception e) {
			// this is happened during running preview
			Collection<JBeansProperty> sp = createContainerProperties(src);
			Collection<JBeansProperty> dp = createContainerProperties(dst);
			Iterator<JBeansProperty> it = dp.iterator();
			for (JBeansProperty p1 : sp) {
				JBeansProperty p2 = it.next();
				p2.changeValue(p1.getValue());
				p2.writeToObject(dst);
			}
		}
	}

	public synchronized void copyProperties(Component src, Component dst) {

		designer.getActionManager().undoRedoEnabled(false);

		// first root container properties (JBeansProperty)
		Map<String, JBeansProperty> srcContMap = getContainerPropertiesMap(src);
		Map<String, JBeansProperty> dstContMap = getContainerPropertiesMap(dst);
		if (srcContMap != null && dstContMap != null) {
			for (JBeansProperty prop : srcContMap.values()) {
				if (prop.isChanged() && !ignored(prop.getName())) {
					JBeansProperty dstProp = dstContMap.get(prop.getName());
					dstProp.setValue(prop.getValue());
					dstProp.writeToObject(dst);
					dstProp.forceChanged();

					// update component property of the container
					Map<String, Property> compMap = getComponentPropertiesMap(dst);

					if (compMap != null) {
						Property dp = compMap.get(dstProp.getName());
						dp.changed = true;
						dp.achanged = true;
					}
				}
			}
		} else if (src != null && dst != null) {
			copyJBeanProperties(src, dst);
		}

		// and components properties (Property)
		Map<String, Property> srcCompMap = getComponentPropertiesMap(src);
		Map<String, Property> dstCompMap = getComponentPropertiesMap(dst);
		if (srcCompMap != null) {
			for (Property prop : srcCompMap.values()) {
				if (!prop.isLayoutProperty && prop.changed
						&& !ignored(prop.name)) {

					try {
						CellsUtil.invokeMethod(prop.pd.getWriteMethod()
								.getName(), new Class[] { prop.type }, dst,
								prop.newValue);
					} catch (Exception ex) {
						DebugPanel.printStackTrace(ex);
					}

					if (dstCompMap != null) {
						Property dstProp = dstCompMap.get(prop.name);
						dstProp.changed = true;

						// update container property of the component
						Map<String, JBeansProperty> contMap = getContainerPropertiesMap(dst);
						if (contMap != null) {
							JBeansProperty dp = contMap.get(dstProp.name);
							if (dp != null) {
								dp.forceChanged();
								dp.writeToObject(dst);
							}
						}
					}
				}
			}
		}

		// and event properties (Property)
		Map<String, Property> srcEventsMap = getEventsPropertiesMap(src);
		Map<String, Property> dstEventsMap = getEventsPropertiesMap(src);
		if (srcEventsMap != null) {
			for (Property prop : srcEventsMap.values()) {
				if (prop.changed) {
					if (dstEventsMap != null) {
						Property dstProp = dstEventsMap.get(prop.name);
						dstProp.changed = true;
					}
				}
			}
		}

		designer.getActionManager().undoRedoEnabled(true);
	}

	public synchronized void loadProperties(Container cont) {
		loadComponent(cont, null);
		for (Component co : cont.getComponents()) {
			if (co instanceof Container) {
				loadProperties((Container) co);
			} else {
				loadComponent(co, null);
			}
		}
	}

	public synchronized void unloadProperties(Container cont) {
		unloadComponent(cont);
		for (Component co : cont.getComponents()) {
			if (co instanceof Container) {
				unloadProperties((Container) co);
			} else {
				unloadComponent(co);
			}
		}
	}

	public synchronized void loadComponent(Component comp, Component oldcomp) {
		cons = null;
		load(comp, oldcomp);
	}

	public synchronized void unloadComponent(Component comp) {
		propsMap.remove(comp);
		eventsMap.remove(comp);
		btable.getBeansModel().removeProperties(comp);
		modifiers.remove(comp);
	}

	private synchronized void load(Component comp, Component oldcomp) {

		props = propsMap.get(comp);
		events = eventsMap.get(comp);

		if (props == null) {
			props = new TreeMap<String, Property>();
			propsMap.put(comp, props);
		}
		if (events == null) {
			events = new TreeMap<String, Property>();
			eventsMap.put(comp, events);
		}

		scanJBeansProperties(comp, oldcomp);
		scanLayoutProperties(comp, props);
		scanBeanProperties(comp, oldcomp, props);
		scanEventProperties(comp, events);

		if (modifiers.get(comp) == null) {
			modifiers.put(comp, MODIFIER_PACKAGE);
		}
	}

	private synchronized void reload(Component comp) {

		if (comp == null) {
			propsTable.clear();
			eventsTable.clear();
			return;
		}

		load(comp, null);

		propsTable.update(props);
		eventsTable.update(events);
	}

	private void scanJBeansProperties(Component comp, Component oldComp) {
		if (comp instanceof JPanel) {
			btable.setComponent("Properties", comp, oldComp, ROOT_CONTAINER_PROPERTIES);
		}
	}

	private void scanLayoutProperties(Component comp, Map<String, Property> props) {

		if (cons != null) {

			// first remove all layoutproperties, since moving
			// component from a container to another container
			List<String> keys = new ArrayList<String>();
			for (Property p : props.values()) {
				if (p.isLayoutProperty) {
					keys.add(p.name);
				}
			}

			for (String key : keys) {
				props.remove(key);
			}

			// ========

			Property[] lprops = cons.getContainer().getConstraintsProperties(comp);
			
			for (Property p : lprops) {
				Property prop = props.get(p.name);
				if (prop != null) {
					prop.newValue = p.newValue;
					prop.readOnly = p.readOnly;
					prop.comp = comp;
				} else {
					p.comp = comp;
					props.put(p.name, p);
				}
			}
		}
	}

	private void scanBeanProperties(Component comp, Component oldcomp,
			Map<String, Property> props) {

		// Attention:
		// We take the properties from the oldcomp and not from the comp
		// because of, if the oldcomp exists then the comp is copy of
		// oldcomp and has not correct beans property! this situation
		// is only happened, wenn a JPanel is replaced by DesignerContainer

		Object xo = oldcomp != null ? oldcomp : comp;

		try {
			BeanInfo info = Introspector.getBeanInfo(xo.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {

				if (pd.getReadMethod() != null) {

					Object o = null;

					try {
						o = pd.getReadMethod().invoke(xo, new Object[0]);

					} catch (Exception ex) {
					}

					Property prop = props.get(pd.getName());
					if (prop == null) {
						prop = new Property(pd, o);
						prop.comp = comp;

						prop.changed = prop.achanged ? true : prop.name
								.equals("text")
								|| (xo instanceof JTable && prop.name
										.equals("model"));

						prop.isReplaced = oldcomp != null;
						props.put(pd.getName(), prop);
					} else {
						prop.newValue = o;
					}
				}
			}

		} catch (IntrospectionException ex) {
			DebugPanel.printStackTrace(ex);
		}
	}

	private void scanEventProperties(Component comp,
			Map<String, Property> events) {
		try {
			BeanInfo info = Introspector.getBeanInfo(comp.getClass(),
					Object.class);

			for (EventSetDescriptor esd : info.getEventSetDescriptors()) {

				Method alm = esd.getAddListenerMethod();
				String s = alm.getName().substring(3);
				Property prop = events.get(s);

				if (prop == null) {
					prop = new Property(s, String.class, "", false, false);
					prop.comp = comp;
					prop.ed = esd;
					events.put(s, prop);
				}

				Method[] ms = esd.getListenerMethods();
				for (Method m : ms) {
					prop = events.get(m.getName());

					if (prop == null) {
						prop = new Property(m.getName(), String.class, "",
								true, false);
						prop.comp = null;
						prop.ed = esd;
						events.put(m.getName(), prop);
					}
				}
			}

		} catch (IntrospectionException ex) {
			DebugPanel.printStackTrace(ex);
		}
	}

	public void changeProperty(Object key, Object oldValue, Object newValue) {
		Property pr = props.get(key);
		Action action = designer.getActionManager().getAction("ChangeProperty");
		action.putValue("Cons", cons);
		action.putValue("Name", key);
		action.putValue("Value", newValue);
		action.putValue("OldValue", oldValue);
		action.putValue("Type", pr == null ? newValue.getClass() : pr.type);
		action.actionPerformed(null);
	}

	public Object changeProperty(DesignerConstraints cons, String name,
			Object value, Class type) {

		Component comp = cons.getComponent();
		Property prop = props.get(name);

		Object oldValue = null;

		if (name.equals("modifier")) {
			oldValue = modifiers.get(comp);
			modifiers.put(comp, (Integer) value);
			designer.getDebuger().appendMessage(
					"property changed (" + name + "): "
							+ MODIFIERS[(Integer) oldValue] + " --> "
							+ MODIFIERS[(Integer) value] + "\n");
		} else if (cons.getContainer().isConstraintsProperty(comp, name)) {
			oldValue = cons.getContainer().changeConstraintsProperty(comp,
					name, value);
			if (!value.equals(oldValue)) {
				designer.getDebuger().appendMessage(
						"property changed (" + name + "): " + oldValue
								+ " --> " + value + "\n");
			}

		} else if (name.equals("titleAt")) {
			JTabbedPane tp = (JTabbedPane) comp;
			oldValue = tp.getTitleAt(tp.getSelectedIndex());

			if (!value.equals(oldValue)) {
				designer.getDebuger().appendMessage(
						"property changed (" + name + "): " + oldValue
								+ " --> " + value + "\n");

				int i = -1;
				String s = value.toString();
				if (s.startsWith("#%#")) {
					i = Integer.valueOf(s.substring(3, 5));
					s = s.substring(5);
				} else {
					i = tp.getSelectedIndex();
				}

				tp.setTitleAt(i, s);

				s = "" + tp.getSelectedIndex();
				if (s.length() == 1) {
					s = "0" + s;
				}

				value = "#%#" + s + value.toString();
				oldValue = "#%#" + s + oldValue.toString();
			}

		} else if (prop != null && !prop.readOnly) {

			oldValue = prop.newValue;
			if (value != null && !value.equals(oldValue)) {

				try {
					prop.pd.getWriteMethod().invoke(comp, value);

				} catch (Exception ex) {
					DebugPanel.printStackTrace(ex);
					return null;
				}
			}
			designer.getDebuger().appendMessage(
					"property changed (" + name + "): " + oldValue + " --> "
							+ value + "\n");
		}

		if (prop != null) {
			if (prop.value == null && value == null) {
				prop.changed = false;
			} else if (value != null && value.equals(prop.value)) {
				prop.changed = false;
			} else {
				prop.changed = true;
				prop.newValue = value;
			}
			if (prop.achanged) {
				prop.changed = true;
			}
			propsTable.checkNode(name, prop.changed);
		}

		// if the name of container was changed, the name of
		// all it's children should be changed too!
		if (comp instanceof Container && name.equals("name")) {
			Container cont = (Container) comp;
			for (Component co : cont.getComponents()) {
				String s = co.getName();
				if (s != null) {
					int i = s.indexOf("_");
					if (i != -1) {
						s = s.substring(i + 1);
						co.setName(cont.getName() + "_" + s);
					}
				}
			}
		}

		return oldValue;
	}

	public void setVisibility(Component comp, int vs) {
		if (changingVisibility) {
			coVisibility.setSelectedIndex(vs == -1 ? 0 : vs);
		} else {
			modifiers.put(comp, vs);
		}
	}

	public int getVisibility(Component comp) {
		Integer vs = modifiers.get(comp);
		return vs == null ? -1 : vs;
	}

	public String getVisibilityName(Component comp) {
		Integer vs = modifiers.get(comp);
		return vs == null ? "" : MODIFIERS[vs];
	}

	private Object getPropertyValue(Object comp, String propertyName) {

		try {
			BeanInfo info = Introspector.getBeanInfo(comp.getClass(),
					Object.class);
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if (pd.getName().equals(propertyName)
						&& pd.getReadMethod() != null) {
					return pd.getReadMethod().invoke(comp, new Object[0]);
				}
			}
		} catch (InvocationTargetException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (IntrospectionException e) {
		}

		return null;
	}

	private boolean ignored(String name) {
		for (String s : IGNORED_COPY_PROPERTIES) {
			if (s.equals(name)) {
				return true;
			}
		}
		return false;
	}

	private void updatePropertyDescription(String txt) {
		if (txt == null) {
			JOriginalTable.JOriginalTableNode node = propsTable.getSelectedNode();
			if (node != null) {
				Property ac = props.get(node.getKey());
				if (ac != null && ac.pd != null) {
					txt = ac.pd.getShortDescription();
				}
			}
		}
		compPropDescTx.setText(txt);
	}

	private void updateJBeansPropertyDescription() {
		contPropDescTx.setText("");
		int i = btable.getSelectedRow();
		if (i != -1) {
			JBeansProperty prop = btable.getBeansModel().getProperty(i);
			if (prop != null) {
				contPropDescTx.setText(prop.getShortDescription());
			}
		}
	}

	// ======== SHEET GUI =========
	
	public void setUpdateAdjusting(boolean b) {
		updateAdjusting = b;
	}

	public void update() {

		remove(pane);

		cons = null;
		DesignerFrame df = designer.getDesktop().getDesigner();

		if (df != null) {

			DesignerContainer cont = df.getContainer();
			cons = cont.getSelectedDesignerConstraints();

			pane.setEnabledAt(0, false);
			pane.setEnabledAt(1, false);
			pane.setEnabledAt(2, false);
			pane.setEnabledAt(3, false);

			add(pane, BorderLayout.CENTER);

			cont.updateSheets();

			JPanel p1 = cont.createRowSheet();
			JPanel p2 = cont.createColumnSheet();

			if (!updateAdjusting) {
				scanJBeansProperties(cont, null);
			}

			layoutPanel.setContainer(cont);

			pane.setComponentAt(0, contSheet);
			pane.setEnabledAt(0, true);

			if (cons.getComponent() != null) {

				pane.setComponentAt(1, compSheet);
				pane.setEnabledAt(1, true);
				pane.setSelectedIndex(1);

				tfType.setText(cons.getComponent().getClass().getName());
				tfName.setText(cons.getComponent().getName());

				changingVisibility = true;
				Integer vs = modifiers.get(cons.getComponent());
				setVisibility(cons.getComponent(), vs == null ? -1 : vs);
				changingVisibility = false;

				reload(cons.getComponent());

			} else if (p1 != null) {
				pane.setComponentAt(2, p1);
				pane.setEnabledAt(2, true);
				pane.setSelectedIndex(2);

			} else if (p2 != null) {
				pane.setComponentAt(3, p2);
				pane.setEnabledAt(3, true);
				pane.setSelectedIndex(3);
			} else {
				pane.setSelectedIndex(0);
			}
		}
	}
}
