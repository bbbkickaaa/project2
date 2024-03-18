package com.API.Board;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import com.API.Board.Entity.Board;
import com.API.User.Entity.User;

public interface BoardRepository extends Repository<Board, Long> {

	    Board save(Board board);
	    
	    List<Board> findByauthor(User user,Sort sort);
	    
	    Optional<Board> findById(Long id);
	    
	    @Query("SELECT COUNT(b) FROM Board b WHERE b.author.id = :id")
	    Long countPostsByAuthorId(@Param("id") Long id);
	    
	    void deleteById(Long id);

	}
