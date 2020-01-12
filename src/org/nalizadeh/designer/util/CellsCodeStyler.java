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

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

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
public class CellsCodeStyler {

	private static final Color bla = new Color(0, 0, 128);
	private static final Color blb = new Color(0, 0, 255);
	private static final Color gr = new Color(0, 128, 0);
	private static final Color mg = new Color(128, 0, 128);
	private static final Color lnc = new Color(230, 255, 230);

	// constant 'States' java source code can be in
	private static final int TEXT = 0;
	private static final int PACKAGE_LINE = 1;
	private static final int IMPORT_LINE = 2;
	private static final int JAVADOC = 3;
	private static final int MULTILINE_COMMENT = 4;
	private static final int LINE_COMMENT = 5;
	private static final int DOUBLE_QUOTE = 6;
	private static final int SINGLE_QUOTE = 7;

	// collect various sets of keywords
	private Collection keywordCollection;
	private Collection primitiveTypeCollection;
	private Collection primitiveLiteralCollection;
	private Collection javadocTagCollection;

	// collection type is Hashset for unique elements and fast retieval
	private static final String[] keywordArray =
		{
			"abstract", "default", "if", "private", "do", "implements", "protected", "throws",
			"break", "import", "public", "transient", "else", "instanceof", "return", "try", "case",
			"extends", "throw", "static", "catch", "final", "interface", "while", "volatile",
			"finally", "super", "synchronized", "class", "native", "switch", "package", "const",
			"for", "new", "goto", "continue", "this", "assert", "strictfp"
		};

	private static final String[] primitiveTypeArray =
		{ "boolean", "char", "byte", "short", "int", "long", "float", "double", "void" };

	private static final String[] primitiveLiteralArray = { "false", "true", "null" };

	private static final String[] javadocTagArray =
		{
			"see", "author", "version", "param", "return", "exception", "deprecated", "throws",
			"link", "since", "serial", "serialField", "serialData", "beaninfo"
		};

	private int tabSize = 4;

