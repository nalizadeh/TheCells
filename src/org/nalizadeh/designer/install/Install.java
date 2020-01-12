// Copyright (c) 2007 N.A.J
//
// All Rights Reserved. Permission to  use, copy, modify, and  distribute
// this software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above  copyright notice
// and this  paragraph appear in all  copies.  The software  program  and
// documentation  are supplied "AS IS", without any accompanying services
// from the author. The author does not warrant that the operation of the
// program will be uninterrupted or error-free. The end-user  understands
// that the program  was developed  for research purposes  and is advised
// not to rely exclusively on the program for any reason.

package org.nalizadeh.designer.install;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class Install extends JFrame {

	JButton bNext;
	JButton bPrev;
	JButton bInst;
	JButton bCancel;
	JPanel imgPanel;
	JPanel dialogPanel;
	JPanel buttPanel1, buttPanel2;

	JPanel panel = null;
	Panel1 panel1 = null;
	Panel2 panel2 = null;
	Panel3 panel3 = null;
	Panel4 panel4 = null;
	Panel5 panel5 = null;
	Panel6 panel6 = null;

    static final String LINE_SEPARATOR = System.getProperty("line.separator");
   	static final String FILE_SEPARATOR = "/"; // File.separator;
   
	static final String location = "TheCells";
	static final String zipFile = "TheCells.zip";
	static final String installFile = "install.jar";
	static final String resDir = "org.nalizadeh.designer/install/images/";
	static final String propertyFile = "TheCells.properties";
	static final String beansFile = "TheBeans.properties";
	static final String batFile = "TheCells.cmd";
	static final String shFile = "TheCells.sh";
	
	String home = "TheCells";
	String javahome = "C:/Java/jdk1.6.0_02";
	String classpath = "C:/Java/jre1.5.0_11";

	int pageNum = -1;

	//Construct the frame
	public Install() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Component initialization
	private void jbInit() throws Exception {

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		double x = (screen.getWidth() - 660) / 2;
		double y = (screen.getHeight() - 400) / 2;

		this.getContentPane().setLayout(new BorderLayout());
		this.setTitle("The Cells Installation Wizard");
		this.setSize(new Dimension(660, 400));
		this.setLocation((int) x, (int) y);
		this.setResizable(false);

		JPanel p1 = new JPanel(new BorderLayout());
		JPanel p2 = new JPanel(new BorderLayout());
		JPanel p3 = new JPanel(new BorderLayout());

		this.getContentPane().add(p1, BorderLayout.NORTH);
		this.getContentPane().add(p2, BorderLayout.CENTER);
		this.getContentPane().add(p3, BorderLayout.SOUTH);

		JLabel l1 = new JLabel(" ");
		l1.setFont(new Font("Dialog", 1, 16));
		l1.setForeground(Color.black);
		p1.add(l1, BorderLayout.WEST);

		// Buttons

		p3.setBorder(new TitledBorder(""));
		bCancel = new JButton("Cancel");
		bCancel.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeWin();
				}
			}
		);

		bNext = new JButton("Next");
		bNext.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changePage(true);
				}
			}
		);

		bPrev = new JButton("Back");
		bPrev.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changePage(false);
				}
			}
		);

		bInst = new JButton("Install");
		bInst.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					changePage(true);
				}
			}
		);

		buttPanel1 = new JPanel();
		buttPanel2 = new JPanel();
		buttPanel1.add(bCancel);
		buttPanel2.add(bPrev);
		buttPanel2.add(bNext);

		p3.add(buttPanel1, BorderLayout.WEST);
		p3.add(new JLabel(" "), BorderLayout.CENTER);
		p3.add(buttPanel2, BorderLayout.EAST);

		// Dialog

		imgPanel = new JPanel();
		dialogPanel = new JPanel(new BorderLayout());

		changePage(true);

		p2.add(imgPanel, BorderLayout.WEST);
		p2.add(dialogPanel, BorderLayout.CENTER);
	}

	private void changePage(boolean next) {

		if (panel != null) {
			((WizardPanel) panel).leave();
		}

		int lastPage = pageNum;

		if (next && pageNum < 5) {
			pageNum++;
		} else if (!next && pageNum > 0) {
			pageNum--;
		} else {
			return;
		}

		String img = "install.jpg";
		String title = "";
		switch (pageNum) {

			case 0 :
				title = " Welcome";
				if (panel1 == null) {
					panel1 = new Panel1(this);
				}
				panel = panel1;
				break;

			case 1 :
				title = " License Agreement";
				if (panel2 == null) {
					panel2 = new Panel2(this);
				}
				panel = panel2;
				break;

			case 2 :
				title = " Destination Folder";
				if (panel3 == null) {
					panel3 = new Panel3(this);
				}
				panel = panel3;
				break;

			case 3 :
				title = " JDK settings";
				if (next) {
					home = ((Panel3) panel).getDir();
				}
				if (panel4 == null) {
					panel4 = new Panel4(this);
				}
				if (lastPage == 4) {
					enableInst(false);
				}
				panel = panel4;
				break;

			case 4 :
				title = " Start Installation";
				if (next) {
					javahome = ((Panel4) panel).getJavaHome();
					classpath = ((Panel4) panel).getClassPath();
				}
				if (panel5 == null) {
					panel5 = new Panel5(this);
				}
				panel = panel5;
				break;

			case 5 :
				title = " Installing";
				if (panel6 == null) {
					panel6 = new Panel6(this);
				}
				panel = panel6;
				break;
		}

		((WizardPanel) panel).enter();

		setPage(img, title, panel);

		if (pageNum == 5) {
			doInstall();
		}
	}

	private void setPage(String img, String tit, JPanel panel) {
		imgPanel.removeAll();
		dialogPanel.removeAll();

		imgPanel.add(new JLabel(getImageIcon(img)));
		dialogPanel.add(new TitlePanel(tit), BorderLayout.NORTH);
		dialogPanel.add(panel, BorderLayout.CENTER);
		invalidate();
		validate();
		repaint();
	}

	void enableNext(boolean v) {
		bNext.setEnabled(v);
	}

	void enablePrev(boolean v) {
		bPrev.setEnabled(v);
	}

	void enableInst(boolean v) {
		if (v) {
			buttPanel2.remove(bNext);
			buttPanel2.add(bInst);
		} else {
			buttPanel2.remove(bInst);
			buttPanel2.add(bNext);
		}
		invalidate();
		validate();
	}

	private void doInstall() {
		Thread t =
			new Thread("") {
				public void run() {
					try {
						panel6.extractJarFile(new File(zipFile), new File(home));
						saveProperties(home, javahome, classpath);
						finished();
					} catch (Exception e) {
					}
				}
			};
		t.start();
	}

	void finished() {
		buttPanel1.removeAll();
		buttPanel2.removeAll();
		JButton b = new JButton("Close");
		b.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeWin();
				}
			}
		);
		buttPanel2.add(b);

		dialogPanel.removeAll();
		JLabel l = new JLabel("                        Installation completed.");
		l.setFont(new Font("Arial", 1, 14));
		dialogPanel.add(l);
		invalidate();
		validate();
	}

	//Overridden so we can exit on System Close
	protected void processWindowEvent(WindowEvent e) {
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			closeWin();
		}
	}

	private void closeWin() {
		System.exit(0);
	}

	private ImageIcon getImageIcon(String filename) {
		String name = resDir + filename;
		File f = new File(name);
		if (f.exists()) {
			return new ImageIcon(name);
		}

		JarResources js = new JarResources(installFile);
		return js.getImage(name);
	}

	private boolean saveProperties(String home, String javahome, String classpath) {

		try {
			StringBuffer sb1 = new StringBuffer(home);
//			for (int i = 0; i < home.length(); i++) {
//				sb1.append(home.charAt(i));
//				if (i < home.length() - 1 && home.charAt(i) == '\\' && home.charAt(i + 1) != '\\') {
//					sb1.append('/');
//				}
//				if (i < home.length() - 1 && home.charAt(i) == '/' && home.charAt(i + 1) != '/') {
//					sb1.append('/');
//				}
//			}

			StringBuffer sb2 = new StringBuffer(javahome);
//			for (int i = 0; i < javahome.length(); i++) {
//				sb2.append(javahome.charAt(i));
//				if (
//					i < javahome.length() - 1
//					&& javahome.charAt(i) == '\\'
//					&& javahome.charAt(i + 1) != '\\'
//				) {
//					sb2.append('/');
//				}
//				if (
//					i < javahome.length() - 1
//					&& javahome.charAt(i) == '/'
//					&& javahome.charAt(i + 1) != '/'
//				) {
//					sb2.append('/');
//				}
//			}

			StringBuffer sb3 = new StringBuffer(classpath);
//			for (int i = 0; i < classpath.length(); i++) {
//				sb3.append(classpath.charAt(i));
//				if (
//					i < classpath.length() - 1
//					&& classpath.charAt(i) == '\\'
//					&& classpath.charAt(i + 1) != '\\'
//				) {
//					sb3.append('/');
//				}
//				if (
//					i < classpath.length() - 1
//					&& classpath.charAt(i) == '/'
//					&& classpath.charAt(i + 1) != '/'
//				) {
//					sb3.append('/');
//				}
//			}

			File f = new File(home + File.separator + propertyFile);

			if (f.exists()) {
				f.delete();
			}
			if (f.createNewFile()) {

				List<String> wb = new ArrayList<String>();
				wb.add("# The working environment of The Cells");
				wb.add("THE_CELLS_HOME=" + sb1.toString());
				wb.add(Install.LINE_SEPARATOR);
				wb.add("# The JDK installation directory");
				wb.add("JAVA_HOME=" + sb2.toString());
				wb.add(Install.LINE_SEPARATOR);
				wb.add("# The Classpath for javac");
				wb.add("CLASSPATH=" + sb3.toString());
				wb.add(Install.LINE_SEPARATOR);
				writeToFile(f, wb);
			}
			
//			f = new File(home + File.separator + beansFile);
//
//			if (f.exists()) {
//				f.delete();
//			}
//			if (f.createNewFile()) {
//				List<String> wb = new ArrayList<String>();
//				wb.add("# put all your Java Beans in this file in following format:");
//				wb.add("# Bean class name,Bean image file");
//				wb.add("# for example:");
//				wb.add("# org.nalizadeh.designer.util.beans.JCalendar,c:/project/original/images/calendar.gif");
//				wb.add(Install.LINE_SEPARATOR);
//				writeToFile(f, wb);
//			}

			File f2 = new File(home + File.separator + batFile);

			if (f2.exists()) {
				f2.delete();
			}
			if (f2.createNewFile()) {

				List<String> wb = new ArrayList<String>();
				wb.add("start javaw -jar TheCells.jar");
				wb.add(Install.LINE_SEPARATOR);
				writeToFile(f2, wb);
			}
			
			File f3 = new File(home + File.separator + shFile);

			if (f3.exists()) {
				f3.delete();
			}
			if (f3.createNewFile()) {

				List<String> wb = new ArrayList<String>();
				wb.add("#!/bin/sh");
				wb.add("java -jar TheCells.jar");
				wb.add(Install.LINE_SEPARATOR);
				writeToFile(f3, wb);
				return true;
			}
		} catch (IOException ex) {
		}
		return false;
	}

	private void writeToFile(File file, List<String> buf) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (String s : buf) {
			bw.write(s);
			bw.newLine();
		}
		bw.close();
	}

	private void readFromFile(File file, List<String> buf) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			buf.add(line);
		}
		br.close();
	}

    public static String formatFilename(String name) {
        return name.replace("\\", FILE_SEPARATOR);
    }
    
	//Main method
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		Install frame = new Install();
		frame.setVisible(true);
	}
}


