package com.API.User;

import org.springframework.beans.factory.annotation.Autowired;    
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.User.Entity.User;
import com.API.User.Oauth2.JwtTokenProvider;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;
	
	@Autowired
	NewUserService newUserService;
	
	@Autowired
	LoginService loginService;
	
	@PostMapping("/public/join")
    public ResponseEntity<String> createUser(@RequestBody User user) {
		System.out.println(user);
        return newUserService.createUser(user);
    }
	
	@PostMapping("/public/login")
	public ResponseEntity<String> loginUser(@RequestBody User user){
		return loginService.loginUser(user);
	}
	
}
