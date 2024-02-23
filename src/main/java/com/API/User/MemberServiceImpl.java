package com.API.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.API.User.Entity.User;

@Service
public class MemberServiceImpl implements MemberService {

	 @Autowired
	    private UserRepository userRepository;
	
	@Override
	public ResponseEntity<User> getUser(Authentication authentication){
		
		String userid = authentication.getName();
		Optional<User> user = userRepository.findByUserid(userid);
		if(user.isPresent()) {
			
			return ResponseEntity.ok().body(user.get());
		}
		
		return ResponseEntity.notFound().build();
	}
}
