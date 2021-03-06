package rws.easierjava.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class SimpleXml {

	private static final ObjectMapper MAPPER = ObjectMapperFactory.newObjectMapper("xml");

	private SimpleXml() {
	}

	public static String toString(Object obj) {
		return ObjectMapperFactory.toString(obj, MAPPER);
	}

	public static <T> T toObject(String str, Class<T> clazz) {
		return ObjectMapperFactory.toObject(str, clazz, MAPPER);
	}

}