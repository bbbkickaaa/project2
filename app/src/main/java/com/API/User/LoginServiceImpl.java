package com.API.User;

import org.springframework.security.core.Authentication; 
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.API.User.Entity.User;
import com.API.User.Jwt.JwtToken;
import com.API.User.Jwt.JwtTokenProvider;
import com.API.User.Security.Custom2AuthenticationProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService{
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	Custom2AuthenticationProvider authenticationProvider;

	@Override
	@Transactional
	public ResponseEntity<?> loginUser(User user, HttpServletResponse response) {
	        Authentication authentication = authenticationProvider.authenticate(
	            new UsernamePasswordAuthenticationToken(user.getUserid(), user.getPassword())
	        );
	        JwtToken token = tokenProvider.generateToken(authentication);
	        String accessToken = token.getAccessToken();
	        String refreshToken = token.getRefreshToken();
	        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
	        refreshTokenCookie.setHttpOnly(true);
	        refreshTokenCookie.setMaxAge(259200);
	        refreshTokenCookie.setDomain("localhost");
	        refreshTokenCookie.setPath("/api/public/refresh");
	        response.addCookie(refreshTokenCookie);
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + accessToken);
	        return ResponseEntity.ok().headers(headers).body("로그인 성공.");
	}
	
}
