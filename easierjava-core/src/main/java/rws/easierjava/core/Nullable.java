package rws.easierjava.core;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

/**
 * <p>
 * If used on a method parameter then indicates the parameter is optional and
 * that the method implementation must be able to handle conditions where the
 * value may be null
 * </p>
 * <p>
 * If used on a method then indicates the method may return null under some
 * circumstances. The program calling this method should have logic to handle
 * null as an output.
 * </p>
 * 
 * @author ross
 *
 */
@Documented
@Retention(RUNTIME)
public @interface Nullable {

}
