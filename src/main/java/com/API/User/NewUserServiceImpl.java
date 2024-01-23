package com.API.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.API.User.Entity.User;

@Service
public class NewUserServiceImpl implements NewUserService {
	
	 @Autowired
	    private UserRepository userRepository;
	 
	 @Override
	 @Transactional(rollbackFor = Exception.class)
		public ResponseEntity<String> createUser(User user) {
			User existingUser = findByUserid(user.getUserid());

			if (existingUser != null) {
				
				return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 사용자입니다.");
			}

			userRepository.save(user);
			
			return ResponseEntity.status(HttpStatus.CREATED).body("사용자가 생성되었습니다.");
		}

		@Override
		public User findByUserid(String userid) {
			return userRepository.findByUserid(userid);
		}


}
