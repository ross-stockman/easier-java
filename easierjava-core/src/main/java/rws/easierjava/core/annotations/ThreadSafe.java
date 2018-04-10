package rws.easierjava.core.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface ThreadSafe {

	IsThreadSafe value() default IsThreadSafe.YES;

	enum IsThreadSafe {
		/**
		 * This class is thread safe.
		 */
		YES,

		/**
		 * This class is probably (most likely) thread safe based on an educated guess.
		 */
		MAYBE,

		/**
		 * No testing has been conducted to prove conclusively whether this class is
		 * thread safe. There is reason to suspect that the class may not be thread
		 * safe.
		 */
		NOT_VALIDATED,

		/**
		 * This class is NOT thread safe.
		 */
		NO;
	}

}
