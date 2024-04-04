package com.API.Alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.API.Alarm.Entity.Alarm;
import com.API.Board.BoardRepository;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

@Service
public class AlarmService {
	
	@Autowired
	AlarmRepository alarmRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BoardRepository boardRepository;

	public ResponseEntity<?> getAll(Authentication authentication, Pageable pageable) {
		String userId = authentication.getName();
		Optional<User> userWrap = userRepository.findByUserid(userId);
		if(userWrap.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		User user = userWrap.get();
		Long id = user.getId();
		List<Alarm> alarmWrap = alarmRepository.findByrecipientId(id,pageable);
		if(alarmWrap.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(alarmWrap);
	}

	public ResponseEntity<?> newAlarm(Authentication authentication) {
		String userId = authentication.getName();
		Optional<User> userWrap = userRepository.findByUserid(userId);
		if(userWrap.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		User user = userWrap.get();
		Long id = user.getId();
		Long count = alarmRepository.countByRecipientId(id);
		
		return ResponseEntity.ok(count);
	}

	public ResponseEntity<?> readAlarm(Long id) {
		Optional<Alarm> alarmWrap = alarmRepository.findById(id);
		if(alarmWrap.isEmpty()){
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}else {
			Alarm alarm = alarmWrap.get();
			alarm.setView(true);
			alarmRepository.save(alarm);
			return ResponseEntity.ok("수정되었습니다.");
		}
	}

	public ResponseEntity<?> deleteAlarm(Long id) {
		alarmRepository.deleteById(id);
		return ResponseEntity.ok("삭제되었습니다.");
	}

}
