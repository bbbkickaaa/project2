package com.API.Board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.API.Board.DTO.BoardDTO;
import com.API.Board.DTO.BoardPostCountDTO;
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
        return ResponseEntity.ok(paging);
	}
	    
	public ResponseEntity<String> postBoard(Map<String, Object> requestData){
		
		Board board = new Board();
		try {
			String idAsString = (String) requestData.get("id");
			Long id = Long.valueOf(idAsString);
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
					    log.error("ClassCastException occurred", e);
			    return ResponseEntity.badRequest().build(); 
			} catch (NullPointerException e) {
			    log.error("NullPointerException occurred", e);
			    return ResponseEntity.status(HttpStatus.CONFLICT).build();
			} catch(EntityNotFoundException e) {
			    log.error("EntityNotFoundException occurred", e);
			    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
	}
	
	public ResponseEntity<?> findbyId(Long id){
		Optional<Board> wrapBoard = boardRepository.findById(id);
		Board board = wrapBoard.get();
		if(wrapBoard.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		BoardDTO boardDTO = new BoardDTO();
		boardDTO.setId(board.getId());
	    boardDTO.setTitle(board.getTitle());
	    boardDTO.setContent(board.getContent());
	    boardDTO.setUserIdx(board.getAuthor().getId());
	    boardDTO.setLikes(board.getLikes());
	    boardDTO.setViews(board.getViews());
	    boardDTO.setWriteDate(board.getWriteDate());
	    boardDTO.setComments(board.getComments());
	    boardDTO.setNickname(board.getAuthor().getNickname());
		
		return ResponseEntity.ok().body(boardDTO);
		
	}
	
	public ResponseEntity<?> countUserPostsAndComments(Long id){
		BoardPostCountDTO dto = new BoardPostCountDTO();
		Long postCount = boardRepository.countPostsByAuthorId(id);
		Long CommentCount = boardRepository.countCommentsByAuthorId(id);
		dto.setCommentCount(CommentCount);
		dto.setPostCount(postCount);
		return ResponseEntity.ok(dto);
	}
	
	public ResponseEntity<?> postViews(String boardIdS){
		Long boardId = Long.valueOf(boardIdS);
		Optional<Board> board = boardRepository.findById(boardId);
		if(board.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		board.get().setViews(board.get().getViews() + 1);
        boardRepository.save(board.get());
        
        return ResponseEntity.ok().body("조회수 증가되었습니다.");
		
		
	}
	
	public ResponseEntity<?> deleteBoard(String boardIdS){
		Long boardId = Long.valueOf(boardIdS);
		boardRepository.deleteById(boardId);
		
		return ResponseEntity.ok().body("삭제 되었습니다.");
		
	}
	    
	    
}

