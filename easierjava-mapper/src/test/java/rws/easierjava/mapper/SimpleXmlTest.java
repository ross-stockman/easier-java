package rws.easierjava.mapper;

import java.io.IOException;
import java.util.Arrays;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import rws.easierjava.test.TestHelper;

public class SimpleXmlTest {

	@Test
	public void testObjectToString() {
		Person input = new Person();
		input.setActive(true);
		input.setAge(1);
		input.setFriend(null);
		input.setName("bill");
		input.setSports(Arrays.asList("football", "baseball"));

		String txt = SimpleXml.toString(input);

		Person output = SimpleXml.toObject(txt, Person.class);

		Assert.assertThat(output.getFriend(), IsNull.nullValue());
		Assert.assertThat(output.getName(), Is.is("bill"));
		Assert.assertThat(output.getActive(), Is.is(true));
		Assert.assertThat(output.getAge(), Is.is(1));
		Assert.assertThat(output.getSports().get(0), Is.is("football"));
		Assert.assertThat(output.getSports().get(1), Is.is("baseball"));
	}

	@Test
	public void testStringToObject() throws IOException {
		String txt = TestHelper.readResourceFile("Person.xml");

		Person output = SimpleXml.toObject(txt, Person.class);

		Assert.assertThat(output.getFriend(), IsNull.nullValue());
		Assert.assertThat(output.getName(), Is.is("bill"));
		Assert.assertThat(output.getActive(), Is.is(true));
		Assert.assertThat(output.getAge(), Is.is(1));
		Assert.assertThat(output.getSports().get(0), Is.is("football"));
		Assert.assertThat(output.getSports().get(1), Is.is("baseball"));
	}

}
