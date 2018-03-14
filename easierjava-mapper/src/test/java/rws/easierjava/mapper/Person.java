package rws.easierjava.mapper;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Person {
	@JsonProperty
	private String name;
	@JsonProperty
	private Integer age;
	@JsonProperty
	private Boolean active;
	@JsonProperty
	private List<String> sports;
	@JsonProperty
	private String friend;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<String> getSports() {
		return sports;
	}

	public void setSports(List<String> sports) {
		this.sports = sports;
	}

	public String getFriend() {
		return friend;
	}

	public void setFriend(String friend) {
		this.friend = friend;
	}

}
