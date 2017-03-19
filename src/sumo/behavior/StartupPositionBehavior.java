package sumo.behavior;

import lejos.nxt.LCD;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import sumo.data.SumoSettings;

public class StartupPositionBehavior implements Behavior {

	private int angle;
	private int distance;
	private boolean completed = false;
	private DifferentialPilot robot;
	private boolean suppressed = false;
	
	public StartupPositionBehavior(DifferentialPilot robot, SumoSettings settings) {
		angle = settings.startupAngle;
		distance = settings.startupDistance;
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
		robot.setTravelSpeed(robot.getMaxTravelSpeed());
		robot.reset();
		
		LCD.drawInt(angle, 0, 0);
		LCD.drawInt(distance, 0, 1);
		
		robot.rotate(angle, true);
		
		while(!suppressed && robot.isMoving()) {
			Thread.yield();
		}
		
		if (!suppressed) {
			robot.forward();
			waitDistance(distance);
		}
		
		completed = true;
	}

	@Override
	public void suppress() {
		suppressed = true;
	}
	
	private void waitDistance(int distance) {
		while(Math.abs(robot.getMovementIncrement()) < distance && !suppressed) {
			Thread.yield();
		}		
	}
	
}
