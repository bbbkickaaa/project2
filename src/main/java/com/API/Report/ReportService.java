package com.API.Report;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.API.Report.DTO.ReportDTO;
import com.API.Report.Entity.Report;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

@Service
public class ReportService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ReportRepository reportRepository;

	public ResponseEntity<?> reportSubmit(ReportDTO dto, Authentication authentication) {
		
		String userId = authentication.getName();
		Optional<User> userWrap =  userRepository.findByUserid(userId);
		if(userWrap.isEmpty()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		User user = userWrap.get();
		Report report = new Report();
		report.setUserId(user);
		report.setCategory(dto.getType());
		report.setContent(dto.getContent());
		reportRepository.save(report);
		return ResponseEntity.ok("저장 되었습니다.");
	}

}
