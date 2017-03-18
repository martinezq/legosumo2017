package sumo.behavior;

import lejos.robotics.navigation.DifferentialPilot;
import sumo.behavior.SumoRadar.SumoRadarListener;
import sumo.data.SumoSettings;

public class SumoAttackPilot implements SumoRadarListener {

	private SumoRadar radar;
	private boolean enabled = false;
	
	public static class Config {
		private final int kp, ki, kd, speedPrc;

		public Config(final SumoSettings settings) {
			super();
			this.kp = settings.pidP;
			this.ki = settings.pidI;
			this.kd = settings.pidD;
			this.speedPrc = settings.attackSpeed;
		}
		
	}
	
	private final Config config;
	
	private int lastError = 0;
	private long integral = 0;
	
	private DifferentialPilot robot;
	
	public SumoAttackPilot(SumoRadar radar, DifferentialPilot robot, SumoSettings settings) {
		this.config = new Config(settings);
		this.radar = radar;
		this.radar.addListener(this);
		
		this.robot = robot;
		
		setupRobot();
	}
	
	public void setupRobot() {
		final int speed = (int)Math.round(robot.getMaxTravelSpeed() * config.speedPrc / 100);
		robot.setTravelSpeed(speed);
		robot.setRotateSpeed(15);
		robot.setAcceleration(10000000);
		robot.setMinRadius(0);
		robot.reset();
	}

	@Override
	public void onChange(SumoRadar radar) {
		if (!enabled) {
			return;
		}
		
		int error = radar.getError();
		
		if (error == SumoRadar.ERR_NOT_FOUND) {
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
