package org.nalizadeh.designer.util.beans;

import java.awt.Component;
import java.awt.Graphics;

public class TestBean extends Component {
	
	private String name;
	private int type;
	
	public TestBean() {
		
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public int getType() {
		return type;
	}
	
	public void paint(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(getForeground());
		g.drawString("" + type, getWidth() / 2, getHeight() / 2);
	}
}