class TitlePanel extends JPanel {
	public TitlePanel(String title) {
		setLayout(new BorderLayout());
		setBackground(new Color(100, 100, 155));
		JLabel l = new JLabel(title);
		l.setFont(new java.awt.Font("Dialog", 1, 14));
		l.setForeground(Color.white);
		add(l, BorderLayout.WEST);
	}
}


interface WizardPanel {
	void enter();

	void leave();
}


class Panel1 extends JPanel implements WizardPanel {
	Install parent;

	public Panel1(Install f) {
		String newline = Install.LINE_SEPARATOR;

		parent = f;
		setLayout(new BorderLayout());
		JTextArea tArea = new JTextArea();
		tArea.setText(
			newline + newline + "  Welcome to The Cells Installation Wizard." + newline + newline
			+ "  The Installation Wizard will install The Cells on your computer." + newline
			+ "  To continue, click Next." + newline + newline + newline + newline + "  The Cells"
			+ newline + "  http://www.nalizadeh.org" + newline + "  Copyright (C) 2007 N.A.J"
		);

		tArea.setLineWrap(true);
		tArea.setFont(new java.awt.Font("Dialog", 0, 12));
		tArea.setEditable(false);
		tArea.setBackground(Color.lightGray);

		JScrollPane sp = new JScrollPane(tArea);
		sp.setBorder(null);
		add(sp, BorderLayout.CENTER);
	}

