package com.example.test.services;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ConvertService {

	public Map<String, String> convertToMap( Object o ){
		Map<String, String> m = new HashMap<String, String>();
		try {
			Field[] fields = o.getClass().getDeclaredFields();
			
			for ( int i = 0; i < fields.length; i++ ) {
				if ( !fields[i].equals(null) ) {
					m.put(fields[i].getName(), fields[i].get(o).toString() );
				}
			}
		} catch (Exception e) {
			
		}
		
		
		return m;
	}
	
	public String convertToJSON( Object o ) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
        try {
            json = mapper.writeValueAsString(o);
        } catch ( Exception e ){
        	
        }
        return json;
	}
}
