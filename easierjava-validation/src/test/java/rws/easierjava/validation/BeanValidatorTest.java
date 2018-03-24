package rws.easierjava.validation;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Test;

public class BeanValidatorTest {

	@Test
	public void testIsValid() {
		Person person = new Person();
		person.setFriend(new Person());
		person.setNexFavNum(-5);
		person.setMoreNums(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		BeanValidator validator = new BeanValidator(v -> LOGGER.debug(
				"Violation: type={} field={} message={}, messageTemplate={} invalidValue={}", v.getType().getName(),
				v.getField(), v.getMessage(), v.getMessageTemplate(), v.getInvalidValue()));
		Assert.assertFalse(validator.isValid(person));
	}

	public static class Person {

		@NotNull
		private String name;

		@NotNull(message = "Must have a favorite number")
		private Integer favNum;

		@Positive
		private Integer nexFavNum;

		@Size(min = 1, max = 5)
		private List<Integer> moreNums;

		@Valid
		private Person friend;

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

		public Integer getNexFavNum() {
			return nexFavNum;
		}

		public void setNexFavNum(Integer nexFavNum) {
			this.nexFavNum = nexFavNum;
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

		public void setFriend(Person friend) {
			this.friend = friend;
		}

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
