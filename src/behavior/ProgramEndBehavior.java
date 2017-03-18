package behavior;

import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;
import lejos.robotics.subsumption.Behavior;

public class ProgramEndBehavior implements Behavior {

	@Override
	public boolean takeControl() {
		return Button.ESCAPE.isDown();
	}
	
	@Override
	public void action() {
		//RConsole.close();
		System.exit(0);
	}

	@Override
	public void suppress() {
	}

}
