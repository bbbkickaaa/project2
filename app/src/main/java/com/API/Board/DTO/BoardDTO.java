package com.API.Board.DTO;

import java.util.List;
import java.util.Set;

import com.API.Board.Entity.Board;
import com.API.Board.Entity.BoardCategory;
import com.API.User.DTO.UserDTO;
import com.API.User.Entity.User;
import com.API.User.Entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long id;
    private String title;
    private String content;
    private int level;
    private int likes;
    private int views;
    private Long userIdx;
    private String nickname;
    private String writeDate;
    private String alterDate;
    private String picture;
    private BoardCategory category;
    private UserRole role;
    private Set<Integer> likesUser;
    private List<CommentDTO> comments;
    private List<String> imageUrls;
    private boolean isFavorite;
    
    public static BoardDTO fromBoard(Board board) {
    	BoardDTO boardDTO = new BoardDTO();
    	boardDTO.setId(board.getId());
	    boardDTO.setTitle(board.getTitle());
	    boardDTO.setContent(board.getContent());
	    boardDTO.setUserIdx(board.getAuthor().getId());
	    boardDTO.setLikes(board.getLikes());
	    boardDTO.setViews(board.getViews());
	    boardDTO.setFavorite(false);
	    boardDTO.setCategory(board.getCategory());
	    boardDTO.setWriteDate(board.getWriteDate());
	    boardDTO.setAlterDate(board.getAlterDate());
	    boardDTO.setPicture(board.getAuthor().getPicture());
	    boardDTO.setLevel(board.getAuthor().getUserLevel().getLevel());
	    boardDTO.setLikesUser(board.getLikesUsers());
        return boardDTO;
    }

}
