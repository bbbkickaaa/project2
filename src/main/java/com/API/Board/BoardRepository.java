package com.API.Board;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.API.Board.DTO.BoardDTO;
import com.API.Board.DTO.BoardPostCountDTO;
import com.API.Board.DTO.BoardReviewDTO;
import com.API.Board.Entity.Board;
import com.API.User.Entity.User;

public interface BoardRepository extends Repository<Board, Long> {

	    Board save(Board board);
	    
	    List<Board> findByauthorId(User user);
	    
	    Optional<Board> findById(Long id);
	    
	    @Query("SELECT COUNT(b) FROM Board b WHERE b.author.id = :id")
	    Long countPostsByAuthorId(@Param("id") Long id);
	    
	    @Query("SELECT new com.API.Board.DTO.BoardReviewDTO(b.id, b.title, u.id, u.nickname, b.views, b.likes, COUNT(c), b.category) FROM Board b JOIN b.author u LEFT JOIN b.comments c GROUP BY b.id, b.title, u.id, u.nickname, b.views")
	    Page<BoardReviewDTO> findAllBoardDTOs(Pageable pageable);
	    
	    void deleteById(Long id);

	}
