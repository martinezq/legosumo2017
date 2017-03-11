package sumo;

import behavior.AttackBehavior;
import behavior.ProgramEndBehavior;
import behavior.RobotUpBehavior;
import behavior.SearchBehavior;
import data.SumoSettings;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import util.SumoRadar;
import util.SumoRadarDisplay;

public class Sumo2017 {

	//private final static double STANDARD_TRACK_WHEEL_DELIMETER = 23.7;
	private final static double GEARED_20_12_TRACK_WHEEL_DELIMETER = 39.5;
	
	public static void main(String[] args) {
		SumoSettings settings = SumoSettings.read();
		action(settings);
	}
	
	private static void action(final SumoSettings settings) {
		SumoRadar radar = new SumoRadar(settings);
		SumoRadarDisplay.show(radar);
		DifferentialPilot robot = new DifferentialPilot(
				GEARED_20_12_TRACK_WHEEL_DELIMETER, 130, Motor.B, Motor.C);
		
		AttackBehavior attack = new AttackBehavior(radar, robot, settings);
		ProgramEndBehavior end = new ProgramEndBehavior();
		RobotUpBehavior up = new RobotUpBehavior(radar, robot, settings);
		SearchBehavior search = new SearchBehavior(radar, robot);
		
		Behavior[] behaviorList = new Behavior[] { search, attack, up, end };
		
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		
		radar.start();
		arbitrator.start();		
	}

}
