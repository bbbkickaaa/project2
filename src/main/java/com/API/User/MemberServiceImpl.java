package com.API.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.API.User.Entity.User;
import com.API.User.Entity.UserDTO;

@Service
public class MemberServiceImpl implements MemberService {

	 @Autowired
	    private UserRepository userRepository;
	
	@Override
	public ResponseEntity<UserDTO> getUser(Authentication authentication){
		
		String userid = authentication.getName();
		Optional<User> user = userRepository.findByUserid(userid);
		if(user.isPresent()) {
			User users = user.get();
			UserDTO dto = UserDTO.builder()
                    .id(users.getId())
                    .userid(users.getUserid())
                    .nickname(users.getNickname())
                    .userLevel(users.getUserLevel())
                    .email(users.getEmail())
                    .createdDate(users.getCreatedDate())
                    .build();
			return ResponseEntity.ok().body(dto);
		}
		
		return ResponseEntity.notFound().build();
	}
}
