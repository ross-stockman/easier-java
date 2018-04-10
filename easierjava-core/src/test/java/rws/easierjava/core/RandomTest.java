package rws.easierjava.core;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

public class RandomTest {

	/**
	 * Note: there is an extremely unlikely chance this could fail in the event that
	 * two identical random uuid's could get generated
	 * 
	 * @see java.util.UUID#randomUUID
	 */
	@Test
	public void testUniqueGetUuidConsistent() {
		for (int i = 0; i < 100; i++) {
			String uuid1 = Random.getUuid();
			String uuid2 = Random.getUuid();
			Assert.assertThat(uuid1, IsNull.notNullValue());
			Assert.assertThat(uuid2, IsNull.notNullValue());
			Assert.assertThat(uuid1, IsNot.not(uuid2));
		}
	}

	@Test
	public void testGetIntConsistent() {
		for (int i = 0; i < 100; i++) {
			int r = Random.getInt(0, 1);
			Assert.assertThat(r, Is.is(0));
			int s = Random.getInt(0, 10);
			Assert.assertTrue(s >= 0);
			Assert.assertTrue(s < 100);
		}
	}

	@Test
	public void testGetLongConsistent() {
		for (int i = 0; i < 100; i++) {
			long r = Random.getLong(0L, 1L);
			Assert.assertThat(r, Is.is(0L));
			long s = Random.getLong(0L, 10L);
			Assert.assertTrue(s >= 0L);
			Assert.assertTrue(s < 100L);
		}
	}

}
