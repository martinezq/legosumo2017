package sumo;

import lejos.nxt.Sound;
import sumo.SumoRadar.Heading;
import sumo.SumoRadar.SumoRadarListener;

public class SumoRadarSound implements SumoRadarListener {
	
	public SumoRadarSound(final SumoRadar radar) {
		radar.addListener(this);
	}

	private static SumoRadarSound instance;
	
	public static SumoRadarSound alert(final SumoRadar radar) {
		if (instance == null) {
			instance = new SumoRadarSound(radar);
		}
		return instance;
	}
	
	@Override
	public void onChange(SumoRadar radar) {
		if (radar.heading == Heading.CENTER) {
			Sound.beep();
		} else if (radar.heading == Heading.UNKNOWN) {
			Sound.buzz();
		} else {
			Sound.beepSequenceUp();
		}
	}
	
}
