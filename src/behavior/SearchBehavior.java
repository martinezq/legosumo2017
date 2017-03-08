package behavior;

import lejos.robotics.navigation.DifferentialPilot;
import util.SumoRadar;

public class SearchBehavior extends RadarDrivenBehavior {

	protected DifferentialPilot robot;
	protected int direction = 1;
	
	public SearchBehavior(SumoRadar radar, DifferentialPilot robot) {
		super(radar);
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		return !radar.objectDetected();
	}

	@Override
	public void action() {
		suppressed = false;
		setup();
		direction = -direction;
		
		robot.steer(90 * direction);
		
		while(!radar.objectDetected() && !suppressed) {
			Thread.yield();
		}
	}

	private void setup() {
		robot.setTravelSpeed(100);
		robot.setRotateSpeed(30);
		robot.setAcceleration(100000);		
	}
}
