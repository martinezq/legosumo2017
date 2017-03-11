package sumo;

import data.SumoSettings;
import gui.SumoSettingsGui;

public class Sumo2017Conf {
	
	public static void main(String[] args) {
		SumoSettings settings = SumoSettings.read();
		SumoSettingsGui.show(settings);
	}
}
