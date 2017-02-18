package sumo;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import sumo.SumoRadar.Distance;
import sumo.SumoRadar.Heading;
import sumo.SumoRadar.SumoRadarListener;

public class SumoPilot implements SumoRadarListener {

	private SumoRadar radar;
	//DifferentialPilot robot;
	
	public SumoPilot(SumoRadar radar) {
		this.radar = radar;
		this.radar.addListener(this);
		/*
		robot = new DifferentialPilot(23.7, 130, Motor.C, Motor.B, true);
		robot.setRotateSpeed(15);
		robot.setTravelSpeed(300);
		*/
		
		Motor.C.setSpeed(1000);
		Motor.B.setSpeed(1000);
	}

	@Override
	public void onChange(SumoRadar radar) {
		if (radar.heading == Heading.UNKNOWN) {
			//robot.setRotateSpeed(45);
			//robot.stop();
			speed(50);
			if (radar.lastHeading == Heading.LEFT) {
				turnLeft();
			} else {
				turnRight();
			}
			return;
		}
		
		if (radar.distance == Distance.TOUCH) {
			stop();
			return;
		}
		
		if (radar.distance == Distance.NEAR) {
			speed(50);
			if (radar.heading == Heading.LEFT) {
				turnLeft();
			} else if (radar.heading == Heading.RIGHT) {
				turnRight();
			} else {
				stop();
			}
			return;
		}

		int turnRate = radar.MAX_RANGE * radar.diffCm / (radar.distanceCm + 1);
		turnRate = Math.min(turnRate, 90);
		turnRate = Math.max(turnRate, 30);

		speed(radar.diffCm);
		if (radar.heading == Heading.RIGHT) {
			steer(-turnRate);
		} else if (radar.heading == Heading.LEFT) {
			steer(turnRate);
		} else {
			steer(0);
		}
	}

	private void steer(int i) {
		if (i < 0) {
			Motor.C.backward();
			Motor.B.flt();
		} else if (i > 0){
			Motor.C.flt();
			Motor.B.backward();
		} else {
			Motor.C.backward();
			Motor.B.backward();
		}
	}

	private void stop() {
		Motor.B.stop();
		Motor.C.stop();
	}

	private void turnRight() {
		Motor.C.backward();
		Motor.B.forward();
	}

	private void turnLeft() {
		Motor.C.forward();
		Motor.B.backward();
	}
	
	private void speed(int speed) {
		Motor.C.setSpeed(speed);
		Motor.B.setSpeed(speed);
	}
	
}
