package com.API.File;

import java.awt.image.BufferedImage;
import java.io.File; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.API.Board.BoardRepository;
import com.API.Board.DTO.BoardIdWithImgDTO;
import com.API.Board.Entity.Board;
import com.API.Board.Entity.BoardImage;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

import jakarta.transaction.Transactional;

@Service
public class FileService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BoardRepository boardRepository;
	
	 @Value("${profile.upload-dir}")
	    private String profileUploadDir;
	 
	 @Value("${board.upload-dir}")
	    private String boardUploadDir;

    public ResponseEntity<?> updateProfilePicture(MultipartFile file, Authentication authentication) {
    	String userId = authentication.getName();
    	Optional<User> userWrap = userRepository.findByUserid(userId);
    	if(userWrap.isEmpty()) {
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    	}
    	User user = userWrap.get();
    	
    	String fileName = user.getId() + "_profile.jpg"; // 파일명 생성
    	String filePath = profileUploadDir + File.separator + "user_" + user.getId(); // 유저별 디렉토리 경로
    	Path targetLocation = Paths.get(filePath).resolve(fileName); // 최종 파일 저장 경로
    	
    	String baseUrl = "http://localhost:8080/resources/user/";
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

@Transactional
public ResponseEntity<?> PostBoardImg(Authentication authentication, List<MultipartFile> files) {
	String userId = authentication.getName();
    Optional<User> userWrap = userRepository.findByUserid(userId);
    if (userWrap.isEmpty()) {
        return null;
    }
    User user = userWrap.get();
    Board board = new Board();
    List<BoardImage> imgList = new ArrayList<>();
    List<String> accessibleUrls = new ArrayList<>();

    files.forEach(file -> {
        try {
        	String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return;
            }
            
             String fileName = file.getOriginalFilename(); // 파일명 생성

	         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	         String currentTimestamp = LocalDateTime.now().format(formatter);
	         String filePath = boardUploadDir + File.separator + "user_" + user.getId() + File.separator + currentTimestamp; // 유저별 디렉토리 경로에 시각 추가
	         Path targetLocation = Paths.get(filePath).resolve(fileName); // 최종 파일 저장 경로
	
	         String baseUrl = "http://localhost:8080/resources/board/";
	         String userDir = "user_" + user.getId();
	         String accessibleUrl = baseUrl + userDir + "/" + currentTimestamp + "/" + fileName;

         if (!Files.exists(Paths.get(filePath))) {
             Files.createDirectories(Paths.get(filePath));
         }
            // 파일 저장
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            BufferedImage image = ImageIO.read(file.getInputStream());
            BoardImage img = new BoardImage();
            if (image == null) {
            	return;
            }
            int width = image.getWidth();
            int height = image.getHeight();

            // BoardImage 객체에 너비와 높이 설정
            img.setWidth(width);
            img.setHeight(height);

        	user.setPicture(accessibleUrl);
            accessibleUrls.add(accessibleUrl);
            img.setFileName(fileName);
            img.setFilePath(accessibleUrl);
            imgList.add(img);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		    });
		
		    // 임시 데이터 설정 및 저장
		    board.setTitle("임시 데이터입니다.");
		    board.setAuthor(user);
		    board.setContent("임시 데이터입니다.");
		    board.setBoardImage(imgList);
		    Board board_n = boardRepository.save(board);
		    BoardIdWithImgDTO Imgdto = new BoardIdWithImgDTO();
		    Imgdto.setId(board_n.getId());
		    Imgdto.setBoardImage(imgList);
		    return ResponseEntity.ok(Imgdto);  // 파일 URL 리스트 반환
		}
		
		}