package sumo;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import sumo.SumoRadar.SumoRadarListener;

public class SumoPilot implements SumoRadarListener {

	private SumoRadar radar;

	private static int KP = 100; //KP < KD
	private static int KI = 1;
	private static int KD = 100;
	
	private static int MAX_SPEED = 200;
	
	private int lastError = 0;
	private long integral = 0;
	
	private DifferentialPilot robot = new DifferentialPilot(23.7, 130, Motor.C, Motor.B, true);
	
	public SumoPilot(SumoRadar radar) {
		this.radar = radar;
		this.radar.addListener(this);
		
		robot.setTravelSpeed(MAX_SPEED);
		robot.setRotateSpeed(15);
	}

	@Override
	public void onChange(SumoRadar radar) {
		int error = radar.error;
		
		if (error == SumoRadar.ERR_NOT_FOUND) {
			reset();
			robot.rotateLeft();
			return;
		}
		
		if (error == SumoRadar.ERR_TOO_CLOSE) {
			robot.stop();
			reset();
			return;
		}
		
		long turnRateLong = KP * error + KI * integral + KD * (error - lastError);
		int turnRate = (int)(turnRateLong / 100);
		
		lastError = error;
		integral += error;

		robot.steer(turnRate);

	}
	
	public final void reset() {
		lastError = 0;
		integral = 0;
	}
	
}
