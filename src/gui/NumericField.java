package gui;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.Screen;

public class NumericField extends Item {

	private int value, min, max, step;
	
	public NumericField(String label, int value, int min, int max, int step) {
		super();
		this.label = label;
		this.value = value;
		this.min = min;
		this.max = max;
		this.step = step;
		
		if (label != null) {
			minWidth = (label.length() * Display.CHAR_WIDTH);
			minHeight = Display.CHAR_HEIGHT;
		}

		String text = Integer.toString(value);
		
		if ((minWidth + text.length() * Display.CHAR_WIDTH) < Display.SCREEN_WIDTH) {
			// Append to current line
			minWidth += (text.length() * Display.CHAR_WIDTH);
		} else {
			minWidth = Math.max(minWidth, text.length() * Display.CHAR_WIDTH);
			minHeight += Display.CHAR_HEIGHT;
		}
	}
	
	@Override
	protected void keyPressed(int keyCode) {
		if (keyCode == Screen.KEY_RIGHT) {
			if (value < max) {
				value += step;
				value = Math.min(value, max);
			}
			repaint();
		} else if (keyCode == Screen.KEY_LEFT) {
			if (value > min) {
				value -= step;
				value = Math.max(value, min);
			}
			repaint();
		} else if ((keyCode == Screen.KEY_BACK) || (keyCode == Screen.KEY_ENTER)) {
			notifyStateChanged();
		}
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public boolean isInteractive() {
		return true;
	}
	
	@Override
	public void paint(Graphics g, int x, int y, int w, int h, boolean selected) {
		if (label != null) {
			g.drawString(label, x, y, 0 );
			if (h <= Display.CHAR_HEIGHT) {
				x += label.length() * Display.CHAR_WIDTH;
			} else {
				y += Display.CHAR_HEIGHT;
			}
		}

		g.drawString(Integer.toString(value), x, y, 0, selected);
	}

}
