package rws.easierjava.mapper;

import java.io.IOException;
import java.util.Arrays;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

import rws.easierjava.test.TestHelper;

public class SimpleYamlTest {

	@Test
	public void testObjectToString() {
		Person input = new Person();
		input.setActive(true);
		input.setAge(1);
		input.setFriend(null);
		input.setName("bill");
		input.setSports(Arrays.asList("football", "baseball"));

		String txt = SimpleYaml.toString(input);
		Person output = SimpleYaml.toObject(txt, Person.class);

		Assert.assertThat(output.getFriend(), IsNull.nullValue());
		Assert.assertThat(output.getName(), Is.is("bill"));
		Assert.assertThat(output.getActive(), Is.is(true));
		Assert.assertThat(output.getAge(), Is.is(1));
		Assert.assertThat(output.getSports().get(0), Is.is("football"));
		Assert.assertThat(output.getSports().get(1), Is.is("baseball"));
	}

	@Test
	public void testStringToJsonNode() throws IOException {
		String txt = TestHelper.readResourceFile("Person.yaml");
		JsonNode output = SimpleYaml.toJsonNode(txt);

		Assert.assertThat(output.path("friend").textValue(), IsNull.nullValue());
		Assert.assertThat(output.path("name").textValue(), Is.is("bill"));
		Assert.assertThat(output.path("active").asBoolean(), Is.is(true));
		Assert.assertThat(output.path("age").asInt(), Is.is(1));
		Assert.assertThat(output.path("sports").path(0).textValue(), Is.is("football"));
		Assert.assertThat(output.path("sports").path(1).textValue(), Is.is("baseball"));
	}
}
