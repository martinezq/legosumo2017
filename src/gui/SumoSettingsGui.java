package gui;

import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;

import data.SumoSettings;

public class SumoSettingsGui implements CommandListener {

	private final static Command COMMAND_EXIT = new Command(1, Command.BACK, 1);
	private final static Command COMMAND_BACK = new Command(0, Command.BACK, 0);
	
	private final static String ATTACK = "Attack";
	private final static String DEFENSE = "Defense";
	private final static String SAVE = "Save";
	private final static String RESTORE_DEFAULT = "Restore default";
	
	private SumoSettings settings;
	
	private Display display;
	
	private List menu;
	
	private Form formAttack;
	private Form formDefense;
	
	private NumericField rangeField;
	private NumericField speedField;
	private NumericField pidPField;
	private NumericField pidIField;
	private NumericField pidDField;
	private NumericField escapeTurnRatio1Field;
	private NumericField escapeTurnDistance1Field;
	private NumericField escapeTurnRatio2Field;
	private NumericField escapeTurnDistance2Field;
	
	public static void show(final SumoSettings settings) {
		final SumoSettingsGui gui = new SumoSettingsGui(settings);
		gui.display();
	}

	public SumoSettingsGui(final SumoSettings settings) {
		this.settings = settings;
		
		menu = new List("Settings:", Choice.IMPLICIT);
		
		menu.append(ATTACK, null);
		menu.append(DEFENSE, null);
		menu.append("-------", null);
		menu.append(SAVE, null);
		menu.append(RESTORE_DEFAULT, null);
		
		menu.setCommandListener(this);
		menu.addCommand(COMMAND_EXIT);
		
		formAttack = new Form("Attack settings:");
		formDefense = new Form("Defense settings:");
		
		rangeField = new NumericField("range[cm]: ", settings.range, 10, 200, 10);
		
		speedField = new NumericField("speed[%] : ", settings.speed, 10, 100, 10);
		pidPField =  new NumericField("pid P:     ", settings.pidP, 0, 500, 50);
		pidIField =  new NumericField("pid I:     ", settings.pidI, 0, 50, 10);
		pidDField =  new NumericField("pid D:     ", settings.pidD, 0, 1000, 50);
		
		escapeTurnRatio1Field =     new NumericField("etr1:      ", settings.escapeTurnRatio1, 0, 200, 10);
		escapeTurnDistance1Field =  new NumericField("etd1[cm]:  ", settings.escapeTurnDistance1, 0, 1000, 10);
		escapeTurnRatio2Field =     new NumericField("etr2:      ", settings.escapeTurnRatio2, 0, 200, 10);
		escapeTurnDistance2Field =  new NumericField("etd2[cm]:  ", settings.escapeTurnDistance2, 0, 1000, 10);

		formAttack.append(rangeField);
		formAttack.append(speedField);
		formAttack.append(pidPField);
		formAttack.append(pidIField);
		formAttack.append(pidDField);
		
		formAttack.addCommand(COMMAND_BACK);
		formAttack.setCommandListener(this);
		
		formDefense.append(escapeTurnRatio1Field);
		formDefense.append(escapeTurnDistance1Field);
		formDefense.append(escapeTurnRatio2Field);
		formDefense.append(escapeTurnDistance2Field);

		formDefense.addCommand(COMMAND_BACK);
		formDefense.setCommandListener(this);
	}

	void display() {
		display = Display.getDisplay();
		display.setCurrent(menu);
		display.show(true);
	}
	
	private void save() {
		settings.range = rangeField.getValue();
		settings.speed = speedField.getValue();
		settings.pidP = pidPField.getValue();
		settings.pidI = pidIField.getValue();
		settings.pidD = pidDField.getValue();
		settings.escapeTurnRatio1 = escapeTurnRatio1Field.getValue();
		settings.escapeTurnDistance1 = escapeTurnDistance1Field.getValue();
		settings.escapeTurnRatio2 = escapeTurnRatio2Field.getValue();
		settings.escapeTurnDistance2 = escapeTurnDistance2Field.getValue();
		
		settings.save();
	}
	
	private void init() {
		rangeField.setValue(settings.range);
		speedField.setValue(settings.speed);
		pidPField.setValue(settings.pidP);
		pidIField.setValue(settings.pidI);
		pidDField.setValue(settings.pidD);
		escapeTurnRatio1Field.setValue(settings.escapeTurnRatio1);
		escapeTurnDistance1Field.setValue(settings.escapeTurnDistance1);
		escapeTurnRatio2Field.setValue(settings.escapeTurnRatio2);
		escapeTurnDistance2Field.setValue(settings.escapeTurnDistance2);
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
			if (ATTACK.equals(option)) {
				display.setCurrent(formAttack);
			} else if (DEFENSE.equals(option)){
				display.setCurrent(formDefense);
			} else if (SAVE.equals(option)) {
				save();
			} else if (RESTORE_DEFAULT.equals(option)) {
				this.settings = new SumoSettings();
				init();
			}
		}
	}
}
