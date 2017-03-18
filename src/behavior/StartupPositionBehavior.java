package behavior;

import data.SumoSettings;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class StartupPositionBehavior implements Behavior {

	private int angle;
	private boolean completed = false;
	private DifferentialPilot robot;
	private boolean suppressed = false;
	
	public StartupPositionBehavior(DifferentialPilot robot, SumoSettings settings) {
		angle = settings.startupAngle;
		this.robot = robot;
	}
	
	@Override
	public boolean takeControl() {
		return !completed;
	}

	@Override
	public void action() {
		suppressed = false;
		
		robot.setRotateSpeed(robot.getMaxRotateSpeed());
		
		robot.rotate(angle, false);
		
		while(!suppressed && robot.isMoving()) {
			Thread.yield();
		}
		
		completed = true;
		robot.stop();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
	
}
