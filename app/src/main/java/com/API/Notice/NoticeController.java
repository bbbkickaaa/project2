package com.API.Notice;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.API.Notice.DTO.NoticePostDTO;
import com.API.Notice.DTO.NoticeReviewDTO;
import com.API.User.Jwt.JwtTokenProvider;

@Controller
@RequestMapping("/api/notice")
public class NoticeController {
	
	@Autowired
	NoticeService noticeService;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@GetMapping("/get-all")
	public ResponseEntity<Page<NoticeReviewDTO>> findAll(
	    @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
	    @RequestHeader("Authorization") String authorizationHeader) {
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
		
		ResponseEntity<Page<NoticeReviewDTO>> results = noticeService.findAll(pageable,authentication);
	    return results;
	}
	

	@PostMapping("/post")
	public ResponseEntity<?> handlePost(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody NoticePostDTO dto){
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
	    return noticeService.postNotice(dto,authentication);
	}

	@PutMapping("/alter")
	public ResponseEntity<?> alterBoard(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody NoticePostDTO dto){
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
	    return noticeService.alterNotice(dto,authentication);
	}
	@GetMapping("/get-detail")
	public ResponseEntity<?> getDetail(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("id") Long id){
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		return noticeService.getDetail(id,authentication);
	}
	
	@GetMapping("/get-detail-only-alter")
	public ResponseEntity<?> findbyIdOnlyAlter(@RequestParam("id") Long id){
		return noticeService.findbyIdOnlyAlter(id);
	}
	
	
	@PostMapping("/post-views")
	public ResponseEntity<?> postViews(@RequestBody String noticeId){
		return noticeService.postViews(noticeId);
	}
	
	@PostMapping("/delete-notice")
	public ResponseEntity<?> deleteNotice(@RequestBody String noticeId){
		return noticeService.deleteNotice(noticeId);
	}
	@PostMapping("/post-recommend")
	public ResponseEntity<?> postRecommend(@RequestBody Map<String, Object> requestData){
		return noticeService.postRecommend(requestData);
	}
	
}