	public void enter() {
		parent.enableNext(true);
		parent.enablePrev(false);
	}

	public void leave() {

	}
}


class Panel2 extends JPanel implements WizardPanel {
	Install parent;
	JRadioButton r1;
	JRadioButton r2;

	public Panel2(Install f) {
		parent = f;
		setLayout(null);
		JLabel l = new JLabel("  Please read the following license agreement carefully.");
		JTextArea t = new JTextArea();
		t.setText(
			"                    GNU LESSER GENERAL PUBLIC LICENSE\n" //
			+ "                              Version 3, 29 June 2007\n" //
			+ "\n" //
			+ " Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>\n" //
			+ " Everyone is permitted to copy and distribute verbatim copies\n" //
			+ " of this license document, but changing it is not allowed.\n" + "\n" //
			+ "\n" //
			+ "  This version of the GNU Lesser General Public License incorporates\n" //
			+ "the terms and conditions of version 3 of the GNU General Public\n" //
			+ "License, supplemented by the additional permissions listed below.\n" //
			+ "\n" //
			+ "  0. Additional Definitions. \n" //
			+ "\n" //
			+ "  As used herein, \"this License\" refers to version 3 of the GNU Lesser\n" //
			+ "General Public License, and the \"GNU GPL\" refers to version 3 of the GNU\n" //
			+ "General Public License.\n" + "\n" //
			+ "  \"The Library\" refers to a covered work governed by this License,\n" //
			+ "other than an Application or a Combined Work as defined below.\n" //
			+ "\n" //
			+ "  An \"Application\" is any work that makes use of an interface provided\n" //
			+ "by the Library, but which is not otherwise based on the Library.\n" //
			+ "Defining a subclass of a class defined by the Library is deemed a mode\n" //
			+ "of using an interface provided by the Library.\n" //
			+ "\n" //
			+ "  A \"Combined Work\" is a work produced by combining or linking an\n" //
			+ "Application with the Library.  The particular version of the Library\n" //
			+ "with which the Combined Work was made is also called the \"Linked\n" //
			+ "Version\".\n" //
			+ "\n" //
			+ "  The \"Minimal Corresponding Source\" for a Combined Work means the\n" //
			+ "Corresponding Source for the Combined Work, excluding any source code\n" //
			+ "for portions of the Combined Work that, considered in isolation, are\n" //
			+ "based on the Application, and not on the Linked Version.\n" //
			+ "\n" //
			+ "  The \"Corresponding Application Code\" for a Combined Work means the\n" //
			+ "object code and/or source code for the Application, including any data\n" //
			+ "and utility programs needed for reproducing the Combined Work from the\n" //
			+ "Application, but excluding the System Libraries of the Combined Work.\n" //
			+ "\n" //
			+ "  1. Exception to Section 3 of the GNU GPL.\n" //
			+ "\n" //
			+ "  You may convey a covered work under sections 3 and 4 of this License\n" //
			+ "without being bound by section 3 of the GNU GPL.\n" //
			+ "\n" //
			+ "  2. Conveying Modified Versions.\n" //
			+ "\n" //
			+ "  If you modify a copy of the Library, and, in your modifications, a\n" //
			+ "facility refers to a function or data to be supplied by an Application\n" //
			+ "that uses the facility (other than as an argument passed when the\n" //
			+ "facility is invoked), then you may convey a copy of the modified\n" //
			+ "version:\n" //
			+ "\n" //
			+ "   a) under this License, provided that you make a good faith effort to\n" //
			+ "   ensure that, in the event an Application does not supply the\n" //
			+ "   function or data, the facility still operates, and performs\n" //
			+ "   whatever part of its purpose remains meaningful, or\n" //
			+ "\n" //
			+ "   b) under the GNU GPL, with none of the additional permissions of\n" //
			+ "   this License applicable to that copy.\n" //
			+ "\n" //
			+ "  3. Object Code Incorporating Material from Library Header Files.\n" //
			+ "\n" //
			+ "  The object code form of an Application may incorporate material from\n" //
			+ "a header file that is part of the Library.  You may convey such object\n" //
			+ "code under terms of your choice, provided that, if the incorporated\n" //
			+ "material is not limited to numerical parameters, data structure\n" //
			+ "layouts and accessors, or small macros, inline functions and templates\n" //
			+ "(ten or fewer lines in length), you do both of the following:\n" //
			+ "\n" //
			+ "   a) Give prominent notice with each copy of the object code that the\n" //
			+ "   Library is used in it and that the Library and its use are\n" //
			+ "   covered by this License.\n" //
			+ "\n" //
			+ "   b) Accompany the object code with a copy of the GNU GPL and this license\n" //
			+ "   document.\n" + "\n" + "  4. Combined Works.\n" //
			+ "\n" //
			+ "  You may convey a Combined Work under terms of your choice that,\n" //
			+ "taken together, effectively do not restrict modification of the\n" //
			+ "portions of the Library contained in the Combined Work and reverse\n" //
			+ "engineering for debugging such modifications, if you also do each of\n" //
			+ "the following:\n" //
			+ "\n" //
			+ "   a) Give prominent notice with each copy of the Combined Work that\n" //
			+ "   the Library is used in it and that the Library and its use are\n" //
			+ "   covered by this License.\n" //
			+ "\n" //
			+ "   b) Accompany the Combined Work with a copy of the GNU GPL and this license\n" //
			+ "   document.\n" //
			+ "\n" //
			+ "   c) For a Combined Work that displays copyright notices during\n" //
			+ "   execution, include the copyright notice for the Library among\n" //
			+ "   these notices, as well as a reference directing the user to the\n" //
			+ "   copies of the GNU GPL and this license document.\n" //
			+ "\n" //
			+ "   d) Do one of the following:\n" //
			+ "\n" //
			+ "       0) Convey the Minimal Corresponding Source under the terms of this\n" //
			+ "       License, and the Corresponding Application Code in a form\n" //
			+ "       suitable for, and under terms that permit, the user to\n" //
			+ "       recombine or relink the Application with a modified version of\n" //
			+ "       the Linked Version to produce a modified Combined Work, in the\n" //
			+ "       manner specified by section 6 of the GNU GPL for conveying\n" //
			+ "       Corresponding Source.\n" //
			+ "\n" //
			+ "       1) Use a suitable shared library mechanism for linking with the\n" //
			+ "       Library.  A suitable mechanism is one that (a) uses at run time\n" //
			+ "       a copy of the Library already present on the user's computer\n" //
			+ "       system, and (b) will operate properly with a modified version\n" //
			+ "       of the Library that is interface-compatible with the Linked\n" //
			+ "       Version.\n" //
			+ "\n" //
			+ "   e) Provide Installation Information, but only if you would otherwise\n" //
			+ "   be required to provide such information under section 6 of the\n" //
			+ "   GNU GPL, and only to the extent that such information is\n" //
			+ "   necessary to install and execute a modified version of the\n" //
			+ "   Combined Work produced by recombining or relinking the\n" //
			+ "   Application with a modified version of the Linked Version. (If\n" //
			+ "   you use option 4d0, the Installation Information must accompany\n" //
			+ "   the Minimal Corresponding Source and Corresponding Application\n" //
			+ "   Code. If you use option 4d1, you must provide the Installation\n" //
			+ "   Information in the manner specified by section 6 of the GNU GPL\n" //
			+ "   for conveying Corresponding Source.)\n" //
			+ "\n" //
			+ "  5. Combined Libraries.\n" //
			+ "\n" //
			+ "  You may place library facilities that are a work based on the\n" //
			+ "Library side by side in a single library together with other library\n" //
			+ "facilities that are not Applications and are not covered by this\n" //
			+ "License, and convey such a combined library under terms of your\n" //
			+ "choice, if you do both of the following:\n" //
			+ "\n" //
			+ "   a) Accompany the combined library with a copy of the same work based\n" //
			+ "   on the Library, uncombined with any other library facilities,\n" //
			+ "   conveyed under the terms of this License.\n" //
			+ "\n" //
			+ "   b) Give prominent notice with the combined library that part of it\n" //
			+ "   is a work based on the Library, and explaining where to find the\n" //
			+ "   accompanying uncombined form of the same work.\n" //
			+ "\n" //
			+ "  6. Revised Versions of the GNU Lesser General Public License.\n" //
			+ "\n" //
			+ "  The Free Software Foundation may publish revised and/or new versions\n" //
			+ "of the GNU Lesser General Public License from time to time. Such new\n" //
			+ "versions will be similar in spirit to the present version, but may\n" //
			+ "differ in detail to address new problems or concerns.\n" //
			+ "\n" //
			+ "  Each version is given a distinguishing version number. If the\n" //
			+ "Library as you received it specifies that a certain numbered version\n" //
			+ "of the GNU Lesser General Public License \"or any later version\"\n" //
			+ "applies to it, you have the option of following the terms and\n" //
			+ "conditions either of that published version or of any later version\n" //
			+ "published by the Free Software Foundation. If the Library as you\n" //
			+ "received it does not specify a version number of the GNU Lesser\n" //
			+ "General Public License, you may choose any version of the GNU Lesser\n" //
			+ "General Public License ever published by the Free Software Foundation.\n" //
			+ "\n" //
			+ "  If the Library as you received it specifies that a proxy can decide\n" //
			+ "whether future versions of the GNU Lesser General Public License shall\n" //
			+ "apply, that proxy's public statement of acceptance of any version is\n" //
			+ "permanent authorization for you to choose that version for the\n" //
			+ "Library.\n" //
			+ "\n" //

		);

		t.setFont(new java.awt.Font("Dialog", 0, 12));
		t.setEditable(false);
		t.setBackground(new Color(180, 180, 180));
		t.setCaretPosition(0);

		JScrollPane s = new JScrollPane(t);
		s.setBorder(BorderFactory.createLoweredBevelBorder());
		//s.setHorizontalScrollBarPolicy(s.HORIZONTAL_SCROLLBAR_ALWAYS);

		r1 = new JRadioButton("I accept the terms of the license agreement.");
		r1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parent.enableNext(true);
				}
			}
		);

		r2 = new JRadioButton("I do not accept the terms of the license agreement.");
		r2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					parent.enableNext(false);
				}
			}
		);

		r2.setSelected(true);
		ButtonGroup g = new ButtonGroup();
		g.add(r1);
		g.add(r2);

		l.setBounds(6, 10, 380, 22);
		s.setBounds(6, 40, 470, 160);
		r1.setBounds(30, 220, 380, 20);
		r2.setBounds(30, 240, 380, 20);

		add(l);
		add(s);
		add(r1);
		add(r2);
	}

	public void enter() {
		parent.enableNext(false);
		parent.enablePrev(true);
		r2.setSelected(true);
	}

	public void leave() {

	}
}


