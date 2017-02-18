package sumo;

import lejos.nxt.Button;

public class Sumo2017 {

	public static void main(String[] args) {
		//Data data = SumoGui.show();
		//LCD.drawString("got " + data.angle, 0, 0);
		
		SumoRadar radar = new SumoRadar();
		SumoPilot pilot = new SumoPilot(radar);
		
		SumoRadarDisplay.show(radar);
		//SumoRadarSound.alert(radar);
		
		radar.start();
		
		Button.waitForAnyPress();
		
		radar.stop();
	}

}
