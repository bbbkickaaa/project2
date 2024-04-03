package com.API.File;

import java.awt.Graphics2D;
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
import com.API.File.DTO.AttrDTO;
import com.API.Notice.NoticeRepository;
import com.API.Notice.DTO.NoticeIdWithImgDTO;
import com.API.Notice.Entity.Notice;
import com.API.Notice.Entity.NoticeImage;
import com.API.User.UserRepository;
import com.API.User.Entity.User;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;

import jakarta.transaction.Transactional;

@Service
public class FileService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	NoticeRepository noticeRepository;
	
	 @Value("${profile.upload-dir}")
	    private String profileUploadDir;
	 
	 @Value("${board.upload-dir}")
	    private String boardUploadDir;
	 
	 @Value("${notice.upload-dir}")
	 private String noticeUploadDir;
	 
	 @Value("${host-dir}")
	 private String hostDir;

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
    	System.out.println(accessibleUrl);
    	
    	return ResponseEntity.ok(accessibleUrl);

    	
    }

@Transactional
public ResponseEntity<?> PostBoardImg(Authentication authentication, List<MultipartFile> files, List<AttrDTO> attrs) {
    String userId = authentication.getName();
    Optional<User> userWrap = userRepository.findByUserid(userId);
    if (userWrap.isEmpty()) {
        return ResponseEntity.badRequest().body("User not found");
    }
    User user = userWrap.get();
    Board board = new Board();
    List<BoardImage> imgList = new ArrayList<>();

    for (int i = 0; i < files.size(); i++) {
        MultipartFile file = files.get(i);
        AttrDTO attr = attrs.get(i);
        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                continue;
            }

            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            String widthStr = attr.getStyle().getWidth();
            String heightStr = attr.getStyle().getHeight();
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            int desiredWidth = (widthStr.equals("100%") || widthStr.equals("auto")) ? originalWidth : parseDimension(widthStr);
            int desiredHeight = (heightStr.equals("100%") || heightStr.equals("auto")) ? originalHeight : parseDimension(heightStr);

	
	             // 이미지 리사이징 수행
	             BufferedImage resizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);
	             Graphics2D g = resizedImage.createGraphics();
	             g.drawImage(originalImage, 0, 0, desiredWidth, desiredHeight, null);
	             g.dispose();
	             originalImage = resizedImage; // 리사이징된 이미지를 originalImage 변수에 할당
	
	         String fileName = file.getOriginalFilename();
	         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	         String currentTimestamp = LocalDateTime.now().format(formatter);
	         String filePath = boardUploadDir + File.separator + "user_" + user.getId() + File.separator + currentTimestamp;
	         Path targetLocation = Paths.get(filePath).resolve(fileName);
	         if (!Files.exists(targetLocation.getParent())) {
	             Files.createDirectories(targetLocation.getParent());
	         }
	         File outputFile = targetLocation.toFile();
	         ImageIO.write(originalImage, "jpg", outputFile);

            String baseUrl =hostDir+"/resources/board/";
            String accessibleUrl = baseUrl + "user_" + user.getId() + "/" + currentTimestamp + "/" + fileName;
            BoardImage img = new BoardImage();
            img.setWidth(desiredWidth);
            img.setHeight(desiredHeight);
            img.setFileName(fileName);
            img.setFilePath(accessibleUrl);
            imgList.add(img);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    board.setTitle("임시 데이터입니다.");
    board.setAuthor(user);
    board.setContent("임시 데이터입니다.");
    board.setBoardImage(imgList);
    Board board_n = boardRepository.save(board);

    BoardIdWithImgDTO Imgdto = new BoardIdWithImgDTO();
    Imgdto.setId(board_n.getId());
    Imgdto.setBoardImage(imgList);

    return ResponseEntity.ok(Imgdto);
}
private static int parseDimension(String dimension) {
    if (dimension == null || dimension.isEmpty() || dimension.equals("100%") || dimension.equals("auto")) {
        return 800;
    }

    // "px" 제거
    dimension = dimension.replace("px", "");

    // 소수점 이전의 숫자 부분만 추출
    int dotIndex = dimension.indexOf('.');
    if (dotIndex != -1) {
        dimension = dimension.substring(0, dotIndex);
    }

    // 숫자만 추출
    dimension = dimension.replaceAll("[^0-9]", "");

    try {
        return Integer.parseInt(dimension);
    } catch (NumberFormatException e) {
        return 800; // 파싱 실패 시 800 반환
    }
}

