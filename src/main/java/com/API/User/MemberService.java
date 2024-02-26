package com.API.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.API.User.Entity.User;
import com.API.User.Entity.UserDTO;

public interface MemberService  {

	public ResponseEntity<UserDTO> getUser(Authentication authentication);
	public ResponseEntity<?> checkPostOwner(Authentication authentication , Long postId);
}
