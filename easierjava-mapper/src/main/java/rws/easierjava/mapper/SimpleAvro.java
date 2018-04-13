package rws.easierjava.mapper;

import java.io.IOException;

import org.apache.avro.Schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;

import rws.easierjava.core.annotations.Experimental;

@Experimental
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
			throw new ParseErrorException(e);
		}
	}
	
	public static byte[] toBytes(JsonNode jsonNode, Schema schema) {
		try {
			final ObjectMapper mapper = ObjectMapperFactory.newObjectMapper("avro");
			final AvroSchema avroSchema = new AvroSchema(schema);
			return mapper.writer(avroSchema).writeValueAsBytes(jsonNode);
		} catch (JsonProcessingException e) {
			throw new ParseErrorException(e);
		}
	}

	public static <T> T toObject(byte[] bytes, Class<T> clazz) {
		try {
			final ObjectMapper mapper = ObjectMapperFactory.newObjectMapper("avro");
			final AvroSchemaGenerator gen = new AvroSchemaGenerator();
			mapper.acceptJsonFormatVisitor(clazz, gen);
			return mapper.readerFor(clazz).with(gen.getGeneratedSchema()).readValue(bytes);
		} catch (IOException e) {
			throw new ParseErrorException(e);
		}
	}

	@Experimental
	public static JsonNode toJsonNode(byte[] bytes, Schema schema) {
		try {
			final ObjectMapper mapper = ObjectMapperFactory.newObjectMapper("avro");
			final AvroSchema avroSchema = new AvroSchema(schema);
			return mapper.readerFor(JsonNode.class).with(avroSchema).readValue(bytes);
		} catch (IOException e) {
			throw new ParseErrorException(e);
		}
	}

	public static Schema toSchema(Class<?> clazz) {
		try {
			final ObjectMapper mapper = ObjectMapperFactory.newObjectMapper("avro");
			final AvroSchemaGenerator gen = new AvroSchemaGenerator();
			mapper.acceptJsonFormatVisitor(clazz, gen);
			return gen.getGeneratedSchema().getAvroSchema();
		} catch (JsonProcessingException e) {
			throw new ParseErrorException(e);
		}
	}

	public static Schema toSchema(String jsonSchema) {
		return new Schema.Parser().setValidate(true).parse(jsonSchema);
	}

}
