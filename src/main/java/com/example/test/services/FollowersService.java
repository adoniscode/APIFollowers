package com.example.test.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.test.domain.Repository;
import com.example.test.domain.User;

@Service
public class FollowersService {
	
	//@Autowired
	RestTemplate restTemplate = new RestTemplate();
	
	public List<User> findFollowers( String id ) {
		List<User> users;
		try{
			ResponseEntity<List<User>> response =
			        restTemplate.exchange("https://api.github.com/users/" + id + "/followers",
			                    HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
			            });
			users = response.getBody();
			if ( users.size() > 5 ) {
				users = users.subList(0, 5); //Only the first 5
			}
							
		} catch( Exception e) {
			users = new ArrayList<>();
		}
		return users;
	}
	
	public List<Repository> findRepositories( String id ) {
		List<Repository> repos;
		try{
			ResponseEntity<List<Repository>> response =
			        restTemplate.exchange("https://api.github.com/users/" + id + "/repos",
			                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Repository>>() {
			            });
			repos = response.getBody();
			if ( repos.size() > 3 ) {
				//repos = repos.subList(repos.size() - 3, repos.size()); //Only the last 3
				repos = repos.subList(0, 3); //Only the first 3
			}	
			
		} catch( Exception e) {
			repos = new ArrayList<>();
		}
		return repos;
	}
	
	public List<User> findStargazers( String repo, String owner ){
		List<User> users;
		try {
			ResponseEntity<List<User>> response =
			        restTemplate.exchange("https://api.github.com/repos/" + owner + "/" + repo + "/stargazers",
			                    HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
			            });
			users = response.getBody();
			if ( users.size() > 3 ) {
				users = users.subList(users.size() - 3, users.size()); //Only the last 3 so we avoid the user watching his/her own repo
				//users = users.subList(0, 3); //Only the first 3
			}
		} catch(Exception e) {
			users = new ArrayList<>();
		}
		
		return users;
	}
}
