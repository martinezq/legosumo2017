package sumo.behavior;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import sumo.data.SumoSettings;

public class SumoRadar extends Thread {

	public static final int ERR_NOT_FOUND = 100000;
	public static final int ERR_TOO_CLOSE = -100000;

	private boolean interrupt = false;

	public static interface SumoRadarListener {
		public void onChange(SumoRadar radar);
	}

	private final int range;

	private SumoRadarListener[] listeners = new SumoRadarListener[5];
	private int listenersCount = 0;

	private boolean pingLeft = true;
	private int lastPingTime = 0;

	private int lastDistanceLeftCm;
	private int lastDistanceRightCm;

	public int distanceLeftCm;
	public int distanceRightCm;
	public int distanceCm;
	private int error = ERR_NOT_FOUND;
	private int lastError = 0;

	private UltrasonicSensor sensorLeft = new UltrasonicSensor(SensorPort.S4);
	private UltrasonicSensor sensorRight = new UltrasonicSensor(SensorPort.S3);

	public void addListener(SumoRadarListener listener) {
		this.listeners[listenersCount++] = listener;
	}

	public SumoRadar(final SumoSettings settings) {
		range = settings.searchRange;
		lastDistanceLeftCm = range;
		lastDistanceRightCm = range;
		distanceCm = range;
		distanceLeftCm = range;
		distanceRightCm = range;
	}

	@Override
	public void run() {
		ping();
		while (!interrupt) {
			step();
		}
	}

	final private void step() {
		final int currentTime = (int) System.currentTimeMillis();

		if (currentTime - lastPingTime < 20) {
			return;
		}

		readDistances();
		pingLeft = !pingLeft;
		ping();

		if (!wasChange()) {
			return;
		}

		distanceCm = Math.min(distanceLeftCm, distanceRightCm);

		int lastErrorBackup = lastError;

		lastError = error;

		if (distanceCm > range) {
			error = ERR_NOT_FOUND;
		} else if (distanceCm < 5) {
			error = ERR_TOO_CLOSE;
		} else {
			error = 100 * (Math.min(distanceRightCm, range) - Math.min(distanceLeftCm, range)) / range;
		}
		if (lastError == ERR_NOT_FOUND || lastError == ERR_TOO_CLOSE) {
			lastError = lastErrorBackup;
		}
		// }

		notifyListeners();

		lastDistanceLeftCm = distanceLeftCm;
		lastDistanceRightCm = distanceRightCm;

	}

	final private void ping() {
		if (pingLeft) {
			sensorLeft.ping();
		} else {
			sensorRight.ping();
		}

		lastPingTime = (int) System.currentTimeMillis();
	}

	final private void readDistances() {
		if (pingLeft) {
			distanceLeftCm = readDistance(sensorLeft);
		} else {
			distanceRightCm = readDistance(sensorRight);
		}
	}

	final private int[] dists = new int[8];

	final private int readDistance(final UltrasonicSensor sensor) {
		int len = sensor.getDistances(dists);
		int min = 255;
		for (int i = 0; i < len; i++) {
			if (dists[i] < min)
				min = dists[i];
		}
		return min;
	}

	final private boolean wasChange() {
		return lastDistanceLeftCm != distanceLeftCm || lastDistanceRightCm != distanceRightCm;
	}

	final private void notifyListeners() {
		// RConsole.println("radar notify listeners");
		for (int i = 0; i < listenersCount; i++) {
			listeners[i].onChange(this);
		}
		// RConsole.println("radar notifi listeners done");
	}

	@Override
	public void interrupt() {
		this.interrupt = true;
	}

	public void stop() {
		interrupt();
	}

	public boolean objectDetected() {
		return error != ERR_NOT_FOUND;
	}

	public int getError() {
		return error;
	}

	public int getLastError() {
		return lastError;
	}
}
