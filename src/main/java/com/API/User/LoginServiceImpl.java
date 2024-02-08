package com.API.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;  
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
public class LoginServiceImpl implements LoginService , UserDetailsService{

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
			 try {
				 
					 UserDetails userDetails = loadUserByUsername(user.getUserid());
					 
				 if (!passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
			            entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다.");
			        }
				 else {
				       Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				       JwtToken token = jwtTokenProvider.generateToken(authentication);
				       HttpHeaders headers = new HttpHeaders();
				       headers.set("Authorization", "Bearer " + token);
				       entity = ResponseEntity.ok().headers(headers).body("로그인 성공.");
				 		}
				 }
			 catch (UsernameNotFoundException e) {
			    	entity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패했습니다.");
			    }
			 return entity;
	 }


	 @Override
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	     return userRepository.findByUserid(username)
	         .map(user -> new org.springframework.security.core.userdetails.User(
	             user.getUserid(), 
	             user.getPassword(), 
	             getAuthorities(user)))
	         .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
	 }

	 private Collection<? extends GrantedAuthority> getAuthorities(User user) {
	     return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
	 }


}
