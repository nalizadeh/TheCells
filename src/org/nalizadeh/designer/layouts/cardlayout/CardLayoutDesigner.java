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

package org.nalizadeh.designer.layouts.cardlayout;

import org.nalizadeh.designer.CellsDesigner;

import org.nalizadeh.designer.base.DesignerFrame;

import java.awt.Container;
import javax.swing.JInternalFrame;

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
public class CardLayoutDesigner extends DesignerFrame {

        public CardLayoutDesigner(
                String title,
                String name,
                Container container,
                CellsDesigner designer,
                JInternalFrame parent
                ) {
                super(title, name, container, designer, parent);
                initUserArea(new CardLayoutContainer(this, true, container), null);
        }
}
