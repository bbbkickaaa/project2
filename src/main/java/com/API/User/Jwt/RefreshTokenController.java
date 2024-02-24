package com.API.User.Jwt;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.API.User.Oauth2.CookieUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api")
@Controller
public class RefreshTokenController {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@PostMapping("/public/refresh")
	public ResponseEntity<?> ReGenerateAccessToken(@RequestHeader("Authorization") String authorizationHeader, HttpServletRequest request){
		Optional<Cookie> refreshToken = CookieUtils.getCookie(request,"refreshToken");
		if(refreshToken.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}else {
			String refreshTokenValue = refreshToken.get().getValue();
			
			if(tokenProvider.validateRefreshToken(refreshTokenValue)) {
				String authorization = tokenProvider.resolveToken(authorizationHeader);
		        Authentication authentication =  tokenProvider.getAuthentication(authorization);
		        String accessToken = tokenProvider.generateToken(authentication).getAccessToken();
		        HttpHeaders headers = new HttpHeaders();
		        headers.set("Authorization", "Bearer " + accessToken);
		        return ResponseEntity.ok().headers(headers).body("로그인 성공.");
			}
			else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("토큰이 만료되었습니다.");

			}
			
			
		}
		
	}
}
