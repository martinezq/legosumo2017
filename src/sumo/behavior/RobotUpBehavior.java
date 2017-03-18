package sumo.behavior;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;
import sumo.data.SumoSettings;

public class RobotUpBehavior extends RadarDrivenBehavior {

	TouchSensor touch = new TouchSensor(SensorPort.S1);
	DifferentialPilot robot;
	
	private final int turnRatio1;
	private final int turnDistance1;
	private final int turnRatio2;
	private final int turnDistance2;
	
	public RobotUpBehavior(SumoRadar radar, DifferentialPilot robot, SumoSettings settings) {
		super(radar, settings);
		turnRatio1 = settings.escapeTurnRatio1;
		turnDistance1 = settings.escapeTurnDistance1;
		turnRatio2 = settings.escapeTurnRatio2;
		turnDistance2 = settings.escapeTurnDistance2;
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		return !touch.isPressed();
	}

	@Override
	public void action() {
		suppressed = false;
		
		setup();
		int error = radar.getError();
		int direction;
		
		if (error == SumoRadar.ERR_NOT_FOUND || error == 0) {
			direction = Math.random() > 0.5 ? 1 : -1;
		} else {
			direction = -Math.round(Math.signum(radar.getError()));
		}
		if (!suppressed) {
			robot.steerBackward(direction * turnRatio1);
			waitDistance(turnDistance1);
		}
		
		if (!suppressed) {
			robot.reset();
			robot.steerBackward(-direction * turnRatio2);
			waitDistance(turnDistance2);
		}
		
	}
	
	private void setup() {
		robot.setTravelSpeed(robot.getMaxTravelSpeed());
		robot.setRotateSpeed(robot.getMaxRotateSpeed());
		robot.setAcceleration(100000);		
		robot.reset();
	}
	
	private void waitDistance(int distance) {
		while(Math.abs(robot.getMovementIncrement()) < distance && !suppressed) {
			Thread.yield();
		}		
	}

}
