package gui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Screen;

import data.SumoSettings;

public class SumoPositionScreen extends Screen {
	
	private static int MX = 50;
	private static int MY = 35;
	private static int LEN = 25;
	
	private int step = 15;
	private int angle = 0;

	public SumoPositionScreen(SumoSettings settings) {
		angle = settings.startupAngle;
	}
	
	@Override
	protected void keyPressed(int keyCode) {
		if (keyCode == KEY_RIGHT) {
			angle += step;
			normalizeAngle();
			repaint();
		} else if (keyCode == KEY_LEFT) {
			angle -= step;
			normalizeAngle();
			repaint();
		} else {
			callCommandListener();
		}
	}
	
	private void normalizeAngle() {
		while(angle < 0) angle += 360;
		angle = angle % 360;		
	}

	@Override
	protected void paint(Graphics g) {
		g.drawString("Startup angle:", 0, 0, 0);
		g.setStrokeStyle(Graphics.DOTTED);
		g.drawArc(MX - LEN, MY - LEN, 2 * LEN, 2 * LEN, 0, 360);
		double rad = Math.toRadians(-angle);
		int x = MX + (int)Math.round(LEN * Math.cos(rad));
		int y = MY + (int)Math.round(LEN * Math.sin(rad));
		g.setStrokeStyle(Graphics.SOLID);
		g.drawLine(MX, MY, x, y);
	}
	
	public int getValue() {
		return angle;
	}
	
	public void setValue(int angle) {
		this.angle = angle;
		normalizeAngle();
	}
}
