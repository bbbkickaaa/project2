package com.API.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.API.Board.DTO.BoardReviewDTO;
import com.API.Board.Entity.Board;
import com.API.User.UserRepository;
import com.API.User.Entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardService {
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	UserRepository userRepository;

	public ResponseEntity<Page<BoardReviewDTO>> findAll(Pageable pageable){
		Page<BoardReviewDTO> paging = boardRepository.findAllBoardDTOs(pageable);
	    if (paging.isEmpty()) {
		return ResponseEntity.noContent().build();
	    }
	    else {
	    	return ResponseEntity.ok(paging);
	    }
	}
	    
	public ResponseEntity<String> postBoard(Map<String, Object> requestData){
		
		Board board = new Board();
		try {
			Integer idAsInteger = (Integer) requestData.get("id");
			Long id = Long.valueOf(idAsInteger);
			User author = userRepository.findById(id).orElseThrow(
		            () -> new EntityNotFoundException("User not found with ID: " + id));
			String title = (String) requestData.get("title");
			String content = (String) requestData.get("content");
			board.setTitle(title);
			board.setContent(content);
			board.setAuthor(author);
			boardRepository.save(board);
			return ResponseEntity.status(HttpStatus.OK).body("정상 등록 되었습니다.");
			
			
		}
		catch (ClassCastException e) {
			log.atError();
		    return ResponseEntity.badRequest().build(); 
		} catch (NullPointerException e) {
			log.atError();
		    return ResponseEntity.status(HttpStatus.CONFLICT).build();
		} catch(EntityNotFoundException e) {
			log.atError();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}    
	    
	    
}

