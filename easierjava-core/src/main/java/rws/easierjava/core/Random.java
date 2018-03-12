package rws.easierjava.core;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

	private Random() {
	}

	public static String getUuid() {
		return UUID.randomUUID().toString();
	}

	public static int getInt() {
		return getInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	public static int getInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	public static long getLong() {
		return getLong(Long.MIN_VALUE, Long.MAX_VALUE);
	}

	public static long getLong(long min, long max) {
		return ThreadLocalRandom.current().nextLong(min, max);
	}

}
