package com.API.User;

import java.util.Optional;  
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.API.User.Entity.User;
import com.API.User.Oauth2.JwtToken;
import com.API.User.Oauth2.JwtTokenProvider;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService{

	@Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
	 
	 
	 @Override
	 @Transactional
		 public ResponseEntity<String> loginUser(User user) {
		 
			 ResponseEntity<String> entity;
			 String userid = user.getUserid();
			 String password = user.getPassword();
			 Optional<User> existingUser = userRepository.findByUserid(userid);
			 
			 if (existingUser.isPresent() && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
			        CustomUserDetails userDetails = new CustomUserDetails(existingUser.get());
			        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			       JwtToken token = jwtTokenProvider.generateToken(authentication);
			        entity = ResponseEntity.ok().body("로그인 성공. Token: " + token);
			    } else {
			    	entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패했습니다.");
			    	log.atTrace();
			    }
			 return entity;
		 }


}
