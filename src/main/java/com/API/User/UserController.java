package com.API.User;

import org.springframework.beans.factory.annotation.Autowired;    
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.User.Entity.User;
import com.API.User.Entity.UserDTO;
import com.API.User.Jwt.JwtTokenProvider;
import com.API.User.Oauth2.CookieUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	NewUserService newUserService;
	@Autowired
	MemberService memberService;
	@Autowired
	LoginService loginService;
	
	@PostMapping("/public/join")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return newUserService.createUser(user);
    }
	
	@PostMapping("/public/login")
	public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletResponse response){
		return loginService.loginUser(user,response);
	}
	
	@PostMapping("/public/logout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request ,HttpServletResponse response){
	    CookieUtils.deleteCookie(request, response, "refreshToken");
        return ResponseEntity.ok().body("삭제되었습니다.");
	}
	
	@GetMapping("/member/getUser")
	public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String authorizationHeader) {
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
        return memberService.getUser(authentication);
	}
	@GetMapping("member/check-post-owner/{id}")
	public ResponseEntity<?> checkPostOwner(@RequestHeader("Authorization") String authorizationHeader , @PathVariable("id") Long id){
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		return memberService.checkPostOwner(authentication,id);
	}
}
