package rws.easierjava.core.assertions;

import java.util.Optional;
import java.util.function.Supplier;

import rws.easierjava.core.annotations.Nullable;

public class Assert {

	private Assert() {
	}

	private static final String FUNCTION_DEFAULT_MESSAGE = "[Assertion failed] - this argument is invalid.";
	private static final String NOT_NULL_DEFAULT_MESSAGE = "[Assertion failed] - this argument is required; it must not be null.";

	/**
	 * Executes a boolean function that throws an {@link IllegalArgumentException}
	 * if the function returns false. This function is intended to be a lambda
	 * expression.
	 * <p />
	 * If the function returns true then void is returned. Example:
	 * 
	 * <pre class="code">
	 * Assert.isValid(() -> 1 == 1);
	 * </pre>
	 * <p />
	 * If the function returns false then {@link IllegalArgumentException} is
	 * thrown. Example:
	 * 
	 * <pre class="code">
	 * Assert.isValid(() -> 1 == 2);
	 * </pre>
	 * 
	 * @param function
	 *            boolean function to be executed
	 * @param message
	 *            custom exception message to be used if the test fails
	 * @throws IllegalArgumentException
	 *             if the function returns false
	 */
	public static void isValid(Supplier<Boolean> function, @Nullable String message) {
		if (!function.get()) {
			throw new IllegalArgumentException(Optional.ofNullable(message).orElse(FUNCTION_DEFAULT_MESSAGE));
		}
	}

	/**
	 * Validates the argument and {@link IllegalArgumentException} if the argument
	 * is null.
	 * 
	 * @param obj
	 *            argument to test
	 * @param message
	 *            custom exception message to be used if the test fails
	 * @throws IllegalArgumentException
	 *             if the argument is null
	 */
	public static void notNull(Object obj, @Nullable String message) {
		isValid(() -> obj != null, Optional.ofNullable(message).orElse(NOT_NULL_DEFAULT_MESSAGE));
	}

}
