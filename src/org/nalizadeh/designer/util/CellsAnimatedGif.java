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

import javax.swing.JLabel;
import java.awt.Image;
import java.awt.image.ImageObserver;
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
public class CellsAnimatedGif extends JLabel implements ImageObserver {

        private ImageIcon img;

        public CellsAnimatedGif(ImageIcon img) {
                this.img = img;
                this.img.setImageObserver(this);
        }

        public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h) {
                if ((flags & (FRAMEBITS | ALLBITS)) != 0) {
                        repaint();
                }
                return (flags & (ALLBITS | ABORT)) == 0;
        }

        public void start() {
                setIcon(img);
                repaint();
        }

        public void stop() {
                setIcon(null);
                repaint();
        }
}

