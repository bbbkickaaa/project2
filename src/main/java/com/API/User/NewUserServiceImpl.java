package com.API.User;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
		 
		 	ResponseEntity<String> entity;
		 	String userid = user.getUserid();
		 	
		 	Optional<User> existingUser = userRepository.findByUserid(userid);

			if (existingUser.isPresent()) {
				entity = ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 사용자입니다.");
			}
			else {
				try {
		            userRepository.save(user);
		            entity = ResponseEntity.status(HttpStatus.CREATED).body("사용자가 생성되었습니다.");
		        } catch (Exception e) {
		            entity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 생성 중 오류가 발생했습니다.");
		        }
			}
			return entity;
		}


}
