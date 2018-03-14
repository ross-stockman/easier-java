package rws.easierjava.mapper;

import java.io.IOException;

import org.apache.avro.Schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;

public final class SimpleAvro {

	private SimpleAvro() {
	}

	public static byte[] toBytes(Object obj) {
		try {
			final ObjectMapper mapper = ObjectMapperFactory.newObjectMapper("avro");
			final AvroSchemaGenerator gen = new AvroSchemaGenerator();
			mapper.acceptJsonFormatVisitor(obj.getClass(), gen);
			return mapper.writer(gen.getGeneratedSchema()).writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new ParseErrorException("parse error", e);
		}
	}

	public static <T> T toObject(byte[] bytes, Class<T> clazz) {
		try {
			final ObjectMapper mapper = ObjectMapperFactory.newObjectMapper("avro");
			final AvroSchemaGenerator gen = new AvroSchemaGenerator();
			mapper.acceptJsonFormatVisitor(clazz, gen);
			return mapper.readerFor(clazz).with(gen.getGeneratedSchema()).readValue(bytes);
		} catch (IOException e) {
			throw new ParseErrorException("parse error", e);
		}
	}

	public static Schema toSchema(Class<?> clazz) {
		try {
			final ObjectMapper mapper = ObjectMapperFactory.newObjectMapper("avro");
			final AvroSchemaGenerator gen = new AvroSchemaGenerator();
			mapper.acceptJsonFormatVisitor(clazz, gen);
			return gen.getGeneratedSchema().getAvroSchema();
		} catch (JsonProcessingException e) {
			throw new ParseErrorException("parse error", e);
		}
	}

}