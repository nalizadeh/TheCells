
package org.nalizadeh.designer.html;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
public class AspxGenerator {

	private List<String> variables = new ArrayList<String>();
	
	public void generateAspx(JPanel container, StringBuffer buffer1, StringBuffer buffer2) {

		buffer1.append("<%@ Page Language=\"C#\" AutoEventWireup=\"True\" CodeBehind=\"CodeBehind.aspx.cs\" Inherits=\"MyPackage.CodeBehind\" %>\n");
		buffer1.append("<!DOCTYPE html>\n");

		buffer1.append("<html>\n");
		buffer1.append("<head runat=\"server\">\n");
		buffer1.append("<title>position</title>\n");
		buffer1.append("<link rel=\"StyleSheet\" href=\"cells.css\" type=\"text/css\"/>\n");
		buffer1.append("<script type=\"text/javascript\" src=\"cells.js\"></script>\n");
		buffer1.append("</head>\n");
		buffer1.append("<body bgcolor=\"FFFFFF\" text=\"#000000\">\n");
		buffer1.append("<form id=\"form1\" runat=\"server\">\n");

		//====
		
		buffer2.append(
			"using System;\n"+
			"using System.Collections.Generic;\n"+
			"using System.Linq;\n"+
			"using System.Web;\n"+
			"using System.Web.UI;\n"+
			"using System.Web.UI.WebControls;\n"+
			"\n"+
			"namespace MyPackage\n"+
			"{\n"+
			"    public partial class CodeBehind : System.Web.UI.Page\n"+
			"    {\n"+
			"        protected void Page_Load(object sender, EventArgs e)\n"+
			"        {\n"+
			"        }\n"+
			"\n");
		
		appendPanel(container, buffer1, buffer2);

		buffer1.append("</form>\n");
		buffer1.append("</body>\n");
		buffer1.append("</html>\n");

		buffer2.append("    }\n");
		buffer2.append("}\n");
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendPanel(JPanel pan, StringBuffer buffer1, StringBuffer buffer2) {

		buffer1.append(
			"<div class=\"wspanel\" id=\"panel_" + pan.getName() + "\" style=\"" + getPosition(pan, false, 0, 0)
			+ getDimension(pan, 0, 0, 0, 0) + " z-index:1; " + getBackgroundColor(pan) + " border:solid 0px; " +
			"padding: 0px; padding-left: 0px; padding-top: 0px; padding-bottom: 0px; margin-bottom: 0px;\">\n");

		Component[] co = pan.getComponents();

		for (int i = 0; i < co.length; i++) {
			if (co[i] instanceof JLabel) {
				appendLabel(co[i], buffer1, buffer2);
			} else if (co[i] instanceof JButton) {
				appendButton(co[i], buffer1, buffer2);
			} else if (co[i] instanceof JTextField) {
				appendTextField(co[i], buffer1);
			} else if (co[i] instanceof JTextArea) {
				appendTextArea(co[i], buffer1, null);
			} else if (co[i] instanceof JCheckBox) {
				appendCheckBox(co[i], buffer1);
			} else if (co[i] instanceof JRadioButton) {
				appendRadioButton(co[i], buffer1);
			} else if (co[i] instanceof JComboBox) {
				appendComboBox(co[i], buffer1);
			} else if (co[i] instanceof JList) {
				appendList(co[i], buffer1, null);
			} else if (co[i] instanceof JTable) {
				appendTable(co[i], buffer1, false);
			} else if (co[i] instanceof JTree) {
				appendTree(co[i], buffer1, false);
			} else if (co[i] instanceof JScrollPane) {
				appendScrollPane(co[i], buffer1);
			} else if (co[i] instanceof JTabbedPane) {
				appendTabbedPane(co[i], buffer1, buffer2);
			} else if (co[i] instanceof JPanel) {
				appendPanel((JPanel)co[i], buffer1, buffer2);
			}
		}
		buffer1.append("</div>\n");
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendLabel(Component co, StringBuffer sb1, StringBuffer sb2) {

		JLabel lb = (JLabel) co;
		int ha = lb.getHorizontalAlignment();
		int va = lb.getVerticalAlignment();

		String id = "label_" + lb.getName();
		variables.add(id);
		
		sb1.append(
			"<div class=\"wslabel\"><asp:Label runat=\"server\" id=\"" + id + "\" Text=\"" + lb.getText() + "\" " + 
					"style=\"" + getPosition(co, false, 0, 0)
			+ getDimension(co, 0, 0, 0, 0) + "text-align:"
			+ (
				ha == JLabel.LEFT ? "left"
				: ha == JLabel.CENTER ? "center" : ha == JLabel.RIGHT ? "right" : "left"
			) + "; " + "vertical-align:"
			+ (
				va == JLabel.CENTER ? "middle"
				: va == JLabel.TOP ? "top" : va == JLabel.BOTTOM ? "bottom" : "top"
			) + "; " + getFont(co, false) + getForegroundColor(co) + "\">" +  "</asp:Label></div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendButton(Component co, StringBuffer sb1, StringBuffer sb2) {

		JButton bt = (JButton) co;
		String id = "button_" + bt.getName();
		variables.add(id);

		sb1.append(
			"<div class=\"wsbutton\">" + "<asp:Button runat=\"server\" id=\"" + id + "\" Text=\"" + bt.getText()
			+ "\" " + "onCommand=\"" + id + "_Click\" "
			+ "style=\"" + getPosition(co, false, 0, 0) + getDimension(co, 0, 0, 0, 0)
			+ getFont(co, false) + "\"></asp:Button>" + "</div>\n"
		);
		
		sb2.append(
				"        protected void " + id + "_Click(object sender, EventArgs e)\n" +
				"        {\n" +
				"        }\n\n");
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
		String id = "textfield_" + tx.getName();
		variables.add(id);
		
		sb.append(
			"<div class=\"wstextfield\">" + "<asp:TextBox runat=\"server\" id=\"" + id + "\" Text=\"" 
			+ tx.getText() + "\" MaxLength=\"" + tx.getColumns() + "\" Columns=\"" + tx.getColumns() + "\" Rows=\"1\" style=\"" + getPosition(co, false, 0, 0)
			+ getDimension(co, 0, 0, 0, 0) + getFont(co, false) + "\"></asp:TextBox>" + "</div>\n"
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
		String id = "textfield_" + tx.getName();
		variables.add(id);

		sb.append(
			"<div class=\"wstextarea\"><asp:TextBox runat=\"server\" id=\"" + id + "\" TextMode=\"multiline\" style=\""
			+ getPosition(sp == null ? co : sp, sp == null ? false : true, 0, 0)
			+ getDimension(sp == null ? co : sp, 0, 0, 0, 0) + getFont(co, false) + "\">" + tx.getText()
			+ "</asp:TextBox>" + "</div>\n"
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
		String id = "textfield_" + cb.getName();
		variables.add(id);

		sb.append(
			"<div class=\"wscheckbox\">" + "<asp:CheckBox runat=\"server\" id=\"checkbox_" + id + "\" " 
			+  "Text=\"" + cb.getText() + "\" Checked=\"" + (cb.isSelected() ? "true " : "false") + "\" style=\""
			+ getPosition(co, false, 20, 0) + getDimension(co, 0, 0, 0, 0) + "text-align:left; "
			+ "vertical-align:middle; " + getFont(co, false) + "\"></asp:CheckBox></div>\n"
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
		String id = "textfield_" + rb.getName();
		variables.add(id);
		
		sb.append(
			"<div class=\"wsradiobutton\">" + "<asp:RadioButton runat=\"server\" id=\"radiobutton_" + id 
			+ "\" " + "Checked=\"" + (rb.isSelected() ? "true " : "false") + "\" Text=\"" + rb.getText() +
			"\" style=\"" + getPosition(co, false, 20, 0) + getDimension(co, 0, 0, 0, 0) + "text-align:left; "
			+ "vertical-align:middle; " + getFont(co, false) + "\"></asp:RadioButton></div>\n"
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
		String id = "textfield_" + cb.getName();
		variables.add(id);

		StringBuffer sb2 = new StringBuffer();
		for (int i = 0; i < cb.getItemCount(); i++) {
			String s = cb.getItemAt(i) == cb.getSelectedItem() ? "<asp:ListItem Selected=\"True\" " : "<asp:ListItem ";
			sb2.append(s + "Text=\"" + cb.getItemAt(i).toString() + "\" Value=\"" + i + "\" />");
		}

		sb.append(
			"<div class=\"wscombobox\">" + "<asp:DropDownList runat=\"server\" id=\"" + id + "\" " 
			+ "style=\"" + getPosition(co, false, 0, 0) + getDimension(co, 0, 0, 0, 0)
			+ getFont(co, false) + "\">" + sb2 + "</asp:DropDownList></div>\n"
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
		String id = "textfield_" + li.getName();
		variables.add(id);

		StringBuffer sb2 = new StringBuffer();
		for (int i = 0; i < li.getModel().getSize(); i++) {
			String s = i == li.getSelectedIndex() ? "<asp:ListItem Selected=\"True\" " : "<asp:ListItem ";
			sb2.append(s + "Text=\"" + li.getModel().getElementAt(i).toString() + "\" Value=\"" + i + "\" />");
		}

		sb.append(
			"<div class=\"wslist\">" + "<asp:ListBox runat=\"server\" id=\"" + id + "\" "
			+ "style=\"" + getPosition(sp == null ? co : sp, sp == null ? false : true, 0, 0)
			+ getDimension(sp == null ? co : sp, 0, 0, 0, 0) + getFont(co, false) + "\">" + sb2
			+ "</asp:ListBox>" + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendTabbedPane(Component co, StringBuffer sb1, StringBuffer sb2) {

		JTabbedPane tb = (JTabbedPane) co;

		sb1.append("<div class=\"wstabbedpane\" style=\"" + getPosition(co, false, 0, 0) 
		+ getDimension(co, 0, 0, 0, 0) + getFont(co, false) +"\">\n");
		sb1.append("<ul>\n");
		
		for (int i=0; i < tb.getTabCount(); i++) {
			String tabtit = tb.getTitleAt(i);
			String sel = i==0 ? " selected" : "";

			sb1.append("<li><a data-tab=\"" + tabtit + "\" class=\"tab-view" + sel + "\" id=\"li_" 
			+ tabtit + "\" style=\"cursor: hand;\">" + tabtit + "</a></li>\n");
		}
		sb1.append("</ul>\n");
		sb1.append("<div id=\"tabCtrl\">\n");
			
		for (int i=0; i < tb.getTabCount(); i++) {
			String tabtit = tb.getTitleAt(i);
			String sel = i==0 ? " default-tab" : "";

			sb1.append("<div class=\"tab" + sel + "\" id=\"" + tabtit + "\">\n");
			Component com = tb.getComponentAt(i);
			if (com instanceof JPanel) {
				appendPanel((JPanel)com, sb1, sb2);
			}
			sb1.append("</div>\n");
		}
		sb1.append("</div>\n");
		sb1.append("<script>initTabView();</script>\n");
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
		String id = "textfield_" + tb.getName();
		variables.add(id);

		int rows = tb.getRowCount() + 1;
		int cols = tb.getColumnCount();

		StringBuffer s = new StringBuffer();
		for (int row = 0; row < rows; row++) {
			s.append("<asp:TableRow>\n");
			for (int col = 0; col < cols; col++) {
				if (row == 0) {
					s.append(
						"<asp:TableCell " + "style=\"" + "height:18px; " + "background:#DDDDDD; "
						+ "font-family:Arial; " + "font-size:11px; " + "font-weight:bold; "
						+ "font-style:normal;\">" + tb.getColumnName(col) + "</asp:TableCell>\n"
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
						"<asp:TableCell " + "style=\"" + "height:18px; " + "background:#fff; "
						+ getFont(c, true) + "\">" + tb.getValueAt(row - 1, col) + "</asp:TableCell>\n"
					);
				}
			}
			s.append("</asp:TableRow>\n");
		}

		sb.append(
			"<div class=\"wstable\" style=\"empty-cells:show; border-collapse:collapse;\">\n"
			+ "<asp:Table runat=\"server\" id=\"table_" + id + "\" BorderWidth=\"1\" GridLines=\"Both\" " 
			+ "style=\"" + getPosition(co, rel, 2, 2) + getDimension(co, 0, 0, 0, 0)
			+ "\"\n>"
			+ s + "</asp:Table>\n" + "</div>\n"
		);
	}

	/**
	 * @param
	 *
	 * @exception
	 *
	 * @see
	 */
	private void appendTree(Component co, StringBuffer sb, boolean rel) {

		JTree tr = (JTree) co;
		String tid = "textfield_" + tr.getName();
		variables.add(tid);

		sb.append(
			"<div class=\"wsxtree\" style=\"" + "background:#ffffff; " + getPosition(co, rel, 2, 2)
			+ getFont(co, false) + " white-space:nowrap;\">\n" 
			+ "<asp:TreeView runat=\"server\" id=\"" + tid + "\" ShowLines=\"true\" ShowExpandCollapse=\"true\" ExpandImageUrl=\"closed.gif\" CollapseImageUrl=\"open.gif\">\n"
		);

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) tr.getModel().getRoot();
		sb.append("<Nodes>\n");
		appendTreeRec(root, sb);

		sb.append("</Nodes>\n</asp:TreeView>\n</div>\n");
	}

	private void appendTreeRec(DefaultMutableTreeNode node, StringBuffer sb) {

		String o = node.getUserObject().toString();
		
		sb.append("<asp:TreeNode Text=\"" + o + "\" Value=\"" + o + "\"");

		if (node.isLeaf()) {
			sb.append("/>\n");
			return;
		}
		sb.append(">\n");

		Enumeration df = node.children();

		while (df.hasMoreElements()) {
			DefaultMutableTreeNode no = (DefaultMutableTreeNode) df.nextElement();
			appendTreeRec(no, sb);
		}
		sb.append("</asp:TreeNode>\n");
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
			+ getPosition(co, false, 0, 0) + getDimension(co, 20, 20, 0, 0) + " overflow:auto;\">\n"
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

	private String getDimension(Component co, int xo, int yo, int xm, int ym) {
		Rectangle r = co.getBounds();
		return "width:" + (r.width + xo - xm) + "px; " + "height:" + (r.height + yo - ym) + "px; ";
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

