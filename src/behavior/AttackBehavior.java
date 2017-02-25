package behavior;

import lejos.robotics.navigation.DifferentialPilot;
import util.SumoAttackPilot;
import util.SumoRadar;

public class AttackBehavior extends RadarDrivenBehavior {

	SumoAttackPilot pilot;
	
	public AttackBehavior(SumoRadar radar, DifferentialPilot robot) {
		super(radar);
		pilot = new SumoAttackPilot(radar, robot);
		pilot.disable();
	}

	@Override
	public boolean takeControl() {
		return radar.objectDetected();
	}
	
	@Override
	public void action() {
		suppressed = false;

		pilot.enable();
		
		while(!suppressed) {
			Thread.yield();
		}
		
		pilot.disable();
	}

}
