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

import org.nalizadeh.designer.DebugPanel;

import java.io.PrintStream;

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
public class CellsExceptionWrapper extends PrintStream {

	private static PrintStream oldStdOut;
	private static PrintStream oldStdErr;
	private static CellsExceptionWrapper stdErrCapture;
	private static CellsExceptionWrapper stdOutCapture;

	private static boolean active;

	private static boolean writeStdOut = false;
	private static DebugPanel debuger = null;

	private CellsExceptionWrapper(PrintStream ps) {
		super(ps);
	}

	/**
	 * Starts the capturing
	 */
	public static synchronized void start(DebugPanel deb, boolean writeStdoutFlag) {
		try {
			if (!active && stdOutCapture == null && stdErrCapture == null) {

				oldStdOut = System.out;
				oldStdErr = System.err;
				stdOutCapture = new CellsExceptionWrapper(System.out);
				stdErrCapture = new CellsExceptionWrapper(System.err);

				System.setOut(stdOutCapture);
				System.setErr(stdErrCapture);
				active = true;
				writeStdOut = writeStdoutFlag;
				debuger = deb;
			}
		} catch (Exception e) {
			e.printStackTrace(oldStdErr);
			stop();
		}
	}

	/**
	 * Restores the original settings.
	 */
	public static synchronized void stop() {
                try {
                        if (oldStdOut != null) {
                                System.setOut(oldStdOut);
                                oldStdOut = null;
                                if (stdOutCapture != null) {
                                        stdOutCapture.close();
                                        stdOutCapture = null;
                                }
                        }
                        if (oldStdErr != null) {
                                System.setErr(oldStdErr);
                                oldStdErr = null;
                                if (stdErrCapture != null) {
                                        stdErrCapture.close();
                                        stdErrCapture = null;
                                }
                        }
                } catch (Exception ex) {

                }
		active = false;
	}

	public static CellsExceptionWrapper getStdOutCapture() {
		return stdOutCapture;
	}

	public static CellsExceptionWrapper getStdErrCapture() {
		return stdErrCapture;
	}

	public static boolean isActive() {
		return active;
	}

	public static void setActive(boolean active, DebugPanel deb, boolean writeStdoutFlag) {
		if (active) {
			start(deb, writeStdoutFlag);
		} else {
			stop();
		}
	}

	// PrintStream override.
	public void write(int b) {
		try {

			// here goes whatever custom code you need for capturing
			// Let's write to the standard stream, too
			if (writeStdOut) {
				super.write(b);
			}
		} catch (Exception e) {

			// Oops something's wrong
			super.write(b);
			e.printStackTrace(oldStdErr);
			setError();
		}
		debuger.appendError((new Integer(b)).toString());
	}

	// PrintStream override.
	public void write(byte[] buf, int off, int len) {
		try {

			// here goes whatever custom code you need for capturing
			// Let's write to the standard stream, too
			if (writeStdOut) {
				super.write(buf, off, len);
			}
		} catch (Exception e) {

			// Oops something's wrong
			super.write(buf, off, len);
			e.printStackTrace(oldStdErr);
			setError();
		}
		debuger.appendError((new String(buf, off, len)));
	}
}
