package com.example.test.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.domain.Repository;
import com.example.test.domain.User;
import com.example.test.services.ConvertService;
import com.example.test.services.FollowersService;

@RestController
public class FollowersController {
	
	@Autowired
	FollowersService fs;
	
	@Autowired
	ConvertService cs;
		
	@RequestMapping(value = "/f/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public /*Map<String, String>*/ String findFollowers( @PathVariable("id") String id ){
		
		List<User> followers = fs.findFollowers(id);
		List<User> followers2;
		List<User> followers3;
		
		if( followers.size() > 0 ) {
			for ( int i = 0; i < followers.size(); i++ ) {
				followers2 = fs.findFollowers( followers.get(i).getLogin() );
				followers.get(i).setFollowers(followers2);
				
				for ( int j = 0; j < followers2.size(); j++ ) {
					followers3 = fs.findFollowers( followers2.get(j).getLogin());
					followers2.get(j).setFollowers(followers3);
				}
			}
		}
		
		//return cs.convertToMap( followers );
		return cs.convertToJSON( followers );
	}
	
	@RequestMapping(value = "/r/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String findRepositories( @PathVariable("id") String id ){
		
		/* I know, I know, so many nested "for" loops? dang, wouldn't be better and easier only one loop containing the levels to dig?
		* or a recursive function wouldn't make it clearer? 
		* Well, yes. But every candidate is going to use that approach so let's have fun and do it in another way.
		* Besides, are only 3 levels, if the requirement changes then we re-factor it.
		*/
		List<Repository> repositories = fs.findRepositories(id);		
		
		if( repositories.size() > 0 ) {
			for ( int i = 0; i < repositories.size(); i++ ) {
				List<User> stargazers = fs.findStargazers( repositories.get(i).getName(), id );
				
				for ( int j = 0; j < stargazers.size(); j++ ) { //Iterate through the first stargazers level
					List<Repository> repositories2 = fs.findRepositories( stargazers.get(j).getLogin());
					stargazers.get(j).setRepositories( repositories2 );
					
					for ( int k = 0; k < repositories2.size(); k++ ) {
						List<User> stargazers2 = fs.findStargazers(repositories2.get(k).getName(), stargazers.get(j).getLogin());
						
						for ( int l = 0; l < stargazers2.size(); l++ ) { //Iterate through the second stargazers level
							List<Repository> repositories3 = fs.findRepositories(stargazers2.get(l).getLogin());
							stargazers2.get(l).setRepositories(repositories3);
							
							for ( int m = 0; m < repositories3.size(); m++ ) {  //OMG, we are running out of letters! Call the Avengers!!!
								List<User> stargazers3 = fs.findStargazers(repositories3.get(m).getName(), stargazers2.get(l).getLogin());								
								repositories3.get(m).setStargazers(stargazers3);
							}							
						}
						repositories2.get(k).setStargazers(stargazers2);
					}					
				}
				repositories.get(i).setStargazers(stargazers);
			}
		}
		
		return cs.convertToJSON( repositories );
	}
}
