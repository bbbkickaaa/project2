package com.API.User;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import com.API.User.DTO.SendPasswordDTO;
import com.API.User.DTO.UserDTO;
import com.API.User.Entity.User;

public interface MemberService  {

	public ResponseEntity<UserDTO> getUser(Authentication authentication);
	public ResponseEntity<?> checkPostOwner(Authentication authentication , Long postId);
	public ResponseEntity<String> deleteUser(Authentication authentication, Long Id);
	public ResponseEntity<String> alterUser(Authentication authentication, Map<String, Object> requestData);
	public ResponseEntity<?> checkUserIdToEmail(String userId);
	public ResponseEntity<?> setPasswordToEmail(SendPasswordDTO dto);
	public ResponseEntity<?> getAnotherUser(Long id);
	public ResponseEntity<String> addFriendUser(Authentication authentication, String id);
	public ResponseEntity<String> blockUser(Authentication authentication, String id);
	public ResponseEntity<Page<?>> getFriend(Authentication authentication, Pageable pageable);
	ResponseEntity<?> deleteFriend(Authentication authentication, Integer id);
	public ResponseEntity<Page<?>> getBlock(Authentication authentication, Pageable pageable);
	public ResponseEntity<?> deleteBlock(Authentication authentication, Integer id);
	public ResponseEntity<?> getRole(Long id);
}
