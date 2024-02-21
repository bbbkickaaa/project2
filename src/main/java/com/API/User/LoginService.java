package com.API.User;

import org.springframework.http.ResponseEntity;

import com.API.User.Entity.User;

import jakarta.servlet.http.HttpServletResponse;

public interface LoginService{

public ResponseEntity<?> loginUser(User user,HttpServletResponse response);
	
}
