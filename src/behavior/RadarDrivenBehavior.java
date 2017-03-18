package behavior;

import lejos.robotics.subsumption.Behavior;
import util.SumoRadar;

public abstract class RadarDrivenBehavior implements Behavior {

	protected boolean suppressed = false;
	protected final SumoRadar radar;
	
	public RadarDrivenBehavior(final SumoRadar radar){
		this.radar = radar;
	}
	
	@Override
	public void suppress() {
		suppressed = true;
	}
	
	protected boolean objectDetected() {
		// synchronized (radar) {
		return radar.objectDetected();
		// }
	}

}
