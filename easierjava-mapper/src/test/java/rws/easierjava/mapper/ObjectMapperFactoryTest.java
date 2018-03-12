package rws.easierjava.mapper;

import java.io.IOException;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import rws.easierjava.test.TestHelper;

public class ObjectMapperFactoryTest {

	@Test
	public void testInvalidType() {
		IllegalArgumentException result = TestHelper.assertThrows(IllegalArgumentException.class,
				() -> ObjectMapperFactory.newObjectMapper("fail"));
		Assert.assertThat(result.getMessage(), Is.is("unsupported type : fail"));
	}

	@Test
	public void testParseErrorOnToString() throws JsonProcessingException {
		ObjectMapper mockedObjectMapper = Mockito.mock(ObjectMapper.class);
		Mockito.when(mockedObjectMapper.writeValueAsString(Mockito.any()))
				.thenThrow(new DummyJsonProcessingException());

		ParseErrorException output = TestHelper.assertThrows(ParseErrorException.class,
				() -> ObjectMapperFactory.toString(new Object(), mockedObjectMapper));
		Assert.assertThat(output.getMessage(), Is.is("parse error"));
		Assert.assertThat(output.getCause().getMessage(), Is.is("dummy"));
	}

	@Test
	public void testParseErrorOnToObject() throws IOException {
		ObjectMapper mockedObjectMapper = Mockito.mock(ObjectMapper.class);
		Mockito.when(mockedObjectMapper.readValue(Mockito.anyString(), Mockito.eq(Person.class)))
				.thenThrow(new IOException("dummy"));

		ParseErrorException output = TestHelper.assertThrows(ParseErrorException.class,
				() -> ObjectMapperFactory.toObject("some string", Person.class, mockedObjectMapper));
		Assert.assertThat(output.getMessage(), Is.is("parse error"));
		Assert.assertThat(output.getCause().getMessage(), Is.is("dummy"));
	}

	public class DummyJsonProcessingException extends JsonProcessingException {

		private static final long serialVersionUID = 8449239075781994271L;

		public DummyJsonProcessingException() {
			super("dummy");
		}

	}
}
