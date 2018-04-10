package rws.easierjava.validation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.junit.Assert;
import org.junit.Test;

import rws.easierjava.validation.BeanValidator.Violation;

public class BeanValidatorTest {

	@Test
	public void testIsValid() {
		Person person = new Person();
		person.setFriend(new Friend());
		person.getFriend().setNextFavNum(1000);
		person.setNextFavNum(-5);
		person.setMoreNums(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

		Map<String, Violation> violations = new HashMap<>();

		BeanValidator validator = new BeanValidator(v -> violations.put(v.getPropertyPath().toString(), v));
		Assert.assertFalse(validator.isValid(person));

		violations.forEach((k, v) -> {
			System.out.println(k + " = " + v.getMessageTemplate());
		});

		Assert.assertThat(violations.size(), Is.is(7));

		Assert.assertThat(violations.get("name"), IsNull.notNullValue());
		Assert.assertThat(violations.get("name").getMessage(), Is.is("must not be null"));
		Assert.assertThat(violations.get("name").getInvalidValue(), IsNull.nullValue());
		Assert.assertThat(violations.get("name").getType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("name").getRootType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("name").getMessageTemplate(),
				Is.is("{javax.validation.constraints.NotNull.message}"));

		Assert.assertThat(violations.get("friend.name"), IsNull.notNullValue());
		Assert.assertThat(violations.get("friend.name").getMessage(), Is.is("must not be null"));
		Assert.assertThat(violations.get("friend.name").getInvalidValue(), IsNull.nullValue());
		Assert.assertThat(violations.get("friend.name").getType().getName(), Is.is(Friend.class.getName()));
		Assert.assertThat(violations.get("friend.name").getRootType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("friend.name").getMessageTemplate(),
				Is.is("{javax.validation.constraints.NotNull.message}"));

		Assert.assertThat(violations.get("favNum"), IsNull.notNullValue());
		Assert.assertThat(violations.get("favNum").getMessage(), Is.is("Must have a favorite number"));
		Assert.assertThat(violations.get("favNum").getInvalidValue(), IsNull.nullValue());
		Assert.assertThat(violations.get("favNum").getType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("favNum").getRootType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("favNum").getMessageTemplate(), Is.is("Must have a favorite number"));

		Assert.assertThat(violations.get("friend.favNum"), IsNull.notNullValue());
		Assert.assertThat(violations.get("friend.favNum").getMessage(), Is.is("Must have a favorite number"));
		Assert.assertThat(violations.get("friend.favNum").getInvalidValue(), IsNull.nullValue());
		Assert.assertThat(violations.get("friend.favNum").getType().getName(), Is.is(Friend.class.getName()));
		Assert.assertThat(violations.get("friend.favNum").getRootType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("friend.favNum").getMessageTemplate(), Is.is("Must have a favorite number"));

		Assert.assertThat(violations.get("nextFavNum"), IsNull.notNullValue());
		Assert.assertThat(violations.get("nextFavNum").getMessage(), Is.is("Must be a number greater than 1"));
		Assert.assertThat(violations.get("nextFavNum").getInvalidValue(), Is.is(-5));
		Assert.assertThat(violations.get("nextFavNum").getType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("nextFavNum").getRootType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("nextFavNum").getMessageTemplate(),
				Is.is("{javax.validation.constraints.Min.message}"));

		Assert.assertThat(violations.get("friend.nextFavNum"), IsNull.notNullValue());
		Assert.assertThat(violations.get("friend.nextFavNum").getMessage(), Is.is("Must be a number less than 100"));
		Assert.assertThat(violations.get("friend.nextFavNum").getInvalidValue(), Is.is(1000));
		Assert.assertThat(violations.get("friend.nextFavNum").getType().getName(), Is.is(Friend.class.getName()));
		Assert.assertThat(violations.get("friend.nextFavNum").getRootType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("friend.nextFavNum").getMessageTemplate(),
				Is.is("Must be a number less than {value}"));

		Assert.assertThat(violations.get("moreNums"), IsNull.notNullValue());
		Assert.assertThat(violations.get("moreNums").getMessage(), Is.is("Must be a size between 1 and 5"));
		Assert.assertThat(violations.get("moreNums").getInvalidValue(),
				Is.is(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
		Assert.assertThat(violations.get("moreNums").getType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("moreNums").getRootType().getName(), Is.is(Person.class.getName()));
		Assert.assertThat(violations.get("moreNums").getMessageTemplate(), Is.is("{validation.size}"));
	}

	public static class Person {

		@NotNull
		private String name;

		@NotNull(message = "Must have a favorite number")
		private Integer favNum;

		@Min(1)
		@Max(value = 100, message = "Must be a number less than {value}")
		private Integer nextFavNum;

		@Size(min = 1, max = 5, message = "{validation.size}")
		private List<Integer> moreNums;

		@Valid
		private Friend friend;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getFavNum() {
			return favNum;
		}

		public void setFavNum(Integer favNum) {
			this.favNum = favNum;
		}

		public Integer getNextFavNum() {
			return nextFavNum;
		}

		public void setNextFavNum(Integer nextFavNum) {
			this.nextFavNum = nextFavNum;
		}

		public List<Integer> getMoreNums() {
			return moreNums;
		}

		public void setMoreNums(List<Integer> moreNums) {
			this.moreNums = moreNums;
		}

		public Person getFriend() {
			return friend;
		}

		public void setFriend(Friend friend) {
			this.friend = friend;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}

	public static class Friend extends Person {
	}
}
