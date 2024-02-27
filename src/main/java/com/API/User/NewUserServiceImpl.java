package com.API.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.API.User.Entity.User;
import com.API.User.Entity.UserLevel;
import com.API.User.Entity.UserRole;
import com.API.User.Etc.RandomNicknameGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewUserServiceImpl implements NewUserService{
	
	 @Autowired
	    private UserRepository userRepository;
	 
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	 
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
					String encodedPassword = passwordEncoder.encode(user.getPassword());
			        String nickName = RandomNicknameGenerator.generateRandomNickname();
			        user.setPassword(encodedPassword);
			        user.setNickname(nickName);
			        user.setRole(UserRole.USER);
			        user.setUserLevel(new UserLevel(1,0));
		            userRepository.save(user);
		            entity = ResponseEntity.status(HttpStatus.CREATED).body("사용자가 생성되었습니다.");
		        } catch (Exception e) {
		            entity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 생성 중 오류가 발생했습니다.");
		        }
			}
			return entity;
		}

	}


