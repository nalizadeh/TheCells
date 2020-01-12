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

package org.nalizadeh.designer.property.jbeans;

import java.awt.Component;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

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
public class JBeansTableModel extends AbstractTableModel implements PropertyChangeListener {

	public static final int VIEW_LIST = 0;
	public static final int VIEW_CATEGORIES = 1;

	public static final int NAME_COLUMN = 0;
	public static final int VALUE_COLUMN = 1;
	public static final int NUM_COLUMNS = 2;

	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	private Map<Component, Map> propertiesMap = new HashMap<Component, Map>();
	private Map<String, JBeansProperty> properties;

	private List<Item> model = new ArrayList<Item>();
	private List<Item> modelExpanded = new ArrayList<Item>();

	private Map<String, Icon> categoryIcons = new HashMap<String, Icon>();

	private int mode;
	private boolean sortingCategories = false;
	private boolean sortingProperties = false;
	private Comparator categorySortingComparator;
	private Comparator propertySortingComparator;

	private JBeansTable table;

	public JBeansTableModel(JBeansTable table) {
		this(table, VIEW_LIST);
	}

	public JBeansTableModel(JBeansTable table, int mode) {
		this.table = table;
		this.mode = mode;
	}

	public Map<String, JBeansProperty> getPropertiesMap(Component comp) {
		return propertiesMap.get(comp);
	}

	public Collection<JBeansProperty> getProperties(Component comp) {
		Map<String, JBeansProperty> props = propertiesMap.get(comp);
		return props == null ? null : props.values();
	}

	public void removeProperties(Component comp) {
		propertiesMap.remove(comp);
	}

	public void replaceProperties(Component oldComp, Component newComp) {
		Map<String, JBeansProperty> props = propertiesMap.get(oldComp);
		if (props != null) {

			propertiesMap.remove(oldComp);
			propertiesMap.put(newComp, props);

			Collection<JBeansProperty> values = props.values();
			for (JBeansProperty p : values) {
				if (p.getOwner() != null) {
					p.setOwner(newComp);
				}
			}
		}
	}

	public Collection<JBeansProperty> createProperties(
		String    category,
		Component comp,
		String[]  includes
	) {
		List<JBeansProperty> props = new ArrayList<JBeansProperty>();

		BeanInfo beanInfo = new SimpleBeanInfo();
		try {
			beanInfo = Introspector.getBeanInfo(comp.getClass());
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return props;
		}

		// take only the included properties
		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

		for (int i = 0; i < descriptors.length; i++) {
			if (includes != null) {
				boolean found = false;
				for (String s : includes) {
					if (s.equals(descriptors[i].getName())) {
						found = true;
					}
				}
				if (!found) {
					continue;
				}
			}
			JBeansProperty prop = new JBeansPropertyDescriptor(comp, descriptors[i]);
			prop.addPropertyChangeListener(this);
			prop.setCategory(category);
			prop.readFromObject(comp);

			props.add(prop);
		}
		return props;
	}

	public void writeProperties(Component comp, Collection<JBeansProperty> props) {
		for (JBeansProperty p : props) {
			p.writeToObject(comp);
		}
	}

	public void setComponent(String category, Component comp, Component oldComp) {
		setComponent(category, comp, oldComp, null);
	}

	public synchronized void setComponent(
		String    category,
		Component comp,
		Component oldComp,
		String[]  includes
	) {

		properties = propertiesMap.get(comp);

		if (properties == null) {
			properties = new HashMap<String, JBeansProperty>();
			propertiesMap.put(comp, properties);
		}

		BeanInfo beanInfo = new SimpleBeanInfo();
		try {
			beanInfo = Introspector.getBeanInfo(comp.getClass());
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}

		PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();

		for (int i = 0; i < descriptors.length; i++) {

			// take only the included properties
			if (includes != null) {
				boolean found = false;
				for (String s : includes) {
					if (s.equals(descriptors[i].getName())) {
						found = true;
					}
				}
				if (!found) {
					continue;
				}
			}

			JBeansProperty prop = properties.get(descriptors[i].getName());

			if (prop == null) {
				prop = new JBeansPropertyDescriptor(comp, descriptors[i]);
				prop.addPropertyChangeListener(this);
				prop.setCategory(category);
				properties.put(descriptors[i].getName(), prop);
			}

			prop.readFromObject(oldComp != null ? oldComp : comp);
		}

		buildModel();
	}

	public void addProperties(
		Component			 comp,
		String				 category,
		PropertyDescriptor[] descriptors,
		String[]			 includes
	) {
		List<JBeansProperty> props = new ArrayList<JBeansProperty>();
		for (int i = 0; i < descriptors.length; i++) {
			if (includes != null) {
				boolean found = false;
				for (String s : includes) {
					if (s.equals(descriptors[i].getName())) {
						found = true;
					}
				}
				if (!found) {
					continue;
				}
			}
			props.add(new JBeansPropertyDescriptor(comp, descriptors[i]));
		}
		addProperties(category, props);
	}

