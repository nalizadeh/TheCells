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

package org.nalizadeh.designer;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2007 N.A.J</p>
 *
 * <p>Organization:</p>
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class ResourceManager {

        public static URL getResource(String uri) {
                URL url = ResourceManager.class.getResource(uri);
                if (url == null) {
                        url = ClassLoader.getSystemResource(uri);
                }

                // if we still couldn't find the resource, then slash it and try again
                if (url == null && !uri.startsWith("/")) {
                        url = getResource("/" + uri);
                }

                return url;
        }

        public static Image createImage(String url) {
                try {
                        URL location = getResource(url);
                        return Toolkit.getDefaultToolkit().createImage(location);
                } catch (NullPointerException e) {
//                        throw new NullPointerException("Unable to locate image: " + url);
                }
                return null;
        }

        public static ImageIcon createIcon(String url) {
                try {
                        URL location = getResource(url);
                        return new ImageIcon(location);
                } catch (NullPointerException e) {
//                        throw new NullPointerException("Unable to locate image: " + url);
                }
                return null;
        }

        public static Cursor createCursor(String url, Point hotPoint, String name) {
                Image image = createImage(url);
                Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(image, hotPoint, name);
                return c;
        }
}
