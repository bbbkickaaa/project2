package com.API.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.API.Board.BoardRepository;
import com.API.Board.Entity.Board;
import com.API.User.DTO.SendPasswordDTO;
import com.API.User.DTO.UserDTO;
import com.API.User.Entity.User;
import com.API.User.Etc.PasswordGenerator;
import com.API.User.Etc.RandomNicknameGenerator;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
		private MailService mailservice;

	 @Autowired
	    private UserRepository userRepository;
	 
	 @Autowired
	  	private BoardRepository boardRepository;
	 
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	
	@Override
	public ResponseEntity<UserDTO> getUser(Authentication authentication){
		
		String userid = authentication.getName();
		Optional<User> user = userRepository.findByUserid(userid);
		if(user.isPresent()) {
			User users = user.get();
			UserDTO dto = UserDTO.builder()
                    .id(users.getId())
                    .userid(users.getUserid())
                    .nickname(users.getNickname())
                    .userLevel(users.getUserLevel())
                    .email(users.getEmail())
                    .createdDate(users.getCreatedDate())
                    .build();
			return ResponseEntity.ok().body(dto);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@Override
	public ResponseEntity<?> checkPostOwner(Authentication authentication , Long postId){
		String userid = authentication.getName();
		Optional<User> user = userRepository.findByUserid(userid);
		Optional<Board> board = boardRepository.findById(postId);
		
		if(user.isPresent() && board.isPresent()) {
			User users = user.get();
			Board boards = board.get();
			
			if(users.getId() == boards.getAuthor().getId()) {
				return ResponseEntity.ok().build();
			}else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
		}else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
	
	@Override
	public ResponseEntity<String> deleteUser(Authentication authentication, Long Id){
		String userid = authentication.getName();
		Optional<User> user = userRepository.findByUserid(userid);
		if(user.isPresent() && user.get().getId() == Id) {
			user.get().setDeletedDate(LocalDate.now().toString().replace("-", ""));
			user.get().setDeleted(true);
			userRepository.save(user.get());
			return ResponseEntity.ok("탈퇴되었습니다.");
		}else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("접근이 불가합니다.");
			
		}
	}
	
	@Override
	public ResponseEntity<String> alterUser(Authentication authentication, Map<String, Object> requestData){
		String userid = authentication.getName();
		Optional<User> user = userRepository.findByUserid(userid);
		String idAsString = (String) requestData.get("id");
		String nickname = (String) requestData.get("nickname");
		String password = (String) requestData.get("password");
		if(user.isPresent() && user.get().getUserid().equals(idAsString)) {
			String encodedPassword = passwordEncoder.encode(password);
			user.get().setNickname(nickname);
	        user.get().setPassword(encodedPassword);
			userRepository.save(user.get());
			return ResponseEntity.ok("저장되었습니다.");
		}else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("접근이 불가합니다.");
			
		}
	}
	
	@Override
	public ResponseEntity<?> checkUserIdToEmail(String userId){
		Optional<User> user = userRepository.findByUserid(userId);
		if(user.isPresent() && !user.get().isDeleted() ) {
			String email = user.get().getEmail();
			return ResponseEntity.ok(email);
		}
		else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("해당하는 정보가 없습니다.");
		}
	}
	
	@Override
	public ResponseEntity<?> setPasswordToEmail(SendPasswordDTO dto){
		Optional<User> user = userRepository.findByUserid(dto.getUserId());
		if(user.isPresent()) {
			String randomPassword = PasswordGenerator.generateRandomPassword(8);
			String password = passwordEncoder.encode(randomPassword);
			user.get().setPassword(password);
			userRepository.save(user.get());
			mailservice.mailSend2(dto.getEmail(),randomPassword);
			
			return ResponseEntity.ok("메일 전송 되었습니다.");
			
			
		}else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("해당하는 정보가 없습니다.");
		}
	};
}
