package com.example.test.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {

	private String name;
	
	@JsonInclude(Include.NON_EMPTY)
	private List<User> stargazers;
	
	public Repository() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<User> getStargazers() {
		return stargazers;
	}
	
	public void setStargazers(List<User> stargazers) {
		this.stargazers = stargazers;
	}
}
