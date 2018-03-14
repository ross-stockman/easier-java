package rws.easierjava.mapper;

import java.io.IOException;
import java.util.TimeZone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule.Priority;

public final class ObjectMapperFactory {

	private ObjectMapperFactory() {
	}

	private static ObjectMapper newJsonObjectMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		JaxbAnnotationModule jaxbModule = new JaxbAnnotationModule();
		jaxbModule.setPriority(Priority.SECONDARY);
		mapper.registerModule(jaxbModule);
		mapper.setTimeZone(TimeZone.getDefault());
		return mapper;
	}

	private static ObjectMapper newXmlObjectMapper() {
		final ObjectMapper mapper = new XmlMapper();
		JaxbAnnotationModule jaxbModule = new JaxbAnnotationModule();
		mapper.registerModule(jaxbModule);
		mapper.setTimeZone(TimeZone.getDefault());
		return mapper;
	}

	private static ObjectMapper newYamlObjectMapper() {
		final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		JaxbAnnotationModule jaxbModule = new JaxbAnnotationModule();
		jaxbModule.setPriority(Priority.SECONDARY);
		mapper.registerModule(jaxbModule);
		mapper.setTimeZone(TimeZone.getDefault());
		return mapper;
	}

	private static ObjectMapper newAvroObjectMapper() {
		final ObjectMapper mapper = new ObjectMapper(new AvroFactory());
		JaxbAnnotationModule jaxbModule = new JaxbAnnotationModule();
		jaxbModule.setPriority(Priority.SECONDARY);
		mapper.registerModule(jaxbModule);
		mapper.setTimeZone(TimeZone.getDefault());
		return mapper;
	}

	public static ObjectMapper newObjectMapper(String type) {
		final String messageType = type.toUpperCase();
		if (messageType.contains("JSON")) {
			return newJsonObjectMapper();
		} else if (messageType.contains("XML")) {
			return newXmlObjectMapper();
		} else if (messageType.contains("YAML")) {
			return newYamlObjectMapper();
		} else if (messageType.contains("AVRO")) {
			return newAvroObjectMapper();
		} else {
			throw new IllegalArgumentException("unsupported type : " + type);
		}
	}

	public static JsonNode toJsonNode(String str, ObjectMapper mapper) {
		try {
			return mapper.readTree(str);
		} catch (IOException e) {
			throw new ParseErrorException("parse error", e);
		}
	}

	public static String toString(Object obj, ObjectMapper mapper) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new ParseErrorException("parse error", e);
		}
	}

	public static <T> T toObject(String str, Class<T> clazz, ObjectMapper mapper) {
		try {
			return mapper.readValue(str, clazz);
		} catch (IOException e) {
			throw new ParseErrorException("parse error", e);
		}
	}

}
