package behavior;

import data.SumoSettings;
import lejos.robotics.navigation.DifferentialPilot;
import util.SumoAttackPilot;
import util.SumoRadar;

public class AttackBehavior extends RadarDrivenBehavior {

	SumoAttackPilot pilot;
	DifferentialPilot robot;
	
	public AttackBehavior(SumoRadar radar, DifferentialPilot robot, SumoSettings settings) {
		super(radar);
		this.robot = robot;
		pilot = new SumoAttackPilot(radar, robot, settings);
		pilot.disable();
	}

	@Override
	public boolean takeControl() {
		return radar.objectDetected();
	}
	
	@Override
	public void action() {
		suppressed = false;

		pilot.setupRobot();
		pilot.reset();
		pilot.enable();
		
		while(radar.objectDetected() && !suppressed) {
			Thread.yield();
		}
		
		pilot.disable();
	}

}
