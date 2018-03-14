package rws.easierjava.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class SimpleYaml {

	private static final ObjectMapper MAPPER = ObjectMapperFactory.newObjectMapper("yaml");

	private SimpleYaml() {
	}

	public static JsonNode toJsonNode(String str) {
		return ObjectMapperFactory.toJsonNode(str, MAPPER);
	}

	public static String toString(Object obj) {
		return ObjectMapperFactory.toString(obj, MAPPER);
	}

	public static <T> T toObject(String str, Class<T> clazz) {
		return ObjectMapperFactory.toObject(str, clazz, MAPPER);
	}

}