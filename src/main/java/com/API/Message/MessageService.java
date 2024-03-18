package com.API.Message;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.API.Message.DTO.MessageDTO;
import com.API.Message.Entity.Message;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

@Service
public class MessageService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	

	public ResponseEntity<?> postMessage(Authentication authentication, MessageDTO messageDTO){
		String userid = authentication.getName();
		Optional<User> user = userRepository.findByUserid(userid);
		Optional<User> user2 = userRepository.findById(messageDTO.getReceiveId());
		if(user.isEmpty() || user2.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		if(user.get().getId().equals(user2.get().getId())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("자기에겐 보낼 수 없습니다.");
		}
		Message message = new Message();
		message.setForwardId(user.get());
		message.setReceiveId(user2.get());
		message.setContent(messageDTO.getContent());
		messageRepository.save(message);
		return ResponseEntity.ok("전송되었습니다.");
	}
}
