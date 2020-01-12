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

import java.awt.Component;
import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

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
public class Property {

	public String name;
	public Class type;
	public Object value;
	public Object newValue;
	public boolean readOnly;
	public boolean changed;
	public boolean achanged;

	public Component comp;
	public PropertyDescriptor pd;
	public EventSetDescriptor ed;

	public boolean isReplaced;
	public boolean isLayoutProperty;

	public Property(PropertyDescriptor pd, Object value) {
		this.pd = pd;
		this.name = pd.getName();
		this.type = pd.getPropertyType();
		this.value = value;

		this.newValue = value;
		this.readOnly = pd.getWriteMethod() == null;
		this.changed = false;
		this.achanged = false;
		this.comp = null;
		this.ed = null;
		this.isReplaced = false;
		this.isLayoutProperty = false;
	}

	public Property(
		String  name,
		Class   type,
		Object  value,
		boolean readOnly,
		boolean isLayoutProperty
	) {
		this.name = name;
		this.type = type;
		this.value = value;

		this.newValue = value;
		this.readOnly = readOnly;
		this.changed = false;
		this.achanged = false;

		this.comp = null;
		this.pd = null;
		this.ed = null;

		this.isReplaced = false;
		this.isLayoutProperty = isLayoutProperty;
	}

	public void read(Object object) {
		if (pd != null) {
			try {
				Method method = pd.getReadMethod();
				if (method != null) {
					value = method.invoke(object, new Object[0]);
					newValue = value;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void write(Object object) {
		if (pd != null) {
			try {
				Method method = pd.getWriteMethod();
				if (method != null) {
					method.invoke(object, new Object[] { newValue });
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public String toString() {
		return name;
	}
}