class Panel3 extends JPanel implements WizardPanel {
	Install parent;
	JTextField t;

	public Panel3(Install f) {
		parent = f;
		setLayout(null);
		JLabel l1 =
			new JLabel("Click Next to install to this folder, or click Change to install to a");
		JLabel l2 = new JLabel("different folder.");

		String s = new File(".").getAbsolutePath();
		s = Install.formatFilename(s.substring(0, s.length() - 1));
		t = new JTextField(s + parent.location);

		t.addFocusListener(
			new FocusAdapter() {
				public void focusLost(FocusEvent fe) {
					if (t.getText().length() == 0) {
						parent.enableNext(false);
					} else {
						parent.enableNext(true);
					}
				}
			}
		);

		t.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (t.getText().length() == 0) {
						parent.enableNext(false);
					} else {
						parent.enableNext(true);
					}
				}
			}
		);

		JButton c = new JButton("Change...");
		c.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					t.setText(Install.formatFilename(chooseDir(t.getText())));
				}
			}
		);

		l1.setBounds(6, 10, 360, 22);
		l2.setBounds(6, 30, 360, 22);
		t.setBounds(6, 60, 360, 24);
		c.setBounds(6, 100, 100, 24);

		add(l1);
		add(l2);
		add(t);
		add(c);
	}

	public void enter() {
		parent.enableNext(true);
		parent.enablePrev(true);
	}

	public void leave() {

	}

	public String getDir() {
		return t.getText();
	}

	private String chooseDir(String txt) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = fileChooser.showDialog(this, "Choose Directory");
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File newFile = fileChooser.getSelectedFile();
			return newFile.getAbsolutePath() + File.separator + parent.location;
		}
		return txt;
	}
}


