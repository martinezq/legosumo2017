package sumo.behavior;

import lejos.nxt.Motor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;
import sumo.data.SumoSettings;
import sumo.gui.SumoRadarDisplay;

public class AttackBehavior extends RadarDrivenBehavior {

	private SumoAttackPilot pilot;
	private int wheelSpeedPrc;
	
	public AttackBehavior(SumoRadar radar, DifferentialPilot robot, SumoSettings settings) {
		super(radar);
		pilot = new SumoAttackPilot(radar, robot, settings);
		pilot.disable();
		wheelSpeedPrc = settings.attackWheelSpeed;
	}

	@Override
	public boolean takeControl() {
		return objectDetected();
	}
	
	@Override
	public void action() {
		//RConsole.println("attack action");
		suppressed = false;
		SumoRadarDisplay.show(radar);

		pilot.setupRobot();
		pilot.reset();
		pilot.enable();

		final int speed = (int)Math.round(Motor.A.getMaxSpeed() * wheelSpeedPrc / 100);
		
		Motor.A.setSpeed(speed);
		Motor.A.forward();
		
		while(objectDetected() && !suppressed) {
			Thread.yield();
		}
		
		pilot.disable();
		Motor.A.flt();
		SumoRadarDisplay.hide();
	}

}
