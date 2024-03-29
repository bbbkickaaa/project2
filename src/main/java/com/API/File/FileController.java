package com.API.File;

import java.io.IOException; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.API.Board.DTO.BoardPostDTO;
import com.API.File.DTO.AttrDTO;
import com.API.User.Jwt.JwtTokenProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/api")
public class FileController {
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@Autowired
	FileService fileSerivce;

	@PostMapping("/upload-profile")
    public ResponseEntity<?> uploadProfile(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일이 제공되지 않았습니다.");
        }
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		
        return fileSerivce.updateProfilePicture(file,authentication);
    }
	@PostMapping("/file/board-img")
	public ResponseEntity<?> PostBoardImg(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestParam("files") List<MultipartFile> files,
			@RequestParam("attrs") String attrsJson) throws IOException  {
		ObjectMapper mapper = new ObjectMapper();
		List<AttrDTO> attrs = mapper.readValue(attrsJson, new TypeReference<List<AttrDTO>>() {});
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
	    return fileSerivce.PostBoardImg(authentication,files,attrs);
	}
	

}
