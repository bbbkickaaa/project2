package com.API.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.API.Message.DTO.MessageDTO;
import com.API.User.Jwt.JwtTokenProvider;

@Controller
@RequestMapping("/api/message")
public class MessageController {
	
	@Autowired
	MessageService messageService;
	@Autowired
	JwtTokenProvider tokenProvider;

	
	@PostMapping("/post-message")
	public ResponseEntity<?> postMessage(@RequestHeader("Authorization") String authorizationHeader, @RequestBody MessageDTO messageDTO){
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		return messageService.postMessage(authentication,messageDTO);
	} 
	
	@GetMapping("/get-message/{id}")
	public ResponseEntity<?> getMessage(@PathVariable("id") Long id){
		System.out.println(123);
		return messageService.getMessage(id);
	}
}
