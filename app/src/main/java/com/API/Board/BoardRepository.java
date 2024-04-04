package com.API.Board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.API.Board.DTO.BoardReviewDTO;
import com.API.Board.Entity.Board;
import com.API.User.Entity.User;

public interface BoardRepository extends Repository<Board, Long> {

	    Board save(Board board);
	    
	    List<Board> findByauthor(User user,Sort sort);
	    
	    Optional<Board> findById(Long id);
	    
	    @Query("SELECT COUNT(b) FROM Board b WHERE b.author.id = :id")
	    Long countPostsByAuthorId(@Param("id") Long id);
	    
	    void deleteById(Long id);
	    
	    
	    @Query("SELECT new com.API.Board.DTO.BoardReviewDTO(b.id, b.title, u.id, u.nickname, b.views, b.likes, COUNT(c), b.category, COUNT(bi)) " +
	    	       "FROM Board b JOIN b.author u LEFT JOIN b.comments c LEFT JOIN b.boardImage bi " +
	    	       "WHERE b.id IN (:list) " +
	    	       "GROUP BY b.id, b.title, u.id, u.nickname, b.views, b.likes, b.category")
	    Page<BoardReviewDTO> findAllBylist(Pageable pageable, @Param("list") Set<Integer> list);


	    @Query("SELECT new com.API.Board.DTO.BoardReviewDTO(b.id, b.title, u.id, u.nickname, b.views, b.likes, COUNT(c), b.category, COUNT(bi)) " +
	    	       "FROM Board b JOIN b.author u LEFT JOIN b.comments c LEFT JOIN b.boardImage bi " +
	    	       "WHERE b.writeDate >= :weekAgo " +
	    	       "GROUP BY b.id, b.title, u.id, u.nickname, b.views, b.likes, b.category")
	    Page<BoardReviewDTO> findPopular(Pageable pageable, @Param("weekAgo") String weekAgo);

	}
