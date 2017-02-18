package sumo;

import javax.microedition.lcdui.Graphics;

import sumo.SumoRadar.SumoRadarListener;

public class SumoRadarDisplay implements SumoRadarListener {

	private static SumoRadarDisplay instance;
	
	public static void show(SumoRadar radar) {
		if(instance == null) {
			instance = new SumoRadarDisplay(radar);
		}
		instance.draw();
	}
	
	private Graphics g = new Graphics();
	private SumoRadar radar;
	
	public SumoRadarDisplay(final SumoRadar radar) {
		this.radar = radar;
		this.radar.addListener(this);
	}

	@Override
	public void onChange(SumoRadar radar) {
		draw();
	}
	
	private void clear() {
		g.clear();
	}
	
	private static int CX = 5;
	private static int CY = 30;
	private static int EX = 95;
	private static int EY1 = 5;
	private static int EY2 = 55;
	
	private static int LENX = EX - CX;
	private static int LENY = EY2 - EY1;
	
	private void draw() {
		clear();
		drawScene();
		drawEnemy();
		drawArrow(5, 30);
	}
	
	private void drawScene() {
		g.setStrokeStyle(Graphics.DOTTED);
		g.drawLine(CX, CY, EX, EY1);
		g.drawLine(CX, CY, EX, CY);
		g.drawLine(CX, CY, EX, EY2);
		g.drawLine(EX, EY1, EX, EY2);
		
		g.drawString("" + radar.distanceLeftCm, 0, 0, 0);
		g.drawString("" + radar.distanceRightCm, 0, 60, Graphics.BOTTOM);

	}
	
	private void drawEnemy() {
		int rx1 = 10 + CX + (LENX * radar.distanceLeftCm / 100);
		int rx2 = 10 + CX + (LENX * radar.distanceRightCm / 100);
		
		int size1 = LENY * rx1 / LENX / 2;
		int size2 = LENY * rx2 / LENX / 2;
		
		g.setStrokeStyle(Graphics.SOLID);
		g.fillRect(rx1, CY - size1, 6, size1);
		g.fillRect(rx2, CY, 6, size2);		
	}
	
	private void drawArrow(int x, int y) {
		g.setStrokeStyle(Graphics.SOLID);
		int len = 20;
		if (radar.error < 0) {
			g.drawLine(x, y, x, y + len);
			g.drawLine(x, y + len, x + 5, y + len - 5);
			g.drawLine(x, y + len, x - 5, y + len - 5);
		} else if (radar.error > 0) {
			g.drawLine(x, y, x, y - len);
			g.drawLine(x, y - len, x + 5, y - len + 5);
			g.drawLine(x, y - len, x - 5, y - len + 5);

		}
	}
	
}
