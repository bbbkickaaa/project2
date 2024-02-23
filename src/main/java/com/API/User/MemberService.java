package com.API.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.API.User.Entity.User;

public interface MemberService  {

	public ResponseEntity<User> getUser(Authentication authentication);

}
