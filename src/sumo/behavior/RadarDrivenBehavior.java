package sumo.behavior;

import lejos.robotics.subsumption.Behavior;
import sumo.data.SumoSettings;

public abstract class RadarDrivenBehavior implements Behavior {

	protected boolean suppressed = false;
	protected final SumoRadar radar;
	protected boolean showRadar;
	
	public RadarDrivenBehavior(final SumoRadar radar, final SumoSettings settings) {
		this.radar = radar;
		this.showRadar = settings.showRadar == 1;
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
