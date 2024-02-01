package com.API.User;

import org.springframework.beans.factory.annotation.Autowired;   
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.API.User.Entity.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NewUserController {

	@Autowired
	NewUserService newUserService;
	
	@Autowired
	LoginService loginService;
	
	private final HttpSession httpSession;

	@PostMapping("/public/join")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return newUserService.createUser(user);
    }
	
	@PostMapping("/public/login")
	public ResponseEntity<String> loginUser(@RequestBody User user){
		return loginService.loginUser(user);
	}
	
	@PostMapping("/public/join/google")
    public String userProfile(@AuthenticationPrincipal OAuth2User principal) {
		System.out.print((String)principal.getAttribute("email"));
        return "User's email: " + principal.getAttribute("email");
    }
}
