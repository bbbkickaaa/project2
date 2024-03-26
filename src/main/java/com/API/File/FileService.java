package com.API.File;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.API.File.Entity.UserProfile;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

@Service
public class FileService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
    private UserProfileRepository userProfileRepository;
	
	 @Value("${file.upload-dir}")
	    private String uploadDir;

    public ResponseEntity<?> updateProfilePicture(MultipartFile file, Authentication authentication) {
    	String userId = authentication.getName();
    	Optional<User> userWrap = userRepository.findByUserid(userId);
    	if(userWrap.isEmpty()) {
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    	}
    	User user = userWrap.get();
    	
    	String fileName = user.getId() + "_profile.jpg"; // 파일명 생성
    	String filePath = uploadDir + File.separator + "user_" + user.getId(); // 유저별 디렉토리 경로
    	Path targetLocation = Paths.get(filePath).resolve(fileName); // 최종 파일 저장 경로
    	
    	String baseUrl = "http://localhost:8080/resources/";
    	String userDir = "user_" + user.getId();
    	String accessibleUrl = baseUrl + userDir + "/" + fileName;
    	user.setPicture(accessibleUrl);
    	
    	Path userDirectory = Paths.get(filePath);
    	if (!Files.exists(userDirectory)) {
    	    try {
				Files.createDirectories(userDirectory);
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}
    	}
    	try {
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} 
    	userRepository.save(user);
    	return ResponseEntity.ok(accessibleUrl);

    	
    }
}