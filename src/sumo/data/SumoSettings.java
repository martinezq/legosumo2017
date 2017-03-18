package sumo.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lejos.nxt.Button;

public class SumoSettings {

	private final static int VERSION = 1;
	
	public int startupDelay = 0;
	public int startupAngle = 0;
	
	public int attackSpeed = 100;
	public int pidP = 150;
	public int pidI = 0;
	public int pidD = 200;
	public int attackWheelSpeed = 100;
	
	public int searchRange = 50;
	public int searchTurnRatio = 90;
	public int searchSpeed = 50;
	
	public int escapeTurnRatio1 = 90;
	public int escapeTurnDistance1 = 350;
	public int escapeTurnRatio2 = 150;
	public int escapeTurnDistance2 = 40;
	
	public int borderValue = 50;
	
	public void save() {
		save("sumo_" + VERSION + ".conf");
	}
	
	public static SumoSettings read() {
		return read("sumo_" + VERSION + ".conf");
	}

	public void save(final String filename) {
		FileOutputStream out = null;
		File data = new File(filename);

		try {
			out = new FileOutputStream(data);
		} catch (IOException e) {
			System.err.println("Failed to create output stream");
			Button.waitForAnyPress();
			return;
		}

		DataOutputStream dataOut = new DataOutputStream(out);

		try {
			dataOut.writeInt(VERSION);
			
			dataOut.writeInt(startupDelay);
			dataOut.writeInt(startupAngle);
			
			dataOut.writeInt(searchSpeed);
			dataOut.writeInt(searchRange);
			dataOut.writeInt(searchTurnRatio);
			
			dataOut.writeInt(attackSpeed);
			dataOut.writeInt(pidP);
			dataOut.writeInt(pidI);
			dataOut.writeInt(pidD);
			dataOut.writeInt(attackWheelSpeed);
			
			dataOut.writeInt(escapeTurnRatio1);
			dataOut.writeInt(escapeTurnDistance1);
			dataOut.writeInt(escapeTurnRatio2);
			dataOut.writeInt(escapeTurnDistance2);
			dataOut.writeInt(borderValue);
			
			out.close();
		} catch (IOException e) {
			System.err.println("Failed to write to output stream");
			Button.waitForAnyPress();
			return;
		}
	}

	public static SumoSettings read(final String filename) {
		final SumoSettings settings = new SumoSettings();
		final File data = new File(filename);
		
		if (!data.exists()) {
			return settings;
		}

		try {
			final InputStream is = new FileInputStream(data);
			final DataInputStream din = new DataInputStream(is);

			int version = din.readInt();
			
			if (version != VERSION) {
				System.err.println("Expected config version " + VERSION + ", got " + version);
				Button.waitForAnyPress();
				din.close();
				return null;
			}
			
			settings.startupDelay = din.readInt();
			settings.startupAngle = din.readInt();
			
			settings.searchSpeed = din.readInt();
			settings.searchRange = din.readInt();
			settings.searchTurnRatio = din.readInt();
			
			settings.attackSpeed = din.readInt();
			settings.pidP = din.readInt();
			settings.pidI = din.readInt();
			settings.pidD = din.readInt();
			settings.attackWheelSpeed = din.readInt();
			
			settings.escapeTurnRatio1 = din.readInt();
			settings.escapeTurnDistance1 = din.readInt();
			settings.escapeTurnRatio2 = din.readInt();
			settings.escapeTurnDistance2 = din.readInt();
			settings.borderValue = din.readInt();
			
			din.close();
		} catch (IOException ioe) {
			System.err.println("Read Exception: " + ioe.getMessage());
			Button.waitForAnyPress();
			return null;
		}
		
		return settings;
	}

}
