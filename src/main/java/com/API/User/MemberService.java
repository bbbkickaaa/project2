package com.API.User;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.API.User.Entity.User;
import com.API.User.Entity.UserDTO;

public interface MemberService  {

	public ResponseEntity<UserDTO> getUser(Authentication authentication);
	public ResponseEntity<?> checkPostOwner(Authentication authentication , Long postId);
	public ResponseEntity<String> deleteUser(Authentication authentication, Long Id);
	public ResponseEntity<String> alterUser(Authentication authentication, Map<String, Object> requestData);
}