	public void addProperties(String category, Collection<JBeansProperty> newProperties) {

		// add listeners
		for (JBeansProperty prop : newProperties) {

			if (!properties.containsKey(prop.getName())) {
				prop.addPropertyChangeListener(this);
				prop.setCategory(category);

				JBeansProperty[] subProp = prop.getSubProperties();
				if (subProp != null) {
					for (int i = 0; i < subProp.length; ++i) {
						subProp[i].addPropertyChangeListener(this);
					}
				}

				properties.put(prop.getName(), prop);
			}
		}

		buildModel();
	}

	public void addProperty(JBeansProperty property) {
		if (!properties.containsKey(property.getName())) {
			property.addPropertyChangeListener(this);
			properties.put(property.getName(), property);
			buildModel();
		}
	}

	public void removeProperty(JBeansProperty property) {
		if (properties.containsKey(property.getName())) {
			property.removePropertyChangeListener(this);
			properties.remove(property);
			buildModel();
		}
	}

	public void removeAll() {

		PropertyChangeListener[] pl = listeners.getPropertyChangeListeners();
		for (PropertyChangeListener ls : pl) {
			removePropertyChangeListener(ls);
		}

		// unregister the listeners from previous properties
		Collection<JBeansProperty> props = properties.values();
		for (JBeansProperty prop : props) {
			prop.removePropertyChangeListener(this);

			JBeansProperty[] subProp = prop.getSubProperties();
			if (subProp != null) {
				for (int i = 0; i < subProp.length; ++i) {
					subProp[i].removePropertyChangeListener(this);
				}
			}
		}

		properties.clear();
		buildModel();
	}

	public JBeansProperty[] getProperties() {
		Collection<JBeansProperty> props = properties.values();
		return (JBeansProperty[]) props.toArray(new JBeansProperty[properties.size()]);
	}

	public int getPropertyCount() {
		return properties.size();
	}

	public JBeansProperty getProperty(String name) {
		return properties.get(name);
	}

	public JBeansProperty getProperty(int index) {
		Item item = getItem(index);
		if (item.isProperty()) {
			return item.getProperty();
		}
		return null;
	}

	public void setCategoryIcon(String category, Icon icon) {
		categoryIcons.put(category, icon);
	}

	public Icon getCategoryIcon(String category) {
		return categoryIcons.get(category);
	}

	public void setMode(int mode) {
		if (this.mode == mode) {
			return;
		}
		this.mode = mode;
		buildModel();
	}

	public int getMode() {
		return mode;
	}

	public Class getColumnClass(int columnIndex) {
		return super.getColumnClass(columnIndex);
	}

	public int getColumnCount() {
		return NUM_COLUMNS;
	}

	public int getRowCount() {
		return modelExpanded.size();
	}

	public Item getItem(int rowIndex) {
		if (getRowCount() > rowIndex) {
			return (Item) modelExpanded.get(rowIndex);
		}
		return null;
	}

	public boolean isSortingCategories() {
		return sortingCategories;
	}

	public void setSortingCategories(boolean value) {
		if (sortingCategories != value) {
			sortingCategories = value;
			buildModel();
		}
	}

	public boolean isSortingProperties() {
		return sortingProperties;
	}

	public void setSortingProperties(boolean value) {
		if (sortingProperties != value) {
			sortingProperties = value;
			buildModel();
		}
	}

	public void setCategorySortingComparator(Comparator comp) {
		if (categorySortingComparator != comp) {
			categorySortingComparator = comp;
			buildModel();
		}
	}

	public void setPropertySortingComparator(Comparator comp) {
		if (propertySortingComparator != comp) {
			propertySortingComparator = comp;
			buildModel();
		}
	}

