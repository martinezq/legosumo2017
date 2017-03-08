package behavior;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;
import util.SumoRadar;

public class RobotUpBehavior extends RadarDrivenBehavior {

	TouchSensor touch = new TouchSensor(SensorPort.S1);
	DifferentialPilot robot;
	
	public RobotUpBehavior(SumoRadar radar, DifferentialPilot robot) {
		super(radar);
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
		
		robot.steerBackward(direction * 90);
		waitDistance(350);
		
		robot.reset();
		
		robot.steerBackward(-direction * 150);
		waitDistance(40);
		
		end();
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
	
	private void end() {
		robot.stop();
	}

}
