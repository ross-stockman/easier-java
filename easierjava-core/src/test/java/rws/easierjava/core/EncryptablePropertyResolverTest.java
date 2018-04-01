package rws.easierjava.core;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;
import org.junit.Before;
import org.junit.Test;

import rws.easierjava.core.PropertyResolver.Mode;

public class EncryptablePropertyResolverTest {

	private SimplePBEConfig config;
	private StandardPBEStringEncryptor encryptor;

	@Before
	public void setup() {
		config = new SimplePBEConfig();
		config.setAlgorithm("PBEWithMD5AndTripleDES");
		config.setKeyObtentionIterations(1000);
		config.setPassword("somethingClever");
		encryptor = new StandardPBEStringEncryptor();
		encryptor.setConfig(config);
		encryptor.initialize();
	}

	@Test
	public void testEncryptedProperty() {
		PropertyResolver systemUnderTest = new EncryptablePropertyResolver(encryptor, Mode.NEVER_USE_SYSTEM_PROPERTIES);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("secret"), Is.is("pie"));
	}

	@Test
	public void testUnecryptedProperty() {
		PropertyResolver systemUnderTest = new EncryptablePropertyResolver(encryptor);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("not.secret"), Is.is("LnoNtGiL51PqEQKsR8ZnEA==)"));
		org.junit.Assert.assertThat(systemUnderTest.getProperty("also.not.secret"),
				Is.is("ENC(LnoNtGiL51PqEQKsR8ZnEA=="));
	}

	@Test
	public void testDefaultValue() {
		PropertyResolver systemUnderTest = new EncryptablePropertyResolver(encryptor,
				Mode.NULLABLE_NEVER_USE_SYSTEM_PROPERTIES);
		systemUnderTest.registerClassPathProperties("testapp.properties");
		org.junit.Assert.assertThat(systemUnderTest.getProperty("secrets"), IsNull.nullValue());
		org.junit.Assert.assertThat(systemUnderTest.getProperty("secrets", "default"), Is.is("default"));
	}
}
