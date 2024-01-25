package com.API.User;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.API.User.Entity.User;
import com.configuration.LoggingFactory;

@Service
public class LoginServiceImpl implements LoginService{

	 @Autowired
	    private UserRepository userRepository;
	 
	 private static final Logger logger = LoggingFactory.getLogger();
	 
	 public ResponseEntity<String> loginUser(User user) {
	 
		 ResponseEntity<String> entity;
		 String userid = user.getUserid();
		 String password = user.getPassword();
		 Optional<User> existingUser = userRepository.findByUserid(userid);
		 System.out.println();
		 if(existingUser.isEmpty()) {
			 entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패했습니다.");
		 }
		 else {
			if(existingUser.get().getPassword().equals(password)) {
				entity = ResponseEntity.status(HttpStatus.ACCEPTED).body("정상적으로 로그인 되었습니다.");
			}
			else {
				entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패했습니다.");
			}
		 }
		 
		 return entity;
	 }


}
