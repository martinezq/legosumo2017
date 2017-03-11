package data;

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
	
	public int speed = 100;
	public int pidP = 150;
	public int pidI = 0;
	public int pidD = 200;
	public int range = 50;
	public int escapeTurnRatio1 = 90;
	public int escapeTurnDistance1 = 350;
	public int escapeTurnRatio2 = 150;
	public int escapeTurnDistance2 = 40;
	
	public void save() {
		save("settings" + VERSION + ".conf");
	}
	
	public static SumoSettings read() {
		return read("settings" + VERSION + ".conf");
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
			dataOut.writeInt(speed);
			dataOut.writeInt(pidP);
			dataOut.writeInt(pidI);
			dataOut.writeInt(pidD);
			dataOut.writeInt(range);
			dataOut.writeInt(escapeTurnRatio1);
			dataOut.writeInt(escapeTurnDistance1);
			dataOut.writeInt(escapeTurnRatio2);
			dataOut.writeInt(escapeTurnDistance2);
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
			
			settings.speed = din.readInt();
			settings.pidP = din.readInt();
			settings.pidI = din.readInt();
			settings.pidD = din.readInt();
			settings.range = din.readInt();
			settings.escapeTurnRatio1 = din.readInt();
			settings.escapeTurnDistance1 = din.readInt();
			settings.escapeTurnRatio2 = din.readInt();
			settings.escapeTurnDistance2 = din.readInt();
			
			din.close();
		} catch (IOException ioe) {
			System.err.println("Read Exception: " + ioe.getMessage());
			Button.waitForAnyPress();
			return null;
		}
		
		return settings;
	}

}
