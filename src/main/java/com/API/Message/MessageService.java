package com.API.Message;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.API.Alarm.AlarmRepository;
import com.API.Alarm.Entity.Alarm;
import com.API.Alarm.Entity.Alarm.AlarmType;
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
	@Autowired
	AlarmRepository alarmRepository;

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
		Message messageSaved = messageRepository.save(message);
		
		Alarm alarm = new Alarm();
		alarm.setContent("새로운 메시지가 도착했습니다.");
		alarm.setForwardNickname(user.get().getNickname());
		alarm.setForwarduserId(user.get().getId());
		alarm.setRecipientId(user2.get().getId());
		alarm.setType(AlarmType.MESSAGE);
		alarm.setReferencedId(messageSaved.getId());
		alarmRepository.save(alarm);
		return ResponseEntity.ok("전송되었습니다.");
	}

	public ResponseEntity<?> getMessage(Long id) {
		Optional<Message> messageWrap = messageRepository.findById(id);
		if(messageWrap.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("오류가 발생했습니다.");
		}else {
			Message message = messageWrap.get();
			MessageDTO dto = new MessageDTO();
			dto.setForwardNickname(message.getForwardId().getNickname());
			dto.setContent(message.getContent());
			dto.setForwardId(message.getForwardId().getId());
			dto.setReceiveId(message.getReceiveId().getId());
			dto.setReceivedNickname(message.getReceiveId().getNickname());
			return ResponseEntity.ok().body(dto);
		}
	}
}
