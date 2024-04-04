package com.API.Admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.API.Admin.DTO.BlockSiteUserDTO;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

@Service
public class AdminService {
	
	@Autowired UserRepository userRepository;

	public ResponseEntity<?> blockBySite(BlockSiteUserDTO dto) {
		Long duration = dto.getDuration();
		Optional<User> userWrap =  userRepository.findById(dto.getId());
		if(userWrap.isEmpty() || userWrap.get().getRole().equals("ADMIN")) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
		User user = userWrap.get();
		String bannedtime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
		
		String Endtime = LocalDateTime.now().plusSeconds(duration).format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
		user.setBannedDate(bannedtime);
		user.setEndBannedDate(Endtime);
		userRepository.save(user);
		return ResponseEntity.ok("적용 되었습니다.");
	}
	
	
}
