package rws.easierjava.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

	private ClassUtils() {
	}

	/**
	 * Retrieves the default ClassLoader. Returns null only if the system
	 * ClassLoader is not accessible.
	 * 
	 * @return
	 */
	public static ClassLoader getDefaultClassLoader(Object obj) {
		ClassLoader classLoader = getClassClassLoader(obj.getClass());
		if (classLoader != null) {
			LOGGER.debug("Using ClassLoader for {}", obj.getClass().getName());
			return classLoader;
		}
		return getDefaultClassLoader();
	}

	/**
	 * @Sonar squid:S3457 String contains no format specifiers.
	 * @NoSonar These log statements have no parameters so no need for format
	 *          specifiers. Retrieves the default ClassLoader. Returns null only if
	 *          the system ClassLoader is not accessible.
	 * 
	 * @return
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader classLoader = getThreadContextClassLoader();
		if (classLoader != null) {
			LOGGER.debug("Using thread context ClassLoader"); // NOSONAR
			return classLoader;
		}
		classLoader = getClassClassLoader(ClassUtils.class);
		if (classLoader != null) {
			LOGGER.debug("Using class context ClassLoader for '{}'", ClassUtils.class.getName());
			return classLoader;
		}
		classLoader = getSystemClassLoader();
		if (classLoader != null) {
			LOGGER.debug("Using system ClassLoader"); // NOSONAR
			return classLoader;
		}
		LOGGER.debug("System ClassLoader not accessible"); // NOSONAR
		return null;
	}

	/**
	 * @see java.lang.Class#getClassLoader()
	 * @param clazz
	 *            Class for which ClassLoader to load
	 * @return
	 */
	public static ClassLoader getClassClassLoader(Class<?> clazz) {
		try {
			return clazz.getClassLoader();
		} catch (Exception e) {
			LOGGER.debug("Cannot access class context ClassLoader for '{}' due to '{}'. Returning null.",
					clazz.getName(), e);
			return null;
		}
	}

	/**
	 * @Sonar squid:S3457 No need to call toString "method()" as formatting and
	 *        string conversion is done by the Formatter.
	 * @NoSonar We have to explicitly call toString otherwise the overloaded
	 *          throwable version of the debug method will be used instead of the
	 *          object version
	 * @see java.lang.Thread#getContextClassLoader()
	 * @return ClassLoader or null
	 */
	public static ClassLoader getThreadContextClassLoader() {
		try {
			return Thread.currentThread().getContextClassLoader();
		} catch (Exception e) {
			LOGGER.debug("Cannot access thread context ClassLoader due to '{}'. Returning null.", e.toString()); // NOSONAR
			return null;
		}
	}

	/**
	 * @Sonar squid:S3457 No need to call toString "method()" as formatting and
	 *        string conversion is done by the Formatter.
	 * @NoSonar We have to explicitly call toString otherwise the overloaded
	 *          throwable version of the debug method will be used instead of the
	 *          object version
	 * @see java.lang.ClassLoader#getSystemClassLoader()
	 * @return ClassLoader or null
	 */
	public static ClassLoader getSystemClassLoader() {
		try {
			return ClassLoader.getSystemClassLoader();
		} catch (Exception e) {
			LOGGER.debug("Cannot access system ClassLoader due to '{}'. Returning null.", e.toString()); // NOSONAR
			return null;
		}
	}
}
