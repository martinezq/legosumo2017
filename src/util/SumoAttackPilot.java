package util;

import lejos.robotics.navigation.DifferentialPilot;
import util.SumoRadar.SumoRadarListener;

public class SumoAttackPilot implements SumoRadarListener {

	private SumoRadar radar;
	private boolean enabled = true;
	
	public static class Config {
		private final int kp, ki, kd, maxSpeed;

		public Config(int kp, int ki, int kd, int maxSpeed) {
			super();
			this.kp = kp;
			this.ki = ki;
			this.kd = kd;
			this.maxSpeed = maxSpeed;
		}
		
	}

	// private static Config CONFIG_SLOW = new Config(150, 0, 200, 50);
	private static Config CONFIG_FAST = new Config(150, 0, 200, 200);
	
	private final Config config = CONFIG_FAST;
	
	private int lastError = 0;
	private long integral = 0;
	
	private DifferentialPilot robot;
	
	public SumoAttackPilot(SumoRadar radar, DifferentialPilot robot) {
		this.radar = radar;
		this.radar.addListener(this);
		
		this.robot = robot;
		
		setup();
	}
	
	private void setup() {
		robot.setTravelSpeed(config.maxSpeed);
		robot.setRotateSpeed(15);
		robot.setAcceleration(10000000);
		robot.setMinRadius(0);
	}

	@Override
	public void onChange(SumoRadar radar) {
		if (!enabled) {
			return;
		}
		
		int error = radar.error;
		
		if (error == SumoRadar.ERR_NOT_FOUND) {
			reset();
			robot.stop();
			//robot.rotateLeft();
			return;
		}
		
		if (error == SumoRadar.ERR_TOO_CLOSE) {
			robot.stop();
			reset();
			return;
		}
		
		long turnRateLong = config.kp * error + config.ki * integral + config.kd * (error - lastError);
		int turnRate = (int)(turnRateLong / 100);
		
		lastError = error;
		if (config.ki > 0) integral += error;

		robot.steer(turnRate);

	}
	
	public final void reset() {
		lastError = 0;
		integral = 0;
	}
	
	public void disable() {
		enabled = false;
	}
	
	public void enable() {
		enabled = true;
	}
	
}
