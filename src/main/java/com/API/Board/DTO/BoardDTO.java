package com.API.Board.DTO;

import java.util.List;

import com.API.Board.Entity.BoardCategory;
import com.API.Board.Entity.Comment;
import com.API.User.Entity.UserLevel;

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
    private List<CommentDTO> comments;
    private List<String> imageUrls;

}
