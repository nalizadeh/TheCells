package org.nalizadeh.designer.util.lf;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class SoneTableHeaderUI extends BasicTableHeaderUI {

    public static ComponentUI createUI(JComponent h) {
        return new SoneTableHeaderUI();
    }

    public void installUI(JComponent c) {
	super.installUI(c);
        header.setDefaultRenderer(new SoneTableCellRenderer());
    }

    public static class SoneTableCellRenderer extends JButton implements TableCellRenderer {

	private int column;

    	public Component getTableCellRendererComponent(
		JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		setText((String) value);
                this.column = column;

		return this;
	}

	public Dimension getPreferredSize() {
          return new Dimension(100,18);
        }
    }

}
