package rws.easierjava.core;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

	private Random() {
	}

	public static String getUuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @param min
	 *            minimum boundary (inclusive)
	 * @param max
	 *            maximum boundary (exclusive)
	 * @return
	 */
	public static int getInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	/**
	 * @param min
	 *            minimum boundary (inclusive)
	 * @param max
	 *            maximum boundary (exclusive)
	 * @return
	 */
	public static long getLong(long min, long max) {
		return ThreadLocalRandom.current().nextLong(min, max);
	}

}
