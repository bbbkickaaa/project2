package com.API.User;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;    
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.API.User.DTO.EmailCheckDTO;
import com.API.User.DTO.EmailRequestDTO;
import com.API.User.DTO.SendPasswordDTO;
import com.API.User.DTO.UserDTO;
import com.API.User.Entity.User;
import com.API.User.Jwt.JwtTokenProvider;
import com.API.User.Oauth2.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final MailService mailService;
	
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
	
	@DeleteMapping("/public/logout")
	public ResponseEntity<?> logoutUser(HttpServletRequest request ,HttpServletResponse response){
	    CookieUtils.deleteCookie(request, response, "refreshToken");
        return ResponseEntity.ok().body("삭제되었습니다.");
	}
	
	@GetMapping("/member/get-user")
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
	
	@PostMapping("member/delete-user")
	public ResponseEntity<String> deleteUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody String id){
		Long userId = Long.valueOf(id);
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		return memberService.deleteUser(authentication,userId);
	}
	
	@PostMapping("member/alter-user")
	public ResponseEntity<String> alterUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, Object> requestData){
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		return memberService.alterUser(authentication,requestData);
	}
	
    @PostMapping ("/public/mail-send")
    public ResponseEntity<?> mailSend(@RequestBody @Valid EmailRequestDTO emailDto){
    System.out.println(emailDto.getEmail());
        return mailService.joinEmail(emailDto.getEmail());
	    }
    
    @PostMapping("/public/mail-auth-check")
    public ResponseEntity<String> AuthCheck(@RequestBody @Valid EmailCheckDTO emailCheckDto){
        return mailService.checkAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum());
    }
    
    @PostMapping("/public/forgot-password")
    public ResponseEntity<?> checkUserIdToEmail(@RequestBody String userId){
        return memberService.checkUserIdToEmail(userId);
    }
    
    @PostMapping("/public/send-password")
    public ResponseEntity<?> setPasswordToEmail(@RequestBody SendPasswordDTO dto){
        return memberService.setPasswordToEmail(dto);
    }
    
    @GetMapping("member/get-another-user")
    public ResponseEntity<?> getAnotherUser(@RequestParam("id") Long id){
    	return memberService.getAnotherUser(id);
    }
    
    @PostMapping("member/block-user")
	public ResponseEntity<String> blockUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody String Id){
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		return memberService.blockUser(authentication,Id);
	}
    
    @PostMapping("member/add-friend-user")
	public ResponseEntity<String> addFriendUser(@RequestHeader("Authorization") String authorizationHeader, @RequestBody String Id){
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		return memberService.addFriendUser(authentication,Id);
	}
    
}
