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

import org.nalizadeh.designer.CellsDesigner;
import org.nalizadeh.designer.DebugPanel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
public class CellsSourceCompiler extends CellsSwingWorker.Worker {

	private CellsDesigner designer;
	private volatile File tmp;
	private File src;
	private Object instance;

	private CompilerThread thread;

	public CellsSourceCompiler(CellsDesigner designer, File src) {
		this.designer = designer;
		this.src = src;
	}

	public void run() {

		try {
			synchronized (this) {
				tmp = new File(CellsUtil.TEMP_HOME);
				if (!tmp.exists()) {
					tmp.mkdirs();
				}
			}

			File f = new File(tmp.getAbsolutePath() + File.separator + src.getName());

			BufferedReader in = new BufferedReader(new FileReader(src));
			BufferedWriter out = new BufferedWriter(new FileWriter(f));

			while (true) {
				String line = in.readLine();
				if (line == null) {
					break;
				}
				out.write(line.replace("private", "public"));
				if (!line.endsWith(CellsUtil.LINE_SEPARATOR)) {
					out.write(CellsUtil.LINE_SEPARATOR);
				}
			}
			in.close();
			out.close();

			thread = new CompilerThread(f);
			thread.start();

			while (thread.isAlive()) {

				// let maximum of 500 millisecond for Compiler thread to work.
				thread.join(500);
			}

			instance = thread.instance;

		} catch (Exception ex) {
		}
	}

	public void interrupt() {
		thread.interrupt();
	}

	public Object result() {
		return instance;
	}

	public void terminate(boolean aborted) {
		clear();
	}

	public synchronized void clear() {
		if (tmp != null) {
			File[] files = tmp.listFiles();
			if (files != null) {
				for (File f : files) {
					f.delete();
				}
			}
			tmp.delete();
		}
	}

	private static class CompilerThread extends Thread {

		private File sourceFile;
		private Process proc;
		private Object instance;

		public CompilerThread(File sourceFile) {
			this.sourceFile = sourceFile;
		}

		public void run() {
			try {
				String javac =
						CellsUtil.JAVA_HOME //
						+ CellsUtil.FILE_SEPARATOR + "bin" //
						+ CellsUtil.FILE_SEPARATOR + "javac";
				
				String arg1 = "-cp";
				String arg2 = CellsUtil.CLASSPATH;
				String arg3 = CellsUtil.formatFilename(sourceFile.getAbsolutePath());

				DebugPanel.appendMessage("JAVA COMMAND: " + javac + " -cp " + arg1 + " " + arg2);
				
				Runtime rtm = Runtime.getRuntime();
				
				proc = rtm.exec(new String[] {javac, arg1, arg2, arg3});
				proc.waitFor();
				
				if (proc.exitValue() == 0) {
					String s = CellsUtil.formatFilename(sourceFile.getName());
					s = s.substring(0, s.length() - 5);
					Class<?> cl = new CellsClassLoader(CellsUtil.TEMP_HOME).loadClass(s, true);
					instance = cl.newInstance();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public int exitValue() {
			return proc.exitValue();
		}

		public void interrupt() {
			if (proc != null) {
				proc.destroy();
			}
			super.interrupt();
		}
	}
}
