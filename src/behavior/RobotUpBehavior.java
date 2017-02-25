package behavior;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
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
		
		robot.travelArc(100, -1000, true);
		waitDistance(300);

		robot.travelArc(-60, -1000, true);
		waitDistance(80);
		
		end();
	}
	
	private void setup() {
		robot.setTravelSpeed(200);
		robot.setRotateSpeed(360);
		robot.setAcceleration(100000);		
	}
	
	private void waitDistance(int distance) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			return;
		}
		while(Math.abs(robot.getMovementIncrement()) < distance && !suppressed) {
			Thread.yield();
		}		
	}
	
	private void end() {
		robot.stop();
	}

}
