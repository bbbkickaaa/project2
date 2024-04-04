package com.API.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.API.User.Entity.User;

import jakarta.servlet.http.HttpServletResponse;

public interface LoginService{

public ResponseEntity<?> loginUser(User user,HttpServletResponse response);

	
}