	public boolean isCellEditable(int row, int column) {
		if (column == 0) {
			return false;
		}
		Item item = getItem(row);
		return item.isProperty() && item.getProperty().isEditable();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result = null;
		Item item = getItem(rowIndex);

		if (item != null && item.isProperty()) {
			switch (columnIndex) {

				case NAME_COLUMN :
					result = item;
					break;

				case VALUE_COLUMN :
					try {
						result = item.getProperty().getValue();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default :
					// should not happen
			}
		} else {
			result = item;
		}
		return result;
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Item item = getItem(rowIndex);
		if (item.isProperty()) {
			if (columnIndex == VALUE_COLUMN) {
				try {
					item.getProperty().setValue(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		JBeansProperty prop = (JBeansProperty) evt.getSource();
		listeners.firePropertyChange(evt);
		table.changeProperty(prop);
	}

	private void visibilityChanged() {
		modelExpanded.clear();
		for (Iterator iter = model.iterator(); iter.hasNext();) {
			Item item = (Item) iter.next();
			Item parent = item.getParent();
			if (parent == null || parent.isVisible()) {
				modelExpanded.add(item);
			}
		}
		fireTableDataChanged();
	}

	private void buildModel() {

		model.clear();
		modelExpanded.clear();

		if (properties != null && properties.size() > 0) {
			Collection<JBeansProperty> sortedProperties = sortProperties(properties.values());

			switch (mode) {

				case VIEW_LIST :

					// just add all the properties without categories
					addPropertiesToModel(sortedProperties, null);
					break;

				case VIEW_CATEGORIES : {

					// add properties by category
					Collection<String> categories =
						sortCategories(getPropertyCategories(sortedProperties));

					for (String category : categories) {
						Item categoryItem = new Item(category, null);
						model.add(categoryItem);
						addPropertiesToModel(
							sortProperties(getPropertiesForCategory(properties.values(), category)),
							categoryItem
						);
					}
					break;
				}

				default :
					// should not happen
			}
		}

		visibilityChanged();
		fireTableDataChanged();
	}

	protected Collection<JBeansProperty> sortProperties(
		Collection<JBeansProperty> localProperties
	) {
		List<JBeansProperty> sortedProperties = new ArrayList<JBeansProperty>(localProperties);
		if (sortingProperties) {
			if (propertySortingComparator == null) {

				// if no comparator was defined by the user, use the default
				propertySortingComparator = new PropertyComparator();
			}
			Collections.sort(sortedProperties, propertySortingComparator);
		}
		return sortedProperties;
	}

	protected Collection<String> sortCategories(Collection<String> localCategories) {
		List<String> sortedCategories = new ArrayList<String>(localCategories);
		if (sortingCategories) {
			if (categorySortingComparator == null) {

				// if no comparator was defined by the user, use the default
				categorySortingComparator = STRING_COMPARATOR;
			}
			Collections.sort(sortedCategories, categorySortingComparator);
		}
		return sortedCategories;
	}

	protected Collection<String> getPropertyCategories(Collection<JBeansProperty> localProperties) {
		List<String> categories = new ArrayList<String>();
		for (Iterator<JBeansProperty> iter = localProperties.iterator(); iter.hasNext();) {
			JBeansProperty property = iter.next();
			if (!categories.contains(property.getCategory())) {
				categories.add(property.getCategory());
			}
		}
		return categories;
	}

	private void addPropertiesToModel(Collection<JBeansProperty> localProperties, Item parent) {
		for (Iterator<JBeansProperty> iter = localProperties.iterator(); iter.hasNext();) {
			JBeansProperty property = iter.next();
			Item propertyItem = new Item(property, parent);
			model.add(propertyItem);

			// add any sub-properties
			JBeansProperty[] subProperties = property.getSubProperties();
			if (subProperties != null && subProperties.length > 0) {
				addPropertiesToModel(Arrays.asList(subProperties), propertyItem);
			}
		}
	}

	private Collection<JBeansProperty> getPropertiesForCategory(
		Collection<JBeansProperty> localProperties,
		String					   category
	) {
		List<JBeansProperty> categoryProperties = new ArrayList();
		for (Iterator<JBeansProperty> iter = localProperties.iterator(); iter.hasNext();) {
			JBeansProperty property = iter.next();
			if (
				(category == property.getCategory())
				|| (category != null && category.equals(property.getCategory()))
			) {
				categoryProperties.add(property);
			}
		}
		return categoryProperties;
	}

	public class Item {
		private String name;
		private JBeansProperty property;
		private Item parent;
		private boolean hasToggle = true;
		private boolean visible = true;

		private Item(String name, Item parent) {
			this.name = name;
			this.parent = parent;
			this.hasToggle = true;
		}

		private Item(JBeansProperty property, Item parent) {
			this.name = property.getDisplayName();
			this.property = property;
			this.parent = parent;
			this.visible = (property == null);

			JBeansProperty[] subProperties = property.getSubProperties();
			hasToggle = subProperties != null && subProperties.length > 0;
		}

		public String getName() {
			return name;
		}

		public JBeansProperty getProperty() {
			return property;
		}

		public Item getParent() {
			return parent;
		}

		public boolean isProperty() {
			return property != null;
		}

		public int getDepth() {
			int depth = 0;
			if (parent != null) {
				depth = parent.getDepth();
				if (parent.isProperty()) {
					++depth;
				}
			}
			return depth;
		}

		public boolean hasToggle() {
			return hasToggle;
		}

		public void toggle() {
			if (hasToggle()) {
				visible = !visible;
				visibilityChanged();
			}
		}

		public boolean isVisible() {
			return (parent == null || parent.isVisible()) && (!hasToggle || visible);
		}
	}

	public static class PropertyComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			if (o1 instanceof JBeansProperty && o2 instanceof JBeansProperty) {
				JBeansProperty prop1 = (JBeansProperty) o1;
				JBeansProperty prop2 = (JBeansProperty) o2;
				if (prop1 == null) {
					return prop2 == null ? 0 : -1;
				} else {
					return STRING_COMPARATOR.compare(
						prop1.getDisplayName() == null ? null
						: prop1.getDisplayName().toLowerCase(),
						prop2.getDisplayName() == null ? null
						: prop2.getDisplayName().toLowerCase()
					);
				}
			} else {
				return 0;
			}
		}
	}

	public static class NaturalOrderStringComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			if (s1 == null) {
				return s2 == null ? 0 : -1;
			} else {
				if (s2 == null) {
					return 1;
				} else {
					return s1.compareTo(s2);
				}
			}
		}
	}

	private static final Comparator STRING_COMPARATOR = new NaturalOrderStringComparator();
}
