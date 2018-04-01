package rws.easierjava.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyResolver.class);

	private boolean neverCheckSystem;
	private boolean systemOverride;
	private boolean failOnUnknownProperty;
	private boolean failOnFileNotFound;
	private boolean environmentFallback;

	private Properties loadedProperties;

	public PropertyResolver() {
		this(Mode.SYSTEM_PROPERTIES_FALLBACK);
	}

	public PropertyResolver(Mode mode) {
		setNeverCheckSystem(mode.neverCheckSystem);
		setSystemOverride(mode.systemOverride);
		setFailOnUnknownProperty(mode.failOnUnknownProperty);
		setFailOnFileNotFound(mode.failOnFileNotFound);
		setEnvironmentFallback(mode.environmentFallback);
		this.loadedProperties = new Properties();
	}

	/**
	 * @param neverCheckSystem
	 *            If true then system and environment properties will not be
	 *            checked. Default is false.
	 */
	public final void setNeverCheckSystem(boolean neverCheckSystem) {
		this.neverCheckSystem = neverCheckSystem;
	}

	/**
	 * @param systemOverride
	 *            If true then system properties will take priority over other
	 *            property files. Default is false.
	 */
	public final void setSystemOverride(boolean systemOverride) {
		this.systemOverride = systemOverride;
	}

	/**
	 * @param failOnUnknownProperty
	 *            If true then a {@code PropertyNotFoundException} will be thrown if
	 *            a property cannot be resolved. If false then null will be returned
	 *            for this scenario. Default is true.
	 */
	public final void setFailOnUnknownProperty(boolean failOnUnknownProperty) {
		this.failOnUnknownProperty = failOnUnknownProperty;
	}

	/**
	 * @param failOnFileNotFound
	 *            If true then a {@code PropertyFileNotFoundException} will be
	 *            thrown if a property resource cannot be resolved. If false then
	 *            the resource will be skipped. Default is true.
	 */
	public final void setFailOnFileNotFound(boolean failOnFileNotFound) {
		this.failOnFileNotFound = failOnFileNotFound;
	}

	/**
	 * @param environmentFallback
	 *            If true then, if unable to resolve a system property, will try to
	 *            resolve the property from the environment. Default is false.
	 */
	public final void setEnvironmentFallback(boolean environmentFallback) {
		this.environmentFallback = environmentFallback;
	}

	@Nullable
	protected String resolveSystemProperty(String key) {
		try {
			String value = System.getProperty(key);
			if (value == null && environmentFallback) {
				value = System.getenv(key);
			}
			return value;
		} catch (Exception e) {
			LOGGER.debug("Could not read system property '{}': {}", key, e);
		}
		return null;
	}

	@Nullable
	public String getProperty(String key) {
		return getProperty(key, null);
	}

	@Nullable
	public String getProperty(String key, String defaultValue) {
		String value = loadedProperties.getProperty(key);
		if (value == null && !neverCheckSystem) {
			value = resolveSystemProperty(key);
		} else if (systemOverride) {
			String override = resolveSystemProperty(key);
			if (override != null) {
				value = override;
			}
		}
		if (value == null && failOnUnknownProperty && defaultValue == null) {
			throw new PropertyNotFoundException(key);
		} else {
			return value != null ? value : defaultValue;
		}
	}

	public void registerClassPathProperties(String classPathLocation) {
		Properties properties = new Properties();
		try (InputStream inputStream = ClassUtils.getDefaultClassLoader(this).getResourceAsStream(classPathLocation)) {
			if (inputStream == null && failOnFileNotFound) {
				throw new PropertyFileNotFoundException(classPathLocation);
			} else if (inputStream == null) {
				LOGGER.info("Property file not found '{}'", classPathLocation);
			} else {
				properties.load(inputStream);
				mergePropertiesIfNotDuplicate(properties, loadedProperties);
			}
		} catch (IOException e) {
			if (failOnFileNotFound) {
				throw new PropertyFileNotFoundException(classPathLocation, e);
			} else {
				LOGGER.info("Property file not found '{}': {}", classPathLocation, e);
			}
		}
	}

	public void registerProperties(String location) {
		Properties properties = new Properties();
		try (InputStream inputStream = new FileInputStream(location)) {
			properties.load(inputStream);
			mergePropertiesIfNotDuplicate(properties, loadedProperties);
		} catch (IOException e) {
			if (failOnFileNotFound) {
				throw new PropertyFileNotFoundException(location, e);
			} else {
				LOGGER.info("Property file not found '{}': {}", location, e);
			}
		}
	}

	private void mergePropertiesIfNotDuplicate(Properties source, Properties target) {
		Assert.notNull(source, null);
		Assert.notNull(target, null);
		source.forEach((sourceKey, sourceValue) -> {
			if (!target.containsKey(sourceKey)) {
				target.put(sourceKey, sourceValue);
			}
		});
	}

	public enum Mode {
		/**
		 * Use this if the desired effect is to read properties only from specified
		 * sources and if null should be returned in the event that a property cannot be
		 * resolved.
		 */
		NULLABLE_NEVER_USE_SYSTEM_PROPERTIES(true, false, false, true, false),
		/**
		 * Use this if the desired effect is to read properties only from specified
		 * sources and if null should be returned in the event that a property cannot be
		 * resolved. Property sources that could not be resolved will be skipped.
		 */
		NULLABLE_NEVER_USE_SYSTEM_PROPERTIES_IGNORE_FILE_NOT_FOUND(true, false, false, false, false),
		/**
		 * Use this if the desired effect is to read properties only from the specified
		 * sources.
		 */
		NEVER_USE_SYSTEM_PROPERTIES(true, false, true, true, false),
		/**
		 * Use this if the desired effect is to read properties only from the specified
		 * sources. Property sources that could not be resolved will be skipped.
		 */
		NEVER_USE_SYSTEM_PROPERTIES_IGNORE_FILE_NOT_FOUND(true, false, true, false, false),
		/**
		 * Use this if the desired effect is to read properties from the specified
		 * source. If not resolvable then check the system properties. Null should be
		 * returned if the property cannot be resolved.
		 */
		NULLABLE_SYSTEM_PROPERTIES_FALLBACK(false, false, false, true, false),
		/**
		 * Use this if the desired effect is to read properties from the specified
		 * source. If not resolvable then check the system properties. Null should be
		 * returned if the property cannot be resolved. Property sources that could not
		 * be resolved will be skipped.
		 */
		NULLABLE_SYSTEM_PROPERTIES_FALLBACK_IGNORE_FILE_NOT_FOUND(false, false, false, false, false),
		/**
		 * Use this if the desired effect is to read properties from the specified
		 * source. If not resolvable then check the system properties. This is the
		 * default.
		 */
		SYSTEM_PROPERTIES_FALLBACK(false, false, true, true, false),
		/**
		 * Use this if the desired effect is to read properties from the specified
		 * source. If not resolvable then check the system properties. Property sources
		 * that could not be resolved will be skipped.
		 */
		SYSTEM_PROPERTIES_FALLBACK_IGNORE_FILE_NOT_FOUND(false, false, true, false, false),
		/**
		 * Use this if the desired effect is that system properties should override
		 * properties provided from other sources. Null should be returned if the
		 * property cannot be resolved.
		 */
		NULLABLE_SYSTEM_PROPERTIES_OVERRIDE(false, true, false, true, false),
		/**
		 * Use this if the desired effect is that system properties should override
		 * properties provided from other sources. Null should be returned if the
		 * property cannot be resolved. Property sources that could not be resolved will
		 * be skipped.
		 */
		NULLABLE_SYSTEM_PROPERTIES_OVERRIDE_IGNORE_FILE_NOT_FOUND(false, true, false, false, false),
		/**
		 * Use this if the desired effect is that system properties should override
		 * properties provided from other sources.
		 */
		SYSTEM_PROPERTIES_OVERRIDE(false, true, true, true, false),
		/**
		 * Use this if the desired effect is that system properties should override
		 * properties provided from other sources. Property sources that could not be
		 * resolved will be skipped.
		 */
		SYSTEM_PROPERTIES_OVERRIDE_IGNORE_FILE_NOT_FOUND(false, true, true, false, false);

		private final boolean neverCheckSystem;
		private final boolean systemOverride;
		private final boolean failOnUnknownProperty;
		private final boolean failOnFileNotFound;
		private final boolean environmentFallback;

		private Mode(boolean neverCheckSystem, boolean systemOverride, boolean failOnUnknownProperty,
				boolean failOnFileNotFound, boolean environmentFallback) {
			this.neverCheckSystem = neverCheckSystem;
			this.systemOverride = systemOverride;
			this.failOnUnknownProperty = failOnUnknownProperty;
			this.failOnFileNotFound = failOnFileNotFound;
			this.environmentFallback = environmentFallback;
		}
	}

	public static class PropertyNotFoundException extends RuntimeException {

		private static final long serialVersionUID = -7257212341636693198L;

		public PropertyNotFoundException(String key) {
			super("Property not found : '" + key + "'");
		}

	}

	public static class PropertyFileNotFoundException extends RuntimeException {

		private static final long serialVersionUID = 3901516356534256034L;

		public PropertyFileNotFoundException(String resourcePath) {
			super("Property file not found : '" + resourcePath + "'");
		}

		public PropertyFileNotFoundException(String resourcePath, Throwable t) {
			super("Property file not found : '" + resourcePath + "'", t);
		}
	}
}
