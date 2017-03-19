package sumo.behavior;

import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import sumo.data.SumoSettings;

public class BorderBehavior implements Behavior {

	private LightSensor sensor = new LightSensor(SensorPort.S2);
	private int borderValue;
	private boolean suppressed = false;
	private DifferentialPilot robot;
	
	public BorderBehavior(final DifferentialPilot robot, final SumoSettings settings) {
		Delay.msDelay(50);
		this.borderValue = sensor.getNormalizedLightValue() + settings.borderValue;
		this.robot = robot;
	}
	
	@Override
	public boolean takeControl() {
		return sensor.getNormalizedLightValue() > borderValue;
	}

	@Override
	public void action() {
		this.suppressed = false;
		setup();
		robot.backward();
		waitDistance(20);
		
		if (!suppressed) {
			robot.rotate(180, true);
			while (robot.isMoving() && !suppressed) {
				Thread.yield();
			}
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
