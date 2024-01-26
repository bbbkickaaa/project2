package com.API.User;

import org.springframework.http.ResponseEntity;

import com.API.User.Entity.User;

public interface LoginService{

public ResponseEntity<String> loginUser(User user);
	
}
