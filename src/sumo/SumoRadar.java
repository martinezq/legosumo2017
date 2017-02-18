package sumo;

import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class SumoRadar extends Thread {
	
	public static final int MAX_RANGE = 50;
	private static final int DIFF_THRESHOLD = 3;
	
	static enum Heading { UNKNOWN, LEFT, RIGHT, CENTER }
	static enum Distance { UNKNOWN, FAR, MIDDLE, NEAR, TOUCH }
	
	static interface SumoRadarListener {
		public void onChange(SumoRadar radar);
	}

	public int distanceLeftCm = MAX_RANGE;
	public int distanceRightCm = MAX_RANGE;
	
	public Heading lastHeading = Heading.UNKNOWN;
	public Heading heading = Heading.UNKNOWN;
	public Distance distance = Distance.UNKNOWN;
	
	public int distanceCm = MAX_RANGE;
	public int diffCm = 0;
	
	private int lastDistanceLeftCm = MAX_RANGE;
	private int lastDistanceRightCm = MAX_RANGE;
	
	private boolean interrupt = false;
	
	private boolean pingLeft = true;
	private int lastPingTime = 0;
	
	private UltrasonicSensor sensorLeft = new UltrasonicSensor(SensorPort.S4);
	private UltrasonicSensor sensorRight = new UltrasonicSensor(SensorPort.S3);
	
	private SumoRadarListener[] listeners = new SumoRadarListener[5];
	private int listenersCount = 0;

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
		diffCm = Math.abs(distanceLeftCm - distanceRightCm);
		
		distance = decodeDistance();
		
		Heading newHeading = decodeHeading();
		
		if (heading != newHeading) {
			lastHeading = heading;
		}
		
		heading = newHeading;
		
		if (wasChange()) {
			notifyListeners();
		}

		lastDistanceLeftCm = distanceLeftCm;
		lastDistanceRightCm = distanceRightCm;		
	}

	final private int[] reads = new int[8];
	
	final private void ping() {
		//Sound.beep();
		
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
	
	final private int readDistance(final UltrasonicSensor sensor) {
		/*
		int count = sensor.getDistances(reads, 0, 8);
			
		if (count > 0) {
			return min(reads, count);
		}
		
		return 255;
		*/
		return sensor.getDistance();
	}
	
	private final static int min(int[] data, int length) {
		int min = 255;
		for(int i = 0; i < length; i++) {
			if (data[i] < min) {
				min = data[i];
			}
		}
		return min;
	}
	
	// distance is decoded first
	final private Heading decodeHeading() {
		if (distance == Distance.UNKNOWN) {
			return Heading.UNKNOWN;
		} else if (diffCm < DIFF_THRESHOLD && distanceLeftCm < MAX_RANGE && distanceRightCm < MAX_RANGE) {
			return Heading.CENTER;
		} else if (distanceLeftCm < distanceRightCm) {
			return Heading.LEFT;
		} else if (distanceLeftCm > distanceRightCm) {
			return Heading.RIGHT;
		} else {
			return Heading.UNKNOWN;
		}
	}
	
	final private Distance decodeDistance() {
		if (distanceCm > MAX_RANGE) {
			return Distance.UNKNOWN;
		} else if (distanceCm > 3 * MAX_RANGE / 4) {
			return Distance.FAR;
		} else if (distanceCm > MAX_RANGE / 4) {
			return Distance.MIDDLE;
		} else if (distanceCm > 6) {
			return Distance.NEAR;
		} else {
			return Distance.TOUCH;
		}
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
}
