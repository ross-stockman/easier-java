package rws.easierjava.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.core.Is;
import org.junit.Assert;

public class TestHelper {

	private TestHelper() {
	}

	/**
	 * <p>
	 * This is a helper assertion method to allow multiple assertions to be
	 * evaluated in one execution, and then output all assertions in one report.
	 * This method is useful when testing the mapping to an object. Here you could
	 * get a list of all the failures at once instead of having to fix one at a
	 * time.
	 * </p>
	 * <p>
	 * Usage example:
	 * </p>
	 * 
	 * <pre>
	 * assertAll(() -> assertThat(customer.getFirstName(), is("BILL")),
	 * 		() -> assertThat(customer.getMiddleName(), is("J")),
	 * 		() -> assertThat(customer.getLastName(), is("DENT")));
	 * </pre>
	 * 
	 * @param executables
	 * @throws Throwable
	 */
	public static void assertAll(Executable... executables) throws Throwable {
		List<AssertionError> assertionErrors = new ArrayList<>();
		for (Executable executable : executables) {
			try {
				executable.execute();
			} catch (AssertionError e) {
				assertionErrors.add(e);
			}
		}
		if (assertionErrors.size() == 1) {
			throw assertionErrors.get(0);
		} else if (!assertionErrors.isEmpty()) {
			Assert.fail("Multiple assertions failed\n"
					+ assertionErrors.stream().map(AssertionError::toString).collect(Collectors.joining("\n")));
		}
	}

	/**
	 * <p>
	 * Asserts the expected exception is thrown when the system under test is
	 * executed via the executable functional interface. The exception object is
	 * returned so that additional validations may be performed as needed.
	 * </p>
	 * <p>
	 * Usage example:
	 * </p>
	 * 
	 * <pre>
	 * IllegalArgumentException actualException = assertThrows(IllegalArgumentException.class,
	 * 		() -> systemUnderTest.divide(1, 0));
	 * assertThat(actualException.getMessage(), is("divide by zero"));
	 * </pre>
	 * 
	 * @param expectedType
	 * @param executable
	 * @return the exception caught.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
		try {
			executable.execute();
		} catch (AssertionError e) {
			throw e;
		} catch (Throwable actualException) {
			if (expectedType.isInstance(actualException)) {
				return (T) actualException;
			} else {
				Assert.assertThat("Unexpected exception type thrown.", actualException.getClass().getName(),
						Is.is(expectedType.getName()));
			}
		}
		AssertionError error = fail(
				String.format("Expected %s to be thrown, but nothing was thrown.", expectedType.getName()));
		if (error == null) {
			// This should be unreachable but required for sonar quality gate pass
			throw new NullPointerException("null");
		}
		throw error;
	}

	/**
	 * Catches the assertion error so it can be returned instead of thrown. Private
	 * method used by assertThrows()
	 * 
	 * @param message
	 * @return
	 */
	private static AssertionError fail(String message) {
		try {
			Assert.fail(message);
			return null; // unreachable
		} catch (AssertionError e) {
			return e;
		}
	}

	/**
	 * Helper method to read file in src/test/resources folder
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readResourceFile(String fileName) throws IOException {
		StringBuilder builder = new StringBuilder();
		try (Stream<String> stream = Files
				.lines(new File(TestHelper.class.getClassLoader().getResource(fileName).getFile()).toPath())) {
			stream.forEach(l -> builder.append(l).append("\n"));
		}
		return builder.toString();
	}

	/*
	 * squid:S00112:
	 * "Define and throw a dedicated exception instead of using a generic one."
	 * NOSONAR: This is just a generic function that needs to be able to throw any
	 * kind of exception thrown by the implementation.
	 */
	@FunctionalInterface
	public static interface Executable {
		void execute() throws Throwable; // NOSONAR
	}
}
