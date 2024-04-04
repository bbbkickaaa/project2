package com.API.Report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.API.Report.DTO.ReportDTO;
import com.API.User.Jwt.JwtTokenProvider;

@Controller
@RequestMapping("/api")
public class ReportController {
	
	@Autowired
	ReportService reportService;

	@Autowired
	JwtTokenProvider tokenProvider;
	
	@PostMapping("/report/submit")
	public ResponseEntity<?> reportSubmit (@RequestBody ReportDTO dto, @RequestHeader("Authorization") String authorizationHeader){
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		 return reportService.reportSubmit(dto,authentication);
	}
	
	@GetMapping("/report/get-report")
	public ResponseEntity<?> getReport (@RequestParam("id") Long id){
		 return reportService.getReport(id);
	}
}
