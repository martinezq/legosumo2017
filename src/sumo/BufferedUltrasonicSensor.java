package sumo;

import java.util.Arrays;

import lejos.nxt.I2CPort;
import lejos.nxt.UltrasonicSensor;

public class BufferedUltrasonicSensor extends UltrasonicSensor {

	private int[] bufor;
	private int cursor = 0;
	private int len;
	
	public BufferedUltrasonicSensor(final I2CPort port, final int buforLength) {
		super(port);
		this.len = buforLength;
		bufor = new int[buforLength];
		Arrays.fill(bufor, 255);
	}
	
	public BufferedUltrasonicSensor(final I2CPort port) {
		this(port, 2);
	}

	@Override
	public int getDistance() {
		bufor[cursor++] = super.getDistance();
		cursor = cursor % len;
		int sum = 0;
		for(int i = 0; i < len; i++) {
			sum += bufor[i];
		}
		return sum / len;
	}
}
