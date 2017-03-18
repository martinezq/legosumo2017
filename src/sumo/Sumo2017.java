package sumo;

import behavior.AttackBehavior;
import behavior.BorderBehavior;
import behavior.ProgramEndBehavior;
import behavior.RobotUpBehavior;
import behavior.SearchBehavior;
import behavior.StartupDelayBehavior;
import behavior.StartupPositionBehavior;
import data.SumoSettings;
import lejos.nxt.Motor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import util.SumoRadar;
import util.SumoRadarDisplay;

public class Sumo2017 {

	//private final static double STANDARD_TRACK_WHEEL_DELIMETER = 23.7;
	private final static double GEARED_20_12_TRACK_WHEEL_DELIMETER = 30.5;
	
	public static void main(String[] args) {
		//RConsole.open();
		SumoSettings settings = SumoSettings.read();
		action(settings);
		//RConsole.close();
	}
	
	private static void action(final SumoSettings settings) {
		SumoRadar radar = new SumoRadar(settings);
		DifferentialPilot robot = new DifferentialPilot(
				GEARED_20_12_TRACK_WHEEL_DELIMETER, 130, Motor.B, Motor.C);
		
		StartupDelayBehavior delay = new StartupDelayBehavior(settings);
		StartupPositionBehavior position = new StartupPositionBehavior(robot, settings);
		AttackBehavior attack = new AttackBehavior(radar, robot, settings);
		ProgramEndBehavior end = new ProgramEndBehavior();
		RobotUpBehavior up = new RobotUpBehavior(radar, robot, settings);
		SearchBehavior search = new SearchBehavior(radar, robot, settings);
		BorderBehavior border = new BorderBehavior(robot, settings);
		
		Behavior[] behaviorList = new Behavior[] { search, attack, border, up, position, delay, end };
		
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		
		radar.start();
		arbitrator.start();		
	}

}
