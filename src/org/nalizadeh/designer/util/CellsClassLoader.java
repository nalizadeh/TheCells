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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
public class CellsClassLoader extends ClassLoader {

	private String classpath;

	public CellsClassLoader(String classpath) {
		this.classpath = classpath;
	}

	public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

		Class<?> clas = findLoadedClass(name);
		if (clas != null) {
			return clas;
		}

		String fileStub = parseClassName(name);
		String javaFilename = classpath + CellsUtil.FILE_SEPARATOR + fileStub + ".java";
		String classFilename = classpath + CellsUtil.FILE_SEPARATOR + fileStub + ".class";
		File javaFile = new File(javaFilename);
		File classFile = new File(classFilename);

		boolean compileLoading = false;

		if (javaFile.exists() && !classFile.exists()) {
			try {
				if (!compile(javaFilename)) {
					throw new ClassNotFoundException("Compile failed: " + javaFilename);
				}
			} catch (IOException ie) {
				throw new ClassNotFoundException(ie.toString());
			}
		}

		if (classFile.exists()) {
			try {
				byte[] raw = getBytes(classFile);
				clas = defineClass(null, raw, 0, raw.length);
				compileLoading = true;

			} catch (IOException ie) {
				// do nothing here
			}
		}

		if (clas == null) {

			// now replay to the super loader
			clas = findSystemClass(name);

			if (clas == null) {
				throw new ClassNotFoundException(name);
			}
		}

		// we link only not compiled classes. The designer classes
		// should be always compiled and loaded (no caching)
		if (clas != null && resolve && !compileLoading) {
			resolveClass(clas);
		}
		return clas;
	}

	private String parseClassName(String name) {
		int i = name.lastIndexOf(".");
		if (i != -1) {
			name = name.substring(i + 1, name.length());
		}
		return name;
	}

	private byte[] getBytes(File file) throws IOException {
		byte[] raw = new byte[(int) file.length()];
		FileInputStream fin = new FileInputStream(file);
		int r = fin.read(raw);
		if (r != file.length()) {
			fin.close();
			throw new IOException("Can't read the file");
		}
		fin.close();
		return raw;
	}

	private boolean compile(String javaFile) throws IOException {
		String javac =
			CellsUtil.JAVA_HOME //
			+ CellsUtil.FILE_SEPARATOR + "bin" //
			+ CellsUtil.FILE_SEPARATOR + "javac -cp " //
			+ CellsUtil.CLASSPATH + " " + CellsUtil.formatFilename(javaFile);

		Process p = Runtime.getRuntime().exec(javac);
		try {
			p.waitFor();
		} catch (InterruptedException ie) {
			System.out.println(ie);
		}

		return p.exitValue() == 0;
	}
}
