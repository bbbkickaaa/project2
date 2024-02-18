package com.API.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	CustomAuthenticationProvider authenticationProvider;

	@Override
	@Transactional
	public ResponseEntity<String> loginUser(User user) {
		
	    try {
	        Authentication authentication = authenticationProvider.authenticate(
	            new UsernamePasswordAuthenticationToken(user.getUserid(), user.getPassword())
	        );

	        JwtToken token = jwtTokenProvider.generateToken(authentication);
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + token);
	        return ResponseEntity.ok().headers(headers).body("로그인 성공.");
	    } catch (AuthenticationException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패.");
	    }
	}
}
