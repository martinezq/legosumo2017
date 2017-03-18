package sumo.behavior;

import javax.microedition.lcdui.Graphics;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.robotics.subsumption.Behavior;
import sumo.data.SumoSettings;
import sumo.gui.SumoRadarDisplay;

public class StartupDelayBehavior implements Behavior {

	private int delay;
	private boolean completed = false;
	
	public StartupDelayBehavior(SumoSettings settings) {
		delay = settings.startupDelay;
	}
	
	@Override
	public boolean takeControl() {
		return !completed;
	}

	@Override
	public void action() {
		if (delay <= 0) {
			completed = true;
			return;
		}
		
		Graphics g = new Graphics();
		
		SumoRadarDisplay.hide();
		
		g.clear();
		g.drawString("Press any key to start", 0, 0, 0);
		
		Button.waitForAnyPress();
		
		g.clear();
		
		int leftMs = delay * 1000;
				
		try {
			while(leftMs > 0) {
				long startMs = System.currentTimeMillis();
				g.clear();
				g.drawString(Integer.toString(Math.round(leftMs / 1000.0f)), 0, 0, 0);
				if (leftMs > 1000) {
					Thread.sleep(1000);
					Sound.beep();
				} else {
					Thread.sleep(leftMs);
					Sound.beep();
				}
				leftMs -= (System.currentTimeMillis() - startMs);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		completed = true;
	}

	@Override
	public void suppress() {
		// not supported
	}

}
