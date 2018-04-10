package rws.easierjava.mapper;

/**
 * Similar to apache commons {@code ToStringBuilder} but will print output as
 * either json or xml. Usage:
 * 
 * <pre>
 * &#64;Override
 * public String toString() {
 * 	return SimpleToString.toJsonString(this);
 * }
 * </pre>
 * 
 * @author rstockm2
 *
 */
public class SimpleToString {

	public static String toJsonString(Object obj) {
		return SimpleJson.toString(obj);
	}

	public static String toXmlString(Object obj) {
		return SimpleXml.toString(obj);
	}
}
