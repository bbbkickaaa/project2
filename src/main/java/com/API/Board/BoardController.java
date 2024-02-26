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

import com.API.Board.DTO.BoardReviewDTO;
import com.API.Board.DTO.DeleteCommentDTO;

import org.springframework.data.domain.Sort;
@Controller
@RequestMapping("/api/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;

	@GetMapping("/getAll")
	public ResponseEntity<Page<BoardReviewDTO>> findAll(@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
		
		return boardService.findAll(pageable);
	}
	
	@PostMapping("/post")
    public ResponseEntity<?> postBoard(@RequestBody Map<String, Object> requestData) {
       return boardService.postBoard(requestData);
    }
	@GetMapping("/getDetail")
	public ResponseEntity<?> findbyId(@RequestParam("id") Long id){
		return boardService.findbyId(id);
	}
	
	@GetMapping("/getDetailOnlyAlter")
	public ResponseEntity<?> findbyIdOnlyAlter(@RequestParam("id") Long id){
		return boardService.findbyIdOnlyAlter(id);
	}
	
	
	@GetMapping("/getPostCount")
	public ResponseEntity<?> countUserPostsAndComments(@RequestParam("id") Long id){
		return boardService.countUserPostsAndComments(id);
	}
	
	@PostMapping("/postViews")
	public ResponseEntity<?> postViews(@RequestBody String boardId){
		return boardService.postViews(boardId);
	}
	
	@PostMapping("/deleteBoard")
	public ResponseEntity<?> deleteBoard(@RequestBody String boardId){
		return boardService.deleteBoard(boardId);
	}
	
	@PostMapping("/postRecommend")
	public ResponseEntity<?> postRecommend(@RequestBody String boardId){
		return boardService.postRecommend(boardId);
	}
	
	@PostMapping("/postComment")
	public ResponseEntity<?> postComment(@RequestBody Map<String, Object> requestData){
		return boardService.postComment(requestData);
	}
	
	@PostMapping("/deleteComment")
	public ResponseEntity<?> deleteComment(@RequestBody DeleteCommentDTO dto){
		return boardService.deleteComment(dto);
	}
}
