package com.API.Board;

import java.util.Map; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.API.Board.DTO.BoardImageUploadDTO;
import com.API.Board.DTO.BoardPostDTO;
import com.API.Board.DTO.BoardReviewDTO;
import com.API.Board.DTO.DeleteCommentDTO;
import com.API.Board.DTO.QueryValidateDTO;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
@Controller
@RequestMapping("/api/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;

	@GetMapping("/get-all")
	public ResponseEntity<Page<BoardReviewDTO>> findAll(
		   @RequestParam(name = "category1", required = false) String category1,
		   @RequestParam(name = "category2", required = false) String category2,
		   @RequestParam(name = "category3", required = false) String category3,
		   @RequestParam(name = "option", required = false) String option, //검색 타입
		   @RequestParam(name = "content", required = false) String content, //검색 내용 
	    @PageableDefault(size = 15, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
	) {

		ResponseEntity<Page<BoardReviewDTO>> results = boardService.findAll(category3,category2,category1,option,content,pageable);
	    return results;
	}
	
	@PostMapping("/post")
    public ResponseEntity<?> postBoard(@RequestBody BoardPostDTO dto) {
		
       return boardService.postBoard(dto);
    }
	@GetMapping("/get-detail")
	public ResponseEntity<?> getDetail(@RequestParam("id") Long id){
		return boardService.getDetail(id);
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
	
	@PostMapping("/post-comment")
	public ResponseEntity<?> postComment(@RequestBody Map<String, Object> requestData){
		return boardService.postComment(requestData);
	}
	
	@PostMapping("/delete-comment")
	public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentDTO dto){
		return boardService.deleteComment(dto);
	}

}
