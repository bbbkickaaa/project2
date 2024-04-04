package com.API.Board;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.API.Board.Entity.Comment;

public interface CommentRepository extends Repository<Comment, Long> {
	
	List<Comment> findByauthorId(Long id);

}