class Panel4 extends JPanel implements WizardPanel {
	Install parent;
	JTextField t1;
	JTextField t2;

	public Panel4(Install f) {
		parent = f;
		setLayout(null);
		final JLabel l1 = new JLabel("JDK installation directory:");
		final JLabel l2 = new JLabel("Class path:");
		JButton b1 = new JButton("...");

		t1 = new JTextField(parent.javahome);
		t2 = new JTextField(parent.home + Install.FILE_SEPARATOR + "TheCells.jar");

		b1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					t1.setText(Install.formatFilename(chooseDir(t1.getText())));
				}
			}
		);

		l1.setBounds(6, 10, 160, 22);
		t1.setBounds(164, 10, 270, 22);
		b1.setBounds(435, 10, 22, 22);
		l2.setBounds(6, 36, 120, 22);
		t2.setBounds(164, 36, 270, 22);

		add(l1);
		add(t1);
		add(b1);
		add(l2);
		add(t2);
	}

	public void enter() {
		t2.setText(parent.home + Install.FILE_SEPARATOR + "TheCells.jar");
		parent.enableNext(true);
		parent.enablePrev(true);
	}

	public void leave() {

	}

	public String getJavaHome() {
		return t1.getText();
	}

	public String getClassPath() {
		return t2.getText();
	}

	private String chooseDir(String txt) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = fileChooser.showDialog(this, "Choose Directory");
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File newFile = fileChooser.getSelectedFile();
			return newFile.getAbsolutePath();
		}
		return txt;
	}
}


