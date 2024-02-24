package com.API.Board;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.API.Board.DTO.BoardReviewDTO;
import com.API.Board.Entity.Board;
import com.API.User.Entity.User;

public interface BoardRepository extends Repository<Board, Long> {

	    Board save(Board board);
	    Optional<Board> findById(Long id);
	    
	    @Query("SELECT new com.API.Board.DTO.BoardReviewDTO(b.id, b.title, u.nickname, b.views) FROM Board b JOIN b.author u")
	    Page<BoardReviewDTO> findAllBoardDTOs(Pageable pageable);
	}
