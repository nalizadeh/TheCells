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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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
public class CellsSwingWorker extends Thread implements ActionListener {

	public enum States {
		STARTING, WORKING, BREAKING, STOPPING
	}

	private Invoker invoker;
	private Worker worker;
	private int handle;
	private Timer timer;
	private int steps;
	private int counter;

	private Object result;
	private boolean aborted;

	public CellsSwingWorker(Invoker invoker, int handle) {
		this(invoker, handle, -1, -1);
	}

	public CellsSwingWorker(Invoker invoker, int handle, int steps, int delay) {

		this.invoker = invoker;
		this.handle = handle;
		this.steps = steps;

		if (steps > 0) {
			this.timer = new Timer(delay, this);
			this.timer.setInitialDelay(0);
		}
	}

	public void startExecuting() {
		start();
	}

	public void stopExecuting() {
		worker.interrupt();
	}

	public void breakExecuting() {
	}

	public void run() {

		counter = 0;
		worker = invoker.worker(handle);
		invoker.report(handle, States.STARTING);
		worker.start();

		if (timer != null) {
			timer.start();
		}

		while (worker.isAlive()) {
			try {
				worker.join(500);
				invoker.report(handle, States.WORKING);

			} catch (InterruptedException ex) {
				break;
			}
		}

		if (timer != null) {
			timer.stop();
		}

		if (worker.interrupted()) {
			aborted = true;
			invoker.report(handle, States.BREAKING);
		} else {
			result = worker.result();
			invoker.report(handle, States.STOPPING);
		}

		try {

			// invokeAndWait or invokeLater should be done by
			// worker and invoker, but to be safe we do it also here
			SwingUtilities.invokeAndWait(
				new Runnable() {
					public void run() {
						worker.terminate(aborted);
						invoker.terminate(handle, result, aborted);
					}
				}
			);
		} catch (Exception ex) {
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (counter > steps) {
			worker.interrupt();
		} else {
			counter++;
		}
	}

	public static interface Invoker {

		public Worker worker(int handle);

		public void report(int handle, States state);

		public void terminate(int handle, Object result, boolean aborted);
	}

	public abstract static class Worker extends Thread {

		public abstract Object result();

		public abstract void terminate(boolean aborted);
	}

	// T E S T ================================

	static JProgressBar bar = new JProgressBar(0, 10);
	static JLabel lab = new JLabel(" ");
	static int n = 0;

	static class TestInvoker implements CellsSwingWorker.Invoker {
		public CellsSwingWorker.Worker worker(int handle) {
			return new CellsSwingWorker.Worker() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(1000);
							System.out.println("======>Thread B : " + n++);
							SwingUtilities.invokeLater(
								new Runnable() {
									public void run() {
										bar.setValue(n);
									}
								}
							);
						} catch (Exception e) {
							break;
						}
					}
				}

				public Object result() {
					return null;
				}

				public void terminate(boolean aborted) {
				}
			};
		}

		public void terminate(int handle, Object result, boolean aborted) {
		}

		public void report(int handle, CellsSwingWorker.States s) {
			switch (s) {

				case STARTING :
					lab.setText("Starting...");
					break;

				case WORKING :
					lab.setText("Working...");
					break;

				case BREAKING :
					lab.setText("Breaked...");
					break;

				case STOPPING :
					lab.setText("Stopping...");
					break;
			}
		}
	}

	public static void main(String[] args) {

		final CellsSwingWorker sw = new CellsSwingWorker(new TestInvoker(), 1, 10, 1000);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton but1 = new JButton("Start");
		JButton but2 = new JButton("Break");
		JButton but3 = new JButton("Stop");

		but1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sw.startExecuting();
				}
			}
		);

		but2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sw.breakExecuting();
				}
			}
		);

		but3.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sw.stopExecuting();
				}
			}
		);

		bar.setStringPainted(true);

		JPanel pan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pan.add(but1);
		pan.add(but2);
		pan.add(but3);

		f.add(lab, BorderLayout.NORTH);
		f.add(bar, BorderLayout.CENTER);
		f.add(pan, BorderLayout.SOUTH);

		f.pack();
		f.setVisible(true);
	}
}
