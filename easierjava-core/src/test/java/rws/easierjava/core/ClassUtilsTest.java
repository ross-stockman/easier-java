package rws.easierjava.core;

import org.junit.Test;

public class ClassUtilsTest {

	@Test
	public void testGetDefaultClassLoader() throws ClassNotFoundException {
		ClassLoader loader = ClassUtils.getDefaultClassLoader();
		org.junit.Assert.assertEquals(String.class, loader.loadClass("java.lang.String"));
	}

	@Test
	public void testGetDefaultClassLoader2() throws ClassNotFoundException {
		ClassLoader loader = ClassUtils.getDefaultClassLoader(this);
		org.junit.Assert.assertEquals(String.class, loader.loadClass("java.lang.String"));
	}
}
