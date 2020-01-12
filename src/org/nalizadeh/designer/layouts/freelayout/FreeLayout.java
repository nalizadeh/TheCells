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

package org.nalizadeh.designer.layouts.freelayout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.awt.Insets;

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
public class FreeLayout implements LayoutManager2 {

        /**
         * Default-Konstruktor
         *
         * @exception
         *
         * @see
         */
        public FreeLayout() {
        }

        /**
         * Notwendig für den LayoutManager. Wird durch den Container aufgerufen, wenn ein Element zu den
         * Container eingefügt wird.
         *
         * @param      comp  - die neue Komponente, die zum Containter eingefügt wurde.
         * @param      co    - das Constraints-Objekt, welches die Regeln zum Layouten. enthält.
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public void addLayoutComponent(Component comp, Object constraints) {
                if (constraints instanceof Rectangle) {
                        comp.setBounds((Rectangle)constraints);
                }
        }

        /**
         * Notwendig für den LayoutManager. Wird von TableLayout nicht verwendet. Zum Einfügen der
         * Elemente zum Container muß immer eine TableConstraints verwendet werden.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public void addLayoutComponent(String name, Component comp) {
                throw new IllegalArgumentException("Rectangle must be used");
        }

        /**
         * Notwendig für den LayoutManager. Wird durch den Container aufgerufen, wenn ein Element aus
         * dem Container entfernt wird.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see
         */
        public void removeLayoutComponent(Component comp) {
        }

        /**
         * Notwendig für den LayoutManager.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public float getLayoutAlignmentX(Container parent) {
                return 0.0f;
        }

        /**
         * Notwendig für den LayoutManager.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public float getLayoutAlignmentY(Container parent) {
                return 0.0f;
        }

        /**
         * Notwendig für den LayoutManager.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public void invalidateLayout(Container parent) {
        }

        /**
         * Notwendig für den LayoutManager. Wird vom Container aufgerufen, sobald die Große des
         * Containers verändert wird oder durch die Methode 'pack' des Containers.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public Dimension minimumLayoutSize(Container parent) {
                return new Dimension(1, 1);
        }

        /**
         * Notwendig für den LayoutManager. Wird vom Container aufgerufen, sobald die Große des
         * Containers verändert wird oder durch die Methode 'pack' des Containers.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public Dimension maximumLayoutSize(Container parent) {
                return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        /**
         * Notwendig für den LayoutManager. Wird vom Container aufgerufen, sobald die Große des
         * Containers verändert wird oder durch die Methode 'pack' des Containers.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public Dimension preferredLayoutSize(Container parent) {
                Insets in = parent.getInsets();
                int xs,ys,xe,ye;
                xs = ys = xe = ye = 0;
                for (Component co : parent.getComponents()) {
                        xs = Math.min(xs, co.getX());
                        ys = Math.min(ys, co.getY());
                        xe = Math.max(xe, co.getX() + co.getWidth());
                        ye = Math.max(ye, co.getY() + co.getHeight());
                }
                return new Dimension(xe-xs + in.left+in.right, ye-ys+in.top+in.bottom);
        }

        /**
         * Notwendig für den LayoutManager. Wird vom Container aufgerufen, sobald die Große des
         * Containers verändert wird oder durch die Methode 'pack' des Containers.
         *
         * @param
         *
         * @return
         *
         * @exception
         *
         * @see        LayoutManager2
         */
        public void layoutContainer(Container parent) {
        }
}
