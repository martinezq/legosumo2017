package util;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class SumoRadar extends Thread {
	
	public static final int MAX_RANGE = 40;
	public static final int ERR_NOT_FOUND = 100000;
	public static final int ERR_TOO_CLOSE = -100000;

	private boolean interrupt = false;
	
	static interface SumoRadarListener {
		public void onChange(SumoRadar radar);
	}

	private SumoRadarListener[] listeners = new SumoRadarListener[5];
	private int listenersCount = 0;
	
	private boolean pingLeft = true;
	private int lastPingTime = 0;
	
	private int lastDistanceLeftCm = MAX_RANGE;
	private int lastDistanceRightCm = MAX_RANGE;	
	
	public int distanceLeftCm = MAX_RANGE;
	public int distanceRightCm = MAX_RANGE;
	public int distanceCm = MAX_RANGE;
	private int error = ERR_NOT_FOUND;
	
	private UltrasonicSensor sensorLeft = new UltrasonicSensor(SensorPort.S4);
	private UltrasonicSensor sensorRight = new UltrasonicSensor(SensorPort.S3);

	public void addListener(SumoRadarListener listener) {
		this.listeners[listenersCount++] = listener;
	}
	
	@Override
	public void run() {
		ping();
		while (!interrupt) {		
			step();
		}
	}
	
	final private void step() {
		final int currentTime = (int)System.currentTimeMillis();
		
		if (currentTime - lastPingTime < 20) {
			return;
		}
		
		readDistances();
		pingLeft = !pingLeft;
		ping();
		
		distanceCm = Math.min(distanceLeftCm, distanceRightCm);
		
//		synchronized (this) {
			if (distanceCm > MAX_RANGE) {
				error = ERR_NOT_FOUND;
			} else if (distanceCm < 5) {
				error = ERR_TOO_CLOSE;
			} else {
				error = 100 * (Math.min(distanceRightCm, MAX_RANGE) - Math.min(distanceLeftCm, MAX_RANGE)) / MAX_RANGE;
			}
//		}
		
		if (wasChange()) {
			notifyListeners();
		}

		lastDistanceLeftCm = distanceLeftCm;
		lastDistanceRightCm = distanceRightCm;		
	}
	
	final private void ping() {
		if (pingLeft) {
			sensorLeft.ping();
		} else {
			sensorRight.ping();
		}
		
		lastPingTime = (int)System.currentTimeMillis();
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
		for(int i = 0; i < len; i++) {
			if (dists[i] < min) min = dists[i];
		}
		return min;
	}
	
	final private boolean wasChange() {
		return lastDistanceLeftCm != distanceLeftCm || lastDistanceRightCm != distanceRightCm;
	}
	
	final private void notifyListeners() {
		//Sound.beep();
		for(int i = 0; i < listenersCount; i++) {
			listeners[i].onChange(this);
		}
	}
	
	@Override
	public void interrupt() {
		this.interrupt = true;
	}

	public void stop() {
		interrupt();
	}
	
	public boolean objectDetected() {
//		synchronized (this) {
			return error != ERR_NOT_FOUND;
//		}
	}
	
	public int getError() {
//		synchronized (this) {
			return error;
//		}
	}
}
