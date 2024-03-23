package com.API.Alarm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.API.User.Jwt.JwtTokenProvider;

@Controller
@RequestMapping("/api/alarm")
public class AlarmController {

	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	AlarmService alarmService;
	
	
	@GetMapping("/new")
	public ResponseEntity<?> newAlarm(@RequestHeader("Authorization") String authorizationHeader){
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
		return alarmService.newAlarm(authentication);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(@RequestHeader("Authorization") String authorizationHeader, Pageable pageable){
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
		return alarmService.getAll(authentication,pageable);
	}
	
	@PutMapping("/read/{id}")
	public ResponseEntity<?> readAlarm(@PathVariable ("id") Long id){
		return alarmService.readAlarm(id);
	}
	
	@DeleteMapping("/read/{id}")
	public ResponseEntity<?> deleteAlarm(@PathVariable ("id") Long id){
		return alarmService.deleteAlarm(id);
	}
	
}