public ResponseEntity<?> PostBoardImgAlter(Authentication authentication, List<MultipartFile> files,
		List<AttrDTO> attrs, Long boardId) {
	String userId = authentication.getName();
    Optional<User> userWrap = userRepository.findByUserid(userId);
    Optional<Board> boardWrap = boardRepository.findById(boardId);
    if (userWrap.isEmpty() || boardWrap.isEmpty()){
        return ResponseEntity.badRequest().body("not found");
    }
    User user = userWrap.get();
    Board board = boardWrap.get();
    List<BoardImage> imgList = new ArrayList<>();

    for (int i = 0; i < files.size(); i++) {
        MultipartFile file = files.get(i);
        AttrDTO attr = attrs.get(i);
        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                continue;
            }

            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            String widthStr = attr.getStyle().getWidth();
            String heightStr = attr.getStyle().getHeight();
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int desiredWidth = (widthStr.equals("100%") || widthStr.equals("auto")) ? originalWidth : parseDimension(widthStr);
            int desiredHeight = (heightStr.equals("100%") || heightStr.equals("auto")) ? originalHeight : parseDimension(heightStr);


	
	             // 이미지 리사이징 수행
	             BufferedImage resizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);
	             Graphics2D g = resizedImage.createGraphics();
	             g.drawImage(originalImage, 0, 0, desiredWidth, desiredHeight, null);
	             g.dispose();
	             originalImage = resizedImage; // 리사이징된 이미지를 originalImage 변수에 할당
	
	         String fileName = file.getOriginalFilename();
	         
	         String url;
	         String currentTimestamp;

	         if (board.getBoardImage() != null && !board.getBoardImage().isEmpty()) {
	             url = board.getBoardImage().stream().findFirst().map(BoardImage::getFilePath).toString();
	             String[] parts = url.split("/");
	             currentTimestamp = parts[5];
	         } else {
	             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	             currentTimestamp = LocalDateTime.now().format(formatter);
	         }

	         String filePath = boardUploadDir + File.separator + "user_" + user.getId() + File.separator + currentTimestamp;
	         Path targetLocation = Paths.get(filePath).resolve(fileName);
	         
	         
	         
	         if (!Files.exists(targetLocation.getParent())) {
	             Files.createDirectories(targetLocation.getParent());
	         }
	         File outputFile = targetLocation.toFile();
	         ImageIO.write(originalImage, "jpg", outputFile);

	        String baseUrl =hostDir+"/resources/board/";
            String accessibleUrl = baseUrl + "user_" + user.getId() + "/" + currentTimestamp + "/" + fileName;
            BoardImage img = new BoardImage();
            img.setWidth(desiredWidth);
            img.setHeight(desiredHeight);
            img.setFileName(fileName);
            img.setFilePath(accessibleUrl);
            imgList.add(img);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    board.setBoardImage(imgList);
    boardRepository.save(board);

    BoardIdWithImgDTO Imgdto = new BoardIdWithImgDTO();
    Imgdto.setId(board.getId());
    Imgdto.setBoardImage(imgList);

    return ResponseEntity.ok(Imgdto);
}

