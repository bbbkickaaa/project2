package com.API.User;

import org.springframework.http.ResponseEntity;

import com.API.User.Entity.User;

public interface NewUserService {

	public ResponseEntity<String> createUser(User user);
	
}