	public void style(StringBuffer buffer, StyledDocument doc, StyledDocument doc2)
		throws BadLocationException
	{

		keywordCollection = new HashSet(Arrays.asList(keywordArray));
		primitiveTypeCollection = new HashSet(Arrays.asList(primitiveTypeArray));
		primitiveLiteralCollection = new HashSet(Arrays.asList(primitiveLiteralArray));
		javadocTagCollection = new HashSet(Arrays.asList(javadocTagArray));

		int state = TEXT;
		int index = -1;
		char ptr = 0;

		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		StyleConstants.setFontFamily(def, "Courier New");
		StyleConstants.setItalic(def, false);
		StyleConstants.setFontSize(def, 13);

		Style pkg = doc.addStyle("package", def);
		StyleConstants.setForeground(pkg, Color.MAGENTA.darker());
		StyleConstants.setBold(pkg, true);

		Style imp = doc.addStyle("import", def);
		StyleConstants.setForeground(imp, Color.MAGENTA.darker());

		Style idt = doc.addStyle("identifier", def);
		StyleConstants.setForeground(idt, bla);
		StyleConstants.setBold(idt, true);

		Style prm = doc.addStyle("primitive", def);
		StyleConstants.setBold(prm, true);
		StyleConstants.setForeground(prm, Color.CYAN);

		Style var = doc.addStyle("variable", def);
		StyleConstants.setForeground(var, mg);

		Style con = doc.addStyle("constant", def);
		StyleConstants.setForeground(con, Color.CYAN.darker());

		Style num = doc.addStyle("digit", def);
		StyleConstants.setForeground(num, Color.RED);

		Style str = doc.addStyle("string", def);
		StyleConstants.setForeground(str, blb);

		Style chr = doc.addStyle("char", str);

		Style met = doc.addStyle("method", def);

		Style com = doc.addStyle("coment", def);
		StyleConstants.setForeground(com, gr);

		Style jdo = doc.addStyle("javadoc", def);
		StyleConstants.setForeground(jdo, gr);

		Style jdp = doc.addStyle("javadocparam", def);
		StyleConstants.setForeground(jdp, gr);
		StyleConstants.setBold(jdp, true);

		Style lin = doc.addStyle("linenumber", def);
		StyleConstants.setBackground(lin, lnc);
		StyleConstants.setForeground(lin, Color.LIGHT_GRAY);

		Style style = def;
		StringBuffer ident = new StringBuffer();
		int linenum1 = -1;
		int linenum2 = 0;

		while (index++ < buffer.length() - 2) {
			if (linenum1 != linenum2) {
				String ls =
					linenum2 < 10 ? "000" + linenum2
					: linenum2 < 100 ? "00" + linenum2
					: linenum2 < 1000 ? "0" + linenum2 : "" + linenum2;

				doc2.insertString(doc2.getLength(), ls + "  ", lin);
				linenum1 = linenum2;
			}

			ptr = buffer.charAt(index);

			if (Character.isJavaIdentifierPart(ptr)) {

				// identifier
				ident.append(ptr);
				continue;
			}

			if (ident.length() > 0) {

				String identifier = ident.toString();

				if (state == DOUBLE_QUOTE) {
					doc.insertString(doc.getLength(), identifier, str);

				} else if (state == SINGLE_QUOTE) {
					doc.insertString(doc.getLength(), identifier, chr);

				} else if (state == MULTILINE_COMMENT) {
					doc.insertString(doc.getLength(), identifier, com);

				} else if (state == LINE_COMMENT) {
					doc.insertString(doc.getLength(), identifier, com);

				} else if (state == JAVADOC) {

					// in javadoc state
					if (
						javadocTagCollection.contains(identifier)
						&& buffer.charAt(index - (identifier.length() + 1)) == '@'
					) {

						// identifier is a javadocTag
						doc.remove(doc.getLength() - 1, 1);
						doc.insertString(doc.getLength(), "@" + identifier, jdp);
					} else {
						doc.insertString(doc.getLength(), identifier, jdo);
					}
				} else if (state == IMPORT_LINE) {

					// import identifier
					doc.insertString(doc.getLength(), identifier, imp);

				} else if (state == PACKAGE_LINE) {

					// package identifier
					doc.insertString(doc.getLength(), identifier, pkg);

				} else if (state == TEXT) {
					if (keywordCollection.contains(identifier)) {

						// identifier is a keyword
						doc.insertString(doc.getLength(), identifier, idt);

						if (identifier.equals("import")) {

							// anything after an import in text mode must be
							// an import name, so enter state to process this
							state = IMPORT_LINE;
							style = imp;

						} else if (identifier.equals("package")) {

							// anything after an package in text mode must be
							// an package name, so enter state to process this
							state = PACKAGE_LINE;
							style = pkg;
						}

					} else if (primitiveTypeCollection.contains(identifier)) {

						// identifier is a primitive type
						doc.insertString(doc.getLength(), identifier, idt);

					} else if (
						identifier.equals(identifier.toUpperCase())
						&& !Character.isDigit(identifier.charAt(0))
					) {

						// identifier is a constant
						doc.insertString(doc.getLength(), identifier, con);

					} else if (Character.isUpperCase(identifier.charAt(0))) {

						// identifier is a constructor or non-primitive type
						doc.insertString(doc.getLength(), identifier, def);

					} else if (
						!Character.isDigit(identifier.charAt(0))
						&& !primitiveLiteralCollection.contains(identifier)
					) {

						// identifier is a method or a variable

						// eat white space
						int i = index;
						while (Character.isWhitespace(buffer.charAt(i++))) {
						}

						// identifier is a method
						if (buffer.charAt(i - 1) == '(') {
							doc.insertString(doc.getLength(), identifier, met);
						}
						// identifier is a variable
						else {
							doc.insertString(doc.getLength(), identifier, var);
						}
					} else {
						if (primitiveLiteralCollection.contains(identifier)) {

							// primitiveLiteral (boolean or null)
							doc.insertString(doc.getLength(), identifier, idt);
						}
						// a numerical literal
						else {
							doc.insertString(doc.getLength(), identifier, num);
						}
					}
				}

				ident.delete(0, ident.length());
			}

			// process characters NOT in identifiers
			switch (ptr) {

				case ';' : // sc
					doc.insertString(doc.getLength(), ";", style);
					style = def;
					state = TEXT;
					break;

				case '\"' : // double quote
					doc.insertString(doc.getLength(), "\"", str);
					if (state == TEXT) {
						state = DOUBLE_QUOTE;
						style = str;

					} else if (state == DOUBLE_QUOTE) {
						state = TEXT;
						style = def;
					}
					break;

				case '\'' : // single quote
					doc.insertString(doc.getLength(), "\'", chr);
					if (state == TEXT) {
						state = SINGLE_QUOTE;
						style = chr;

					} else if (state == SINGLE_QUOTE) {
						state = TEXT;
						style = def;
					}
					break;

				case '\\' : // backslash
					doc.insertString(doc.getLength(), "\'", style);

					if (state == DOUBLE_QUOTE || state == SINGLE_QUOTE) {

						// treat as a character escape sequence
						String s = "" + buffer.charAt(++index);
						doc.insertString(doc.getLength(), s, style);
					}
					break;

				case '\t' : // tab

					// replace tabs with tabsize number of spaces
					for (int i = 0; i < tabSize; i++) {
						doc.insertString(doc.getLength(), " ", style);
					}
					break;

				case '*' : // star
					if (state == TEXT && buffer.charAt(index - 1) == '/') {
						if (buffer.length() - 1 > index && buffer.charAt(index + 1) == '*') {
							state = JAVADOC;
							style = jdo;
							doc.insertString(doc.getLength(), "/*", jdo);

						} else {
							state = MULTILINE_COMMENT;
							style = com;
							doc.insertString(doc.getLength(), "/*", com);
						}
					} else if (state == JAVADOC) {
						doc.insertString(doc.getLength(), "*", jdo);
					} else if (state == MULTILINE_COMMENT) {
						doc.insertString(doc.getLength(), "*", com);
					} else if (state == LINE_COMMENT) {
						doc.insertString(doc.getLength(), "*", com);
					}
					break;

				case '/' : // foward slash

					if (
						(state == MULTILINE_COMMENT || state == JAVADOC)
						&& buffer.charAt(index - 1) == '*'
					) {
						state = TEXT;
						style = def;
						doc.insertString(doc.getLength(), "/", com);
					}
					if (state == TEXT && index > 0 && buffer.charAt(index - 1) == '/') {
						state = LINE_COMMENT;
						style = com;
						doc.insertString(doc.getLength(), "//", com);
					}
					break;

				case '\r' : // carriage return

				// fall through
				case '\n' : // line feed

					if (
						buffer.charAt(index) == '\r'
						&& buffer.length() - 1 > index
						&& buffer.charAt(index + 1) == '\n'
					) {
						++index;
					}

					// end single line comments
					if (state == LINE_COMMENT) {
						state = TEXT;
						style = def;
					}

					// this is windows-style (unix-style is '\r\n')
					doc.insertString(doc.getLength(), "\n", style);
					linenum2++;
					break;

				case 0 : // nul character
					break;

				default : // everything else
					doc.insertString(doc.getLength(), "" + ptr, style);
			}
		}
	}
}
