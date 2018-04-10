package rws.easierjava.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

import rws.easierjava.core.PropertyResolver;
import rws.easierjava.core.PropertyResolver.Mode;
import rws.easierjava.core.annotations.Nullable;

public class SimplePropertyResolver {

	private SimplePropertyResolver() {
	}

	private static final PropertyResolver propertyResolver;
	static {
		EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
		config.setAlgorithm("PBEWithMD5AndTripleDES");
		config.setPasswordEnvName("jceencr");
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setConfig(config);
		encryptor.initialize();
		propertyResolver = new EncryptablePropertyResolver(encryptor, Mode.SYSTEM_PROPERTIES_OVERRIDE);
		propertyResolver.registerClassPathProperties("application.properties");
	}

	@Nullable
	public static String getProperty(String key) {
		return propertyResolver.getProperty(key);
	}

	@Nullable
	public static String getProperty(String key, String defaultValue) {
		return propertyResolver.getProperty(key, defaultValue);
	}
}
