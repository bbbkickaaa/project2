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

import com.API.Board.DTO.BoardReviewDTO;

import org.springframework.data.domain.Sort;
@Controller
@RequestMapping("/api/board")
public class BoardController {
	
	@Autowired
	BoardService boardService;

	@GetMapping("/get")
	public ResponseEntity<Page<BoardReviewDTO>> findAll(@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
		
		return boardService.findAll(pageable);
	}
	
	@PostMapping("/post")
    public ResponseEntity<?> postBoard(@RequestBody Map<String, Object> requestData) {
       return boardService.postBoard(requestData);
    }
}