class Panel5 extends JPanel implements WizardPanel {
	Install parent;

	public Panel5(Install f) {
		parent = f;
		setLayout(null);
		JLabel l1 =
			new JLabel("The wizard has all the information it needs. If you want to review or ");
		JLabel l2 = new JLabel("change any settings, click Back. If you are satisfied with the ");
		JLabel l3 = new JLabel("settings, click Next to continue.");
		JLabel l4 = new JLabel("Installation settings:");

		JTextArea t = new JTextArea();

		String newline = Install.LINE_SEPARATOR;

		t.setText(
			" The Cells" + newline + "     Location " + parent.home + newline + newline + " JDK\n"
			+ "     Location " + parent.javahome + newline + "     Classpath " + parent.classpath
			+ newline
		);

		t.setFont(new java.awt.Font("Dialog", 0, 12));
		t.setEditable(false);
		t.setBackground(new Color(180, 180, 180));

		JScrollPane s = new JScrollPane(t);
		s.setBorder(BorderFactory.createLoweredBevelBorder());

		l1.setBounds(8, 10, 380, 20);
		l2.setBounds(8, 28, 380, 20);
		l3.setBounds(8, 46, 380, 20);
		l4.setBounds(8, 76, 380, 20);
		s.setBounds(6, 100, 380, 140);

		add(l1);
		add(l2);
		add(l3);
		add(l4);
		add(s);
	}

