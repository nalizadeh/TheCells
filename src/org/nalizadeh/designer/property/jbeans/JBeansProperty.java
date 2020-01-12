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
import java.io.Serializable;
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
public interface JBeansProperty extends Serializable, Cloneable {

	public Component getOwner();

	public void setOwner(Component owner);

	public String getName();

	public String getDisplayName();

	public String getShortDescription();

	public Class getType();

	public Object getValue();

	public void setValue(Object value);

	public Object getOldValue();

	public void setOldValue(Object value);

	public Object getOriginalValue();

	public void setOriginalValue(Object value);

	public void changeValue(Object value);

	public void copyValue(JBeansProperty other);

	public boolean isEditable();

	public String getCategory();

	public void setCategory(String category);

	public boolean isChanged();

	public void setChanged(boolean changed);

	public void forceChanged();

	public boolean isForceChanged();

	public void readFromObject(Object object);

	public void writeToObject(Object object);

	public Method getReadMethod(Object object);

	public Method getWriteMethod(Object object);

	public void addPropertyChangeListener(PropertyChangeListener listener);

	public void removePropertyChangeListener(PropertyChangeListener listener);

	public JBeansProperty clone();

	public JBeansProperty getParentProperty();

	public JBeansProperty[] getSubProperties();
}
