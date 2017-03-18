package behavior;

import data.SumoSettings;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;
import util.SumoRadar;
import util.SumoRadarDisplay;

public class SearchBehavior extends RadarDrivenBehavior {

	protected DifferentialPilot robot;
	protected int direction = 1;
	
	private int speedPrc;
	private int turnRatio;
	
	public SearchBehavior(SumoRadar radar, DifferentialPilot robot, SumoSettings settings) {
		super(radar);
		this.robot = robot;
		this.speedPrc = settings.searchSpeed;
		this.turnRatio = settings.searchTurnRatio;
	}

	@Override
	public boolean takeControl() {
		return !objectDetectedSafe();
	}

	@Override
	public void action() {
		//RConsole.println("search action");
		suppressed = false;
		SumoRadarDisplay.show(radar);
		setup();
		direction = (int)Math.signum(radar.getLastError());
		
		robot.steer(turnRatio * direction);
		
		while(!suppressed) {
			Thread.yield();
		}
		
		SumoRadarDisplay.hide();
	}

	private void setup() {
		final int speed = (int)Math.round(robot.getMaxTravelSpeed() * speedPrc / 100);
		robot.setTravelSpeed(speed);
		robot.setAcceleration(100000);		
	}
}