	public void enter() {
		parent.enableNext(false);
		parent.enablePrev(true);
		parent.enableInst(true);
	}

	public void leave() {

	}
}


class Panel6 extends JPanel implements WizardPanel {
	JProgressBar progressBar;
	JLabel label;
	Install parent;

	public Panel6(Install f) {
		parent = f;
		setLayout(null);
		parent.enableNext(false);
		parent.enablePrev(true);
		parent.enableInst(true);

		JLabel l1 = new JLabel("The selected products are installing. If you want to break the");
		JLabel l2 = new JLabel("installation click the Cancel button.");
		l1.setBounds(20, 50, 340, 20);
		l2.setBounds(20, 68, 340, 20);

		label = new JLabel("");
		label.setBounds(20, 130, 340, 20);

		progressBar = new JProgressBar(0, 0);

		//progressBar.setBorder(null);
		progressBar.setValue(0);
		progressBar.setBounds(20, 150, 340, 22);
		progressBar.setMinimum(0);
		progressBar.setMaximum(10);
		progressBar.setStringPainted(true);
		add(l1);
		add(l2);
		add(label);
		add(progressBar);
	}

	public void enter() {
	}

	public void leave() {

	}

	/**
	 * Extracts an archive (jarFile) into the given directory.
	 */
	public void extractJarFile(File jarFile, File directory) throws IOException {
		JarFile jar = new JarFile(jarFile);
		progressBar.setMaximum(jar.size());
		int n = 0;
		for (Enumeration e = jar.entries(); e.hasMoreElements();) {

			ZipEntry entry = (ZipEntry) e.nextElement();

			if (entry.getName().toLowerCase().startsWith("meta-inf")) {
				continue;
			}

			String filename = entry.getName();
			File file = new File(directory.getAbsolutePath(), filename);

			//file.deleteOnExit();
			if (!entry.isDirectory()) {
				InputStream is = jar.getInputStream(new ZipEntry(entry));
				boolean mkdirs = true;
				if (!file.getParentFile().exists()) {
					mkdirs = file.getParentFile().mkdirs();
				}
				if (mkdirs) {
					FileOutputStream fos = new FileOutputStream(file);
					copy(is, fos);
					fos.close();
				} else {
					throw new IOException(
						"Unpacking archive failed: could not make parent file for " + file
					);
				}
				is.close();
			}
			label.setText(filename);
			progressBar.setValue(n++);
		}
		jar.close();
	}

	/**
	 * Utility method that eads the input stream fully and writes the bytes to the current entry in
	 * the output stream.
	 */
	private void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[1024];
		int len = 0;
		while (len != -1) {
			try {
				len = is.read(buf, 0, buf.length);
			} catch (EOFException eof) {
				break;
			}

			if (len != -1) {
				os.write(buf, 0, len);
			}
		}
		is.close();
	}
}
