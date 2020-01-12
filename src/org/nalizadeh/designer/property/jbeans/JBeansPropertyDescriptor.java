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
public class JBeansPropertyDescriptor extends JBeansDefaultProperty {

	private Component owner;
	private PropertyDescriptor descriptor;

        public JBeansPropertyDescriptor(Component owner, PropertyDescriptor descriptor) {
		super(
			descriptor.getName(),
			descriptor.getDisplayName(),
			descriptor.getShortDescription(),
			descriptor.getPropertyType(),
			null,
			descriptor.getWriteMethod() != null
		);
		this.owner = owner;
		this.descriptor = descriptor;
	}

	public Component getOwner() {
		return owner;
	}

	public void setOwner(Component owner) {
		this.owner = owner;
	}

	public void setDescriptor(PropertyDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	public PropertyDescriptor getDescriptor() {
		return descriptor;
	}

	public JBeansProperty clone() {
		JBeansPropertyDescriptor clone = new JBeansPropertyDescriptor(owner, descriptor);
		clone.copyValue(this);
		return clone;
	}

	public Method getReadMethod(Object object) {
		return descriptor.getReadMethod();
	}

	public Method getWriteMethod(Object object) {
		return descriptor.getWriteMethod();
	}
}
