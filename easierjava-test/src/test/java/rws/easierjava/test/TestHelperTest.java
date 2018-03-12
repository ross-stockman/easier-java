package rws.easierjava.test;

import java.io.IOException;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import rws.easierjava.test.TestHelper;

public class TestHelperTest {

	@Test
	public void testAssertThrowsCanPass() {
		IllegalArgumentException e = TestHelper.assertThrows(IllegalArgumentException.class, () -> {
			throw new IllegalArgumentException("test");
		});
		Assert.assertThat(e.getMessage(), Is.is("test"));
	}

	@Test
	public void testAssertThrowsCanFailWithWrongException() {
		try {
			TestHelper.assertThrows(IllegalArgumentException.class, () -> {
				throw new UnsupportedOperationException("test");
			});
		} catch (Throwable e) {
			Assert.assertTrue(e instanceof AssertionError);
		}
	}

	@Test
	public void testAssertThrowsCanFailIfNoException() {
		try {
			TestHelper.assertThrows(IllegalArgumentException.class, () -> {
			});
		} catch (Throwable e) {
			Assert.assertTrue(e instanceof AssertionError);
		}
	}

	@Test
	public void testAssertThrowsCanPassWithSubClass() {
		RuntimeException e = TestHelper.assertThrows(RuntimeException.class, () -> {
			throw new IllegalArgumentException("test");
		});
		Assert.assertThat(e.getMessage(), Is.is("test"));
	}

	@Test
	public void testAssertThrowsWillNotSwallowOtherAssertionErrors() {
		try {
			TestHelper.assertThrows(IllegalArgumentException.class, () -> {
				Assert.fail("test");
			});
		} catch (Throwable e) {
			Assert.assertTrue(e instanceof AssertionError);
			Assert.assertThat(e.getMessage(), Is.is("test"));
		}
	}

	@Test
	public void testAssertAllCanPass() throws Throwable {
		TestHelper.assertAll(() -> Assert.assertThat(1, Is.is(1)), () -> Assert.assertThat(2, Is.is(2)));
	}

	@Test
	public void testAssertAllOneFailureWillFail() throws Throwable {
		try {
			TestHelper.assertAll(() -> Assert.assertThat(1, Is.is(3)), () -> Assert.assertThat(2, Is.is(2)));
		} catch (AssertionError e) {
			Assert.assertFalse(e.getMessage().contains("Multiple assertions failed"));
			Assert.assertTrue(e.getMessage().contains("Expected: is <3>"));
			Assert.assertTrue(e.getMessage().contains("but: was <1>"));
		}
	}

	@Test
	public void testAssertAllMultipleFailureWillFail() throws Throwable {
		try {
			TestHelper.assertAll(() -> Assert.assertThat(1, Is.is(3)), () -> Assert.assertThat(2, Is.is(4)));
		} catch (AssertionError e) {
			Assert.assertTrue(e.getMessage().contains("Multiple assertions failed"));
			Assert.assertTrue(e.getMessage().contains("Expected: is <3>"));
			Assert.assertTrue(e.getMessage().contains("but: was <1>"));
			Assert.assertTrue(e.getMessage().contains("Expected: is <4>"));
			Assert.assertTrue(e.getMessage().contains("but: was <2>"));
		}
	}

	@Test
	public void testReadResourceFile() throws IOException {
		Assert.assertThat(TestHelper.readResourceFile("data.txt"), Is.is("data"));
	}
}
