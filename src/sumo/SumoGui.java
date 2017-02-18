package sumo;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.Button;

public class SumoGui {

	static class Data {
		public int angle;
	}
	
	private static SumoGui instance;
	
	public static Data show() {
		if(instance == null) {
			instance = new SumoGui();
		}
		instance.reset();
		instance.clear();
		instance.draw();
		instance.loop();
		
		Data data = new Data();
		data.angle = instance.angle;
		
		return data;
	}
	
	private static int MX = 50;
	private static int MY = 35;
	private static int LEN = 25;
	
	private Graphics g = new Graphics();
	
	private int step;
	private int angle;
	
	private void clear() {
		g.clear();
	}
	
	private void reset() {
		angle = 180;
		step = 15;
	}
	
	private void draw() {
		g.drawString("Enemy Heading", 0, 0, 0);
		g.setStrokeStyle(Graphics.DOTTED);
		g.drawArc(MX - LEN, MY - LEN, 2 * LEN, 2 * LEN, 0, 360);
		double rad = Math.toRadians(angle);
		int x = MX + (int)Math.round(LEN * Math.cos(rad));
		int y = MY + (int)Math.round(LEN * Math.sin(rad));
		g.setStrokeStyle(Graphics.SOLID);
		g.drawLine(MX, MY, x, y);
	}
	
	private void loop() {
		while(true) {
			int button = Button.waitForAnyPress();
			if(button == Button.ID_ENTER) break;
			if(button == Button.ID_ESCAPE) reset();
			if(button == Button.ID_LEFT) angle -= step;
			if(button == Button.ID_RIGHT) angle += step;
			while(angle < 0) angle += 360;
			angle = angle % 360;
			clear();
			draw();
		}
		clear();
	}
	
}
