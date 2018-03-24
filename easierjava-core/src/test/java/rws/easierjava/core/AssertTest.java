package rws.easierjava.core;

import org.hamcrest.core.Is;
import org.junit.Test;

import rws.easierjava.test.TestHelper;

public class AssertTest {

	@Test
	public void testNotNull() {
		IllegalArgumentException iae = TestHelper.assertThrows(IllegalArgumentException.class,
				() -> Assert.notNull(null, "message"));
		org.junit.Assert.assertThat(iae.getMessage(), Is.is("message"));
	}

	@Test
	public void testNotNull2() {
		Assert.notNull("non-null", "message");
	}
}