public ResponseEntity<?> PostNoticeImg(Authentication authentication, List<MultipartFile> files, List<AttrDTO> attrs) {
	 String userId = authentication.getName();
	    Optional<User> userWrap = userRepository.findByUserid(userId);
	    if (userWrap.isEmpty()) {
	        return ResponseEntity.badRequest().body("User not found");
	    }
	    User user = userWrap.get();
	    Notice notice = new Notice();
	    List<NoticeImage> imgList = new ArrayList<>();

	    for (int i = 0; i < files.size(); i++) {
	        MultipartFile file = files.get(i);
	        AttrDTO attr = attrs.get(i);
	        try {
	            String contentType = file.getContentType();
	            if (contentType == null || !contentType.startsWith("image/")) {
	                continue;
	            }

	            BufferedImage originalImage = ImageIO.read(file.getInputStream());
	            String widthStr = attr.getStyle().getWidth();
	            String heightStr = attr.getStyle().getHeight();
	            int originalWidth = originalImage.getWidth();
	            int originalHeight = originalImage.getHeight();

	            int desiredWidth = (widthStr.equals("100%") || widthStr.equals("auto")) ? originalWidth : parseDimension(widthStr);
	            int desiredHeight = (heightStr.equals("100%") || heightStr.equals("auto")) ? originalHeight : parseDimension(heightStr);

		
		             // 이미지 리사이징 수행
		             BufferedImage resizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);
		             Graphics2D g = resizedImage.createGraphics();
		             g.drawImage(originalImage, 0, 0, desiredWidth, desiredHeight, null);
		             g.dispose();
		             originalImage = resizedImage; // 리사이징된 이미지를 originalImage 변수에 할당
		
		         String fileName = file.getOriginalFilename();
		         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		         String currentTimestamp = LocalDateTime.now().format(formatter);
		         String filePath = noticeUploadDir + File.separator + "admin_" + user.getId() + File.separator + currentTimestamp;
		         Path targetLocation = Paths.get(filePath).resolve(fileName);
		         if (!Files.exists(targetLocation.getParent())) {
		             Files.createDirectories(targetLocation.getParent());
		         }
		         File outputFile = targetLocation.toFile();
		         ImageIO.write(originalImage, "jpg", outputFile);

	            String baseUrl =hostDir+"/resources/notice/";
	            String accessibleUrl = baseUrl + "admin_" + user.getId() + "/" + currentTimestamp + "/" + fileName;
	            NoticeImage img = new NoticeImage();
	            img.setWidth(desiredWidth);
	            img.setHeight(desiredHeight);
	            img.setFileName(fileName);
	            img.setFilePath(accessibleUrl);
	            imgList.add(img);

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    notice.setTitle("임시 데이터입니다.");
	    notice.setAuthor(user);
	    notice.setContent("임시 데이터입니다.");
	    notice.setNoticeImage(imgList);
	    Notice notice_n = noticeRepository.save(notice);

	    NoticeIdWithImgDTO Imgdto = new NoticeIdWithImgDTO();
	    Imgdto.setId(notice_n.getId());
	    Imgdto.setNoticeImage(imgList);

	    return ResponseEntity.ok(Imgdto);
}

public ResponseEntity<?> PostNoticeImgAlter(Authentication authentication, List<MultipartFile> files,
		List<AttrDTO> attrs, Long noticeId) {
	String userId = authentication.getName();
    Optional<User> userWrap = userRepository.findByUserid(userId);
    Optional<Notice> noticeWrap = noticeRepository.findById(noticeId);
    if (userWrap.isEmpty() || noticeWrap.isEmpty()){
        return ResponseEntity.badRequest().body("not found");
    }
    User user = userWrap.get();
    Notice notice = noticeWrap.get();
    List<NoticeImage> imgList = new ArrayList<>();

    for (int i = 0; i < files.size(); i++) {
        MultipartFile file = files.get(i);
        AttrDTO attr = attrs.get(i);
        try {
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                continue;
            }

            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            String widthStr = attr.getStyle().getWidth();
            String heightStr = attr.getStyle().getHeight();
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int desiredWidth = (widthStr.equals("100%") || widthStr.equals("auto")) ? originalWidth : parseDimension(widthStr);
            int desiredHeight = (heightStr.equals("100%") || heightStr.equals("auto")) ? originalHeight : parseDimension(heightStr);


	
	             // 이미지 리사이징 수행
	             BufferedImage resizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);
	             Graphics2D g = resizedImage.createGraphics();
	             g.drawImage(originalImage, 0, 0, desiredWidth, desiredHeight, null);
	             g.dispose();
	             originalImage = resizedImage; // 리사이징된 이미지를 originalImage 변수에 할당
	
	         String fileName = file.getOriginalFilename();
	         
	         String url;
	         String currentTimestamp;

	         if (notice.getNoticeImage() != null && !notice.getNoticeImage().isEmpty()) {
	             url = notice.getNoticeImage().stream().findFirst().map(NoticeImage::getFilePath).toString();
	             String[] parts = url.split("/");
	             currentTimestamp = parts[5];
	         } else {
	             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	             currentTimestamp = LocalDateTime.now().format(formatter);
	         }

	         String filePath = noticeUploadDir + File.separator + "admin_" + user.getId() + File.separator + currentTimestamp;
	         Path targetLocation = Paths.get(filePath).resolve(fileName);
	         
	         
	         
	         if (!Files.exists(targetLocation.getParent())) {
	             Files.createDirectories(targetLocation.getParent());
	         }
	         File outputFile = targetLocation.toFile();
	         ImageIO.write(originalImage, "jpg", outputFile);

	        String baseUrl =hostDir+"/resources/notice/";
            String accessibleUrl = baseUrl + "admin_" + user.getId() + "/" + currentTimestamp + "/" + fileName;
            NoticeImage img = new NoticeImage();
            img.setWidth(desiredWidth);
            img.setHeight(desiredHeight);
            img.setFileName(fileName);
            img.setFilePath(accessibleUrl);
            imgList.add(img);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    notice.setNoticeImage(imgList);
    noticeRepository.save(notice);

    NoticeIdWithImgDTO Imgdto = new NoticeIdWithImgDTO();
    Imgdto.setId(notice.getId());
    Imgdto.setNoticeImage(imgList);

    return ResponseEntity.ok(Imgdto);
}
	
	
}
