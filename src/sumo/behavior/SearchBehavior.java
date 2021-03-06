package sumo.behavior;

import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;
import sumo.data.SumoSettings;
import sumo.gui.SumoRadarDisplay;

public class SearchBehavior extends RadarDrivenBehavior {

	protected DifferentialPilot robot;
	protected int direction = 1;
	
	private int speedPrc;
	private int turnRatio;
	
	public SearchBehavior(SumoRadar radar, DifferentialPilot robot, SumoSettings settings) {
		super(radar, settings);
		this.robot = robot;
		this.speedPrc = settings.searchSpeed;
		this.turnRatio = settings.searchTurnRatio;
	}

	@Override
	public boolean takeControl() {
		return !objectDetected();
	}

	@Override
	public void action() {
		//RConsole.println("search action");
		suppressed = false;
		
		if (showRadar) {
			SumoRadarDisplay.show(radar);
		}
		
		setup();
		direction = (int)Math.signum(radar.getLastError());
		
		if (direction == 0) {
			direction = Math.random() > 0.5 ? 1 : -1;
		}
		
		robot.steer(turnRatio * direction);
		
		while(!suppressed) {
			Thread.yield();
		}
		
		if (showRadar) {
			SumoRadarDisplay.hide();
		}
	}

	private void setup() {
		final int speed = (int)Math.round(robot.getMaxTravelSpeed() * speedPrc / 100);
		robot.setTravelSpeed(speed);
		robot.setAcceleration(100000);	
		robot.reset();
	}
}
