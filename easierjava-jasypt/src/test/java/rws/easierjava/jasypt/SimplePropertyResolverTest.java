package rws.easierjava.jasypt;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore("This test is disabled because the system under test requires an environment variable being set on the operating system")
public class SimplePropertyResolverTest {

	@Test
	public void testGetProperty() {
		Assert.assertThat(SimplePropertyResolver.getProperty("is.found"), Is.is("pie"));
	}

	@Test
	public void testGetPropertyWithDefault() {
		Assert.assertThat(SimplePropertyResolver.getProperty("not.found", "defaultValue"), Is.is("defaultValue"));
	}
}
