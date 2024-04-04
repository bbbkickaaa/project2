package com.API.Board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.API.Board.DTO.BoardDTO;
import com.API.Board.DTO.BoardPostDTO;
import com.API.Board.DTO.BoardReviewDTO;
import com.API.Board.DTO.DeleteCommentDTO;
import com.API.Board.Entity.Board;
import com.API.File.FileService;
import com.API.Report.DTO.ReportDTO;
import com.API.User.DTO.UserDTO;
import com.API.User.Jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/api/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	JwtTokenProvider tokenProvider;

	@GetMapping("/get-all")
	public ResponseEntity<Page<BoardReviewDTO>> findAll(
		   @RequestParam(name = "category1", required = false) String category1,
		   @RequestParam(name = "category2", required = false) String category2,
		   @RequestParam(name = "category3", required = false) String category3,
		   @RequestParam(name = "option", required = false) String option, //검색 타입
		   @RequestParam(name = "content", required = false) String content, //검색 내용 
	    @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
	    @RequestHeader("Authorization") String authorizationHeader) {
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
		
		ResponseEntity<Page<BoardReviewDTO>> results = boardService.findAll(category3,category2,category1,option,content,pageable,authentication);
	    return results;
	}
	
	
	@GetMapping("/favorite")
	public ResponseEntity<Page<BoardReviewDTO>> favorite(
			@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
			@RequestHeader("Authorization") String authorizationHeader){
			String token = tokenProvider.resolveToken(authorizationHeader);
			Authentication authentication =  tokenProvider.getAuthentication(token);
			return boardService.favorite(pageable,authentication);
	}
	
	@GetMapping("/report")
	public ResponseEntity<Page<ReportDTO>> report(
			@PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
	return boardService.report(pageable);
	}
	
	@PostMapping("/post")
	public ResponseEntity<?> handlePost(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody BoardPostDTO dto){
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
	    return boardService.postBoard(dto,authentication);
	}
	
	
	
	@PutMapping("/alter")
	public ResponseEntity<?> alterBoard(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody BoardPostDTO dto){
		String token = tokenProvider.resolveToken(authorizationHeader);
		Authentication authentication =  tokenProvider.getAuthentication(token);
	    return boardService.alterBoard(dto,authentication);
	}
	
	@GetMapping("/get-detail")
	public ResponseEntity<?> getDetail(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("id") Long id){
		String token = tokenProvider.resolveToken(authorizationHeader);
        Authentication authentication =  tokenProvider.getAuthentication(token);
		return boardService.getDetail(id,authentication);
	}
	
	@GetMapping("/get-detail-only-alter")
	public ResponseEntity<?> findbyIdOnlyAlter(@RequestParam("id") Long id){
		return boardService.findbyIdOnlyAlter(id);
	}
	
	
	@GetMapping("/get-post-count")
	public ResponseEntity<?> countUserPostsAndComments(@RequestParam("id") Long id){
		return boardService.countUserPostsAndComments(id);
	}
	
	@PostMapping("/post-views")
	public ResponseEntity<?> postViews(@RequestBody String boardId){
		return boardService.postViews(boardId);
	}
	
	@PostMapping("/delete-board")
	public ResponseEntity<?> deleteBoard(@RequestBody String boardId){
		return boardService.deleteBoard(boardId);
	}
	
	@PostMapping("/post-recommend")
	public ResponseEntity<?> postRecommend(@RequestBody Map<String, Object> requestData){
		return boardService.postRecommend(requestData);
	}
	
	@PostMapping("/post-favorite")
	public ResponseEntity<?> postFavorite(@RequestBody Map<String, Object> requestData){
		return boardService.postFavorite(requestData);
	}
	
	@PostMapping("/post-comment")
	public ResponseEntity<?> postComment(@RequestBody Map<String, Object> requestData){
		return boardService.postComment(requestData);
	}
	
	@PostMapping("/delete-comment")
	public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentDTO dto){
		return boardService.deleteComment(dto);
	}
	
	@GetMapping("get-popular")
	public ResponseEntity<?> getPopular(
			@PageableDefault(size = 5, sort = "id") Pageable pageable){
		return boardService.getPopular(pageable);
	}
	

}
