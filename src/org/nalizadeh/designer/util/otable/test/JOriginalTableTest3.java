/*
 * Copyright 2007 N.A.J. nalizadeh.org - All rights reserved. nalizadeh.org
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */

package org.nalizadeh.designer.util.otable.test;

import org.nalizadeh.designer.util.otable.JOriginalTable;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 * <p>Title:</p>
 *
 * <p>Description:</p>
 *
 * <p>Copyright: Copyright (c) 2007 N.A.J</p>
 *
 * <p>Organisation:</p>
 *
 *
 *
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class JOriginalTableTest3 {

	private final ImageIcon iconLevel1 = JOriginalTable.getImage("level1.gif");
	private final ImageIcon iconLevel2 = JOriginalTable.getImage("level2.gif");
	private final ImageIcon iconLevel3 = JOriginalTable.getImage("level3.gif");
	private final ImageIcon iconLevel1r = JOriginalTable.getImage("level1-red.gif");
	private final ImageIcon iconLevel1g = JOriginalTable.getImage("level1-green.gif");
	private final ImageIcon iconLevel1b = JOriginalTable.getImage("level1-blue.gif");

	private static final String[] columnNames =
		{
			"Beleg", "Aussteller", "Erf.Datum", "Erf.Nr.", "Buchungsdatum", "Vertrag",
			"Versicherungsobjekt", "Belegart", "Soll", "Haben", "Betrag", "Ausgleichstatus",
			"Ausgleichsgrund", "Ausgleichsbeleg", "Rechnungsnummer", "Rechnungsdatum",
			"Aktenzeichen", "Forderungsvorgang", "Beitragsmonat", "Zahlungsavis"
		};

	private static final Object[][] data =
		{
			{
				"001000000176", "506", "05.04.2006", "AB001", "05.04.2006", "110000000200",
				"L079041058123456789", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-10.00f),
				" ", false, " ", " ", " ", " ", " ", "050506AB008506", "000000", " "
			},
			{
				"001000000176", "506", "05.04.2006", "AB001", "05.04.2006", "110000000200",
				"L079041058123456789", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-10.00f),
				" ", false, " ", " ", " ", " ", " ", "050506AB008506", "000000", " ", "000"
			},
			{
				"001000000176", "506", "05.04.2006", "AB001", "05.04.2006", "110000000200",
				"L079041058123456789", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-10.00f),
				" ", false, " ", " ", " ", " ", " ", "050506AB008506", "000000", " ", "000", "0001"
			},
			{
				"001000000461", "506", "05.05.2006", "AB008", "05.05.2006", "110000000200",
				"L079041058123456789", "Forderung", new Geldbetrag(123.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "050506AB008506", " ", " "
			},
			{
				"001000000461", "506", "05.05.2006", "AB008", "05.05.2006", "110000000200",
				"L079041058123456789", "Forderung", new Geldbetrag(123.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "050506AB008506", " ", " ", "000"
			},
			{
				"001000000461", "506", "05.05.2006", "AB008", "05.05.2006", "110000000200",
				"L079041058123456789", "Forderung", new Geldbetrag(123.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "050506AB008506", " ", " ", "000", "0001"
			},
			{
				"001000000251", "506", "12.04.2006", "AB022", "12.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(10.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "120406AB022506", "", " "
			},
			{
				"001000000251", "506", "12.04.2006", "AB022", "12.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(10.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "120406AB022506", "", " ", "000"
			},
			{
				"001000000251", "506", "12.04.2006", "AB022", "12.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(10.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "120406AB022506", "", " ", "001"
			},
			{
				"001000000251", "506", "12.04.2006", "AB022", "12.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(10.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "120406AB022506", "", " ", "002"
			},
			{
				"001000000251", "506", "12.04.2006", "AB022", "12.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(10.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "120406AB022506", "", " ", "001", "0001"
			},
			{
				"001000000251", "506", "12.04.2006", "AB022", "12.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(10.00f), new Geldbetrag(0.00f),
				" ", true, " ", " ", " ", " ", " ", "120406AB022506", "", " ", "002", "0001"
			},
			{
				"001000000354", "507", "25.04.2006", "AB008", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(60.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB008507", "", " "
			},
			{
				"001000000354", "507", "25.04.2006", "AB008", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(60.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB008507", "", " ", "000"
			},
			{
				"001000000354", "507", "25.04.2006", "AB008", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(60.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB008507", "", " ", "000", "0001"
			},
			{
				"001000000354", "507", "25.04.2006", "AB008", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(60.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB008507", "", " ", "000", "0002"
			},
			{
				"001000000354", "507", "25.04.2006", "AB008", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(60.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB008507", "", " ", "000", "0003"
			},
			{
				"001000000355", "507", "25.04.2006", "AB007", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(50.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB007507", "", " ", "000", "0001"
			},
			{
				"001000000355", "507", "25.04.2006", "AB007", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(20.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB007507", "", " ", "000", "0002"
			},
			{
				"001000000355", "507", "25.04.2006", "AB007", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(50.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB007507", "", " ", "001", "0001"
			},
			{
				"001000000355", "507", "25.04.2006", "AB007", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(50.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB007507", "", " ", "001", "0002"
			},
			{
				"001000000356", "507", "25.04.2006", "AB009", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(2000.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB009507", " ", " ", "000", "0001"
			},
			{
				"001000000357", "507", "25.04.2006", "AB010", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(5000.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB010507", " ", " ", "000", "0001"
			},
			{
				"001000000360", "829", "25.04.2006", "AB021", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(120.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB021829", " ", " ", "000", "0001"
			},
			{
				"001000000361", "829", "25.04.2006", "AB022", "25.04.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(320.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "250406AB022829", " ", " ", "000", "0001"
			},
			{
				"001000000404", "825", "26.04.2006", "AB001", "25.04.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f),
				new Geldbetrag(-10381.35f), " ", false, " ", " ", " ", " ", " ", " ", " ", " ",
				"000", "0001"
			},
			{
				"001000000404", "825", "26.04.2006", "AB001", "25.04.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f),
				new Geldbetrag(-12505.99f), " ", false, " ", " ", " ", " ", " ", " ", " ", " ",
				"000", "0002"
			},
			{
				"001000000421", "507", "03.05.2006", "AB001", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(100.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB001507", " ", " ", "000", "0001"
			},
			{
				"001000000421", "507", "03.05.2006", "AB001", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(30.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB001507", "", " ", "000", "0002"
			},
			{
				"001000000423", "507", "03.05.2006", "AB006", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-500.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0001"
			},
			{
				"001000000423", "507", "03.05.2006", "AB006", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-500.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "001", "0001"
			},
			{
				"001000000425", "506", "03.05.2006", "AB009", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(530.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB009506", " ", " "
			},
			{
				"001000000425", "506", "03.05.2006", "AB009", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(530.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB009506", " ", " ", "000"
			},
			{
				"001000000425", "506", "03.05.2006", "AB009", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(530.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB009506", " ", " ", "000", "0001"
			},
			{
				"001000000426", "506", "03.05.2006", "AB007", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(100.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB007506", " ", " "
			},
			{
				"001000000426", "506", "03.05.2006", "AB007", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(-40.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB007506", " ", " ", "000"
			},
			{
				"001000000426", "506", "03.05.2006", "AB007", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(150.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB007506", " ", " ", "000", "0001"
			},
			{
				"001000000466", "829", "08.05.2006", "AB003", "08.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-88.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0001"
			},
			{
				"001000000484", "1", "03.05.2006", "AB003", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(300.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB003001", " ", " ", "000", "0001"
			},
			{
				"001000000484", "1", "03.05.2006", "AB003", "03.05.2006", "110000000200",
				"L079041058260200013", "Forderung", new Geldbetrag(500.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", "030506AB003001", " ", " ", "000", "0002"
			},
			{
				"001000000513", "507", "03.05.2006", "AB008", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-100.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " "
			},
			{
				"001000000513", "507", "03.05.2006", "AB008", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(100.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000"
			},
			{
				"001000000513", "507", "03.05.2006", "AB008", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(1600.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "001"
			},
			{
				"001000000513", "507", "03.05.2006", "AB008", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(100.00f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0001"
			},
			{
				"001000000526", "829", "10.05.2006", "AB002", "10.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-12.35f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0001"
			},
			{
				"001000000534", "825", "10.05.2006", "AB001", "10.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-310.21f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0001"
			},
			{
				"001000000589", "829", "03.05.2006", "AB001", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-10.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0001"
			},
			{
				"001000000589", "829", "03.05.2006", "AB001", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-10.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " "
			},
			{
				"001000000589", "829", "03.05.2006", "AB001", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-10.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000"
			},
			{
				"001000000589", "829", "03.05.2006", "AB001", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-10.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "001"
			},
			{
				"001000000589", "829", "03.05.2006", "AB001", "03.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-10.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "001", "0001"
			},
			{
				"001000000646", "1", "12.05.2006", "AB001", "12.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-100.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0001"
			},
			{
				"001000000661", "1", "12.05.2006", "AB002", "12.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-100.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " "
			},
			{
				"001000000661", "1", "12.05.2006", "AB002", "12.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-100.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000"
			},
			{
				"001000000661", "1", "12.05.2006", "AB002", "12.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-100.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "001"
			},
			{
				"001000000661", "1", "12.05.2006", "AB002", "12.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-100.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0001"
			},
			{
				"001000000661", "1", "12.05.2006", "AB002", "12.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-100.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "000", "0002"
			},
			{
				"001000000661", "1", "12.05.2006", "AB002", "12.05.2006", "110000000200",
				"L079041058260200013", "Leistung", new Geldbetrag(0.00f), new Geldbetrag(-100.00f),
				" ", false, " ", " ", " ", " ", " ", " ", " ", " ", "001", "0001"
			},
			{
				"001000000180", "506", "05.04.2006", "AB014", "05.04.2006", "110000000200",
				"L179041058123456789", "Forderung", new Geldbetrag(678.12f), new Geldbetrag(0.00f),
				" ", false, " ", " ", " ", " ", " ", " ", "050406AB014506", "000000", "000", "0001"
			},
		};

	private JOriginalTable table = null;

	private static JOriginalTable.TextEditorComponent tt =
		new JOriginalTable.TextEditorComponent() {
			public String getValidState(Object value) {
				String s = (String) value;
				if (s.equals("")) {
					return "Eingabe im Pflichfeld erforderlich";
				}
				if (!s.equals("Hello")) {
					return "Text muss Hello heissen";
				}
				return null;
			}
		};

	private static class MyTestListEditor extends JOriginalTable.ListEditorComponent {
		public MyTestListEditor() {
			super(
				new String[] { "A", "B", "Hello", "Word" },
				new String[] { "A", "B", "Hello", "Word" },
				null
			);
		}
	}

	private MyTestListEditor cc = new MyTestListEditor();

	private class NodeListener extends JOriginalTable.JOriginalTableNodeAdapter {

		public Object createKey(Object[] d, int aggrigationLevel, int level) {
			if (aggrigationLevel == 1) {
				switch (level) {

					case 1 :
						return d[0];

					case 2 :
						return d.length > 20 ? d[20] : null;

					case 3 :
						return d.length > 21 ? d[21] : null;

					default :
						break;
				}
				return null;
			}

			if (aggrigationLevel == 2) {
				switch (level) {

					case 1 :
						return d.length > 20 ? d[0] + " / " + d[20] : null;

					case 2 :
						return d.length > 21 ? d[21] : null;

					default :
						break;
				}
				return null;
			}

			if (aggrigationLevel == 3) {
				switch (level) {

					case 1 :
						return d.length > 21 ? d[0] + " / " + d[20] + " / " + d[21] : null;

					default :
						break;
				}
				return null;
			}

			return null;
		}

		public void initData(JOriginalTable.JOriginalTableNode node) {

			Geldbetrag soll = new Geldbetrag(0);
			Geldbetrag haben = new Geldbetrag(0);
			Geldbetrag betrag = new Geldbetrag(0);

			List<JOriginalTable.JOriginalTableNode> childs = node.getChilds();
			for (JOriginalTable.JOriginalTableNode n : childs) {
				soll.add((Geldbetrag) n.getData()[8]);
				haben.add((Geldbetrag) n.getData()[9]);
			}
			betrag.add(soll);
			betrag.add(haben);

			switch (node.getLevel()) {

				case 1 :
					node.getData()[8] = Geldbetrag.NULL;
					node.getData()[9] = Geldbetrag.NULL;
					node.getData()[10] = betrag;
					break;

				case 2 :
					node.getData()[8] = soll;
					node.getData()[9] = haben;
					node.getData()[10] = Geldbetrag.NULL;
					node.getData()[1] = "";
					node.getData()[2] = "";
					node.getData()[3] = "";
					node.getData()[7] = "";
					break;

				case 3 :
					node.getData()[1] = "";
					node.getData()[2] = "";
					node.getData()[3] = "";
					node.getData()[7] = "";
					break;

				default :
					break;
			}
		}

		public ImageIcon getIcon(JOriginalTable.JOriginalTableNode node) {

			int level = node.getLevel();
			int aggrigationLevel = node.getAggrigationLevel();
			int t = level + aggrigationLevel;

			if (level == 1 && aggrigationLevel == 1) {

				List<JOriginalTable.JOriginalTableNode> childs = node.getChilds();
				if (childs.size() > 1) {
					t = 20;
				}

				for (JOriginalTable.JOriginalTableNode n : childs) {
					if (n.getChilds().size() > 1) {
						t = 30;
						break;
					}
				}
			}

			ImageIcon icon = null;

			if (level == 1 && aggrigationLevel == 1) {
				icon = iconLevel1;
			} else if (level == 2 || aggrigationLevel == 2) {
				icon = iconLevel2;
			} else if (level == 3 || aggrigationLevel == 3) {
				icon = iconLevel3;
			}

			if (t == 20) {
				icon = iconLevel1b;
			} else if (t == 30) {
				icon = iconLevel1r;
			}

			return icon;
		}

		public boolean isEditable(JOriginalTable.JOriginalTableNode node, int row, int column) {
			if (node.getLevel() == 1 && column == 3) {
				return true;
			}
			if (node.getLevel() == 2 && column == 11) {
				return true;
			}
			if ((node.getLevel() == 3 || node.getLevel() == 7) && column == 3) {
				return true;
			}
			return false;
		}

		public JOriginalTable.EditorComponent getEditor(
			JOriginalTable.JOriginalTableNode node,
			int								  row,
			int								  column
		) {
			if (node.getLevel() == 1 && column == 3) {
				return tt;
			}
			if ((node.getLevel() == 3 || node.getLevel() == 7) && column == 3) {
				return cc;
			}

			return null;
		}
	}

	public JOriginalTable makeTreeTable() {
		this.table = new JOriginalTable(columnNames, new NodeListener());
		this.table.init(data, 1);
		return table;
	}

	void setLevel(int lv) {
		table.init(data, lv);
	}

	public static JPanel asPanel() {
		final JOriginalTableTest3 test = new JOriginalTableTest3();

		JOriginalTable treetable = test.makeTreeTable();
		treetable.setRowHeight(20);
		treetable.setShowDetailPopup(true);

		JScrollPane sp = new JScrollPane();
		sp.getViewport().add(treetable);

		JButton b1 = new JButton("Level1");
		JButton b2 = new JButton("Level2");
		JButton b3 = new JButton("Level3");
		b1.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					test.setLevel(1);
				}
			}
		);
		b2.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					test.setLevel(2);
				}
			}
		);
		b3.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					test.setLevel(3);
				}
			}
		);

		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p.add(b1);
		p.add(b2);
		p.add(b3);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(sp, BorderLayout.CENTER);
		panel.add(p, BorderLayout.SOUTH);
		return panel;
	}

	public static void main(String[] args) {

		try {

			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("TreeTable");
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(JOriginalTableTest3.asPanel(), BorderLayout.CENTER);

		frame.pack();
		frame.setSize(1000, 800);
		frame.setVisible(true);
	}
}

class Geldbetrag implements Comparable {
	public static final Geldbetrag NULL = new Geldbetrag();

	private float betrag = -9999.99f;

	public Geldbetrag() {
	}

	public Geldbetrag(float betrag) {
		this.betrag = betrag;
	}

	public void add(Geldbetrag n) {
		betrag += n.betrag;
	}

	public String toString() {
		return betrag == NULL.betrag ? "" : "" + betrag;
	}

	public int compareTo(Object o) {
		return toString().compareTo(o.toString());
	}
}
