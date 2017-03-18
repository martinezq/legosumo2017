package behavior;

import data.SumoSettings;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class BorderBehavior implements Behavior {

	private LightSensor sensor = new LightSensor(SensorPort.S2);
	private int borderValue;
	private boolean suppressed = false;
	private DifferentialPilot robot;
	
	public BorderBehavior(final DifferentialPilot robot, final SumoSettings settings) {
		this.borderValue = settings.borderValue;
		this.robot = robot;
	}
	
	@Override
	public boolean takeControl() {
		return sensor.getLightValue() > borderValue;
	}

	@Override
	public void action() {
		this.suppressed = false;
		setup();
		robot.backward();
		waitDistance(20);
		
		if (!suppressed) {
			// TODO this cannot be suppressed - change to non-blocking
			robot.rotate(180);
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
	
	@Override
	public void suppress() {
		this.suppressed = true;
	}

}
