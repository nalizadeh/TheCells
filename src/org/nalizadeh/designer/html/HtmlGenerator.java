
package org.nalizadeh.designer.html;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * @author   Nader Alizadeh
 * @version  1.0
 */
public class HtmlGenerator {

	public StringBuffer generateHTML(JPanel container) {

		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<html>\n");
		buffer.append("<head><title>position</title>\n");
		buffer.append("<link rel=\"StyleSheet\" href=\"cells.css\" type=\"text/css\"/>\n");
		buffer.append("<script type=\"text/javascript\" src=\"cells.js\"></script>\n");
		buffer.append("<style type=\"text/css\">\n");
		buffer.append("<!--\n");
		buffer.append("body { line-height:16.0pt; }\n");
		buffer.append("-->\n");
		buffer.append("</style>\n");
		buffer.append("</head>\n");
		buffer.append("<body bgcolor=\"FFFFFF\" text=\"#000000\">\n");
		buffer.append("<form action=\"\" method=\"get\">\n");

		appendPanel(container, buffer);

		buffer.append("</form>\n");
		buffer.append("</body>\n");
		buffer.append("</html>\n");

		//System.out.println(buffer);
		
		return buffer;
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendPanel(JPanel pan, StringBuffer buffer) {

		buffer.append(
			"<div class=\"wspanel\" id=\"panel_" + pan.getName() + "\" style=\"" + getPosition(pan, false, 0, 0)
			+ getDimension(pan, 0, 0) + " z-index:1; " + getBackgroundColor(pan) + " border:solid 0px; " +
			"padding: 0px; padding-left: 0px; padding-top: 0px; padding-bottom: 0px; margin-bottom: 0px;\">\n");

		Component[] co = pan.getComponents();

		for (int i = 0; i < co.length; i++) {
			if (co[i] instanceof JLabel) {
				appendLabel(co[i], buffer);
			} else if (co[i] instanceof JButton) {
				appendButton(co[i], buffer);
			} else if (co[i] instanceof JTextField) {
				appendTextField(co[i], buffer);
			} else if (co[i] instanceof JTextArea) {
				appendTextArea(co[i], buffer, null);
			} else if (co[i] instanceof JCheckBox) {
				appendCheckBox(co[i], buffer);
			} else if (co[i] instanceof JRadioButton) {
				appendRadioButton(co[i], buffer);
			} else if (co[i] instanceof JComboBox) {
				appendComboBox(co[i], buffer);
			} else if (co[i] instanceof JList) {
				appendList(co[i], buffer, null);
			} else if (co[i] instanceof JTable) {
				appendTable(co[i], buffer, false);
			} else if (co[i] instanceof JTree) {
				appendTree(co[i], buffer, false);
			} else if (co[i] instanceof JScrollPane) {
				appendScrollPane(co[i], buffer);
			} else if (co[i] instanceof JTabbedPane) {
				appendTabbedPane(co[i], buffer);
			} else if (co[i] instanceof JPanel) {
				appendPanel((JPanel)co[i], buffer);
			}
		}
		buffer.append("</div>\n");
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendLabel(Component co, StringBuffer sb) {

		JLabel lb = (JLabel) co;
		int ha = lb.getHorizontalAlignment();
		int va = lb.getVerticalAlignment();

		sb.append(
			"<div class=\"wslabel\" id=\"label_" + lb.getName() + "\" " + "style=\"" + getPosition(co, false, 0, 0)
			+ getDimension(co, 0, 0) + "text-align:"
			+ (
				ha == JLabel.LEFT ? "left"
				: ha == JLabel.CENTER ? "center" : ha == JLabel.RIGHT ? "right" : "left"
			) + "; " + "vertical-align:"
			+ (
				va == JLabel.CENTER ? "middle"
				: va == JLabel.TOP ? "top" : va == JLabel.BOTTOM ? "bottom" : "top"
			) + "; " + getFont(co, false) + getForegroundColor(co) + "\">" + lb.getText() + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendButton(Component co, StringBuffer sb) {

		JButton bt = (JButton) co;
		sb.append(
			"<div class=\"wsbutton\" id=\"button_" + bt.getName() + "\">" + "<input " + "type=\"button\" " + "name=\"" + bt.getText()
			+ "\" " + "value=\"" + bt.getText() + "\" " + "onClick=\"tue etwas!" + "\" "
			+ "style=\"" + getPosition(co, false, 0, 0) + getDimension(co, 0, 0)
			+ getFont(co, false) + "\">" + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendTextField(Component co, StringBuffer sb) {

		JTextField tx = (JTextField) co;
		sb.append(
			"<div class=\"wstextfield\" id=\"textfield_" + tx.getName() + "\">" + "<input " + "type=\"text\" " + "name=\"" + tx.getName()
			+ "\" " + "size=\"" + tx.getColumns() + "\" " + "maxlength=\"" + tx.getColumns() + "\" "
			+ "value=\"" + tx.getText() + "\" " + "style=\"" + getPosition(co, false, 0, 0)
			+ getDimension(co, 0, 0) + getFont(co, false) + "\">" + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendTextArea(Component co, StringBuffer sb, JScrollPane sp) {

		JTextArea tx = (JTextArea) co;
		sb.append(
			"<div class=\"wstextarea\" id=\"textarea_" + tx.getName() + "\">" + "<textarea " + "name=\"" + tx.getName() + "\" " + "cols=\""
			+ tx.getColumns() + "\" " + "rows=\"" + tx.getRows() + "\" " + "style=\""
			+ getPosition(sp == null ? co : sp, sp == null ? false : true, 0, 0)
			+ getDimension(sp == null ? co : sp, 0, 0) + getFont(co, false) + "\">" + tx.getText()
			+ "</textarea>" + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendCheckBox(Component co, StringBuffer sb) {

		JCheckBox cb = (JCheckBox) co;
		sb.append(
			"<div class=\"wscheckbox\" id=\"checkbox_" + cb.getName() + "\">" + "<input " + "type=\"checkbox\" " + "name=\"" + cb.getName()
			+ "\" " + "value=\"" + true + "\" " + (cb.isSelected() ? "checked " : "") + "style=\""
			+ getPosition(co, false, 0, 0) + "\">" + "<div " + "style=\""
			+ getPosition(co, false, 20, 0) + getDimension(co, 20, 0) + "text-align:left; "
			+ "vertical-align:middle; " + getFont(co, false) + "\">" + cb.getText() + "</div>"
			+ "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendRadioButton(Component co, StringBuffer sb) {

		JRadioButton rb = (JRadioButton) co;
		sb.append(
			"<div class=\"wsradiobutton\" id=\"radiobutton_" + rb.getName() + "\">" + "<input " + "type=\"radio\" " + "name=\"" + rb.getName()
			+ "\" " + "value=\"" + true + "\" " + (rb.isSelected() ? "checked " : "") + "style=\""
			+ getPosition(co, false, 0, 0) + "\">" + "<div " + "style=\""
			+ getPosition(co, false, 20, 0) + getDimension(co, 20, 0) + "text-align:left; "
			+ "vertical-align:middle; " + getFont(co, false) + "\">" + rb.getText() + "</div>"
			+ "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendComboBox(Component co, StringBuffer sb) {

		JComboBox cb = (JComboBox) co;

		StringBuffer sb2 = new StringBuffer();
		for (int j = 0; j < cb.getItemCount(); j++) {
			String s = cb.getItemAt(j) == cb.getSelectedItem() ? "<option selected>" : "<option>";
			sb2.append(s + cb.getItemAt(j).toString() + "</option>");
		}

		sb.append(
			"<div class=\"wscombobox\" id=\"combobox_" + cb.getName() + "\">" + "<select " + "name=\"" + cb.getName() + "\" " + "size=\"1\" "
			+ "style=\"" + getPosition(co, false, 0, 0) + getDimension(co, 0, 0)
			+ getFont(co, false) + "\">" + sb2 + "</select>" + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendList(Component co, StringBuffer sb, JScrollPane sp) {

		JList li = (JList) co;

		StringBuffer sb2 = new StringBuffer();
		for (int j = 0; j < li.getModel().getSize(); j++) {
			String st = j == li.getSelectedIndex() ? "<option selected>" : "<option>";
			sb2.append(st + li.getModel().getElementAt(j).toString() + "</option>");
		}

		sb.append(
			"<div class=\"wslist\" id=\"list_" + li.getName() + "\">" + "<select " + "name=\"" + li.getName() + "\" " + "size=\""
			+ li.getModel().getSize() + "\" "
			+ (li.getSelectionMode() != ListSelectionModel.SINGLE_SELECTION ? "multple " : "")
			+ "style=\"" + getPosition(sp == null ? co : sp, sp == null ? false : true, 0, 0)
			+ getDimension(sp == null ? co : sp, 0, 0) + getFont(co, false) + "\">" + sb2
			+ "</select>" + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendTabbedPane(Component co, StringBuffer sb) {

		JTabbedPane tb = (JTabbedPane) co;

		sb.append("<div class=\"wstabbedpane\" style=\"" + getPosition(co, false, 0, 0) + getDimension(co, 0, 0) + getFont(co, false) +"\">\n");
		sb.append("<ul>\n");
		
		for (int i=0; i < tb.getTabCount(); i++) {
			String tabtit = tb.getTitleAt(i);
			String sel = i==0 ? " selected" : "";

			sb.append("<li><a data-tab=\"" + tabtit + "\" class=\"tab-view" + sel + "\" id=\"li_" + tabtit + "\" style=\"cursor: hand;\">" + tabtit + "</a></li>\n");
		}
		sb.append("</ul>\n");
		sb.append("<div id=\"tabCtrl\">\n");
			
		for (int i=0; i < tb.getTabCount(); i++) {
			String tabtit = tb.getTitleAt(i);
			String sel = i==0 ? " default-tab" : "";

			sb.append("<div class=\"tab" + sel + "\" id=\"" + tabtit + "\">\n");
			Component com = tb.getComponentAt(i);
			if (com instanceof JPanel) {
				appendPanel((JPanel)com, sb);
			}
			sb.append("</div>\n");
		}
		sb.append("</div>\n");
		sb.append("<script>initTabView();</script>\n");
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendTable(Component co, StringBuffer sb, boolean rel) {

		JTable tb = (JTable) co;

		int rows = tb.getRowCount() + 1;
		int cols = tb.getColumnCount();

		StringBuffer s = new StringBuffer();
		for (int row = 0; row < rows; row++) {
			s.append("<tr>\n");
			for (int col = 0; col < cols; col++) {
				if (row == 0) {
					s.append(
						"<td " + "style=\"" + "height:18px; " + "background:#DDDDDD; "
						+ "font-family:Arial; " + "font-size:11px; " + "font-weight:bold; "
						+ "font-style:normal;\">" + tb.getColumnName(col) + "</td>\n"
					);
				} else {
					Component c =
						tb.getCellRenderer(row - 1, col).getTableCellRendererComponent(
							tb,
							tb.getValueAt(row - 1, col),
							tb.isRowSelected(row - 1),
							tb.hasFocus(),
							row - 1,
							col
						);

					s.append(
						"<td " + "style=\"" + "height:18px; " + "background:#fff; "
						+ getFont(c, true) + "\">" + tb.getValueAt(row - 1, col) + "</td>\n"
					);
				}
			}
			s.append("</tr>\n");
		}

		sb.append(
			"<div class=\"wstable\" id=\"table_" + tb.getName() + "\" style=\"empty-cells:show; border-collapse:collapse;\">\n"
			+ "<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" " + "style=\""
			+ getPosition(co, rel, 2, 2) + getDimension(co, 0, 0)
			+ "\"\n>"
//     "height:" + r.height + "px;\">" +
			+ s + "</table>\n" + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private int in = 0;
	private int id = 1;

	private void appendTree(Component co, StringBuffer sb, boolean rel) {

		JTree tr = (JTree) co;

		in = 0;
		id = 1;

		sb.append(
			"<div class=\"wstree\" id=\"tree_" + tr.getName() + "\" " + "style=\"" + "background:#ffffff; " + getPosition(co, rel, 2, 2)
			+ getFont(co, false) + " white-space:nowrap;\">" + "<script type=\"text/javascript\">"
			+ "var Tree = new Array; "
		);

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tr.getModel().getRoot();
		appendTreeRec(root, sb, 0);

		sb.append("createTree(Tree, 0, 1); " + "</script>" + "</div>\n");
	}

	private void appendTreeRec(DefaultMutableTreeNode node, StringBuffer sb, int pa) {

		sb.append(
			"Tree[" + in + "] = \"" + id + "|" + pa + "|" + node.getUserObject() + "|" + "##\"; "
		);

		if (node.isLeaf()) {
			return;
		}

		int i = id;
		Enumeration df = node.children();

		while (df.hasMoreElements()) {
			in++;
			id++;
			DefaultMutableTreeNode no = (DefaultMutableTreeNode) df.nextElement();
			appendTreeRec(no, sb, i);
		}
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendScrollPane(Component co, StringBuffer sb) {

		JScrollPane sp = (JScrollPane) co;
		Component c = sp.getViewport().getView();

		sb.append(
			"<div class=\"wsschrollpane\" id=\"scrollpane_" + sp.getName() + "\" " + "style=\"" + "border: 0px solid #000; "
			+ getPosition(co, false, 0, 0) + getDimension(co, 0, 0) + " overflow:auto;\">\n"
		);

		if (c instanceof JTextArea) {
			appendTextArea(c, sb, sp);
		} else if (c instanceof JList) {
			appendList(c, sb, sp);
		} else if (c instanceof JTable) {
			appendTable(c, sb, true);
		} else if (c instanceof JTree) {
			appendTree(c, sb, true);
		}

		sb.append("</div>\n");
	}

	private String getPosition(Component co, boolean rel, int xo, int yo) {
		Rectangle r = co.getBounds();

		String s = "absolute";
		if (rel) {
			r.x = xo;
			r.y = yo;
			xo = 0;
			yo = 0;
			s = "relative";
		}
		return "position:" + s + "; " + "left:" + (r.x + xo) + "px; " + "top:" + (r.y + yo)
		+ "px; ";
	}

	private String getDimension(Component co, int xo, int yo) {
		Rectangle r = co.getBounds();
		return "width:" + (r.width - xo) + "px; " + "height:" + (r.height - yo) + "px; ";
	}

	private String getForegroundColor(Component co) {
		Color c = co.getForeground();
		return "color:" + toHex(c.getRed(), c.getGreen(), c.getBlue()) + ";";
	}
	
	private String getBackgroundColor(Component co) {
		Color c = co.getBackground();
		return "background-color:" + toHex(c.getRed(), c.getGreen(), c.getBlue()) + ";";
	}
	
	private String getFont(Component co, boolean allways) {
		Font fo = co.getFont();

		if (
			allways
			|| !fo.getFamily().equals("sansserif")
			|| fo.isBold()
			|| fo.getSize() != 11
			|| fo.isItalic()
		) {

			return "font-family:" + fo.getFamily() + "; " + "font-size:" + fo.getSize() + "px; "
			+ "font-weight:" + (fo.isBold() ? "bold" : "normal") + "; " + "font-style:"
			+ (fo.isItalic() ? "italic" : "normal") + ";";
		}

		return "";
	}

	private String toHex(int r, int g, int b) {
	    return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
	  }

	private String toBrowserHexValue(int number) {
	    StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
	    while (builder.length() < 2) {
	      builder.append("0");
	    }
	    return builder.toString().toUpperCase();
	 }
}

