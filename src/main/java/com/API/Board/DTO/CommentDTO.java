package com.API.Board.DTO;

import java.util.List;

import com.API.Board.Entity.Board;
import com.API.Board.Entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
	
		Long commentId;
		Long userid;
		String content;
		String nickname;
		int level;
		String writeDate;
		String picture;
		
		public static CommentDTO fromCommentAndBoard(Comment comment,Board board) {
			CommentDTO commentDTO = new CommentDTO();
			commentDTO.setContent(comment.getContent());
			commentDTO.setNickname(comment.getAuthor().getNickname());
			commentDTO.setUserid(comment.getAuthor().getId());
			commentDTO.setWriteDate(comment.getWriteDate());
			commentDTO.setCommentId(comment.getId());
			commentDTO.setPicture(comment.getAuthor().getPicture());
			commentDTO.setLevel(board.getAuthor().getUserLevel().getLevel());
	        return commentDTO;
	    }
}
