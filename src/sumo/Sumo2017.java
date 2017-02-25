package sumo;

import behavior.AttackBehavior;
import behavior.ProgramEndBehavior;
import behavior.RobotUpBehavior;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import util.SumoRadar;

public class Sumo2017 {

	public static void main(String[] args) {
		//Data data = SumoGui.show();
		
		SumoRadar radar = new SumoRadar();
		DifferentialPilot robot = new DifferentialPilot(23.7, 130, Motor.B, Motor.C, true);
		
		AttackBehavior attack = new AttackBehavior(radar, robot);
		ProgramEndBehavior end = new ProgramEndBehavior();
		RobotUpBehavior up = new RobotUpBehavior(radar, robot);
		
		Behavior[] behaviorList = new Behavior[] { attack, up, end };
		
		Arbitrator arbitrator = new Arbitrator(behaviorList);
		
		radar.start();
		arbitrator.start();
	}

}
