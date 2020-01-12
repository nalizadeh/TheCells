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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
public class JBeansDefaultProperty implements JBeansProperty {

	private String name;
	private String displayName;
	private String shortDescription;
	private Class type;
	private Object value;
	private Object oldValue;
	private Object origValue;
	private boolean editable;
	private boolean changed;
	private boolean forcechanged;

	private String category;

	private JBeansProperty parent;
	private List<JBeansProperty> subProperties = new ArrayList<JBeansProperty>();

	// PropertyChangeListeners are not serialized.
	private transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	public JBeansDefaultProperty() {
		this(null, null, null, null, null, false);
	}

	public JBeansDefaultProperty(
		String  name,
		String  displayName,
		String  shortDescription,
		Class   type,
		Object  value,
		boolean editable
	) {
		this.name = name;
		this.displayName = displayName;
		this.shortDescription = shortDescription;
		this.type = type;
		this.value = value;
		this.oldValue = value;
		this.origValue = value;
		this.editable = editable;
		this.parent = null;
		this.category = null;
		this.changed = false;
		this.forcechanged = false;
	}

	public JBeansDefaultProperty(
		String  category,
		String  name,
		String  displayName,
		String  shortDescription,
		Class   type,
		Object  value,
		boolean editable
	) {
		this(name, displayName, shortDescription, type, value, editable);
		this.category = category;
	}

	public JBeansProperty clone() {
		JBeansDefaultProperty other = new JBeansDefaultProperty();
		other.name = name;
		other.displayName = displayName;
		other.shortDescription = shortDescription;
		other.type = type;
		other.editable = editable;
		other.changed = changed;
		other.forcechanged = forcechanged;
		other.category = category;
		other.value = value;
		other.oldValue = oldValue;
		other.origValue = origValue;
		return other;
	}

	public Component getOwner() {
		return null;
	}

	public void setOwner(Component owner) {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public Class getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object v) {
		if (v != value && (v == null || !v.equals(value))) {
			oldValue = value;
			value = v;
			firePropertyChange(oldValue, value);
		}
	}

	public Object getOldValue() {
		return oldValue;
	}

	public void setOldValue(Object value) {
		this.oldValue = value;
	}

	public Object getOriginalValue() {
		return origValue;
	}

	public void setOriginalValue(Object value) {
		this.origValue = value;
	}

	public void changeValue(Object value) {
		this.oldValue = this.value;
		this.value = value;
	}

	public void copyValue(JBeansProperty other) {
		this.value = other.getValue();
		this.oldValue = other.getOldValue();
		this.origValue = other.getOriginalValue();
	}

	public boolean isEditable() {
		return editable;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = forcechanged ? true : changed;
	}

	public boolean isForceChanged() {
		return forcechanged;
	}

	public void forceChanged() {
		this.forcechanged = true;
		this.changed = true;
	}

	public JBeansProperty getParentProperty() {
		return null;
	}

	public void setParentProperty(JBeansProperty parent) {
		this.parent = parent;
	}

	public JBeansProperty[] getSubProperties() {
		return (JBeansProperty[]) subProperties.toArray(new JBeansProperty[subProperties.size()]);
	}

	public void clearSubProperties() {
		for (Iterator iter = this.subProperties.iterator(); iter.hasNext();) {
			JBeansProperty subProp = (JBeansProperty) iter.next();
			if (subProp instanceof JBeansDefaultProperty) {
				((JBeansDefaultProperty) subProp).setParentProperty(null);
			}
		}
		this.subProperties.clear();
	}

	public void addSubProperties(Collection subProperties) {
		this.subProperties.addAll(subProperties);
		for (Iterator iter = this.subProperties.iterator(); iter.hasNext();) {
			JBeansProperty subProp = (JBeansProperty) iter.next();
			if (subProp instanceof JBeansDefaultProperty) {
				((JBeansDefaultProperty) subProp).setParentProperty(this);
			}
		}
	}

	public void addSubProperties(JBeansProperty[] subProperties) {
		this.addSubProperties(Arrays.asList(subProperties));
	}

	public void addSubProperty(JBeansProperty subProperty) {
		this.subProperties.add(subProperty);
		if (subProperty instanceof JBeansDefaultProperty) {
			((JBeansDefaultProperty) subProperty).setParentProperty(this);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
		JBeansProperty[] subProperties = getSubProperties();
		if (subProperties != null) {
			for (int i = 0; i < subProperties.length; ++i) {
				subProperties[i].addPropertyChangeListener(listener);
			}
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
		JBeansProperty[] subProperties = getSubProperties();
		if (subProperties != null) {
			for (int i = 0; i < subProperties.length; ++i) {
				subProperties[i].removePropertyChangeListener(listener);
			}
		}
	}

	protected void firePropertyChange(Object oldValue, Object newValue) {
		listeners.firePropertyChange("value", oldValue, newValue);
	}

	public Method getReadMethod(Object object) {
		return JBeansUtils.getReadMethod(object.getClass(), getName());
	}

	public Method getWriteMethod(Object object) {
		return JBeansUtils.getWriteMethod(object.getClass(), getName(), getType());
	}

	public void readFromObject(Object object) {
		try {
			Method method = getReadMethod(object);
			if (method != null) {
				Object value = method.invoke(object, new Object[0]);
				this.origValue = value;
				this.oldValue = value;
				this.value = value;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		for (Iterator iter = subProperties.iterator(); iter.hasNext();) {
			JBeansProperty subProperty = (JBeansProperty) iter.next();
			subProperty.readFromObject(object);
		}
	}

	public void writeToObject(Object object) {
		try {
			Method method = getWriteMethod(object);
			if (method != null) {
				Object value = getValue();
				method.invoke(object, new Object[] { value });

				setChanged(forcechanged
					|| (value != null && origValue == null)
					|| (value == null && origValue != null)
					|| (value != null && !value.equals(origValue))
				);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (parent != null) {
			parent.readFromObject(object);
		}
		for (Iterator iter = subProperties.iterator(); iter.hasNext();) {
			JBeansProperty subProperty = (JBeansProperty) iter.next();
			subProperty.readFromObject(object);
		}
	}

	public int hashCode() {
		return 28 + ((name != null) ? name.hashCode() : 3)
		+ ((displayName != null) ? displayName.hashCode() : 94)
		+ ((shortDescription != null) ? shortDescription.hashCode() : 394)
		+ ((category != null) ? category.hashCode() : 34) + ((type != null) ? type.hashCode() : 39)
		+ Boolean.valueOf(editable).hashCode();
	}

	public boolean equals(Object other) {
		if (other == null || getClass() != other.getClass()) {
			return false;
		}

		if (other == this) {
			return true;
		}

		JBeansDefaultProperty dp = (JBeansDefaultProperty) other;

		return compare(name, dp.name) && compare(displayName, dp.displayName)
		&& compare(shortDescription, dp.shortDescription) && compare(category, dp.category)
		&& compare(type, dp.type) && editable == dp.editable;
	}

	private boolean compare(Object o1, Object o2) {
		return (o1 != null) ? o1.equals(o2) : o2 == null;
	}

	public String toString() {
		return "name=" + getName() + ", displayName=" + getDisplayName() + ", type=" + getType()
		+ ", category=" + getCategory() + ", editable=" + isEditable() + ", value=" + getValue();
	}
}
