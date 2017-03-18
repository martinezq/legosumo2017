package sumo.gui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;

import sumo.data.SumoSettings;

public class SumoSettingsGui implements CommandListener {

	private final static Command COMMAND_EXIT = new Command(1, Command.BACK, 1);
	private final static Command COMMAND_BACK = new Command(0, Command.BACK, 0);
	
	private final static String GENERAL = "General";
	private final static String STARTUP_POSITION = "Startup position";
	private final static String SEARCH = "Search";
	private final static String ATTACK = "Attack";
	private final static String DEFENSE = "Defense";
	private final static String SAVE = "Save";
	private final static String RESTORE_DEFAULT = "Restore default";
	
	private SumoSettings settings;
	
	private Display display;
	
	private List menu;
	
	private Form formGeneral;
	private Form formAttack;
	private Form formDefense;
	private Form formSearch;	
	
	private NumericField showRadarField;
	
	private SumoPositionScreen positionScreen;
	private NumericField startupDelayField;
	
	private NumericField attackSpeedField;
	private NumericField pidPField;
	private NumericField pidIField;
	private NumericField pidDField;
	private NumericField attackWheelSpeedField;
	
	private NumericField searchRangeField;
	private NumericField searchSpeedField;
	private NumericField searchTurnRatioField;
	
	private NumericField escapeTurnRatio1Field;
	private NumericField escapeTurnDistance1Field;
	private NumericField escapeTurnRatio2Field;
	private NumericField escapeTurnDistance2Field;
	
	private NumericField borderValueField;
	
	public static void show(final SumoSettings settings) {
		final SumoSettingsGui gui = new SumoSettingsGui(settings);
		gui.display();
	}

	public SumoSettingsGui(final SumoSettings settings) {
		this.settings = settings;
		
		menu = new List("Settings:", Choice.IMPLICIT);
		
		menu.append(GENERAL, null);
		menu.append(STARTUP_POSITION, null);
		menu.append(SEARCH, null);
		menu.append(ATTACK, null);
		menu.append(DEFENSE, null);
		
		menu.append(SAVE, null);
		menu.append(RESTORE_DEFAULT, null);
		
		menu.setCommandListener(this);
		menu.addCommand(COMMAND_EXIT);
		
		positionScreen = new SumoPositionScreen(settings);
		positionScreen.setCommandListener(this);
		positionScreen.addCommand(COMMAND_BACK);
		
		formGeneral = new Form("General settings:");
		formSearch = new Form("Search settings:");
		formAttack = new Form("Attack settings:");
		formDefense = new Form("Defense settings:");
		
		showRadarField =    new NumericField("show rad.: ", settings.showRadar, 0, 1, 1);
				
		startupDelayField =    new NumericField("delay[s]:  ", settings.startupDelay, 0, 5, 1);
		
		searchRangeField =     new NumericField("range[cm]: ", settings.searchRange, 10, 200, 10);
		searchSpeedField =     new NumericField("speed[%]:  ", settings.searchSpeed, 10, 100, 10);
		searchTurnRatioField = new NumericField("turn rat.: ", settings.searchTurnRatio, 10, 200, 10);
		
		attackSpeedField = new NumericField("speed[%] : ", settings.attackSpeed, 10, 100, 10);
		pidPField =  new NumericField("pid P:     ", settings.pidP, 0, 500, 50);
		pidIField =  new NumericField("pid I:     ", settings.pidI, 0, 50, 10);
		pidDField =  new NumericField("pid D:     ", settings.pidD, 0, 1000, 50);
		attackWheelSpeedField = new NumericField("wheel[%] : ", settings.attackWheelSpeed, 10, 100, 10);
		
		escapeTurnRatio1Field =     new NumericField("etr1:      ", settings.escapeTurnRatio1, 0, 200, 10);
		escapeTurnDistance1Field =  new NumericField("etd1[cm]:  ", settings.escapeTurnDistance1, 0, 1000, 10);
		escapeTurnRatio2Field =     new NumericField("etr2:      ", settings.escapeTurnRatio2, 0, 200, 10);
		escapeTurnDistance2Field =  new NumericField("etd2[cm]:  ", settings.escapeTurnDistance2, 0, 1000, 10);

		borderValueField = new NumericField("border[cl]:", settings.borderValue, 0, 100, 2);
		
		formGeneral.append(showRadarField);
		formGeneral.append(startupDelayField);
		
		formGeneral.addCommand(COMMAND_BACK);
		formGeneral.setCommandListener(this);
		
		formSearch.append(searchRangeField);
		formSearch.append(searchSpeedField);
		formSearch.append(searchTurnRatioField);

		formSearch.addCommand(COMMAND_BACK);
		formSearch.setCommandListener(this);
		
		formAttack.append(attackSpeedField);
		formAttack.append(pidPField);
		formAttack.append(pidIField);
		formAttack.append(pidDField);
		formAttack.append(attackWheelSpeedField);
		
		formAttack.addCommand(COMMAND_BACK);
		formAttack.setCommandListener(this);
		
		formDefense.append(escapeTurnRatio1Field);
		formDefense.append(escapeTurnDistance1Field);
		formDefense.append(escapeTurnRatio2Field);
		formDefense.append(escapeTurnDistance2Field);
		formDefense.append(borderValueField);

		formDefense.addCommand(COMMAND_BACK);
		formDefense.setCommandListener(this);
	}

