package rws.easierjava.core;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;

import rws.easierjava.core.PropertyResolver.Mode;
import rws.easierjava.core.PropertyResolver.PropertyFileNotFoundException;
import rws.easierjava.core.PropertyResolver.PropertyNotFoundException;
import rws.easierjava.test.TestHelper;

public class PropertyResolverTest {

	@Before
	public void setup() {
		System.setProperty("me.first", "system");
		System.setProperty("me.next", "system");
	}

	@Test
	public void testReadPropertyFromClasspath() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NEVER_USE_SYSTEM_PROPERTIES);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.first"), Is.is("classpath"));
	}

	@Test
	public void testReadMultipleSourcesFirstTakesPriority() {
		PropertyResolver systemUnderTest = new PropertyResolver();
		systemUnderTest.registerClassPathProperties("testapp.properties");
		systemUnderTest.registerClassPathProperties("testapp2.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.first"), Is.is("classpath"));
	}

	@Test
	public void testReadMultipleSourcesFirstTakesPriority2() {
		PropertyResolver systemUnderTest = new PropertyResolver();
		systemUnderTest.registerProperties(PropertyResolverTest.class.getClassLoader().getResource("testapp.properties")
				.toString().replace("file:", ""));
		systemUnderTest.registerClassPathProperties("testapp2.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.first"), Is.is("classpath"));
	}

	@Test
	public void testOverridePropertyFromSystem() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.SYSTEM_PROPERTIES_OVERRIDE);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.first"), Is.is("system"));
	}

	@Test
	public void testOverridePropertyFromSystemDefault() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.SYSTEM_PROPERTIES_OVERRIDE);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.second", "default"), Is.is("classpath"));
	}

	@Test
	public void testOverridePropertyFromSystemDefault2() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.SYSTEM_PROPERTIES_OVERRIDE);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.next", "default"), Is.is("system"));
	}

	@Test
	public void testUseSystemIfPropertyNotFound() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.SYSTEM_PROPERTIES_FALLBACK);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.next"), Is.is("system"));
	}

	@Test
	public void testDefaultNull() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NULLABLE_NEVER_USE_SYSTEM_PROPERTIES);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.next"), IsNull.nullValue());
	}

	@Test
	public void testDefaultNull2() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NULLABLE_SYSTEM_PROPERTIES_FALLBACK);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.last"), IsNull.nullValue());
	}

	@Test
	public void testDefaultNull3() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NULLABLE_SYSTEM_PROPERTIES_FALLBACK);
		systemUnderTest.setEnvironmentFallback(true);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.last"), IsNull.nullValue());
	}

	@Test
	public void testPropertyNotFoundException() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NEVER_USE_SYSTEM_PROPERTIES);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		PropertyNotFoundException result = TestHelper.assertThrows(PropertyNotFoundException.class,
				() -> systemUnderTest.getProperty("me.next"));
		org.junit.Assert.assertThat(result.getMessage(), Is.is("Property not found : 'me.next'"));
	}

	@Test
	public void testPropertyNotFoundException2() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.SYSTEM_PROPERTIES_FALLBACK);
		PropertyNotFoundException result = TestHelper.assertThrows(PropertyNotFoundException.class,
				() -> systemUnderTest.getProperty("me.last"));
		org.junit.Assert.assertThat(result.getMessage(), Is.is("Property not found : 'me.last'"));
	}

	@Test
	public void testSkipPropertyFileNotFound() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NEVER_USE_SYSTEM_PROPERTIES_IGNORE_FILE_NOT_FOUND);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		systemUnderTest.registerClassPathProperties("testapp2.properties");
		systemUnderTest.registerClassPathProperties("testapp3.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.first"), Is.is("classpath"));
	}

	@Test
	public void testSkipPropertyFileNotFound2() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NEVER_USE_SYSTEM_PROPERTIES_IGNORE_FILE_NOT_FOUND);
		systemUnderTest.registerProperties(PropertyResolverTest.class.getClassLoader().getResource("testapp.properties")
				.toString().replace("file:", ""));
		systemUnderTest.registerProperties(PropertyResolverTest.class.getClassLoader()
				.getResource("testapp2.properties").toString().replace("file:", ""));

		String noSuchFilePath = PropertyResolverTest.class.getClassLoader().getResource("testapp2.properties")
				.toString().replace("file:", "") + "3";

		systemUnderTest.registerProperties(noSuchFilePath);
		org.junit.Assert.assertThat(systemUnderTest.getProperty("me.first"), Is.is("classpath"));
	}

	@Test
	public void testFailOnPropertyFileNotFound() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NEVER_USE_SYSTEM_PROPERTIES);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		systemUnderTest.registerClassPathProperties("testapp2.properties");
		PropertyFileNotFoundException result = TestHelper.assertThrows(PropertyFileNotFoundException.class,
				() -> systemUnderTest.registerClassPathProperties("testapp3.properties"));
		org.junit.Assert.assertThat(result.getMessage(), Is.is("Property file not found : 'testapp3.properties'"));
	}

	@Test
	public void testFailOnPropertyFileNotFound2() {
		PropertyResolver systemUnderTest = new PropertyResolver(Mode.NEVER_USE_SYSTEM_PROPERTIES);
		systemUnderTest.registerProperties(PropertyResolverTest.class.getClassLoader().getResource("testapp.properties")
				.toString().replace("file:", ""));
		systemUnderTest.registerProperties(PropertyResolverTest.class.getClassLoader()
				.getResource("testapp2.properties").toString().replace("file:", ""));

		String noSuchFilePath = PropertyResolverTest.class.getClassLoader().getResource("testapp2.properties")
				.toString().replace("file:", "") + "3";

		PropertyFileNotFoundException result = TestHelper.assertThrows(PropertyFileNotFoundException.class,
				() -> systemUnderTest.registerProperties(noSuchFilePath));
		org.junit.Assert.assertThat(result.getMessage(), Is.is("Property file not found : '" + noSuchFilePath + "'"));
	}
}
