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

package org.nalizadeh.designer.html;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.nalizadeh.designer.CellsDesigner;
import org.nalizadeh.designer.util.CellsUtil;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007 N.A.J
 * </p>
 * 
 * <p>
 * Organization:
 * </p>
 * 
 * @author Nader Alizadeh
 * @version 1.0
 */
public class HtmlViewer {

	private CellsDesigner designer;
	private HtmlGenerator generator;

	public HtmlViewer(CellsDesigner designer) {

		this.designer = designer;
		this.generator = new HtmlGenerator();
	}

	public void show() {
		try {
			
			Dimension d = designer.getDesktop().getDesigner().getContainer().getSize();
			
			StringBuffer html = generator.generateHTML(designer.getDesktop()
					.getDesigner().getContainer());

			File tmp = new File(CellsUtil.TEMP_HOME);
			if (!tmp.exists()) {
				tmp.mkdirs();
			}

			String tempFilename = tmp.getAbsolutePath() + File.separator + "temp.html";
			File f = new File(tempFilename);
			if (f.exists()) {
				f.delete();
			}

			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			out.write(html.toString());
			out.close();
			
			f = new File(tmp.getAbsolutePath() + File.separator + "cells.css");
			Path src = Paths.get(getClass().getResource("scripts" + File.separator + "cells.css").toURI());
			Path des = Paths.get(f.toURI());
				
			Files.copy(src, des, StandardCopyOption.REPLACE_EXISTING);
			
			f = new File(tmp.getAbsolutePath() + File.separator + "cells.js");
			src = Paths.get(getClass().getResource("scripts" + File.separator + "cells.js").toURI());
			des = Paths.get(f.toURI());

			Files.copy(src, des, StandardCopyOption.REPLACE_EXISTING);
			
			// javascript does not work with loadContain
			
//			HtmlViewer.showHtml(null, html.toString(), false);
			HtmlBrowser.showHtml("file:/" + tempFilename, null, false, d.width + 50, d.height + 150);
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

}