	void display() {
		display = Display.getDisplay();
		display.setCurrent(menu);
		display.show(true);
	}
	
	private void save() {
		settings.showRadar = showRadarField.getValue();
		
		settings.startupDelay = startupDelayField.getValue();
		settings.startupAngle = positionScreen.getValue();
		
		settings.searchRange = searchRangeField.getValue();
		settings.searchSpeed = searchSpeedField.getValue();
		settings.searchTurnRatio = searchTurnRatioField.getValue();
		
		settings.attackSpeed = attackSpeedField.getValue();
		settings.pidP = pidPField.getValue();
		settings.pidI = pidIField.getValue();
		settings.pidD = pidDField.getValue();
		settings.attackWheelSpeed = attackWheelSpeedField.getValue();
		
		settings.escapeTurnRatio1 = escapeTurnRatio1Field.getValue();
		settings.escapeTurnDistance1 = escapeTurnDistance1Field.getValue();
		settings.escapeTurnRatio2 = escapeTurnRatio2Field.getValue();
		settings.escapeTurnDistance2 = escapeTurnDistance2Field.getValue();
		
		settings.borderValue = borderValueField.getValue();
		
		settings.save();
	}
	
	private void init() {
		showRadarField.setValue(settings.showRadar);
		
		startupDelayField.setValue(settings.startupDelay);
		positionScreen.setValue(settings.startupAngle);
		
		searchRangeField.setValue(settings.searchRange);
		searchSpeedField.setValue(settings.searchSpeed);
		searchTurnRatioField.setValue(settings.searchTurnRatio);
		
		attackSpeedField.setValue(settings.attackSpeed);
		pidPField.setValue(settings.pidP);
		pidIField.setValue(settings.pidI);
		pidDField.setValue(settings.pidD);
		attackWheelSpeedField.setValue(settings.attackWheelSpeed);
		
		escapeTurnRatio1Field.setValue(settings.escapeTurnRatio1);
		escapeTurnDistance1Field.setValue(settings.escapeTurnDistance1);
		escapeTurnRatio2Field.setValue(settings.escapeTurnRatio2);
		escapeTurnDistance2Field.setValue(settings.escapeTurnDistance2);
		
		borderValueField.setValue(settings.borderValue);
	}
	
	@Override
	public void commandAction(Command c, Displayable d) {
		if (c == COMMAND_BACK) {
			display.setCurrent(menu);
			return;
		}
		
		if (c == COMMAND_EXIT) {
			display.quit();
			return;
		}
		
		if (d == menu) {
			final String option = menu.getString(menu.getSelectedIndex());
			if (GENERAL.equals(option)) {
				display.setCurrent(formGeneral);
			} else if (STARTUP_POSITION.equals(option)) {
				display.setCurrent(positionScreen);
			} else if (SEARCH.equals(option)) {
				display.setCurrent(formSearch);
			} else if (ATTACK.equals(option)) {
				display.setCurrent(formAttack);
			} else if (DEFENSE.equals(option)){
				display.setCurrent(formDefense);
			} else if (SAVE.equals(option)) {
				save();
			} else if (RESTORE_DEFAULT.equals(option)) {
				settings = new SumoSettings();
				init();
			}
		}
	}
}
